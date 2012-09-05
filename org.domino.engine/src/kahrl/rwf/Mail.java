package kahrl.rwf;

import lotus.domino.*;
import java.util.*;
import java.lang.reflect.*;

/** Description: 
 *  This class is the single point where all mail is sent from the engine for workflow or subscriptions
 *
 * @author:  Phillip Kahrl
*/

public class Mail {

/**
The following is a view name for a view that will contain all the documents for which doc links will be sent.
This can be changed if you want to use a particular view, for example, in order to use a view with a form formula.
The default is '0'.
**/
private String ALLDOCUMENTS_VIEW = "0";

private Session ses;
private Database db;
private Database mailDb;
private DBConfiguration c;
////
private String mailFrom = ""; 
private String mailOriginator="";
private boolean sendMail=false;
private boolean useMailBox =false;
private String serverURL="";

/** 
	* Constructor for the Mail object
    	* @param  theDb  the current database
**/
public Mail(Database theDb){
	try{
		db = theDb;
		serverURL = "http://" + theDb.getServer() ;
		try{
				c = new DBConfiguration(db);
				useMailBox = c.useMailBox();
				sendMail = c.sendMail();
				mailFrom = c.getMailFrom();
				mailOriginator = c.getMailOriginator();
				serverURL = c.getServerURL();
		}catch(ConfigDocNotFoundException e){
			
			e.printStackTrace();}
		ses = db.getParent();
		
		if((useMailBox) && c.sendMail()){
			mailDb = getMailDb();
			}
		else{
			mailDb = db;
			}
			
		}catch(NotesException e){e.printStackTrace();}
	}

	/** 
	* Constructor for the Mail object, use this constructor to send mail by creating a document in mail.box
	* Using this constructor would be the same as using using Mail(theDb, true)
    	* @param theDb  the current database
    	* @param contextDoc the context document.
    	**/

public Mail(Database theDb, Document contextDoc){
	try{
		db = theDb;
		serverURL = "http://" + theDb.getServer() ;
		try{
				c = new DBConfiguration(db, contextDoc);
				useMailBox = c.useMailBox();
				sendMail = c.sendMail();
				mailFrom = c.getMailFrom();
				mailOriginator = c.getMailOriginator();
				serverURL = c.getServerURL();
		}catch(ConfigDocNotFoundException e){e.printStackTrace();}
		ses = db.getParent();
		if((useMailBox) && c.sendMail()){
			mailDb = getMailDb();
			}
		else{
			mailDb = db;
			}
		
		}catch(NotesException e){e.printStackTrace();}
	}
/** set the mail from field
@param String the value for from.
**/
void setMailFrom(String from){
	mailFrom = from;
	}

/** 
	* Returns the Database from which mail will be sent.  This will be either the Database passed to the constructor, or mail.box
	* depending on the constructor.
	* @return Database
    	* @param  theDb  the current database
    	* @param umb a boolean umb determines how mail is sent. umb=false will send mail from this database using Document.send() umb=true will send from mail.box using Document.save()
	 * @see Mail(Database, boolean)
	 * @see Mail(Database)	
    **/
public Database getMailDb(){
		Database tmp = null;
		try{
			tmp = ses.getDatabase(db.getServer(), "mail.box");
		}catch(NotesException e){e.printStackTrace();}
		return tmp;
		}
/** 
	Either sends or saves a mail memo, depending on the Configuration document
	@param recipients The mail memo To recipients
	@param ccRecipients The mail memo CopyTo recipients
	@param subject The subject text of the mail memo
    	@param body RichTextItem comprising the body of the mail memo
    	@param currentDoc The context document from which the mail memo was generated.
**/

public void sendMail(Vector recipients, Vector ccRecipients, String subject, RichTextItem body, Document currentDoc){
	Vector bccRecipients = new Vector(1);
	bccRecipients.addElement("");
	sendMail(recipients, ccRecipients, bccRecipients, subject, body, currentDoc);
	}


/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param ccRecipients The mail memo CopyTo recipients
	* @param bccRecipient The mail memo BlindCopyTo recipients
	* @param subject  The subject text of the mail memo
    	* @param body  RichTextItem comprising the body of the mail memo
    	* @param currentDoc  The context document from which the mail memo was generated.
**/

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, RichTextItem body, Document currentDoc){
	sendMail(recipients, ccRecipients, bccRecipients, subject, body, currentDoc, false);
}

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, 
		RichTextItem body, Document currentDoc, boolean useMIME){
	sendMail(recipients, ccRecipients, bccRecipients, subject, body, currentDoc, useMIME, "Normal");
}

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, 
		RichTextItem body, Document currentDoc, boolean useMIME, String importance){
		try{
			Document memo = mailDb.createDocument();
			memo.replaceItemValue("Form", "Memo");
			memo.replaceItemValue("SendTo", recipients);
			memo.replaceItemValue("CopyTo", ccRecipients);
			memo.replaceItemValue("BlindCopyTo", bccRecipients);
			memo.replaceItemValue("Recipients", recipients);
			memo.replaceItemValue("From",mailFrom);
			memo.replaceItemValue("Principal", mailFrom);
			memo.replaceItemValue("Sender", mailFrom);
			memo.replaceItemValue("SMTPOriginator", mailOriginator);
			memo.replaceItemValue("Importance", importance);
			memo.replaceItemValue("Subject", subject);
			memo.removeItem("Body");
			memo.copyItem(body,"Body");
			if(useMIME){
				createMIMEBody(currentDoc, body.getText());
			}
			
			
			// Send the mail
			if(!(useMailBox) && sendMail){
				memo.send(recipients);
				}
				else{
					memo.save();
					}
		}catch(NotesException e){e.printStackTrace(); }	
	}

/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param ccRecipients The mail memo CopyTo recipients
	* @param bccRecipient The mail memo BlindCopyTo recipients
	* @param subject  The subject text of the mail memo
    	* @param body  String comprising the text of the body of the mail memo
    	* @param currentDoc  The context document from which the mail memo was generated.
    	* @param attachLink  Boolean specifies whether to attach a URL link to the context document at the bottom of the body of the message.
**/

/**
 
  **/
public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, String body, Document currentDoc, boolean attachLink, boolean attachDocLink){
	sendMail(recipients, ccRecipients, bccRecipients, subject, body, currentDoc, attachLink, attachDocLink, false);
}

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, 
					String subject, String body, Document currentDoc, boolean attachLink, 
					boolean attachDocLink, 	boolean useMIME){
		sendMail(recipients, ccRecipients, bccRecipients, subject, body, currentDoc, attachLink,
				attachDocLink,  useMIME, "Normal");
}

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, 
					String subject, String body, Document currentDoc, boolean attachLink, 
					boolean attachDocLink, boolean useMIME,	String importance){
	if (attachLink){
		body = body + getDocumentURL(currentDoc);
	}
	if(attachDocLink){
		try{
			RichTextItem temp = currentDoc.createRichTextItem("rwfTempBody");
			temp.appendText(body);
			temp.addNewLine();
			temp.appendDocLink(currentDoc, Approver.getDocLinkHint(currentDoc));
			sendMail(recipients, ccRecipients, bccRecipients, subject, temp, currentDoc, useMIME, importance);
			currentDoc.removeItem("rwfTempBody");
			}catch(NotesException ex){ex.printStackTrace(); }
		}else{
			sendMail(recipients, ccRecipients, bccRecipients, subject, body, useMIME, importance);
		}
}

/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param ccRecipients The mail memo CopyTo recipients
	* @param bccRecipient The mail memo BlindCopyTo recipients
	* @param subject  The subject text of the mail memo
    	* @param body  String comprising the text of the body of the mail memo
    	* @param currentDoc  The context document from which the mail memo was generated.
**/


public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, String body, Document currentDoc){
	body = body + getDocumentURL(currentDoc);
	sendMail(recipients, ccRecipients, bccRecipients, subject, body);
}

/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param subject  The subject text of the mail memo
    	* @param body  String comprising the text of the body of the mail memo
    	* @param currentDoc  The context document from which the mail memo was generated.
**/

public void sendMail(Vector recipients, String subject, String body, Document currentDoc){
	Approver approver = new Approver(currentDoc);
	if (approver.fromWeb() ){
		body = body + getDocumentURL(currentDoc);
		sendMail(recipients, subject, body);
	}else{
		try{
			RichTextItem temp = currentDoc.createRichTextItem("rwfTempBody");
			temp.appendText(body);
			temp.addNewLine();
			temp.appendDocLink(currentDoc, Approver.getDocLinkHint(currentDoc));
			sendMail(recipients, new Vector(0), new Vector(0), subject, temp, currentDoc);
			currentDoc.removeItem("rwfTempBody");
			}catch(NotesException ex){ex.printStackTrace(); }
		}
}
/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param subject  The subject text of the mail memo
    	* @param body  String comprising the text of the body of the mail memo
 **/

public void sendMail(Vector recipients,  String subject, String body){
	Vector ccRecipients = new Vector(1);
	ccRecipients.addElement("");
	sendMail(recipients, ccRecipients, subject, body);
	}

/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param ccRecipients The mail memo CopyTo recipients
	* @param subject  The subject text of the mail memo
    	* @param body  String comprising the text of the body of the mail memo
 **/

public void sendMail(Vector recipients, Vector ccRecipients,  String subject, String body){
	Vector bccRecipients = new Vector(1);
	bccRecipients.addElement("");
	sendMail(recipients, ccRecipients, bccRecipients, subject, body);
	}

/** 
	* Either sends or saves a mail memo, depending on the Configuration document
	* @param recipients  The mail memo To recipients
	* @param ccRecipients The mail memo CopyTo recipients
	* @param bccRecipient The mail memo BlindCopyTo recipients
	* @param subject  The subject text of the mail memo
    * @param body  String comprising the text of the body of the mail memo
**/

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, String body){
	sendMail(recipients, ccRecipients, bccRecipients, subject, body, false);
}

/** 
* Either sends or saves a mail memo, depending on the Configuration document
* @param recipients  The mail memo To recipients
* @param ccRecipients The mail memo CopyTo recipients
* @param bccRecipient The mail memo BlindCopyTo recipients
* @param subject  The subject text of the mail memo
* @param body  String comprising the text of the body of the mail memo
* @param useMIME specifies whether the body of the mail document will be MIME html.
**/
public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, 
					String subject, String body, boolean useMIME){
	sendMail(recipients, ccRecipients, bccRecipients, subject, body, useMIME, "Normal");
}

public void sendMail(Vector recipients, Vector ccRecipients, Vector bccRecipients, String subject, 
					String body, boolean useMIME, String importance){
		try{
			Document memo = mailDb.createDocument();
			memo.replaceItemValue("Form", "Memo");
			memo.replaceItemValue("SendTo", recipients);
			memo.replaceItemValue("CopyTo", ccRecipients);
			memo.replaceItemValue("BlindCopyTo", bccRecipients);
			memo.replaceItemValue("Recipients", recipients);
			memo.replaceItemValue("Importance", importance);
			memo.replaceItemValue("From",mailFrom);
			memo.replaceItemValue("Principal", mailFrom);
			memo.replaceItemValue("Sender", mailFrom);
			memo.replaceItemValue("SMTPOriginator", mailOriginator);

			memo.replaceItemValue("Subject", subject);
			if(useMIME){
				memo = createMIMEBody(memo, body);
			}else{
				memo.removeItem("Body");
				memo.replaceItemValue("Body", body);
			}
		// Send the mail
			if(!(useMailBox) && sendMail){
				memo.send(recipients);
				}
				else{
					memo.save();
					}
		}catch(NotesException e){e.printStackTrace(); }
	}

/** 
	* Given a document, returns a string representing the URL of that document on the server.
	*  Uses the base server url found in the Configuration document
     * @return A string representing the URL of a document on the Domino server.
    	* @param d  The document to generate the URL for.
    	* @see Configuration
**/

public  String getDocumentURL(Document d){
		String tmp="";
		try{
			String path = d.getParentDatabase().getFilePath();
			path = path.replace('\\','/');
			tmp = " " + serverURL + "/" + path+ "/" +ALLDOCUMENTS_VIEW + "/"  + d.getUniversalID() + "?OpenDocument";
			}catch(Exception e){e.printStackTrace();}
			return tmp;
		}
/** 
	* Substitutes values in fields from the context document.
	*  Text within triangle brackets <> is assumed to be a fieldname and will be replaced with the value of the field from the context document
	*  A fieldname with another field can be denoted by square brackets [].  Triangle brackets within a field value will be encoded and passed through.
	* @return A new string with the field values from the context document subsituted.
	* @param s The string to perform the field subsitutions on.
    	* @param d  The context document
    	* @see Configuration
**/
public static String subFieldValues(String s, Document d){
	return subFieldValues(s, d, false);
}

public static String subFieldValues( String s, Document d, boolean useMIME){
	String start = "<";
	String end = ">";
	
	if(useMIME){
		start = "<%field:";
		end = "%>";
	}
	
	String tmp = s;
		while((s.indexOf(start) != -1) && (s.indexOf(end) != -1)){
			int startField = s.indexOf(start);
			int endField = s.indexOf(end);
			String fieldName = s.substring(startField + start.length(), endField);
			try{
				String fieldValue = null;
				if(d.hasItem(fieldName)){
					fieldValue = d.getFirstItem(fieldName).getText();
				}
				// escape greater and less than characters
				if(fieldValue==null){fieldValue = "";}
				fieldValue = fieldValue.replace('<','^');
 				fieldValue = fieldValue.replace('>','~');
				// if the field contains any values enclosed by square brackets , those are also subsituted
				while ((fieldValue.indexOf("[") != -1) && (fieldValue.indexOf("]") != -1)){
						int startField1 = fieldValue.indexOf("[");
						int endField1 = fieldValue.indexOf("]");
						String fieldName1 = fieldValue.substring(startField1+1, endField1);
						try{
							String fieldValue1 = d.getItemValueString(fieldName1);
							if(fieldValue1 == null){fieldValue1="";}
							fieldValue = fieldValue.substring(0, startField1) + fieldValue1 + fieldValue.substring(endField1 + 1, fieldValue.length());
						}catch(NotesException e){e.printStackTrace(); }
					}
				//
				s = s.substring(0, startField) + fieldValue + s.substring(endField + end.length(), s.length());
				
		}catch(NotesException e){return tmp;}
		}
		// unescape the greater and less than characters
		s = s.replace('^','<');
 		s = s.replace('~','>');
 		if(useMIME){
 			s = subMethodValues(s);
 		}
		return s;
		}
/**
 * Replaces tag values by invoking the named static method.
 * tag is in the format <%staticmethod:className:methodName:arg%>
 * The static method can have a single String arg.
 * @return
 */
public static String subMethodValues(String s){
	String start = "<%staticmethod:";
	String end = "%>";
	
	while((s.indexOf(start) != -1) && (s.indexOf(end) != -1)){
		int startTag = s.indexOf(start);
		int endTag = s.indexOf(end);
		String tag = s.substring(startTag, endTag);
		int pointer = start.length();
		String className = tag.substring(pointer, tag.indexOf(":", pointer));
		pointer += className.length()+1;
		String methodName = tag.substring(pointer, tag.indexOf(":", pointer));
		pointer += methodName.length()+1;
		String argName = tag.substring(pointer, tag.length());
		argName = (argName.equals("null"))?null:argName;
		String val = invokeStaticMethod(className, methodName, argName);
		if(val == null){
			val = "";
		}
		s = s.substring(0, startTag) + val + s.substring(endTag + end.length(), s.length());
	}
	return s;
}

/**
 * Adds a MIME html body to a passed document from the passed String
 */
Document createMIMEBody(Document theDoc, String theString){
	try{		
	    ses.setConvertMIME(false); 
		Stream stream = ses.createStream();
		stream.writeText(theString);
	    MIMEEntity body = theDoc.createMIMEEntity();
	    body.setContentFromText(stream, "text/html;charset=UTF-8", MIMEEntity.ENC_NONE);
	   
	}catch(NotesException e){
		e.printStackTrace();
		return theDoc;
	}
	return theDoc;
}
/**
 * Used invoke a static method by name, using Reflection
 * @param className
 * @param methodName
 * @param arg
 * @return toString() of the object returned by the named method, null if the method
 * or class does not exist.
 */
public static String invokeStaticMethod(String className, String methodName, String arg){
	String tmp = null;
	String[] margs = {arg};
	if(arg==null){
		margs = null;
	}
	
	try{
		Class cls = Class.forName(className);
		Method m[] = cls.getDeclaredMethods();
		for(int i=0;i<m.length;i++){
			if(methodName.equals(m[i].getName())){
				Object retObject = m[i].invoke(null, margs);
				return retObject.toString();
			}
		}
   	// do nothing if the class or method can't be found.
	}catch(Exception e){e.printStackTrace();}
	return tmp;
}
}

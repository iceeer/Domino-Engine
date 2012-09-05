package kahrl.rwf;
import lotus.domino.*;
/**
Parent class for all exceptions in the rules engine
**/
public class RWFException extends Exception {
// Field mappings for documents generated for the exception log.
static final long serialVersionUID = -5575428265885984355L;
public static final String EXCEPTION_TYPE_FIELD = "rwfExceptionType";
public static final String EXCEPTION_MSG_FIELD = "rwfExceptionMsg";
public static final String EXCEPTION_DOCLINK_FIELD = "rwfDocLinks";
public static final String EXCEPTION_FORM = "rwfException";
//// The below are used for displaying error pages to web users.
public static final String EXCEPTION_VIEW = "(rwfExceptionMsgWebPages)";
String EXCEPTION_KEY = "RWFException";

/**
RWFException constructor
@param - msg, String the error message to be used.
@param Database d the current database
@param Document doc the context document. 
**/
public RWFException(String msg, Database d, Document doc){
	super(msg);
	try{
		Document e = d.createDocument();
		e.replaceItemValue("Form",EXCEPTION_FORM);
		e.replaceItemValue(EXCEPTION_TYPE_FIELD,"Workflow Exception");
		e.replaceItemValue(EXCEPTION_MSG_FIELD,msg);
		RichTextItem rt = e.createRichTextItem(EXCEPTION_DOCLINK_FIELD);
		rt.appendDocLink(doc);
		e.save();
	}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
}
/**
RWFException constructor
@param - msg, String the error message to be used.
@param Database d the current database
**/
public RWFException(String msg, Database d){
	super(msg);
	try{
		Document e = d.createDocument();
		e.replaceItemValue("Form",EXCEPTION_FORM);
		e.replaceItemValue(EXCEPTION_TYPE_FIELD,"Workflow Exception");
		e.replaceItemValue(EXCEPTION_MSG_FIELD,msg);
		e.save();
	}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
}
/**
RWFException constructor
@param Database d the current database
**/
public RWFException(Database d){
	super();
	try{
	Document e = d.createDocument();
	e.replaceItemValue("Form",EXCEPTION_FORM);
	e.replaceItemValue(EXCEPTION_TYPE_FIELD,"Workflow Exception");
	e.save();
	}catch(NotesException e){e.printStackTrace(); System.out.println(e.text);}
}
/**
RWFException constructor
@param - msg, String the error message to be used.
@param Document doc the context document. 
**/
public RWFException(String msg, Document doc){
	Database db = null;
	try{
		db = doc.getParentDatabase();
		}catch(NotesException e){e.printStackTrace();}
		new RWFException(msg,db,doc);
	}
/**
RWFException empty default constructor
**/	
public RWFException(){
	super();
	}
/**
RWFException constructor
@param - msg, String the error message to be used.
**/	
public RWFException(String msg){
	super("RWF Exception: " + msg);
	System.out.println("RWF Exception: " + msg);
	}
/** This will display an error page to the user.
* The error page must be in a view in the current Database with the name matching the value of EXCEPTION_VIEW.
*  The key of the document in the view is the same as the class of the exception that is calling this method.
@param - contextDoc Document , the context document.
**/
public void showExceptionWebPage(Document contextDoc){
	try{
		Database db = contextDoc.getParentDatabase();
		View view = db.getView(EXCEPTION_VIEW);
		if(view != null){
			Document errorPage = view.getDocumentByKey(EXCEPTION_KEY);
			if(errorPage != null){
				contextDoc.replaceItemValue("$$Return", "[/" + db.getFilePath() + "/" + EXCEPTION_VIEW + "/" + EXCEPTION_KEY + "?OpenDocument]");
				}
		
		}
		}catch(NotesException e){ ; /// Do nothing if the database doesn't have an exception page  for this exception 
									}
	}

}
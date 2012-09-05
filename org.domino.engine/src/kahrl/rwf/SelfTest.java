package kahrl.rwf;

import lotus.domino.*;
import java.io.*;
import java.util.*;

public class SelfTest {
static Session session;
static Database db;
static AgentContext agentContext;
static DBConfiguration config;
static View stateView;
static View delegateView;

	public static boolean run(Session theSession){
		try{
			session = theSession;
			agentContext = session.getAgentContext();
			db = agentContext.getCurrentDatabase();
		}catch(NotesException e){}
		PrintStream ps = System.out;
		ps.println("Running RealWorkFlow system check:" + '\n');
		ps.println("RealWorkFlow version: " + WorkflowDocument.getVersionName() + '\n');
		ps.println("JVM System Information:");
		ps.println("java.version: " + System.getProperty("java.version"));
		try{
			ps.println("Notes version=" + session.getNotesVersion());
			ps.println("address books=" + session.getAddressBooks());
			ps.println('\n' + "RealWorkFlow configuration:" + '\n');
			try {
				config = new DBConfiguration(db);
			} catch (Exception e) {
				ps.println("ERROR:  The configuration view or document could not be found:");
				ps.println("Ensure that a document created with the form: 'rwfMailConfig|mailConfig' exists in the view: " + DBConfiguration.DB_CONFIG_VIEW_NAME);;
				return false;
			}
			ps.println("Mail Configuration:");
			ps.println("Mail is now set to be " + ((config.sendMail())?"Sent":"Saved").toString());
			ps.println("The default base server URL is:" + config.getServerURL());
			ps.println("The default replyTo address is: " + config.getMailFrom());
			ps.println("The default SMTP originator address is: " + config.getMailOriginator());
			ps.println("Mail will be sent from the database:" + (config.useMailBox()?"mail.box":db.toString()).toString());
							    
			ps.println('\n' + "Workflow State Configuration:" + '\n');
						
			try{
				stateView = db.getView(State.STATE_VIEW_NAME);
				if(stateView == null){
					stateView = db.getView(State.STATE_VIEW_NAME_DEP);
					ps.println("Warning:  '" + State.STATE_VIEW_NAME_DEP + "' is deprecated use view '" + State.STATE_VIEW_NAME + "' instead.");
				}
			}catch(NotesException e){}
			if(stateView == null){
				ps.println("Error: database does not contain the state configuration view name:'" + State.STATE_VIEW_NAME + "'");
			    return false;
			}
			
			ps.println('\n' + "Available Workflow States:" + '\n');
			
			Document doc = stateView.getFirstDocument();
			while(doc != null){
				if(!doc.isResponse()){
					ps.println(doc.getItemValueString(State.STATE_NAME_FIELD));
				}
				doc = stateView.getNextDocument(doc);
			}
			ps.println('\n' + "Approval Delegation:" + '\n');
			try{
				delegateView = db.getView(Approver.DELEGATE_VIEW_NAME);
				if(delegateView == null){
					delegateView = db.getView(Approver.DELEGATE_VIEW_NAME_DEP);
					throw new RWFException("Warning: view name' " + Approver.DELEGATE_VIEW_NAME_DEP + "' is deprecated, use view name '" + Approver.DELEGATE_VIEW_NAME + "' instead." );
				}
			}catch(Exception e){e.printStackTrace();}
			if(delegateView == null){
				ps.println("No approval delegation is configured in this database.");
			}
			else{
				boolean foundDelegates = false;
				Vector entries = new Vector(0);
				ViewEntryCollection vec = delegateView.getAllEntries();
				ViewEntry ve= vec.getFirstEntry();
				while(ve != null){
					entries.addElement(ve);
					ve = vec.getNextEntry(ve);
				}
				ve = null;
				System.gc();
				for(Iterator i = entries.iterator(); i.hasNext(); ){
					ve = (ViewEntry)i.next();
					String person = "";
					if((ve.getColumnValues() != null) && (ve.getColumnValues().size() != 0)){
					Object obj = ve.getColumnValues().elementAt(0);
							if((obj instanceof Vector)){
							Vector vector = (Vector)obj;
							if((vector != null) && (vector.size() > 0)){
								person = (String)vector.elementAt(0);
							}
						}else{
							person = (String)obj;
						}
						Vector delegates = Approver.getDelegates(person, false, ve.getDocument(), session, db);
						if(delegates.size()>0){
							ps.println(person + " currently has delegates: " + delegates);
							foundDelegates = true;
						}
						delegates = null;
						person = null;
						obj = null;
						ve = null;
						System.gc();
					}
				}
				if(!foundDelegates){
					ps.println("No persons in this application currently have delegates.");
				}
			}
			
		}catch(NotesException e){e.printStackTrace();}
		ps.println('\n' + "RealWorkFlow system check complete.");	
	return true;	
	
	}
	
}

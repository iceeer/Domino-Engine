/**
 * 
 */
package org.domino.engine.workflow;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.doc.DocHelper;
import org.domino.engine.foundation.DataStore;
import org.domino.engine.foundation.DominoDataStore;
import org.domino.engine.utility.DominoObjectHelper;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.log.ScopeMessage;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * 工作项
 * 
 * @author iceeer
 * 
 */
public class Job extends DominoDataStore {

	private static String FORM_NAME = "fmNavItem";

	private String id;

	private boolean deleted;

	private String sourceID;

	private String jobName;

	private String jobType;

	private Vector<String> vWorker;

	private Vector<String> vHistoryWorker;

	private Vector<String> vJobOwner;

	private Date dtArrivedTime;

	private Vector<Date> vUpdatedTime;

	/**
	 * 
	 */
	public Job() {
		super();
		deleted = false;
		dtArrivedTime = new Date();
	}

	public boolean create(Object o) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object get(String id) {
		return null;
	}

	public boolean update(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean redirectToXPage() throws NotesException {
		Session session = Engine.getSession();
		DominoDocument doc = getDominoDocument();
		String strSourceID = doc.getItemValueString("SourceID");
		Document docMain = DocHelper.getDocumentByID(
				Application.getApplicationDB(session), strSourceID);

		if (Helper.ValidateNotNull(docMain)) {
			String strForm = docMain.getItemValueString("Form");

			XSPHelper.getXSPContext().redirectToPage(
					XSPHelper.getPageXspUrl(Engine.getProperty(strForm
							+ ".page.name"))
							+ "?databaseName="
							+ session.getServerName()
							+ "!!"
							+ Application.getApplicationDBPath(session)
							+ "&documentId="
							+ docMain.getUniversalID()
							+ "&action=editDocument");
			return true;
		} else {
			ScopeMessage.printError("",
					Engine.getProperty("lang.error.doc.not.exist.text"),
					DominoObjectHelper.getCurrentDBURL(Engine.getSession())
							+ "/" + Engine.getProperty("index.page.name"));
			return false;
		}
	}

	/**
	 * 获得文档数据源对象
	 * 
	 * @param strDataName
	 * @return
	 */
	private DominoDocument getDominoDocument() {
		return XSPHelper.getDominoDocument("dominoDocumentJobInfo");
	}

	/**
	 * 发送待办
	 * 
	 * @param session
	 * @param v
	 * @param strJobType
	 * @param strSubject
	 * @param strID
	 * @param strJobOwner
	 * @return
	 */
	private static boolean sendTODOTask(Session session, Vector v,
			String strJobType, String strSubject, String strID,
			String strJobOwner) {
		try {
			Database db = Application.getApplicationDB(session);
			Document docTODO;

			for (Iterator i = v.iterator(); i.hasNext();) {
				String strWorker = (String) i.next();
				docTODO = db.createDocument();
				DocHelper.buildCommonField(session, docTODO);
				docTODO.replaceItemValue("Form", "fmJobInfo");
				docTODO.replaceItemValue("JobType", strJobType);
				docTODO.replaceItemValue("JobName", strSubject);

				docTODO.replaceItemValue("SourceID", strID);
				docTODO.replaceItemValue("Worker", strWorker);
				docTODO.replaceItemValue("JobOwner", strJobOwner);
				docTODO.save();
			}

			return true;
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 待办转为已办
	 * 
	 * @return
	 */
	private boolean transferTODOToDone(Session session, String strUserName,
			String strID) {
		try {
			Database db = Application.getApplicationDB(session);
			View vw = db.getView("vwJobListByWorkerAndID");
			Vector v = new Vector();
			v.add(strUserName);
			v.add(strID);
			DocumentCollection dc = vw.getAllDocumentsByKey(v, true);
			dc.stampAll("HistoryWorker", strUserName);
			dc.stampAll("Worker", "");

			return true;
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 待办转为已办
	 * 
	 * @return
	 */
	private boolean transferTODOToDone(Session session, String strID) {
		try {
			String strUserName = DominoObjectHelper.getCurrentUserName(session);
			return transferTODOToDone(session, strUserName, strID);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 待办转为已办
	 * 
	 * @return
	 */
	private boolean removeTODOTask(Session session, String strID) {
		try {

			Database db = Application.getApplicationDB(session);
			View vw = db.getView("vwJobListBySourceID");
			DocumentCollection dc = vw.getAllDocumentsByKey(strID, true);
			dc.removeAll(true);

			return true;
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
	}
}

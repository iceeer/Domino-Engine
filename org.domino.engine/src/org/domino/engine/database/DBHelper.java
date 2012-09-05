/**
 * 
 */
package org.domino.engine.database;

import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DbDirectory;
import lotus.domino.Directory;
import lotus.domino.DirectoryNavigator;
import lotus.domino.Document;
import lotus.domino.DxlImporter;
import lotus.domino.Form;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.Stream;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.EngineConstants;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;

/**
 * @author iceeer
 * 
 */
public class DBHelper {

	/**
	 * 获得当前服务器下数据库
	 * 
	 * @param strDBPath
	 * @return
	 */
	public static Database getDatabase(String strDBPath) {
		try {
			Session session = Engine.getSessionAsSignerWithFullAccess();
			return DominoObjectHelper.getDatabase(session, session
					.getServerName(), strDBPath);
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return null;
	}

	/**
	 * 判断当前服务器下数据库是否存在
	 * 
	 * @param strDBPath
	 * @return
	 */
	public static boolean hasDatabase(String strDBPath) {
		return (DominoObjectHelper.isValid(getDatabase(strDBPath)));
	}

	public static boolean hasView() {
		return true;
	}

	public static boolean hasForm() {
		return true;
	}

	/**
	 * 创建数据库（不打开数据库）
	 * 
	 * @param strDBFileName 数据库文件路径
	 * @return
	 */
	public static Database createDatabase(String strDBFileName) {
		return createDatabase(strDBFileName, false);
	}

	/**
	 * 创建数据库
	 * @param strDBFileName 数据库文件路径
	 * @param open 是否打开
	 * @return
	 */
	public static Database createDatabase(String strDBFileName, boolean open) {
		Session session = Engine.getSessionAsSignerWithFullAccess();
		try {
			DbDirectory dir = session.getDbDirectory(null);
			Database db = dir.createDatabase(strDBFileName, open);
			System.out.println(db.getFileName());
			return db;
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return null;
	}

	/**
	 * 创建视图
	 * 
	 * @param db
	 * @param viewName
	 * @return
	 * @throws NotesException
	 */
	public static boolean createView(Database db, String viewName)
			throws NotesException {
		Session session = Engine.getSessionAsSignerWithFullAccess();

		// Get DXL file
		String filename = "c:\\dxl\\exporteddb.dxl";
		Stream stream = session.createStream();
		if (stream.open(filename) & (stream.getBytes() > 0)) {

			// Import DXL from file to new database
			DxlImporter importer = session.createDxlImporter();
			// importer.setReplaceDbProperties(true);
			// importer.setReplicaRequiredForReplaceOrUpdate(false);
			// importer
			// .setAclImportOption(DxlImporter.DXLIMPORTOPTION_REPLACE_ELSE_IGNORE);
			importer.setDesignImportOption(DxlImporter.DXLIMPORTOPTION_CREATE);
			importer.importDxl(stream, db);
		}
		return true;
	}

	public static boolean createForm() {
		return true;
	}

	/**
	 * 删除指定路径的数据库
	 * 
	 * @param strDBFileName
	 * @return
	 */
	public static boolean deleteDatabase(String strDBFileName) {
		Session session = Engine.getSessionAsSignerWithFullAccess();
		Database db;
		try {
			db = session.getDatabase(session.getServerName(), strDBFileName);
			if ((db != null) && (db.isOpen())) {
				db.removeFTIndex();
				db.remove();
				db.recycle();
				return true;
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	public static boolean deleteView() {
		return true;
	}

	public static boolean deleteForm() {
		return true;
	}

	/**
	 * 复制数据库
	 * 
	 * @param dbSource
	 * @param strDestinationServerName
	 * @param strDestinationDBPath
	 * @return
	 */
	public static Database copyDatabase(Database dbSource,
			String strDestinationServerName, String strDestinationDBPath) {
		try {
			if (DominoObjectHelper.isValid(dbSource)) {
				Database dbDestination = dbSource.createCopy(
						strDestinationServerName, strDestinationDBPath);
				if (DominoObjectHelper.isValid(dbDestination)) {
					return dbDestination;
				}
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return null;
	}

	/**
	 * 复制数据库
	 * 
	 * @param strSourceServerName
	 *            源数据库所在服务器
	 * @param strSourceDBPath
	 *            源数据库地址
	 * @param strDestinationServerName
	 *            目标数据库所在服务器
	 * @param strDestinationDBPath
	 *            目标数据库地址
	 * @return 复制是否成功，成功则返回true
	 */
	public static Database copyDatabase(String strSourceServerName,
			String strSourceDBPath, String strDestinationServerName,
			String strDestinationDBPath) {
		try {
			Session session = Engine.getSessionAsSignerWithFullAccess();
			if (!Helper.ValidateNotEmpty(strSourceServerName)) {// 如果源数据库所在服务器没有指定，就取当前服务器
				strSourceServerName = session.getServerName();
			}
			if (!Helper.ValidateNotEmpty(strDestinationServerName)) {// 如果目标数据库所在服务器没有指定，就取当前服务器
				strDestinationServerName = session.getServerName();
			}
			Database dbSource = session.getDatabase(strSourceServerName,
					strSourceDBPath);
			return copyDatabase(dbSource, strDestinationServerName,
					strDestinationDBPath);
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return null;
	}

	/**
	 * 复制数据库
	 * 
	 * @param strSourceDBPath
	 *            源数据库地址
	 * @param strDestinationDBPath
	 *            目标数据库地址
	 * @return
	 */
	public static Database copyDatabase(String strSourceDBPath,
			String strDestinationDBPath) {
		return copyDatabase("", strSourceDBPath, "", strDestinationDBPath);
	}

	/**
	 * 删除数据库
	 * 
	 * @param db
	 * @return
	 */
	public static boolean removeDatabase(Database db) {
		try {
			if (DominoObjectHelper.isValid(db)) {
				Helper.logMessage("START REMOVE DATABASE[" + db.getFilePath()
						+ "] on server[" + db.getServer() + "]");
				db.removeFTIndex();
				db.remove();

				db.recycle();
				return true;
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 删除数据库
	 * 
	 * @param strServerName
	 * @param strDBPath
	 * @return
	 */
	public static boolean removeDatabase(String strServerName, String strDBPath) {
		try {
			Session session = Engine.getSessionAsSignerWithFullAccess();
			if (!Helper.ValidateNotEmpty(strServerName)) {// 如果数据库所在服务器没有指定，就取当前服务器
				strServerName = session.getServerName();
			}
			Database db = session.getDatabase(strServerName, strDBPath);
			return removeDatabase(db);
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 删除数据库
	 * 
	 * @param strDBPath
	 * @return
	 */
	public static boolean removeDatabase(String strDBPath) {
		return removeDatabase("", strDBPath);
	}

	/**
	 * 删除数据库所有设计
	 * 
	 * @param db
	 * @return
	 */
	public static boolean removeAllDesignElement(Database db) {
		try {// TOOD TEST
			View view = db.getView(EngineConstants.DESIGN_VIEW_NAME);
			view.getAllEntries().removeAll(true);
			return true;
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 
	 * @param dbSource
	 * @param dbTarget
	 * @return
	 */
	public static boolean updateDesignElement(Database dbSource,
			Database dbTarget) {
		try {
			String strTemplateName = dbSource.getTemplateName();

			View viewSource = dbSource
					.getView(EngineConstants.DESIGN_VIEW_NAME);
			View viewTarget = dbTarget
					.getView(EngineConstants.DESIGN_VIEW_NAME);

			if (viewTarget == null) {
				viewTarget = dbTarget.createView(
						EngineConstants.DESIGN_VIEW_NAME, "", viewSource);
			}

			ViewNavigator nav = viewSource.createViewNav();

			ViewEntry tmpentry;
			ViewEntry entry = nav.getFirst();
			while (entry != null) {// TODO 实现
				if (entry.isDocument()) {
					Document doc = entry.getDocument();
					String strTitle = doc.getItemValueString("$TITLE");
					Helper.logMessage(strTitle);
					if (Helper.ValidateNotEmpty(strTemplateName)) {
						doc.replaceItemValue("$Class", strTemplateName);
						doc.save(true, true);
					}
				}
				tmpentry = nav.getNext();
				entry.recycle();
				entry = tmpentry;
			}
			nav.recycle();
			viewTarget.recycle();
			return true;
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 设置数据库所有设计的继承模板
	 * 
	 * @param db
	 * @param strTemplateName
	 * @return
	 */
	public static boolean setAllDBDesignTemplate(Database db,
			String strTemplateName) {
		try {
			// NoteCollection nc = db.createNoteCollection(false);
			// nc.g
			// Vector forms = db.getForms();
			// System.out.println("Agents in database:");
			// for (int i=0; i<forms.size(); i++) {
			// Form from = (Form)forms.elementAt(i);
			// form.
			// }
			View viewTarget = db.getView(EngineConstants.DESIGN_VIEW_NAME);

			if (viewTarget != null) {
				ViewNavigator nav = viewTarget.createViewNav();

				ViewEntry tmpentry;
				ViewEntry entry = nav.getFirst();
				while (entry != null) {
					try {
						if (entry.isDocument()) {
							Document doc = entry.getDocument();
							String strFlags = doc.getItemValueString("$Flags");
							// if (strFlags.equals("K")) {// XPage
							//
							// } else if (strFlags.equals("|")) {// Custom
							// Control
							//
							// } else if ((strFlags.contains("g"))
							// && (strFlags.contains("~"))) {// Eclipse Content
							//
							// } else if (strFlags.equals("")) {// Form
							//
							// } else if
							// ((strFlags.equals("Y"))||(strFlags.equals("c")))
							// {//
							// View
							//
							// } else if (strFlags.equals("U")) {// SubForm
							//
							// } else if (strFlags.equals("")) {// File Resource
							//
							// } else if (strFlags.equals("i")) {// Image
							// Resource
							//
							// } else if (strFlags.equals("=")) {// CSS
							//
							// } else if (strFlags.equals("h")) {// JavaScript
							// Library
							//
							// } else if (strFlags.equals("W")) {// Page
							//
							// } else if (strFlags.equals("f")) {// Agent
							//
							// } else if (strFlags.equals("s")) {// LotusScript
							// Library
							//
							// } else if (strFlags.equals("y")) {// Shared
							// Action
							//
							// } else if (strFlags.equals("#")) {// Frameset
							//
							// } else if (strFlags.equals("F")) {// Folder
							//
							// } else if (strFlags.equals("G")) {// Navigator
							//
							// } else {
							// continue;
							// }
							if (Helper.ValidateNotNull(strFlags)) {
								if (strFlags.equals("R")) {// Database
								} else if (strFlags.contains("X")) {// Agent
									// Data
								} else if (strFlags.contains("O")) {// Full Text
									// Query
								} else if (strFlags.contains("z")) {// DB2 View
								} else if ((strFlags.contains("g"))
										&& (strFlags.contains("~"))) {// Eclipse
									// Component
								} else if (strFlags.equals("")
										&& doc.hasItem("$Body")) {// Shared
									// Field
									// 不能修改$Class，原因未知
								} else {
									doc.replaceItemValue("$Class",
											strTemplateName);
									try {
										doc.save(true, true);
									} catch (NotesException e) {
										Helper
												.logError("can not change $Class:"
														+ strFlags
														+ doc.getItems());
									}

								}
							} else {
								Helper.logMessage("strFlags is null"
										+ doc.getItems());
							}
						}
					} catch (NotesException e) {
						Helper.logError(e);
					}

					tmpentry = nav.getNext();
					entry.recycle();
					entry = tmpentry;
				}
				nav.recycle();
				viewTarget.recycle();
				return true;
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return true;
	}

	/**
	 * 设置数据库继承的模板
	 * 
	 * @param db
	 *            要设置的数据库
	 * @param strTemplateName
	 *            继承的模板名
	 * @return
	 */
	public static boolean inheritDBTemplate(Database db, String strTemplateName) {
		try {
			Document iconDoc = getDatabaseIconDocument(db);
			iconDoc.replaceItemValue("$TITLE", db.getTitle() + (char) 10 + "#2"
					+ strTemplateName);
			return iconDoc.save(true, true);
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return true;
	}

	/**
	 * 设置数据库为模板
	 * 
	 * @param db
	 *            要设置的数据库
	 * @param strTemplateName
	 *            模板名
	 * @return
	 */
	public static boolean setDBTemplate(Database db, String strTemplateName) {
		try {// TODO TEST
			Document iconDoc = getDatabaseIconDocument(db);
			iconDoc.replaceItemValue("$TITLE", db.getTitle() + (char) 10 + "#1"
					+ strTemplateName);
			return iconDoc.save(true, true);
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return true;
	}

	/**
	 * 获得数据库属性文档
	 * 
	 * @return
	 */
	public static Document getDatabaseIconDocument(Database db) {
		try {
			Document iconDoc = db.getDocumentByID("FFFF0010");
			return iconDoc;
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return null;
	}

	/**
	 * 设置数据库默认属性
	 * 
	 * @param db
	 * @return
	 */
	public static boolean setDefaultDatabaseOption(Database db) {

		try {
			// 开启软删除
			int hours = Integer.parseInt(Engine.getProperty(
					"db.default.option.softdeletehour", "480"));
			if (Engine.getProperty("db.default.option.softdelete") == "true") {
				setDBSoftOption(db, true, hours);
			} else {
				setDBSoftOption(db, false, hours);
			}

			// 不需要未读标记
			if (Engine.getProperty("db.default.option.noreadflag") == "true") {
				db.setOption(Database.DBOPT_NOUNREAD, true);
			} else {
				db.setOption(Database.DBOPT_NOUNREAD, false);
			}

		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 设置软删除设置
	 * 
	 * @param db
	 * @param enable
	 * @param expirehours
	 * @return
	 */
	public static boolean setDBSoftOption(Database db, boolean enable,
			int expirehours) {
		try {
			db.setOption(Database.DBOPT_SOFTDELETE, enable);

			Document iconDoc = getDatabaseIconDocument(db);
			iconDoc.replaceItemValue("$SoftDeleteExpireHours", expirehours);
			return iconDoc.save();
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * ATTENTION|注意 删除文件夹下所有数据库
	 * 
	 * @return
	 */
	public static boolean removeAllFolderDatabase(String strDirectory) {
		try {
			Directory dir = Engine.getSessionAsSignerWithFullAccess()
					.getDirectory();

			DirectoryNavigator nav = dir.createNavigator();
			// TODO 实现

		} catch (NotesException e) {
			Helper.logError(e);
		}

		return true;
	}
}

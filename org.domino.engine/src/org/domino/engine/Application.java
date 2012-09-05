/**
 * 
 */
package org.domino.engine;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

import org.domino.engine.database.DBHelper;
import org.domino.engine.utility.DominoObjectHelper;

/**
 * @author admin
 * 
 */
public class Application {

	/**
	 * 应用是否处于调试状态
	 * 
	 * @return
	 */
	public static boolean isDebug() {
		return false;
	}

	public static boolean isSSOLoginForm() {
		return Engine.getProperty("menu.login.type").equals("sso");
	}

	public static boolean isMultiSessionAuthentication() {
		return Engine.getProperty("multiple.site.auth", "false").equals("true");
	}

	public static String getDefaultPassword() {
		return Engine.getProperty("default.password", "12345678");
	}

	/**
	 * 
	 * @return
	 */
	public static String getDominoSecret() {
		return Engine.getProperty("auth.domino.secret");
	}

	/**
	 * 获得引擎库数据库对象
	 * 
	 * @return
	 */
	public static Database getEngineDB() {
		return getEngineDB(Engine.getSession());
	}

	/**
	 * 获得引擎库数据库对象（管理员权限）
	 * 
	 * @return
	 */
	public static Database getFullEngineDB() {
		Database dbEngine = null;
		try {
			// 获得数据库
			dbEngine = Engine.getSessionAsSignerWithFullAccess().getDatabase(
					Engine.getSessionAsSignerWithFullAccess().getServerName(),
					getEngineDBPath());
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbEngine;
	}

	/**
	 * 获得引擎库数据库对象
	 * 
	 * @param session
	 * @return
	 */
	public static Database getEngineDB(Session session) {
		Database dbEngine = null;
		try {
			// 获得数据库
			dbEngine = session.getCurrentDatabase();
			if (!DominoObjectHelper.isValid(dbEngine)) {
				dbEngine = session.getDatabase(session.getServerName(),
						getEngineDBPath());
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbEngine;
	}

	/**
	 * 获得引擎库数据库路径
	 * 
	 * @return
	 */
	public static String getEngineDBPath() {
		return getEngineDBPath(Engine.getSession());
	}

	/**
	 * 获得引擎库数据库路径
	 * 
	 * @param session
	 * @return
	 */
	public static String getEngineDBPath(Session session) {
		return DominoObjectHelper.getPathByFileName(session,
				Helper.getProperty("engine.db.file.name"));
	}

	/**
	 * 获得设置库数据库对象
	 * 
	 * @return
	 */
	public static Database getSettingDB() {
		return getSettingDB(Engine.getSession());
	}

	/**
	 * 获得设置库数据库对象（管理员权限）
	 * 
	 * @return
	 */
	public static Database getFullSettingDB() {
		Database dbSetting = null;
		try {
			// 获得数据库
			dbSetting = Engine.getSessionAsSignerWithFullAccess().getDatabase(
					Engine.getSessionAsSignerWithFullAccess().getServerName(),
					getSettingDBPath());
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbSetting;
	}

	/**
	 * 获得设置库数据库对象
	 * 
	 * @param session
	 * @return
	 */
	public static Database getSettingDB(Session session) {
		Database dbSetting = null;
		try {
			// 获得数据库
			dbSetting = session.getDatabase(session.getServerName(),
					getSettingDBPath(session));
			if (!DominoObjectHelper.isValid(dbSetting)) {
				dbSetting = session.getDatabase(session.getServerName(),
						getSettingDBPath());
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbSetting;
	}

	/**
	 * 获得设置库数据库路径
	 * 
	 * @return
	 */
	public static String getSettingDBPath() {
		return getSettingDBPath(Engine.getSession());
	}

	/**
	 * 获得设置库数据库路径
	 * 
	 * @param session
	 * @return
	 */
	public static String getSettingDBPath(Session session) {
		return DominoObjectHelper.getPathByFileName(session,
				Helper.getProperty("setting.db.file.name"));
	}

	/**
	 * 获得日记库数据库对象
	 * 
	 * @return
	 */
	public static Database getLogDB() {
		return getLogDB(Engine.getSession());
	}

	/**
	 * 获得日记库数据库对象（管理员权限）
	 * 
	 * @return
	 */
	public static Database getFullLogDB() {
		Database dbLog = null;
		try {
			// 获得数据库
			dbLog = Engine.getSessionAsSignerWithFullAccess().getDatabase(
					Engine.getSessionAsSignerWithFullAccess().getServerName(),
					getLogDBPath());
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbLog;
	}

	/**
	 * 获得日记库数据库对象
	 * 
	 * @param session
	 * @return
	 */
	public static Database getLogDB(Session session) {
		Database dbLog = null;
		try {
			// 获得数据库
			dbLog = session.getDatabase(session.getServerName(),
					getLogDBPath(session));
			if (!DominoObjectHelper.isValid(dbLog)) {
				dbLog = session.getDatabase(session.getServerName(),
						getLogDBPath());
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbLog;
	}

	/**
	 * 获得日记库数据库路径
	 * 
	 * @return
	 */
	public static String getLogDBPath() {
		return getLogDBPath(Engine.getSession());
	}

	/**
	 * 获得日记库数据库路径
	 * 
	 * @param session
	 * @return
	 */
	public static String getLogDBPath(Session session) {
		return DominoObjectHelper.getPathByFileName(session,
				Helper.getProperty("log.db.file.name"));
	}

	/**
	 * 获得应用库数据库对象
	 * 
	 * @return
	 */
	public static Database getApplicationDB() {
		return getApplicationDB(Engine.getSession());
	}

	/**
	 * 获得应用库数据库对象（管理员权限）
	 * 
	 * @return
	 */
	public static Database getFullApplicationDB() {
		Database dbApplication = null;
		try {
			// 获得数据库
			dbApplication = Engine.getSessionAsSignerWithFullAccess()
					.getDatabase(
							Engine.getSessionAsSignerWithFullAccess()
									.getServerName(), getApplicationDBPath());
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbApplication;
	}

	/**
	 * 获得应用库数据库对象
	 * 
	 * @param session
	 * @return
	 */
	public static Database getApplicationDB(Session session) {
		Database dbApplication = null;
		try {
			// 获得数据库
			dbApplication = session.getDatabase(session.getServerName(),
					getApplicationDBPath(session));
			if (!DominoObjectHelper.isValid(dbApplication)) {
				dbApplication = session.getDatabase(session.getServerName(),
						getApplicationDBPath());
			}
		} catch (NotesException e) {
			Helper.logError(e);
		}

		return dbApplication;
	}

	/**
	 * 获得应用库数据库路径
	 * 
	 * @return
	 */
	public static String getApplicationDBPath() {
		return getApplicationDBPath(Engine.getSession());
	}

	/**
	 * 获得应用库数据库路径
	 * 
	 * @param session
	 * @return
	 */
	public static String getApplicationDBPath(Session session) {
		return DominoObjectHelper.getPathByFileName(session,
				Helper.getProperty("application.db.file.name"));
	}

	/**
	 * 创建引擎数据库至指定文件夹
	 * 
	 * @param strFolderPath
	 * @return
	 */
	public static boolean createAllEngineDatabase(String strFolderPath) {
		if (Helper.ValidateNotEmpty(strFolderPath)) {
			if (!strFolderPath.endsWith("\\")) {
				strFolderPath += "\\";
			}
		}

		// 复制引擎数据库
		// Database dbEngineSource = DBHelper
		// .getDatabase(EngineConstants.BASE_TEMPLATE_FOLDER
		// + EngineConstants.ENGINE_TEMPLATE_FILE_NAME);
		// Database dbEngine = DBHelper.copyDatabase(dbEngineSource, "",
		// strFolderPath + EngineConstants.ENGINE_TEMPLATE_FILE_NAME);
		Database dbEngine = DBHelper.copyDatabase(
				EngineConstants.BASE_TEMPLATE_FOLDER
						+ EngineConstants.ENGINE_TEMPLATE_FILE_NAME,
				strFolderPath + EngineConstants.ENGINE_TEMPLATE_FILE_NAME);
		if (Engine.getProperty("engine.create.db.engine.type").equals("full")) {
			DBHelper.inheritDBTemplate(dbEngine,
					EngineConstants.ENGINE_TEMPLATE_NAME);
		} else if (Engine.getProperty("engine.create.db.engine.type").equals(
				"partial")) {
			DBHelper.setAllDBDesignTemplate(dbEngine,
					EngineConstants.ENGINE_TEMPLATE_NAME);
		}

		Database dbSetting = DBHelper.copyDatabase(
				EngineConstants.BASE_TEMPLATE_FOLDER
						+ EngineConstants.SETTING_TEMPLATE_FILE_NAME,
				strFolderPath + EngineConstants.SETTING_TEMPLATE_FILE_NAME);
		if (Engine.getProperty("engine.create.db.setting.type").equals("full")) {
			DBHelper.inheritDBTemplate(dbSetting,
					EngineConstants.SETTING_TEMPLATE_NAME);
		} else if (Engine.getProperty("engine.create.db.setting.type").equals(
				"partial")) {
			DBHelper.setAllDBDesignTemplate(dbSetting,
					EngineConstants.SETTING_TEMPLATE_NAME);
		}

		Database dbApplication = DBHelper.copyDatabase(
				EngineConstants.BASE_TEMPLATE_FOLDER
						+ EngineConstants.APPLICATION_TEMPLATE_FILE_NAME,
				strFolderPath + EngineConstants.APPLICATION_TEMPLATE_FILE_NAME);
		if (Engine.getProperty("engine.create.db.application.type").equals(
				"full")) {
			DBHelper.inheritDBTemplate(dbApplication,
					EngineConstants.APPLICATION_TEMPLATE_NAME);
		} else if (Engine.getProperty("engine.create.db.application.type")
				.equals("partial")) {
			DBHelper.setAllDBDesignTemplate(dbApplication,
					EngineConstants.APPLICATION_TEMPLATE_NAME);
		}

		Database dbLog = DBHelper.copyDatabase(
				EngineConstants.BASE_TEMPLATE_FOLDER
						+ EngineConstants.LOG_TEMPLATE_FILE_NAME, strFolderPath
						+ EngineConstants.LOG_TEMPLATE_FILE_NAME);
		if (Engine.getProperty("engine.create.db.log.type").equals("full")) {
			DBHelper.inheritDBTemplate(dbLog, EngineConstants.LOG_TEMPLATE_NAME);
		} else if (Engine.getProperty("engine.create.db.log.type").equals(
				"partial")) {
			DBHelper.setAllDBDesignTemplate(dbLog,
					EngineConstants.LOG_TEMPLATE_NAME);
		}

		return true;
	}

	public static boolean updateAllEngineDatabase(String strFolderPath) {
		if (Helper.ValidateNotEmpty(strFolderPath)) {
			if (!strFolderPath.endsWith("\\")) {
				strFolderPath += "\\";
			}
		}

		Database dbEngineSource = DBHelper
				.getDatabase(EngineConstants.BASE_TEMPLATE_FOLDER
						+ EngineConstants.ENGINE_TEMPLATE_FILE_NAME);
		Database dbEngine = DBHelper.getDatabase(strFolderPath
				+ EngineConstants.ENGINE_TEMPLATE_FILE_NAME);
		DBHelper.updateDesignElement(dbEngineSource, dbEngine);

		return true;
	}

	/**
	 * ATTENTION|注意 删除指定文件夹下所有引擎数据库
	 * 
	 * @return
	 */
	public static boolean removeAllEngineDatabase(String strFolderPath) {
		if (Helper.ValidateNotEmpty(strFolderPath)) {
			if (!strFolderPath.endsWith("\\")) {
				strFolderPath += "\\";
			}
		}

		DBHelper.removeDatabase(strFolderPath
				+ EngineConstants.ENGINE_TEMPLATE_FILE_NAME);

		DBHelper.removeDatabase(strFolderPath
				+ EngineConstants.SETTING_TEMPLATE_FILE_NAME);

		DBHelper.removeDatabase(strFolderPath
				+ EngineConstants.APPLICATION_TEMPLATE_FILE_NAME);

		DBHelper.removeDatabase(strFolderPath
				+ EngineConstants.LOG_TEMPLATE_FILE_NAME);

		return true;
	}
}

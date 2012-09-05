/**
 * 
 */
package org.domino.engine.utility;

import lotus.domino.NotesException;
import lotus.domino.Session;

import org.domino.engine.Engine;
import org.domino.engine.Helper;

/**
 * @author iceeer
 * 
 */
public class ConsoleCommand {

	/**
	 * 重建NAMES库索引
	 */
	public static final String UPDALL_NAMES = "load updall names.nsf -R";

	/**
	 * 运行代理
	 */
	public static final String RUN_AGENT = "tell amgr run ";

	/**
	 * 更新mail（邮箱）文件夹下数据库
	 */
	public static final String CONVERT_MAIL_TEMPLATE = "load convert mail\\*.nsf ";

	/**
	 * 检查并修复数据库
	 */
	public static final String FIXUP_DATABASE = "load fixup –f";

	/**
	 * 更新视图索引
	 */
	public static final String UPDATE_VIEW = "load updall";

	/**
	 * 更新搜索索引
	 */
	public static final String UPDATE_INDEX = "load update";

	/**
	 * 更新设计
	 */
	public static final String UPDATE_DESIGN = "load design";
	
	/**
	 * 重启HTTP服务
	 */
	public static final String RESTART_HTTP = "restart task http";

	
	/**
	 * 重启服务器
	 */
	public static final String RESTART_DOMINO_SERVER = "restart server";
	
	/**
	 * 关闭服务器
	 */
	public static final String SHUTDOWN_DOMINO_SERVER = "quit";
	
	/**
	 * 开启HTTP调试
	 */
	public static final String HTTP_DEBUG_ON = "tell http debug thread on";
	
	/**
	 * 关闭HTTP调试
	 */
	public static final String HTTP_DEBUG_OFF = "tell http debug thread off";
	
	/**
	 * 运行服务器命令
	 * @param strCommand
	 * @return
	 */
	public static String runCommand(String strCommand) {
		return runCommand(null,"",strCommand);
	}
	
	/**
	 * 运行服务器命令
	 * @param strServerName
	 * @param strCommand
	 * @return
	 */
	public static String runCommand(String strServerName,
			String strCommand) {
		return runCommand(null,strServerName,strCommand);
	}
	
	/**
	 * 运行服务器命令
	 * @param session
	 * @param strServerName
	 * @param strCommand
	 * @return
	 */
	public static String runCommand(Session session, String strServerName,
			String strCommand) {
		try {
			if ((!Helper.ValidateNotNull(session))) {
				session = Engine.getSessionAsSignerWithFullAccess();
			}
			if (!Helper.ValidateNotEmpty(strServerName)) {
				strServerName = session.getServerName();
			}
			return session.sendConsoleCommand(strServerName, strCommand);
		} catch (NotesException e) {
			Helper.logError(e);
		}
		return "";
	}

	/**
	 * 运行代理
	 * 
	 * @param strServerName
	 *            服务器名
	 * @param strDBPath
	 *            数据库路径
	 * @param agentName
	 *            代理名
	 * @return
	 */
	public static String runAgent(String strServerName, String strDBPath,
			String agentName) {
			return runCommand(RUN_AGENT + strDBPath + " '" + agentName + "'");
	}

	/**
	 * 运行代理
	 * 
	 * @param strDBPath
	 *            数据库路径
	 * @param agentName
	 *            代理名
	 * @return
	 */
	public static String runAgent(String strDBPath, String agentName) {
		return runAgent("", strDBPath, agentName);
	}

	/**
	 * 升级Domino后运行
	 * 1、修复数据库
	 * 2、更新设计
	 * 3、更新视图
	 * 
	 * @return 运行结果
	 */
	public static String runAfterUpgradeServer() {
		String strReturn = "";
		try {
			strReturn += fixupDatabase();
			strReturn += updateDatabaseDesign();
			strReturn += updateView();
		} catch (Exception e) {
			Helper.logError(e);
		}
		return strReturn;
	}
	
	/**
	 * 替换邮箱数据库设计
	 * @param strMailTemplatePath
	 * @return
	 */
	public static String updateMailDesign(String strMailTemplatePath){
		return runCommand(CONVERT_MAIL_TEMPLATE + strMailTemplatePath);
	}
	
	/**
	 * 更新数据库设计
	 * @return
	 */
	public static String updateDatabaseDesign(){
		return runCommand(UPDATE_DESIGN);
	}
	
	/**
	 * 检查并修复数据库
	 * @return
	 */
	public static String fixupDatabase(){
		return runCommand(FIXUP_DATABASE);
	}

	/**
	 * 更新视图
	 * @return
	 */
	public static String updateView(){
		return runCommand(UPDATE_VIEW);
	}
	
	/**
	 * 重启HTTP服务
	 * @return
	 */
	public static String restartHTTP(){
		return runCommand(RESTART_HTTP);
	}
	
	/**
	 * 重启Domino服务器
	 * @return
	 */
	public static String restartServer(){
		return runCommand(RESTART_DOMINO_SERVER);
	}
	
	/**
	 * 关闭Domino服务器
	 * @return
	 */
	public static String shutdownServer(){
		return runCommand(SHUTDOWN_DOMINO_SERVER);
	}
	
	/**
	 * 开启HTTP调试，输出至IBM_TECHNICAL_SUPPORT\htthr_???.log
	 */
	public static String enableHTTPDEBUG(){
		return runCommand(HTTP_DEBUG_ON);
	}
	
	/**
	 * 关闭HTTP调试
	 */
	public static String disbleHTTPDEBUG(){
		return runCommand(HTTP_DEBUG_OFF);
	}
}

package org.domino.engine.utility.security;

import java.util.Iterator;
import java.util.Vector;

import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;

import lotus.domino.Document;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

public class PasswordUtil {

	/**
	 * 
	 * @param session
	 * @param strUserName
	 * @param strPassword
	 * @return
	 * @throws NotesException
	 */
	public static boolean comparePassword(Session session, String strUserName,
			String strPassword) {
		Name nmUser;
		try {
			nmUser = session.createName(strUserName);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return comparePassword(session, nmUser, strPassword);
	}

	/**
	 * 
	 * @param session
	 * @param nmUser
	 * @param strPassword
	 * @return
	 * @throws NotesException
	 */
	public static boolean comparePassword(Session session, Name nmUser,
			String strPassword) {
		Document docUser = null;
		try {
			docUser = DominoObjectHelper.getDocumentByKey(session, "names.nsf",
					"($VIMPeople)", nmUser.getAbbreviated());
		} catch (NotesException e) {
			e.printStackTrace();
		}
		return comparePassword(session, docUser, strPassword);
	}

	/**
	 * 
	 * @param session
	 * @param docUser
	 * @param strPassword
	 * @return
	 * @throws NotesException
	 */
	public static boolean comparePassword(Session session, Document docUser,
			String strPassword) {
		try {
			if (docUser != null) {
				String strHashPassword = docUser
						.getItemValueString("HTTPPassword");
				if (Helper.ValidateNotEmpty(strHashPassword)
						&& Helper.ValidateNotEmpty(strPassword)) {
					return session.verifyPassword(strPassword, strHashPassword);
				} else {
					return strPassword.equals(strHashPassword);
				}
			} else {
				Helper.logError("用户文档为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param strUserName
	 * @param strOldPassword
	 * @param strNewPassword
	 * @param strNewPassword2
	 * @return
	 */
	public static String changePassword(String strUserName,
			String strOldPassword, String strNewPassword, String strNewPassword2) {
		if (strNewPassword.equals(strNewPassword2)) {
			try {
				Session session = Engine.getSessionAsSignerWithFullAccess();
				if (comparePassword(session, strUserName, strOldPassword)) {
					if (changePassword(session, strUserName, strNewPassword)) {
						return "修改成功！";
					} else {

						return "密码修改失败！";
					}
				} else {
					return "原密码输入错误！";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "有错误发生了！";
			}
		} else {
			return "新密码不匹配！";
		}
	}

	/**
	 * 
	 * @param session
	 * @param strUserName
	 * @param strPassword
	 * @return
	 * @throws NotesException
	 */
	public static boolean changePassword(Session session, String strUserName,
			String strPassword) {
		Name nmUser;
		try {
			nmUser = session.createName(strUserName);
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return changePassword(session, nmUser, strPassword);
	}

	/**
	 * 
	 * @param session
	 * @param nmUser
	 * @param strPassword
	 * @return
	 * @throws NotesException
	 */
	public static boolean changePassword(Session session, Name nmUser,
			String strPassword) {
		Document docUser;
		try {
			// return session.resetUserPassword("", nmUser.getAbbreviated(),
			// strPassword);
			docUser = DominoObjectHelper.getDocumentByKey(session, "names.nsf",
					"($VIMPeople)", nmUser.getAbbreviated());
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
		return changePassword(session, docUser, strPassword);
	}

	/**
	 * 通过用户文档修改密码（有时无效）
	 * 
	 * @param session
	 * @param docUser
	 * @param strPassword
	 * @return
	 * @throws NotesException
	 */
	public static boolean changePassword(Session session, Document docUser,
			String strPassword) {
		try {
			if (docUser != null) {
				docUser.replaceItemValue("HTTPPassword",
						hashPassword(session, strPassword));
				docUser.save();
				return true;
			} else {
				Helper.logError("用户文档为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param session
	 * @param strSourcePassword
	 * @return
	 * @throws NotesException
	 */
	public static String hashPassword(Session session, String strSourcePassword)
			throws NotesException {
		return session.hashPassword(strSourcePassword);
	}

	/**
	 * 
	 * @param session
	 * @param strPassword
	 * @return
	 */
	public static Vector<Document> getUserHasThisPassword(Session session,
			String strPassword) {
		Vector<Document> v = new Vector<Document>();
		try {
			Vector<Document> vAllDocuments = DominoObjectHelper
					.getAllDocuments(session, "names.nsf", "($VIMPeople)");
			for (Iterator i = vAllDocuments.iterator(); i.hasNext();) {
				Document doc = (Document) i.next();
				if (comparePassword(session, doc, strPassword)) {
					v.add(doc);
				}
			}

		} catch (NotesException e) {
			Helper.logError(e);
		}
		return v;
	}

	/**
	 * 
	 * @param session
	 * @param strPassword
	 * @param blForce
	 *            是否强制修改密码
	 * @return
	 */
	public static String getUserHasThisPasswordHTML(Session session,
			String strPassword, boolean blSetForceChange, String strNewPassword) {
		long startTime = System.currentTimeMillis(); // 获取开始时间

		StringBuilder sbHTML = new StringBuilder("<table border='1'>");
		sbHTML.append("<tr><td colspan='2' align='center'>密码统计:" + strPassword
				+ "</td></tr>");
		sbHTML.append("<tr><td>全名</td><td>简名</td></tr>");
		try {
			String strNewHASHPassword = "";
			boolean changePassword = false;
			if (Helper.ValidateNotEmpty(strNewPassword)) {
				changePassword = true;
				strNewHASHPassword = session.hashPassword(strNewPassword);
			}

			View view = DominoObjectHelper.getView(session, "names.nsf",
					"($VIMPeople)");
			// ignore updates
			view.setAutoUpdate(false);
			// create a ViewNavigator
			ViewNavigator nav = view.createViewNav();
			// enable cache for max buffering
			nav.setBufferMaxEntries(400);
			// System.out.println("view: " + view.getName() +
			// " 10000 entries, Cache: " + nav.getBufferMaxEntries()); // if
			// we are not interested in the number of children, we can go a
			// little faster
			nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA
					+ ViewNavigator.VN_ENTRYOPT_NOCOLUMNVALUES);

			ViewEntry tmpentry;
			ViewEntry entry = nav.getFirst();
			while (entry != null) {
				if (entry.isDocument()) {
					Document doc = entry.getDocument();
					if (comparePassword(session, doc, strPassword)) {
						String strUserName = doc.getItemValueString("FullName");
						sbHTML.append("<tr>");
						sbHTML.append("<td>");
						sbHTML.append(strUserName);
						sbHTML.append("</td>");
						sbHTML.append("<td>");
						sbHTML.append(doc.getItemValue("FullName").toString());
						sbHTML.append("</td>");
						sbHTML.append("</tr>");
						if (blSetForceChange) {
							doc.replaceItemValue("HTTPPasswordForceChange", "1");
							doc.save();
						}
						if (changePassword) {
							doc.replaceItemValue("HTTPPassword",
									strNewHASHPassword);
							doc.save();
							System.out.println("CHANGE " + strUserName + ":"
									+ strNewPassword);
						}
					}
				}

				// if((endTime - startTime)> 60000){//运行一分钟后停止
				// sbHTML.append("<tr><td>...</td><td>...</td></tr>");
				// break;
				// }

				tmpentry = nav.getNext();
				entry.recycle();
				entry = tmpentry;
			}

			long endTime = System.currentTimeMillis(); // 获取结束时间
			sbHTML.append("<tr><td>运行时间</td><td>" + (endTime - startTime)
					+ "</td></tr>");

		} catch (NotesException e) {
			Helper.logError(e);
		}
		sbHTML.append("</table>");
		return sbHTML.toString();
	}
}

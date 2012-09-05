package org.domino.engine.menu;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.xhtml.a;
import org.apache.ecs.xhtml.div;
import org.apache.ecs.xhtml.li;
import org.apache.ecs.xhtml.span;
import org.apache.ecs.xhtml.ul;
import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.utility.DominoObjectHelper;
import org.domino.engine.utility.XSPHelper;
import org.domino.engine.utility.html.HTMLHelper;

import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.View;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;

/**
 * @author iceeer
 * 
 */
public class MenuBuilder {

	/**
	 * 
	 */
	public MenuBuilder() {
		super();
		if(Engine.getProperty("menu.display.profile").equals("false")){
			this.setDisplayUtility(false);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getHTML() {
		try {

			// 生成导航条DIV对象
			div divMenu = new div();
			divMenu.setID("menu");

			// 获得导航项视图
			View viewAllNavItems = DominoObjectHelper.getView(
					Application.getSettingDB(Engine.getSession()),
					"vwAllNavItemHierarchicalForBuildNav");

			// 添加资源文件
			attachCSSFile(divMenu);
			attachIE6JSFile(divMenu);

			ul ulNav = null;
			viewAllNavItems.setAutoUpdate(false);
			if (viewAllNavItems.getEntryCount() > 0) {// 判断导航条配置文档是否为空
				ViewNavigator nav = viewAllNavItems.createViewNav();
				ulNav = buildNavUl(nav);
			} else {
				ulNav = new ul();
			}
			viewAllNavItems.setAutoUpdate(true);

			if (isDisplayUtility()) {
				buildUtility(ulNav);
			}

			HTMLHelper.addClass(ulNav, "dropdown dropdown-horizontal");

			divMenu.addElement(ulNav);

			return divMenu.toString();
		} catch (Exception e) {
			Helper.logError(e);
			return "";
		}
	}

	/**
	 * 生成导航条常用工具菜单
	 * 
	 * @param ulNav
	 */
	private void buildUtility(ul ulNav) {
		try {
			li liNavItem = new li();// 常用工具条
			liNavItem.setID("menu-toolbar-utility");
			HTMLHelper.addClass(liNavItem, "dir");

			ul ulUtility = new ul();
			if (DominoObjectHelper.isAnonymous(Engine.getSession())) {
				liNavItem.addElement(DominoObjectHelper.NAME_OF_ANONYMOUS);

				li liLogin = new li();
				a aLogin = new a();
				aLogin.addAttribute("href", XSPHelper.getContextPath() + "/"
						+ Engine.getProperty("login.page.name")
						+ ("&redirectto=" + XSPHelper.getFullURL()));
				aLogin.addAttribute("title", "点击登录");
				aLogin.addElement(Engine.getProperty("lang.login.text"));
				liLogin.addElement(aLogin);
				ulUtility.addElement(liLogin);
			} else {
				String strDiplayUserName = DominoObjectHelper
						.getCurrentUserCommonName(Engine.getSession());
				if (Engine.isDebug()) {
					Helper.logMessage("current user name:" + strDiplayUserName);
				}

				a aProfile = new a();
				aProfile.addAttribute("href", XSPHelper.getContextPath() + "/"
						+ Engine.getProperty("profile.page.name"));
				aProfile.addAttribute("title", "设置");
				aProfile.addElement(strDiplayUserName);
				liNavItem.addElement(aProfile);

				li liLogout = new li();
				a aLogout = new a();
				aLogout.addAttribute("href", XSPHelper.getContextPath() + "/"
						+ Engine.getProperty("logout.page.name")
						+ ("&redirectto=" + XSPHelper.getHostURL() + XSPHelper.getContextPath()));
				aLogout.addAttribute("title", "点击注销");
				aLogout.addElement(Engine.getProperty("lang.logout.text"));
				liLogout.addElement(aLogout);
				ulUtility.addElement(liLogout);
			}

			liNavItem.addElement(ulUtility);

			ulNav.addElement(liNavItem);
		} catch (Exception e) {
			Helper.logError(e);
		}
	}

	/**
	 * 生成导航条UL对象并设置属性
	 * 
	 * @param nav
	 * @return
	 */
	private ul buildNavUl(ViewNavigator nav) {
		ul ulNav = new ul();
		try {
			ViewEntry entry = nav.getCurrent();
			if (entry != null) {
				while (entry != null) {
					li liNavItem = buildNavLi(entry);
					ulNav.addElement(liNavItem);

					// 创建子菜单
					entry = nav.getChild();
					if (entry != null) {
						HTMLHelper.addClass(liNavItem, "dir");
						ul ulChild = buildNavUl(nav);
						liNavItem.addElement(ulChild);
					}
					entry = nav.getNextSibling();
					if ((entry == null)
							&& !(HTMLHelper.hasClass(liNavItem, "first"))) {
						HTMLHelper.addClass(liNavItem, "last");
					}
				}
				entry = nav.getParent();
			}
		} catch (Exception e) {
			Helper.logError(e);
		}
		return ulNav;
	}

	/**
	 * 
	 * @param entry
	 * @return
	 */
	private li buildNavLi(ViewEntry entry) {
		li liNavItem = new li();
		try {
			Document doc = entry.getDocument();
			liNavItem.setID(doc.getItemValueString("NavItemID"));
			if (isFirstEntry(entry)) {
				HTMLHelper.addClass(liNavItem, "first");
			}
			buildLiInnerElement(liNavItem, doc);
		} catch (Exception e) {
			Helper.logError(e);
		}
		return liNavItem;
	}

	/**
	 * 
	 * @param liNavItem
	 * @param doc
	 * @throws NotesException
	 */
	private void buildLiInnerElement(li liNavItem, Document doc)
			throws NotesException {
		String strURL = doc.getItemValueString("NavItemUrl");
		String strDescription = doc.getItemValueString("NavItemDescription");
		String strName = doc.getItemValueString("NavItemName");

		strURL = transferURL(strURL);

		if (Helper.ValidateNotEmpty(strURL)) {
			a aNaItem = new a();

			aNaItem.addAttribute("href", strURL);
			aNaItem.addAttribute("title", strDescription);
			aNaItem.addAttribute("target",
					doc.getItemValueString("NavItemUrlTarget"));
			aNaItem.addElement(strName);
			liNavItem.addElement(aNaItem);
		} else {
			if (Helper.ValidateNotEmpty(strDescription)) {
				span s = new span();
				s.addAttribute("title", strDescription);
				s.addElement(strName);
				liNavItem.addElement(s);
			} else {
				liNavItem.addElement(strName);
			}

		}

	}

	/**
	 * @param strURL
	 * @return
	 */
	private String transferURL(String strURL) {
		if (StringUtils.indexOfIgnoreCase(strURL, "<<folder>>") != -1) {// 当前文件夹
			strURL = StringUtils
					.replace(strURL, "<<folder>>", DominoObjectHelper
							.getCurrentFolderURL(Engine.getSession()));
		}
		if (StringUtils.indexOfIgnoreCase(strURL, "<<nsf>>") != -1) {// 当前数据库
			strURL = StringUtils.replace(strURL, "<<nsf>>",
					DominoObjectHelper.getCurrentDBURL(Engine.getSession()));
		}
		if (StringUtils.indexOfIgnoreCase(strURL, "<<plugins>>") != -1) {// 插件路径
			strURL = StringUtils
					.replace(strURL, "<<plugins>>", DominoObjectHelper
							.getPluginsFolderURL(Engine.getSession()));
		}
		if (StringUtils.indexOfIgnoreCase(strURL, "<<resource>>") != -1) {// 资源路径
			strURL = StringUtils.replace(strURL, "<<resource>>",
					DominoObjectHelper.getResourceBaseURL(Engine.getSession()));
		}
		return strURL;
	}

	/**
	 * 清楚所有设置
	 * 
	 * @return
	 */
	public boolean clearAllSetting() {
		try {
			View viewAllNavItems = DominoObjectHelper.getView(
					Application.getSettingDB(Engine.getSession()),
					"vwAllNavItemHierarchicalForBuildNav");

			viewAllNavItems.getAllEntries().removeAll(true);
			viewAllNavItems.refresh();
			return true;
		} catch (NotesException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isFirstEntry(ViewEntry entry) {
		try {
			String strPos = entry.getPosition('.');
			if (strPos.endsWith("1")) {
				return true;
			}
		} catch (Exception e) {
			Helper.logError(e);
		}
		return false;
	}

	/**
	 * 
	 * @param divNav
	 */
	public void attachCSSFile(div divNav) {
		divNav.addElement(HTMLHelper.addCSSLink(DominoObjectHelper
				.getResourceBaseURL(Engine.getSession())
				+ "css/dropdown/dropdown.css"));
		divNav.addElement(HTMLHelper.addCSSLink(DominoObjectHelper
				.getResourceBaseURL(Engine.getSession())
				+ "css/dropdown/themes/default/default.advanced.css"));
		// 加到CSS文件里了
		// divNav.addElement(HTMLHelper
		// .addCSSText("html > body .lotusFrame {margin-top:34px;}"));

	}

	/**
	 * 
	 * @param divMenu
	 */
	public void attachIE6JSFile(div divMenu) {
		divMenu.addElement("<!--[if lt IE 7]>");
		divMenu.addElement(HTMLHelper.addJSLink(DominoObjectHelper
				.getResourceBaseURL(Engine.getSession())
				+ "js/jquery/jquery.dropdown.js"));
		divMenu.addElement("<![endif]-->");
	}

	/**
	 * 设置是否显示常用工具条
	 * 
	 * @param isDisplayUtility
	 *            the isDisplayUtility to set
	 */
	public void setDisplayUtility(boolean isDisplayUtility) {
		this.isDisplayUtility = isDisplayUtility;
	}

	/**
	 * 是否显示常用工具条
	 * 
	 * @return the isDisplayUtility
	 */
	public boolean isDisplayUtility() {
		return isDisplayUtility;
	}

	/**
	 * 是否显示常用工具条
	 */
	private boolean isDisplayUtility = true;

}

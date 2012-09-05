package org.domino.engine.utility.menu;



import junit.framework.TestCase;
import lotus.domino.Session;

import org.domino.engine.Engine;
import org.domino.engine.menu.MenuBuilder;
import org.domino.engine.menu.MenuItem;
import org.domino.engine.menu.MenuItemDoc;
import org.junit.Assert;
import org.junit.Test;

public class NavBuilderTest extends TestCase{

	@Test
	public void testNavBuilder_Accuracy1() {
		MenuBuilder oNavBuilder = new MenuBuilder();
		Assert.assertNotNull(oNavBuilder);
	}
	
	@Test
	public void testNavBuilder_Anonymous_Accuracy1() {
		MenuBuilder oNavBuilder = new MenuBuilder();
		Assert.assertNotNull(oNavBuilder);
	}

	@Test
	public void testGetHTML_Accuracy1() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		String strHTML = oNavBuilder.getHTML();
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'></ul></div>");
	}
	
	@Test
	public void testGetHTML_Accuracy2() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("/");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		buildTestData(Engine.getSession());
		String strHTML = oNavBuilder.getHTML();
		//Helper.logMessage(strHTML);
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='home' class='first'><a href='/' target='' title=''>首页</a></li><li id='menu1' class='dir'>菜单1<ul><li id='menu11' class='first'><a href='http://127.0.0.1/' target='' title=''>子菜单11</a></li><li id='menu12' class='last'><a href='http://127.0.0.1/' target='' title=''>子菜单12</a></li></ul></li><li id='menu2' class='dir last'><a href='/' target='' title=''>菜单2</a><ul><li id='menu22' class='first'><a href='http://127.0.0.1/' target='' title=''>子菜单22</a></li><li id='menu23' class='last'><a href='http://127.0.0.1/' target='' title=''>子菜单23</a></li></ul></li></ul></div>");
	}
	
	/**
	 * 显示常用工具条
	 */
	@Test
	public void testGetHTML_Accuracy3() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("/");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(true);
		oNavBuilder.clearAllSetting();
		buildTestData(Engine.getSession());
		String strHTML = oNavBuilder.getHTML();
		//Helper.logMessage(strHTML);
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='home' class='first'><a href='/' target='' title=''>首页</a></li><li id='menu1' class='dir'>菜单1<ul><li id='menu11' class='first'><a href='http://127.0.0.1/' target='' title=''>子菜单11</a></li><li id='menu12' class='last'><a href='http://127.0.0.1/' target='' title=''>子菜单12</a></li></ul></li><li id='menu2' class='dir last'><a href='/' target='' title=''>菜单2</a><ul><li id='menu22' class='first'><a href='http://127.0.0.1/' target='' title=''>子菜单22</a></li><li id='menu23' class='last'><a href='http://127.0.0.1/' target='' title=''>子菜单23</a></li></ul></li><li id='menu-toolbar-utility' class='dir'><a href='/profile.xsp' title='设置'></a><ul><li><a href='/names.nsf?logout&redirectto=' title='点击注销'>注销</a></li></ul></li></ul></div>");
	}
	
	@Test
	public void testGetHTML_Pluign_Accuracy() {
		Engine.setCurrentFolder("testoa\\");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		
		MenuItem oMenuItem = new MenuItem("","<<plugins>>","");
		oMenuItem.setID("test");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		String strHTML = oNavBuilder.getHTML();
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='test' class='first'><a href='/testoa/plugins/' target='' title=''></a></li></ul></div>");
	}
	
	@Test
	public void testGetHTML_Folder_Accuracy() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("/");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		
		MenuItem oMenuItem = new MenuItem("","<<folder>>","");
		oMenuItem.setID("test");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		String strHTML = oNavBuilder.getHTML();
		Assert
				.assertEquals(strHTML,
						"<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='test' class='first'><a href='/testoa/' target='' title=''></a></li></ul></div>");
	}
	
	@Test
	public void testGetHTML_NSF_Accuracy() {
		Engine.setCurrentFolder("testoa\\");

		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		
		MenuItem oMenuItem = new MenuItem("","<<nsf>>","");
		oMenuItem.setID("test");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		String strHTML = oNavBuilder.getHTML();
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='test' class='first'></li></ul></div>");
	}
	
	@Test
	public void testGetHTML_RES_Accuracy() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("/");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		
		MenuItem oMenuItem = new MenuItem("","<<resource>>","");
		oMenuItem.setID("test");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		String strHTML = oNavBuilder.getHTML();
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='test' class='first'><a href='/' target='' title=''></a></li></ul></div>");
	}
	
	@Test
	public void testGetHTML_IP_Accuracy() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("/");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		
		MenuItem oMenuItem = new MenuItem("","<<ip>>","");
		oMenuItem.setID("test");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		String strHTML = oNavBuilder.getHTML();
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='test' class='first'><a href='<<ip>>' target='' title=''></a></li></ul></div>");
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetHTML_PORT_Accuracy() {
		Engine.setCurrentFolder("testoa\\");
		Engine.setBaseResourcePath("/");
		
		MenuBuilder oNavBuilder = new MenuBuilder();
		oNavBuilder.setDisplayUtility(false);
		oNavBuilder.clearAllSetting();
		
		MenuItem oMenuItem = new MenuItem("","<<port>>","");
		oMenuItem.setID("test");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		String strHTML = oNavBuilder.getHTML();
		Assert.assertEquals(strHTML, "<div id='menu'><link media='screen' rel='stylesheet' href='/css/dropdown/dropdown.css' type='text/css'/><link media='screen' rel='stylesheet' href='/css/dropdown/themes/default/default.advanced.css' type='text/css'/><!--[if lt IE 7]><script type='text/javascript' src='/js/jquery/jquery.dropdown.js' language='Javascript'></script><![endif]--><ul class='dropdown dropdown-horizontal'><li id='test' class='first'><a href='<<port>>' target='' title=''></a></li></ul></div>");
	}
	

	/**
	 * 
	 * @param session
	 */
	public void buildTestData(Session session){
		MenuItem oMenuItem = new MenuItem("首页","","");
		oMenuItem.setID("home");
		oMenuItem.setNavItemNo(0.0);
		oMenuItem.setNavItemUrl("/");
		MenuItemDoc doc = new MenuItemDoc();
		doc.create(oMenuItem);
		
		MenuItem fMenuItem = new MenuItem("菜单1","","");
		fMenuItem.setID("menu1");
		fMenuItem.setNavItemNo(1.0);
		doc.create(fMenuItem);
		
		MenuItem sMenuItem12 = new MenuItem("子菜单12","http://127.0.0.1/",fMenuItem.getID());
		sMenuItem12.setNavItemNo(120.0);
		sMenuItem12.setID("menu12");
		doc.create(sMenuItem12);
		
		MenuItem sMenuItem11 = new MenuItem("子菜单11","http://127.0.0.1/",fMenuItem.getID());
		sMenuItem11.setNavItemNo(110.0);
		sMenuItem11.setID("menu11");
		doc.create(sMenuItem11);
		
		MenuItem fMenuItem2 = new MenuItem("菜单2","","");
		fMenuItem2.setID("menu2");
		fMenuItem2.setNavItemNo(999.0);
		fMenuItem2.setNavItemUrl("/");
		doc.create(fMenuItem2);
		
		MenuItem sMenuItem22 = new MenuItem("子菜单22","http://127.0.0.1/",fMenuItem2.getID());
		sMenuItem22.setNavItemNo(110.0);
		sMenuItem22.setID("menu22");
		doc.create(sMenuItem22);
		
		MenuItem sMenuItem23 = new MenuItem("子菜单23","http://127.0.0.1/",fMenuItem2.getID());
		sMenuItem23.setNavItemNo(120.0);
		sMenuItem23.setID("menu23");
		doc.create(sMenuItem23);
		
	}

}

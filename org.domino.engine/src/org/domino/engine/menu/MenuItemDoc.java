/**
 * 
 */
package org.domino.engine.menu;

import org.domino.engine.Application;
import org.domino.engine.Engine;
import org.domino.engine.Helper;
import org.domino.engine.foundation.DataStore;
import org.domino.engine.foundation.DominoDataStore;
import org.domino.engine.utility.DominoObjectHelper;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

/**
 * 菜单项文档存储
 * 
 * @author iceeer
 * 
 */
public class MenuItemDoc {

	private static String FORM_NAME = "fmNavItem";
	
	
	public boolean create(Object o) {
		try {
			MenuItem oMenuItem = (MenuItem) o;
			Database db = Application.getSettingDB(Engine.getSession());

			Document doc = db.createDocument();
			doc.replaceItemValue("Form",FORM_NAME);
			doc.replaceItemValue("NavItemID",oMenuItem.getID());
			doc.replaceItemValue("NavItemName",oMenuItem.getNavItemName());
			doc.replaceItemValue("NavItemDescription",oMenuItem.getNavItemDescription());
			doc.replaceItemValue("NavItemNo",oMenuItem.getNavItemNo());
			doc.replaceItemValue("ParentNavItemID",oMenuItem.getParentNavItemID());
			doc.replaceItemValue("NavItemUrl",oMenuItem.getNavItemUrl());
			doc.replaceItemValue("NavItemUrlTarget",oMenuItem.getNavItemUrlTarget());
			doc.replaceItemValue("IsEnable",oMenuItem.isEnable()?"1":"0");
			//doc.replaceItemValue("NavItemUser",oMenuItem.getID());//TODO
			//doc.replaceItemValue("NavItemManager",oMenuItem.getID());
			
			
			if(Helper.ValidateNotEmpty(oMenuItem.getParentNavItemID())){//创建回复文档
				Document fDocument = getDocumentByID(oMenuItem.getParentNavItemID());
				if(Helper.ValidateNotNull(fDocument)){
					doc.makeResponse(fDocument);
					fDocument.save();
				}else{
					Helper.logError("菜单项文档设置的父文档找不到:" + oMenuItem.getParentNavItemID());
				}
			}else{//清除回复文档系统域
				if(doc.hasItem(DominoObjectHelper.REF_SYSTEM_FIELD_NAME)){
					doc.removeItem(DominoObjectHelper.REF_SYSTEM_FIELD_NAME);
				}
			}
			doc.save();
			return true;
		} catch (NotesException e) {
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
	

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Document getDocumentByID(String id) {
		Document doc = null;
		try {
			Database db = Application.getSettingDB(Engine.getSession());
			doc = DominoObjectHelper.getDocumentByKey(db, "vwAllNavItemByNavItemIDForBuildHierarchy", id);
		} catch (Exception e) {
			Helper.logError(e);
		}
		return doc;
	}

}

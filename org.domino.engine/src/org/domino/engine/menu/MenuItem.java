/**
 * 
 */
package org.domino.engine.menu;

import java.util.ArrayList;
import java.util.List;

import org.domino.engine.foundation.UniDataObject;

/**
 * 导航项配置类
 * 
 * @author iceeer
 * 
 */
public class MenuItem extends UniDataObject{

	/**
	 * 默认构造函数
	 * 
	 */
	public MenuItem() {
		super();
		this.navItemName = "";
		this.navItemDescription = "";
		this.navItemUrl = "";
		this.parentNavItemID = "";
		this.navItemUrlTarget = "";
		this.navItemNo = 0.0;
		this.enable = true;
	}

	/**
	 *  默认构造函数
	 * @param strNavItemName 导航菜单标题
	 * @param strNavItemUrl 菜单链接
	 * @param strParentNavItemID 父菜单标识
	 */
	public MenuItem(String strNavItemName, String strNavItemUrl, String strParentNavItemID) {
		this();
		this.setNavItemName(strNavItemName);
		this.setNavItemUrl(strNavItemUrl);
		this.setParentNavItemID(strParentNavItemID);
	}

	/**
	 * @param navItemName
	 *            the navItemName to set
	 */
	public void setNavItemName(String navItemName) {
		this.navItemName = navItemName;
	}

	/**
	 * @return the navItemName
	 */
	public String getNavItemName() {
		return navItemName;
	}

	/**
	 * 设置导航项使用者
	 * 
	 * @param navItemUser
	 *            the navItemUser to set
	 */
	public void setNavItemUser(List<String> navItemUser) {
		this.navItemUser = navItemUser;
	}

	/**
	 * 添加导航项使用者
	 * 
	 * @param strNavItemUser
	 */
	public void addNavItemUser(String strNavItemUser) {
		this.navItemUser.add(strNavItemUser);
	}

	/**
	 * 删除导航项使用者
	 * 
	 * @param strNavItemUser
	 */
	public boolean removeNavItemUser(String strNavItemUser) {
		if (this.navItemUser.contains(strNavItemUser)) {
			this.navItemUser.remove(strNavItemUser);
			return true;
		}
		return false;
	}

	/**
	 * 删除全部导航项使用者
	 */
	public void clearNavItemUser() {
		if (!this.navItemUser.isEmpty()) {
			this.navItemUser.clear();
		}
	}

	/**
	 * 获得导航项使用者
	 * 
	 * @return the navItemUser
	 */
	public List<String> getNavItemUser() {
		return navItemUser;
	}

	/**
	 * @param navItemDescription
	 *            the navItemDescription to set
	 */
	public void setNavItemDescription(String navItemDescription) {
		this.navItemDescription = navItemDescription;
	}

	/**
	 * @return the navItemDescription
	 */
	public String getNavItemDescription() {
		return navItemDescription;
	}

	/**
	 * @param navItemNo
	 *            the navItemNo to set
	 */
	public void setNavItemNo(Double navItemNo) {
		this.navItemNo = navItemNo;
	}

	/**
	 * @return the navItemNo
	 */
	public Double getNavItemNo() {
		return navItemNo;
	}

	/**
	 * @param parentNavItemID
	 *            the parentNavItemID to set
	 */
	public void setParentNavItemID(String parentNavItemID) {
		this.parentNavItemID = parentNavItemID;
	}

	/**
	 * @return the parentNavItemID
	 */
	public String getParentNavItemID() {
		return parentNavItemID;
	}

	/**
	 * @param navItemUrl
	 *            the navItemUrl to set
	 */
	public void setNavItemUrl(String navItemUrl) {
		this.navItemUrl = navItemUrl;
	}

	/**
	 * @return the navItemUrl
	 */
	public String getNavItemUrl() {
		return navItemUrl;
	}

	/**
	 * @param navItemUrlTarget
	 *            the navItemUrlTarget to set
	 */
	public void setNavItemUrlTarget(String navItemUrlTarget) {
		this.navItemUrlTarget = navItemUrlTarget;
	}

	/**
	 * @return the navItemUrlTarget
	 */
	public String getNavItemUrlTarget() {
		return navItemUrlTarget;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * 设置导航项管理者
	 * 
	 * @param navItemManager
	 *            the navItemManager to set
	 */
	public void setNavItemManager(List<String> navItemManager) {
		this.navItemManager = navItemManager;
	}

	/**
	 * 添加导航项管理者
	 * 
	 * @param strNavItemManager
	 */
	public void addNavItemManager(String strNavItemManager) {
		this.getNavItemManager().add(strNavItemManager);
	}

	/**
	 * 删除导航项管理者
	 * 
	 * @param strNavItemManager
	 */
	public boolean removeNavItemManager(String strNavItemManager) {
		if (this.getNavItemManager().contains(strNavItemManager)) {
			this.getNavItemManager().remove(strNavItemManager);
			return true;
		}
		return false;
	}

	/**
	 * 删除全部导航项管理者
	 */
	public void clearNavItemManager() {
		if (!this.getNavItemManager().isEmpty()) {
			this.getNavItemManager().clear();
		}
	}

	/**
	 * 获得导航项管理者
	 * 
	 * @return the navItemManager
	 */
	public List<String> getNavItemManager() {
		return navItemManager;
	}

	private String navItemName;
	private String navItemDescription;
	private Double navItemNo;
	private String parentNavItemID;
	private List<String> navItemUser = new ArrayList<String>();
	private String navItemUrl;
	private String navItemUrlTarget;
	private boolean enable;
	private List<String> navItemManager = new ArrayList<String>();

}

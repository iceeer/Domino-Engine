/**
 * 
 */
package org.domino.engine.menu.portal;

import org.domino.engine.menu.MenuItem;

/**
 * @author admin
 *
 */
public class PortalMenuItem extends MenuItem {

	/**
	 * 
	 */
	public PortalMenuItem() {
	}

	/**
	 * @param strNavItemName
	 * @param strNavItemUrl
	 * @param strParentNavItemID
	 */
	public PortalMenuItem(String strNavItemName, String strNavItemUrl,
			String strParentNavItemID) {
		super(strNavItemName, strNavItemUrl, strParentNavItemID);
	}

}

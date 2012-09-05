package org.domino.engine.foundation;

import org.domino.engine.Helper;

import lotus.domino.Item;
import lotus.domino.NotesException;

/**
 * 
 * 
 * item.getType() has some value:
Item.ACTIONCD 
Item.ASSISTANTINFO 
Item.ATTACHMENT (file attachment) 
Item.AUTHORS 
Item.COLLATION 
Item.DATETIMES (date-time or range of date-time values) 
Item.EMBEDDEDOBJECT 
Item.ERRORITEM (error occurred while getting type) 
Item.FORMULA (Domino formula) 
Item.HTML (HTML source text) 
Item.ICON 
Item.LSOBJECT 
Item.MIME_PART 
Item.NAMES 
Item.NOTELINKS (link to a database, view, or document) 
Item.NOTEREFS (reference to the parent document) 
Item.NUMBERS (number or number list) 
Item.OTHEROBJECT 
Item.QUERYCD 
Item.RICHTEXT 
Item.READERS 
Item.SIGNATURE 
Item.TEXT (text or text list) 
Item.UNAVAILABLE 
Item.UNKNOWN 
Item.USERDATA 
Item.USERID 
Item.VIEWMAPDATA 
Item.VIEWMAPLAYOUT 
 * 
 * @author admin
 *
 */
public class ExtendItem {
	
	/**
	 * 
	 */
	private String itemName = "";
	
	/**
	 * 
	 */
	private String itemValue = "";
	
	/**
	 * 
	 */
	private int itemType;
	
	/**
	 * 
	 * @param item
	 */
	public ExtendItem(Item item){
		try {
			itemName = item.getName();
			setItemType(item.getType());
			if(item.getType() == Item.ATTACHMENT){
				itemValue = "附件未实现";
			}else{
				itemValue = item.getText();
			}
			
			
		} catch (NotesException e) {
			Helper.logError(e);
		}
		
	}
	
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the itemType
	 */
	public int getItemType() {
		return itemType;
	}
	
	
	
}

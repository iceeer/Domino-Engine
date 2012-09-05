package org.domino.engine.foundation;

import java.util.Hashtable;

import org.domino.engine.EngineConstants;
import org.domino.engine.Helper;
import org.domino.engine.utility.StringUtil;

/**
 * Abstract base object for a data store object that provides an internal list
 * of changed object properties. The purpose is to quickly get information in a
 * data source what property changes have to be transferred to persistent
 * storage.
 */
public abstract class UniDataObject extends DataObject{
	private String id;

	public UniDataObject() {
		super();
		this.id = Helper.buildUniqueID();
	}

	/**
	 * 获得标识
	 * 
	 * @return
	 */
	public String getID() {
		return id;
	}

	/**
	 * 设置标识
	 * 
	 * @param strID
	 */
	public void setID(String strID) {
		if (!StringUtil.isEqual(this.id, strID)) {
			ObjectPropertyChangeEvent evt = new ObjectPropertyChangeEvent(EngineConstants.FIELD_ID_NAME,
					this.id, strID);
			this.id = strID;
			addChangeEvent(evt);
		}
	}

}
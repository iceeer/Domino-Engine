package org.domino.engine.foundation;

/**
 * This class is used to track property changes in a {@link DataObject}.
 */
public class ObjectPropertyChangeEvent {
	private String m_fieldID;
	private Object m_oldValue;
	private Object m_newValue;
	
	public ObjectPropertyChangeEvent(String fieldID, Object oldValue, Object newValue) {
		m_fieldID=fieldID;
		m_oldValue=oldValue;
		m_newValue=newValue;
	}
	
	/**
	 * Returns an ID for the changed property
	 * 
	 * @return ID
	 */
	public String getFieldId() {
		return m_fieldID;
	}
	
	/**
	 * Returns the old property value
	 * 
	 * @return old value
	 */
	public Object getOldValue() {
		return m_oldValue;
	}
	
	/**
	 * Returns the new property value
	 * 
	 * @return new value
	 */
	public Object getNewValue() {
		return m_newValue;
	}
}

package org.domino.engine.foundation;

import java.util.Hashtable;

/**
 * Abstract base object for a data store object that provides an internal list
 * of changed object properties. The purpose is to quickly get information in a
 * data source what property changes have to be transferred to persistent
 * storage.
 */
public abstract class DataObject {
	private boolean m_initialized = false;
	private Hashtable<String, ObjectPropertyChangeEvent> m_changeEvents;

	public DataObject() {
	}

	protected void init() {
		if (!m_initialized) {
			// lazy creation of member variables
			m_changeEvents = new Hashtable<String, ObjectPropertyChangeEvent>();
			m_initialized = true;
		}
	}

	/**
	 * Method to check whether properties in this object have been changed.
	 * 
	 * @return <code>true</code> if there are changed properties
	 */
	public boolean hasChanges() {
		init();

		return !m_changeEvents.isEmpty();
	}

	/**
	 * Returns the property change events for this object
	 * 
	 * @return change events or an empty list
	 */
	public ObjectPropertyChangeEvent[] getChanges() {
		init();

		return m_changeEvents.values().toArray(
				new ObjectPropertyChangeEvent[m_changeEvents.size()]);
	}

	/**
	 * Adds a change event to the internal list. If the property has been
	 * changed before, the old and new change event will be merged into one.
	 * 
	 * @param evt
	 *            change event
	 */
	protected void addChangeEvent(ObjectPropertyChangeEvent evt) {
		init();

		String fieldId = evt.getFieldId();
		ObjectPropertyChangeEvent oldEvt = m_changeEvents.get(fieldId);
		if (oldEvt == null) {
			m_changeEvents.put(fieldId, evt);
		} else {
			// merge old and new event
			Object oldValue = oldEvt.getOldValue();
			m_changeEvents.put(fieldId, new ObjectPropertyChangeEvent(fieldId,
					oldValue, evt.getNewValue()));
		}
	}

	/**
	 * Method to clear the change event list
	 */
	public void resetChanges() {
		m_initialized = false;
		init();
	}

}
package org.domino.engine.foundation;

import java.util.List;

/**
 * Interface for a CRUD-based Data Store
 */
public interface DataStore {

	/**
	 * The method create an Object in the data store
	 * @param o
	 * @return
	 */
	public boolean create(Object o);

	/**
	 * The method get an Object from the data store
	 * @param id
	 * @return
	 */
	public Object get(Object o);
	
	/**
	 * Writes changes made to the specified Object instance to the data store to
	 * store them permanently.
	 * 
	 * @param o	 
	 * @return
	 */
	public boolean update(Object o);

	/**
	 * The method delete an Object from the data store
	 * @param o
	 * @return
	 */
	public boolean delete(Object o);

}
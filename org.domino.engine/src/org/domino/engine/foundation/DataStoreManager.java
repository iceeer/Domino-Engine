package org.domino.engine.foundation;

import javax.faces.FacesException;

import org.domino.engine.utility.XSPHelper;

/**
 * The DataStoreManager manages the {@link DataStore} instance that is used for
 * the data storage of the application.
 */
public class DataStoreManager {
	private String m_storageClass;
	private DataStore m_store;

	public DataStoreManager() {
	}

	/**
	 * This method is used by JSF to set the class name of the {@link DataStore}
	 * to be used.
	 * 
	 * @param className
	 *            class name
	 */
	@SuppressWarnings("unchecked")
	public void setStorageClass(String className) {
		m_storageClass = className;
		try {
			// try to initialize the data store using Java reflection
			Class<? extends DataStore> storageClazz = (Class<? extends DataStore>) getClass()
					.forName(className);
			m_store = (DataStore) storageClazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new FacesException(e);
		} catch (IllegalAccessException e) {
			throw new FacesException(e);
		} catch (InstantiationException e) {
			throw new FacesException(e);
		}
	}

	/**
	 * Returns the class name of the {@link DataStore} to be used.
	 * 
	 * @return class name
	 */
	public String getStorageClass() {
		return m_storageClass;
	}

	/**
	 * Returns the data store instance for the data access.
	 * 
	 * @return data store
	 */
	public DataStore getStore() {
		return m_store;
	}

	/**
	 * Convenience method to access the instance of the DataStoreManager for the
	 * current user from the session scope.
	 * 
	 * @return DataStoreManager instance
	 */
	public static DataStoreManager getManager() {
        return (DataStoreManager) XSPHelper.getBindingValue("#{DataStoreManager}"); 
	}
}
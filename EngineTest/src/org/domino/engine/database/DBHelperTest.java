/**
 * 
 */
package org.domino.engine.database;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.domino.engine.Helper;
import org.domino.engine.foundation.NotesTestCase;
import org.junit.Test;

/**
 * @author iceeer
 * 
 */
public class DBHelperTest extends NotesTestCase {

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#getDatabase(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDatabase() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#hasDatabase(java.lang.String)}
	 * .
	 */
	@Test
	public void testHasDatabase() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.domino.engine.database.DBHelper#hasView()}.
	 */
	@Test
	public void testHasView() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.domino.engine.database.DBHelper#hasForm()}.
	 */
	@Test
	public void testHasForm() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#createDatabase()}.
	 */
	@Test
	public void testCreateDatabase() {
		String strDBPath = "test/create/database" + Helper.GetSimpleDateAndTime() + ".nsf";
		Assert.assertTrue("创建数据库失败",
				(DBHelper.createDatabase(strDBPath) != null));
		Assert.assertTrue(DBHelper.hasDatabase(strDBPath));
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#createView(lotus.domino.Database, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateView() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.domino.engine.database.DBHelper#createForm()}.
	 */
	@Test
	public void testCreateForm() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#deleteDatabase()}.
	 */
	@Test
	public void testDeleteDatabase() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.domino.engine.database.DBHelper#deleteView()}.
	 */
	@Test
	public void testDeleteView() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.domino.engine.database.DBHelper#deleteForm()}.
	 */
	@Test
	public void testDeleteForm() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#copyDatabase(lotus.domino.Database, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCopyDatabaseDatabaseStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#copyDatabase(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCopyDatabaseStringStringStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#copyDatabase(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCopyDatabaseStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#removeDatabase(lotus.domino.Database)}
	 * .
	 */
	@Test
	public void testRemoveDatabaseDatabase() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#removeDatabase(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testRemoveDatabaseStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#removeDatabase(java.lang.String)}
	 * .
	 */
	@Test
	public void testRemoveDatabaseString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#removeAllDesignElement(lotus.domino.Database)}
	 * .
	 */
	@Test
	public void testRemoveAllDesignElement() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#updateDesignElement(lotus.domino.Database, lotus.domino.Database)}
	 * .
	 */
	@Test
	public void testUpdateDesignElement() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#setAllDBDesignTemplate(lotus.domino.Database, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetAllDBDesignTemplate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#inheritDBTemplate(lotus.domino.Database, java.lang.String)}
	 * .
	 */
	@Test
	public void testInheritDBTemplate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#setDBTemplate(lotus.domino.Database, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetDBTemplate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#getDatabaseIconDocument(lotus.domino.Database)}
	 * .
	 */
	@Test
	public void testGetDatabaseIconDocument() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#setDefaultDatabaseOption(lotus.domino.Database)}
	 * .
	 */
	@Test
	public void testSetDefaultDatabaseOption() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#setDBSoftOption(lotus.domino.Database, boolean, int)}
	 * .
	 */
	@Test
	public void testSetDBSoftOption() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.database.DBHelper#removeAllFolderDatabase(java.lang.String)}
	 * .
	 */
	@Test
	public void testRemoveAllFolderDatabase() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#Object()}.
	 */
	@Test
	public void testObject() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#clone()}.
	 */
	@Test
	public void testClone() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#finalize()}.
	 */
	@Test
	public void testFinalize() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#getClass()}.
	 */
	@Test
	public void testGetClass() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#notify()}.
	 */
	@Test
	public void testNotify() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#notifyAll()}.
	 */
	@Test
	public void testNotifyAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#wait()}.
	 */
	@Test
	public void testWait() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long)}.
	 */
	@Test
	public void testWaitLong() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link java.lang.Object#wait(long, int)}.
	 */
	@Test
	public void testWaitLongInt() {
		fail("Not yet implemented");
	}

}

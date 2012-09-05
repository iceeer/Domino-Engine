/**
 * 
 */
package org.domino.engine;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author iceeer
 * 
 */
public class ApplicationTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.domino.engine.Application#isDebug()}.
	 */
	@Test
	public void testIsDebug() {
		Assert.assertEquals(Application.isDebug(), false);
	}

	/**
	 * Test method for {@link org.domino.engine.Application#isSSOLoginForm()}.
	 */
	@Test
	public void testIsSSOLoginForm() {
		Assert.assertEquals(Application.isSSOLoginForm(), false);
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#isMultiSessionAuthentication()}.
	 */
	@Test
	public void testIsMultiSessionAuthentication() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getDefaultPassword()}.
	 */
	@Test
	public void testGetDefaultPassword() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getDominoSecret()}.
	 */
	@Test
	public void testGetDominoSecret() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getEngineDB()}.
	 */
	@Test
	public void testGetEngineDB() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getFullEngineDB()}.
	 */
	@Test
	public void testGetFullEngineDB() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getEngineDB(lotus.domino.Session)}.
	 */
	@Test
	public void testGetEngineDBSession() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getEngineDBPath()}.
	 */
	@Test
	public void testGetEngineDBPath() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getEngineDBPath(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetEngineDBPathSession() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getSettingDB()}.
	 */
	@Test
	public void testGetSettingDB() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getFullSettingDB()}.
	 */
	@Test
	public void testGetFullSettingDB() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getSettingDB(lotus.domino.Session)}.
	 */
	@Test
	public void testGetSettingDBSession() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getSettingDBPath()}.
	 */
	@Test
	public void testGetSettingDBPath() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getSettingDBPath(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetSettingDBPathSession() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getLogDB()}.
	 */
	@Test
	public void testGetLogDB() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getFullLogDB()}.
	 */
	@Test
	public void testGetFullLogDB() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getLogDB(lotus.domino.Session)}.
	 */
	@Test
	public void testGetLogDBSession() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getLogDBPath()}.
	 */
	@Test
	public void testGetLogDBPath() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getLogDBPath(lotus.domino.Session)}.
	 */
	@Test
	public void testGetLogDBPathSession() {

	}

	/**
	 * Test method for {@link org.domino.engine.Application#getApplicationDB()}.
	 */
	@Test
	public void testGetApplicationDB() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getFullApplicationDB()}.
	 */
	@Test
	public void testGetFullApplicationDB() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getApplicationDB(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetApplicationDBSession() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getApplicationDBPath()}.
	 */
	@Test
	public void testGetApplicationDBPath() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#getApplicationDBPath(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetApplicationDBPathSession() {

	}

	/**
	 * Test method for
	 * {@link org.domino.engine.Application#createAllEngineDatabase(java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateAllEngineDatabase() {
		String strFolderPath = Engine.getProperty("host.folder");
		Application.removeAllEngineDatabase(strFolderPath);
		Assert.assertEquals(Application.createAllEngineDatabase(strFolderPath),
				true);
	}
	
	@Test
	public void testUpdateAllEngineDatabase() {
		String strFolderPath = Engine.getProperty("host.folder");
		Assert.assertEquals(Application.updateAllEngineDatabase(strFolderPath),
				true);
	}

}

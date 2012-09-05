package org.domino.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.domino.engine");
		//$JUnit-BEGIN$
		suite.addTestSuite(HelperTest.class);
		suite.addTestSuite(org.domino.engine.utility.security.PasswordUtilTest.class);
		suite.addTestSuite(org.domino.engine.utility.menu.NavBuilderTest.class);
		suite.addTestSuite(org.domino.engine.sap.SAPConnectionTest.class);
		//$JUnit-END$
		return suite;
	}

}

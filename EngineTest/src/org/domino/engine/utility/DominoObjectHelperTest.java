/**
 * 
 */
package org.domino.engine.utility;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.domino.engine.Engine;
import org.domino.engine.utility.diiop.DIIOPHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author iceeer
 * 
 */
public class DominoObjectHelperTest extends TestCase {

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#createSession(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateSession() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getSessionToken(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetSessionToken() {
		Assert.assertTrue(!(DominoObjectHelper.getSessionToken(Engine
				.getSession()).equals("")));
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocument(lotus.domino.Session, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocument() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentAndLock(lotus.domino.Session, lotus.domino.Database, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocumentAndLockSessionDatabaseStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentAndLock(lotus.domino.Session, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocumentAndLockSessionStringStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentByKey(lotus.domino.Database, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocumentByKeyDatabaseStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentByKey(lotus.domino.Session, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocumentByKeySessionStringStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentByKey(lotus.domino.Database, java.lang.String, java.util.Vector)}
	 * .
	 */
	@Test
	public void testGetDocumentByKeyDatabaseStringVectorOfString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentByKey(lotus.domino.Session, java.lang.String, java.lang.String, java.util.Vector)}
	 * .
	 */
	@Test
	public void testGetDocumentByKeySessionStringStringVectorOfString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getAllEntriesByKey(lotus.domino.Session, java.lang.String, java.lang.String, java.util.Vector)}
	 * .
	 */
	@Test
	public void testGetAllEntriesByKeySessionStringStringVectorOfString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getAllEntriesByKey(lotus.domino.Database, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAllEntriesByKeyDatabaseStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getAllEntriesByKey(lotus.domino.Session, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAllEntriesByKeySessionStringStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCountByKeyFromView(lotus.domino.Session, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetCountByKeyFromView() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getView(lotus.domino.Database, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetViewDatabaseString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getView(lotus.domino.Session, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetViewSessionStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getAllEntries(lotus.domino.Session, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAllEntries() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getAllDocuments(lotus.domino.Session, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAllDocuments() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getURLByFileName(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetURLByFileName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getPathByFileName(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetPathByFileName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentURL()}.
	 */
	@Test
	public void testGetCurrentURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentDBFileName(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentDBFileName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentDBURL(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentDBURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentDBFilePath(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentDBFilePath() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentFolderURL(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentFolderURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentFolderPath(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentFolderPath() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getPluginsFolderURL(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetPluginsFolderURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getPluginsFolderPath(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetPluginsFolderPath() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getResourceBaseURL(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetResourceBaseURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#buildStaticID(lotus.domino.Document)}
	 * .
	 */
	@Test
	public void testBuildStaticID() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#attachFileToRichTextItem(lotus.domino.Document, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testAttachFileToRichTextItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCNFromField(lotus.domino.Session, lotus.domino.Document, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetCNFromField() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDateFromField(lotus.domino.Document, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDateFromField() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDisplayDate(lotus.domino.Document, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDisplayDate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getEntryType(lotus.domino.ViewEntry)}
	 * .
	 */
	@Test
	public void testGetEntryType() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentCount(lotus.domino.Session, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocumentCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getChildDocumentCount(lotus.domino.ViewNavigator)}
	 * .
	 */
	@Test
	public void testGetChildDocumentCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDescendantsDocumentCount(lotus.domino.ViewNavigator)}
	 * .
	 */
	@Test
	public void testGetDescendantsDocumentCount() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCommonNameString(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetCommonNameString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCommonName(lotus.domino.Session, java.util.Vector)}
	 * .
	 */
	@Test
	public void testGetCommonName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentUserName(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentUserName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentUser(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentUserCommonName(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentUserCommonName() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#isAnonymous(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testIsAnonymousSession() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#isAnonymous(java.lang.String)}
	 * .
	 */
	@Test
	public void testIsAnonymousString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getItemValue(lotus.domino.Session, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetItemValue() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getItemValueString(lotus.domino.Session, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetItemValueString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDocumentFromNames(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetDocumentFromNames() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getGroupMembers(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetGroupMembers() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#buildLoginURL(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testBuildLoginURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#buildLogoutURL(lotus.domino.Session, java.lang.String)}
	 * .
	 */
	@Test
	public void testBuildLogoutURL() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getCurrentDateTime(lotus.domino.Session)}
	 * .
	 */
	@Test
	public void testGetCurrentDateTime() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#setItemAuthors(lotus.domino.Item)}
	 * .
	 */
	@Test
	public void testSetItemAuthors() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#setItemReaders(lotus.domino.Item)}
	 * .
	 */
	@Test
	public void testSetItemReaders() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#setItemNames(lotus.domino.Item)}
	 * .
	 */
	@Test
	public void testSetItemNames() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#setFieldAuthors(lotus.domino.Document, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetFieldAuthors() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#setFieldReaders(lotus.domino.Document, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetFieldReaders() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#setFieldNames(lotus.domino.Document, java.lang.String)}
	 * .
	 */
	@Test
	public void testSetFieldNames() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#buildCommonField(lotus.domino.Session, lotus.domino.Document)}
	 * .
	 */
	@Test
	public void testBuildCommonFieldSessionDocument() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getEnvironmentString(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetEnvironmentString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDominoHTMLPath()}.
	 */
	@Test
	public void testGetDominoHTMLPath() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDominoHTMLPathByDocument(lotus.domino.Document)}
	 * .
	 */
	@Test
	public void testGetDominoHTMLPathByDocument() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.domino.engine.utility.DominoObjectHelper#getDominoHTMLURLByDocument(lotus.domino.Document)}
	 * .
	 */
	@Test
	public void testGetDominoHTMLURLByDocument() {
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
	
	/**
	 * Test method for {@link java.lang.Object#wait(long, int)}.
	 */
	@Test
	public void testGetDomAuthSessId() {
		DominoObjectHelper.getDomAuthSessId("http://std.localhost.com/names.nsf?login",DIIOPHelper.getStrUser(), DIIOPHelper.getStrPwd());
	}

}

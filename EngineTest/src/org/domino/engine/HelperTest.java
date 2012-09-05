package org.domino.engine;
/**
 * 
 */



import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.domino.engine.Helper;

import org.junit.After;
import org.junit.Before;

/**
 * @author iceeer
 *
 */
public class HelperTest extends TestCase{

	/**
	 * 
	 */
	@Override
	public void setUp() {
	}

	/**
	 * 
	 */
	@Override
	public void tearDown() {
	}
	
	public void testGetQueryMap_Accuracy1(){
		String strQuery = "a=aaa";
		Map<String, String> map = Helper.getQueryMap(strQuery);
		assertEquals(map.containsKey("a"), true);
		assertEquals(map.containsValue("aaa"), true);
		assertEquals(map.get("a"), "aaa");
	}
	
	public void testGetQueryMap_Accuracy2(){
		String strQuery = "12312&a=aaa&123rqwre";
		Map<String, String> map = Helper.getQueryMap(strQuery);
		assertEquals(map.containsKey("a"), true);
		assertEquals(map.containsValue("aaa"), true);
		assertEquals(map.get("a"), "aaa");
	}
	
	public void testGetQueryMap_Failure1(){
		String strQuery = "a=aaa";
		Map<String, String> map = Helper.getQueryMap(strQuery);
		assertEquals(map.containsKey("b"), false);
		assertEquals(map.containsValue("bbb"), false);
	}
	
	public void testGetQueryMap_Failure2(){
		String strQuery = "12312&a=aaa&123rqwre";
		Map<String, String> map = Helper.getQueryMap(strQuery);
		assertEquals(map.containsKey("b"), false);
		assertEquals(map.containsValue("bbb"), false);
	}
	
	public void testisMonthEnd(){
		assertFalse(Helper.isMonthEnd(new Date(2010,6,28,13,43,0)));
	}
	
	public void testisMonthEnd2(){
		assertTrue(Helper.isMonthEnd(new Date(2010,6,30,13,43,0)));
	}
	
	public void testTrims1(){
		String empty = " ";//半角空格
		assertEquals(Helper.trims(empty),"");
	}
	
	public void testTrims2(){
		String empty = "　";//全角空格
		assertEquals(Helper.trims(empty),"");
	}
	
	public void testTrims3(){
		String empty = "　 ";//全角空格+半角空格
		assertEquals(Helper.trims(empty),"");
	}
	
	public void testTrims4(){
		String empty = "　中国 ";//全角空格+半角空格
		assertEquals(Helper.trims(empty),"中国");
	}
	
	public void testTrims5(){
		String empty = "　china ";//全角空格+半角空格
		assertEquals(Helper.trims(empty),"china");
	}

}

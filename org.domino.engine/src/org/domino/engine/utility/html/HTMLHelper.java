package org.domino.engine.utility.html;

import org.apache.ecs.html.Script;
import org.apache.ecs.html.Style;
import org.apache.ecs.xhtml.link;
import org.apache.ecs.xhtml.script;
import org.domino.engine.Helper;

import com.ibm.commons.util.StringUtil;

public class HTMLHelper {

	/**
	 * add a class for the object
	 * @param element
	 * @param strAddClass
	 */
	public static void addClass(org.apache.ecs.MultiPartElement element,
			String strAddClass) {
		String strClass = element.getAttribute("class");
		if (Helper.ValidateNotEmpty(strClass)) {
			element.removeAttribute("class");
			element.addAttribute("class", strClass + " " + strAddClass);
		} else {
			element.addAttribute("class", strAddClass);
		}
	}

	/**
	 * remove ths class for the object
	 * @param element
	 * @param strRemoveClass
	 */
	public static void removeClass(org.apache.ecs.MultiPartElement element,
			String strRemoveClass) {
		// TODO
	}

	/**
	 * set the object a class
	 * @param element
	 * @param strClass
	 */
	public static void setClass(org.apache.ecs.MultiPartElement element,
			String strClass) {
		if (Helper.ValidateNotEmpty(element.getAttribute("class"))) {
			element.removeAttribute("class");
		}
		element.addAttribute("class", strClass);
	}

	/**
	 * check the object has the class
	 * @param element
	 * @param strClass
	 * @return
	 */
	public static boolean hasClass(org.apache.ecs.MultiPartElement element,
			String strClass) {
		String strAllClass = element.getAttribute("class");
		if (Helper.ValidateNotEmpty(strAllClass)) {
			strAllClass = strAllClass.trim();
			String[] arrClass = strAllClass.split(" ");
			for(int i=0;i<arrClass.length;i++){
				if(StringUtil.equalsIgnoreCase(strClass, arrClass[i].trim())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * add a css link
	 * @return
	 */
	public static link addCSSLink(String strHref){
		link linkCss = new link();
		linkCss.setHref(strHref);
		linkCss.setRel("stylesheet");
		linkCss.setType("text/css");
		linkCss.setMedia("screen");
		
		return linkCss;
	}
	
	/**
	 * add a css link
	 * @param strJSText
	 * @return
	 */
	public static String addCSSLinkS(String strHref){
		return addCSSLink(strHref).toString();
	}
	
	/**
	 * add a js link
	 * @return
	 */
	public static script addJSLink(String strHref) {
		script oScript = new script();
		oScript.setType("text/javascript");
		oScript.setSrc(strHref);

		return oScript;
	}
	
	/**
	 * add a js link
	 * @param strJSText
	 * @return
	 */
	public static String addJSLinkS(String strHref){
		return addJSLink(strHref).toString();
	}
	
	/**
	 * add some css code
	 * @return
	 */
	public static Style addCSSText(String strCSSText){
		Style oStyle = new Style();
		oStyle.setType("text/css");
		oStyle.addElement(strCSSText);
		
		return oStyle;
	}
	
	/**
	 * add some css code
	 * @param strJSText
	 * @return
	 */
	public static String addCSSTextS(String strCSSText){
		return addCSSText(strCSSText).toString();
	}
	
	/**
	 * add some js code
	 * @return
	 */
	public static Script addJSText(String strJSText){
		Script oScript = new Script();
		oScript.setType("text/javascript");
		oScript.addElement(strJSText);
		
		return oScript;
	}
	
	/**
	 * add some js code
	 * @param strJSText
	 * @return
	 */
	public static String addJSTextS(String strJSText){
		return addJSText(strJSText).toString();
	}
	
	/**
	 * JS redirect to some url
	 * @param strURL
	 * @return
	 */
	public static String getRedirectJS(String strURL){
		try {
			String strJS = "window.location='" + strURL + "';";
			return addJSTextS(strJS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * remove the browser cookie
	 * @return
	 */
	public static String removeCookieJS(){//TODO no use
		try {
			String strJS = "document.cookie='';";
			return addJSTextS(strJS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}

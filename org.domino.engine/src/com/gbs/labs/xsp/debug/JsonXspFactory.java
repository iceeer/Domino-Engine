package com.gbs.labs.xsp.debug;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.el.ValueBinding;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.xsp.component.UIPassThroughTag.TagAttribute;
import com.ibm.xsp.model.AbstractDataModel;
import com.ibm.xsp.model.DataSource;

public class JsonXspFactory extends JsonJavaFactory {
	public static final JsonXspFactory	instance	= new JsonXspFactory();

	public boolean isUIComponent (Object obj) throws JsonException {
		return obj instanceof UIComponentBase;
	}

	public boolean isDatasource (Object obj) throws JsonException {
		return obj instanceof DataSource;
	}

	public boolean isDate (Object obj) throws JsonException {
		return obj instanceof Date;
	}

	public boolean isDataModel (Object obj) throws JsonException {
		return obj instanceof AbstractDataModel;
	}

	public boolean isLog (Object obj) throws JsonException {
		return obj instanceof XspCommonsLogging;
	}

	public boolean isConsoleEvent (Object obj) throws JsonException {
		return obj instanceof ConsoleEvent;
	}

	public boolean isStackTraceElement (Object obj) throws JsonException {
		return obj instanceof StackTraceElement;
	}

	public boolean isTagAtribute (Object obj) throws JsonException {
		return obj instanceof TagAttribute;
	}

	public boolean isLocale (Object obj) throws JsonException {
		return obj instanceof Locale;
	}

	public boolean isIterator (Object obj) throws JsonException {
		return obj instanceof Iterator;
	}

	public boolean isValueBinding (Object obj) throws JsonException {
		return obj instanceof ValueBinding;
	}

}

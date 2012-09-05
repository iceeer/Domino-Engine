package com.gbs.labs.xsp.debug;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log; //import org.apache.commons.logging.LogFactory;

public class XspCommonsLogging implements Log {

	// private static Log logger = LogFactory.getLog(XspCommonsLogging.class);

	public static final int			LOG_LEVEL_TRACE	= 1;
	public static final int			LOG_LEVEL_DEBUG	= 2;
	public static final int			LOG_LEVEL_INFO	= 3;
	public static final int			LOG_LEVEL_WARN	= 4;
	public static final int			LOG_LEVEL_ERROR	= 5;
	public static final int			LOG_LEVEL_FATAL	= 6;
	public static final int			LOG_LEVEL_ALL	= (LOG_LEVEL_TRACE - 1);
	public static final int			LOG_LEVEL_OFF	= (LOG_LEVEL_FATAL + 1);

	// private Vector<ConsoleEvent> _logTrace = new Vector<ConsoleEvent>();
	// private Vector<ConsoleEvent> _logDebug = new Vector<ConsoleEvent>();
	// private Vector<ConsoleEvent> _logInfo = new Vector<ConsoleEvent>();
	// private Vector<ConsoleEvent> _logWarn = new Vector<ConsoleEvent>();
	// private Vector<ConsoleEvent> _logError = new Vector<ConsoleEvent>();
	// private Vector<ConsoleEvent> _logFatal = new Vector<ConsoleEvent>();
	private Vector<ConsoleEvent>	_console		= new Vector<ConsoleEvent>();
	// private TreeMap<Date, ConsoleEvent> _consoleMap = new TreeMap<Date, ConsoleEvent>();
	protected int					currentLogLevel;
	private WrappedViewRoot			_contextView;

	public XspCommonsLogging () {

	}

	public void setView (WrappedViewRoot view) {
		_contextView = view;
	}

	public WrappedViewRoot getView () {
		return _contextView;
	}

	public Vector<ConsoleEvent> getConsole () {
		return _console;
	}

	public Vector<ConsoleEvent> getConsole (int level) {
		Vector<ConsoleEvent> result = new Vector<ConsoleEvent>();
		for (ConsoleEvent curEvent : _console) {
			if (curEvent.getType() >= level) {
				result.add(curEvent);
			}
		}
		return result;
	}

	// public TreeMap<Date, ConsoleEvent> getConsoleMap () {
	// return _consoleMap;
	// }
	//
	// public TreeMap<Date, ConsoleEvent> getConsoleMap (int level) {
	// TreeMap<Date, ConsoleEvent> returnMap = new TreeMap<Date, ConsoleEvent>();
	// for (Map.Entry<Date, ConsoleEvent> curEntry : returnMap.entrySet()) {
	// if (curEntry.getValue().getType() >= level) {
	// returnMap.put(curEntry.getKey(), curEntry.getValue());
	// }
	// }
	// return returnMap;
	// }

	protected void setCurrentLogLevel (int currentLogLevel) {
		this.currentLogLevel = currentLogLevel;
	}

	protected boolean isLevelEnabled (int logLevel) {
		return (logLevel >= currentLogLevel);
	}

	protected void log (int type, Object message, Throwable t) {
		// TODO: Make this not suck so hard.
		// Don't have access to java timestamp pattern references while I'm on the plane
		// Well, technically I do but only if I spend $10 on wi-fi service

		_console.add(new ConsoleEvent(type, message.toString(), t));
		// try {
		// Calendar curTime = Calendar.getInstance();
		// _consoleMap.put(curTime.getTime(), new ConsoleEvent(type, message.toString(), t));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public void debug (Object message) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_DEBUG)) {
			log(XspCommonsLogging.LOG_LEVEL_DEBUG, message, null);
		}
	}

	public void debug (Object message, Throwable t) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_DEBUG)) {
			log(XspCommonsLogging.LOG_LEVEL_DEBUG, message, t);
		}
	}

	public void error (Object message) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_ERROR)) {
			if (message instanceof Exception) {
				log(XspCommonsLogging.LOG_LEVEL_ERROR, message, (Exception) message);
			} else {
				log(XspCommonsLogging.LOG_LEVEL_ERROR, message, null);
			}

		}
	}

	public void error (Object message, Throwable t) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_ERROR)) {
			log(XspCommonsLogging.LOG_LEVEL_ERROR, message, t);
		}
	}

	public void fatal (Object message) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_FATAL)) {
			log(XspCommonsLogging.LOG_LEVEL_FATAL, message, null);
		}
	}

	public void fatal (Object message, Throwable t) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_FATAL)) {
			log(XspCommonsLogging.LOG_LEVEL_FATAL, message, t);
		}
	}

	public void info (Object message) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_INFO)) {
			log(XspCommonsLogging.LOG_LEVEL_INFO, message, null);
		}
	}

	public void info (Object message, Throwable t) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_INFO)) {
			log(XspCommonsLogging.LOG_LEVEL_INFO, message, t);
		}
	}

	public boolean isDebugEnabled () {
		return isLevelEnabled(XspCommonsLogging.LOG_LEVEL_DEBUG);
	}

	public boolean isErrorEnabled () {
		return isLevelEnabled(XspCommonsLogging.LOG_LEVEL_ERROR);
	}

	public boolean isFatalEnabled () {
		return isLevelEnabled(XspCommonsLogging.LOG_LEVEL_FATAL);
	}

	public boolean isInfoEnabled () {
		return isLevelEnabled(XspCommonsLogging.LOG_LEVEL_INFO);
	}

	public boolean isTraceEnabled () {
		return isLevelEnabled(XspCommonsLogging.LOG_LEVEL_TRACE);
	}

	public boolean isWarnEnabled () {
		return isLevelEnabled(XspCommonsLogging.LOG_LEVEL_WARN);
	}

	public void trace (Object message) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_TRACE)) {
			log(XspCommonsLogging.LOG_LEVEL_TRACE, message, null);
		}
	}

	public void trace (Object message, Throwable t) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_TRACE)) {
			log(XspCommonsLogging.LOG_LEVEL_TRACE, message, t);
		}
	}

	public void warn (Object message) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_WARN)) {
			log(XspCommonsLogging.LOG_LEVEL_WARN, message, null);
		}
	}

	public void warn (Object message, Throwable t) {
		if (isLevelEnabled(XspCommonsLogging.LOG_LEVEL_WARN)) {
			log(XspCommonsLogging.LOG_LEVEL_WARN, message, t);
		}
	}

	// private Vector<ConsoleEvent> getConsoleByType (int type) {
	// Vector<ConsoleEvent> console = null;
	// switch (type) {
	// case XspCommonsLogging.LOG_LEVEL_TRACE:
	// console = _logTrace;
	// break;
	// case XspCommonsLogging.LOG_LEVEL_DEBUG:
	// console = _logDebug;
	// break;
	// case XspCommonsLogging.LOG_LEVEL_INFO:
	// console = _logInfo;
	// break;
	// case XspCommonsLogging.LOG_LEVEL_WARN:
	// console = _logWarn;
	// break;
	// case XspCommonsLogging.LOG_LEVEL_ERROR:
	// console = _logError;
	// break;
	// case XspCommonsLogging.LOG_LEVEL_FATAL:
	// console = _logFatal;
	// break;
	// }
	// return console;
	// }

	public String toJson () {
		String result = "";
		try {
			result = getView().getParent().getJsonGenerator().toJson(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String toJson (int level) {
		getView().getParent().getJsonGenerator().setLogOutputLevel(level);
		return toJson();
	}

	// public String toJson () {
	// // Vector<ConsoleEvent> console = getConsoleByType(currentLogLevel);
	// // if (console != null) {
	// // return toJSONString(console);
	// // } else {
	// // return null;
	// // }
	// String output = "";
	// JSONArray events = new JSONArray();
	// Iterator<ConsoleEvent> iter = _console.iterator();
	// while (iter.hasNext()) {
	// ConsoleEvent event = iter.next();
	// JSONObject eventJson = new JSONObject();
	// events.add(eventJson);
	// eventJson.put("type", event.getType());
	// eventJson.put("message", event.getMessage());
	// if (event.getStackTrace() != null) {
	// JSONArray stackJson = new JSONArray();
	// eventJson.put("stackTrace", stackJson);
	// StackTraceElement[] stackTrace = event.getStackTrace();
	// for (int i = 0; i < 20; i++) {
	// stackJson.add(stackTrace[i].toString());
	// }
	// }
	// }
	// output = events.toJSONString();
	// return output;
	// }

	// public String toJson (int type) {
	// // Vector<ConsoleEvent> console = getConsoleByType(type);
	// // if (console != null) {
	// // return toJSONString(console);
	// // } else {
	// // return null;
	// // }
	// String output = "";
	// JSONArray events = new JSONArray();
	// Iterator<ConsoleEvent> iter = _console.iterator();
	// while (iter.hasNext()) {
	// ConsoleEvent event = iter.next();
	// if (event.getType() == type) {
	// JSONObject eventJson = new JSONObject();
	// events.add(eventJson);
	// eventJson.put("type", event.getType());
	// eventJson.put("message", event.getMessage());
	// if (event.getStackTrace() != null) {
	// JSONArray stackJson = new JSONArray();
	// eventJson.put("stackTrace", stackJson);
	// StackTraceElement[] stackTrace = event.getStackTrace();
	// for (int i = 0; i < 20; i++) {
	// stackJson.add(stackTrace[i].toString());
	// }
	// }
	// }
	// }
	// output = events.toJSONString();
	// return output;
	// }

	// private String toJSONString (Vector<ConsoleEvent> console) {
	// String output = "";
	// JSONArray events = new JSONArray();
	// Iterator<ConsoleEvent> iter = console.iterator();
	// while (iter.hasNext()) {
	// ConsoleEvent event = iter.next();
	// JSONObject eventJson = new JSONObject();
	// events.add(eventJson);
	// eventJson.put("message", event.getMessage());
	// if (event.getStackTrace() != null) {
	// JSONArray stackJson = new JSONArray();
	// eventJson.put("stackTrace", stackJson);
	// StackTraceElement[] stackTrace = event.getStackTrace();
	// for (int i = 0; i < 20; i++) {
	// stackJson.add(stackTrace[i].toString());
	// }
	// }
	// }
	// output = events.toJSONString();
	// return output;
	// }

}
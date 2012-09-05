package com.gbs.labs.xsp.debug;

import java.util.Calendar;
import java.util.Date;

public class ConsoleEvent {
	private StackTraceElement[]	stackTrace	= null;
	private String				message;
	private Date				timestamp;
	private int					type;

	// public ConsoleEvent (String parmMessage) {
	//		
	// this.message = parmMessage;
	// this.stackTrace = new Throwable().getStackTrace();
	// }

	// public ConsoleEvent (String parmMessage, boolean includeTrace) {
	// initTimestamp();
	// this.message = parmMessage;
	// if (includeTrace) this.stackTrace = new Throwable().getStackTrace();
	// }

	public ConsoleEvent (int parmType, String parmMessage, Throwable t) {
		init(parmType, parmMessage, t);
	}

	private void init (int parmType, String parmMessage, Throwable t) {
		initTimestamp();
		this.message = parmMessage;
		this.type = parmType;
		if (t != null) {
			this.stackTrace = t.getStackTrace();
		}
	}

	public void initTimestamp () {
		Calendar curTime = Calendar.getInstance();
		timestamp = curTime.getTime();
	}

	public String getMessage () {
		return this.message;
	}

	public int getType () {
		return this.type;
	}

	public Date getTime () {
		return this.timestamp;
	}

	public StackTraceElement[] getStackTrace () {
		return stackTrace;
	}

}

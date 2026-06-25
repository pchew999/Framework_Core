package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;
import com.wm.app.b2b.server.*;
// --- <<IS-END-IMPORTS>> ---

public final class jsleep

{
	// ---( internal utility methods )---

	final static jsleep _instance = new jsleep();

	static jsleep _newInstance() { return new jsleep(); }

	static jsleep _cast(Object o) { return (jsleep)o; }

	// ---( server methods )---




	public static final void sleep (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sleep)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional milliSeconds
		// [i] field:0:optional seconds
		IDataCursor cursor = pipeline.getCursor();
		
		// sleep - sleep for given number of milliseconds
		//
		//		milliSeconds (input,opt) - the number of milliseconds to sleep the current thread
		//		seconds (input,opt) - the number of seconds to sleep the current thread
		//
		String msecs = null;
		String secs = null;
		if (cursor.first("milliSeconds")) msecs = (String) cursor.getValue();
		if (cursor.first("seconds")) secs = (String) cursor.getValue();
		if (msecs == null && secs == null) throw new ServiceException("sleep: Required parameter 'milliSeconds' or 'seconds' missing"); 
		if (msecs != null && secs != null) throw new ServiceException("sleep: Ambiguous input 'milliSeconds' and 'seconds' both supplied"); 
		
		try {
			int i;
			if(msecs != null) {
				i = Integer.parseInt(msecs.toString());
			}else{
				i = Integer.parseInt(secs.toString()) * 1000;
			}
			Thread.sleep(i);
		} catch (Exception e) {
			throw new ServiceException("sleep error: " + e.toString());
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


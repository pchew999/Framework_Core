package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:27:24 CST
// -----( ON-HOST: wm1

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

public final class jrandom

{
	// ---( internal utility methods )---

	final static jrandom _instance = new jrandom();

	static jrandom _newInstance() { return new jrandom(); }

	static jrandom _cast(Object o) { return (jrandom)o; }

	// ---( server methods )---




	public static final void random (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(random)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required maxsize
		// [o] field:0:required random_value
		IDataCursor cursor = pipeline.getCursor();
		
		// random - generate a random integer
		//
		//		maxsize (input,req) - a number indicating the maximum allowable random value
		//
		//		random_value (output,str) - a random number between 0 <= n < maxsize
		//
		String sMaxSize = null;
		int maxsize,randomInt;
		
		if (cursor.first("maxsize")) sMaxSize = (String) cursor.getValue();
		if (sMaxSize == null) throw new ServiceException("random: 'maxsize' parameter must not be null");
		try {
			Integer i = new Integer(sMaxSize);
			maxsize = i.intValue();
			Random generator = new Random();
			randomInt = generator.nextInt(maxsize);	
		}catch(Exception ex) {
			throw new ServiceException("random: error generating random number using maxsize='" + sMaxSize + "'");
		}
		
		IDataUtil.put(cursor, "random_value", Integer.toString(randomInt));
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


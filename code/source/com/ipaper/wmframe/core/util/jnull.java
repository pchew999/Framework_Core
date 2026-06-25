package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jnull

{
	// ---( internal utility methods )---

	final static jnull _instance = new jnull();

	static jnull _newInstance() { return new jnull(); }

	static jnull _cast(Object o) { return (jnull)o; }

	// ---( server methods )---




	public static final void blankIfNull (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(blankIfNull)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional string
		// [o] field:0:required newstring
		IDataCursor cursor = pipeline.getCursor();
		
		// blankIfNull - return an empty string if the input string is null
		//
		//		string (input,req) - a string to test
		//
		//		newstring (output,str) - a string containing either 'string' or "" if 'string' is null
		//
		String sString = null;
		if (cursor.first("string")) sString = (String) cursor.getValue();
		
		if (sString == null) {
			IDataUtil.put(cursor, "newstring", "");
		}else{
			IDataUtil.put(cursor, "newstring", sString);
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void ifNull (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ifNull)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional string
		// [i] field:0:optional value
		// [o] field:0:required newstring
		IDataCursor cursor = pipeline.getCursor();
		
		// ifNull - return a supplied value if an input string is null
		//
		//		string (input,req) - a string to test
		//		value (input,req) - the replacement value if 'string' is null
		//
		//		newstring (output,str) - a string containing either 'string' or 'value' if 'string' is null
		//
		String sString = null;
		String sValue = null;
		
		if (cursor.first("string")) sString = (String) cursor.getValue();
		if (cursor.first("value")) sValue = (String) cursor.getValue();
		
		if (sString == null) {
			if (sValue == null) throw new ServiceException("ifNull: Missing input 'value'");
			IDataUtil.put(cursor, "newstring", sValue);
		}else{
			IDataUtil.put(cursor, "newstring", sString);
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


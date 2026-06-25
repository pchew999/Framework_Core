package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:26:57 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.StringTokenizer;
import java.text.*;
import java.util.Date;
// --- <<IS-END-IMPORTS>> ---

public final class jdigits

{
	// ---( internal utility methods )---

	final static jdigits _instance = new jdigits();

	static jdigits _newInstance() { return new jdigits(); }

	static jdigits _cast(Object o) { return (jdigits)o; }

	// ---( server methods )---




	public static final void isDigits (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(isDigits)>> ---
		// @sigtype java 3.5
		// [i] field:0:required istring
		// [o] field:0:required result
		IDataCursor cursor = pipeline.getCursor();
		
		// isDigits - determine if a string contains only 0-9
		//
		//		istring (input,opt) - a string to test
		//
		//		result (output,str) - true/false
		//
		String sString = null;
		boolean result = true;
		
		if (cursor.first("istring")) sString = (String) cursor.getValue();
		if (sString == null) throw new ServiceException("isDigits: 'istring' required");
		
		for (int i = 0; i < sString.length(); i++) {
		    if (!Character.isDigit(sString.charAt(i))) {
		    	result = false;
		    	break;
		    }
		}
		
		//return the result
		IDataUtil.put(cursor, "result", (result) ?"true" :"false");
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


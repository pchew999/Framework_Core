package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jmod

{
	// ---( internal utility methods )---

	final static jmod _instance = new jmod();

	static jmod _newInstance() { return new jmod(); }

	static jmod _cast(Object o) { return (jmod)o; }

	// ---( server methods )---




	public static final void mod (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(mod)>> ---
		// @sigtype java 3.5
		// [i] field:0:required dividend
		// [i] field:0:required divisor
		// [o] field:0:required remainder
		IDataCursor cursor = pipeline.getCursor();
		
		// mod - generate the modulus (remainder) of dividend / divisor
		//
		//		dividend (input,req) - a numeric string dividend
		//		divisor (input,req) - a string to convert
		//
		//		remainder (output,str) - a numeric value representing the modulus (remainder) of dividend / divisor
		//
		String sDividend = null;	//input
		String sDivisor = null;
		
		if (cursor.first("dividend")) sDividend = (String)cursor.getValue();
		if (sDividend == null) throw new ServiceException("mod: Missing input 'dividend'");
		if (!sDividend.matches("[0-9]*")) throw new ServiceException("mod: Dividend not numeric");
		if (cursor.first("divisor")) sDivisor = (String)cursor.getValue();
		if (sDivisor == null) throw new ServiceException("mod: Missing input 'divisor'");
		if (!sDivisor.matches("[0-9]*")) throw new ServiceException("mod: Divisor not numeric");
		
		long rem = Long.parseLong(sDividend) % Long.parseLong(sDivisor);
		
		IDataUtil.put(cursor, "remainder", rem + "");
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


package com.ipaper.wmframe.core.convert;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.net.*;
// --- <<IS-END-IMPORTS>> ---

public final class fromstring

{
	// ---( internal utility methods )---

	final static fromstring _instance = new fromstring();

	static fromstring _newInstance() { return new fromstring(); }

	static fromstring _cast(Object o) { return (fromstring)o; }

	// ---( server methods )---




	public static final void StringToDouble (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(StringToDouble)>> ---
		// @sigtype java 3.5
		// [i] field:0:required input
		// [o] object:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// StringToDouble - convert a string to a 64 bit Double
		//
		//		input (input,req) - string double
		//		output (output,double) - double
		Double fOutput = null;
		String sInput = null;
		if (cursor.first("input")) sInput = (String) cursor.getValue();
		if (sInput == null) throw new ServiceException("StringToDouble: Missing input 'input'");
		
		fOutput = Double.valueOf(sInput);
		IDataUtil.put(cursor, "output", fOutput );
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void StringToInteger (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(StringToInteger)>> ---
		// @sigtype java 3.5
		// [i] field:0:required input
		// [o] object:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// StringToInteger - convert a string to a 32 bit integer
		//
		//		input (input,req) - string integer
		//		output (output,int) - integer
		String sInput = null;
		Integer fOutput = null;
		if (cursor.first("input")) sInput = (String) cursor.getValue();
		if (sInput == null) throw new ServiceException("StringToInteger: Missing input 'input'");
		
		fOutput = Integer.valueOf(sInput);
		IDataUtil.put(cursor, "output", fOutput );
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void StringToLong (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(StringToLong)>> ---
		// @sigtype java 3.5
		// [i] field:0:required input
		// [o] object:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// StringToLong - convert a string to a 64 bit Long
		//
		//		input (input,req) - string long
		//		output (output,long) - long
		Long fOutput = null;
		String sInput = null;
		if (cursor.first("input")) sInput = (String) cursor.getValue();
		if (sInput == null) throw new ServiceException("StringToLong: Missing input 'input'");
		
		fOutput = Long.valueOf(sInput);
		IDataUtil.put(cursor, "output", fOutput );
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


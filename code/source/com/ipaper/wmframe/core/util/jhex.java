package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jhex

{
	// ---( internal utility methods )---

	final static jhex _instance = new jhex();

	static jhex _newInstance() { return new jhex(); }

	static jhex _cast(Object o) { return (jhex)o; }

	// ---( server methods )---




	public static final void toHex (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(toHex)>> ---
		// @sigtype java 3.5
		// [i] object:0:optional bytes
		// [i] field:0:optional string
		// [o] field:0:required hexString
		IDataCursor cursor = pipeline.getCursor();
		
		// toHex - generate a hex representation from either a string or set of bytes
		//
		//		bytes (input,opt) - a set of bytes to convert
		//		string (input,opt) - a string to convert
		//
		//		hexString (output,str) - a string containing the hex value of each byte, ie, "FF 9C 01 64 E7 "
		//
		
		Object oBytes = null;	//input
		byte[] bBytes = null;
		String sString = null;
		
		if (cursor.first("bytes")) oBytes = cursor.getValue();
		if (cursor.first("string")) sString = (String) cursor.getValue();
		
		if (oBytes != null) {
			if (!(oBytes instanceof byte[])) throw new ServiceException("toHex: 'bytes' parameter must be of type byte[]");
			bBytes = (byte[]) oBytes;
		}else if (sString != null) {
			bBytes = sString.getBytes();
		}else{
			throw new ServiceException("toHex: Missing input 'bytes' or 'string'"); 
		}
		
		StringBuilder sb = new StringBuilder();
		for (byte b : bBytes) {
			sb.append(String.format("%02X ", b));
		}
		
		//return the result
		IDataUtil.put(cursor, "hexString", sb.toString());
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


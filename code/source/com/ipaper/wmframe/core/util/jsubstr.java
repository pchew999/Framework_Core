package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:27:35 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.regex.*;
// --- <<IS-END-IMPORTS>> ---

public final class jsubstr

{
	// ---( internal utility methods )---

	final static jsubstr _instance = new jsubstr();

	static jsubstr _newInstance() { return new jsubstr(); }

	static jsubstr _cast(Object o) { return (jsubstr)o; }

	// ---( server methods )---




	public static final void substrTrim (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(substrTrim)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required instring
		// [i] field:0:required offset1
		// [i] field:0:required offset2
		// [o] field:0:required outstring
		IDataCursor cursor = pipeline.getCursor();
		
		// substrTrim - extracts a substring from a string and trims spaces, without all the error nonsense
		//
		//		instring (input,req) - input string
		//		offset1 (input,req) - index where to start extracting
		//		offset2 (input,req) - index where to end extracting
		//
		//		outstring (output,str) - substring or ""
		
		String sInstring = IDataUtil.getString(cursor, "instring");
		String sOff1 = IDataUtil.getString(cursor, "offset1");
		String sOff2 = IDataUtil.getString(cursor, "offset2");
		String sOutstring = "";
		
		int lOff1 = Integer.parseInt(sOff1);
		int lOff2 = Integer.parseInt(sOff2);
		int lLen = sInstring.length();
		if(lOff1<0) lOff1 = lLen;
		if(lOff2<0) lOff2 = lLen;
		if(lOff1>lLen) lOff1 = lLen;
		if(lOff2>lLen) lOff2 = lLen;
		if(lOff2<lOff1) lOff2 = lOff1;
		sOutstring = sInstring.substring(lOff1, lOff2);
		sOutstring = sOutstring.trim();
		
		IDataUtil.put(cursor, "outstring", sOutstring);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


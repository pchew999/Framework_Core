package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.regex.*;
// --- <<IS-END-IMPORTS>> ---

public final class jlastindexof

{
	// ---( internal utility methods )---

	final static jlastindexof _instance = new jlastindexof();

	static jlastindexof _newInstance() { return new jlastindexof(); }

	static jlastindexof _cast(Object o) { return (jlastindexof)o; }

	// ---( server methods )---




	public static final void lastIndexOf (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(lastIndexOf)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required inString
		// [i] field:0:required subString
		// [i] field:0:optional fromIndex
		// [o] field:0:required value
		IDataCursor cursor = pipeline.getCursor();
		
		// lastIndexOf - finds the position of a substring starting from the end
		//
		//		inString (input,req) - input string
		//		subString (input,req) - string to search for
		//		fromIndex (input,req) - index where to start looking
		//
		//		value (output,str) - index position or -1 if not found
		String inString = null;
		String subString = null;
		int fromIndex = -1;
		
		if (cursor.first("inString")) inString = (String)cursor.getValue();
		if (inString == null) throw new ServiceException("lastIndexOf: Missing input 'inString'"); 
		if (cursor.first("subString")) subString = (String)cursor.getValue();
		if (subString == null) throw new ServiceException("lastIndexOf: Missing input 'subString'"); 
		if (cursor.first("fromIndex")) fromIndex = Integer.parseInt((String)cursor.getValue());
		if (fromIndex == -1) fromIndex = inString.length(); 
		
		int pos = inString.lastIndexOf(subString,fromIndex);
		IDataUtil.put(cursor, "value", "" + pos);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


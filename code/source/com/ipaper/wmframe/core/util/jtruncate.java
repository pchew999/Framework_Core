package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
// --- <<IS-END-IMPORTS>> ---

public final class jtruncate

{
	// ---( internal utility methods )---

	final static jtruncate _instance = new jtruncate();

	static jtruncate _newInstance() { return new jtruncate(); }

	static jtruncate _cast(Object o) { return (jtruncate)o; }

	// ---( server methods )---




	public static final void truncateStringlist (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(truncateStringlist)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:1:required inList
		// [i] field:0:required numItems
		// [o] field:1:required outList
		IDataCursor cursor = pipeline.getCursor();
		//
		//  truncateStringlist - service to truncate a string list
		//
		//		inList (input,req) - a string list to truncate
		//		numItems (input,req) - the number of items to truncate the list to
		//
		//		outList (output) - output string list, truncated to numItems
		String[] inList = null;
		String numItems = null;
		if (cursor.first("inList")) inList = IDataUtil.getStringArray( cursor, "inList" );
		if (inList == null) throw new ServiceException("truncateStringlist: Missing input 'inList'");
		if (cursor.first("numItems")) numItems = (String) cursor.getValue();
		if (numItems == null) throw new ServiceException("truncateStringlist: Missing input 'numItems'");
		
		numItems = "" + Integer.min(inList.length, Integer.parseInt(numItems));
		String[] outList = Arrays.copyOf(inList, Integer.parseInt(numItems)); 
		
		IDataUtil.put(cursor, "outList", outList);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


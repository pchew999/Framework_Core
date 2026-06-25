package com.ipaper.wmframe.core.file;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-04-12 11:19:49 CDT
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jdisk

{
	// ---( internal utility methods )---

	final static jdisk _instance = new jdisk();

	static jdisk _newInstance() { return new jdisk(); }

	static jdisk _cast(Object o) { return (jdisk)o; }

	// ---( server methods )---




	public static final void diskSpace (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(diskSpace)>> ---
		// @sigtype java 3.5
		// [i] field:0:required path
		// [o] field:0:required total
		// [o] field:0:required avail
		// [o] field:0:required pctUsed
		IDataCursor cursor = pipeline.getCursor();
		
		// diskSpace - returns the total and usable (free) space on the disk associated with a supplied path
		//
		//		path (input,req) - a fully-qualified path
		//
		//		total (output,string) - the total space on disk in bytes
		//		avail (output,string) - the usable space on disk in bytes
		//		pctUsed (output,string) - the percentage of used space to 1 decimal point
		
		String sPath = null;	//input
		long lTotal = -1;		//output
		long lAvail = -1;		//output
		float fPctUsed = -1;	//output
		
		if (cursor.first("path")) sPath = (String) cursor.getValue();
		if (sPath == null) throw new ServiceException("usableSpace: Missing input 'path'");
		
		try {
		    File oFile = new File(sPath);
		    lTotal = oFile.getTotalSpace();
			lAvail = oFile.getUsableSpace();
			fPctUsed = (float)((lTotal - lAvail) * 100) / (float)lTotal;
		} catch (Exception e) {
		    //throw new ServiceException(e);
		}
		IDataUtil.put(cursor, "total", Long.toString(lTotal));
		IDataUtil.put(cursor, "avail", Long.toString(lAvail));
		IDataUtil.put(cursor, "pctUsed", String.format("%.1f", fPctUsed));
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


package com.ipaper.wmframe.core.file;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jfolder

{
	// ---( internal utility methods )---

	final static jfolder _instance = new jfolder();

	static jfolder _newInstance() { return new jfolder(); }

	static jfolder _cast(Object o) { return (jfolder)o; }

	// ---( server methods )---




	public static final void mkDir (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(mkDir)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required directory
		// [o] field:0:required status
		IDataCursor cursor = pipeline.getCursor();
		
		//  mkDir - create a folder
		//
		//		directory (input,req) - fully qualified path
		//
		//		status (output,str) - true if it was created, false if it already existed 
		
		String sFolder = null;
		boolean status = false;
		
		if (cursor.first("directory")) sFolder = (String) cursor.getValue();
		if (sFolder == null) throw new ServiceException("mkDir: Required input string 'directory' is missing");
		
		File sFolderSpec = new java.io.File(sFolder);
		if (!sFolderSpec.exists()) {
		   status = sFolderSpec.mkdirs();
		}
		IDataUtil.put( cursor, "status", status + "");
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


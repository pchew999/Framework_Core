package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:28:00 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jthread

{
	// ---( internal utility methods )---

	final static jthread _instance = new jthread();

	static jthread _newInstance() { return new jthread(); }

	static jthread _cast(Object o) { return (jthread)o; }

	// ---( server methods )---




	public static final void getThreadInfo (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getThreadInfo)>> ---
		// @sigtype java 3.5
		// [o] field:0:required tid
		// [o] field:0:required tname
		IDataCursor cursor = pipeline.getCursor();
		
		// getThreadInfo - return information about the current thread
		//
		//		no input
		//
		//		tid (output,str) - the current thread id (a simple number)
		//		tname (output,str) - the current name of the thread (a string containing most any character)
		
		long lThreadId = -1;			//output
		String sThreadName = "unknown";
		
		lThreadId = Thread.currentThread().getId();
		sThreadName = Thread.currentThread().getName();
		
		IDataUtil.put(cursor, "tid", Long.toString(lThreadId));
		IDataUtil.put(cursor, "tname", sThreadName);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void setThreadName (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setThreadName)>> ---
		// @sigtype java 3.5
		// [i] field:0:required tname
		IDataCursor cursor = pipeline.getCursor();
		
		// vFileSize - return the size of a file
		//
		//		filename (input,req) - a fully-qualified path and filename
		//
		//		size (output,string) - the size of the file in bytes (-1 if file doesnt exist)
		
		String sName = null;	//input
		
		if (cursor.first("tname")) sName = (String) cursor.getValue();
		if (sName == null) throw new ServiceException("setThreadName: Missing input 'tname'");
		Thread.currentThread().setName(sName);
		
		//try {
		//    File oFile = new File(sFilename);
		//    if ( oFile.exists()) {	// if this check isn't made, the size=0 which could be legitimate
		//    	lSize = oFile.length();
		//    }
		//} catch (Exception e) {
		    //throw new ServiceException(e);
		//}
		//IDataUtil.put(cursor, "tid", Long.toString(lThreadId));
		//IDataUtil.put(cursor, "tname", sThreadName);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


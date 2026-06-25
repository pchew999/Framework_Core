package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:27:16 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.net.*;
// --- <<IS-END-IMPORTS>> ---

public final class jhost

{
	// ---( internal utility methods )---

	final static jhost _instance = new jhost();

	static jhost _newInstance() { return new jhost(); }

	static jhost _cast(Object o) { return (jhost)o; }

	// ---( server methods )---




	public static final void getHostInfo (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getHostInfo)>> ---
		// @sigtype java 3.5
		// [o] field:0:required hostname
		// [o] field:0:required hostipaddrv4
		IDataCursor cursor = pipeline.getCursor();
		
		// getHostInfo - execute the java isReachable call to determine if a server is alive
		//
		//		hostname (output,str) - unknown/error/hostname
		//		hostipaddrv4 (output,str) - unknown/hostipaddrv4
		
		String sHostname = "unknown";
		String sHostipaddrv4 = "unknown";
		
		try {
			sHostname = InetAddress.getLocalHost().getHostName();
			sHostipaddrv4 = InetAddress.getLocalHost().getHostAddress();
		
		} catch (Exception e) {
			sHostname = "error: " + e.toString();
		}
		
		IDataUtil.put(cursor, "hostname", sHostname);
		IDataUtil.put(cursor, "hostipaddrv4", sHostipaddrv4);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


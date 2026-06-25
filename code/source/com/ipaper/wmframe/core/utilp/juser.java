package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:28:04 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.wm.util.Debug;
import java.io.*;
import java.util.*;
import java.lang.System;
import com.wm.app.b2b.server.*;
import com.wm.util.Table;
import java.text.*;
import com.wm.lang.ns.*;
// --- <<IS-END-IMPORTS>> ---

public final class juser

{
	// ---( internal utility methods )---

	final static juser _instance = new juser();

	static juser _newInstance() { return new juser(); }

	static juser _cast(Object o) { return (juser)o; }

	// ---( server methods )---




	public static final void getCurrentUsername (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentUsername)>> ---
		// @sigtype java 3.5
		// [o] field:0:required username
		IDataCursor cursor = pipeline.getCursor();
		
		// getCurrentUsername - return the current IS (or LDAP) username
		//
		//		no input
		//
		//		username (output,str) - the current username as a string
		
		InvokeState is = InvokeState.getCurrentState();
		User user = is.getCurrentUser();
		String sUsername = user.getName();
		
		IDataUtil.put(cursor, "username", sUsername);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


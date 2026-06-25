package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.app.b2b.server.InvokeState;
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jcaller

{
	// ---( internal utility methods )---

	final static jcaller _instance = new jcaller();

	static jcaller _newInstance() { return new jcaller(); }

	static jcaller _cast(Object o) { return (jcaller)o; }

	// ---( server methods )---




	public static final void getCallStack (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCallStack)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional excludeme {"false","true"}
		// [i] field:0:optional nameonly {"false","true"}
		// [o] field:0:required stack
		// [o] record:1:required callStack
		IDataCursor cursor = pipeline.getCursor();
		//
		// getCallStack - return the current stack of flow service names, from wherever this call is made 
		//
		//		excludeme (input,opt) - true/false (to exclude your service from the call stack output - default=false)
		//		nameonly (input,opt) - true/false (to return only service names without the full paths - default=false)
		//
		//		stack (output,str) - a simple string of the stack, delimited by * (good for a single-line log)
		//		callStack (output,doclist) - an actual call stack that looks like that found in lastError
		//
		String sName = null;
		String sStack = "";
		int i,lMax,lPos;
		String sDelim = "*";
		String sExcludeme = "false";
		String sNameonly = "false";
		
		if (cursor.first("excludeme")) sExcludeme = (String) cursor.getValue();
		if (cursor.first("nameonly")) sNameonly = (String) cursor.getValue();
		
		java.util.Stack callStack = com.wm.app.b2b.server.InvokeState.getCurrentState().getCallStack();
		lMax = callStack.size() - 2;	//always exclude getCallStack (this service) from what gets returned
		if (sExcludeme.equals("true")) lMax--;
		IData[] dlStack = new IData[(lMax>=0)?lMax+1:0]; 
		
		for( i = 0; i <= lMax; i++) {
			com.wm.lang.ns.NSService myService = (com.wm.lang.ns.NSService) callStack.elementAt(i);
			sName = myService.getNSName().getFullName();
			if (sNameonly.equals("true")) {
				lPos = sName.indexOf(':');
				if (lPos > -1) sName = sName.substring(lPos + 1);
			}
			if (i == lMax) sDelim = "";	// leave off the final *
			sStack = sStack + sName + sDelim;
		
			IData tempID = IDataFactory.create(); 
			IDataCursor tempIDC = tempID.getCursor(); 
			IDataUtil.put(tempIDC, "service", sName); 
			IDataUtil.put(tempIDC, "flowStep", "/0");	//dont know how to find the flow step
			tempIDC.destroy(); 
			dlStack[lMax-i] = tempID; 
		}
		
		//com.wm.app.b2b.server.InvokeState is = com.wm.app.b2b.server.InvokeState.getCurrentState();
		//com.wm.app.b2b.server.InvokeState.getCurrentState();
		//com.wm.app.b2b.server.User user = InvokeState.getCurrentUser();
		String sUser = InvokeState.getCurrentUser().getName();
		
		if (sStack.equals("")) sStack = "debug*" + sUser;	//in debug mode, a stack doesnt exist apparently
		
		IDataUtil.put(cursor, "stack", sStack);
		IDataUtil.put(cursor, "callStack", dlStack);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getCallingService (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCallingService)>> ---
		// @sigtype java 3.5
		// [o] field:0:required callingService
		String sName = "NotFound";
		java.util.Stack callStack = com.wm.app.b2b.server.InvokeState.getCurrentState().getCallStack();
		int lSize = callStack.size();
		
		if (lSize >= 3) {
		 com.wm.lang.ns.NSService myService = (com.wm.lang.ns.NSService) callStack.elementAt (lSize - 3);
		 sName = myService.getNSName().getFullName();
		}
		
		// pipeline out
		IDataCursor iCursor = pipeline.getCursor();
		IDataUtil.put(iCursor, "callingService", sName);
		iCursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


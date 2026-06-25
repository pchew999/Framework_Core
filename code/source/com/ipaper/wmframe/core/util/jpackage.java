package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.lang.ns.*;
// --- <<IS-END-IMPORTS>> ---

public final class jpackage

{
	// ---( internal utility methods )---

	final static jpackage _instance = new jpackage();

	static jpackage _newInstance() { return new jpackage(); }

	static jpackage _cast(Object o) { return (jpackage)o; }

	// ---( server methods )---




	public static final void getCurrentPackage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentPackage)>> ---
		// @sigtype java 3.5
		// [o] field:0:required packageName
		String sPackageName = "unknown";
		
		NSService callingServiceName = Service.getCallingService();
		//NSName serviceNSNode = callingServiceName.getNSName();
		//String serviceName = serviceNSNode.getFullName();
		NSPackage nsNodePackageName = callingServiceName.getPackage();
		sPackageName = nsNodePackageName.getName();
		
		IDataCursor iCursor = pipeline.getCursor();
		//IDataUtil.put(iCursor, "serviceName", serviceName);
		IDataUtil.put(iCursor, "packageName", sPackageName);
		iCursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


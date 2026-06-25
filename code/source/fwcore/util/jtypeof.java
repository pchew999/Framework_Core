package fwcore.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jtypeof

{
	// ---( internal utility methods )---

	final static jtypeof _instance = new jtypeof();

	static jtypeof _newInstance() { return new jtypeof(); }

	static jtypeof _cast(Object o) { return (jtypeof)o; }

	// ---( server methods )---




	public static final void typeOf (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(typeOf)>> ---
		// @sigtype java 3.5
		// [i] object:0:required object
		// [o] field:0:required objtype
		IDataCursor cursor = pipeline.getCursor();
		
		// typeOf - return the type of an input object
		//
		//		object (input) - an object to test
		//
		//		objtype (output,str) - a string containing either 'FWNULL' or 'java.lang.String' or 'com.wm.net.mime.MimeData' or 'java.io.ByteArrayInputStream'
		//
		
		Object oObj = IDataUtil.get(cursor, "object");	//get the input
		if(oObj == null) {
			IDataUtil.put(cursor, "objtype", "FWNULL");	//special value representing null
		}else{
			//if(oObj instanceof IData) IDataUtil.put(cursor, "objtype", "itsa-idata");
			//if(oObj instanceof String) IDataUtil.put(cursor, "objtype", "itsa-string");
			//if(oObj instanceof byte[]) IDataUtil.put(cursor, "objtype", "itsa-bytearray");
			//IDataUtil.put(cursor, "objtype", oObj.toString());
			IDataUtil.put(cursor, "objtype", oObj.getClass().getName());
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


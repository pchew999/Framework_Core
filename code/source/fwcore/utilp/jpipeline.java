package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;
// --- <<IS-END-IMPORTS>> ---

public final class jpipeline

{
	// ---( internal utility methods )---

	final static jpipeline _instance = new jpipeline();

	static jpipeline _newInstance() { return new jpipeline(); }

	static jpipeline _cast(Object o) { return (jpipeline)o; }

	// ---( server methods )---




	public static final void putString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(putString)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required name
		// [i] field:0:required value
		IDataCursor cursor = pipeline.getCursor();
		//
		//  putString - service to create a string variable in the pipeline based on input
		//
		//		name (input,req) - the name of the output string
		//		value (input,req) - the value assigned to the named output string
		//
		//		<name> (output,str) - a named string containing the value
		//
		//input
		String name=null;
		String value=null;
		if (cursor.first("name")) name = (String) cursor.getValue();
		if (cursor.first("value")) value = (String) cursor.getValue();
		if (name==null || name.length()==0) throw new ServiceException("putString: Missing input 'name'"); 
		if (value==null || value.length()==0) throw new ServiceException("putString: Missing input 'value'"); 
		
		IDataUtil.put(cursor, name, value);
		
		cursor.destroy();
			
		// --- <<IS-END>> ---

                
	}
}


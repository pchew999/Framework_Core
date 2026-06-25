package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class jhextobytes

{
	// ---( internal utility methods )---

	final static jhextobytes _instance = new jhextobytes();

	static jhextobytes _newInstance() { return new jhextobytes(); }

	static jhextobytes _cast(Object o) { return (jhextobytes)o; }

	// ---( server methods )---




	public static final void hexToBytes (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(hexToBytes)>> ---
		// @sigtype java 3.5
		// [i] field:0:required hexString
		// [o] object:0:required bytes
		IDataCursor cursor = pipeline.getCursor();
		
				
		String sString = null;
				
		if (cursor.first("hexString")) sString = (String) cursor.getValue();
		
		if (sString == null || sString.length() == 0)
		{
			
			throw new ServiceException("Hex String Parameter is missing");
		}
		
		    byte[] bytes = new byte[sString.length() / 2];
		    for (int i = 0; i < sString.length(); i += 2) {
		        bytes[i / 2] = (byte) ((Character.digit(sString.charAt(i), 16) << 4) + Character.digit(sString.charAt(i + 1), 16));
		    }
		  
		   
			//IDataUtil.put(cursor, "newSTring", bytes.toString());
		    IDataUtil.put(cursor, "bytes", bytes);
		
		 
			
			cursor.destroy();
		
			
		// --- <<IS-END>> ---

                
	}
}


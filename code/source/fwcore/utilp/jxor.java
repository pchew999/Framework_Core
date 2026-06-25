package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jxor

{
	// ---( internal utility methods )---

	final static jxor _instance = new jxor();

	static jxor _newInstance() { return new jxor(); }

	static jxor _cast(Object o) { return (jxor)o; }

	// ---( server methods )---




	public static final void xor (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(xor)>> ---
		// @sigtype java 3.5
		// [i] object:0:required instream
		// [i] object:0:required xorbytes
		// [o] object:0:required outbytes
		IDataCursor cursor = pipeline.getCursor();
		
		// xor - xor an input stream of bytes with a set of supplied bytes
		//
		//		instream (input,req) - a stream of bytes to xor
		//		xorbytes (input,req) - a set of bytes to xor with (limited to 512 bytes)
		//
		//		outbytes (output,bytes) - the output that has been xor'ed
		//
		Object oInstream = null;	//input
		Object oXorbytes = null;	//input
		byte[] bXorBytes = null;	//input
		
		if (cursor.first("instream")) oInstream = cursor.getValue();
		if (oInstream == null) throw new ServiceException("xor: Missing input stream 'instream'"); 
		if (!(oInstream instanceof InputStream)) throw new ServiceException("xor: instream parameter must be of type InputStream");
		InputStream IS = (InputStream)oInstream;
		if (cursor.first("xorbytes")) oXorbytes = cursor.getValue();
		if (oXorbytes == null) throw new ServiceException("xor: Missing input 'xorbytes'"); 
		if (!(oXorbytes instanceof byte[])) throw new ServiceException("xor: xorbytes parameter must be of type byte[]");
		bXorBytes = (byte[]) oXorbytes;
		if (bXorBytes.length > 512) throw new ServiceException("xor: xorbytes length must be <= 512");
		
		byte[] buf = new byte[512];
		int lActuallyRead = -1;
		int lToGet = bXorBytes.length;
		
		ByteArrayOutputStream BOS = new ByteArrayOutputStream();	//output
		byte[] outbuf = null;										//output
		
		try {
			while ((lActuallyRead = IS.read(buf, 0, lToGet)) != -1) {
				for(int i = 0; i < lActuallyRead; i++) {
					buf[i] = (byte)( buf[i] ^ bXorBytes[i] );	//XOR
				}
				BOS.write(buf, 0, lActuallyRead);
			}
			BOS.flush();
			outbuf = BOS.toByteArray();
		}catch(Exception e) {
			throw new ServiceException("xor error: " + e.toString());
		}
		
		IDataUtil.put(cursor, "outbytes", outbuf);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


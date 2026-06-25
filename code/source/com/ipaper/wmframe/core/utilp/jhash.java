package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:27:42 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jhash

{
	// ---( internal utility methods )---

	final static jhash _instance = new jhash();

	static jhash _newInstance() { return new jhash(); }

	static jhash _cast(Object o) { return (jhash)o; }

	// ---( server methods )---




	public static final void genFullHash (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(genFullHash)>> ---
		// @sigtype java 3.5
		// [i] field:0:required filename
		// [o] field:0:required hash1
		// [o] field:0:required hash2
		IDataCursor cursor = pipeline.getCursor();
		
		// genFullHash - generate two hash values from the supplied file
		//
		//		filename (input,req) - a fully qualified filename of the file to hash
		//
		//		hash1 (output,str) - hash value 1 (from Zobel/Ramakrishna algorithm)
		//		hash2 (output,str) - hash value 2 (from magic 33 algorithm)
		//
		String filename = null;
		FileInputStream oFIS;
		int lBytesread = -1;
		byte bytebuffer[] = new byte[16000];
		int lHash1 = 31;				//random starting value
		long unsignedHash2 = 0xEB639D;	//random set of starter bits
		int lChar = 0;
		
		if (cursor.first("filename")) filename = (String) cursor.getValue();
		if (filename == null) throw new ServiceException("streamOpenFile: Missing input 'filename'");
		
		try {
		
			oFIS = new FileInputStream(filename);
		
			lBytesread = oFIS.read(bytebuffer);
			while (lBytesread != -1) {
		
				//generate the first hash using the Zobel/Ramakrishna algorithm
				int lModulus1 = 0x2000000;   //one followed by twenty-five zero bits
				for (int i = 0; i < lBytesread; i++) {
					lChar = (int) bytebuffer[i];
				  //lHash1 = (lHash1 Xor ((lHash1 * 32) + (lHash1 \ 4) + lChar)) And (lModulus1 - 1) -- from the VB version
					lHash1 = (lHash1 ^ ((lHash1 * 32) + (lHash1 / (int)4) + lChar)) & (lModulus1 - 1);
				}
				
				//generate the second hash using the magic 33 algorithm
				unsignedHash2 = unsignedHash2 & 0xFFFFFFFFl;	// if unsignedHash2 happens to be "negative", this will not sign extend to long
				for (int i = 0; i < lBytesread; i++) {
					lChar = (int) bytebuffer[i] & 0xFF;
					//unsignedHash2 = ((unsignedHash2 << 5) + unsignedHash2) + lChar;	// faster, but less understandable
					unsignedHash2 = (unsignedHash2 * 33) + lChar;						// easier to understand
					unsignedHash2 = unsignedHash2 & 0xFFFFFFFFl;						// block any possible sign extension
				}
		
				lBytesread = oFIS.read(bytebuffer);
			}
		
			oFIS.close();
		
		} catch (IOException e) {
		  throw new ServiceException(e);
		}
		
		 //return the results
		IDataUtil.put(cursor, "hash1", Integer.toString(lHash1));
		IDataUtil.put(cursor, "hash2", Long.toString(unsignedHash2));
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void genIterativeHash (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(genIterativeHash)>> ---
		// @sigtype java 3.5
		// [i] object:0:required bytes
		// [i] field:0:required hash1
		// [i] field:0:required hash2
		// [o] field:0:required hash1
		// [o] field:0:required hash2
		IDataCursor cursor = pipeline.getCursor();
		
		// genIterativeHash - generate two hash values iteratively from a given set of bytes
		//
		//		bytes (input,req) - a set of bytes to hash
		//		hash1 (input,opt) - use null to start - supply prior hash when iterating
		//		hash2 (input,opt) - use null to start - supply prior hash when iterating
		//
		//		hash1 (output,str) - hash value 1 (from Zobel/Ramakrishna algorithm)
		//		hash2 (output,str) - hash value 2 (from magic 33 algorithm)
		//
		//	in order to iterate, make an initial call with hash1 and hash2 set to null - then supply 
		//	the output hash values from one call as input to the next call
		
		Object oBytes = null;	//input
		String sHash1 = null;	//input
		String sHash2 = null;	//input
		byte[] bBytes = null;
		int lChar = 0;
		int lHash1 = 0;
		long unsignedHash2 = 0;
		
		if (cursor.first("bytes")) oBytes = cursor.getValue();
		if (oBytes == null) throw new ServiceException("genIterativeHash: Missing input 'bytes'"); 
		if (!(oBytes instanceof byte[])) throw new ServiceException("genIterativeHash: bytes parameter must be of type byte[]");
		bBytes = (byte[]) oBytes;
		if (cursor.first("hash1")) sHash1 = (String) cursor.getValue();
		if (cursor.first("hash2")) sHash2 = (String) cursor.getValue();
		
		//generate the first hash using the Zobel/Ramakrishna algorithm
		lHash1 = (sHash1 == null) ? 31 : Integer.parseInt(sHash1); //random starting value
		int lModulus1 = 0x2000000;   //one followed by twenty-five zero bits
		for (int i = 0; i < bBytes.length; i++) {
			lChar = (int) bBytes[i];
		  //lHash1 = (lHash1 Xor ((lHash1 * 32) + (lHash1 \ 4) + lChar)) And (lModulus1 - 1) -- from the VB version
			lHash1 = (lHash1 ^ ((lHash1 * 32) + (lHash1 / (int)4) + lChar)) & (lModulus1 - 1);
		}
		
		//generate the second hash using the magic 33 algorithm
		unsignedHash2 = (sHash2 == null) ? 0xEB639D : Long.parseLong(sHash2);	//random set of starter bits
		unsignedHash2 = unsignedHash2 & 0xFFFFFFFFl;	// if unsignedHash2 happens to be "negative", this will not sign extend to long
		for (int i = 0; i < bBytes.length; i++) {
			lChar = (int) bBytes[i] & 0xFF;
			//unsignedHash2 = ((unsignedHash2 << 5) + unsignedHash2) + lChar;	// faster, but less understandable
			unsignedHash2 = (unsignedHash2 * 33) + lChar;						// easier to understand
			unsignedHash2 = unsignedHash2 & 0xFFFFFFFFl;						// block any possible sign extension
		}
		
		 //return the results
		IDataUtil.put(cursor, "hash1", Integer.toString(lHash1));
		IDataUtil.put(cursor, "hash2", Long.toString(unsignedHash2));
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


package com.ipaper.wmframe.core.file;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jfile

{
	// ---( internal utility methods )---

	final static jfile _instance = new jfile();

	static jfile _newInstance() { return new jfile(); }

	static jfile _cast(Object o) { return (jfile)o; }

	// ---( server methods )---




	public static final void fileCount (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(fileCount)>> ---
		// @sigtype java 3.5
		// [i] field:0:required directory
		// [o] field:0:required numFiles
		IDataCursor cursor = pipeline.getCursor();
		
		// fileCount - get a count of the files and subdirectories in a folder
		//
		//	directory (input,req) - fully-qualified directory
		//	numFiles (output,str) - the number of files AND folders that are found
		
		String sDirectory = null;
		if (cursor.first("directory")) sDirectory = (String) cursor.getValue();
		if (sDirectory == null) throw new ServiceException("fileCount: Missing input 'directory'");
		
		int count = new File(sDirectory).list().length;
		IDataUtil.put(cursor, "numFiles", "" + count);
		
		cursor.destroy();		
		// --- <<IS-END>> ---

                
	}



	public static final void fileDelete (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(fileDelete)>> ---
		// @sigtype java 3.5
		// [i] field:0:required filename
		// [o] field:0:required status
		IDataCursor cursor = pipeline.getCursor();
		
		// fileDelete - delete a file
		//
		//		filename (input,req) - fully-qualified source path and name of file
		//
		//		status (output,string) - true if the delete succeeded
		//								 false if the delete failed
		
		String sFilename = null;	//input
		
		if (cursor.first("filename")) sFilename = (String) cursor.getValue();
		if (sFilename == null) throw new ServiceException("fileDelete: Missing input 'filename'");
		
		File oFile = new File(sFilename);
		boolean status = oFile.delete();
		IDataUtil.put( cursor, "status", "" + status);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void fileExists (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(fileExists)>> ---
		// @sigtype java 3.5
		// [i] field:0:required pathFile
		// [o] field:0:required exists
		IDataCursor cursor = pipeline.getCursor();
		
		//  fileExists - check if a file exists on the file system
		//
		//		pathFile (input,req) - fully qualified path and filename
		//
		//		exists (output,str) - true or false
		
		String sPathfile = null;
		
		if (cursor.first("pathFile")) sPathfile = (String) cursor.getValue();
		if (sPathfile == null) throw new ServiceException("fileExists: Required input string 'pathFile' is missing");
		
		java.io.File fpTest = new File(sPathfile);
		if (fpTest.exists() && fpTest.isFile() ) {
			IDataUtil.put( cursor, "exists", "true");
		}else{
			IDataUtil.put( cursor, "exists", "false");
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void fileRename (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(fileRename)>> ---
		// @sigtype java 3.5
		// [i] field:0:required fromFilename
		// [i] field:0:required toFilename
		// [o] field:0:required status
		IDataCursor cursor = pipeline.getCursor();
		
		// fileRename - rename a file - also allows moving the file to another folder
		//
		//		fromFilename (input,req) - fully-qualified source path and name of file
		//		toFilename (input,req) - fully-qualified target path and name of file
		//
		//		status (output,string) - true if the rename succeeded
		//								 false if the rename failed
		
		String sFromFilename = null;	//input
		String sToFilename = null;		//input
		
		if (cursor.first("fromFilename")) sFromFilename = (String) cursor.getValue();
		if (sFromFilename == null) throw new ServiceException("fileRename: Missing input 'fromFilename'");
		if (cursor.first("toFilename")) sToFilename = (String) cursor.getValue();
		if (sToFilename == null) throw new ServiceException("fileRename: Missing input 'toFilename'");
		
		File oFromFile = new File(sFromFilename);
		File oToFile = new File(sToFilename);
		boolean status = oFromFile.renameTo(oToFile);
		Boolean bObj = new Boolean(status); //this is what is so wrong with java
		IDataUtil.put( cursor, "status", bObj.toString());
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void fileSize (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(fileSize)>> ---
		// @sigtype java 3.5
		// [i] field:0:required filename
		// [o] field:0:required size
		IDataCursor cursor = pipeline.getCursor();
		
		// fileSize - return the size of a file
		//
		//		filename (input,req) - a fully-qualified path and filename
		//
		//		size (output,string) - the size of the file in bytes (-1 if file doesnt exist)
		
		String sFilename = null;	//input
		long lSize = -1;			//output
		
		if (cursor.first("filename")) sFilename = (String) cursor.getValue();
		if (sFilename == null) throw new ServiceException("fileSize: Missing input 'filename'");
		
		try {
		    File oFile = new File(sFilename);
		    if ( oFile.exists()) {	// if this check isn't made, the size=0 which could be legitimate
		    	lSize = oFile.length();
		    }
		} catch (Exception e) {
		    //throw new ServiceException(e);
		}
		IDataUtil.put(cursor, "size", Long.toString(lSize));
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void streamClose (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamClose)>> ---
		// @sigtype java 3.5
		// [i] object:0:required stream
		IDataCursor cursor = pipeline.getCursor();
		
		// streamClose - close a stream
		//
		//		stream (input,req) - an already open stream
		
		Object oStream = null;			//input
		
		if (cursor.first("stream")) oStream = cursor.getValue();
		if (oStream == null) throw new ServiceException("streamClose: Missing input 'stream'");
		
		try {
		    if (oStream instanceof InputStream) {
		    	InputStream is = (InputStream) oStream;
				is.close();
		    } else if (oStream instanceof OutputStream) {
				OutputStream os = (OutputStream) oStream;
				os.flush();
				os.close();
		    }else{
		    	throw new ServiceException("streamClose: Unable to interpret what 'stream' is");
		    }
		} catch (IOException ioe) {
		  throw new ServiceException(ioe);
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void streamOpenFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamOpenFile)>> ---
		// @sigtype java 3.5
		// [i] field:0:required filename
		// [i] field:0:required openFor {"input","output","append"}
		// [o] object:0:required stream
		IDataCursor cursor = pipeline.getCursor();
		
		// streamOpenFile - open a file for either reading or writing as a stream
		//
		//		filename (input,req) - a fully-qualified path and filename
		//		openFor (input,req) - "input", "output" or "append"
		//
		//		stream (output,object) - the stream
		
		String filename = null;
		String openFor = null;
		
		if (cursor.first("filename")) filename = (String) cursor.getValue();
		if (filename == null) throw new ServiceException("streamOpenFile: Missing input 'filename'");
		if (cursor.first("openFor"))  openFor = (String) cursor.getValue();
		if (openFor == null) throw new ServiceException("streamOpenFile: Missing input 'openFor'");
		
		try {
			if ("input".equals(openFor)) {
				IDataUtil.put(cursor, "stream", new FileInputStream(filename));
			} else if ("output".equals(openFor)) {
				IDataUtil.put(cursor, "stream", new FileOutputStream(filename, false));
			} else if ("append".equals(openFor)) {
				IDataUtil.put(cursor, "stream", new FileOutputStream(filename, true));
			} else {
				throw new ServiceException("streamOpenFile: Invalid parameter openFor=" + openFor);
			}
		} catch (IOException ioe) {
		    throw new ServiceException(ioe);
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void streamReadBytes (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamReadBytes)>> ---
		// @sigtype java 3.5
		// [i] object:0:required stream
		// [i] field:0:required bytesrequested
		// [o] object:0:required bytes
		// [o] field:0:required bytesread
		// [o] field:0:required eos
		IDataCursor cursor = pipeline.getCursor();
		
		// streamReadBytes - read a given number of bytes from an open stream (LIMITED TO 2G)
		//
		//		stream (input,req) - an already open stream
		//		bytesrequested (input,req) - the maximum number of bytes to read (limit 2,147,483,647 bytes or 2G)
		//
		//		bytes (output,object) - the bytes that were read from the stream
		//		bytesread (output,string) - the number of bytes that were read
		//		eos - 'true' if end of stream has been reached, 'false' otherwise
		
		Object oInputStream = null;			//input
		String sBytesrequested = null;		//input
		int lBytesrequested = -1;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();	//output
		int lBytesread = 0;													//output
		String eos = "false";												//output
		
		if (cursor.first("stream")) oInputStream = cursor.getValue();
		if (oInputStream == null) throw new ServiceException("streamReadBytes: Missing input 'stream'");
		if (cursor.first("bytesrequested")) sBytesrequested = (String) cursor.getValue();
		if (sBytesrequested == null) throw new ServiceException("streamReadBytes: Missing input 'bytesrequested'");
		
		ByteArrayInputStream input1 = null;
		BufferedInputStream input2 = null;
		InputStream input3 = null;
		
		byte[] buf = new byte[16384];
		int lToGet = 0;
		int lActuallyRead = 0;
		
		try {
			lBytesrequested = Integer.parseInt(sBytesrequested);
		
			if (oInputStream instanceof byte[]) {
			    input1 = new ByteArrayInputStream((byte[]) oInputStream);
			    for (;;) {
			    	lToGet = buf.length; if(lBytesrequested < buf.length) lToGet = lBytesrequested;
			    	lActuallyRead = input1.read(buf, 0, lToGet);
			    	if(lActuallyRead < 0) {eos = "true"; break;}
			    	outputStream.write(buf, 0, lActuallyRead);
			    	lBytesread += lActuallyRead;
			    	lBytesrequested -= lActuallyRead;
			    	if(lBytesrequested <= 0) break;
		    	}
			} else if (oInputStream instanceof ByteArrayInputStream) {
			    input1 = (ByteArrayInputStream) oInputStream;
			    for (;;) {
			    	lToGet = buf.length; if(lBytesrequested < buf.length) lToGet = lBytesrequested;
			    	lActuallyRead = input1.read(buf, 0, lToGet);
			    	if(lActuallyRead < 0) {eos = "true"; break;}
			    	outputStream.write(buf, 0, lActuallyRead);
			    	lBytesread += lActuallyRead;
			    	lBytesrequested -= lActuallyRead;
			    	if(lBytesrequested <= 0) break;
		    	}
			} else if (oInputStream instanceof BufferedInputStream) {
			    input2 = (BufferedInputStream) oInputStream;
			    for (;;) {
			    	lToGet = buf.length; if(lBytesrequested < buf.length) lToGet = lBytesrequested;
			    	lActuallyRead = input2.read(buf, 0, lToGet);
			    	if(lActuallyRead < 0) {eos = "true"; break;}
			    	outputStream.write(buf, 0, lActuallyRead);
			    	lBytesread += lActuallyRead;
			    	lBytesrequested -= lActuallyRead;
			    	if(lBytesrequested <= 0) break;
		    	}
			} else if (oInputStream instanceof InputStream) {
			    input3 = (InputStream) oInputStream;
			    for (;;) {
			    	lToGet = buf.length; if(lBytesrequested < buf.length) lToGet = lBytesrequested;
			    	lActuallyRead = input3.read(buf, 0, lToGet);
			    	if(lActuallyRead < 0) {eos = "true"; break;}
			    	outputStream.write(buf, 0, lActuallyRead);
			    	lBytesread += lActuallyRead;
			    	lBytesrequested -= lActuallyRead;
			    	if(lBytesrequested <= 0) break;
		    	}
			} else {
				throw new ServiceException("streamReadBytes: Unable to interpret what 'stream' is");
			}
			IDataUtil.put(cursor, "bytes", outputStream.toByteArray());	//output
			IDataUtil.put(cursor, "eos", eos);							//output
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		IDataUtil.put(cursor, "bytesread", String.valueOf(lBytesread));	//output
		
		cursor.destroy();	
		// --- <<IS-END>> ---

                
	}



	public static final void streamReadLine (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamReadLine)>> ---
		// @sigtype java 3.5
		// [i] object:0:required stream
		// [o] field:0:required line
		IDataCursor cursor = pipeline.getCursor();
		
		// streamReadLine - read a complete line from an open stream (assumed to be standard text)
		//
		//		stream (input,req) - an already open stream
		//
		//		line (output,string) - the next line in the file (delimited by \n) - at end-of-stream, a null will be returned
		
		Object oStream = null;			//input
		InputStream input3 = null;
		byte[] b = new byte[1];
		String sOut = null;
		long lBytesread = -1;
		char c;
		
		if (cursor.first("stream")) oStream = cursor.getValue();
		if (oStream == null) throw new ServiceException("streamReadLine: Missing input 'stream'");
		if (!(oStream instanceof InputStream)) throw new ServiceException("streamReadLine: Unsupported stream type 'stream'");
		
		try {
			input3 = (InputStream) oStream;
			lBytesread = input3.read(b);
			if(lBytesread == -1) {						//read the first available byte from the stream
				IDataUtil.put(cursor, "line", null);	//if none found, this is the true end of file, so return null
			}else{
				sOut = "";								//otherwise we have to return something
				while(lBytesread != -1) {
					if (b[0] == 10) break;				//stop reading when we find an LF (or end of file)
					c = (char)b[0];
					sOut = sOut + c;
					lBytesread = input3.read(b);
				}
				IDataUtil.put(cursor, "line", sOut);	//output
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		cursor.destroy();	
		// --- <<IS-END>> ---

                
	}



	public static final void streamSkip (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamSkip)>> ---
		// @sigtype java 3.5
		// [i] object:0:required stream
		// [i] field:0:required bytesToSkip
		IDataCursor cursor = pipeline.getCursor();
		
		// streamSkip - skip a given number of bytes from an open stream
		//
		//		stream (input,req) - an already open stream
		//		bytesToSkip (input,req) - the number of bytes to skip ahead in the stream
		//
		
		Object oStream = null;			//input
		String sSkip = null;
		InputStream input3 = null;
		long lPos = 0;
		
		if (cursor.first("stream")) oStream = cursor.getValue();
		if (oStream == null) throw new ServiceException("streamSkip: Missing input 'stream'");
		if (!(oStream instanceof InputStream)) throw new ServiceException("streamSkip: Unsupported stream type 'stream'");
		if (cursor.first("bytesToSkip")) sSkip = (String)cursor.getValue();
		if (sSkip == null) throw new ServiceException("streamSkip: Missing input 'sSkip'");
		
		try {
			input3 = (InputStream) oStream;
			lPos = input3.skip(Long.parseLong(sSkip));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		cursor.destroy();	
		// --- <<IS-END>> ---

                
	}



	public static final void streamWriteBytes (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamWriteBytes)>> ---
		// @sigtype java 3.5
		// [i] object:0:required stream
		// [i] object:0:required bytes
		// [o] field:0:required byteswritten
		IDataCursor cursor = pipeline.getCursor();
		
		// streamWriteBytes - write a given number of bytes to an open stream
		//
		//		stream (input,req) - an already open stream
		//		bytes (input,req) - the bytes to write
		//
		//		byteswritten (output) - the number of bytes written
		
		Object oStream = null;	//input
		Object oBytes = null;	//input
		OutputStream os = null;
		byte[] bBytes = null;
		String sLen = "-1";		//output
		
		if (cursor.first("stream")) oStream = cursor.getValue();
		if (oStream == null) throw new ServiceException("streamWriteBytes: Missing input 'stream'");
		if (cursor.first("bytes")) oBytes = cursor.getValue();
		if (oBytes == null) throw new ServiceException("streamWriteBytes: Missing input 'bytes'"); 
		if (!(oBytes instanceof byte[])) throw new ServiceException("streamWriteBytes: bytes parameter must be of type byte[]");
		
		try {
			os = (OutputStream) oStream;
			bBytes = (byte[]) oBytes;
			sLen = "" + bBytes.length;
			os.write(bBytes);
			os.flush();
		} catch (IOException ioe) {
			throw new ServiceException(ioe);
		}
		IDataUtil.put(cursor, "byteswritten", sLen);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void streamWriteString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(streamWriteString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required stream
		// [i] field:0:required string
		// [o] field:0:required byteswritten
		IDataCursor cursor = pipeline.getCursor();
		
		// streamWriteString - write a string to an open stream
		//
		//		stream (input,req) - an already open stream
		//		string (input,req) - the string to write
		//
		//		byteswritten (output) - the number of bytes written
		
		Object oStream = null;	//input
		String sString = null;	//input
		
		Writer writer = null;
		
		if (cursor.first("stream")) oStream = cursor.getValue();
		if (oStream == null) throw new ServiceException("streamWriteString: Missing input 'stream'");
		
		if (cursor.first("string")) sString = (String) cursor.getValue();
		if (sString == null) throw new ServiceException("streamWriteString: Missing input 'string'");
		
		try {
			System.out.println("[DEBUG] stream type = " + oStream.getClass().getName());
			System.out.println("[DEBUG] stream sSring = " + sString);
			FileOutputStream fos = (FileOutputStream) oStream;
			long pos1 = fos.getChannel().position();					//get the file position before writing the string
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			writer = new BufferedWriter(osw);
			writer.write(sString);
			writer.flush();
			long pos2 = fos.getChannel().position();					//get the file position after writing the string
			IDataUtil.put(cursor, "byteswritten", "" + (pos2 - pos1));	//return the actual number of bytes written
		} catch (IOException ioe) {
			throw new ServiceException(ioe.toString() + "[DEBUG] stream type = " + oStream.getClass().getName());
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


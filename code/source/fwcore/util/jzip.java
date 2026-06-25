package fwcore.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.lang.*;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
// --- <<IS-END-IMPORTS>> ---

public final class jzip

{
	// ---( internal utility methods )---

	final static jzip _instance = new jzip();

	static jzip _newInstance() { return new jzip(); }

	static jzip _cast(Object o) { return (jzip)o; }

	// ---( server methods )---




	public static final void unzip (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(unzip)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:optional zipBytes
		// [i] field:0:required returnAs {"string","bytes","file"}
		// [i] field:0:optional zipFileName
		// [i] field:0:optional unZipFileName
		// [o] field:0:optional string
		// [o] object:0:optional bytes
		// [o] field:0:required message
		IDataCursor cursor = pipeline.getCursor();
		
		// unzip - this service will unzip a set of bytes looking only for the first zip entry (subsequent entries will be ignored)
		//
		//		zipBytes (input,opt) - the set of zipped bytes to unzip (or zipFileName)
		//		returnAs (input,req) - either string, bytes or file to indicate how to return the unzipped data
		//		zipFileName (input,opt) - string of the full path/filename of the input file to unzip (or zipBytes)
		//		unZipFileName (input,opt) - string of the full path/filename of the file to unzip to (when returnAs is 'file')
		//
		//		string (output,string) - unzipped string from either zipBytes or zipFileName
		//		bytes (output,bytes) - unzipped bytes from either zipBytes or zipFileName
		//		message (output,string) - informational message
		//
		//		Added GZIP functionality to the service
		
		//input
		Object oZipBytes = null;
		String sReturnAs = null;
		String sZipFileName  = null;
		String sUnZipFileName = null;
		
		if (cursor.first("zipBytes")) oZipBytes = cursor.getValue();
		if (cursor.first("zipFileName"))   sZipFileName = (String) cursor.getValue();
		if (cursor.first("unZipFileName"))  sUnZipFileName  = (String) cursor.getValue();
		if (cursor.first("returnAs")) sReturnAs = (String) cursor.getValue();
		if (sReturnAs == null) throw new ServiceException("unzip: Missing input 'returnAs'"); 
		if ( !sReturnAs.equals("bytes") && !sReturnAs.equals("string") && !sReturnAs.equals("file")) throw new ServiceException("unzip: returnAs must be either 'bytes' or 'string' or 'file'"); 
		
		int len = 0;
		String sZipFilename = "";
		int n = 0;
		int count = 0;
		int BUFFERSIZE = 4192;
		
		try{
			
			if (oZipBytes != null) {   // if the caller supplies zipBytes, then we will unzip bytes (and ignore the zipFileName)
		
				if ( !(oZipBytes instanceof byte[]) ) throw new ServiceException("unzip: zipBytes parameter must be of type byte[]");
				byte[] zipdata = (byte[]) oZipBytes;
		
				ByteArrayInputStream BIS1 = new ByteArrayInputStream(zipdata);	
			
				//read first 2 chars from the byte to check if BYTES are in zip or gzip format
				int size = BIS1.available();
				byte[] bytes = new byte[size];
				BIS1.read(bytes, 0, 2);
				char[] first2chars = new char[size];
				for (int i=0; i < size; i++)
					first2chars[i] = (char)(bytes[i]&0xff);
				BIS1.reset();
				//Append the first 2 char
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < first2chars.length; i++) {
				    sb.append(first2chars[i]);
				}
				String sJoinString = sb.toString();
				String sJoinStringTrim = sJoinString.trim(); 
			
				if (!(sJoinStringTrim).equals("PK")) { // bytes is in gzip file format
					ByteArrayInputStream BIS = new ByteArrayInputStream(zipdata);	
					ByteArrayOutputStream BOS = new ByteArrayOutputStream();
				
					GZIPInputStream ZIS = new GZIPInputStream(BIS);
					if(ZIS == null) throw new ServiceException("unzip: unable to find any entries in the zipBytes data");
							
				    byte buffer[] = new byte[BUFFERSIZE];
				    while ((n = ZIS.read(buffer, 0, BUFFERSIZE)) != -1) {
				    	BOS.write(buffer, 0, n);
				    }
			    	if ( sReturnAs.equals("bytes") ) {
						byte outBytes[] = BOS.toByteArray();
						len = outBytes.length;
						IDataUtil.put(cursor, "bytes", outBytes);
					}else{
						String outString = BOS.toString();
						len = outString.length();
						IDataUtil.put(cursor, "string", outString);
					}
			    	
			    	IDataUtil.put(cursor, "message", "Content uncompressed from " + zipdata.length + " bytes to " + len + " bytes under the name " + sZipFilename);
			    	
				}else{		//Byte is in ZIP file format
			
					ByteArrayInputStream BIS = new ByteArrayInputStream(zipdata);	
					ByteArrayOutputStream BOS = new ByteArrayOutputStream();
					ZipInputStream ZIS = new ZipInputStream(BIS);
					ZipEntry entry = ZIS.getNextEntry();
					if(entry == null) throw new ServiceException("unzip: unable to find any entries in the zipBytes data");
			
					sZipFilename = entry.getName();	// the name of the file as it is embedded in the zip data
				    byte buffer[] = new byte[BUFFERSIZE];
				    while ((n = ZIS.read(buffer, 0, BUFFERSIZE)) != -1) {
				    	BOS.write(buffer, 0, n);
				    }
					ZIS.close();
					if ( sReturnAs.equals("bytes") ) {
						byte outBytes[] = BOS.toByteArray();
						len = outBytes.length;
						IDataUtil.put(cursor, "bytes", outBytes);
					}else{
						String outString = BOS.toString();
						len = outString.length();
						IDataUtil.put(cursor, "string", outString);
					}
					IDataUtil.put(cursor, "message", "Content uncompressed from " + zipdata.length + " bytes to " + len + " bytes under the name " + sZipFilename);
				}
			
			
			}else{  //the caller wants to unzip a pk/gzip file (zipBytes is null)
		
				if (sZipFileName == null) throw new ServiceException("unzip: Must provide 'zipBytes' or 'zipFileName'");
				if (sUnZipFileName == null) throw new ServiceException("unzip: Missing input 'unZipFileName' "); 
				if ( !sReturnAs.equals("file")) throw new ServiceException("unzip: returnAs must be 'file' when providing 'zipFileName'");
		
				FileInputStream fis = new FileInputStream(sZipFileName);
							
				//check if a file zip or gzip
				RandomAccessFile raf = new RandomAccessFile(sZipFileName, "r");
				raf.seek(0); // set the file pointer at 0 position
				byte firstByte = raf.readByte();
				raf.seek(1);
				byte secondByte = raf.readByte();
				long fileLength = raf.length();
				raf.close();
		
				char chFirst = (char)firstByte;
				char chSecond = (char)secondByte;
				//get the first 2 bytes and convert them into string
				String sCombineString = new StringBuilder().append(chFirst).append(chSecond).toString();
		
				//sCombineString = PK meaning file is in zip format
				if (!(sCombineString).equals("PK") ) {
					FileOutputStream fos = new FileOutputStream(sUnZipFileName);
					GZIPInputStream gis1 = new GZIPInputStream(fis);
					byte buffer[] = new byte[BUFFERSIZE];
				    while ((n = gis1.read(buffer, 0, BUFFERSIZE)) != -1) {
				    	fos.write(buffer, 0, n); 
				    	count += n;
				    }
			        fos.close();
			        gis1.close();
			        //IDataUtil.put(cursor, "unZipFile", sUnZipFileName);
			        IDataUtil.put(cursor, "message", "Content uncompressed from " +  fileLength + " bytes to " + count + " bytes under the name " + sUnZipFileName);
				}else{ //Zip FIle
					FileOutputStream fos = new FileOutputStream(sUnZipFileName);
					ZipInputStream zis = new ZipInputStream(fis);
		            ZipEntry ze = zis.getNextEntry();
		            byte buffer[] = new byte[BUFFERSIZE];
				    while ((n = zis.read(buffer, 0, BUFFERSIZE)) != -1) {
				    	fos.write(buffer, 0, n); 
				    	count += n;
				    }
			        //close resources
			        fos.close();
			        zis.close();
			        //IDataUtil.put(cursor, "unZipFile", sUnZipFileName);
			        IDataUtil.put(cursor, "message", "Content uncompressed from " + fileLength + " bytes to " + count + " bytes under the name " + sUnZipFileName);
				}
		
			}
		
		
		}catch(Exception e) {
			//System.out.println(e.getMessage());
			throw new ServiceException(e);
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void zip (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(zip)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional string
		// [i] object:0:optional bytes
		// [i] field:0:required inputFileName
		// [i] field:0:required outputFilename
		// [i] field:0:required zipFormat {"zip","gZip"}
		// [i] field:0:required returnAs {"bytes","file"}
		// [i] field:0:optional zipFilename
		// [o] object:0:optional zipBytes
		// [o] field:0:required message
		IDataCursor cursor = pipeline.getCursor();
		
		// zip - zip a string or bytes
		//
		//		string (input,opt) - either string or bytes must be specified
		//		bytes (input,opt)
		//		inputFileName (input, opt) - the name of the file which contents are going to be zipped
		//		outputFileName (input, opt) - the name of the file which stores the zipped content
		//		returnAS (input, req) Return as required in all the cases. If input as a string then return as "bytes", if input is file then returnAs "file"
		//		zipBytes (output,bytes) - zipped bytes
		//		message (output,string) - informational message
		//
		//	    added gzip functionality to the service
		//		
		//      String, byte or File one of the parameter is required. If more then one input is supplied then service will throw the error
		//		Added multiple file zip functionality to the service. Pass the multiple file name in string filed as comma separated
		
		//input
		 
				String sString = null;
				Object oZipBytes = null;
				String sReturnAs = null;
				String sZipFileName  = null;
				String sUnZipFileName = null;
				String sZipFormat = null;
				String sEmbeddedZipFileName =null;
				
				if (cursor.first("bytes")) oZipBytes = cursor.getValue();
				if (cursor.first("outputFilename"))   sZipFileName = (String) cursor.getValue();
				if (cursor.first("inputFileName"))  sUnZipFileName  = (String) cursor.getValue();
				if (cursor.first("returnAs")) sReturnAs = (String) cursor.getValue();
				if (cursor.first("string")) sString = (String) cursor.getValue();
				if (cursor.first("zipFormat")) sZipFormat = (String) cursor.getValue();
				if (cursor.first("zipFilename")) sEmbeddedZipFileName = (String) cursor.getValue();
				
				if (sReturnAs == null) throw new ServiceException("zip: Missing input 'returnAs'"); 
				if ( !sReturnAs.equals("bytes") && !sReturnAs.equals("string") && !sReturnAs.equals("file")) throw new ServiceException("zip: returnAs must be either 'bytes' or 'string' or 'file'"); 
				
				//if ( !oZipBytes.equals("bytes") && !sUnZipFileName.equals("string") && !sString.equals("string")) throw new ServiceException("zip: Please speicfy one input 'bytes' or 'string' or 'unzip file name'");
				
				
				if (sString != null && oZipBytes != null && sUnZipFileName != null) throw new ServiceException("zip: Ambiguous input - specify either 'string' or 'bytes' or unzip file name, not all three"); 
				
				if (sZipFormat == null) throw new ServiceException("zip/gZip: Missing input 'gZip' or 'zip'"); 
				
				if (oZipBytes != null && !(oZipBytes instanceof byte[])) throw new ServiceException("zip: bytes parameter must be of type byte[]");
				if (sString != null) oZipBytes = sString.getBytes();
				
				
				
				int len = 0;
				 
				int n = 0;
				int count = 0;
				int BUFFERSIZE = 4192;
				
				try{
					
					if (oZipBytes != null) {   // if the caller supplies Bytes, then we will zip bytes (and ignore the zipFileName)
				
						if ( !(oZipBytes instanceof byte[]) ) throw new ServiceException("zip: Bytes parameter must be of type byte[]");
						byte[] zipdata = (byte[]) oZipBytes;
				
						ByteArrayInputStream BIS1 = new ByteArrayInputStream(zipdata);	
					
						 
					
						if (sZipFormat.equals("gZip")) { //Bytes or string  to gZip  format
							
							if ( !sReturnAs.equals("bytes")) throw new ServiceException("zip: returnAs must be 'bytes' when providing input as string or bytes");
							ByteArrayInputStream BIS = new ByteArrayInputStream(zipdata);	
							ByteArrayOutputStream BOS = new ByteArrayOutputStream();
						
							GZIPOutputStream  ZIS = new  GZIPOutputStream (BOS) ;
							if(ZIS == null) throw new ServiceException("zip: unable to read data");
									
						    byte buffer[] = new byte[BUFFERSIZE];
						    while ((n =BIS.read(buffer, 0, BUFFERSIZE)) != -1) {
						    	ZIS.write(buffer, 0, n);
						    	count += n;
						    }
						    byte outBytes[] = BOS.toByteArray();
					    	if ( sReturnAs.equals("bytes") ) {
								
								len = outBytes.length;
								IDataUtil.put(cursor, "zipBytes", outBytes);
							}
					    	
					    	
					    	IDataUtil.put(cursor, "message", "Content compressed from " + count + " bytes to " + len );
					    	
						}else{		//Bytes or string  to ZIP  format
					
							if ( !sReturnAs.equals("bytes")) throw new ServiceException("zip: returnAs must be 'bytes' when providing input as string or bytes");
							ByteArrayInputStream BIS = new ByteArrayInputStream((byte[]) oZipBytes);	
							ByteArrayOutputStream BOS = new ByteArrayOutputStream();
							ZipOutputStream ZOS = new ZipOutputStream(BOS);
							//ZipEntry entry = new ZipEntry("ZipFileName");	// the name of the file as it is embedded in the zip data
							ZipEntry entry = new ZipEntry(sEmbeddedZipFileName);  // the name of the file as it is embedded in the zip data
							ZOS.putNextEntry(entry);
							
							byte buffer[] = new byte[BUFFERSIZE];
							 
							while( (n = BIS.read(buffer, 0, BUFFERSIZE)) != -1 ) {
								ZOS.write(buffer, 0, n);
								count += n;
							}
							ZOS.close();
							
					
							byte outBytes[] = BOS.toByteArray();
							String outString = BOS.toString();
							if ( sReturnAs.equals("bytes") ) {
								
								len = outBytes.length;
								IDataUtil.put(cursor, "zipBytes", outBytes);
							}
							//IDataUtil.put(cursor, "zipBytes", outBytes);
							IDataUtil.put(cursor, "message", "Content compressed from " + count + " bytes to " + outBytes.length);
						}
					
					
					}else{  //the caller wants to zip file (zipBytes is null)
				
						if (sZipFileName == null) throw new ServiceException("zip:  'outputFilename'");
						if (sUnZipFileName == null) throw new ServiceException("zip: Missing input 'inputFileName' "); 
						if ( !sReturnAs.equals("file")) throw new ServiceException("zip: returnAs must be 'file' when providing 'inputFileName'");
						
						//check if we have multiple files to zip or single file needs to zip
						String[] checkMultiFile = sUnZipFileName.trim().split("\\s*,\\s*");  //Remove the space leading, trailing and whitespaces
						int arrySize = checkMultiFile.length;
						
						if (arrySize == 1 ) 
				 	 	  {
								if (sZipFormat.equals("gZip") ) { //gZip file
									FileInputStream fis = new FileInputStream(sUnZipFileName);
									FileOutputStream fos = new FileOutputStream(sZipFileName);
									GZIPOutputStream gzipOS  = new GZIPOutputStream(fos);
									byte buffer[] = new byte[BUFFERSIZE];
								    while ((n = fis.read(buffer, 0, BUFFERSIZE)) != -1) {
								    	gzipOS.write(buffer, 0, n); 
								    	count += n;
								    }
								    fis.close();
							        gzipOS.close();
							       
							       // IDataUtil.put(cursor, "zipFileName", sZipFileName);
							        IDataUtil.put(cursor, "message", "Content compressed from " + " bytes to " + count + " bytes " + sZipFileName.length() + "  under the file name  " + sZipFileName);
								}else{ //Zip FIle
									
									FileOutputStream fos = new FileOutputStream(sZipFileName);
									  ZipOutputStream zos = new ZipOutputStream(fos);
									  //ZipEntry ze= new ZipEntry(sZipFileName);
									  ZipEntry ze= new ZipEntry(sEmbeddedZipFileName);  // the name of the file as it is embedded in the zip data
									  
									  zos.putNextEntry(ze);
									  FileInputStream in = new FileInputStream(sUnZipFileName);
									  
									  byte buffer[] = new byte[BUFFERSIZE];
									  while ((n = in.read(buffer)) > 0) {
									    zos.write(buffer, 0, n);
									    count += n;
									  }
									  in.close();
									  zos.closeEntry();
									  
									  zos.close();
									
							      
							        IDataUtil.put(cursor, "message", "Content compressed from " + " bytes to " + count +" bytes " + sZipFileName.length() + "  under the file name  " + sZipFileName);
							       // IDataUtil.put(cursor, "zipFileName", sZipFileName);
								}
								
				 	 	  }//single file zip ended here
						
						else{ //user has input multiple for zip 
							
							if (sZipFormat.equals("gZip") ) { //gZip file
								
								
										
										FileOutputStream fos = new FileOutputStream(sZipFileName);
										GZIPOutputStream gzipOS  = new GZIPOutputStream(fos);
										byte buffer[] = new byte[BUFFERSIZE];
								for (int i = 0; i < checkMultiFile.length; i++)
								{
										FileInputStream fis = new FileInputStream(checkMultiFile[i]);
									    while ((n = fis.read(buffer, 0, BUFFERSIZE)) != -1) {
									    	gzipOS.write(buffer, 0, n); 
									    	count += n;
									    }
									    fis.close();
								}
								        gzipOS.close();
								    
								       // IDataUtil.put(cursor, "zipFileName", sZipFileName);
								        IDataUtil.put(cursor, "message", "Content compressed from " + " bytes to " + count + " bytes " + sZipFileName.length() + "  under the file name  " + sZipFileName);
							}else{ //Zip FIle
								
								FileOutputStream fos = new FileOutputStream(sZipFileName);
								  ZipOutputStream zos = new ZipOutputStream(fos);
								  //ZipEntry ze= new ZipEntry(sZipFileName);
								  
								  
								  String[] checkEmbeddedFile = sEmbeddedZipFileName.trim().split("\\s*,\\s*");  //Remove the space leading, trailing and whitespaces
									
								  for (int i = 0; i < checkMultiFile.length; i++){
									 // for (int j = 0; j <= i; j++)
									 // {
									      ZipEntry ze= new ZipEntry(checkEmbeddedFile [i]);  // the name of the file as it is embedded in the zip data
									  	 
									      zos.putNextEntry(ze);
									 // }
										  FileInputStream in = new FileInputStream(checkMultiFile[i]);
										  
										  byte buffer[] = new byte[BUFFERSIZE];
										  while ((n = in.read(buffer)) > 0) {
										    zos.write(buffer, 0, n);
										    count += n;
										  }
										  zos.closeEntry();
										  in.close();
								  }
								  
								  
								  zos.close();
								
						      
						        IDataUtil.put(cursor, "message", "Content compressed from " + " bytes to " + count +" bytes " + sZipFileName.length() + "  under the file name  " + sZipFileName);
						       // IDataUtil.put(cursor, "zipFileName", sZipFileName);
						}
				
					}
				
					}
				}catch(Exception e) {
					 
					throw new ServiceException(e);
				}
				
				cursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void zip_old (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(zip_old)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional string
		// [i] object:0:optional bytes
		// [i] field:0:required zipFilename
		// [o] object:0:required zipBytes
		// [o] field:0:required message
		IDataCursor cursor = pipeline.getCursor();
		
		// zip - zip a string or bytes
		//
		//		string (input,opt) - either string or bytes must be specified
		//		bytes (input,opt)
		//		zipFilename (input,req) - the name of the file as it is stored in the zip itself - this should be a standard filename with
		//								  extension, so that when the zip data is unzipped later, the content can be placed in this file
		//
		//		zipBytes (output,bytes) - zipped bytes
		//		message (output,string) - informational message
		
		//input
		String sString = null;
		Object oBytes = null;
		String sZipFilename = null;
		if (cursor.first("string")) sString = (String) cursor.getValue();
		if (cursor.first("bytes")) oBytes = cursor.getValue();
		if (cursor.first("zipFilename")) sZipFilename = (String) cursor.getValue();
		if (sString == null && oBytes == null) throw new ServiceException("zip: Missing input 'string' or 'bytes'"); 
		if (sString != null && oBytes != null) throw new ServiceException("zip: Ambiguous input - specify either 'string' or 'bytes', not both"); 
		if (sZipFilename == null) throw new ServiceException("zip: Missing input 'zipFilename'");
		
		if (oBytes != null && !(oBytes instanceof byte[])) throw new ServiceException("zip: bytes parameter must be of type byte[]");
		if (sString != null) oBytes = sString.getBytes();
		
		try {
			int BUFFERSIZE = 4096;
			byte buffer[] = new byte[BUFFERSIZE];
			ByteArrayInputStream BIS = new ByteArrayInputStream((byte[]) oBytes);	
			ByteArrayOutputStream BOS = new ByteArrayOutputStream();
			ZipOutputStream ZOS = new ZipOutputStream(BOS);
			ZipEntry entry = new ZipEntry(sZipFilename);	// the name of the file as it is embedded in the zip data
			ZOS.putNextEntry(entry);
			int n = 0,count = 0;
			while( (n = BIS.read(buffer, 0, BUFFERSIZE)) != -1 ) {
				ZOS.write(buffer, 0, n);
				count += n;
			}
			ZOS.close();
			byte outBytes[] = BOS.toByteArray();
			IDataUtil.put(cursor, "zipBytes", outBytes);
			IDataUtil.put(cursor, "message", "Content compressed from " + count + " bytes to " + outBytes.length + " bytes under the name " + sZipFilename);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


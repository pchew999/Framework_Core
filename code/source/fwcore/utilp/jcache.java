package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.lang.SecurityException;
import java.net.InetAddress;
import java.util.Properties;
import com.wm.lang.ns.*;
import com.wm.app.b2b.server.*;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
// --- <<IS-END-IMPORTS>> ---

public final class jcache

{
	// ---( internal utility methods )---

	final static jcache _instance = new jcache();

	static jcache _newInstance() { return new jcache(); }

	static jcache _cast(Object o) { return (jcache)o; }

	// ---( server methods )---




	public static final void getTransactionidFromCache (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getTransactionidFromCache)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required cacheFolder
		// [o] field:0:required cachedTransactionid
		IDataCursor cursor = pipeline.getCursor();
		
		// getTransactionidFromCache - look for and return a transaction id from the framework cache
		//
		//		cacheFolder (input,req) - fully qualified path of where the cache is located i.e. fwidcache/current/
		//
		//		cachedTransactionid (output,str) - a string transaction id fully allocated and ready to use (or null if none exist)
		//
		//	The framework cache is a folder, usually on the /csutil shared SAN, but also could be a local disk
		//  in the case of a distributed webMethods instance.   This cache folder is either empty or has a set of
		//  .txt files that start with the letter "D", "T" or "P", for dev/test/prod, followed by a numeric value.
		//  The numeric value is a NEXTVAL value that was generated from an Oracle sequence.  These text files
		//  therefore, represent pre-allocated sequence numbers from a central Oracle database in DEV, TEST, or PROD.
		//  The text files themselves are empty - only the name is important.
		//
		//  This service scans this cache folder for the lowest sequence file (the first file when the list of files
		//  is sorted alphabetically).  If it finds a file, it attempts to rename it from "something.txt" to 
		//  "something_x_y_z.alloc", where the x, y and z portions are system derived.  Then it deletes the 
		//  ".alloc" file.  If this rename and delete both succeed, then the file is considered to have been 
		//  allocated as a transaction id.  The transaction id is then returned to the caller.  This largely eliminates 
		//  potential collisions from multiple threads or systems.  
		
		//get and test input parameters
		String sCacheFolder = null;
		if (cursor.first("cacheFolder")) sCacheFolder = (String)cursor.getValue();
		if (sCacheFolder == null) throw new ServiceException("getTransactionidFromCache: Missing input 'cacheFolder'"); 
		char sRightChar = sCacheFolder.charAt(sCacheFolder.length() - 1);	// if the string has a trailing / or \ then strip it off
		if ( sRightChar == '/' || sRightChar == '\\' ) sCacheFolder = sCacheFolder.substring(0, sCacheFolder.length() - 1);
		
		//check that the cache folder exists
		File zDir = new File(sCacheFolder);
		if (!zDir.exists() || !zDir.isDirectory()) throw new ServiceException("getTransactionidFromCache: Folder '" + sCacheFolder + "' does not exist");
		
		//generate a list of *.txt files from the cache folder
		int lTotalEntries = 0;
		String saFilesAndDirs[];
		FilenameFilter ff = new MyWildCardFilter( "*.txt" );	//the MyWildCardFilter filter is implemented under the "shared" section below
		saFilesAndDirs = zDir.list(ff);							//note that this method returns files AND folders in a single call
		
		//if there are files, then sort them and select the first entry
		lTotalEntries = saFilesAndDirs.length;
		if (lTotalEntries > 0) {
			java.util.Arrays.sort( saFilesAndDirs );	// sort the list alphabetically
		
			//attempt to "allocate" one of these files, one at a time, in a loop, until we get one to work
			//most of the time we will get the first one, but many simultaneous threads could be operating
			//rename the one we find, so that we have exclusive control over this transaction id
			String sFileSep = System.getProperty("file.separator");
			File zCurTxt, zCurAlloc;	// these will be the filenames with .txt and .alloc as extensions
			String sNameAlloc = null;
			String sHostname = "DEVXXX";
			try {sHostname = java.net.InetAddress.getLocalHost().getHostName();} catch(Exception e){};
			long lThreadId = Thread.currentThread().getId();
			long lNano = System.nanoTime();
			String sCachedSessionid = null;
			for (int i = 0; i < lTotalEntries; i++) {
				sNameAlloc = saFilesAndDirs[i].replaceAll("\\.txt", "_" + sHostname + "_" + lThreadId + "_" + lNano + ".alloc");
				zCurTxt = new File(sCacheFolder + sFileSep + saFilesAndDirs[i]);
				zCurAlloc = new File(sCacheFolder + sFileSep + sNameAlloc);
				if ( zCurTxt.isFile() ) {	// this will make sure the file still exists after getting the entire list 
					zCurTxt.renameTo(zCurAlloc);
					if ( zCurAlloc.isFile() ) {		// this will make sure the rename actually worked
						sCachedSessionid = saFilesAndDirs[i].replaceAll("\\.txt", "");
						zCurAlloc.delete();
						IDataUtil.put(cursor, "cachedTransactionid", sCachedSessionid);
						break;
					}
				}
			}
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static class MyWildCardFilter implements FilenameFilter{
	  private String sRegEx;
	  private Pattern zPattern;
	
	  public MyWildCardFilter(String isWildcard) {  // accept * or ? as a wildcard character in the filename
		sRegEx = isWildcard.replace("^", "\\^");	// first, replace any ^ carats with a \^ where the \ needs to be escaped
		sRegEx = isWildcard.replace(".", "\\.");	// then, replace any . periods with a \. where the \ needs to be escaped
		sRegEx = isWildcard.replace("$", "\\$");	// then, replace any $ dollar signs with a \$ where the \ needs to be escaped
		sRegEx = sRegEx.replace("*", ".*");			// then, replace any * asterisks with the regular expression .*
		sRegEx = sRegEx.replace("?",  ".");			// then, replace any ? question marks with the regular expression .
		sRegEx = "^" + sRegEx + "$";				// then surround the result with start-of-string and end-of-string characters
		sRegEx = sRegEx.toLowerCase();				// lowercase the whole thing
	    zPattern = Pattern.compile(sRegEx);
	  }
	
	  public boolean accept(File dir, String sFilename){
	    return zPattern.matcher(sFilename.toLowerCase()).find();
	  }
	}
		
	// --- <<IS-END-SHARED>> ---
}


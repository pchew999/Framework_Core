package com.ipaper.wmframe.core.file;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:26:39 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.lang.SecurityException;
import java.util.Properties;
import com.wm.lang.ns.*;
import com.wm.app.b2b.server.*;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
// --- <<IS-END-IMPORTS>> ---

public final class jlistfiles

{
	// ---( internal utility methods )---

	final static jlistfiles _instance = new jlistfiles();

	static jlistfiles _newInstance() { return new jlistfiles(); }

	static jlistfiles _cast(Object o) { return (jlistfiles)o; }

	// ---( server methods )---




	public static final void listFiles (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(listFiles)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required directory
		// [i] field:0:optional filter
		// [i] field:0:optional folders {"false","true"}
		// [o] field:0:required numFiles
		// [o] field:1:required fileList
		// [o] field:1:required fileList_fullpathnames
		IDataCursor cursor = pipeline.getCursor();
		
		// listFiles - get a list of the files in a folder
		//
		//	directory - ie, /csutil/framework/testfiles/  [required, end this string with / ]
		//	filter - can use * to match multiple, ? to match single, ie, * or *.xml [optional, default=*]
		//	folders - true/false [optional, default=false]
		//
		//	numFiles - the number of files or folders that are found
		//	fileList - the list of files or folders, simple name only
		//	fileList_fullpathnames - the same list of files or folders, but with fully qualified paths
		//
		// This version of listFiles implements support for the filter input parameter which is missing in all 
		// older versions - also optionally supports the listing of folders, plus it returns the list sorted
		
		//get and test input parameters
		    String isFolder = null;
			if (cursor.first("directory")) isFolder = (String) cursor.getValue();
			if (isFolder == null) throw new ServiceException("listFiles: Missing input 'directory'");
		    char sRightChar = isFolder.charAt(isFolder.length() - 1);
		    if ( sRightChar == '/' ) isFolder = isFolder.substring(0, isFolder.length() - 1);
		
		    File zDir = new File(isFolder);
		    if (!zDir.exists() || !zDir.isDirectory()) {
		      throw new ServiceException("Directory " + isFolder + " does not exist");
		    }
		
		    String isWildCard = "*";		// use a default wildcard of *
			if (cursor.first("filter")) isWildCard = (String) cursor.getValue();
		
		    String isFolders = "false";		// use a default of false
			if (cursor.first("folders")) isFolders = (String) cursor.getValue();
		
		 //generate a list of files using the wildcard specification provided as input - allows for * and ?
		    String sFileSep = System.getProperty("file.separator");
		    int lNumFiles = 0, lTotalEntries = 0;
		    String saFilesAndDirs[];
		    FilenameFilter ff = new MyWildCardFilter( isWildCard );  //the MyWildCardFilter filter is implemented under the "shared" section below
		    saFilesAndDirs = zDir.list(ff);				// this method will return files AND folders in a single call
		    java.util.Arrays.sort( saFilesAndDirs );	// sort the list alphabetically
		    lTotalEntries = saFilesAndDirs.length;
		    if (lTotalEntries < 1) {
			  IDataUtil.put(cursor, "numFiles", Integer.toString(0));
			  cursor.destroy();
		      return;
		    }
		
		 //determine the count of files or folders
		    File zCur;
		    for (int i = 0; i < lTotalEntries; i++) {
		      zCur = new File(isFolder + sFileSep + saFilesAndDirs[i]);
		      if ( zCur.isDirectory() && isFolders.equals("true") ) lNumFiles++;
		      if ( zCur.isFile()      && isFolders.equals("false") ) lNumFiles++;
		    }
		    zCur = null;
		
		 //create two output arrays - one of the names of files/folders - the other with the input folder prepended
		    String[] osaFileList = new String[lNumFiles];
		    String[] osaPathFileList = new String[lNumFiles];
		    lNumFiles = 0;
		    for (int i = 0; i < lTotalEntries; i++) {
		      zCur = new File(isFolder + sFileSep + saFilesAndDirs[i]);
		      if ( zCur.isDirectory() && isFolders.equals("true") ) {
		        osaFileList[lNumFiles] = saFilesAndDirs[i] + sFileSep;
		        osaPathFileList[lNumFiles] = isFolder + sFileSep + saFilesAndDirs[i] + sFileSep;
		        lNumFiles++;
		      }
		      if ( zCur.isFile() && isFolders.equals("false") ) {
		        osaFileList[lNumFiles] = saFilesAndDirs[i];
		        osaPathFileList[lNumFiles] = isFolder + sFileSep + saFilesAndDirs[i];
		        lNumFiles++;
		      }
		    }
		    zCur = null;
		
		 //return the results
			IDataUtil.put(cursor, "numFiles", Integer.toString(lNumFiles));
			IDataUtil.put(cursor, "fileList", osaFileList);
			IDataUtil.put(cursor, "fileList_fullpathnames", osaPathFileList);
		
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


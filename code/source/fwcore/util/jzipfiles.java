package fwcore.util;

// -----( IS Java Code Template v1.2

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
import java.util.zip.*;
// --- <<IS-END-IMPORTS>> ---

public final class jzipfiles

{
	// ---( internal utility methods )---

	final static jzipfiles _instance = new jzipfiles();

	static jzipfiles _newInstance() { return new jzipfiles(); }

	static jzipfiles _cast(Object o) { return (jzipfiles)o; }

	// ---( server methods )---




	public static final void zipFiles (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(zipFiles)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required folder
		// [i] field:0:optional wildcard
		// [o] object:0:required zipBytes
		// [o] field:0:required message
		IDataCursor cursor = pipeline.getCursor();
		
		// zipFiles - zip a set of files in a folder
		//
		//	folder - ie, /csutil/framework/testfiles/  [required, end this string with / ]
		//	wildcard - can use * to match multiple, ? to match single, ie, * or *.xml [optional, default=*]
		//
		//	zipBytes - the zipped bytes of all files
		//	message - an output summary message
		
		//get and test input parameters
		    String sFolder = null;
			if (cursor.first("folder")) sFolder = (String) cursor.getValue();
			if (sFolder == null) throw new ServiceException("zipFiles: Missing input 'folder'");
		    char sRightChar = sFolder.charAt(sFolder.length() - 1);
		    if ( sRightChar == '/' ) sFolder = sFolder.substring(0, sFolder.length() - 1);
		    File zDir = new File(sFolder);
		    if (!zDir.exists() || !zDir.isDirectory()) {
		      throw new ServiceException("zipFiles: Folder " + sFolder + " does not exist");
		    }
		
		    String sWildCard = "*";		// use a default wildcard of *
			if (cursor.first("wildcard")) sWildCard = (String) cursor.getValue();
		
		    byte outBytes[] = null;
		
		 //generate a list of files AND folders using the wildcard specification provided as input - allows for * and ?
		    String sFileSep = System.getProperty("file.separator");
		    int lNumFiles = 0, lTotalEntries = 0, count = 0;
		    String saFilesAndDirs[];
		    FilenameFilter ff = new MyWildCardFilter( sWildCard );  //the MyWildCardFilter filter is implemented under the "shared" section below
		    saFilesAndDirs = zDir.list(ff);				// this method will return files AND folders in a single call
		    java.util.Arrays.sort( saFilesAndDirs );	// sort the list alphabetically
		    lTotalEntries = saFilesAndDirs.length;
		    if (lTotalEntries < 1) {
			  IDataUtil.put(cursor, "numFiles", Integer.toString(0));
			  cursor.destroy();
		      return;
		    }
		
		 //process just the files (no folders)
		    try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos);
				for (int i = 0; i < lTotalEntries; i++) {
					File zCur = new File(sFolder + sFileSep + saFilesAndDirs[i]);
					if ( zCur.isFile() ) {	// skip folders
						lNumFiles++;
						ZipEntry ze = new ZipEntry(saFilesAndDirs[i]);
						zos.putNextEntry(ze);	//add a new 'zip entry' to the zip output stream
			            //then read the file and write it to the zip output stream
						FileInputStream fis = new FileInputStream(sFolder + sFileSep + saFilesAndDirs[i]);
						byte[] buffer = new byte[1024];
						int len;
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
							count += len;
						}
						zos.closeEntry();
					}
				    zCur = null;
				}
				zos.finish();
				zos.flush();
				zos.close();
				outBytes = baos.toByteArray();	//now take the output stream to a set of bytes
		    } catch (IOException e) {
		    	//e.printStackTrace();
				IDataUtil.put(cursor, "err", e.toString());
				cursor.destroy();
				return;
		    }
		
			 //return the results
			IDataUtil.put(cursor, "zipBytes", outBytes);
			IDataUtil.put(cursor, "message", "Compressed " + lNumFiles + " files (" + count + " bytes) to " + outBytes.length + " bytes");
		
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


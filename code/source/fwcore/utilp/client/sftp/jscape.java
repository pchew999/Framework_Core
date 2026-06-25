package fwcore.utilp.client.sftp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.lang.String.*;
import java.util.*;
import com.jscape.inet.ssh.util.*;
import com.jscape.inet.sftp.*;
// --- <<IS-END-IMPORTS>> ---

public final class jscape

{
	// ---( internal utility methods )---

	final static jscape _instance = new jscape();

	static jscape _newInstance() { return new jscape(); }

	static jscape _cast(Object o) { return (jscape)o; }

	// ---( server methods )---




	public static final void append (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(append)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:optional localfile
		// [i] field:0:required remotefile
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// append - append a local file to an existing file on sftp server
		//
		//		sftpsession (input,object) - the session object returned by login
		//		localfile (input,str) - the local file to upload
		//		remotefile (input,str) - the server side file that will be appended to
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		Sftp sess = null;
		String localfile = null;
		String remotefile = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("append: Missing input 'sess'"); 
		if (cursor.first("localfile")) localfile = (String) cursor.getValue();
		if (localfile == null) throw new ServiceException("append: Missing input 'localfile'"); 
		if (cursor.first("remotefile")) remotefile = (String) cursor.getValue();
		if (remotefile == null) throw new ServiceException("append: Missing input 'remotefile'"); 
		
		try {
			sess.upload(localfile, remotefile, true);
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - file  " + localfile + " appended to " + remotefile );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "append error of " + localfile + " to " + remotefile + " is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void cd (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(cd)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:required dirpath
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// cd - change directory
		//
		//		sftpsession (input,object) - the session object returned by login
		//		dirpath (input,str) - the folder to change to
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		
		Sftp sess = null;
		String dirpath = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("cd: Missing input 'sess'"); 
		if (cursor.first("dirpath")) dirpath = (String) cursor.getValue();
		if (dirpath == null) throw new ServiceException("cd: Missing input 'dirpath'"); 
		
		try	{
			sess.setDir(dirpath);
			String dir = sess.getDir();	//.getRealPath(".");	//neither of these two work the way they should
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - directory is now " + dir );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "cd error to " + dirpath + " is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void deleteFile (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(deleteFile)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:required remotefile
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// deleteFile - delete a file on sftp server
		//
		//		sftpsession (input,object) - the session object returned by login
		//		remotefile (input,str) - the server side file that will be appended to
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		Sftp sess = null;
		String remotefile = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("deleteFile: Missing input 'sess'"); 
		if (cursor.first("remotefile")) remotefile = (String) cursor.getValue();
		if (remotefile == null) throw new ServiceException("deleteFile: Missing input 'remotefile'"); 
			
		try {
			sess.deleteFile(remotefile);
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - delete of " + remotefile + " successful" );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "deleteFile of " + remotefile + " error is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void get (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(get)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:required remotefile
		// [i] field:0:optional localfile
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// get - get a file from the sftp server
		//
		//		sftpsession (input,object) - the session object returned by login
		//		localfile (input,str) - the local file to overwrite
		//		remotefile (input,str) - the server side file to get
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		Sftp sess = null;
		String localfile = null;
		String remotefile = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("get: Missing input 'sftpsession'"); 
		if (cursor.first("remotefile")) remotefile = (String) cursor.getValue();
		if (remotefile == null) throw new ServiceException("get: Missing input 'remotefile'"); 
		if (cursor.first("localfile")) localfile = (String) cursor.getValue();
		if (localfile == null) throw new ServiceException("get: Missing input 'localfile'"); 
		//ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		try {
			sess.download(localfile, remotefile);
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - " + remotefile + " downloaded as " + localfile );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "get of " + remotefile + " error is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void login (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(login)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required hostname
		// [i] field:0:optional hostport
		// [i] field:0:required username
		// [i] field:0:required passwordkey
		// [i] field:0:optional timeout
		// [o] object:0:required sftpsession
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		//
		// login - log in to an sftp server (generally on port 22)
		//
		//				Note that we don't use the standard webMethods sftp client implementation because it requires
		//				setting up a client alias for every different connection (a terrible decision on SoftwareAG's part).
		//				This implementation relies on sftp.jar, which must be in the code/jars folder under this Framework_Core 
		//				package.  You must use the older version of this jar (1,494,878 bytes) because the new 
		//				version (2,104,863 bytes) has a huge bug in it where relative folders are turned into absolute folders.
		//
		//		see http://files.jscape.com/sftp/docs/javadoc/com/jscape/inet/sftp/Sftp.html for documentation
		//
		//		hostname (input,req) - sftp server host name
		//		hostport (input,opt) - sftp server host port (default 22)
		//		username (input,req) - login id
		//		passwordkey (input,req) - an actual password OR an openssh or PuTTY key (as a string) in the format:
		//			-----BEGIN RSA PRIVATE KEY-----
		//			MIIEpQIBAAKCAQEAlPHtfsrxrW0b5OdNOq9C0bIjCcAFSslH6lMNhNIKLl9FzYW5
		//			uBwm73Ks6necbe6pjqnYV6Ex5ZjFlb7Hy9eGBwiVXLHfFDA2NWaqA4X+DmSgadoK
		//			....
		//			XCYPunXPvV6WwbAbQ15BN3esV9vpGn6IOMGvZ6Lw9DAe3mgepNODiWdH9hcUpq9o
		//			We1LI0UqJzT9D9EqDOaJqzbPIOV0BTUNM/7MwtcE+/yI9Ln1Um06QeU=
		//			-----END RSA PRIVATE KEY-----
		//		timeout (input,opt) - set the connection and read timeouts in seconds (default 30 seconds)
		//
		//		sftpsession (output,object) - the session object to be passed to subsequent calls
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		//
		//		What is a PuTTY key?  Great question.  See https://www.ssh.com/ssh/putty/windows/puttygen
		//		What is an OpenSSH key?  Another great question.  See https://www.ssh.com/ssh/keygen/
		//
		//		Note that the above passwordkey, if longer than 100 chars, is assumed to be a private certificate, and will get
		//		written to a temporary file exactly as is prior to logging in, after which the temporary file will be deleted.
		//
		/*
		import com.jscape.inet.ssh.util.*;
		import com.jscape.inet.sftp.*;
		import com.jscape.inet.sftp.Sftp.*;		//not necessary
		import com.jscape.inet.sftp.events.*;	//not necessary
		*/
		
		String sHostname = null;
		String sHostport = null;
		String sUsername = null;
		String sPasswordkey = null;
		String sTimeout = null;
		if (cursor.first("hostname")) sHostname = (String) cursor.getValue();
		if (sHostname == null) throw new ServiceException("login: Missing input 'hostname'"); 
		if (cursor.first("hostport")) sHostport = (String) cursor.getValue();
		if (sHostport == null) sHostport = "22";
		if (cursor.first("username")) sUsername = (String) cursor.getValue();
		if (sUsername == null) throw new ServiceException("login: Missing input 'username'"); 
		if (cursor.first("passwordkey")) sPasswordkey = (String) cursor.getValue();
		if (sPasswordkey == null) throw new ServiceException("login: Missing input 'passwordkey'"); 
		if (cursor.first("timeout")) sTimeout = (String) cursor.getValue();
		if (sTimeout == null) sTimeout = "30";
		
		File oFile = null;
		long iTimeout = Long.parseLong(sTimeout);
		SshParameters parms = null;
		
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			if( sPasswordkey.length() > 100 ) {
				oFile =  new File("pipeline/" + UUID.randomUUID().toString().replace("-", "") + ".ppk");
				FileWriter fw = new FileWriter(oFile);
				fw.write(sPasswordkey);
				fw.close();
				parms = new SshParameters(sHostname, Integer.parseInt(sHostport), sUsername, oFile);
			}else{
				parms = new SshParameters(sHostname, Integer.parseInt(sHostport), sUsername, sPasswordkey);
			}
			parms.setConnectionTimeout(iTimeout * 500);	// another oddity of the jscape jar
			parms.setReadingTimeout(iTimeout * 500);
			Sftp sess = new Sftp(parms);
			long cto = parms.getConnectionTimeout();
			long rto = parms.getReadingTimeout();
			sess.connect();
			if( sPasswordkey.length() > 100 ) {
				boolean status = oFile.delete();
			}
			String dir = sess.getRealPath(".");
			IDataUtil.put( cursor, "sftpsession", sess );
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - dir is " + dir + " cto=" + cto + " rto=" + rto );
		} catch (Exception e) {
			if( sPasswordkey.length() > 100 ) {
				boolean status = oFile.delete();
			}
			IDataUtil.put( cursor, "returncode", "400" );
			//StringWriter sw = new StringWriter();	// to get the full stack trace
			//PrintWriter pw = new PrintWriter(sw);
			//e.printStackTrace(pw);
			//s = sw.toString();
			IDataUtil.put( cursor, "returnmsg", "login error to " + sHostname + ":" + sHostport + " (" + sUsername + "/" + "****" + ") is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void logout (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(logout)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// logout - log out of an sftp server
		//
		//		sftpsession (input,object) - the session object returned by login
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		
		Sftp sess = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("logout: Missing input 'sftpsession'"); 
		
		try {
			sess.disconnect();
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK" );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "logout: error is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void ls (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ls)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:optional filenamepattern
		// [i] field:0:optional orderby {"none"}
		// [o] field:1:required dirlist
		// [o] field:1:required sizelist
		// [o] field:1:required datelist
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// ls - get a list of files (NOT FULLY IMPLEMENTED)
		//
		//		sftpsession (input,object) - the session object returned by login
		//		filenamepattern (input,str) - a regex string pattern, ie, .+\\.(?:java|class)   (defaults to .+ or all files)
		//		orderby (input,str) - none/name (defaults to none)
		//
		//		dirlist (output,arr) - list of files
		//		sizelist (output,arr) - size of files
		//		datelist (output,arr) - modification date of files
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		
		Sftp sess = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("ls: Missing input 'sftpsession'"); 
		String filenamepattern = null;
		if (cursor.first("filenamepattern")) filenamepattern = (String) cursor.getValue();
		if (filenamepattern == null) filenamepattern = ".+";	// all files
		String orderby = null;
		if (cursor.first("orderby")) orderby = (String) cursor.getValue();
		if (orderby == null) orderby = "none";
		String filenames="",filesizes="",filedates="";	//,perms="",isdirs="";
		
		try {
			Enumeration e = sess.getDirListing(filenamepattern);
			while( e.hasMoreElements() ) {
				SftpFile f = (SftpFile)e.nextElement();
				if (! f.isDirectory() ) {
					filenames = filenames + f.getFilename() + '\n';
					filesizes = filesizes + f.getFilesize() + '\n';
					java.time.Instant instant = java.time.Instant.ofEpochMilli( f.getModificationTime()); // 1,322,018,752,992L
					filedates = filedates + instant.toString() + '\n';	// 2011-11-23T03:25:52Z
				}
				//perms = perms + f.getPermissions() + '\n';
				//isdirs = isdirs + f.isDirectory() + '\n';
			}
			if( filenames.equals("") ) {	// this little bit of stupidity is because the "split" java method is f'ed
				IDataUtil.put( cursor, "returncode", "450" );
				IDataUtil.put( cursor, "returnmsg", "No files found for " + filenamepattern );
				String dirlist[] = null;
				IDataUtil.put( cursor, "dirlist", dirlist );
			}else{
				String[] dirlist = filenames.split("\\n");
				//if(orderby.equals("name")) Arrays.sort(dirlist);  disabled intentionally
				IDataUtil.put( cursor, "dirlist", dirlist );
				String[] sizelist = filesizes.split("\\n");
				IDataUtil.put( cursor, "sizelist", sizelist );
				String[] datelist = filedates.split("\\n");
				IDataUtil.put( cursor, "datelist", datelist );
				//String[] permlist = perms.split("\\n");
				//IDataUtil.put( cursor, "permlist", permlist );
				//String[] isdirlist = isdirs.split("\\n");
				//IDataUtil.put( cursor, "isdirlist", isdirlist );
				IDataUtil.put( cursor, "returncode", "200" );
				IDataUtil.put( cursor, "returnmsg", "OK - ls of " + filenamepattern + " returned " + dirlist.length + " files" );
			}
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "ls of " + filenamepattern + " error is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void mkdir (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(mkdir)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:required dirpath
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// mkdir - create a directory
		//
		//		sftpsession (input,object) - the session object returned by login
		//		dirpath (input,str) - the folder to create
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		
		Sftp sess = null;
		String dirpath = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("mkdir: Missing input 'sess'"); 
		if (cursor.first("dirpath")) dirpath = (String) cursor.getValue();
		if (dirpath == null) throw new ServiceException("mkdir: Missing input 'dirpath'"); 
		
		try	{
			sess.makeDir(dirpath);
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - directory " + dirpath + " created" );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "mkdir error of " + dirpath + " is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void put (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(put)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:optional localfile
		// [i] object:0:optional stream
		// [i] field:0:required remotefile
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// put - put a local file to an sftp server
		//
		//		sftpsession (input,object) - the session object returned by login
		//		localfile (input,str) - the local file to upload
		//		remotefile (input,str) - the server side file
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		Sftp sess = null;
		String localfile = null;
		InputStream stream = null;
		String remotefile = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("put: Missing input 'sftpsession'"); 
		if (cursor.first("localfile")) localfile = (String) cursor.getValue();
		if (cursor.first("stream")) stream = (InputStream) cursor.getValue();
		if (localfile == null && stream == null) throw new ServiceException("put: Missing input 'localfile' or 'stream'"); 
		if (localfile != null && stream != null) throw new ServiceException("put: Ambiguous input 'localfile' and 'stream'"); 
		if (cursor.first("remotefile")) remotefile = (String) cursor.getValue();
		if (remotefile == null) throw new ServiceException("put: Missing input 'remotefile'"); 
		
		try {
			if(localfile != null) sess.upload(localfile, remotefile);
			if(stream != null) sess.upload(stream, remotefile);
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK" );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "put error of " + remotefile + " is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void rename (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(rename)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:required oldname
		// [i] field:0:required newname
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// rename - rename a file on an sftp server
		//
		//		sftpsession (input,object) - the session object returned by login
		//		oldname (input,str) - the file to rename
		//		newname (input,str) - the new name of the file
		//
		//		returncode (output,str) - numeric status code 200 or 400
		//		returnmsg (output,str) - return message
		Sftp sess = null;
		String oldname = null;
		String newname = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("rename: Missing input 'sftpsession'"); 
		if (cursor.first("oldname")) oldname = (String) cursor.getValue();
		if (oldname == null) throw new ServiceException("rename: Missing input 'oldname'"); 
		if (cursor.first("newname")) newname = (String) cursor.getValue();
		if (newname == null) throw new ServiceException("rename: Missing input 'newname'"); 
					
		try {
			sess.renameFile(oldname, newname);
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - file renamed" );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "rename error of " + oldname + " to " + newname + " is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void setMode (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(setMode)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required sftpsession
		// [i] field:0:required mode {"ascii","binary"}
		// [o] field:0:required returncode
		// [o] field:0:required returnmsg
		IDataCursor cursor = pipeline.getCursor();
		
		// setMode - set the transfer mode
		//
		//		sftpsession (input,object) - the session object returned by login
		//		mode (input,str) - ascii/binary
		Sftp sess = null;
		String mode = null;
		if (cursor.first("sftpsession")) sess = (Sftp) cursor.getValue();
		if (sess == null) throw new ServiceException("setMode: Missing input 'sftpsession'");
		if (cursor.first("mode")) mode = (String) cursor.getValue();
		if (mode == null) throw new ServiceException("setMode: Missing input 'mode'");
		
		try {
			if(mode.equals("ascii")) {
				sess.setAscii();
			}else if (mode.equals("binary")) {
				sess.setBinary();
			}
			IDataUtil.put( cursor, "returncode", "200" );
			IDataUtil.put( cursor, "returnmsg", "OK - mode is now " + mode );
		} catch (Exception e) {
			IDataUtil.put( cursor, "returncode", "400" );
			IDataUtil.put( cursor, "returnmsg", "setMode error setting " + mode + " is " + e );
		}
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


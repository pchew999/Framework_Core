package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.net.*;
// --- <<IS-END-IMPORTS>> ---

public final class jexec

{
	// ---( internal utility methods )---

	final static jexec _instance = new jexec();

	static jexec _newInstance() { return new jexec(); }

	static jexec _cast(Object o) { return (jexec)o; }

	// ---( server methods )---




	public static final void exec (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(exec)>> ---
		// @sigtype java 3.5
		// [i] field:0:required commandline
		// [o] field:0:required status
		// [o] field:0:required exitValue
		// [o] field:0:required stdout
		// [o] field:0:required stderr
		IDataCursor cursor = pipeline.getCursor();
		//
		// exec - call the java exec command which will execute almost any unix shell command
		//
		//		commandline (input,req) - a complete command line sent to the unix shell (redirection may not be available)
		//
		//		status (output,str) - ok, unknown or error: message
		//		exitValue (output,str) - the numeric result of the exec call (0, 1, or whatever the actual call may return)
		//		stdout (output,str) - the standard output of the command (multiline, loggable and parseable)
		//		stderr (output,str) - the standard error output of the command (multiline, loggable and parseable)
		//
		//	exec can execute any standard command line at the unix command shell - however, the output is not always
		//	the same from command to command - this call attempts to provide whatever is available from most commands
		//	in the form of the exit value, standard output, standard error and a general status of the call.
		//
		//	some examples of unix shell commands:
		//
		//		ls -al
		//		ping -s google.com 56 4
		//		curl --ntlm --user x-iptest@corp:Paper2014 --verbose --upload-file abc.txt https://some.site.com/xyz.txt
		//
		//	you need to know what each call might produce in the way of exit values, output and errors - also do not use
		//	this exec service to invoke any shell commands that could produce a large amount of output, since there is
		//	limited buffer space for capturing the command's output, and the called process may get blocked as a result
		//
		
		//input
		String sCmdline = null;
		if (cursor.first("commandline")) sCmdline = (String) cursor.getValue();
		if (sCmdline == null || sCmdline.length()==0) throw new ServiceException("exec: Missing input 'commandline'"); 
		
		//output
		String sStatus = "unknown";
		int iExitVal = -999;
		String sStdout = "";
		String sStderr = "";
		
		try {
		    Process p = Runtime.getRuntime().exec(sCmdline);	// ping -s 10.206.111.96 56 4
			p.waitFor();
		
			BufferedReader iStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = "";		// read the output of the command (yes, I know it says inputStream, but thats how this thing works)
			while ((s = iStream.readLine()) != null) {
				sStdout = sStdout + s + "\n";
			}
		
			BufferedReader eStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			s = "";				// read the error stream of the command
			while ((s = eStream.readLine()) != null) {
				sStderr = sStderr + s + "\n";
			}
		  
			iExitVal = p.exitValue();
			sStatus = "ok";
		
		} catch (Exception e) {
			//e.printStackTrace();
			sStatus = "error: " + e.toString();
		}
		
		IDataUtil.put(cursor, "status", sStatus);				// ok, unknown, or error: message
		IDataUtil.put(cursor, "exitValue", "" + iExitVal);		// 0, 1 or other numeric value
		IDataUtil.put(cursor, "stdout", sStdout);
		IDataUtil.put(cursor, "stderr", sStderr);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void execv (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(execv)>> ---
		// @sigtype java 3.5
		// [i] field:1:required commandlineargs
		// [o] field:0:required status
		// [o] field:0:required exitValue
		// [o] field:0:required stdout
		// [o] field:0:required stderr
		IDataCursor cursor = pipeline.getCursor();
		//
		// execv - call the java exec command which will execute almost any unix shell command
		//
		//		commandlineargs (input,req) - a set of individual string arguments comprising a complete command line sent to the unix shell (redirection may not be available)
		//
		//		status (output,str) - ok, unknown or error: message
		//		exitValue (output,str) - the numeric result of the exec call (0, 1, or whatever the actual call may return)
		//		stdout (output,str) - the standard output of the command (multiline, loggable and parseable)
		//		stderr (output,str) - the standard error output of the command (multiline, loggable and parseable)
		//
		//	execv can execute any standard command line at the unix command shell - however, the output is not always
		//	the same from command to command - this call attempts to provide whatever is available from most commands
		//	in the form of the exit value, standard output, standard error and a general status of the call.
		//
		//	some examples of unix shell commands:
		//
		//		ls -al
		//		ping -s google.com 56 4
		//		curl --ntlm --user x-iptest@corp:Paper2014 --verbose --upload-file abc.txt https://some.site.com/xyz.txt
		//
		//	you need to know what each call might produce in the way of exit values, output and errors - also do not use
		//	this execv service to invoke any shell commands that could produce a large amount of output, since there is
		//	limited buffer space for capturing the command's output, and the called process may get blocked as a result
		//
		
		//input
		String[] sCmdlineargs = null;
		if (cursor.first("commandlineargs")) sCmdlineargs = (String[]) cursor.getValue();
		if (sCmdlineargs == null || sCmdlineargs.length==0) throw new ServiceException("exec: Missing input 'commandlineargs'"); 
		
		//output
		String sStatus = "unknown";
		int iExitVal = -999;
		String sStdout = "";
		String sStderr = "";
		
		try {
		    Process p = Runtime.getRuntime().exec(sCmdlineargs);	// ping -s 10.206.111.96 56 4
			p.waitFor();
		
			BufferedReader iStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = "";		// read the output of the command (yes, I know it says inputStream, but thats how this thing works)
			while ((s = iStream.readLine()) != null) {
				sStdout = sStdout + s + "\n";
			}
		
			BufferedReader eStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			s = "";				// read the error stream of the command
			while ((s = eStream.readLine()) != null) {
				sStderr = sStderr + s + "\n";
			}
		  
			iExitVal = p.exitValue();
			sStatus = "ok";
		
		} catch (Exception e) {
			//e.printStackTrace();
			sStatus = "error: " + e.toString();
		}
		
		IDataUtil.put(cursor, "status", sStatus);				// ok, unknown, or error: message
		IDataUtil.put(cursor, "exitValue", "" + iExitVal);		// 0, 1 or other numeric value
		IDataUtil.put(cursor, "stdout", sStdout);
		IDataUtil.put(cursor, "stderr", sStderr);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


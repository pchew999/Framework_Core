package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.net.*;
// --- <<IS-END-IMPORTS>> ---

public final class jping

{
	// ---( internal utility methods )---

	final static jping _instance = new jping();

	static jping _newInstance() { return new jping(); }

	static jping _cast(Object o) { return (jping)o; }

	// ---( server methods )---




	public static final void echo (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(echo)>> ---
		// @sigtype java 3.5
		// [i] field:0:required server
		// [o] field:0:required status
		IDataCursor cursor = pipeline.getCursor();
		
		// echo - open a socket to port 7 (the echo port) to determine if a server is alive
		//				(seems none of the port 7's are open within ip though)
		//
		//		server (input,req) - a server name or ip address to echo
		//
		//		status (output,str) - result of echo (ok/unavail/unknown/error)
		//
		
		//input
		String sServer = null;
		if (cursor.first("server")) sServer = (String) cursor.getValue();
		if (sServer == null || sServer.length()==0) throw new ServiceException("ping: Missing input 'server'"); 
		
		//output
		String sStatus = "unknown";
		
		try {
			Socket t = new Socket(sServer, 7);
			//DataInputStream dis = new DataInputStream(t.getInputStream());
			BufferedReader dis = new BufferedReader(new InputStreamReader(t.getInputStream()));
			PrintStream ps = new PrintStream(t.getOutputStream());
			ps.println("echo message");
			String str = dis.readLine();
		    if (str.equals("echo message")) {
		    	sStatus = "ok";
			}else{
				sStatus = "unavail";	//host is inaccessible or the echo port is not active
			}
		    t.close();
		} catch (Exception e) {
		    sStatus = "error: " + e.toString();
		}
		
		IDataUtil.put(cursor, "status", sStatus);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void ping (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(ping)>> ---
		// @sigtype java 3.5
		// [i] field:0:required server
		// [o] field:0:required status
		// [o] field:0:required log
		IDataCursor cursor = pipeline.getCursor();
		//
		// ping - execute the unix ping command to determine if a server is alive (9/27/22 - accepts 50% packet loss)
		//
		//		server (input,req) - a server name or ip address to ping
		//
		//		status (output,str) - result of ping (ok/unavail/unknown)
		//		log (output,str) - full log of ping output
		//
		//	normal output from ping to an available server: 
		//
		//wm1-[wmb2b]/home/share/wmb2b> ping  -s wm01.ipaper.com  56  2
		//PING wm01.ipaper.com: 56 data bytes
		//64 bytes from 164.103.157.12: icmp_seq=0. time=1.887 ms
		//64 bytes from 164.103.157.12: icmp_seq=1. time=1.711 ms
		//----wm01.ipaper.com PING Statistics----
		//2 packets transmitted, 2 packets received, 0% packet loss
		//round-trip (ms)  min/avg/max/stddev = 0.055/0.063/0.071/0.011
		//
		//	normal output from ping to a suspect server: 
		//
		//wm1-[wmb2b]/home/share/wmb2b> ping  -s beyondfeedback.exavault.com  56  2
		//PING beyondfeedback.exavault.com: 56 data bytes
		//64 bytes from 164.103.157.12: icmp_seq=0. time=1.887 ms
		//64 bytes from 164.103.157.12: icmp_seq=1. time=1.711 ms
		//----beyondfeedback.exavault.com PING Statistics----
		//2 packets transmitted, 1 packets received, 50% packet loss
		//round-trip (ms)  min/avg/max/stddev = 9223372036854776.000/0.000/0.000/nan
		//
		//	normal output from ping to an unavailable server:
		//
		//wm1-[wmb2b]/home/share/wmb2b> ping  -s chase.com  56  2
		//PING chase.com: 56 data bytes
		//----chase.com PING Statistics----
		//2 packets transmitted, 0 packets received, 100% packet loss
		//Changed the ping command as Linux has different syntax for ping command
		//This service works for Sun Solaris and Linux OS
		
		//input
		String sServer = null;
		if (cursor.first("server")) sServer = (String) cursor.getValue();
		if (sServer==null || sServer.length()==0) throw new ServiceException("ping: Missing input 'server'"); 
		
		//output
		String sStatus = "unknown";
		String sLog = "";
		String oSystem = System.getProperty("os.name");
		Process p = null;
		try {
				if (oSystem.equals("Linux"))
				{
					p = Runtime.getRuntime().exec("ping  " + sServer + " -c 2");	//.exec("ping -c 4 10.206.111.108");
				}
				else if (oSystem.equals("SunOS")) 
				{
					p = Runtime.getRuntime().exec("ping -s " + sServer + " 56 2");	//.exec("ping -c 4 10.206.111.108");
				}
				BufferedReader inputStream = new BufferedReader(
			            new InputStreamReader(p.getInputStream()));
			
			    String s = "";
			    // reading output stream of the command
			    while ((s = inputStream.readLine()) != null) {
			    	sLog = sLog + s + "\n";
			    	
			    	 // reading output stream of the command
			    	if (oSystem.equals("Linux"))
					{
				    	if (s.contains("2 packets transmitted, 2 received, 0% packet loss")) {
				    		sStatus = "ok";
				    	}
				    	if (s.contains("2 packets transmitted, 1 received, 50% packet loss")) {
				    		sStatus = "ok";
				    	}
				    	if (s.contains("2 packets transmitted, 0 received, 100% packet loss")) {
				    		sStatus = "unavail";
				    	}
					}
			    	else if (oSystem.equals("SunOS")) 
					{
				    	if (s.equals("2 packets transmitted, 2 packets received, 0% packet loss")) {
				    		sStatus = "ok";
				    	}
				    	if (s.equals("2 packets transmitted, 1 packets received, 50% packet loss")) {
				    		sStatus = "ok";
				    	}
				    	if (s.equals("2 packets transmitted, 0 packets received, 100% packet loss")) {
				    		sStatus = "unavail";
				    	}
					}	
			    }
		
		} 
		catch (Exception e) {
		    e.printStackTrace();
		}
		
		IDataUtil.put(cursor, "log", sLog);
		IDataUtil.put(cursor, "status", sStatus);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void reach (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(reach)>> ---
		// @sigtype java 3.5
		// [i] field:0:required server
		// [o] field:0:required status
		IDataCursor cursor = pipeline.getCursor();
		
		// reach - execute the java isReachable call to determine if a server is alive
		//
		//		server (input,req) - a server name or ip address to reach
		//
		//		status (output,str) - result of reach (ok/unavail/unknown/error)
		//
		
		//input
		String sServer = null;
		if (cursor.first("server")) sServer = (String) cursor.getValue();
		if (sServer == null || sServer.length()==0) throw new ServiceException("ping: Missing input 'server'"); 
		
		//output
		String sStatus = "unknown";
		
		InetAddress address = null;
		
		try {
			address = InetAddress.getByName(sServer);
		//	System.out.println("Name: " + address.getHostName());
		//		       System.out.println("Addr: " + address.getHostAddress());
			sStatus = "unavail";
			if (address.isReachable(5000)) sStatus = "ok";
		
		} catch (Exception e) {
		    sStatus = "error: " + e.toString();
		}
		
		IDataUtil.put(cursor, "status", sStatus);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


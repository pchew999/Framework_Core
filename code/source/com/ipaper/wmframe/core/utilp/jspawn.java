package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.wm.util.Debug;
import java.io.*;
import java.util.*;
import java.lang.System;
import com.wm.app.b2b.server.*;
import com.wm.util.Table;
import java.text.*;
import com.wm.lang.ns.*;
// --- <<IS-END-IMPORTS>> ---

public final class jspawn

{
	// ---( internal utility methods )---

	final static jspawn _instance = new jspawn();

	static jspawn _newInstance() { return new jspawn(); }

	static jspawn _cast(Object o) { return (jspawn)o; }

	// ---( server methods )---




	public static final void spawnThread (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(spawnThread)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serviceName
		// [i] record:0:optional input
		// [o] object:0:required threadHandle
		IDataCursor cursor = pipeline.getCursor();
		
		// spawnThread - create a thread and invoke a service in that thread
		//
		//		serviceName (input,req) - a string containing the fully qualified name of the service to execute
		//		input (input,opt) - a document containing the input to send to the service - what falls under this
		//							document is the exact signature of the flow service being called
		//
		//		threadHandle (output,str) - a string containing the thread id that was created
		//
		//		Note that this service doesnt seem to fail when the supplied flow service does not exist - use
		//		the spawnFlow service instead of calling this directly
		//
		String serviceName = null;
		IData input = null;
		
		if (cursor.first("serviceName")) serviceName = (String) cursor.getValue();
		if (serviceName == null) throw new ServiceException("spawnThread: Missing input 'serviceName'");
		if (serviceName.indexOf(":") == -1) throw new ServiceException("spawnThread: Service name must be in the format folder:service");
		
		if (cursor.first("input")) input = (IData) cursor.getValue();
		
		try	{
		  // Split the folder and service from serviceName
		  String ifc = serviceName.substring(0, serviceName.indexOf(":"));
		  String service = serviceName.substring(serviceName.indexOf(":") + 1, serviceName.length());
		
		  // Invoke service
		  ServiceThread threadHandle = Service.doThreadInvoke(ifc, service, IDataUtil.deepClone(input));
		  IDataUtil.put(cursor, "threadHandle", threadHandle);
		}
		catch (Exception e)	{
		  throw new ServiceException(e.getMessage());
		}
		finally	{
		  cursor.destroy();
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	/**
	 * Used by "deepConvert"
	 * 
	 */
	private static final Values convert(Hashtable hT)
	{
	  // Following statement gets all arrays in this object.
	  boolean nullFlag = false;
	  Object[] hTArray = hT.values().toArray();
	  Enumeration hTEnumeration = hT.keys();
	  Values outbound = new Values();
	
	  for (int i = 0; i < hTArray.length; i++)
	  {
	    String key = (String) hTEnumeration.nextElement();
	    if (hTArray[i] instanceof java.lang.String)
	    {
	      outbound.put(key, (String) hTArray[i]);
	    }
	    else if (hTArray[i] instanceof java.util.Hashtable)
	    {
	      Values internalObject = convert((Hashtable) hTArray[i]);
	      if (internalObject == null)
	      {
	        nullFlag = true;
	        return null;
	      }
	      outbound.put(key, internalObject);
	    }
	    else
	    {
	      System.out.println("Conversion Failure:" + "unsupported type within inbound Hashtable.");
	      return null;
	    }
	  }
	  return outbound;
	}
	
	public static final int MAXSLEEP = 3600000; // one hr
		
	// --- <<IS-END-SHARED>> ---
}


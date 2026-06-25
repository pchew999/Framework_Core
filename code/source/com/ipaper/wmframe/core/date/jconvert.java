package com.ipaper.wmframe.core.date;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.sql.*;
import java.util.*;
import java.text.*;
// --- <<IS-END-IMPORTS>> ---

public final class jconvert

{
	// ---( internal utility methods )---

	final static jconvert _instance = new jconvert();

	static jconvert _newInstance() { return new jconvert(); }

	static jconvert _cast(Object o) { return (jconvert)o; }

	// ---( server methods )---




	public static final void usec2JavaSqlDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(usec2JavaSqlDate)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required time_in_msec
		// [o] object:0:required date
		IDataCursor cursor = pipeline.getCursor();
		
		// usec2JavaSqlDate - convert supplied milliseconds to a java.sql.Date object
		//
		//		time_in_msec(input,req) - milliseconds value to convert
		//
		//		date (output,obj) - the resulting date
		
		String sTimeUsec = null;		//input
		java.sql.Date oDate = null;		//output
		
		if (cursor.first("time_in_msec")) sTimeUsec = (String) cursor.getValue();
		if (sTimeUsec == null) throw new ServiceException("usec2JavaSqlDate: Missing input 'time_in_msec'");
		
		Long time_in_msec = new Long(sTimeUsec);
		oDate = new java.sql.Date(time_in_msec.longValue());
		
		IDataUtil.put(cursor, "date", oDate);
		cursor.destroy();		
		// --- <<IS-END>> ---

                
	}



	public static final void usec2JavaSqlTime (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(usec2JavaSqlTime)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required time_in_msec
		// [o] object:0:required time
		IDataCursor cursor = pipeline.getCursor();
		
		// usec2JavaSqlTime - convert supplied milliseconds to a java.sql.Time object
		//
		//		time_in_msec(input,req) - milliseconds value to convert
		//
		//		time (output,obj) - the resulting time
		
		String sTimeUsec = null;		//input
		java.sql.Time oTime = null;		//output
		
		if (cursor.first("time_in_msec")) sTimeUsec = (String) cursor.getValue();
		if (sTimeUsec == null) throw new ServiceException("usec2JavaSqlTime: Missing input 'time_in_msec'");
		
		Long time_in_msec = new Long(sTimeUsec);
		oTime = new Time(time_in_msec.longValue());
		
		IDataUtil.put(cursor, "time", oTime);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void usec2JavaSqlTimestamp (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(usec2JavaSqlTimestamp)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required time_in_msec
		// [o] object:0:required timestamp
		IDataCursor cursor = pipeline.getCursor();
		
		// usec2JavaSqlTimestamp - convert supplied milliseconds to a java.sql.Timestamp object
		//
		//		time_in_msec(input,req) - millisecond value to convert
		//
		//		timestamp (output,obj) - the resulting timestamp
		
		String sTimeUsec = null;				//input
		java.sql.Timestamp oTimestamp = null;	//output
		
		if (cursor.first("time_in_msec")) sTimeUsec = (String) cursor.getValue();
		if (sTimeUsec == null) throw new ServiceException("usec2JavaSqlTimestamp: Missing input 'time_in_msec'");
		
		Long time_in_msec = new Long(sTimeUsec);
		oTimestamp = new Timestamp(time_in_msec.longValue());
		
		IDataUtil.put(cursor, "timestamp", oTimestamp);
		cursor.destroy();	
		// --- <<IS-END>> ---

                
	}



	public static final void usec2JavaUtilDate (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(usec2JavaUtilDate)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required time_in_msec
		// [o] object:0:required date
		IDataCursor cursor = pipeline.getCursor();
		
		// usec2JavaUtilDate - convert supplied milliseconds to a java.util.Date object
		//
		//		time_in_msec(input,req) - millisecond value to convert
		//
		//		date (output,obj) - the resulting date
		
		String sTimeUsec = null;		//input
		java.util.Date oDate = null;	//output
		
		if (cursor.first("time_in_msec")) sTimeUsec = (String) cursor.getValue();
		if (sTimeUsec == null) throw new ServiceException("usec2JavaUtilDate: Missing input 'time_in_msec'");
		
		Long time_in_msec = new Long(sTimeUsec);
		oDate = new java.sql.Date(time_in_msec.longValue());
		
		IDataUtil.put(cursor, "date", oDate);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


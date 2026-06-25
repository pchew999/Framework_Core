package com.ipaper.wmframe.core.date;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:26:28 CST
// -----( ON-HOST: wm1

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.sql.*;
import java.util.*;
import java.text.*;
// --- <<IS-END-IMPORTS>> ---

public final class jepoch

{
	// ---( internal utility methods )---

	final static jepoch _instance = new jepoch();

	static jepoch _newInstance() { return new jepoch(); }

	static jepoch _cast(Object o) { return (jepoch)o; }

	// ---( server methods )---




	public static final void cvtDate2Milliseconds (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(cvtDate2Milliseconds)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required date_string
		// [i] field:0:required date_pattern
		// [o] field:0:required time_in_msec
		IDataCursor cursor = pipeline.getCursor();
		
		// cvtDate2Milliseconds - convert input date to milliseconds
		//
		//		date_string (output,str) - the date to convert
		//		date_pattern (input,req) - the format of the input date (any valid format, ie, yyyy-MM-dd HH:mm:ss.SSS)
		//
		//		time_in_msec (output,str) - millisecond value of the date in unix epoch
		
		String sInputDate = null;		//input
		String sDateFormat = null;		//input
		
		if (cursor.first("date_string")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("cvtDate2Milliseconds: Missing input 'date_string'");
		if (cursor.first("date_pattern")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("cvtDate2Milliseconds: Missing input 'date_pattern'");
		
		ParsePosition parse_pos = new ParsePosition(0);
		SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat);
		java.util.Date d = sdf.parse(sInputDate, parse_pos);
		long t = d.getTime();
		
		IDataUtil.put( cursor, "time_in_msec", t + "");
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getCurrentTimeInMilliseconds (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentTimeInMilliseconds)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [o] field:0:required time_in_msec
		IDataCursor cursor = pipeline.getCursor();
		
		// getCurrentTimeInMilliseconds - get the current time as a linear numeric value in milliseconds
		//
		//		The value returned is the number of milliseconds since January 1st, 1970 at midnight UTC.  It is NOT an accurate
		//		representation of the current time, since leap seconds are not taken into consideration.  Instead it is a simple
		//		linear, always increasing, numeric value that can be used mathematically, ie, for determining elapsed times.  The
		//		value returned is a 64-bit long, which has an upper limit of 2^63-1.  During the year 2018, the range of values
		//		returned is roughly between 1,514,800,000,000 and 1,546,300,000,000.
		//
		//		time_in_msec (output,str) - the number of milliseconds since 1/1/1970 as a long
		
		long t = System.currentTimeMillis();
		IDataUtil.put( cursor, "time_in_msec", t + "");
		cursor.destroy();		
		// --- <<IS-END>> ---

                
	}
}


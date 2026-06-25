package fwcore.date;

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




	public static final void date2UTC (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(date2UTC)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required inputDateFormat
		// [i] field:0:optional inputTimezone
		// [i] field:0:optional resultDateFormat
		// [o] field:0:required resultDateUTC
		IDataCursor cursor = pipeline.getCursor();
		
		// date2UTC - add or subtract a number of days to the supplied date
		//
		//		inputDate (input,req) - required date on which to perform the conversion
		//		inputDateFormat (input,req) - required format of the input date, ie, yyyy-MM-dd HH:mm:ss
		//		inputTimezone (input,opt) - optional timezone of the input, default is the local timezone CST
		//		resultDateFormat (input,opt) - optional result format of the output, default is the input format
		//
		//		resultDateUTC (output,string) - the resulting date in UTC
		
		String sInputDate = null;			//input
		String sInputDateFormat = null;		//input
		String sInputTimezone = null;		//optional input
		String sResultDateFormat = null;	//optional input
		
		String sResultDateUTC = null;		//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("date2UTC: Missing input 'inputDate'");
		if (cursor.first("inputDateFormat")) sInputDateFormat = (String) cursor.getValue();
		if (sInputDateFormat == null) throw new ServiceException("date2UTC: Missing input 'inputDateFormat'");
		if (cursor.first("inputTimezone")) sInputTimezone = (String) cursor.getValue();
		if (cursor.first("resultDateFormat")) sResultDateFormat = (String) cursor.getValue();
		if (sResultDateFormat == null) sResultDateFormat = sInputDateFormat;
		
		//String date = "2015-08-21 03:15";				//Mon, 12 Feb 2024 12:59:31 +0000
		//String form = "yyyy-MM-dd HH:mm";				//EEE, dd MMM yyyy HH:mm:ss Z
		SimpleDateFormat sdfin = new SimpleDateFormat(sInputDateFormat);
		sdfin.setTimeZone(TimeZone.getDefault());
		if (sInputTimezone != null ) sdfin.setTimeZone(TimeZone.getTimeZone(sInputTimezone));
		
		java.util.Date oDate = null;
		try {
			oDate = sdfin.parse(sInputDate);
		} catch (ParseException e) {
			throw new ServiceException("date2UTC: Unable to parse inputDate='" + sInputDate + "', " + e.toString());
		}
		
		SimpleDateFormat sdfout = new SimpleDateFormat(sResultDateFormat);
		sdfout.setTimeZone(TimeZone.getTimeZone("UTC"));
		sResultDateUTC = sdfout.format(oDate);			// format the output date as UTC
		
		//String idlist[] = TimeZone.getAvailableIDs();		// will get the list of available timezones
		//IDataUtil.put( cursor, "allIds", idlist);
		IDataUtil.put( cursor, "resultDateUTC", sResultDateUTC);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


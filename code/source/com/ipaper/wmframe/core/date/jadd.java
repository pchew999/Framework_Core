package com.ipaper.wmframe.core.date;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
// --- <<IS-END-IMPORTS>> ---

public final class jadd

{
	// ---( internal utility methods )---

	final static jadd _instance = new jadd();

	static jadd _newInstance() { return new jadd(); }

	static jadd _cast(Object o) { return (jadd)o; }

	// ---( server methods )---




	public static final void addDays (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addDays)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required numberOfDays
		// [i] field:0:required dateFormat
		// [i] field:0:optional timezone
		// [o] field:0:required resultDate
		IDataCursor cursor = pipeline.getCursor();
		
		// addDays - add or subtract a number of days to the supplied date
		//
		//		inputDate(input,req) - the date on which to perform the math
		//		numberOfDays(input,req) - a string integer indicating the days to add (can be negative)
		//		dateFormat (input,req) - the format of the input (and output) date, ie, yyyy-MM-dd HH:mm:ss
		//		timezone (input,opt) - the optional timezone to return the output, default is the local timezone
		//
		//		resultDate (output,string) - the resulting date
		
		String sInputDate = null;		//input
		String sNumberOfDays = null;	//input
		String sDateFormat = null;		//input
		String sTimezone = null;		//optional input
		
		String sResultDate = null;		//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("addDays: Missing input 'inputDate'");
		if (cursor.first("numberOfDays")) sNumberOfDays = (String) cursor.getValue();
		if (sNumberOfDays == null) throw new ServiceException("addDays: Missing input 'numberOfDays'");
		if (cursor.first("dateFormat")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("addDays: Missing input 'dateFormat'");
		if (cursor.first("timezone")) sTimezone = (String) cursor.getValue();
		
		Date oDate = null;
		
		//parse the input date sInputDate using the format sDateFormat 
		SimpleDateFormat oSDF = new SimpleDateFormat (sDateFormat);
		try {
			oDate = oSDF.parse(sInputDate);
			if(sTimezone != null) oSDF.setTimeZone(TimeZone.getTimeZone(sTimezone));
		} catch (ParseException e) {
			throw new ServiceException("addDays: Unable to parse inputDate='" + sInputDate + "', " + e.toString());
		}
		
		Calendar oCal = Calendar.getInstance();		// get an instance of a calendar object
		oCal.setTime(oDate);						// set this calendar object to the supplied inputDate
		oCal.add(Calendar.DAY_OF_MONTH, new Integer(sNumberOfDays).intValue());		// add the supplied input as number of days
		
		sResultDate = oSDF.format(oCal.getTime());	// format the output date the same as the input date
		
		IDataUtil.put( cursor, "resultDate", sResultDate);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void addHours (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addHours)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required numberOfHours
		// [i] field:0:required dateFormat
		// [i] field:0:optional timezone
		// [o] field:0:required resultDate
		IDataCursor cursor = pipeline.getCursor();
		
		// addHours - add or subtract a number of hours to the supplied date
		//
		//		inputDate(input,req) - the date on which to perform the math
		//		numberOfHours(input,req) - a string integer indicating the hours to add (can be negative)
		//		dateFormat (input,req) - the format of the input (and output) date, ie, yyyy-MM-dd HH:mm:ss
		//		timezone (input,opt) - the optional timezone to return the output, default is the local timezone
		//
		//		resultDate (output,string) - the resulting date
		
		String sInputDate = null;		//input
		String sNumberOfHours = null;	//input
		String sDateFormat = null;		//input
		String sTimezone = null;		//optional input
		
		String sResultDate = null;		//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("addHours: Missing input 'inputDate'");
		if (cursor.first("numberOfHours")) sNumberOfHours = (String) cursor.getValue();
		if (sNumberOfHours == null) throw new ServiceException("addHours: Missing input 'numberOfHours'");
		if (cursor.first("dateFormat")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("addHours: Missing input 'dateFormat'");
		if (cursor.first("timezone")) sTimezone = (String) cursor.getValue();
		
		Date oDate = null;
		
		//parse the input date sInputDate using the format sDateFormat 
		SimpleDateFormat oSDF = new SimpleDateFormat (sDateFormat);
		try {
			oDate = oSDF.parse(sInputDate);
			if(sTimezone != null) oSDF.setTimeZone(TimeZone.getTimeZone(sTimezone));
		} catch (ParseException e) {
			throw new ServiceException("addHours: Unable to parse inputDate='" + sInputDate + "', " + e.toString());
		}
		
		Calendar oCal = Calendar.getInstance();		// get an instance of a calendar object
		oCal.setTime(oDate);						// set this calendar object to the supplied inputDate
		oCal.add(Calendar.HOUR_OF_DAY, new Integer(sNumberOfHours).intValue());		// add the supplied input as number of hours
		
		sResultDate = oSDF.format(oCal.getTime());	// format the output date the same as the input date
		
		IDataUtil.put( cursor, "resultDate", sResultDate);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void addMinutes (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addMinutes)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required numberOfMinutes
		// [i] field:0:required dateFormat
		// [i] field:0:optional timezone
		// [o] field:0:required resultDate
		IDataCursor cursor = pipeline.getCursor();
		
		// addMinutes - add or subtract a number of minutes to the supplied date
		//
		//		inputDate(input,req) - the date on which to perform the math
		//		numberOfMinutes(input,req) - a string integer indicating the minutes to add (can be negative)
		//		dateFormat (input,req) - the format of the input (and output) date, ie, yyyy-MM-dd HH:mm:ss
		//		timezone (input,opt) - the optional timezone to return the output, default is the local timezone
		//
		//		resultDate (output,string) - the resulting date
		
		String sInputDate = null;		//input
		String sNumberOfMinutes = null;	//input
		String sDateFormat = null;		//input
		String sTimezone = null;		//optional input
		
		String sResultDate = null;		//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("addMinutes: Missing input 'inputDate'");
		if (cursor.first("numberOfMinutes")) sNumberOfMinutes = (String) cursor.getValue();
		if (sNumberOfMinutes == null) throw new ServiceException("addMinutes: Missing input 'numberOfMinutes'");
		if (cursor.first("dateFormat")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("addMinutes: Missing input 'dateFormat'");
		if (cursor.first("timezone")) sTimezone = (String) cursor.getValue();
		
		Date oDate = null;
		
		//parse the input date sInputDate using the format sDateFormat 
		SimpleDateFormat oSDF = new SimpleDateFormat (sDateFormat);
		try {
			oDate = oSDF.parse(sInputDate);
			if(sTimezone != null) oSDF.setTimeZone(TimeZone.getTimeZone(sTimezone));
		} catch (ParseException e) {
			throw new ServiceException("addMinutes: Unable to parse inputDate='" + sInputDate + "', " + e.toString());
		}
		
		Calendar oCal = Calendar.getInstance();		// get an instance of a calendar object
		oCal.setTime(oDate);						// set this calendar object to the supplied inputDate
		oCal.add(Calendar.MINUTE, new Integer(sNumberOfMinutes).intValue());		// add the supplied input as number of minutes
		
		sResultDate = oSDF.format(oCal.getTime());	// format the output date the same as the input date
		
		IDataUtil.put( cursor, "resultDate", sResultDate);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void addMonths (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addMonths)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required numberOfMonths
		// [i] field:0:required dateFormat
		// [i] field:0:optional timezone
		// [o] field:0:required resultDate
		IDataCursor cursor = pipeline.getCursor();
		
		// addMonths - add or subtract a number of months to the supplied date
		//
		//		inputDate(input,req) - the date on which to perform the math
		//		numberOfMonths(input,req) - a string integer indicating the months to add (can be negative)
		//		dateFormat (input,req) - the format of the input (and output) date, ie, yyyy-MM-dd HH:mm:ss
		//		timezone (input,opt) - the optional timezone to return the output, default is the local timezone
		//
		//		resultDate (output,string) - the resulting date
		
		String sInputDate = null;		//input
		String sNumberOfMonths = null;	//input
		String sDateFormat = null;		//input
		String sTimezone = null;		//optional input
		
		String sResultDate = null;		//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("addMonths: Missing input 'inputDate'");
		if (cursor.first("numberOfMonths")) sNumberOfMonths = (String) cursor.getValue();
		if (sNumberOfMonths == null) throw new ServiceException("addMonths: Missing input 'numberOfMonths'");
		if (cursor.first("dateFormat")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("addMonths: Missing input 'dateFormat'");
		if (cursor.first("timezone")) sTimezone = (String) cursor.getValue();
		
		Date oDate = null;
		
		//parse the input date sInputDate using the format sDateFormat 
		SimpleDateFormat oSDF = new SimpleDateFormat (sDateFormat);
		try {
			oDate = oSDF.parse(sInputDate);
			if(sTimezone != null) oSDF.setTimeZone(TimeZone.getTimeZone(sTimezone));
		} catch (ParseException e) {
			throw new ServiceException("addMonths: Unable to parse inputDate='" + sInputDate + "', " + e.toString());
		}
		
		Calendar oCal = Calendar.getInstance();		// get an instance of a calendar object
		oCal.setTime(oDate);						// set this calendar object to the supplied inputDate
		oCal.add(Calendar.MONTH, new Integer(sNumberOfMonths).intValue());		// add the supplied input as number of months
		
		sResultDate = oSDF.format(oCal.getTime());	// format the output date the same as the input date
		
		IDataUtil.put( cursor, "resultDate", sResultDate);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void addSeconds (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(addSeconds)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required numberOfSeconds
		// [i] field:0:required dateFormat
		// [i] field:0:optional timezone
		// [o] field:0:required resultDate
		IDataCursor cursor = pipeline.getCursor();
		
		// addSeconds - add or subtract a number of seconds to the supplied date
		//
		//		inputDate(input,req) - the date on which to perform the math
		//		numberOfSeconds(input,req) - a string integer indicating the seconds to add (can be negative)
		//		dateFormat (input,req) - the format of the input (and output) date, ie, yyyy-MM-dd HH:mm:ss
		//		timezone (input,opt) - the optional timezone to return the output, default is the local timezone
		//
		//		resultDate (output,string) - the resulting date
		
		String sInputDate = null;		//input
		String sNumberOfMonths = null;	//input
		String sDateFormat = null;		//input
		String sTimezone = null;		//optional input
		
		String sResultDate = null;		//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("addSeconds: Missing input 'inputDate'");
		if (cursor.first("numberOfSeconds")) sNumberOfMonths = (String) cursor.getValue();
		if (sNumberOfMonths == null) throw new ServiceException("addSeconds: Missing input 'numberOfSeconds'");
		if (cursor.first("dateFormat")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("addSeconds: Missing input 'dateFormat'");
		if (cursor.first("timezone")) sTimezone = (String) cursor.getValue();
		
		Date oDate = null;
		
		//parse the input date sInputDate using the format sDateFormat 
		SimpleDateFormat oSDF = new SimpleDateFormat (sDateFormat);
		try {
			oDate = oSDF.parse(sInputDate);
			if(sTimezone != null) oSDF.setTimeZone(TimeZone.getTimeZone(sTimezone));
		} catch (ParseException e) {
			throw new ServiceException("addMonths: Unable to parse inputDate='" + sInputDate + "', " + e.toString());
		}
		
		Calendar oCal = Calendar.getInstance();		// get an instance of a calendar object
		oCal.setTime(oDate);						// set this calendar object to the supplied inputDate
		oCal.add(Calendar.SECOND, new Integer(sNumberOfMonths).intValue());		// add the supplied input as number of seconds
		
		sResultDate = oSDF.format(oCal.getTime());	// format the output date the same as the input date
		
		IDataUtil.put( cursor, "resultDate", sResultDate);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


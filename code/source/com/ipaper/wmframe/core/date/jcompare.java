package com.ipaper.wmframe.core.date;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2018-03-02 08:26:20 CST
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

public final class jcompare

{
	// ---( internal utility methods )---

	final static jcompare _instance = new jcompare();

	static jcompare _newInstance() { return new jcompare(); }

	static jcompare _cast(Object o) { return (jcompare)o; }

	// ---( server methods )---




	public static final void isDateBefore (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(isDateBefore)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inputDate
		// [i] field:0:required compareDate
		// [i] field:0:required dateFormat
		// [o] field:0:required result
		IDataCursor cursor = pipeline.getCursor();
		
		// isDateBefore - check if inputDate is a date prior to compareDate
		//
		//		inputDate(input,req) - the date to compare
		//		compareDate(input,req) - the date to compare inputDate to
		//		dateFormat (input,req) - the format of the input dates (any valid format, ie, yyyy-MM-dd HH:mm:ss.SSS)
		//
		//		result (output,str) - true if inputDate < compareDate, false if inputDate >= compareDate
		
		String sInputDate = null;		//input
		String sCompareDate = null;		//input
		String sDateFormat = null;		//input
		
		String sResult = null;			//output
		
		if (cursor.first("inputDate")) sInputDate = (String) cursor.getValue();
		if (sInputDate == null) throw new ServiceException("isDateBefore: Missing input 'inputDate'");
		if (cursor.first("compareDate")) sCompareDate = (String) cursor.getValue();
		if (sCompareDate == null) throw new ServiceException("isDateBefore: Missing input 'compareDate'");
		if (cursor.first("dateFormat")) sDateFormat = (String) cursor.getValue();
		if (sDateFormat == null) throw new ServiceException("isDateBefore: Missing input 'dateFormat'");
		
		try {
			SimpleDateFormat f1 = new SimpleDateFormat (sDateFormat);
			java.util.Date inputDate = f1.parse(sInputDate);
			java.util.Date compareDate = f1.parse(sCompareDate);
			if (inputDate.before(compareDate)) {
				sResult = "true";
			}else{
				sResult = "false";
			}
		}catch (Exception e){
			throw new ServiceException (e.getMessage());
		}
		
		IDataUtil.put( cursor, "result", sResult);
		cursor.destroy();		
		// --- <<IS-END>> ---

                
	}
}


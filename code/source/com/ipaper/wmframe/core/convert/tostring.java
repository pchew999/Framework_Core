package com.ipaper.wmframe.core.convert;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.net.*;
// --- <<IS-END-IMPORTS>> ---

public final class tostring

{
	// ---( internal utility methods )---

	final static tostring _instance = new tostring();

	static tostring _newInstance() { return new tostring(); }

	static tostring _cast(Object o) { return (tostring)o; }

	// ---( server methods )---




	public static final void BooleanToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(BooleanToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required input
		// [i] field:0:optional returnBlankIfNull {"false","true"}
		// [o] field:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// BooleanToString - convert a boolean to a string
		//
		//		input (input,req) - boolean
		//		returnBlankIfNull (input,opt) - false (default) or true
		//
		//		output (output,str) - string integer
		Boolean bInput = null;
		String returnBlankIfNull = "false";
		if (cursor.first("input")) bInput = (Boolean) cursor.getValue();
		if (cursor.first("returnBlankIfNull")) returnBlankIfNull = IDataUtil.getString( cursor, "returnBlankIfNull" );
		
		if ( (bInput == null) && returnBlankIfNull.equals("false") ) {
			throw new ServiceException("BooleanToString: Missing input 'input'"); 
		}else if ( (bInput == null) && returnBlankIfNull.equals("true") ) {
			IDataUtil.put(cursor, "output", "");
		}else{
			String sOutput = Boolean.toString(bInput);
			IDataUtil.put(cursor, "output", sOutput);
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void DoubleToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(DoubleToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required input
		// [i] field:0:optional returnBlankIfNull {"false","true"}
		// [o] field:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// DoubleToString - convert a 64 bit double to a string
		//
		//		input (input,req) - 64 bit double
		//		returnBlankIfNull (input,opt) - false (default) or true
		//
		//		output (output,str) - string integer
		Double dInput = null;
		String returnBlankIfNull = "false";
		if (cursor.first("input")) dInput = (Double) cursor.getValue();
		if (cursor.first("returnBlankIfNull")) returnBlankIfNull = IDataUtil.getString( cursor, "returnBlankIfNull" );
		
		if ( (dInput == null) && returnBlankIfNull.equals("false") ) {
			throw new ServiceException("DoubleToString: Missing input 'input'"); 
		}else if ( (dInput == null) && returnBlankIfNull.equals("true") ) {
			IDataUtil.put(cursor, "output", "");
		}else{
			String sOutput = Double.toString(dInput);
			IDataUtil.put(cursor, "output", sOutput);
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void IntegerToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(IntegerToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required input
		// [i] field:0:optional returnBlankIfNull {"false","true"}
		// [o] field:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// IntegerToString - convert a 32 bit integer to a string
		//
		//		input (input,req) - 32 bit integer
		//		returnBlankIfNull (input,opt) - false (default) or true
		//
		//		output (output,str) - string integer
		Integer iInput = null;
		String returnBlankIfNull = "false";
		if (cursor.first("input")) iInput = (Integer) cursor.getValue();
		if (cursor.first("returnBlankIfNull")) returnBlankIfNull = IDataUtil.getString( cursor, "returnBlankIfNull" );
		
		if ( (iInput == null) && returnBlankIfNull.equals("false") ) {
			throw new ServiceException("IntegerToString: Missing input 'input'"); 
		}else if ( (iInput == null) && returnBlankIfNull.equals("true") ) {
			IDataUtil.put(cursor, "output", "");
		}else{
			String sOutput = Integer.toString(iInput);
			IDataUtil.put(cursor, "output", sOutput);
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void LongToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(LongToString)>> ---
		// @sigtype java 3.5
		// [i] object:0:required input
		// [i] field:0:optional returnBlankIfNull {"false","true"}
		// [o] field:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// LongToString - convert a 64 bit integer to a string
		//
		//		input (input,req) - 64 bit integer
		//		returnBlankIfNull (input,opt) - false (default) or true
		//
		//		output (output,str) - string integer
		Long iInput = null;
		String returnBlankIfNull = "false";
		if (cursor.first("input")) iInput = (Long) cursor.getValue();
		if (cursor.first("returnBlankIfNull")) returnBlankIfNull = IDataUtil.getString( cursor, "returnBlankIfNull" );
		
		if ( (iInput == null) && returnBlankIfNull.equals("false") ) {
			throw new ServiceException("LongToString: Missing input 'input'"); 
		}else if ( (iInput == null) && returnBlankIfNull.equals("true") ) {
			IDataUtil.put(cursor, "output", "");
		}else{
			String sOutput = Long.toString(iInput);
			IDataUtil.put(cursor, "output", sOutput);
		}
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void objectToString (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(objectToString)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] object:0:required input
		// [i] field:0:optional returnBlankIfNull {"false","true"}
		// [o] field:0:required output
		IDataCursor cursor = pipeline.getCursor();
		
		// objectToString - convert an object to a string
		//
		//		input (input,req) - object
		//		returnBlankIfNull (input,opt) - false (default) or true
		//
		//		output (output,str) - string
		String sInput = null;
		String returnBlankIfNull = "false";
		if (cursor.first("input")) sInput = IDataUtil.getString( cursor, "input" );
		if (cursor.first("returnBlankIfNull")) returnBlankIfNull = IDataUtil.getString( cursor, "returnBlankIfNull" );
		
		if ( (sInput == null) && returnBlankIfNull.equals("false") ) {
			throw new ServiceException("objectToString: Missing input 'input'"); 
		}else if ( (sInput == null) && returnBlankIfNull.equals("true") ) {
			sInput = "";
		}
		
		IDataUtil.put(cursor, "output", sInput);
		cursor.destroy(); 
		// --- <<IS-END>> ---

                
	}
}


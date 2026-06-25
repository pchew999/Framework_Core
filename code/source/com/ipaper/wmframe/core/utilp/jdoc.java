package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;
// --- <<IS-END-IMPORTS>> ---

public final class jdoc

{
	// ---( internal utility methods )---

	final static jdoc _instance = new jdoc();

	static jdoc _newInstance() { return new jdoc(); }

	static jdoc _cast(Object o) { return (jdoc)o; }

	// ---( server methods )---




	public static final void extractStrings (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(extractStrings)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required document
		// [o] field:0:required numStrings
		// [o] field:0:required logMsg
		IDataCursor cursor = pipeline.getCursor();
		//
		//  extractStrings - service to create string variables in the pipeline based on an input document
		//
		//		document (input,req) - a document that contains a set of strings that can look like:
		//									TEST_PARM1		value1
		//									TEST_PARM2		value2
		//									env/class		value3
		//									env/type		value4
		//									env/style		value5
		//									hdrs/Accept		value6
		//									hdrs/Content	value7
		//
		//		numStrings (output,str) - the number of strings extracted
		//		logMsg (output,str) - a message containing each name=val suitable for logging
		//		TEST_PARM1	value1
		//		TEST_PARM2	value2
		//		env
		//			class	value3
		//			type	value4
		//			style	value5
		//		hdrs
		//			Accept	value6
		//			Content	value7
		//
		//  this service will only look at the highest level for any strings, but if the string contains a / then it will
		//	create a document using the first part and a string underneath that for the second part, as shown above
		//
		Object oDoc = null;
		if (cursor.first("document")) oDoc = IDataUtil.get(cursor, "document");
		if (oDoc == null) throw new ServiceException("extractStrings: Missing input 'document'"); 
		if(!(oDoc instanceof IData)) throw new ServiceException("extractStrings: Input 'document' must be a document"); 
		
		String[] saOutname = new String[1000];	//create up to 1000 strings
		String[] saOutval = new String[1000];
		String sKey = null;
		Object oObj = null;
		String logMsg = "";
		IData docset[] = new IData[10];			//create up to 10 documents
		String docsetname[] = new String[10];
		int c = 0;								//maintain a count of all strings to be output to the pipeline
		IDataCursor tempCursor = null;			//define a temp cursor for general use below
		
		
		//first step is to simply collect up all the strings from the input document (strings might have a / in the string name itself)
		tempCursor = ((IData)oDoc).getCursor();
		while(tempCursor.next() != false) {
			sKey = tempCursor.getKey();						//get the item name
			oObj = tempCursor.getValue();
			if((oObj instanceof String) && (sKey.length() > 0)) {	//if the item is a string, collect it
				saOutname[c] = sKey;
				saOutval[c] = (String) oObj;
				c++;
			//}else if (oObj instanceof IData) {					//if we find another level, 
			//	IDataCursor iCursor3 = ((IData)oObj).getCursor();	// then set up a third cursor to walk through its children
			//	while(iCursor3.next() != false) {
			//		sKey = iCursor3.getKey();						//get the item name itself
			//		oObj = iCursor3.getValue();
			//		if((oObj instanceof String) && (sKey.length() > 0)) {	//collect only strings
			//			sVal = (String) oObj;
			//			saOutname[c] = sKey;
			//			saOutval[c] = sVal;
			//			c++;
			//		}
			//	}
			//	iCursor3.destroy();
			}
		}
		tempCursor.destroy();
		
		
		//now we will go through the strings that we have collected and figure out if we should output a simple string or a document
		int d = 0;
		for(int x = 0; x < c; x++) {
		
			if(saOutname[x].indexOf("/") > 0) {						//if we collected a document style string that has a / in the
				String items[] = saOutname[x].split("/", 2);		// name, then split the name
				boolean found = false;
				int di = 0;
				for(di = 0; di < d; di++) {
					if(docsetname[di].equals(items[0])) {
						found = true;
						break;
					}
				}
				if(!found) {										//if we didnt find the document name in our list, then create
					docset[di] = IDataFactory.create();				// a new document and add the name to our list
					docsetname[di] = items[0];
					d++;
				}
				tempCursor = docset[di].getCursor();				//now add the string name and its value to the document
				IDataUtil.put( tempCursor, items[1], saOutval[x] );
				tempCursor.destroy();
			}else{													//if we only collected a simple string
				IDataUtil.put(cursor, saOutname[x], saOutval[x]);	// then simply output the string name and its value
			}
		
			logMsg = logMsg + " " + saOutname[x] + "=" + saOutval[x] + "\r\n";
		
		}
		
		
		//finally, output the count of strings, a log message, and any documents we created in the above loop
		IDataUtil.put(cursor, "numStrings", "" + c);	//output the count of strings
		IDataUtil.put(cursor, "logMsg", logMsg);		//output the log message
		for(int di = 0; di < d; di++) {					//output any documents we created above
			IDataUtil.put( cursor, docsetname[di], docset[di] );
		}
		
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void getStrings (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getStrings)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:0:required document
		// [o] field:0:required strings
		IDataCursor cursor = pipeline.getCursor();
		//
		//  getStrings - service to create string variables in the pipeline based on an input document
		//
		//		document (input,req) - a document that contains strings
		//
		//		strings (output,str) - a string containing each name=val suitable for logging, each separated by \r\n
		//
		//  this service will only look at the highest level of the input document
		//
		//input
		boolean found = false;
		if (cursor.first("document")) found = true;
		
		String[] saOutname = new String[1000];
		String[] saOutval = new String[1000];
		String sKey = "kkk";
		Object oObj = null;
		String sVal = "vvv";
		String strings = "";
		
		int i = 0;
		
		if(found) {
		Object oDoc = IDataUtil.get(cursor, "document");	//get the input document
		if(oDoc instanceof IData) {							//make sure its actually a document
		
		  IDataCursor cursor2 = ((IData)oDoc).getCursor();	//set up a second cursor to walk through its children
		  while(cursor2.next() != false) {
		    sKey = cursor2.getKey();						//get the item name itself
		    oObj = cursor2.getValue();
		    if(oObj instanceof String) {					//collect only strings
		      sVal = (String) oObj;
		      saOutname[i] = sKey;
		      saOutval[i] = sVal;
		      i = i + 1;
		    }
		  }
		  cursor2.destroy();
		
		}
		}
		
		//IDataUtil.put(cursor, "numStrings", "" + i);	//count of strings
		for(int x = 0; x < i; x++) {					//output everything we collected
		  //IDataUtil.put(cursor, saOutname[x], saOutval[x]);
			strings = strings + saOutname[x] + "=" + saOutval[x] + "\r\n";
		}
		IDataUtil.put(cursor, "strings", strings);
		
		cursor.destroy();
			
		// --- <<IS-END>> ---

                
	}
}


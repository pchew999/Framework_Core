package com.ipaper.wmframe.core;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
// --- <<IS-END-IMPORTS>> ---

public final class sort

{
	// ---( internal utility methods )---

	final static sort _instance = new sort();

	static sort _newInstance() { return new sort(); }

	static sort _cast(Object o) { return (sort)o; }

	// ---( server methods )---




	public static final void sortDoc (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sortDoc)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] record:1:required docList
		// [i] field:0:required keyField
		// [i] field:0:required sortOrder {"ascending","descending"}
		// [o] record:1:required docList
		IDataCursor cursor = pipeline.getCursor();
		//
		//  sortDoc - service to sort a doc list IN PLACE - note the input and output are the same
		//
		//		docList (input,req) - a document list that contains a set of fields
		//		keyField (input,req) - the name of one of the fields in the document list on which to sort the list
		//		sortOrder (input,req) - ascending / descending - there is no default
		//
		//		docList (output) - the SAME document list, now sorted
		IData[] docList = null;
		String keyField = null;
		String sortOrder = null;
		if (cursor.first("docList")) docList = IDataUtil.getIDataArray( cursor, "docList" );
		if (docList == null) throw new ServiceException("sortDoc: Missing input 'docList'");
		if (cursor.first("keyField")) keyField = (String) cursor.getValue();
		if (keyField == null) throw new ServiceException("sortDoc: Missing input 'keyField'");
		if (cursor.first("sortOrder")) sortOrder = (String) cursor.getValue();
		if (sortOrder == null) throw new ServiceException("sortDoc: Missing input 'sortOrder'");
		
		//		IData[] sortedItemList =
			IDataUtil.sortIDataArrayByKey(docList,
											keyField,  
											IDataUtil.COMPARE_TYPE_COLLATION,  
											null,  
											sortOrder.equals("descending")); 
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void sortStringlist (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sortStringlist)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:1:required stringList
		// [i] field:0:required sortOrder {"ascending","descending"}
		// [o] field:1:required stringList
		IDataCursor cursor = pipeline.getCursor();
		//
		//  sortStringlist - service to sort a doc list IN PLACE - note the input and output are the same
		//
		//		stringList (input,req) - a string list to sort
		//		sortOrder (input,req) - ascending / descending - there is no default
		//
		//		stringList (output) - the SAME string list, now sorted
		String[] stringList = null;
		String sortOrder = null;
		if (cursor.first("stringList")) stringList = IDataUtil.getStringArray( cursor, "stringList" );
		if (stringList == null) throw new ServiceException("sortStringlist: Missing input 'stringList'");
		if (cursor.first("sortOrder")) sortOrder = (String) cursor.getValue();
		if (sortOrder == null) throw new ServiceException("sortStringlist: Missing input 'sortOrder'");
		
		//String[] sortedList = stringList.clone();    we'll just sort in place to conserve memory
		if (sortOrder.equalsIgnoreCase("ascending")) {
		    Arrays.sort(stringList);
		} else {
		    Arrays.sort(stringList, Collections.reverseOrder());
		}
		   
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


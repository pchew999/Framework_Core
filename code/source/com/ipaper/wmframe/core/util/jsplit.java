package com.ipaper.wmframe.core.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.regex.*;
// --- <<IS-END-IMPORTS>> ---

public final class jsplit

{
	// ---( internal utility methods )---

	final static jsplit _instance = new jsplit();

	static jsplit _newInstance() { return new jsplit(); }

	static jsplit _cast(Object o) { return (jsplit)o; }

	// ---( server methods )---




	public static final void regexSplit (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(regexSplit)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required input
		// [i] field:0:required regex
		// [i] field:0:optional limit
		// [o] field:1:required items
		// [o] field:0:required length
		IDataCursor cursor = pipeline.getCursor();
		
		// regexSplit - performs a proper split of a string using regex (the standard java tokenize method
		//				has a HUGE bug in it where blank strings are removed)
		//
		//		input (input,req) - the input string to split
		//		regex (input,req) - regular expression (or just a character on which to split)
		//		limit (input,opt) - <0 or null means no limit (default)
		//							0 means no limit but trailing empty strings will be removed
		//							>0 upper limit of split (maximum number of items generated)
		//
		//		items (output,array) - a string list of the split items
		//		length (output,str) - the size of the items array
		
		String input = null;
		String regex = null;
		String limit = null;
		if (cursor.first("input")) input = (String) cursor.getValue();
		if (input == null) throw new ServiceException("regexSplit: Missing input 'input'"); 
		if (cursor.first("regex")) regex = (String) cursor.getValue();
		if (regex == null) throw new ServiceException("regexSplit: Missing input 'regex'"); 
		if (cursor.first("limit")) limit = (String) cursor.getValue();
		if (limit == null) limit = "-1"; 
		
		//Pattern pattern = Pattern.compile(regex);
		//String items[] = pattern.split(input);
		String items[] = input.split(regex, Integer.parseInt(limit));
		int length = items.length;
		
		IDataUtil.put(cursor, "items", items);
		IDataUtil.put(cursor, "length", "" + length);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void regexSplitBetween (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(regexSplitBetween)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required input
		// [i] field:0:required regex1
		// [i] field:0:required regex2
		// [o] field:1:required items
		// [o] field:0:required length
		IDataCursor cursor = pipeline.getCursor();
		
		// regexSplitBetween - performs a split of the input between the two regex's, preserving the 
		//					   expressions themselves
		//
		//		input (input,req) - the input string to split
		//		regex1 (input,req) - regular expression (first half of the delimiter on which to split)
		//		regex2 (input,req) - regular expression (second half of the delimiter on which to split)
		//
		//		items (output,array) - a string list of the split items
		//		length (output,str) - the count of split items
		//
		//	Note that this method preserves the content of the delimiter in the output, contrary to the
		//	traditional split, which removes the delimiter - it also performs escaping on both halves of the
		//	delimiter such that \r, \n and \t as literally seen in the delimiter get translated to real carriage
		// returns, linefeeds and tabs
		
		String input = null;
		String regex1 = null;
		String regex2 = null;
		if (cursor.first("input")) input = (String) cursor.getValue();
		if (input == null) throw new ServiceException("regexSplitBetween: Missing input 'input'"); 
		if (cursor.first("regex1")) regex1 = (String) cursor.getValue();
		if (regex1 == null) throw new ServiceException("regexSplitBetween: Missing input 'regex1'"); 
		if (cursor.first("regex2")) regex2 = (String) cursor.getValue();
		if (regex2 == null) throw new ServiceException("regexSplitBetween: Missing input 'regex2'"); 
		String regex = regex1 + regex2;			//combine the two regex's to create a single delimiter for splitting
		
		String items[] = input.split(regex, -1);
		int length = items.length;
		if(length > 0) {
			regex1 = regex1.replaceAll("\\\\t", "\t");	// substitute real values for any escaped characters - there
			regex1 = regex1.replaceAll("\\\\r", "\r");	// are four \'s in each because both the compiler and
			regex1 = regex1.replaceAll("\\\\n", "\n");	// replaceAll do escaping (actually it is the regex engine
			//regex1 = regex1.replaceAll("\\\\", "");	// that does the second escaping)
			regex2 = regex2.replaceAll("\\\\t", "\t");
			regex2 = regex2.replaceAll("\\\\r", "\r");
			regex2 = regex2.replaceAll("\\\\n", "\n");
			//regex2 = regex2.replaceAll("\\\\", "");
			if( length > 1) {
				items[0] = items[0] + regex1;					// append the suffix to the first output item
				for (int i = 1; i < length - 1; i++) {
					items[i] = regex2 + items[i] + regex1;		// prepend and append to the items in the middle
				}
				items[length - 1] = regex2 + items[length - 1];	// prepend the prefix to the last item
			}
		}
		
		IDataUtil.put(cursor, "items", items);
		IDataUtil.put(cursor, "length", "" + length);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


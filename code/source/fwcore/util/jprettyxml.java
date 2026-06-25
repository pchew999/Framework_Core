package fwcore.util;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.StringTokenizer;
import java.text.*;
import java.util.Date;
// --- <<IS-END-IMPORTS>> ---

public final class jprettyxml

{
	// ---( internal utility methods )---

	final static jprettyxml _instance = new jprettyxml();

	static jprettyxml _newInstance() { return new jprettyxml(); }

	static jprettyxml _cast(Object o) { return (jprettyxml)o; }

	// ---( server methods )---




	public static final void prettyXml (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(prettyXml)>> ---
		// @sigtype java 3.5
		// [i] field:0:required inXml
		// [o] field:0:required outXml
		IDataCursor cursor = pipeline.getCursor();
		//
		// prettyXml - this is a really basic xml formatter - it is intended to be used for logging
		//		and general readability - do NOT use the output of this service if the content might
		//		contain important whitespace that the target application might need, since all
		//		extraneous whitespace, such as leading/trailing spaces, is removed
		//
		//		inXml (input,opt) - an xml string to convert
		//		outXml (output,str) - formatted xml
		//
		String sInXml = null;
		
		if (cursor.first("inXml")) sInXml = (String) cursor.getValue();
		if (sInXml == null) throw new ServiceException("prettyXml: 'inXml' required");
		
		int stack = 0;
		StringBuilder pretty = new StringBuilder();
		String[] rows = sInXml.trim().replaceAll(">", ">\n").replaceAll("<", "\n<").split("\n");
		
		for (int i = 0; i < rows.length; i++) {
		    if (rows[i] == null || rows[i].trim().length() == 0) continue;
		
		    String row = rows[i].trim();
		    if (row.startsWith("<?")) {
		        pretty.append(row + "\n");
		    } else if (row.startsWith("</")) {
		        String indent = repeatString(--stack);
		        pretty.append(indent + row + "\n");
		    } else if (row.startsWith("<") && row.endsWith("/>") == false) {
		        String indent = repeatString(stack++);
		        pretty.append(indent + row + "\n");
		        if (row.endsWith("]]>")) stack--;
		    } else {
		        String indent = repeatString(stack);
		        pretty.append(indent + row + "\n");
		    }
		}
		
		//return the result
		IDataUtil.put(cursor, "outXml", (String)pretty.toString().trim());
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static String repeatString(int stack) {
	 StringBuilder indent = new StringBuilder();
	 for (int i = 0; i < stack; i++) {
	    indent.append("  ");
	 }
	 return indent.toString();
	} 
		
	// --- <<IS-END-SHARED>> ---
}


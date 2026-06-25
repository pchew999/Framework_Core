package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.utils.PdfMerger;
// --- <<IS-END-IMPORTS>> ---

public final class jpdf

{
	// ---( internal utility methods )---

	final static jpdf _instance = new jpdf();

	static jpdf _newInstance() { return new jpdf(); }

	static jpdf _cast(Object o) { return (jpdf)o; }

	// ---( server methods )---




	public static final void stringToPdfBytearray (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(stringToPdfBytearray)>> ---
		// @sigtype java 3.5
		// [i] field:0:required string
		// [o] object:0:required bytearray
		IDataCursor cursor = pipeline.getCursor();
		
		// This service takes an input string "content" and makes a pdf out of it
		// The output is the bytearray of the pdf
		
		String sContent = null;
		if (cursor.first("string")) sContent = (String) cursor.getValue();
		if (sContent == null) throw new ServiceException("login: Missing input 'string'");
		
		try {
			
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			PdfWriter writer = new PdfWriter(stream);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);
			document.add(new Paragraph(sContent));
			document.close();
			
			IDataUtil.put(cursor, "bytearray", stream.toByteArray());
			
			    
		} catch (Exception e) {
			
		}
				
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


package com.ipaper.wmframe.core.excel;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
// --- <<IS-END-IMPORTS>> ---

public final class hssfw

{
	// ---( internal utility methods )---

	final static hssfw _instance = new hssfw();

	static hssfw _newInstance() { return new hssfw(); }

	static hssfw _cast(Object o) { return (hssfw)o; }

	// ---( server methods )---




	public static final void writeXLS (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(writeXLS)>> ---
		// @sigtype java 3.5
		// [i] field:0:required filename
		// [i] field:1:required spreadsheetData
		// [i] field:0:required spreadsheetName
		// [o] field:0:required status
		// get our input fields from the pipeline  
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	filename = IDataUtil.getString( pipelineCursor, "filename" );
		String[]	spreadsheetData = IDataUtil.getStringArray( pipelineCursor, "spreadsheetData" );
		////String	filepath = IDataUtil.getString( pipelineCursor, "filepath" );
		String	spreadsheetName = IDataUtil.getString( pipelineCursor, "spreadsheetName" );
		pipelineCursor.destroy();
		
		//file.createNewFile(); 
		 Workbook writableWorkbook = new HSSFWorkbook(); 
		 CreationHelper createHelper = writableWorkbook.getCreationHelper();
		 
		 Sheet writableSheet = writableWorkbook.createSheet(spreadsheetName); 
		
		 // for each row in spreadsheetData, split the comma delimited values and stick them 
		 // in a cell.  
		 for(int x =0; x < spreadsheetData.length; ++x)
		 {
			 Row row = writableSheet.createRow((short) x);
			 String[] rowArray = spreadsheetData[x].split("\\|");
			 for(int y=0;y<rowArray.length; ++y)
			 {
				 // now process this cell
				 Cell thisCell = row.createCell(y); //.setCellValue(rowArray[y]);
				 // make sure rowArray[y] meets the import standards before writing it out
				 // rowArray[y] = rowArray[y].replace("!", ",");
				 
				 thisCell.setCellValue(rowArray[y]);
			 }
		 }
		 
		 
		 // Or do it on one line.
		//		 createHelper.createRichTextString("This is a string"));
		 
		 
		 String status = new String();
		try
		{
		 FileOutputStream  file = new FileOutputStream(filename);
		 writableWorkbook.write(file); 
		 writableWorkbook.close();
		 file.close();
		 status = "true";
		}
		catch(Exception e)
		{
		 status = "false";	
		}
		finally
		{
			// put our status in the pipeline
			IDataCursor pipelineCursor_1 = pipeline.getCursor();
			IDataUtil.put( pipelineCursor_1, "status", status );
			pipelineCursor_1.destroy();
		}
		// --- <<IS-END>> ---

                
	}
}


package com.ipaper.wmframe.core.excel;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.util.*;
import java.math.*;
import java.text.*;
import org.apache.poi.hssf.usermodel.*;
// --- <<IS-END-IMPORTS>> ---

public final class hssf

{
	// ---( internal utility methods )---

	final static hssf _instance = new hssf();

	static hssf _newInstance() { return new hssf(); }

	static hssf _cast(Object o) { return (hssf)o; }

	// ---( server methods )---




	public static final void readXLS (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(readXLS)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional pathfilename
		// [i] object:0:optional stream
		// [i] field:0:required sheetnum {"$all","1","2","3","4","5","6","7","8","9","10"}
		// [i] field:0:required useheaderrow {"true","false"}
		// [i] field:0:required usesheetnames {"true","false"}
		// [i] field:0:required loadAs {"document","csv"}
		// [o] record:1:optional docList
		// [o] field:1:optional csvList
		// [o] field:0:required numDocs
		IDataCursor cursor = pipeline.getCursor();
		
		// readXLS - reads a microsoft excel XLS spreadsheet (.xls 2003 and older)
		//
		//	Uses the Apache POI implementation (see https://en.wikipedia.org/wiki/Apache_POI and https://poi.apache.org/changes.html)
		//	as of Feb 2019, we are running version 3.17 of POI since 4.00+ seems 'too new'.  The following jars are required:
		//				commons-collections4-4.1.jar
		//				poi-3.17-20170915.jar
		//				poi-ooxml-3.17-20170908.jar
		//				poi-ooxml-schemas-3.17-20170908.jar
		//
		//	Note that this and readXLSX should be identical in every regard except for the references to hssf/xssf
		//
		//		pathfilename (input,opt) - full path of input filename OR
		//		stream (input,req) - file stream
		//
		//		sheetnum (input,req) - either $all or a specific sheet number 1-n
		//		useheaderrow (input,req) - true/false - use the 1st row of the sheet(s) as field names in the output
		//		usesheetnames (input,req) - true/false - use the name of the sheet in the output
		//												if loadAs=document, the sheet name will be applied in the document itself
		//												if loadAs=csv, the sheet name will be placed in the first row
		//		loadAs (input,req) - document/csv - return the output as a document list or as a csv string list
		//
		//		docList (output,array) - a document list, one for each sheet in the spreadsheet  OR
		//		csvList (output,array) - a string list of CSV files, one for each sheet in the spreadsheet (tab and LF delimited)
		//
		//	Modified 8/16/22 to stop using the first row in the sheet to determine the number of columns - instead guess from 1000 columns backwards
		//
		String sPathfilename = null;
		BufferedInputStream	fileStream = null;
		if (cursor.first("pathfilename")) sPathfilename = (String) cursor.getValue();
		if (cursor.first("stream")) fileStream = new BufferedInputStream((FileInputStream) cursor.getValue());
		if (sPathfilename == null && fileStream == null) throw new ServiceException("readXLS: 'pathfilename' or 'stream' required");
		if (sPathfilename != null && fileStream != null) throw new ServiceException("readXLS: ambiguous input 'pathfilename' and 'stream' both supplied");
		
		String sheetNum = null;
		String useHeaderRow = null;
		String useSheetNames = null;
		String loadAs = null;
		boolean loadAsDoc = false, loadAsCSV = false;
		if (cursor.first("sheetnum")) sheetNum = (String) cursor.getValue();
		if (sheetNum == null) throw new ServiceException("readXLS: input parameter 'sheetnum' required");
		if (cursor.first("useheaderrow")) useHeaderRow = (String) cursor.getValue();
		if (useHeaderRow == null) throw new ServiceException("readXLS: input parameter 'useheaderrow' required");
		if (cursor.first("usesheetnames")) useSheetNames = (String) cursor.getValue();
		if (useSheetNames == null) throw new ServiceException("readXLS: input parameter 'useSheetNames' required");
		if (cursor.first("loadAs")) loadAs = (String) cursor.getValue();
		if (loadAs == null) throw new ServiceException("readXLS: input parameter 'loadAs' required");
		if( loadAs.equals("document") ) loadAsDoc = true;
		if( loadAs.equals("csv") ) loadAsCSV = true;
		
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		
		IData[]	row_list = null;
		IData[] doc_list = null;
		String[] csv_list = null;
		StringBuilder sb = null;
		
		int sheetlow = 0, sheethi = 0;
		
		try	{
			if (sPathfilename != null)	wb = new HSSFWorkbook(new FileInputStream(sPathfilename));
			if (fileStream != null)		wb = new HSSFWorkbook(fileStream);
		
			//set up the range of sheets we want to return, either one specific sheet or all sheets
			int sheet_count = wb.getNumberOfSheets();
			if( sheetNum.equals("$all") ) {
				sheetlow = 0; sheethi = sheet_count - 1;
			}else{
				sheetlow = Integer.parseInt(sheetNum) - 1;  sheethi = sheetlow;
			}
			if (sheethi + 1 > sheet_count) throw new ServiceException("readXLS: input parameter 'sheetNum' exceeds number of available sheets");
		
			//start the output based on what the caller wants
			if( loadAsDoc )	doc_list = new IData[sheethi - sheetlow + 1];
		    if( loadAsCSV )	csv_list = new String[sheethi - sheetlow + 1];
		
			//go through the spreadsheet and extract each of the sheets that was requested
			for(int sheetnm = sheetlow; sheetnm <= sheethi; sheetnm++) {
				if( loadAsDoc )	doc_list[sheetnm - sheetlow] = IDataFactory.create();
				if( loadAsCSV ) sb = new StringBuilder();
		
			    sheet = wb.getSheetAt(sheetnm);
			    String sheetname = null;
			    if(useSheetNames.equals("true")) {
			    	sheetname = fix_name(sheet.getSheetName());
			    }else{
			    	sheetname = "rowlist";
			    }
				if( loadAsCSV ) sb.append("SheetName\t" + sheetname + "\n");
		
			    String cval = null;
		
		
				// trim the size of the sheet down in case any stray blank lines or columns exist
				int col_count = 1000;	// sheet.getRow(0).getPhysicalNumberOfCells();	//use the first row in the sheet to determine the # of columns
				int	row_count = sheet.getPhysicalNumberOfRows();
				int maxr = -1;
				for (int r = row_count + 13; r >= 0; r--) {		// why go beyond the 'end' of the worksheet?  i don't know, because people are idiots
					row = sheet.getRow(r);
					if (row != null) {
						for (int c = 0; c <= col_count - 1; c++) {
							cell = row.getCell(c);
				        	if (cell != null && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {		//CellType.BLANK
								maxr = r;
								break;
				        	}
						}
						if(maxr > -1) break;
					}
				}
		
				int maxc = -1;
				for (int r = 0; r <= maxr; r++) {
					row = sheet.getRow(r);
					if (row != null) {
						for (int c = col_count - 1; c >= 0; c--) {
							cell = row.getCell(c);
				        	if (cell != null && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {		//CellType.BLANK
								if(c > maxc) maxc = c;
								break;
				        	}
						}
					}
				}
		//IDataUtil.put( cursor, "maxc", ""+maxc );
		//IDataUtil.put( cursor, "maxr", ""+maxr );
				if (maxc == -1 || maxr == -1) throw new ServiceException("readXLS: spreadsheet appears to be empty");
		
		
			// build a column name list using either the default column names from excel, or the first row of the sheet
				if( loadAsCSV ) csv_list[sheetnm - sheetlow] = "";
				String[] col_name = new String[maxc+1];
				for (int c = 0; c <= maxc; c++) {
					int div = c / 26;
					int rem = c % 26;
					if(c < 26)  col_name[c] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(rem, rem+1);	//default column names
					if(c >= 26) col_name[c] = "xABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(div, div+1)+"ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(rem, rem+1);
					row = sheet.getRow(0);
					if(row != null) {
						cell = row.getCell(c);	// first row of the sheet
						if (cell != null && useHeaderRow.equals("true")) {
							if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) col_name[c] = fix_name(cell.getStringCellValue());
							if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) col_name[c] = fix_name(""+cell.getNumericCellValue());
						}
					}
					if( loadAsCSV ) sb.append(col_name[c]);
					if( loadAsCSV ) if( c < maxc) sb.append("\t");
			    }
				if( loadAsCSV ) sb.append("\n");
		
		
			// now read the sheet and create records
				int s=0;	//set the starting row to 0 or 1 depending on the caller's selection
				if (useHeaderRow.equals("true")) s=1;
				if( loadAsDoc ) row_list = new IData[maxr+1-s];
		
				for (int r = s; r <= maxr; r++) {
		
					row = sheet.getRow(r);
					IDataCursor row_list_cursor = null;
					if(row != null) {
		
						if( loadAsDoc ) {
							row_list[r-s] = IDataFactory.create();
							row_list_cursor = row_list[r-s].getCursor();
						}
		
						for (int c = 0; c <= maxc; c++) {
							cell = row.getCell(c);
							if (cell != null) {
								switch (cell.getCellType()) {
									case HSSFCell.CELL_TYPE_STRING:			//CellType.STRING
										cval = cell.getStringCellValue();
										break;
		
									case HSSFCell.CELL_TYPE_NUMERIC:		//CellType.NUMERIC or CellType.BOOLEAN
										Double dval = cell.getNumericCellValue();
										if (isHSSFCellDateFormatted(cell)) {
											org.apache.poi.ss.usermodel.DataFormatter hdf = new org.apache.poi.ss.usermodel.DataFormatter();	//format it like excel does
											cval = hdf.formatCellValue(cell);
										}else{
											DecimalFormat df = new DecimalFormat("0");
											df.setMaximumFractionDigits(15);					//max=340
											cval = df.format(dval);
										}
										break;
		
									case HSSFCell.CELL_TYPE_BLANK:			//CellType.BLANK
										cval = "";
										break;
		
									default:
										cval = ""+cell.getCellType();
										break;
								}
						 	}else{
								cval = "";
							}
		
							if( loadAsDoc ) row_list_cursor.insertAfter( col_name[c], cval );
							if( loadAsCSV ) sb.append(cval);
							if( loadAsCSV ) if ( c < maxc ) sb.append("\t");
		
						}//endfor c
		
					}//if row != null
		
					if( loadAsCSV )      if (r < maxr) sb.append("\n");
		
				}//endfor r
		
				if( loadAsDoc ) {
					IDataCursor doc_list_cursor = doc_list[sheetnm - sheetlow].getCursor();
					IDataUtil.put(doc_list_cursor, sheetname, row_list);
					doc_list_cursor.destroy();
				}
		
				if( loadAsCSV ) csv_list[sheetnm - sheetlow] = sb.toString();
		
			}//endfor sheet
		
		} catch (Exception e) {
			throw new ServiceException(e);	//e.printStackTrace();
		}
		
		if( loadAsDoc ) IDataUtil.put( cursor, "docList", doc_list );
		if( loadAsCSV ) IDataUtil.put( cursor, "csvList", csv_list );
		IDataUtil.put( cursor, "numDocs", ""+(sheethi - sheetlow + 1) );
		
		cursor.destroy();
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	public static String fix_name(String str)	{
		str = str.replaceAll("[^A-Z|a-z|0-9|#|\\-|_|\\.]", " ");	//replace any non-normal characters with space
		str = str.trim();											//trim all leading and trailing spaces
		str = str.replaceAll(" +", "_");							//replace any remaining multiple spaces with a single _
		return str;
	}
	
	public static boolean isValidExcelDate(double value) {		//checks if it is a valid Excel date
		return (value > -Double.MIN_VALUE);
	}
	
	public static boolean isHSSFCellDateFormatted(HSSFCell cell) {	//determine if the cell is a date (rather than just a number)
		boolean bIsDate = false;
	
		double d = cell.getNumericCellValue();
		if ( isValidExcelDate(d) ) {
			HSSFCellStyle style = cell.getCellStyle();
			int i = style.getDataFormat();
			switch(i) {		// Internal Date Formats as described on page 427 in Microsoft Excel Dev's Kit...
				case 0x0e:
				case 0x0f:
				case 0x10:
				case 0x11:
				case 0x12:
				case 0x13:
				case 0x14:
				case 0x15:
				case 0x16:
				case 0x2d:
				case 0x2e:
				case 0x2f:
					bIsDate = true;
				break;
			}
		}
		return bIsDate;
	}
	// --- <<IS-END-SHARED>> ---
}


package com.ipaper.wmframe.core.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jpipeline

{
	// ---( internal utility methods )---

	final static jpipeline _instance = new jpipeline();

	static jpipeline _newInstance() { return new jpipeline(); }

	static jpipeline _cast(Object o) { return (jpipeline)o; }

	// ---( server methods )---




	public static final void readPipeline (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(readPipeline)>> ---
		// @sigtype java 3.5
		// [o] record:0:required pipeline
		IDataCursor cursor = pipeline.getCursor();
		
		// readPipeline - read the pipeline into its own document
		//
		//		pipeline (output,document) - the contents of the pipeline
		
		IData pipelineData = IDataFactory.create();
		IDataCursor pipelineDataCursor = pipelineData.getCursor();
		
		cursor.first();
		do {
			IDataUtil.put(pipelineDataCursor,cursor.getKey(), cursor.getValue());
			pipelineDataCursor.next();
		} while (cursor.next());
		
		pipelineDataCursor.destroy();
		
		IDataUtil.put(cursor,"pipeline",pipelineData);
		
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


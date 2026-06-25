package fwcore.utilp;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.entrust.toolkit.archive.PemHeader;
import com.entrust.toolkit.credentials.PKCS12Reader;
import com.wm.util.Base64;
import iaik.x509.CertificateFactory.*;
import iaik.x509.X509Certificate.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
// --- <<IS-END-IMPORTS>> ---

public final class jcertconvert

{
	// ---( internal utility methods )---

	final static jcertconvert _instance = new jcertconvert();

	static jcertconvert _newInstance() { return new jcertconvert(); }

	static jcertconvert _cast(Object o) { return (jcertconvert)o; }

	// ---( server methods )---




	public static final void certConvertToX509 (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(certConvertToX509)>> ---
		// @sigtype java 3.5
		// [i] object:0:required certBase64Decode
		// [o] object:0:required x509Certificate
		IDataCursor cursor = pipeline.getCursor();
		
		byte[]  sCertString = null;
		if (cursor.first("certBase64Decode"))   sCertString = (byte[]) cursor.getValue();
		
		X509Certificate certificate = null;
		CertificateFactory cf = null;
		try {
		    if (sCertString != null ) {
		    	cf = CertificateFactory.getInstance("X509");
		        certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(sCertString));
		    }
		} catch (CertificateException e) {
		    //throw new CertificateException(e);
		}
		   
		
		IDataUtil.put(cursor, "x509Certificate", certificate);
		cursor.destroy();
		   
		// --- <<IS-END>> ---

                
	}
}


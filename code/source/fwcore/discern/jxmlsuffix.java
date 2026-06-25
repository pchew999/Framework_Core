package fwcore.discern;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
// --- <<IS-END-IMPORTS>> ---

public final class jxmlsuffix

{
	// ---( internal utility methods )---

	final static jxmlsuffix _instance = new jxmlsuffix();

	static jxmlsuffix _newInstance() { return new jxmlsuffix(); }

	static jxmlsuffix _cast(Object o) { return (jxmlsuffix)o; }

	// ---( server methods )---




	public static final void genXmlSuffix (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(genXmlSuffix)>> ---
		// @sigtype java 3.5
		// [i] field:0:required xmlfrag
		// [o] field:0:required xmlsuff
		IDataCursor cursor = pipeline.getCursor();
		
		// genXmlSuffix - generate an appropriate suffix for a fragment of xml
		//
		//		xmlfrag (input,req) - a leading fragment of xml
		//
		//		xmlsuff (output,str) - a suffix that can be appended to xmlfrag making it well-formed
		//
		//	This service will generate an xml suffix for the supplied xml fragment - the fragment 
		//	should be a truncated (or whole) string representing the leading portion of any well-formed
		//	xml, without references to DTDs or schemas - the idea behind this service is that you can 
		//	load any truncated leading portion of any size xml document and call this service to generate 
		//	a suffix appropriate for appending, such that the 2 pieces together will load as a proper 
		//	document to the xmlStringToXMLNode service - it is assumed that this will allow the caller
		//  to use the resulting xml to then scan the envelope portion and other values located near
		//  the top of the xml for key pieces of information, even in the largest of documents.
		//
		//	This is implemented as a Finite State Machine, where:
		//
		//		lState = 0		we are skipping input
		//		lState = 1		we are collecting an element name
		//		lState = 2		we are collecting the closing element name
		//		lState = 3		we are collecting a comment (exclamation point)
		//		lState = 4		we are collecting somewhere within the attribute section of an element
		//		lState = 5		we are collecting the attribute name itself
		//		lState = 6		we are collecting the spaces between the attribute name and its = sign
		//		lState = 7		we are collecting the = sign of an attribute
		//		lState = 8		we are collecting an attribute value within ' quotes
		//		lState = 9		we are collecting an attribute value within " quotes
		//		lState = 10		we are collecting a special xml tag terminated by a ; semi-colon
		//
		//	Keep in mind that the goal of this service is to generate a string suffix that can be
		//	appended to the input xml fragment, *regardless* of where the input xml has been truncated.
		//	The goal is not to try and generate 'business level' validated xml, only well-formed xml.
		//
		String sXmlfrag = null;	//input
		String sXmlsuff = "";	//output
		String[] saTag = new String[99];
		int lTagindex = 0;
		long lState = 0;
		int i = 0;
		char c = 0;
		int x = 0;
		
		if (cursor.first("xmlfrag")) sXmlfrag = (String) cursor.getValue();
		if (sXmlfrag == null) throw new ServiceException("vjGenXmlSuffix: Missing input 'xmlfrag'"); 
		
		for ( x = 0; x < 99; x++ ) saTag[x] = "";		// init our tag array
		
		i = sXmlfrag.indexOf("?>");						// skip past any possible xml header
		i = ( i > 0 ) ? i = i + 2 : 0;
		
		for(; i < sXmlfrag.length(); i++) {
		
			c = sXmlfrag.charAt(i);
		
			if ( c == '<') {
				if ( lState != 8 && lState != 9 ) {		// as long as we arent embedded in quotes, we
					lState = 1;							//  are going to start collecting an element name
				}
			}else if (c == '!' ) {
				if ( lState == 1 ) {					// if we were collecting an element name
					lState = 3;							//  then switch to collecting a comment
				}
			}else if ( c == '/' ) {
				if ( lState == 4 ) {					// if we were processing the attribute section
					lTagindex--;						//  then adjust the pointer to our saved list of names
					lState = 1;							//  and immediately switch to processing the element name again and fall thru to the next if statement
				}
				if ( lState == 1) {						// if we were collecting an element name
					lState = 2;							// then switch that to collecting the element name's close
					if ( !saTag[lTagindex].equals("") ) {	// the trick here is that if we have just collected the name, then
						saTag[lTagindex] = "";				// the / is at the end of the element and we don't need the name anymore
					}else{
						lTagindex--;						// otherwise the / is at the beginning of the element, and we got the 
					}										// element name a long time ago, so just adjust our pointer to point to it
				}
			}else if ( c == '>' ) {
		        if ( lState == 1 ) {			// if we were collecting an element name, then we need
		            lTagindex++;				//  to close that off and hold onto it
		            lState = 0;      			//  and go back to just skipping along
		        }
		        if ( lState == 2 ) {			// if we were collecting a closing element name, we already
		            lState = 0;					//  got that awhile ago, so just go back to skipping along
		        }
		        if ( lState == 3 ) {			// if we were collecting a comment, then just
		        	lState = 0;					//  go back to skipping along
		        }
		        if ( lState == 4 ) {			// if we were processing the attribute section of
		            lState = 0;					//  an element, then go back to skipping along
		        }
			}else if ( c == ' ' ) {
		        if (lState == 1 ) {				// if we were collecting the element name, we are now
		            lTagindex++;				//  done with it, so bump up our pointer and
		            lState = 4;					//  set our state to scanning the attribute section
		        }
		        if ( lState == 5 ) {			// if we were collecting the attribute name, then switch
		            lState = 6;					//  to empty space between the attr name and the =
		        }
			}else if ( c == '=' ) {
		        if ( lState == 5 ) {			// if we were collecting an attribute name, then switch
		            lState = 7;					//  to the = between the attribute name and its value
		        }
		        if ( lState == 6 ) {			// if we were skipping the spaces between the attribute name
		            lState = 7;					//  and the = then switch to the = also
		        }
			}else if ( c == '\'' ) {
		        if ( lState == 7 ) {			// if we just processed the attribute = then this
		            lState = 8;					//  is an open quote ' so switch to skipping the text of the attribute's value
		        } else if ( lState == 8 ) {		// if we were skipping the text of the attribute's value
		            lState = 4;					//  then this is the close quote ' so switch back to the attribute section of an element
		        }
			}else if ( c == '"' ) {
		        if ( lState == 7 ) {			// if we just processed the attribute = then this
		            lState = 9;					//  is an open quote " so switch to skipping the text of the attribute's value
		        } else if ( lState == 9 ) {		// if we were skipping the text of the attribute's value
		            lState = 4;					//  then this is the close quote " so switch back to the attribute section of an element
		        }
			}else if ( c == '&' ) {
		        if ( lState == 0 ) {			// if we are just skipping along, then switch
		            lState = 10;				//  to the collection of a special xml tag
		        }
			}else if ( c == ';' ) {
				if ( lState == 10) {			// if we are collecting a special xml tag, the
					saTag[lTagindex] = "";		//  clear that tag name out because we have what we want
					lState = 0;					//  and go back to just skipping along
				}
		
			}else{		// --- we are now looking at any other character in the xml string ---
		
		
		        if ( lState == 1 ) {			// if we are collecting the element name
		            saTag[lTagindex] = saTag[lTagindex] + c;	// then collect the character
		        }
		        if ( lState == 2 ) {			//if we are skipping through the closing tag of the element name
		            saTag[lTagindex] = saTag[lTagindex].substring(1);	// then consume a character from our saved element name to maintain the name's integrity
		        }
		        if ( lState == 4 ) {			// if we are processing an element's attribute section
		            lState = 5;					//  then switch to collecting the name of the attribute
		        }
		        if ( lState == 10 ) {			// if we are collecting an & xml special code
		            saTag[lTagindex] = saTag[lTagindex] + c;	//then collect the character
		        }
		
			}
		}
		
		
		
		// now based on what state we ended in, fabricate a string appropriate for appending to our input
		
		if ( lState == 1 && lTagindex == 0 && saTag[0].equals("") ) {	//we only received a <
		    sXmlsuff = "xml/>";
		} else if ( lState == 1 && !saTag[lTagindex].equals("") ) {		//we ended in the middle of an element name
		    sXmlsuff = "></" + saTag[lTagindex] + ">";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 0 && lTagindex > 0 ) {					//we ended in general text
		    sXmlsuff = "";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 1 && lTagindex > 0 ) {
		    sXmlsuff = "/" + saTag[lTagindex - 1] + ">";
		    for ( x = lTagindex - 2; x >= 0; x--) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 2 ) {										//we ended in a closing element name
		    sXmlsuff = saTag[lTagindex] + ">";
		    for (x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 3 ) {										//we ended in a comment
		    sXmlsuff = ">";
		    for (x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 4 ) {          //we ended in the middle of the attribute section of an element, but not within a a specific attribute definition
		    sXmlsuff = saTag[lTagindex] + ">";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 5 ) {			//we ended in an attribute name
		    sXmlsuff = saTag[lTagindex] + "XXX=''>";			//we can't just close the attribute because it may
		    for ( x = lTagindex - 1; x >= 0; x-- ) {			// result in a duplicate attribute, so we append an arbitrary
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";	// string to the name
		    }
		} else if ( lState == 6 ) {			//we ended after an attribute name but before the = sign
		    sXmlsuff = saTag[lTagindex] + "=''>";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 7 ) {          //we ended on the = between an attribute name and its value
		    sXmlsuff = saTag[lTagindex] + "''>";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 8 ) {          //we ended in the middle of ' surrounding an attribute value
		    sXmlsuff = saTag[lTagindex] + "'>";
		    for ( x = lTagindex - 1; x >= 0; x--) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 9 ) {          //we ended in the middle of " surrounding an attribute value
		    sXmlsuff = saTag[lTagindex] + "\">";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 10 && !saTag[lTagindex].equals("") ) {	//we ended in the middle of 
		    if ( saTag[lTagindex].equals("l") ) sXmlsuff = "t;";		// a special xml code, so 
		    if ( saTag[lTagindex].equals("lt") ) sXmlsuff = ";";		// finish it out based on what
		    if ( saTag[lTagindex].equals("g") ) sXmlsuff = "t;";		// we've collected
		    if ( saTag[lTagindex].equals("gt") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("a") ) sXmlsuff = "mp;";
		    if ( saTag[lTagindex].equals("am") ) sXmlsuff = "p;";
		    if ( saTag[lTagindex].equals("amp") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("q") ) sXmlsuff = "uot;";
		    if ( saTag[lTagindex].equals("qu") ) sXmlsuff = "ot;";
		    if ( saTag[lTagindex].equals("quo") ) sXmlsuff = "t;";
		    if ( saTag[lTagindex].equals("quot") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("a") ) sXmlsuff = "pos;";
		    if ( saTag[lTagindex].equals("ap") ) sXmlsuff = "os;";
		    if ( saTag[lTagindex].equals("apo") ) sXmlsuff = "s;";
		    if ( saTag[lTagindex].equals("apos") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#") ) sXmlsuff = "39;";
		    if ( saTag[lTagindex].equals("#3") ) sXmlsuff = "9;";
		    if ( saTag[lTagindex].equals("#39") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#34") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#38") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#6") ) sXmlsuff = "0;";
		    if ( saTag[lTagindex].equals("#60") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#62") ) sXmlsuff = ";";
		    for (x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 10 && lTagindex > 0 ) {			// this could have been combined above
		    sXmlsuff = "lt;";									//  but we ended on just an &
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		}
		IDataUtil.put(cursor, "xmlsuff", sXmlsuff);
		cursor.destroy();
			
		// --- <<IS-END>> ---

                
	}



	public static final void genXmlSuffix_old (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(genXmlSuffix_old)>> ---
		// @sigtype java 3.5
		// [i] field:0:required xmlfrag
		// [o] field:0:required xmlsuff
		IDataCursor cursor = pipeline.getCursor();
		
		// genXmlSuffix - generate an appropriate suffix for a fragment of xml
		//
		//		xmlfrag (input,req) - a leading fragment of xml
		//
		//		xmlsuff (output,str) - a suffix that can be appended to xmlfrag making it well-formed
		//
		//	This service will generate an xml suffix for the supplied xml fragment - the fragment 
		//	should be a truncated (or whole) string representing the leading portion of any well-formed
		//	xml, without references to DTDs or schemas - the idea behind this service is that you can 
		//	load any truncated leading portion of any size xml document and call this service to generate 
		//	a suffix appropriate for appending, such that the 2 pieces together will load as a proper 
		//	document to the xmlStringToXMLNode service - it is assumed that this will allow the caller
		//  to use the resulting xml to then scan the envelope portion and other values located near
		//  the top of the xml for key pieces of information, even in the largest of documents.
		//
		//	This is implemented as a Finite State Machine, where:
		//
		//		lState = 0		we are skipping input
		//		lState = 1		we are collecting an element name
		//		lState = 2		we are collecting the closing element name
		//		lState = 3		unused
		//		lState = 4		we are collecting somewhere within the attribute section of an element
		//		lState = 5		we are collecting the attribute name itself
		//		lState = 6		we are collecting the spaces between the attribute name and its = sign
		//		lState = 7		we are collecting the = sign of an attribute
		//		lState = 8		we are collecting an attribute value within ' quotes
		//		lState = 9		we are collecting an attribute value within " quotes
		//		lState = 10		we are collecting a special xml tag terminated by a ; semi-colon
		//
		//	Keep in mind that the goal of this service is to generate a string suffix that can be
		//	appended to the input xml fragment, *regardless* of where the input xml has been truncated.
		//	The goal is not to try and generate 'business level' validated xml, only well-formed xml.
		//
		String sXmlfrag = null;	//input
		String sXmlsuff = null;	//output
		String[] saTag = new String[99];
		int lTagindex = 0;
		long lState = 0;
		int i = 0;
		char c = 0;
		int x = 0;
		
		if (cursor.first("xmlfrag")) sXmlfrag = (String) cursor.getValue();
		if (sXmlfrag == null) throw new ServiceException("vjGenXmlSuffix: Missing input 'xmlfrag'"); 
		
		for ( x = 0; x < 99; x++ ) saTag[x] = "";		// init our tag array
		
		i = sXmlfrag.indexOf("?>");						// skip past any possible xml header
		i = ( i > 0 ) ? i = i + 2 : 0;
		
		for(; i < sXmlfrag.length(); i++) {
		
			c = sXmlfrag.charAt(i);
		
			if ( c == '<') {
				if ( lState != 8 && lState != 9 ) {		// as long as we arent embedded in quotes, we
					lState = 1;							//  are going to start collecting an element name
				}
			}else if ( c == '/' ) {
				if ( lState == 4 ) {					// if we were processing the attribute section
					lTagindex--;						//  then adjust the pointer to our saved list of names
					lState = 1;							//  and immediately switch to processing the element name again and fall thru to the next if statement
				}
				if ( lState == 1) {						// if we were collecting an element name
					lState = 2;							// then switch that to collecting the element name's close
					if ( !saTag[lTagindex].equals("") ) {	// the trick here is that if we have just collected the name, then
						saTag[lTagindex] = "";				// the / is at the end of the element and we don't need the name anymore
					}else{
						lTagindex--;						// otherwise the / is at the beginning of the element, and we got the 
					}										// element name a long time ago, so just adjust our pointer to point to it
				}
			}else if ( c == '>' ) {
		        if ( lState == 1 ) {			// if we were collecting an element name, then we need
		            lTagindex++;				//  to close that off and hold onto it
		            lState = 0;      			//  and go back to just skipping along
		        }
		        if ( lState == 2 ) {			// if we were collecting a closing element name, we already
		            lState = 0;					//  got that awhile ago, so just go back to skipping along
		        }
		        if ( lState == 4 ) {			// if we were processing the attribute section of
		            lState = 0;					//  an element, then go back to skipping along
		        }
			}else if ( c == ' ' ) {
		        if (lState == 1 ) {				// if we were collecting the element name, we are now
		            lTagindex++;				//  done with it, so bump up our pointer and
		            lState = 4;					//  set our state to scanning the attribute section
		        }
		        if ( lState == 5 ) {			// if we were collecting the attribute name, then switch
		            lState = 6;					//  to empty space between the attr name and the =
		        }
			}else if ( c == '=' ) {
		        if ( lState == 5 ) {			// if we were collecting an attribute name, then switch
		            lState = 7;					//  to the = between the attribute name and its value
		        }
		        if ( lState == 6 ) {			// if we were skipping the spaces between the attribute name
		            lState = 7;					//  and the = then switch to the = also
		        }
			}else if ( c == '\'' ) {
		        if ( lState == 7 ) {			// if we just processed the attribute = then this
		            lState = 8;					//  is an open quote ' so switch to skipping the text of the attribute's value
		        } else if ( lState == 8 ) {		// if we were skipping the text of the attribute's value
		            lState = 4;					//  then this is the close quote ' so switch back to the attribute section of an element
		        }
			}else if ( c == '"' ) {
		        if ( lState == 7 ) {			// if we just processed the attribute = then this
		            lState = 9;					//  is an open quote " so switch to skipping the text of the attribute's value
		        } else if ( lState == 9 ) {		// if we were skipping the text of the attribute's value
		            lState = 4;					//  then this is the close quote " so switch back to the attribute section of an element
		        }
			}else if ( c == '&' ) {
		        if ( lState == 0 ) {			// if we are just skipping along, then switch
		            lState = 10;				//  to the collection of a special xml tag
		        }
			}else if ( c == ';' ) {
				if ( lState == 10) {			// if we are collecting a special xml tag, the
					saTag[lTagindex] = "";		//  clear that tag name out because we have what we want
					lState = 0;					//  and go back to just skipping along
				}
		
			}else{		// --- we are now looking at any other character in the xml string ---
		
		        if ( lState == 1 ) {			// if we are collecting the element name
		            saTag[lTagindex] = saTag[lTagindex] + c;	// then collect the character
		        }
		        if ( lState == 2 ) {			//if we are skipping through the closing tag of the element name
		            saTag[lTagindex] = saTag[lTagindex].substring(1);	// then consume a character from our saved element name to maintain the name's integrity
		        }
		        if ( lState == 4 ) {			// if we are processing an element's attribute section
		            lState = 5;					//  then switch to collecting the name of the attribute
		        }
		        if ( lState == 10 ) {			// if we are collecting an & xml special code
		            saTag[lTagindex] = saTag[lTagindex] + c;	//then collect the character
		        }
		
			}
		}
		
		
		// now based on what state we ended in, fabricate a string appropriate for appending to our input
		
		if ( lState == 1 && lTagindex == 0 && saTag[0].equals("") ) {	//we only received a <
		    sXmlsuff = "xml/>";
		} else if ( lState == 1 && !saTag[lTagindex].equals("") ) {		//we ended in the middle of an element name
		    sXmlsuff = "></" + saTag[lTagindex] + ">";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 0 && lTagindex > 0 ) {					//we ended in general text
		    sXmlsuff = "";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 1 && lTagindex > 0 ) {
		    sXmlsuff = "/" + saTag[lTagindex - 1] + ">";
		    for ( x = lTagindex - 2; x >= 0; x--) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 2 ) {										//we ended in a closing element name
		    sXmlsuff = saTag[lTagindex] + ">";
		    for (x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 4 ) {          //we ended in the middle of the attribute section of an element, but not within a a specific attribute definition
		    sXmlsuff = saTag[lTagindex] + ">";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 5 ) {			//we ended in an attribute name
		    sXmlsuff = saTag[lTagindex] + "XXX=''>";			//we can't just close the attribute because it may
		    for ( x = lTagindex - 1; x >= 0; x-- ) {			// result in a duplicate attribute, so we append an arbitrary
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";	// string to the name
		    }
		} else if ( lState == 6 ) {			//we ended after an attribute name but before the = sign
		    sXmlsuff = saTag[lTagindex] + "=''>";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 7 ) {          //we ended on the = between an attribute name and its value
		    sXmlsuff = saTag[lTagindex] + "''>";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 8 ) {          //we ended in the middle of ' surrounding an attribute value
		    sXmlsuff = saTag[lTagindex] + "'>";
		    for ( x = lTagindex - 1; x >= 0; x--) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 9 ) {          //we ended in the middle of " surrounding an attribute value
		    sXmlsuff = saTag[lTagindex] + "\">";
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 10 && !saTag[lTagindex].equals("") ) {	//we ended in the middle of 
		    if ( saTag[lTagindex].equals("l") ) sXmlsuff = "t;";		// a special xml code, so 
		    if ( saTag[lTagindex].equals("lt") ) sXmlsuff = ";";		// finish it out based on what
		    if ( saTag[lTagindex].equals("g") ) sXmlsuff = "t;";		// we've collected
		    if ( saTag[lTagindex].equals("gt") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("a") ) sXmlsuff = "mp;";
		    if ( saTag[lTagindex].equals("am") ) sXmlsuff = "p;";
		    if ( saTag[lTagindex].equals("amp") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("q") ) sXmlsuff = "uot;";
		    if ( saTag[lTagindex].equals("qu") ) sXmlsuff = "ot;";
		    if ( saTag[lTagindex].equals("quo") ) sXmlsuff = "t;";
		    if ( saTag[lTagindex].equals("quot") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("a") ) sXmlsuff = "pos;";
		    if ( saTag[lTagindex].equals("ap") ) sXmlsuff = "os;";
		    if ( saTag[lTagindex].equals("apo") ) sXmlsuff = "s;";
		    if ( saTag[lTagindex].equals("apos") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#") ) sXmlsuff = "39;";
		    if ( saTag[lTagindex].equals("#3") ) sXmlsuff = "9;";
		    if ( saTag[lTagindex].equals("#39") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#34") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#38") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#6") ) sXmlsuff = "0;";
		    if ( saTag[lTagindex].equals("#60") ) sXmlsuff = ";";
		    if ( saTag[lTagindex].equals("#62") ) sXmlsuff = ";";
		    for (x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		} else if ( lState == 10 && lTagindex > 0 ) {			// this could have been combined above
		    sXmlsuff = "lt;";									//  but we ended on just an &
		    for ( x = lTagindex - 1; x >= 0; x-- ) {
		        sXmlsuff = sXmlsuff + "</" + saTag[x] + ">";
		    }
		}
		
		IDataUtil.put(cursor, "xmlsuff", sXmlsuff);
		cursor.destroy();
		// --- <<IS-END>> ---

                
	}
}


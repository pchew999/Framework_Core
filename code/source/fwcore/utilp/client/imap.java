package fwcore.utilp.client;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import com.wm.lang.xml.XmlUtil;
import com.wm.util.coder.IDataXMLCoder;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.Enumeration;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.MessageIDTerm;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.ibm.icu.text.DateFormat;
import com.sun.mail.imap.IMAPFolder;
// --- <<IS-END-IMPORTS>> ---

public final class imap

{
	// ---( internal utility methods )---

	final static imap _instance = new imap();

	static imap _newInstance() { return new imap(); }

	static imap _cast(Object o) { return (imap)o; }

	// ---( server methods )---




	public static final void clearAllExpungeFlags (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(clearAllExpungeFlags)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [o] field:0:required status
		// pipeline
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		
		// initializes the new properties object props, with the current set of system properties		
		Properties props = new Properties(System.getProperties());		
		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("clearAllExpungeFlags: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("clearAllExpungeFlags - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			inbox.open(Folder.READ_WRITE);
		
			Message[] msgs = inbox.getMessages();		//get all messages
			Flags deletedFlag = new Flags(Flags.Flag.DELETED);
			inbox.setFlags(msgs, deletedFlag, false);								
			inbox.expunge();
		
			inbox.close(true);
			store.close();
		    
			IDataUtil.put(cursor, "status", "unexpunged " + msgs.length + " messages");
			cursor.destroy();
		
			}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("clearAllExpungeFlags: internal failure: " + e.getMessage());
				}
				throw new ServiceException("clearAllExpungeFlags: failure " + ex.getMessage());
			}
		// --- <<IS-END>> ---

                
	}



	public static final void deleteEmailMessage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(deleteEmailMessage)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:required emailMessageID
		// [i] field:0:optional permanent {"true","false"}
		// [o] field:0:required status
		// pipeline
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String emailMessageID = IDataUtil.getString( cursor, "emailMessageID" );
		String permanent = IDataUtil.getString( cursor, "permanent" );
		
		Properties props = new Properties(System.getProperties());		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("deleteEmailMessage: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("deleteEmailMessage - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			Folder deleteFolder = store.getFolder("Deleted Items");
		
			inbox.open(Folder.READ_WRITE);
			SearchTerm searchTerm = new MessageIDTerm(emailMessageID);
			Message[] msgs = inbox.search(searchTerm);		//search for 1 message based on the UID
			if(permanent==null || permanent.equals("false")) {
				((IMAPFolder) inbox).moveMessages(msgs, deleteFolder);	//move to "Deleted Items"
			}else{
				for(int i = 0; i < msgs.length; i++) {
					msgs[i].setFlag(Flags.Flag.DELETED, true);			//permanently delete (but still recoverable)
				}
		   	}
		
			if(permanent==null || permanent.equals("false")) {
				inbox.close(false);		//do not expunge
				IDataUtil.put(cursor, "status", msgs.length + " message(s) moved to trash");
			}else{
				inbox.close(true);		//expunge - basically 'permanently delete', although outlook still lets you restore the email
				IDataUtil.put(cursor, "status", msgs.length + " message(s) permanently deleted");
			}
		
			store.close();
			cursor.destroy();
		
			}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("deleteEmailMessage: internal failure: " + e.getMessage());
				}
				throw new ServiceException("deleteEmailMessage: general failure " + ex.getMessage());
			}
		// --- <<IS-END>> ---

                
	}



	public static final void forwardEmailMessage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(forwardEmailMessage)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:required emailMessageID
		// [i] field:0:required forwardTo
		// [i] field:0:required forwardFrom
		// [i] field:0:required forwardCC
		// [i] field:0:required forwardSubject
		// pipeline
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String emailMessageID = IDataUtil.getString( cursor, "emailMessageID" );
		String forwardTo = IDataUtil.getString( cursor, "forwardTo" );
		String forwardFrom = IDataUtil.getString( cursor, "forwardFrom" );
		String forwardCC = IDataUtil.getString( cursor, "forwardCC" );
		String forwardSubject = IDataUtil.getString( cursor, "forwardSubject" );
		
		Properties props = new Properties(System.getProperties());		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("forwardEmailMessage: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("forwardEmailMessage - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		
		try {
			inbox = store.getFolder(mailBoxFolder);
			inbox.open(Folder.READ_WRITE);
			SearchTerm searchTerm = new MessageIDTerm(emailMessageID);
			Message[] msgs = inbox.search(searchTerm);		//search for 1 message based on the UID
			
			for(int i = 0; i < msgs.length; i++) {
				MimeMessage msg2 = new MimeMessage((MimeMessage) msgs[i]); //copy original message without changing
					
				if(forwardTo != null) {	        	
					String fwd_to = createAddressString(forwardTo, msgs[i]);
					InternetAddress[] recipients = InternetAddress.parse(fwd_to, true);
					msg2.setRecipients(javax.mail.Message.RecipientType.TO, recipients);	        		
				}
				if(forwardFrom != null) {
					msg2.setFrom(new InternetAddress(forwardFrom));
					msg2.setReplyTo(InternetAddress.parse(forwardFrom, true));
				}
				if(forwardCC != null) {	        	
					String fwd_cc = createAddressString(forwardCC, msgs[i]);
					InternetAddress[] recipients = InternetAddress.parse(fwd_cc, true);
					msg2.setRecipients(javax.mail.Message.RecipientType.CC, recipients);	        		
				}else{
					msg2.setRecipients(Message.RecipientType.CC, (Address[])null); //remove original CC addresses
				}
				if(forwardSubject != null)	{
					String fwd_subject = updateSubject(forwardSubject, msgs[i]);
					msg2.setSubject(fwd_subject, "UTF-8");
				}
					   
				msg2.setRecipients(Message.RecipientType.BCC, (Address[])null); //remove original BCC addresses
		
				Transport.send(msg2);
				msgs[i].setFlag(Flags.Flag.ANSWERED, true);
			}
		
			inbox.close(false);
			store.close();
			cursor.destroy();
		
		}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("forwardEmailMessage: internal failure: " + e.getMessage());
				}
				throw new ServiceException("forwardEmailMessage: general failure " + ex.getMessage());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void getEmailMessageById (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getEmailMessageById)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:required emailMessageID
		// [o] field:0:required mimeMsg
		// pipeline
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String emailMessageID = IDataUtil.getString( cursor, "emailMessageID" );
		
		Properties props = new Properties(System.getProperties());		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("getEmailMessageById: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("getEmailMessageById - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			inbox.open(Folder.READ_ONLY);
			SearchTerm searchTerm = new MessageIDTerm(emailMessageID);
			Message[] msgs = inbox.search(searchTerm);		//search for 1 message based on the UID
		
			StringBuilder sb = new StringBuilder();
			Enumeration<Header> headers = msgs[0].getAllHeaders();	//we could possibly get more than 1 message, so just return the 0'th message
			while (headers.hasMoreElements()) {
				Header header = (Header) headers.nextElement();
				sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
			}
		
			sb.append("\n");
		
			char[] buffer = new char[1024];
			Reader in = new InputStreamReader(msgs[0].getInputStream(), "UTF-8");
			for (int n; (n = in.read(buffer, 0, buffer.length)) > 0; ) {
				sb.append(buffer, 0, n);
			}
		
			inbox.close(false);
			store.close();
		
			IDataUtil.put(cursor, "mimeMsg", sb.toString());
			cursor.destroy();
		
			}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("getEmailMessageById: internal failure: " + e.getMessage());
				}
				throw new ServiceException("getEmailMessageById: failure " + ex.getMessage());
			}
		// --- <<IS-END>> ---

                
	}



	public static final void getEmailMessageList (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getEmailMessageList)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:optional retrieve {"all","unread","read"}
		// [i] field:0:optional startingNumber
		// [i] field:0:optional countLimit
		// [o] field:1:required messageIds
		// [o] field:1:required subjects
		IDataCursor cursor = pipeline.getCursor();
		int lStartnum = 0,lCountLimit = 0;
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String retrieve = IDataUtil.getString( cursor, "retrieve" );
		if (cursor.first("startingNumber")) lStartnum = Integer.parseInt((String)cursor.getValue());
		if (cursor.first("countLimit")) lCountLimit = Integer.parseInt((String)cursor.getValue());
		
		String[] messageIds = null;
		//String[] flags = null;
		String[] subjects = null;
		
		// initializes the new properties object props, with the current set of system properties		
		Properties props = new Properties(System.getProperties());		
		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("getEmailMessageList: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("getEmailMessageList - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			inbox.open(Folder.READ_ONLY);
			//inbox.getMessageCount()
			//inbox.getUnreadMessageCount()
		
			Message[] msgs = null;
			if(retrieve==null || retrieve.equals("all")) {
				msgs = inbox.getMessages();		//get all messages
			}else if (retrieve.equals("unread")) {
				msgs = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));	//get only unread messages
			}else{
				msgs = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));	//get only read messages
			}
				//inbox.fetch(msgs, fp);		
				/* When we recover items deleted from the 'Deleted Items' in Outlook, the application does not remove
				 * the 'deleted' flag from emails. This leads to errors when calling the 'inbox.expunge()' function.
				 * Invoking this function results in permanently deleting all the emails again, which is not expected behavior.
				 * To prevent this, I added the following 3 lines code
				 */
				//Flags deletedFlag = new Flags(Flags.Flag.DELETED);
				//inbox.setFlags(msgs, deletedFlag, false);								
				//inbox.expunge();
		
			int lUpper = 0;
			if(lCountLimit == 0) lCountLimit = msgs.length;
			lUpper = lStartnum + lCountLimit;
			if(lUpper > msgs.length) lUpper = msgs.length;
		
			messageIds = new String[lUpper - lStartnum];	//allocate an array of strings
			//flags = new String[lUpper - lStartnum];
			subjects = new String[lUpper - lStartnum];
		
			for(int i = lStartnum; i < lUpper; i++) {
				messageIds[i - lStartnum] = ((MimeMessage) msgs[i]).getMessageID();	//not unique?
				//flags[i] = msgs[i].getFlags().contains(Flags.Flag.DELETED);
				//flags[i] = msgs[i].getFlags().toString();
				subjects[i - lStartnum] = msgs[i].getSubject();
				
				//FileOutputStream os = new FileOutputStream("/csutil/miscTesting/croll/aaa"+i+".txt", false);
				//StringBuilder sb = new StringBuilder();
				//Enumeration<Header> headers = msgs[i].getAllHeaders();
				//while (headers.hasMoreElements()) {
				//	Header header = (Header) headers.nextElement();
				//	sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
				//}
				//byte[] sbbytes = sb.toString().getBytes();
				//os.write(sb.toString().getBytes());
				//os.write("\n".getBytes());
		
				//InputStream is = msgs[i].getInputStream();
				//byte[] bytes = new byte[1024];
				//int numbts;
				//while ((numbts = is.read(bytes)) != -1) {
				//	os.write(bytes, 0, numbts);
		    	//}
				//os.flush();
				//os.close();
		
				//msgs[i].setFlag(Flags.Flag.DELETED, false);
		
			}
			inbox.close(false);
			store.close();
		    
			IDataUtil.put(cursor, "messageIds", messageIds);
			//IDataUtil.put(cursor, "flags", flags);
			IDataUtil.put(cursor, "subjects", subjects);
			cursor.destroy();
		
			}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("getEmailMessageList: internal failure: " + e.getMessage());
				}
				throw new ServiceException("getEmailMessageList: failure " + ex.getMessage());
			}
		// --- <<IS-END>> ---

                
	}



	public static final void markEmailMessageAsRead (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(markEmailMessageAsRead)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:required emailMessageID
		// [o] field:0:required status
		// pipeline
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String emailMessageID = IDataUtil.getString( cursor, "emailMessageID" );
		
		Properties props = new Properties(System.getProperties());		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("markEmailMessageAsRead: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("markEmailMessageAsRead - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			inbox.open(Folder.READ_WRITE);
			SearchTerm searchTerm = new MessageIDTerm(emailMessageID);
			Message[] msgs = inbox.search(searchTerm);		//search for 1 message based on the UID
			for(int i = 0; i < msgs.length; i++) {
				inbox.setFlags(new Message[] {msgs[i]}, new Flags(Flags.Flag.SEEN), true);
			}
		
			inbox.close(false);
			store.close();
		
			IDataUtil.put(cursor, "status", msgs.length + " message(s) marked as read");
			cursor.destroy();
		
			}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("markEmailMessageAsRead: internal failure: " + e.getMessage());
				}
				throw new ServiceException("markEmailMessageAsRead: failure " + ex.getMessage());
			}
		// --- <<IS-END>> ---

                
	}



	public static final void moveEmailMessage (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(moveEmailMessage)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:required emailMessageID
		// [i] field:0:required targetMailboxFolder
		// [o] field:0:required status
		// getEmailMessageByIdgetMessageByIdgetEmailMessageByIdpipeline
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String emailMessageID = IDataUtil.getString( cursor, "emailMessageID" );
		String targetMailboxFolder = IDataUtil.getString( cursor, "targetMailboxFolder" );
		
		Properties props = new Properties(System.getProperties());		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("moveEmailMessage: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("moveEmailMessage - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			Folder targetFolder = store.getFolder(targetMailboxFolder);
			if(targetFolder==null) {
				IDataUtil.put(cursor, "status", "target folder is null");
			}else{
				inbox.open(Folder.READ_WRITE);
				SearchTerm searchTerm = new MessageIDTerm(emailMessageID);
				Message[] msgs = inbox.search(searchTerm);		//search for 1 message based on the UID
				if (msgs.length==0) // try to force synchronization
				   {
				      inbox.getMessageCount();                          
				      inbox.close(false);
				      inbox.open(Folder.READ_WRITE);
				      msgs = inbox.search(searchTerm);
				   }
		
		    	((IMAPFolder) inbox).moveMessages(msgs, targetFolder);
		
		    	inbox.close(false);
				IDataUtil.put(cursor, "status", msgs.length + " message(s) moved to target");
			}
			store.close();
			cursor.destroy();
		
			}catch (Exception ex) {
				try {
					if(inbox != null && inbox.isOpen()) inbox.close(false);
					if(store != null) store.close();
				}catch (MessagingException e) {
					throw new ServiceException("moveEmailMessage: internal failure: " + e.getMessage());
				}
				throw new ServiceException("moveEmailMessage: general failure " + ex.getMessage());
			}
		// --- <<IS-END>> ---

                
	}



	public static final void z_getEmailMessageDetails (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(z_getEmailMessageDetails)>> ---
		// @sigtype java 3.5
		// [i] field:0:required serverHostName
		// [i] field:0:required oauthToken
		// [i] field:0:required emailAddress
		// [i] field:0:required mailboxFolder
		// [i] field:0:required emailMessageID
		// [o] field:0:required emailFrom
		// [o] field:0:required emailTo
		// [o] field:0:required emailCC
		// [o] field:0:required emailSubject
		// [o] field:0:required emailFlags
		// [o] field:0:required emailDateSent
		// [o] field:0:required emailDateReceived
		// [o] field:0:required emailSize
		IDataCursor cursor = pipeline.getCursor();
		
		String host = IDataUtil.getString( cursor, "serverHostName" );
		String oauthToken = IDataUtil.getString( cursor, "oauthToken" );
		String emailAddress = IDataUtil.getString( cursor, "emailAddress" );
		String mailBoxFolder = IDataUtil.getString( cursor, "mailboxFolder" );
		String emailMessageID = IDataUtil.getString( cursor, "emailMessageID" );
		
		Properties props = new Properties(System.getProperties());		
		props.put("mail.imaps.partialfetch", "false");
		props.put("mail.imaps.fetchsize", "1048576");
		props.put("mail.imaps.ssl.enable", "true");
		props.put("mail.imaps.auth.mechanisms", "XOAUTH2");						
		
		Session session = Session.getInstance(props,null); // get a Session object (Bearer authentication)
		session.setDebug(false);	//try true
		
		Store store = null;
		try {
			store = session.getStore("imaps");	//imap secure
			store.connect(host, emailAddress, oauthToken);
		}catch (Exception e) {
			throw new ServiceException("getMessageById: connection failure: " + e.getMessage());
		}
		if(!store.isConnected()) throw new ServiceException("getMessageById - Connection Error (host: "+host+" - emailAddress:"+emailAddress+")");
		
		Folder inbox = null;
		try {
			inbox = store.getFolder(mailBoxFolder);
			inbox.open(Folder.READ_ONLY);
			SearchTerm searchTerm = new MessageIDTerm(emailMessageID);
			Message[] msgs = inbox.search(searchTerm);		//search for 1 message based on the UID
		
			String efr = msgs[0].getFrom()[0].toString();
		
			Address emailTo[] = msgs[0].getRecipients(Message.RecipientType.TO);
			String eto = "";
			if(emailTo != null) for (Address addr : emailTo) eto = eto + addr.toString() + ";";
		
			Address emailCC[] = msgs[0].getRecipients(Message.RecipientType.CC);
			String ecc = "";
			if(emailCC != null) for (Address addr : emailCC) ecc = ecc + addr.toString() + ";";
		
			String esb = msgs[0].getSubject();
			String flgs = msgs[0].getFlags().toString();
			//String fld = msgs[0].getFolder().toString();
			SimpleDateFormat oSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
			oSDF.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
			String dtr = oSDF.format(msgs[0].getReceivedDate());	//.toGMTString();
			String dts = oSDF.format(msgs[0].getSentDate());
			String sze = "" + msgs[0].getSize();
		
			inbox.close(false);
			store.close();
		
			IDataUtil.put(cursor, "emailFrom", efr);
			IDataUtil.put(cursor, "emailTo", eto);
			IDataUtil.put(cursor, "emailCC", ecc);
			IDataUtil.put(cursor, "emailSubject", esb);
			IDataUtil.put(cursor, "emailFlags", flgs);
			IDataUtil.put(cursor, "emailDateSent", dts);
			IDataUtil.put(cursor, "emailDateReceived", dtr);
			IDataUtil.put(cursor, "emailSize", sze);
			cursor.destroy();
		
		}catch (Exception ex) {
			try {
				if(inbox != null && inbox.isOpen()) inbox.close(false);
				if(store != null) store.close();
			}catch (MessagingException e) {
				throw new ServiceException("getMessageById: internal failure: " + e.getMessage());
			}
			throw new ServiceException("getMessageById: failure " + ex.getMessage());
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static public String updateSubject(String newSubject,Message msg) throws MessagingException
	{	
		if (newSubject.indexOf("use_sub") != -1)
		  {
			String oldSubject=msg.getSubject();
			if (oldSubject != null)
				newSubject = newSubject.replace("use_sub",oldSubject);
			else 
				newSubject = newSubject.replace("use_sub","");		
		  }
		
		if (newSubject.indexOf("use_from") != -1)
		  {
			String addrTmp=msg.getFrom()[0].toString();
			if (addrTmp != null)
				newSubject = newSubject.replace("use_from",addrTmp);
			else 
				newSubject = newSubject.replace("use_from","n/a");		
		  }
			
		return newSubject;
	}
	
	static public String createAddressString(String addr, Message msg) throws MessagingException
	{
		 Address[] a;
		 if (addr.indexOf("use_from")!= -1)
		 {
			 String addrTmp= "";
			 if ((a = msg.getFrom()) != null) {	    		
		    	    for (int j = 0; j < a.length; j++)
		    	    	addrTmp=addrTmp + a[j].toString()+", ";	    	    
		    	}
			 addr=addr.replace("use_from",addrTmp);
		 }
		 
		 if (addr.indexOf("use_replyto")!= -1)
		 {
			 String addrTmp= "";
			 if ((a = msg.getReplyTo()) != null) {	    		
		    	    for (int j = 0; j < a.length; j++)
		    	    	addrTmp=addrTmp + a[j].toString()+", ";	    	    
		    	}
			 addr=addr.replace("use_replyto",addrTmp);
		 }
		 
		 if (addr.indexOf("use_to")!= -1)
		 {
			 String addrTmp= "";
			 if ((a = msg.getRecipients(Message.RecipientType.TO)) != null) {	    		
		    	    for (int j = 0; j < a.length; j++)
		    	    	addrTmp=addrTmp + a[j].toString()+", ";	    	    
		    	}
			 addr=addr.replace("use_to",addrTmp);
		 }
		 
		 if (addr.indexOf("use_cc")!= -1)
		 {
			 String addrTmp= "";
			 if ((a = msg.getRecipients(Message.RecipientType.CC)) != null) {	    		
		    	    for (int j = 0; j < a.length; j++)
		    	    	addrTmp=addrTmp + a[j].toString()+", ";	    	    
		    	}
			 addr=addr.replace("use_cc",addrTmp);
			 }
				 
			return addr;	
		}
		
	// --- <<IS-END-SHARED>> ---
}


package gmailbrowser.model;
import gmailbrowser.json.DefaultDeserializer;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class Email implements IEmail { 
	
	private String id;
	private String meta_file;
	private String main_file;
	private String file_directory;
	private MimeMessage message;
    private EmailMetaData metaData;

	public Email (String threadID, String directory) throws Exception {
		
		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");
		Session mailSession = Session.getDefaultInstance(props, null);
        
		this.id = threadID;
		this.file_directory = directory;
		//same as using '/' but easier for people to understand
		this.meta_file = directory + File.separator + threadID + ".meta";
		
		//create two files to test if the file that exists is zipped or not
		File f = new File(directory + File.separator + threadID + ".eml");
		File f2 = new File(directory + File.separator + threadID + ".eml.gz");

		//if unzipped file exists
		if (f.exists()) {
			this.main_file = directory + File.separator + threadID + ".eml";
			//obtains bytes from the file
	        InputStream source = new FileInputStream(this.main_file);
	        //create message using the bytes and the mailSession
	        this.message = new MimeMessage(mailSession, source);
		}
		else if (f2.exists()) {
			this.main_file = directory + File.separator + threadID + ".eml.gz";
			//GZIPInputStream reads COMPRESSED data from an input stream
	        InputStream source = new GZIPInputStream(new FileInputStream(this.main_file));
	        this.message = new MimeMessage(mailSession, source);
		}
		else {
			throw new IllegalArgumentException("This file does not exists.");
		}
		// Read metadata.
		this.metaData = new DefaultDeserializer().deserializeEmailMetadata(new File(this.meta_file));
	}

    public EmailMetaData getMetadata() {
	   return this.metaData;
    }

	//Enumeration is like bucky("nice", "22"), Sahil("cool", "18");
	@Override
	public Map<String, String> getHeaders() {
		Map<String, String> headerMap = new HashMap<>();
		Enumeration<Header> allHeaders = null;
		try {
			allHeaders = message.getAllHeaders();
		} catch (MessagingException e) {
		    return ExceptionUtils.rethrow(e);
		}
		//while the enumeration has more elements, retriever the next element and get the name and value
		while (allHeaders.hasMoreElements()) {
			Header header = (Header) allHeaders.nextElement();
            String headerName = header.getName();
            String headerVal = header.getValue();
            headerMap.put(headerName, headerVal);
		}
		return headerMap;
	}

	@Override
	public String getID() {
        return String.valueOf(this.metaData.getThreadIds());
	}

	@Override
	public Map<String, String> getContent() {
		HashMap<String, String> end = new HashMap<String, String>();
		try {
			if (message.isMimeType("text/plain")) {
				end.put(message.getContentType().split(";")[0], message.getContent().toString());
			}
			else if (message.isMimeType("multipart/*")) {
				String pathname = message.getContentType().split(";")[0] + "|";
				MimeMultipart test = (MimeMultipart) message.getContent();
				Map<String, String > result = getMultiPart(test, pathname);
				for (String i : result.keySet()) {
					end.put(i, result.get(i));
				}
			}
		} catch (Exception e) {
		    return ExceptionUtils.rethrow(e);
		}
		return end;
	}
	
	public Map<String, String> getMultiPart(MimeMultipart test, String pathname) throws Exception {
		int numParts = test.getCount();
		HashMap<String, String> contents = new HashMap<String, String>();
		for (int i = 0; i < numParts; i++) {
			BodyPart testPart = test.getBodyPart(i);
			if (testPart.isMimeType("text/plain")) {
				contents.put(pathname + testPart.getContentType().split(";")[0] + "|", testPart.getContent().toString());
			}
			else if (testPart.isMimeType("text/html")) {
				contents.put(pathname + testPart.getContentType().split(";")[0] + "|", testPart.getContent().toString());
			}
			else if (testPart.isMimeType("multipart/*")) {
				pathname += testPart.getContentType().split(";")[0] +"|";
				getMultiPart((MimeMultipart) testPart.getContent(), pathname);
			}
		}
		return contents;
	}

	@Override
	public String getFromName() {
		//getFrom returns an array of the name and the email
		String messageString = null;
		try {
			messageString = message.getFrom()[0].toString();
		} catch (MessagingException e) {
			return ExceptionUtils.rethrow(e);
		}
		//get index of < and return the name
		int index = messageString.lastIndexOf("<");
        return messageString.substring(0, index);
	}
	
	@Override
	public String getFromEmail() {
		//do the opposite of getFromName
		String messageString = null;
		try {
			messageString = message.getFrom()[0].toString();
		} catch (MessagingException e) {
		    return ExceptionUtils.rethrow(e);
		}
		int index = messageString.lastIndexOf("<");
        return messageString.substring(index + 1, messageString.length() - 1);
	}

	@Override
	public String getSubject() {
		return this.metaData.getSubject();
	}

	@Override
	//get the labels
	public List<String> getLabels() {
	    return this.metaData.getLabels();
	}
}

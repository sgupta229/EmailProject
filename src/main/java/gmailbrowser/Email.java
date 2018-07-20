package gmailbrowser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Email implements IEmail { 
	
	private String id;
	private String meta_file;
	private String main_file;
	private String file_directory;
	private MimeMessage message;
	
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
	}
	
	public JSONObject getMetadata() {
		StringBuilder content = new StringBuilder();
		//FileReader reads characters instead of bytes
		//BufferedReader takes in a character input stream
		try (BufferedReader br = new BufferedReader(new FileReader(this.meta_file))) {
			String currentLine = br.readLine();
			while (currentLine != null) {
				content.append(currentLine).append("\n");
				currentLine = br.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//content is a StringBuilder. convert to string and then try converting to JSONObject
		try {
			JSONObject jsonObj = new JSONObject(content.toString());
			return jsonObj;
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	//Enumeration is like bucky("nice", "22"), Sahil("cool", "18");
	@Override
	public Map<String, String> getHeaders() throws MessagingException {
		Map<String, String> headerMap = new HashMap<>();
		Enumeration<Header> allHeaders = message.getAllHeaders();
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
	public String getID() throws JSONException {
		//get the message in JSON form
		JSONObject test = this.getMetadata();
		//get the thread ID
		Number stringID = test.getNumber("thread_ids");
		return stringID.toString();
	}

	@Override
	public Map<String, String> getContent() throws Exception {
		HashMap<String, String> end = new HashMap<String, String>();
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
	public String getFromName() throws Exception {
		//getFrom returns an array of the name and the email
		String messageString = message.getFrom()[0].toString();
		//get index of < and return the name
		int index = messageString.lastIndexOf("<");
        return messageString.substring(0, index);
	}
	
	@Override
	public String getFromEmail() throws Exception {
		//do the opposite of getFromName
		String messageString = message.getFrom()[0].toString();
		int index = messageString.lastIndexOf("<");
        return messageString.substring(index + 1, messageString.length() - 1);
	}

	@Override
	public String getSubject() throws JSONException {
		JSONObject test = this.getMetadata();
		String subject = test.getString("subject");
		return subject;
	}

	@Override
	//get the labels
	public List<String> getLabels() throws JSONException {
		JSONObject test = this.getMetadata();
		//make JSONArray of the labels
		JSONArray end = test.getJSONArray("labels");
		List<String> last = new ArrayList<String>();
		//convert the JSONArray to a List
		for (int i = 0; i < end.length(); i++) {
			String use = (String) end.get(i);
			last.add(use);
		}
		return last;
	}
}

package emailbrowser;
import java.io.IOException;
import java.util.*;

import javax.mail.Header;
import javax.mail.MessagingException;

import org.json.JSONException;

public interface IEmail {
	public abstract Map<String, String> getHeaders() throws MessagingException;
	//retrieves header of email
	
	public abstract String getID() throws JSONException;
	//retrieves ID of email
	
	public abstract Map<String, String> getContent() throws Exception;
	//retrieves content of email
	
	public abstract String getSubject() throws JSONException;
	//retrieves email subject
	
	public abstract List<String> getLabels() throws JSONException;
	//retrieve labels of the email
	
	public abstract String getFromEmail() throws Exception;
	//retrieve sender of the email
	
	public abstract String getFromName() throws Exception;
	//retrieves name of person who sent the email
}

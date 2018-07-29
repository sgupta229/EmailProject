package gmailbrowser.model;

import java.util.List;
import java.util.Map;

public interface IEmail {
	public abstract Map<String, String> getHeaders();
	//retrieves header of email
	
	public abstract String getID();
	//retrieves ID of email
	
	public abstract Map<String, String> getContent();
	//retrieves content of email
	
	public abstract String getSubject();
	//retrieves email subject
	
	public abstract List<String> getLabels();
	//retrieve labels of the email
	
	public abstract String getFromEmail();
	//retrieve sender of the email
	
	public abstract String getFromName();
	//retrieves name of person who sent the email
}

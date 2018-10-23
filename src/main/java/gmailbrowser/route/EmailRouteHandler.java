package gmailbrowser.route;

import org.json.JSONObject;
import gmailbrowser.Email;
import spark.Request;
import spark.Response;

/**
 * The route /email/:id
 *
 */
public class EmailRouteHandler extends AbstractRouteHandler {
	String emailsDirectory;
	
	public EmailRouteHandler(String emailsDirectoryFlattened) {
		this.emailsDirectory = emailsDirectoryFlattened;
	}
	
	@Override
	public JSONObject get(Request request, Response response) throws Exception {
		String id = request.params(":id");
		Email email = new Email(id, this.emailsDirectory);
		
		JSONObject obj = new JSONObject();
		obj.put("id", email.getID());
		obj.put("labels", email.getLabels());
		obj.put("subject", email.getSubject());
		JSONObject fromObj = new JSONObject();
			fromObj.put("name", email.getFromName());
			fromObj.put("email", email.getFromEmail());
		obj.put("from", fromObj);
		obj.put("headers", email.getHeaders());
		obj.put("body", email.getContent());
		obj.put("_meta_", email.getMetadata());

		return obj;
	}
}

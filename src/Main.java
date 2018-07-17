import spark.Spark;
import org.json.JSONException;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) throws Exception {
		String fileDirectory = args[0];

		Spark.setPort(8080);
		Spark.get("/hello", new spark.Route() {
			public String handle(spark.Request req, spark.Response res) throws Exception {
				return "World!";
			}
		});
		Spark.get("/email/:id", (spark.Request request, spark.Response response) -> {
			String id = request.params(":id");
			response.type("application/json");

			try {
				Email email = new Email(id, fileDirectory);
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
			} catch (Throwable e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				response.type("text/plain");
				response.status(500);

				return sw.toString();
			}
		});
	}
}

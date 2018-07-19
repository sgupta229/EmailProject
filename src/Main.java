import emailbrowser.route.EmailRouteHandler;
import spark.Spark;

public class Main {

	public static void main(String[] args) throws Exception {
		String emailsDirectoryFlattened = args[0];

		Spark.port(8080);
		/*Spark.get("/hello", (req, res) -> "World!  Java lambda expression.");
		Spark.get("/hi", new spark.Route() {
			public String handle(spark.Request req, spark.Response res) throws Exception {
				return "Anonymous interface implementation";
			}
		});*/
		Spark.get("/email/:id", new EmailRouteHandler(emailsDirectoryFlattened));
	}
}

package emailbrowser.route;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import spark.Request;
import spark.Response;

public class AbstractRouteHandler implements spark.Route {
	@Override
	public Object handle(Request request, Response response) {
		Method[] methods = this.getClass().getMethods();
		for (Method m : methods) {
			if (m.getName().equalsIgnoreCase(request.requestMethod())) {
				try {
					response.type("application/json");
					return m.invoke(this, request, response);
				} catch (Throwable error) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					error.printStackTrace(pw);
					response.type("text/plain");
					response.status(500);

					return sw.toString();
				}
			}
		}
		response.type("text/plain");
		response.status(500);
		return "Unreachable internal server error.";
	}
	
	public Object get(Request request, Response response) throws Exception {
		response.status(405); // method not allowed
		response.type("text/plain");
		return "This method for the URL is unimplemented.";
	}
	public Object post(Request request, Response response) throws Exception {
		response.status(405); // method not allowed
		response.type("text/plain");
		return "This method for the URL is unimplemented.";
	}
	public Object put(Request request, Response response) throws Exception {
		response.status(405); // method not allowed
		response.type("text/plain");
		return "This method for the URL is unimplemented.";
	}
	public Object delete(Request request, Response response) throws Exception {
		response.status(405); // method not allowed
		response.type("text/plain");
		return "This method for the URL is unimplemented.";
	}
}

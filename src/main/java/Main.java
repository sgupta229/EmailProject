import gmailbrowser.route.EmailWS;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    private static HttpServer startServer(String baseUri) {
        final ResourceConfig rc = new ResourceConfig().packages("gmailbrowser.route");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
    }

    public static void main(String[] args) throws IOException {
        // FIXME This is a hack. Inject instead of setting static field.
        EmailWS.emailsDirectory = args[0];
        final String baseUri = "http://localhost:8080/";

        final HttpServer server = startServer(baseUri);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl", baseUri));
        // System.in.read();
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.shutdownNow();
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }
}

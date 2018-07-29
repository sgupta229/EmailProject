package gmailbrowser.route;

import gmailbrowser.json.DefaultSerializer;
import gmailbrowser.json.Serializer;
import gmailbrowser.model.Email;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/email")
public class EmailWS {
    // TODO This is a hack. This should be an instance variable which should be injected into this class.
    // There is way to do that, will figure out soon.
    public static String emailsDirectory;

    private Serializer serializer = new DefaultSerializer();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String getEmailById(@PathParam("id") String id, @QueryParam("truncated") String truncated) {
        try {
            Email email = new Email(id, emailsDirectory);
            if (truncated != null && truncated.equals("true")) {
                return new DefaultSerializer().serializeEmailTruncated(email);
            } else {
                return new DefaultSerializer().serializeEmailComplete(email);
            }
        } catch (Exception e) {
            return ExceptionUtils.rethrow(e);
        }
    }
}

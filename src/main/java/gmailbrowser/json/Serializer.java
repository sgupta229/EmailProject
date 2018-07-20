package gmailbrowser.json;

import gmailbrowser.model.Email;
import gmailbrowser.model.EmailMetaData;

public interface Serializer {

    public String serializeEmailMetaData(EmailMetaData emailMetaData);

    public String serializeEmailComplete(Email email);

    public String serializeEmailTruncated(Email email);

}

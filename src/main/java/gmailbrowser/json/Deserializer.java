package gmailbrowser.json;

import gmailbrowser.model.EmailMetaData;

import java.io.File;

public interface Deserializer {
    public EmailMetaData deserializeEmailMetadata(String jsonString);
    public EmailMetaData deserializeEmailMetadata(File file);
}

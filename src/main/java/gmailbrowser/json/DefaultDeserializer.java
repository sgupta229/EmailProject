package gmailbrowser.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmailbrowser.model.EmailMetaData;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;

import static gmailbrowser.json.EmailPojos.*;

public class DefaultDeserializer implements Deserializer {
    private static final ObjectMapper OM = new ObjectMapper();

    @Override
    public EmailMetaData deserializeEmailMetadata(String jsonString) {
        EmailMetaDataPojo mdPojo;
        try {
            mdPojo = OM.readValue(jsonString, EmailMetaDataPojo.class);
        } catch (IOException e) {
           return ExceptionUtils.rethrow(e);
        }
        return fromPojo(mdPojo);
    }

    @Override
    public EmailMetaData deserializeEmailMetadata(File file) {
        EmailMetaDataPojo mdPojo;
        try {
            mdPojo = OM.readValue(file, EmailMetaDataPojo.class);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
        return fromPojo(mdPojo);
    }

    private EmailMetaData fromPojo(EmailMetaDataPojo pojo) {
        return new EmailMetaData()
                .setFlags(pojo.getFlags())
                .setGmId(pojo.getGm_id())
                .setInternalDate(pojo.getInternal_date())
                .setLabels(pojo.getLabels())
                .setMsgId(pojo.getMsg_id())
                .setSubject(pojo.getSubject())
                .setThreadIds(pojo.getThread_ids())
                .setXGmailReceived(pojo.getX_gmail_received());
    }
}

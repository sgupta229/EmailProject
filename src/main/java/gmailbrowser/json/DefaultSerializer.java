package gmailbrowser.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import gmailbrowser.model.Email;
import gmailbrowser.model.EmailMetaData;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static gmailbrowser.json.EmailPojos.*;

public class DefaultSerializer implements Serializer{
    private static ObjectMapper OM = new ObjectMapper();

    @Override
    public String serializeEmailMetaData(EmailMetaData emailMetaData) {
        return writeValueAsStringExWrapper(OM, createMetadataPojo(emailMetaData));
    }

    @Override
    public String serializeEmailComplete(Email email) {
        return writeValueAsStringExWrapper(OM.writerWithView(EmailViews.Complete.class), createEmailPojo(email));
    }

    @Override
    public String serializeEmailTruncated(Email email) {
        return writeValueAsStringExWrapper(OM.writerWithView(EmailViews.Truncated.class), createEmailPojo(email));
    }

    private EmailMetaDataPojo createMetadataPojo(EmailMetaData emailMetaData) {
        // EmailMetaDataPojo is redundant given that current definition is same as EmailMetaData
        // but it gives us option to change both separately
        return new EmailMetaDataPojo()
                .setFlags(emailMetaData.getFlags())
                .setGm_id(emailMetaData.getGmId())
                .setInternal_date(emailMetaData.getInternalDate())
                .setLabels(emailMetaData.getLabels())
                .setMsg_id(emailMetaData.getMsgId())
                .setSubject(emailMetaData.getSubject())
                .setThread_ids(emailMetaData.getThreadIds())
                .setX_gmail_received(emailMetaData.getXGmailReceived())
                .setSubject(emailMetaData.getSubject());
    }

    private EmailPojo createEmailPojo(Email email) {
        EmailMetaDataPojo metaData = createMetadataPojo(email.getMetadata());
        NameAndEmailAddressPojo from = new NameAndEmailAddressPojo()
                                            .setEmail(email.getFromEmail())
                                            .setName(email.getFromName());
        return new EmailPojo()
                .set_meta_(metaData)
                .setBody(email.getContent())
                .setFrom(from)
                .setHeaders(email.getHeaders())
                .setId(email.getID())
                .setLabels(email.getLabels())
                .setSubject(email.getSubject());
    }

    private String writeValueAsStringExWrapper(ObjectMapper om, Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    private String writeValueAsStringExWrapper(ObjectWriter om, Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return ExceptionUtils.rethrow(e);
        }
    }
}

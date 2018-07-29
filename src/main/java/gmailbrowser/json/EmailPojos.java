package gmailbrowser.json;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

public class EmailPojos {
    @Getter
    @Setter
    @Accessors(chain = true)
    static class EmailMetaDataPojo {
        public Number thread_ids;
        public String msg_id;
        public List<String> flags;
        public Number gm_id;
        public Long internal_date;
        public Object x_gmail_received;
        public List<String> labels;
        public String subject;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class NameAndEmailAddressPojo {
        public String name;
        public String email;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class EmailPojo {
        @JsonView(EmailViews.Complete.class)
        public EmailMetaDataPojo _meta_;

        @JsonView(EmailViews.Complete.class)
        public Map<String, String> headers;

        @JsonView(EmailViews.Truncated.class)
        public String id;

        @JsonView(EmailViews.Complete.class)
        public Map<String, String> body;

        @JsonView(EmailViews.Complete.class)
        public NameAndEmailAddressPojo from;

        @JsonView(EmailViews.Truncated.class)
        public String subject;

        @JsonView(EmailViews.Complete.class)
        public List<String> labels;
    }
}

package gmailbrowser.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EmailMetaData {
    private Number threadIds;
    private String msgId;
    private List<String> flags;
    private Number gmId;
    private Long internalDate;
    private Object xGmailReceived;
    private List<String> labels;
    private String subject;
}

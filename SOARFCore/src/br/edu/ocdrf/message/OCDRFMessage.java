package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class OCDRFMessage extends XMLSerializedable {

    @XStreamAlias("MessageStatus")
    @XStreamAsAttribute
    private String messageStatus;

    @XStreamAlias("ErrorStackTree")
    @XStreamAsAttribute
    private String errorStackTree;

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getErrorStackTree() {
        return errorStackTree;
    }

    public void setErrorStackTree(String errorStackTree) {
        this.errorStackTree = errorStackTree;
    }

}

package br.edu.ocdrf.oal.domain;

import java.util.ArrayList;
import java.util.List;

public class QueryResponseEntity {

    private String messageStatus;

    private String errorStackTree;

    private List<OResourceEntity> foundResourcesList = new ArrayList<>();

    public List<OResourceEntity> getFoundResourcesList() {
        return foundResourcesList;
    }

    public void setFoundResourcesList(List<OResourceEntity> foundResourcesList) {
        this.foundResourcesList = foundResourcesList;
    }

    public void setFoundResource(OResourceEntity foundRes) {
        this.foundResourcesList.add(foundRes);
    }

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

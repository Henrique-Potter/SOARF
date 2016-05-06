package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;

public class DirectoryResponseType2 {

    private List<DirectoryResponseType2Info> directoryResponseType2InfoList = new LinkedList<>();

    public List<DirectoryResponseType2Info> getResponseInfos() {
        return directoryResponseType2InfoList;
    }

    public void addResponseInfo(DirectoryResponseType2Info response) {
        directoryResponseType2InfoList.add(response);
    }

    public void setResponseInfos(List<DirectoryResponseType2Info> responseInfos) {
        this.directoryResponseType2InfoList = responseInfos;
    }

    public void addAllResponseInfos(List<DirectoryResponseType2Info> responseInfos) {
        this.directoryResponseType2InfoList.addAll(responseInfos);
    }

    public boolean removeDirectoryResponseType2Info(DirectoryResponseType2Info directoryResponseType2Info) {
        return this.directoryResponseType2InfoList.remove(directoryResponseType2Info);
    }

    public boolean findResourceById(String resourceId) {
        for (DirectoryResponseType2Info info : directoryResponseType2InfoList) {
            if (info.getID().equals(resourceId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((directoryResponseType2InfoList == null) ? 0 : directoryResponseType2InfoList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DirectoryResponseType2 other = (DirectoryResponseType2) obj;
        if (directoryResponseType2InfoList == null) {
            if (other.directoryResponseType2InfoList != null) {
                return false;
            }
        }
        else if (!directoryResponseType2InfoList.equals(other.directoryResponseType2InfoList)) {
            return false;
        }
        return true;
    }

}

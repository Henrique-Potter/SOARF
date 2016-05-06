package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;

public class DirectoryResponseType1 {

    private List<DirectoryResponseType1Info> directoryResponseType1InfoList = new LinkedList<>();

    public List<DirectoryResponseType1Info> getDirectoryResponseType1InfoList() {
	return directoryResponseType1InfoList;
    }

    public void setResponseInfos(List<DirectoryResponseType1Info> directoryResponseType1InfoList) {
	this.directoryResponseType1InfoList = directoryResponseType1InfoList;
    }

    public void addResourceInfo(DirectoryResponseType1Info directoryResponseType1Info) {
	this.directoryResponseType1InfoList.add(directoryResponseType1Info);
    }

    public boolean removeDiscoveryResponseInfo(DirectoryResponseType1Info directoryResponseType1Info) {
	return this.directoryResponseType1InfoList.remove(directoryResponseType1Info);
    }

    public boolean findResourceById(String resourceId) {
	for (DirectoryResponseType1Info info : directoryResponseType1InfoList) {
	    if (info.getId().equals(resourceId)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((directoryResponseType1InfoList == null) ? 0 : directoryResponseType1InfoList.hashCode());
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
	final DirectoryResponseType1 other = (DirectoryResponseType1) obj;
	if (directoryResponseType1InfoList == null) {
	    if (other.directoryResponseType1InfoList != null) {
		return false;
	    }
	} else if (!directoryResponseType1InfoList.equals(other.directoryResponseType1InfoList)) {
	    return false;
	}
	return true;
    }
}

package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;

public class DiscoveryResponse {

    private List<WorkResponseInfo> discoveryResponseInfoList = new LinkedList<>();

    public DiscoveryResponse() {
    }

    public DiscoveryResponse(DiscoveryResponse dr) {
        this.discoveryResponseInfoList = dr.discoveryResponseInfoList;
    }

    public List<WorkResponseInfo> getDiscoveryResponseInfoList() {
        return discoveryResponseInfoList;
    }

    public void setDiscoveryResponseInfoList(List<WorkResponseInfo> discoveryResponseInfoList) {
        this.discoveryResponseInfoList = discoveryResponseInfoList;
    }

    public void addDiscoveryResponseInfo(WorkResponseInfo discoveryResponseInfo) {
        this.discoveryResponseInfoList.add(discoveryResponseInfo);
    }

    public static DiscoveryResponse prepareForParsing(DiscoveryResponse dr) {
        // Anular os atributos que eu nao quero que aparecam no XML de resposta
        // - senao o xstrem pega estes atribs tb

        DiscoveryResponse discoveyResponse = new DiscoveryResponse(dr);
//
//        for (WorkResponseInfo discoveryResponseInfo : discoveyResponse.getDiscoveryResponseInfoList()) {
//            discoveryResponseInfo.setCapacityNameInRDS(null);
//            discoveryResponseInfo.setInvokeMethod(null);
//
//            for (WorkAttribute at : discoveryResponseInfo.getAttributes()) {
//                at.setOperatorInQuery(null);
//                at.setValueInQuery(null);
//                at.setNameInRDS(null);
//                at.setUnitInRDS(null);
//                at.setTypeInRDS(null);
//            }
//        }

        return discoveyResponse;
    }

    public boolean isEmpty() {
        if (this.discoveryResponseInfoList == null) {
            return true;
        }
        return this.discoveryResponseInfoList.isEmpty();
    }

    public void clear() {
        this.discoveryResponseInfoList.clear();
    }

    public boolean removeDiscoveryResponseInfo(WorkResponseInfo discoveryResponseInfo) {
        return this.discoveryResponseInfoList.remove(discoveryResponseInfo);
    }

    public boolean findResourceById(String resourceId) {
        for (WorkResponseInfo info : discoveryResponseInfoList) {
            if (info.getId().equals(resourceId)) {
                return true;
            }
        }
        return false;
    }
    //
    //	public WorkResponseInfo getResourceById(String resourceId) {
    //		for (WorkResponseInfo info : discoveryResponseInfoList) {
    //			if (info.getId().equals(resourceId)) {
    //				return info;
    //			}
    //		}
    //		return null;
    //	}

    public void removeDiscoveryResponseInfoById(String resourceId) {

        for (int i = 0; i < discoveryResponseInfoList.size(); i++) {

            WorkResponseInfo info = discoveryResponseInfoList.get(i);

            if (info.getId().equals(resourceId)) {
                discoveryResponseInfoList.remove(info);
            }
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((discoveryResponseInfoList == null) ? 0 : discoveryResponseInfoList.hashCode());
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
        final DiscoveryResponse other = (DiscoveryResponse) obj;
        if (discoveryResponseInfoList == null) {
            if (other.discoveryResponseInfoList != null) {
                return false;
            }
        }
        else if (!discoveryResponseInfoList.equals(other.discoveryResponseInfoList)) {
            return false;
        }
        return true;
    }

}

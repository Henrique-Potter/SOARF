package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;

public class ContextResponse {

    private List<WorkResponseInfo> contextResponseInfoList = new LinkedList<>();

    public ContextResponse() {
    }

    public ContextResponse(ContextResponse cr) {
        this.contextResponseInfoList = cr.getWorkResponseInfoList();
    }

    public List<WorkResponseInfo> getWorkResponseInfoList() {
        return contextResponseInfoList;
    }

    public void setWorkResponseInfoList(List<WorkResponseInfo> contextResponseInfoList) {
        this.contextResponseInfoList = contextResponseInfoList;
    }

    public void addWorkResponseInfo(WorkResponseInfo contextResponseInfo) {
        this.contextResponseInfoList.add(contextResponseInfo);
    }

    public void removeWorkResponseInfo(WorkResponseInfo contextResponseInfo) {
        this.contextResponseInfoList.remove(contextResponseInfo);
    }

    public static ContextResponse prepareForParsing(ContextResponse cr) {
        // Anular os atributos que eu nao quero que aparecam no XML de resposta
        // - senao o xstrem pega estes atribs tb

        ContextResponse contextResponse = new ContextResponse(cr);

//        for (WorkResponseInfo contextResponseInfo : cr.getWorkResponseInfoList()) {
//            contextResponseInfo.setCapacityNameInRDS(null);
//            contextResponseInfo.setInvokeMethod(null);
//
//            for (WorkAttribute at : contextResponseInfo.getAttributes()) {
//                at.setOperatorInQuery(null);
//                at.setValueInQuery(null);
//                at.setNameInRDS(null);
//                at.setTypeInRDS(null);
//                //Temporario dado que nao estou fazendo conversao das unidades
//                //Unit In RDS que vai ser retornado enquanto isso - zerar a Unit In Query
//                //at.setUnitInRDS(null);
//                at.setUnitInQuery(null);
//            }
//        }

        return contextResponse;
    }

    public boolean isEmpty() {
        return this.contextResponseInfoList.isEmpty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contextResponseInfoList == null) ? 0 : contextResponseInfoList.hashCode());
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
        final ContextResponse other = (ContextResponse) obj;
        if (contextResponseInfoList == null) {
            if (other.contextResponseInfoList != null) {
                return false;
            }
        }
        else if (!contextResponseInfoList.equals(other.contextResponseInfoList)) {
            return false;
        }
        return true;
    }
}

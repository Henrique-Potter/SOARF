package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;


public class ContextQuery {

    private String requestFrom = "";//Client Application or DS - DiscoveryService

    private List<ContextQueryTarget> targets = new LinkedList<>();

    public ContextQuery() {
    }

    public ContextQuery(ContextQuery cq) {
        this.requestFrom = cq.requestFrom;
        this.targets = cq.targets;
    }

    public ContextQuery(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public List<ContextQueryTarget> getTargets() {
        return targets;
    }

    public void addTarget(ContextQueryTarget target) {
        this.targets.add(target);
    }

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((requestFrom == null) ? 0 : requestFrom.hashCode());
        result = prime * result + ((targets == null) ? 0 : targets.hashCode());
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
        final ContextQuery other = (ContextQuery) obj;
        if (requestFrom == null) {
            if (other.requestFrom != null) {
                return false;
            }
        }
        else if (!requestFrom.equals(other.requestFrom)) {
            return false;
        }
        if (targets == null) {
            if (other.targets != null) {
                return false;
            }
        }
        else if (!targets.equals(other.targets)) {
            return false;
        }
        return true;
    }

    public static ContextQuery prepareForParsing(ContextQuery cq) {
	// Anular os atributos que eu nao quero que aparecam no XML de resposta
        // - senao o xstrem pega estes atribs tb

        ContextQuery contextQuery = new ContextQuery(cq);

//        for (ContextQueryTarget target : contextQuery.getTargets()) {
//            for (Attribute at : target.getAttributes()) {
//                at.setType(null);
//                at.setOperator(null);
//                at.setValue(null);
//            }
//        }
        return contextQuery;
    }

}

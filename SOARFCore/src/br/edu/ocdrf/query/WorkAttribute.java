package br.edu.ocdrf.query;

import br.edu.ocdrf.entities.Attribute;

public class WorkAttribute {

    public static String DYNAMIC = "Dynamic";
    public static String STATIC = "Static";

    private String nameInQuery;
    private String nameInRDS;
    private String operatorInQuery;
    private Object valueInQuery;//Valor para comparacao - solicitado na query
    private String unitInQuery;
    private String unitInRDS;
    private String typeInRDS; // Dynamic ou Static
    private Object valueUpdated;

    public WorkAttribute() {
    }

    public WorkAttribute(String nameInQuery, String nameInRDS, String operatorInQuery, Object valueInQuery,
            String unitInQuery, String unitInRDS, String typeInRDS, Object valueUpdated) {
        this.nameInQuery = nameInQuery;
        this.nameInRDS = nameInRDS;
        this.operatorInQuery = operatorInQuery;
        this.valueInQuery = valueInQuery;
        this.valueUpdated = valueUpdated;
        this.unitInQuery = unitInQuery;
        this.unitInRDS = unitInRDS;
        this.typeInRDS = typeInRDS;
    }

    public WorkAttribute(Attribute at) {
        nameInQuery = at.getName();
        operatorInQuery = at.getOperator();
        valueInQuery = at.getValue();
        unitInQuery = at.getUnit();
        typeInRDS = at.getType();
    }

    public boolean isDynamic() {
        try {
            if (typeInRDS.equals("Dynamic")) {
                return true;
            }
        }
        catch (RuntimeException e) {
        }
        return false;
    }

    public String getNameInQuery() {
        return nameInQuery;
    }

    public void setNameInQuery(String nameInQuery) {
        this.nameInQuery = nameInQuery;
    }

    public String getOperatorInQuery() {
        return operatorInQuery;
    }

    public void setOperatorInQuery(String operatorInQuery) {
        this.operatorInQuery = operatorInQuery;
    }

    public Object getValueInQuery() {
        return valueInQuery;
    }

    public void setValueInQuery(Object valueInQuery) {
        this.valueInQuery = valueInQuery;
    }

    public Object getValueUpdated() {
        return valueUpdated;
    }

    public void setValueUpdated(Object valueUpdated) {
        this.valueUpdated = valueUpdated;
    }

    public String getUnitInQuery() {
        return unitInQuery;
    }

    public void setUnitInQuery(String unitInQuery) {
        this.unitInQuery = unitInQuery;
    }

    public String getTypeInRDS() {
        return typeInRDS;
    }

    public void setTypeInRDS(String typeInRDS) {
        this.typeInRDS = typeInRDS;
    }

    public String getNameInRDS() {
        return nameInRDS;
    }

    public void setNameInRDS(String nameInRDS) {
        this.nameInRDS = nameInRDS;
    }

    public String getUnitInRDS() {
        return unitInRDS;
    }

    public void setUnitInRDS(String unitInRDS) {
        this.unitInRDS = unitInRDS;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nameInQuery == null) ? 0 : nameInQuery.hashCode());
        result = prime * result + ((nameInRDS == null) ? 0 : nameInRDS.hashCode());
        result = prime * result + ((operatorInQuery == null) ? 0 : operatorInQuery.hashCode());
        result = prime * result + ((typeInRDS == null) ? 0 : typeInRDS.hashCode());
        result = prime * result + ((unitInQuery == null) ? 0 : unitInQuery.hashCode());
        result = prime * result + ((unitInRDS == null) ? 0 : unitInRDS.hashCode());
        result = prime * result + ((valueInQuery == null) ? 0 : valueInQuery.hashCode());
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
        final WorkAttribute other = (WorkAttribute) obj;
        if (nameInQuery == null) {
            if (other.nameInQuery != null) {
                return false;
            }
        }
        else if (!nameInQuery.equals(other.nameInQuery)) {
            return false;
        }
        if (nameInRDS == null) {
            if (other.nameInRDS != null) {
                return false;
            }
        }
        else if (!nameInRDS.equals(other.nameInRDS)) {
            return false;
        }
        if (operatorInQuery == null) {
            if (other.operatorInQuery != null) {
                return false;
            }
        }
        else if (!operatorInQuery.equals(other.operatorInQuery)) {
            return false;
        }
        if (typeInRDS == null) {
            if (other.typeInRDS != null) {
                return false;
            }
        }
        else if (!typeInRDS.equals(other.typeInRDS)) {
            return false;
        }
        if (unitInQuery == null) {
            if (other.unitInQuery != null) {
                return false;
            }
        }
        else if (!unitInQuery.equals(other.unitInQuery)) {
            return false;
        }
        if (unitInRDS == null) {
            if (other.unitInRDS != null) {
                return false;
            }
        }
        else if (!unitInRDS.equals(other.unitInRDS)) {
            return false;
        }
        if (valueInQuery == null) {
            if (other.valueInQuery != null) {
                return false;
            }
        }
        else if (!valueInQuery.equals(other.valueInQuery)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNameInQuery();
    }

}

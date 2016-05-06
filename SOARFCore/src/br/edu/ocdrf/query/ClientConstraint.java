package br.edu.ocdrf.query;

public class ClientConstraint {

    /*
     * <ClientConstraint Element="Resource" Property="locatedIn" op="1meterFrom" Object="ID um Resource - AR" ObjectType="ResourceOntologyObject" />
     * <ClientConstraint Element="Resource" Property="locatedIn" op="1meterFrom" Object="PortaDaFrente" 		ObjectType="ClientOntologyObject" />
     */
    String element; //Resource ou Attribute
    String property; //Propriedade de algum Resource ou de seus atributos
    String operation; //Propriedade dentro da ontologia Location.owl
    String object; //Objeto da comparacao
    String objectType; //ResourceOntologyObject ou ClientOntologyObject

    @Override
    public String toString() {
        return "[element: " + element + ", property: " + property + ", operation: " + operation + ", object: " + object + ", objectType: " + objectType + "]";
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((element == null) ? 0 : element.hashCode());
        result = prime * result + ((object == null) ? 0 : object.hashCode());
        result = prime * result + ((objectType == null) ? 0 : objectType.hashCode());
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
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
        final ClientConstraint other = (ClientConstraint) obj;
        if (element == null) {
            if (other.element != null) {
                return false;
            }
        }
        else if (!element.equals(other.element)) {
            return false;
        }
        if (object == null) {
            if (other.object != null) {
                return false;
            }
        }
        else if (!object.equals(other.object)) {
            return false;
        }
        if (objectType == null) {
            if (other.objectType != null) {
                return false;
            }
        }
        else if (!objectType.equals(other.objectType)) {
            return false;
        }
        if (operation == null) {
            if (other.operation != null) {
                return false;
            }
        }
        else if (!operation.equals(other.operation)) {
            return false;
        }
        if (property == null) {
            if (other.property != null) {
                return false;
            }
        }
        else if (!property.equals(other.property)) {
            return false;
        }
        return true;
    }

}

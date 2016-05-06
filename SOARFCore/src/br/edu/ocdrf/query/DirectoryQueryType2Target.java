package br.edu.ocdrf.query;

public class DirectoryQueryType2Target {

    private String ID;
    private String element;
    private String property;

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        result = prime * result + ((element == null) ? 0 : element.hashCode());
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
        final DirectoryQueryType2Target other = (DirectoryQueryType2Target) obj;
        if (ID == null) {
            if (other.ID != null) {
                return false;
            }
        }
        else if (!ID.equals(other.ID)) {
            return false;
        }
        if (element == null) {
            if (other.element != null) {
                return false;
            }
        }
        else if (!element.equals(other.element)) {
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

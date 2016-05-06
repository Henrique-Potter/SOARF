package br.edu.ocdrf.query;

public class DirectoryResponseType2Info {

    private String id;
    private String property;
    private String value;

    public DirectoryResponseType2Info() {
    }

    public DirectoryResponseType2Info(String ID, String property, String value) {
        this.id = ID;
        this.property = property;
        this.value = value;
    }

    public String getID() {
        return id;
    }

    public void setID(String iD) {
        id = iD;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        final DirectoryResponseType2Info other = (DirectoryResponseType2Info) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
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
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        }
        else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}

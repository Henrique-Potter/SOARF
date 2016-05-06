package br.edu.ocdrf.entities;

public class Attribute {

    private String name;
    private String operator;
    private Object value;
    private String unit;
    private String type; //Dynamic ou Static

    public String toString() {
        return "[name: " + name + ", operator:" + operator + ", value: " + value + ", unit: " + unit + ", type: " + type + "]";
    }

    public Attribute() {
    }

    public Attribute(String name, Object value, String unit, String type) {
        super();
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.type = type;
    }

    public Attribute(String name) {
        this.name = name;
    }

    public Attribute(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDynamic() {
        try {
            if (type.equals("Dynamic")) {
                return true;
            }
        }
        catch (RuntimeException e) {
        }
        return false;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
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
        final Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!name.equals(other.name)) {
            return false;
        }
        if (operator == null) {
            if (other.operator != null) {
                return false;
            }
        }
        else if (!operator.equals(other.operator)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        }
        else if (!type.equals(other.type)) {
            return false;
        }
        if (unit == null) {
            if (other.unit != null) {
                return false;
            }
        }
        else if (!unit.equals(other.unit)) {
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

package br.edu.ocdrf.entities;

public class InvokeMethod {

    private String invokeString;
    private String operationName;
    private String returnType;

    public InvokeMethod() {
    }

    public InvokeMethod(String invokeString, String operationName, String returnType) {
        this.invokeString = invokeString;
        this.operationName = operationName;
        this.returnType = returnType;
    }

    public String getInvokeString() {
        return invokeString;
    }

    public void setInvokeString(String invokeString) {
        this.invokeString = invokeString;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((invokeString == null) ? 0 : invokeString.hashCode());
        result = prime * result + ((operationName == null) ? 0 : operationName.hashCode());
        result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
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
        final InvokeMethod other = (InvokeMethod) obj;
        if (invokeString == null) {
            if (other.invokeString != null) {
                return false;
            }
        }
        else if (!invokeString.equals(other.invokeString)) {
            return false;
        }
        if (operationName == null) {
            if (other.operationName != null) {
                return false;
            }
        }
        else if (!operationName.equals(other.operationName)) {
            return false;
        }
        if (returnType == null) {
            if (other.returnType != null) {
                return false;
            }
        }
        else if (!returnType.equals(other.returnType)) {
            return false;
        }
        return true;
    }
}

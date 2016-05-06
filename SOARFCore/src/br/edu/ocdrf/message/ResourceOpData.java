package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.OInvokeOpParameter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceOpData extends MessageData {

    public String invokeOperationID;
    public String invokeOperationName;
    public String resourceComponentName;
    public String capabilityName;

    public ArrayList<OInvokeOpParameter> invokeOperationParameters = new ArrayList<>();

    public String getResourceCapabilityKey() {
        return resourceComponentName + capabilityName;
    }

    public OInvokeOpParameter getInvParameterByName(String operationName) {
        for (OInvokeOpParameter invokeOperationParameter : invokeOperationParameters) {
            if (invokeOperationParameter.name.equals(operationName)) {
                return invokeOperationParameter;
            }
        }
        return null;
    }

}

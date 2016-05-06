package br.edu.ocdrf.message;

import br.edu.ocdrf.entities.CapabilityAttribute;
import java.util.HashMap;
import java.util.Map;

public class CapabilityData {

    public String capabilityNodeID;
    public Map<String, CapabilityAttribute> capabilityAttributes = new HashMap<>();

    public void addAttributeValue(String mapKey, String value, long date) {
        CapabilityAttribute capAtt = capabilityAttributes.get(mapKey);
        capAtt.addAttValue(value, date);
    }

}

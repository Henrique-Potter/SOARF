package br.edu.ocdrf.entities;

import br.edu.ocdrf.message.CapabilityAttributeData;
import br.edu.ocdrf.oal.domain.OAttribute;
import java.util.ArrayList;
import java.util.List;

public class CapabilityAttribute {

    public OAttribute attributeModel;
    public List<CapabilityAttributeData> attValues = new ArrayList<>();

    public void addAttValue(String value, long date) {
        CapabilityAttributeData capAttData = new CapabilityAttributeData();
        capAttData.value = value;
        capAttData.modDate = date;
        attValues.add(capAttData);
    }

    public List<CapabilityAttributeData> getDataByDate(long intialDate, long endDate) {

        List<CapabilityAttributeData> dateRangedData = new ArrayList<>();

        for (CapabilityAttributeData capAttData : attValues) {
            if (capAttData.modDate <= endDate && capAttData.modDate >= intialDate) {
                dateRangedData.add(capAttData);
            }
        }

        return dateRangedData;
    }

    public CapabilityAttributeData pullLastData() {

        int index = attValues.size() - 1;
        return attValues.get(index);

    }

    public List<CapabilityAttributeData> restrictDataByDate(long intialDate, long endDate) {

        List<CapabilityAttributeData> restrictedData = new ArrayList<>();

        for (CapabilityAttributeData capAttData : attValues) {
            if (capAttData.modDate <= endDate && capAttData.modDate >= intialDate) {
                restrictedData.add(capAttData);
            }
        }

        return restrictedData;
    }

    public void restrictToLastData() {
        List<CapabilityAttributeData> restrictedData = new ArrayList<>();
        int index = attValues.size() - 1;

        restrictedData.add(attValues.get(index));

        attValues = restrictedData;

    }

}

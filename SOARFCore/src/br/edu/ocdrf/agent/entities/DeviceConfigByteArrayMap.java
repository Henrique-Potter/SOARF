package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class DeviceConfigByteArrayMap extends XMLSerializedable{
 
    @XStreamAlias("StartOfText")
    @XStreamAsAttribute
    private String startOfText;
    
    @XStreamAlias("EndtOfText")
    @XStreamAsAttribute
    private String endtOfText;
    
    @XStreamAlias("LittleEndian")
    @XStreamAsAttribute
    private boolean littleEndian;

    @XStreamAlias("ValidSerialPacketSize")
    @XStreamAsAttribute
    private int validSerialPacketSize;
    
    @XStreamAlias("DataByteSize")
    @XStreamAsAttribute
    private int dataByteSize;
    
    @XStreamAlias("DataFormat")
    @XStreamAsAttribute
    private String dataFormat;
    
    @XStreamAlias("CharSet")
    @XStreamAsAttribute
    private String CharSet;

    public String getStartOfText() {
        return startOfText;
    }

    public void setStartOfText(String startOfText) {
        this.startOfText = startOfText;
    }

    public String getEndtOfText() {
        return endtOfText;
    }

    public void setEndtOfText(String endtOfText) {
        this.endtOfText = endtOfText;
    }

    public boolean isLittleEndian() {
        return littleEndian;
    }

    public void setLittleEndian(boolean littleEndian) {
        this.littleEndian = littleEndian;
    }

    public int getValidSerialPacketSize() {
        return validSerialPacketSize;
    }

    public void setValidSerialPacketSize(int validSerialPacketSize) {
        this.validSerialPacketSize = validSerialPacketSize;
    }

    public int getDataByteSize() {
        return dataByteSize;
    }

    public void setDataByteSize(int dataByteSize) {
        this.dataByteSize = dataByteSize;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getCharSet() {
        return CharSet;
    }

    public void setCharSet(String CharSet) {
        this.CharSet = CharSet;
    }
    
    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(DeviceConfigByteArrayMap.class);
    }

    @Override
    public DeviceConfigByteArrayMap createObjectFromXML(String xml) {
        return (DeviceConfigByteArrayMap) ontologyXStreamModel.fromXML(xml);
    }
    
}

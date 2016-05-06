package br.edu.ocdrf.entities;

import br.edu.ocdrf.oal.domain.OAttribute;
import br.edu.ocdrf.oal.domain.OResourceComponent;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ResourceState {

    private Date collectTime;
    private long interval;
    private String type;
    private String uri;
    private final List<OAttribute> attributes = new LinkedList<>();
    private List<OResourceComponent> components = new LinkedList<>();

    /**
     *
     * @param collectTime
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    /**
     *
     * @param value
     */
    public void addAttribute(OAttribute value) {
        this.attributes.add(value);
    }

    /**
     *
     * @return
     */
    public List<OAttribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    /**
     *
     * @param uri
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * @return the collectTime
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * @return the uri
     */
    public String getURI() {
        return uri;
    }

    /**
     * Retorna o intervalo entre o último estado do recurso e o atual.
     *
     * @return
     */
    public long getInterval() {
        return interval;
    }

    /**
     *
     * @param interval
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public List<OResourceComponent> getComponents() {
        return components;
    }

    public void setComponents(List<OResourceComponent> components) {
        this.components = components;
    }
    

    /**
     * Define o valor de um determinado atributo.
     *
     * @param attributeName
     * @param value
     */
    public void setAttribute(String attributeName, String value) {
        for (OAttribute attrValue : attributes) {

            if (attributeName.equals(attrValue.getNodeId())) {
                attrValue.setValue(value);
                break;
            }
        }
    }

    /**
     *
     * @param attributeName
     * @return
     */
    public OAttribute getAttribute(String attributeName) {
        for (OAttribute attrValue : attributes) {
            if (attributeName.equals(attrValue.getNodeId())) {
                return attrValue;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((collectTime == null) ? 0 : collectTime.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

}
package br.edu.ocdrf.entities;


import br.edu.ocdrf.interfaces.IContextObserver;
import java.io.Serializable;

/**
 * @author andre
 *
 */
public class ResourceObserver implements Serializable {

    private static final long serialVersionUID = -7446307891032321505L;

    private String uri;

    private String description;

    private String resourceType;

    private String serviceName;

    private IContextObserver contextObserver;

    /**
     * @return the uri
     */
    public final String getURI() {
        return uri;
    }

    /**
     *
     * @param uri
     */
    public final void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param resourceType
     */
    public final void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return the resourceType
     */
    public final String getResourceType() {
        return resourceType;
    }

    /**
     *
     * @param serviceName
     */
    public final void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the serviceName
     */
    public final String getServiceName() {
        return serviceName;
    }

    /**
     *
     * @return
     */
    public final IContextObserver getContextObserver() {
        return contextObserver;
    }

    /**
     * @param contextObserver the contextObserver to set
     */
    public final void setContextObserver(IContextObserver contextObserver) {
        this.contextObserver = contextObserver;
    }
}

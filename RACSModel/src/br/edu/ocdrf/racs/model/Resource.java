/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.racs.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author henrique
 */
@Entity
@Table(name = "resource")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resource.findAll", query = "SELECT r FROM Resource r"),
    @NamedQuery(name = "Resource.findById", query = "SELECT r FROM Resource r WHERE r.id = :id"),
    @NamedQuery(name = "Resource.findBySecretkey", query = "SELECT r FROM Resource r WHERE r.secretkey = :secretkey"),
    @NamedQuery(name = "Resource.findByUuid", query = "SELECT r FROM Resource r WHERE r.uuid = :uuid"),
    @NamedQuery(name = "Resource.findByType", query = "SELECT r FROM Resource r WHERE r.type = :type"),
    @NamedQuery(name = "Resource.findByServiceUrl", query = "SELECT r FROM Resource r WHERE r.serviceUrl = :serviceUrl"),
    @NamedQuery(name = "Resource.findBySecuritypolicy", query = "SELECT r FROM Resource r WHERE r.securitypolicy = :securitypolicy")})
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "secretkey")
    private String secretkey;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "type")
    private Integer type;
    @Column(name = "service_url")
    private String serviceUrl;
    @Column(name = "securitypolicy")
    private Integer securitypolicy;
    @OneToMany(mappedBy = "resourceFk")
    private Collection<Capability> capabilityCollection;
    @JoinColumn(name = "entityfk", referencedColumnName = "id")
    @ManyToOne
    private br.edu.ocdrf.racs.model.Entity entityfk;
    @OneToMany(mappedBy = "directoryServiceFk")
    private Collection<Resource> resourceCollection;
    @JoinColumn(name = "directory_service_fk", referencedColumnName = "id")
    @ManyToOne
    private Resource directoryServiceFk;

    public Resource() {
    }

    public Resource(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Integer getSecuritypolicy() {
        return securitypolicy;
    }

    public void setSecuritypolicy(Integer securitypolicy) {
        this.securitypolicy = securitypolicy;
    }

    @XmlTransient
    public Collection<Capability> getCapabilityCollection() {
        return capabilityCollection;
    }

    public void setCapabilityCollection(Collection<Capability> capabilityCollection) {
        this.capabilityCollection = capabilityCollection;
    }

    public br.edu.ocdrf.racs.model.Entity getEntityfk() {
        return entityfk;
    }

    public void setEntityfk(br.edu.ocdrf.racs.model.Entity entityfk) {
        this.entityfk = entityfk;
    }

    @XmlTransient
    public Collection<Resource> getResourceCollection() {
        return resourceCollection;
    }

    public void setResourceCollection(Collection<Resource> resourceCollection) {
        this.resourceCollection = resourceCollection;
    }

    public Resource getDirectoryServiceFk() {
        return directoryServiceFk;
    }

    public void setDirectoryServiceFk(Resource directoryServiceFk) {
        this.directoryServiceFk = directoryServiceFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resource)) {
            return false;
        }
        Resource other = (Resource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.Resource[ id=" + id + " ]";
    }
    
}

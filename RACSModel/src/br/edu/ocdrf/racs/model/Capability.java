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
@Table(name = "capability")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Capability.findAll", query = "SELECT c FROM Capability c"),
    @NamedQuery(name = "Capability.findById", query = "SELECT c FROM Capability c WHERE c.id = :id"),
    @NamedQuery(name = "Capability.findByName", query = "SELECT c FROM Capability c WHERE c.name = :name"),
    @NamedQuery(name = "Capability.findByCapabilityuuid", query = "SELECT c FROM Capability c WHERE c.capabilityuuid = :capabilityuuid"),
    @NamedQuery(name = "Capability.findByCapabilitytype", query = "SELECT c FROM Capability c WHERE c.capabilitytype = :capabilitytype"),
    @NamedQuery(name = "Capability.findByAccesspolicy", query = "SELECT c FROM Capability c WHERE c.accesspolicy = :accesspolicy")})
public class Capability implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "capabilityuuid")
    private String capabilityuuid;
    @Column(name = "capabilitytype")
    private String capabilitytype;
    @Column(name = "accesspolicy")
    private Integer accesspolicy;
    @OneToMany(mappedBy = "capabilityFk")
    private Collection<EntityCapability> entityCapabilityCollection;
    @JoinColumn(name = "resource_fk", referencedColumnName = "id")
    @ManyToOne
    private Resource resourceFk;
    @OneToMany(mappedBy = "capabilityFk")
    private Collection<GroupCapability> groupCapabilityCollection;

    public Capability() {
    }

    public Capability(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapabilityuuid() {
        return capabilityuuid;
    }

    public void setCapabilityuuid(String capabilityuuid) {
        this.capabilityuuid = capabilityuuid;
    }

    public String getCapabilitytype() {
        return capabilitytype;
    }

    public void setCapabilitytype(String capabilitytype) {
        this.capabilitytype = capabilitytype;
    }

    public Integer getAccesspolicy() {
        return accesspolicy;
    }

    public void setAccesspolicy(Integer accesspolicy) {
        this.accesspolicy = accesspolicy;
    }

    @XmlTransient
    public Collection<EntityCapability> getEntityCapabilityCollection() {
        return entityCapabilityCollection;
    }

    public void setEntityCapabilityCollection(Collection<EntityCapability> entityCapabilityCollection) {
        this.entityCapabilityCollection = entityCapabilityCollection;
    }

    public Resource getResourceFk() {
        return resourceFk;
    }

    public void setResourceFk(Resource resourceFk) {
        this.resourceFk = resourceFk;
    }

    @XmlTransient
    public Collection<GroupCapability> getGroupCapabilityCollection() {
        return groupCapabilityCollection;
    }

    public void setGroupCapabilityCollection(Collection<GroupCapability> groupCapabilityCollection) {
        this.groupCapabilityCollection = groupCapabilityCollection;
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
        if (!(object instanceof Capability)) {
            return false;
        }
        Capability other = (Capability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.Capability[ id=" + id + " ]";
    }
    
}

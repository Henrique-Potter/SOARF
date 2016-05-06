/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.racs.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author henrique
 */
@Entity
@Table(name = "entity_capability")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntityCapability.findAll", query = "SELECT e FROM EntityCapability e"),
    @NamedQuery(name = "EntityCapability.findByName", query = "SELECT e FROM EntityCapability e WHERE e.name = :name"),
    @NamedQuery(name = "EntityCapability.findByAccesslevel", query = "SELECT e FROM EntityCapability e WHERE e.accesslevel = :accesslevel"),
    @NamedQuery(name = "EntityCapability.findByType", query = "SELECT e FROM EntityCapability e WHERE e.type = :type"),
    @NamedQuery(name = "EntityCapability.findById", query = "SELECT e FROM EntityCapability e WHERE e.id = :id")})
public class EntityCapability implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "name")
    private String name;
    @Column(name = "accesslevel")
    private Integer accesslevel;
    @Column(name = "type")
    private Integer type;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "capability_fk", referencedColumnName = "id")
    @ManyToOne
    private Capability capabilityFk;
    @JoinColumn(name = "entity_fk", referencedColumnName = "id")
    @ManyToOne
    private br.edu.ocdrf.racs.model.Entity entityFk;

    public EntityCapability() {
    }

    public EntityCapability(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAccesslevel() {
        return accesslevel;
    }

    public void setAccesslevel(Integer accesslevel) {
        this.accesslevel = accesslevel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Capability getCapabilityFk() {
        return capabilityFk;
    }

    public void setCapabilityFk(Capability capabilityFk) {
        this.capabilityFk = capabilityFk;
    }

    public br.edu.ocdrf.racs.model.Entity getEntityFk() {
        return entityFk;
    }

    public void setEntityFk(br.edu.ocdrf.racs.model.Entity entityFk) {
        this.entityFk = entityFk;
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
        if (!(object instanceof EntityCapability)) {
            return false;
        }
        EntityCapability other = (EntityCapability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.EntityCapability[ id=" + id + " ]";
    }
    
}

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
import javax.persistence.Id;
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
@javax.persistence.Entity
@Table(name = "entity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entity.findAll", query = "SELECT e FROM Entity e"),
    @NamedQuery(name = "Entity.findByType", query = "SELECT e FROM Entity e WHERE e.type = :type"),
    @NamedQuery(name = "Entity.findByName", query = "SELECT e FROM Entity e WHERE e.name = :name"),
    @NamedQuery(name = "Entity.findByPassword", query = "SELECT e FROM Entity e WHERE e.password = :password"),
    @NamedQuery(name = "Entity.findById", query = "SELECT e FROM Entity e WHERE e.id = :id"),
    @NamedQuery(name = "Entity.findBySecurityPolicy", query = "SELECT e FROM Entity e WHERE e.securityPolicy = :securityPolicy")})
public class Entity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "type")
    private String type;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "security_policy")
    private Integer securityPolicy;
    @OneToMany(mappedBy = "entityFk")
    private Collection<EntityCapability> entityCapabilityCollection;
    @OneToMany(mappedBy = "entityfk")
    private Collection<Resource> resourceCollection;
    @OneToMany(mappedBy = "entityfk")
    private Collection<EntityGroup> entityGroupCollection;
    @OneToMany(mappedBy = "creatingEntity")
    private Collection<Group1> group1Collection;

    public Entity() {
    }

    public Entity(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSecurityPolicy() {
        return securityPolicy;
    }

    public void setSecurityPolicy(Integer securityPolicy) {
        this.securityPolicy = securityPolicy;
    }

    @XmlTransient
    public Collection<EntityCapability> getEntityCapabilityCollection() {
        return entityCapabilityCollection;
    }

    public void setEntityCapabilityCollection(Collection<EntityCapability> entityCapabilityCollection) {
        this.entityCapabilityCollection = entityCapabilityCollection;
    }

    @XmlTransient
    public Collection<Resource> getResourceCollection() {
        return resourceCollection;
    }

    public void setResourceCollection(Collection<Resource> resourceCollection) {
        this.resourceCollection = resourceCollection;
    }

    @XmlTransient
    public Collection<EntityGroup> getEntityGroupCollection() {
        return entityGroupCollection;
    }

    public void setEntityGroupCollection(Collection<EntityGroup> entityGroupCollection) {
        this.entityGroupCollection = entityGroupCollection;
    }

    @XmlTransient
    public Collection<Group1> getGroup1Collection() {
        return group1Collection;
    }

    public void setGroup1Collection(Collection<Group1> group1Collection) {
        this.group1Collection = group1Collection;
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
        if (!(object instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.Entity[ id=" + id + " ]";
    }
    
}

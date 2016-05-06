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
@Table(name = "group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Group1.findAll", query = "SELECT g FROM Group1 g"),
    @NamedQuery(name = "Group1.findById", query = "SELECT g FROM Group1 g WHERE g.id = :id"),
    @NamedQuery(name = "Group1.findByName", query = "SELECT g FROM Group1 g WHERE g.name = :name")})
public class Group1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "groupfk")
    private Collection<EntityGroup> entityGroupCollection;
    @OneToMany(mappedBy = "groupFk")
    private Collection<GroupCapability> groupCapabilityCollection;
    @JoinColumn(name = "creating_entity", referencedColumnName = "id")
    @ManyToOne
    private br.edu.ocdrf.racs.model.Entity creatingEntity;

    public Group1() {
    }

    public Group1(Integer id) {
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

    @XmlTransient
    public Collection<EntityGroup> getEntityGroupCollection() {
        return entityGroupCollection;
    }

    public void setEntityGroupCollection(Collection<EntityGroup> entityGroupCollection) {
        this.entityGroupCollection = entityGroupCollection;
    }

    @XmlTransient
    public Collection<GroupCapability> getGroupCapabilityCollection() {
        return groupCapabilityCollection;
    }

    public void setGroupCapabilityCollection(Collection<GroupCapability> groupCapabilityCollection) {
        this.groupCapabilityCollection = groupCapabilityCollection;
    }

    public br.edu.ocdrf.racs.model.Entity getCreatingEntity() {
        return creatingEntity;
    }

    public void setCreatingEntity(br.edu.ocdrf.racs.model.Entity creatingEntity) {
        this.creatingEntity = creatingEntity;
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
        if (!(object instanceof Group1)) {
            return false;
        }
        Group1 other = (Group1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.Group1[ id=" + id + " ]";
    }
    
}

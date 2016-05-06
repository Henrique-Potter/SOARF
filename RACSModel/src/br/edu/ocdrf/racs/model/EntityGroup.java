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
@Table(name = "entity_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntityGroup.findAll", query = "SELECT e FROM EntityGroup e"),
    @NamedQuery(name = "EntityGroup.findById", query = "SELECT e FROM EntityGroup e WHERE e.id = :id")})
public class EntityGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entityfk", referencedColumnName = "id")
    @ManyToOne
    private br.edu.ocdrf.racs.model.Entity entityfk;
    @JoinColumn(name = "groupfk", referencedColumnName = "id")
    @ManyToOne
    private Group1 groupfk;

    public EntityGroup() {
    }

    public EntityGroup(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public br.edu.ocdrf.racs.model.Entity getEntityfk() {
        return entityfk;
    }

    public void setEntityfk(br.edu.ocdrf.racs.model.Entity entityfk) {
        this.entityfk = entityfk;
    }

    public Group1 getGroupfk() {
        return groupfk;
    }

    public void setGroupfk(Group1 groupfk) {
        this.groupfk = groupfk;
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
        if (!(object instanceof EntityGroup)) {
            return false;
        }
        EntityGroup other = (EntityGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.EntityGroup[ id=" + id + " ]";
    }
    
}

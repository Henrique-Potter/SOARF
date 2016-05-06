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
@Table(name = "group_capability")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupCapability.findAll", query = "SELECT g FROM GroupCapability g"),
    @NamedQuery(name = "GroupCapability.findById", query = "SELECT g FROM GroupCapability g WHERE g.id = :id"),
    @NamedQuery(name = "GroupCapability.findByName", query = "SELECT g FROM GroupCapability g WHERE g.name = :name"),
    @NamedQuery(name = "GroupCapability.findByAccesslevel", query = "SELECT g FROM GroupCapability g WHERE g.accesslevel = :accesslevel"),
    @NamedQuery(name = "GroupCapability.findByType", query = "SELECT g FROM GroupCapability g WHERE g.type = :type")})
public class GroupCapability implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "accesslevel")
    private Integer accesslevel;
    @Column(name = "type")
    private Integer type;
    @JoinColumn(name = "capability_fk", referencedColumnName = "id")
    @ManyToOne
    private Capability capabilityFk;
    @JoinColumn(name = "group_fk", referencedColumnName = "id")
    @ManyToOne
    private Group1 groupFk;

    public GroupCapability() {
    }

    public GroupCapability(Integer id) {
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

    public Capability getCapabilityFk() {
        return capabilityFk;
    }

    public void setCapabilityFk(Capability capabilityFk) {
        this.capabilityFk = capabilityFk;
    }

    public Group1 getGroupFk() {
        return groupFk;
    }

    public void setGroupFk(Group1 groupFk) {
        this.groupFk = groupFk;
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
        if (!(object instanceof GroupCapability)) {
            return false;
        }
        GroupCapability other = (GroupCapability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.edu.ocdrf.racs.model.GroupCapability[ id=" + id + " ]";
    }
    
}

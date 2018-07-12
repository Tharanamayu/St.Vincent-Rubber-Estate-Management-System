/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author SahanRid
 */
@Entity
@Table(name = "treeblockstatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Treeblockstatus.findAll", query = "SELECT t FROM Treeblockstatus t")
    , @NamedQuery(name = "Treeblockstatus.findById", query = "SELECT t FROM Treeblockstatus t WHERE t.id = :id")
    , @NamedQuery(name = "Treeblockstatus.findByName", query = "SELECT t FROM Treeblockstatus t WHERE t.name = :name")})
public class Treeblockstatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treeblockstatusId", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private List<Treeblock> treeblockList;

    public Treeblockstatus() {
    }

    public Treeblockstatus(Integer id) {
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
    public List<Treeblock> getTreeblockList() {
        return treeblockList;
    }

    public void setTreeblockList(List<Treeblock> treeblockList) {
        this.treeblockList = treeblockList;
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
        if (!(object instanceof Treeblockstatus)) {
            return false;
        }
        Treeblockstatus other = (Treeblockstatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}

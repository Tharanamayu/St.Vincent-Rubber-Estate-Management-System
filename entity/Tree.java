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
 * @author SahanRid
 */
@Entity
@Table(name = "tree")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tree.findAll", query = "SELECT t FROM Tree t")
    , @NamedQuery(name = "Tree.findById", query = "SELECT t FROM Tree t WHERE t.id = :id")
    , @NamedQuery(name = "Tree.findByNo", query = "SELECT t FROM Tree t WHERE t.no = :no")
        
        
//<editor-fold defaultstate="collapsed" desc="Search Queries">
        , @NamedQuery(name = "Tree.findAllByTreeNo", query = "SELECT t FROM Tree t WHERE t.no LIKE :treeNo")
        , @NamedQuery(name = "Tree.findAllByTreeBlock", query = "SELECT t FROM Tree t WHERE t.blockId = :treeBlock")
        , @NamedQuery(name = "Tree.findAllByStatus", query = "SELECT t FROM Tree t WHERE t.treestatusId = :status")
        , @NamedQuery(name = "Tree.findAllByTreeNoStatus", query = "SELECT t FROM Tree t WHERE t.no LIKE :treeNo AND t.treestatusId = :status")
        , @NamedQuery(name = "Tree.findAllByTreeNoTreeBlock", query = "SELECT t FROM Tree t WHERE t.no LIKE :treeNo AND t.blockId = :treeBlock")
        , @NamedQuery(name = "Tree.findAllByStatusTreeBlock", query = "SELECT t FROM Tree t WHERE t.treestatusId = :status AND t.blockId = :treeBlock")
        , @NamedQuery(name = "Tree.findAllByTreeNoStatusTreeBlock", query = "SELECT t FROM Tree t WHERE t.no LIKE :treeNo AND t.treestatusId = :status AND t.blockId = :treeBlock")
//</editor-fold>
})
public class Tree implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treeId", fetch = FetchType.EAGER)
    private List<Complain> complainList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "no")
    private String no;
    @JoinColumn(name = "block_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Treeblock blockId;
    @JoinColumn(name = "treestatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Treestatus treestatusId;

    public Tree() {
    }

    public Tree(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public boolean setNo(String no) {
        boolean validity = no != null && (no.matches("\\d{3}"));
        if (validity) {
            this.no = no;
        } else {
            this.no = null;
        }
        return validity;
    }

    public Treeblock getBlockId() {
        return blockId;
    }

    public void setBlockId(Treeblock blockId) {
        this.blockId = blockId;
    }

    public Treestatus getTreestatusId() {
        return treestatusId;
    }

    public boolean setTreestatusId(Treestatus treestatusId) {
        this.treestatusId = treestatusId;
        return this.treestatusId != null;
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
        if (!(object instanceof Tree)) {
            return false;
        }
        Tree other = (Tree) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return no;
    }

    @XmlTransient
    public List<Complain> getComplainList() {
        return complainList;
    }

    public void setComplainList(List<Complain> complainList) {
        this.complainList = complainList;
    }

}

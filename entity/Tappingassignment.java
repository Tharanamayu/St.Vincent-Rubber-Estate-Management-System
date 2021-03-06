/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SahanRid
 */
@Entity
@Table(name = "tappingassignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tappingassignment.findAll", query = "SELECT t FROM Tappingassignment t")
    , @NamedQuery(name = "Tappingassignment.findById", query = "SELECT t FROM Tappingassignment t WHERE t.id = :id")
    , @NamedQuery(name = "Tappingassignment.findByDate", query = "SELECT t FROM Tappingassignment t WHERE t.date = :date")
    , @NamedQuery(name = "Tappingassignment.findByTreeBlockAndEmployee", query = "SELECT t FROM Tappingassignment t WHERE t.employeeId = :employee AND t.treeblockId = :treeBlock")    
})
public class Tappingassignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;
    @JoinColumn(name = "treeblock_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Treeblock treeblockId;

    public Tappingassignment() {
    }

    public Tappingassignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public boolean setDate(Date date) {
        this.date = date;
        return date !=null;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public boolean setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
        return employeeId != null;
    }

    public Treeblock getTreeblockId() {
        return treeblockId;
    }

    public boolean setTreeblockId(Treeblock treeblockId) {
        this.treeblockId = treeblockId;
        return treeblockId != null;
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
        if (!(object instanceof Tappingassignment)) {
            return false;
        }
        Tappingassignment other = (Tappingassignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Tappingassignment[ id=" + id + " ]";
    }
    
}

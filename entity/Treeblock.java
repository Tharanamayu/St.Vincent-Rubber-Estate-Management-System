/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SahanRid
 */
@Entity
@Table(name = "treeblock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Treeblock.findAll", query = "SELECT t FROM Treeblock t")
    , @NamedQuery(name = "Treeblock.findById", query = "SELECT t FROM Treeblock t WHERE t.id = :id")
    , @NamedQuery(name = "Treeblock.findByNo", query = "SELECT t FROM Treeblock t WHERE t.no = :no")
        
    , @NamedQuery(name = "Treeblock.findAllExceptAssignedBlockTP", query = "SELECT t FROM Treeblock t WHERE t.id NOT IN (SELECT t.treeblockId FROM Tappingassignment t)")
    , @NamedQuery(name = "Treeblock.findAllExceptAssignedBlockWD", query = "SELECT t FROM Treeblock t WHERE t.id NOT IN (SELECT w.treeblockId FROM Weedingassignment w)")
    , @NamedQuery(name = "Treeblock.findAllByEmployeeTP", query = "SELECT t FROM Treeblock t WHERE t.id IN (SELECT t.treeblockId FROM Tappingassignment t WHERE t.employeeId = :employeeId)")
    , @NamedQuery(name = "Treeblock.findAllByEmployeeWD", query = "SELECT t FROM Treeblock t WHERE t.id IN (SELECT w.treeblockId FROM Weedingassignment w WHERE w.employeeId = :employeeId)")
    
        
//<editor-fold defaultstate="collapsed" desc="Search Queries">
        , @NamedQuery(name = "Treeblock.findAllByBlockNo", query = "SELECT t FROM Treeblock t WHERE t.no LIKE :blockNo")
        , @NamedQuery(name = "Treeblock.findAllByTappingLayer", query = "SELECT t FROM Treeblock t WHERE t.tappinglayerId = :tappingLayer")
        , @NamedQuery(name = "Treeblock.findAllByStatus", query = "SELECT t FROM Treeblock t WHERE t.treeblockstatusId = :status")
        , @NamedQuery(name = "Treeblock.findAllByBlockNoStatus", query = "SELECT t FROM Treeblock t WHERE t.no LIKE :blockNo AND t.treeblockstatusId = :status")
        , @NamedQuery(name = "Treeblock.findAllByBlockNoTappingLayer", query = "SELECT t FROM Treeblock t WHERE t.no LIKE :blockNo AND t.tappinglayerId = :tappingLayer")
        , @NamedQuery(name = "Treeblock.findAllByStatusTappingLayer", query = "SELECT t FROM Treeblock t WHERE t.treeblockstatusId = :status AND t.tappinglayerId = :tappingLayer")
        , @NamedQuery(name = "Treeblock.findAllByBlockNoStatusTappingLayer", query = "SELECT t FROM Treeblock t WHERE t.no LIKE :blockNo AND t.treeblockstatusId = :status AND t.tappinglayerId = :tappingLayer")
//</editor-fold>

})
public class Treeblock implements Serializable {

    @Column(name = "numberoftrees")
    private Integer numberoftrees;
    @Column(name = "planteddate")
    @Temporal(TemporalType.DATE)
    private Date planteddate;
    @Column(name = "estimatedlifetime")
    private Integer estimatedlifetime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area")
    private BigDecimal area;
    @Lob
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "clone_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Clone cloneId;
    @JoinColumn(name = "tappinglayer_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Tappinglayer tappinglayerId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treeblockId", fetch = FetchType.EAGER)
    private List<Weedingassignment> weedingassignmentList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "no")
    private String no;
    @JoinColumn(name = "treeblockstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Treeblockstatus treeblockstatusId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treeblockId", fetch = FetchType.EAGER)
    private List<Tappingassignment> tappingassignmentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blockId", fetch = FetchType.EAGER)
    private List<Tree> treeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treeblockId", fetch = FetchType.EAGER)
    private List<Dailytapping> dailytappingList;

    public Treeblock() {
    }

    public Treeblock(Integer id) {
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
        boolean validity = no != null && (no.matches("[A-Z]"));
        if (validity) {
            this.no = no;
        } else {
            this.no = null;
        }
        return validity;
    }

    public Treeblockstatus getTreeblockstatusId() {
        return treeblockstatusId;
    }

    public boolean setTreeblockstatusId(Treeblockstatus treeblockstatusId) {
        this.treeblockstatusId = treeblockstatusId;
        return treeblockstatusId != null;
    }

    @XmlTransient
    public List<Tappingassignment> getTappingassignmentList() {
        return tappingassignmentList;
    }

    public void setTappingassignmentList(List<Tappingassignment> tappingassignmentList) {
        this.tappingassignmentList = tappingassignmentList;
    }

    @XmlTransient
    public List<Tree> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<Tree> treeList) {
        this.treeList = treeList;
    }

    @XmlTransient
    public List<Dailytapping> getDailytappingList() {
        return dailytappingList;
    }

    public void setDailytappingList(List<Dailytapping> dailytappingList) {
        this.dailytappingList = dailytappingList;
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
        if (!(object instanceof Treeblock)) {
            return false;
        }
        Treeblock other = (Treeblock) object;
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
    public List<Weedingassignment> getWeedingassignmentList() {
        return weedingassignmentList;
    }

    public void setWeedingassignmentList(List<Weedingassignment> weedingassignmentList) {
        this.weedingassignmentList = weedingassignmentList;
    }

    public Integer getNumberoftrees() {
        return numberoftrees;
    }

    public boolean setNumberoftrees(Integer numberoftrees) {
        boolean validity = numberoftrees != null && numberoftrees <= 300 && numberoftrees >= 200;
        if (validity) {
            this.numberoftrees = numberoftrees;
        } else {
            this.numberoftrees = null;
        }
        return validity;
    }

    public Date getPlanteddate() {
        return planteddate;
    }

    public boolean setPlanteddate(Date planteddate) {
        this.planteddate = planteddate;
        return planteddate != null;
    }

    public Integer getEstimatedlifetime() {
        return estimatedlifetime;
    }

    public boolean setEstimatedlifetime(Integer estimatedlifetime) {
        boolean validity = estimatedlifetime != null && estimatedlifetime > 8 && estimatedlifetime < 25;
        if (validity) {
            this.estimatedlifetime = estimatedlifetime;
        } else {
            this.estimatedlifetime = null;
        }
        return validity;
    }

    public BigDecimal getArea() {
        return area;
    }

    public boolean setArea(String area) {
        Double areaD = null;
        if (area.matches("1.0|1.1|1.2|1.3|1.4|1.5|1.6|1.7|1.8|1.9|2.0")) {
            areaD = Double.parseDouble(area);
        }
        boolean validity = areaD != null;
        if (validity) {
            this.area = new BigDecimal(areaD);
        } else {
            this.area = null;
        }
        return validity;
    }

    public String getDescription() {
        return description;
    }

    public boolean setDescription(String description) {
        this.description = description;
        return description != null;
    }

    public Clone getCloneId() {
        return cloneId;
    }

    public boolean setCloneId(Clone cloneId) {
        this.cloneId = cloneId;
        return cloneId != null;
    }

    public Tappinglayer getTappinglayerId() {
        return tappinglayerId;
    }

    public boolean setTappinglayerId(Tappinglayer tappinglayerId) {
        this.tappinglayerId = tappinglayerId;
        return tappinglayerId != null;
    }

}

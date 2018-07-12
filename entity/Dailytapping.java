/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "dailytapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dailytapping.findAll", query = "SELECT d FROM Dailytapping d")
    , @NamedQuery(name = "Dailytapping.findById", query = "SELECT d FROM Dailytapping d WHERE d.id = :id")
    , @NamedQuery(name = "Dailytapping.findByVolume", query = "SELECT d FROM Dailytapping d WHERE d.volume = :volume")
    , @NamedQuery(name = "Dailytapping.findByMetrolac", query = "SELECT d FROM Dailytapping d WHERE d.metrolac = :metrolac")
    , @NamedQuery(name = "Dailytapping.findByDryweight", query = "SELECT d FROM Dailytapping d WHERE d.dryweight = :dryweight")


//<editor-fold defaultstate="collapsed" desc="Search Queries">
        , @NamedQuery(name = "Dailytapping.findAllByTappingDate", query = "SELECT d FROM Dailytapping d WHERE d.date = :date")
        , @NamedQuery(name = "Dailytapping.findAllByEmployee", query = "SELECT d FROM Dailytapping d WHERE d.employeeId = :employee")
        , @NamedQuery(name = "Dailytapping.findAllByTreeBlock", query = "SELECT d FROM Dailytapping d WHERE d.treeblockId = :treeBlock")
        , @NamedQuery(name = "Dailytapping.findAllByTappingDateTreeBlock", query = "SELECT d FROM Dailytapping d WHERE d.date = :date AND d.treeblockId = :treeBlock")
        , @NamedQuery(name = "Dailytapping.findAllByTappingDateEmployee", query = "SELECT d FROM Dailytapping d WHERE d.date = :date AND d.employeeId = :employee")
        , @NamedQuery(name = "Dailytapping.findAllByTreeBlockEmployee", query = "SELECT d FROM Dailytapping d WHERE d.treeblockId = :treeBlock AND d.employeeId = :employee")
        , @NamedQuery(name = "Dailytapping.findAllByTappingDateTreeBlockEmployee", query = "SELECT d FROM Dailytapping d WHERE d.date = :date AND d.treeblockId = :treeBlock AND d.employeeId = :employee")
//</editor-fold>
})
public class Dailytapping implements Serializable {

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "volume")
    private BigDecimal volume;
    @Column(name = "metrolac")
    private String metrolac;
    @Column(name = "dryweight")
    private String dryweight;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;
    @JoinColumn(name = "treeblock_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Treeblock treeblockId;

    public Dailytapping() {
    }

    public Dailytapping(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public boolean setVolume(BigDecimal volume) {
        boolean validity = volume != null;
        if (validity) {
            this.volume = volume;
        } else {
            this.volume = null;
        }
        return validity;
    }

    public String getMetrolac() {
        return metrolac;
    }

    public boolean setMetrolac(String metrolac) {
        boolean validity = metrolac != null && (metrolac.matches("\\d{3}"));
        if (validity) {
            this.metrolac = metrolac;
        } else {
            this.metrolac = null;
        }
        return validity;
    }

    public String getDryweight() {
        return dryweight;
    }

    public boolean setDryweight(String dryweight) {
        boolean validity = dryweight != null && dryweight.matches("\\d{3}");
        if (validity) {
            this.dryweight = dryweight;
        } else {
            this.dryweight = null;
        }
        return validity;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public boolean setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
        return this.employeeId != null;
        
    }

    public Treeblock getTreeblockId() {
        return treeblockId;
    }

    public boolean setTreeblockId(Treeblock treeblockId) {
        this.treeblockId = treeblockId;
        return this.treeblockId != null;
        
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
        if (!(object instanceof Dailytapping)) {
            return false;
        }
        Dailytapping other = (Dailytapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Dailytapping[ id=" + id + " ]";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}

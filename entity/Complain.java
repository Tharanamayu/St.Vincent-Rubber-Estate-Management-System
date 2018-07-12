/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
@Table(name = "complain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Complain.findAll", query = "SELECT c FROM Complain c")
    , @NamedQuery(name = "Complain.findById", query = "SELECT c FROM Complain c WHERE c.id = :id")
    , @NamedQuery(name = "Complain.findByDate", query = "SELECT c FROM Complain c WHERE c.date = :date")

    //<editor-fold defaultstate="collapsed" desc="Search Queries">
    , @NamedQuery(name = "Dailytapping.findAllByComplainDate", query = "SELECT c FROM Complain c WHERE c.date = :date")
    , @NamedQuery(name = "Dailytapping.findAllByComplainStatus", query = "SELECT c FROM Complain c WHERE c.complainstatusId = :complainStatus")
    , @NamedQuery(name = "Dailytapping.findAllByComplainType", query = "SELECT c FROM Complain c WHERE c.complaintypeId = :complainType")
    , @NamedQuery(name = "Dailytapping.findAllByComplainDateComplainType", query = "SELECT c FROM Complain c WHERE c.date = :date AND c.complaintypeId = :complainType")
    , @NamedQuery(name = "Dailytapping.findAllByComplainDateComplainStatus", query = "SELECT c FROM Complain c WHERE c.date = :date AND c.complainstatusId = :complainStatus")
    , @NamedQuery(name = "Dailytapping.findAllByComplainTypeComplainStatus", query = "SELECT c FROM Complain c WHERE c.complaintypeId = :complainType AND c.complainstatusId = :complainStatus")
    , @NamedQuery(name = "Dailytapping.findAllByComplainDateComplainTypeComplainStatus", query = "SELECT c FROM Complain c WHERE c.date = :date AND c.complaintypeId = :complainType AND c.complainstatusId = :complainStatus")
//</editor-fold>

})
public class Complain implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "complainId", fetch = FetchType.EAGER)
    private List<Treatment> treatmentList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Lob
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "complainstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Complainstatus complainstatusId;
    @JoinColumn(name = "complaintype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Complaintype complaintypeId;
    @JoinColumn(name = "disease_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Disease diseaseId;
    @JoinColumn(name = "tree_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Tree treeId;

    public Complain() {
    }

    public Complain(Integer id) {
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
        return date != null;
    }

    public String getDescription() {
        return description;
    }

    public boolean setDescription(String description) {
        this.description = description;
        return description != null;
    }

    public Complainstatus getComplainstatusId() {
        return complainstatusId;
    }

    public boolean setComplainstatusId(Complainstatus complainstatusId) {
        this.complainstatusId = complainstatusId;
        return complainstatusId != null;
    }

    public Complaintype getComplaintypeId() {
        return complaintypeId;
    }

    public void setComplaintypeId(Complaintype complaintypeId) {
        this.complaintypeId = complaintypeId;
    }

    public Disease getDiseaseId() {
        return diseaseId;
    }

    public boolean setDiseaseId(Disease diseaseId) {
        this.diseaseId = diseaseId;
        return diseaseId != null;
    }

    public Tree getTreeId() {
        return treeId;
    }

    public boolean setTreeId(Tree treeId) {
        this.treeId = treeId;
        return treeId != null;
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
        if (!(object instanceof Complain)) {
            return false;
        }
        Complain other = (Complain) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Complain[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Treatment> getTreatmentList() {
        return treatmentList;
    }

    public void setTreatmentList(List<Treatment> treatmentList) {
        this.treatmentList = treatmentList;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Acer
 */
@Entity
@Table(name = "registro_vacunas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroVacunas.findAll", query = "SELECT r FROM RegistroVacunas r"),
    @NamedQuery(name = "RegistroVacunas.findById", query = "SELECT r FROM RegistroVacunas r WHERE r.id = :id"),
    @NamedQuery(name = "RegistroVacunas.findByIdVacuna", query = "SELECT r FROM RegistroVacunas r WHERE r.idVacuna = :idVacuna"),
    @NamedQuery(name = "RegistroVacunas.findByIdAplicacion", query = "SELECT r FROM RegistroVacunas r WHERE r.idAplicacion = :idAplicacion"),
    @NamedQuery(name = "RegistroVacunas.findByAplicada", query = "SELECT r FROM RegistroVacunas r WHERE r.aplicada = :aplicada"),
    @NamedQuery(name = "RegistroVacunas.findByFechaAplicacion", query = "SELECT r FROM RegistroVacunas r WHERE r.fechaAplicacion = :fechaAplicacion"),
    @NamedQuery(name = "RegistroVacunas.findByNombreVacuna", query = "SELECT r FROM RegistroVacunas r WHERE r.nombreVacuna = :nombreVacuna"),
    @NamedQuery(name = "RegistroVacunas.findByCiHijo", query = "SELECT r FROM RegistroVacunas r WHERE r.ciHijo = :ciHijo")})
public class RegistroVacunas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_vacuna")
    private Integer idVacuna;
    @Column(name = "id_aplicacion")
    private Integer idAplicacion;
    @Size(max = 2147483647)
    @Column(name = "aplicada")
    private String aplicada;
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;
    @Size(max = 2147483647)
    @Column(name = "nombre_vacuna")
    private String nombreVacuna;
    @Column(name = "ci_hijo")
    private Integer ciHijo;

    public RegistroVacunas() {
    }

    public RegistroVacunas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(Integer idVacuna) {
        this.idVacuna = idVacuna;
    }

    public Integer getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(Integer idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public String getAplicada() {
        return aplicada;
    }

    public void setAplicada(String aplicada) {
        this.aplicada = aplicada;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getNombreVacuna() {
        return nombreVacuna;
    }

    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }

    public Integer getCiHijo() {
        return ciHijo;
    }

    public void setCiHijo(Integer ciHijo) {
        this.ciHijo = ciHijo;
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
        if (!(object instanceof RegistroVacunas)) {
            return false;
        }
        RegistroVacunas other = (RegistroVacunas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paquete.RegistroVacunas[ id=" + id + " ]";
    }
    
}

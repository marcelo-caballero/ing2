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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Acer
 */
@Entity
@Table(name = "vacunas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vacunas.findAll", query = "SELECT v FROM Vacunas v"),
    @NamedQuery(name = "Vacunas.findByIdHijo", query = "SELECT v FROM Vacunas v WHERE v.idHijo = :idHijo"),
    @NamedQuery(name = "Vacunas.findByNombre", query = "SELECT v FROM Vacunas v WHERE v.nombre = :nombre"),
    @NamedQuery(name = "Vacunas.findByFechaAplicacion", query = "SELECT v FROM Vacunas v WHERE v.fechaAplicacion = :fechaAplicacion"),
    @NamedQuery(name = "Vacunas.findByAplicada", query = "SELECT v FROM Vacunas v WHERE v.aplicada = :aplicada"),
    @NamedQuery(name = "Vacunas.findByDosis", query = "SELECT v FROM Vacunas v WHERE v.dosis = :dosis"),
    @NamedQuery(name = "Vacunas.findById", query = "SELECT v FROM Vacunas v WHERE v.id = :id")})
public class Vacunas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "id_hijo")
    private int idHijo;
    @Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAplicacion;
    @Basic(optional = false)
    //@NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "aplicada")
    private String aplicada;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "dosis")
    private int dosis;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public Vacunas() {
    }

    public Vacunas(Integer id) {
        this.id = id;
    }

    public Vacunas(Integer id, int idHijo, String nombre, Date fechaAplicacion, String aplicada, int dosis) {
        this.id = id;
        this.idHijo = idHijo;
        this.nombre = nombre;
        this.fechaAplicacion = fechaAplicacion;
        this.aplicada = aplicada;
        this.dosis = dosis;
    }

    public int getIdHijo() {
        return idHijo;
    }

    public void setIdHijo(int idHijo) {
        this.idHijo = idHijo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getAplicada() {
        return aplicada;
    }

    public void setAplicada(String aplicada) {
        this.aplicada = aplicada;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof Vacunas)) {
            return false;
        }
        Vacunas other = (Vacunas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paquete.Vacunas[ id=" + id + " ]";
    }
    
}

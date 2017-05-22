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
@Table(name = "hijos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hijos.findAll", query = "SELECT h FROM Hijos h"),
    @NamedQuery(name = "Hijos.findByNombre", query = "SELECT h FROM Hijos h WHERE h.nombre = :nombre"),
    @NamedQuery(name = "Hijos.findByApellido", query = "SELECT h FROM Hijos h WHERE h.apellido = :apellido"),
    @NamedQuery(name = "Hijos.findByEmail", query = "SELECT h FROM Hijos h WHERE h.email = :email"),
    @NamedQuery(name = "Hijos.findByFechaNac", query = "SELECT h FROM Hijos h WHERE h.fechaNac = :fechaNac"),
    @NamedQuery(name = "Hijos.findByCi", query = "SELECT h FROM Hijos h WHERE h.ci = :ci")})
public class Hijos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "apellido")
    private String apellido;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 2147483647)
    @Column(name = "email")
    private String email;
    @Column(name = "fecha_nac")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ci")
    private Integer ci;

    public Hijos() {
    }

    public Hijos(Integer ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public Integer getCi() {
        return ci;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ci != null ? ci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hijos)) {
            return false;
        }
        Hijos other = (Hijos) object;
        if ((this.ci == null && other.ci != null) || (this.ci != null && !this.ci.equals(other.ci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "paquete.Hijos[ ci=" + ci + " ]";
    }
    
}

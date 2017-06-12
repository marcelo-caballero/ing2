/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import paquete.RegistroVacunas;

/**
 *
 * @author Acer
 */
@Stateless
@Path("paquete.registrovacunas")
public class RegistroVacunasFacadeREST extends AbstractFacade<RegistroVacunas> {

    @PersistenceContext(unitName = "restPU")
    private EntityManager em;

    public RegistroVacunasFacadeREST() {
        super(RegistroVacunas.class);
    }

    @POST
    @Override
    @Consumes({ MediaType.APPLICATION_JSON})
    public void create(RegistroVacunas entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, RegistroVacunas entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Override
    @Produces({ MediaType.APPLICATION_JSON})
    public List<RegistroVacunas> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("/vacuna/asc")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> vacuna_asc(@QueryParam("ciHijo") Integer ci) {
        
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = :param "+
                          "order by r.nombreVacuna asc";
        Query query = em.createQuery(consulta);
        query.setParameter("param", ci);
        
        
        
        return query.getResultList();
        
    }
    
    @GET
    @Path("/vacuna/desc")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> vacuna_desc(@QueryParam("ciHijo") Integer ci) {
        
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = :param "+
                          "order by r.nombreVacuna desc";
        Query query = em.createQuery(consulta);
        query.setParameter("param", ci);
        
        
        
        return query.getResultList();
        
    }

    @GET
    @Path("/fecha/asc")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> fecha_asc(@QueryParam("ciHijo") Integer ci) {
        
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = :param "+
                          "order by r.fechaAplicacion asc";
        Query query = em.createQuery(consulta);
        query.setParameter("param", ci);
        
        
        
        return query.getResultList();
        
    }
    
    @GET
    @Path("/fecha/desc")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> fecha_desc(@QueryParam("ciHijo") Integer ci) {
        
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = :param "+
                          "order by r.fechaAplicacion desc";
        Query query = em.createQuery(consulta);
        query.setParameter("param", ci);
        
        
        
        return query.getResultList();
        
    }
    
    @GET
    @Path("/aplicada/asc")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> aplicada_asc(@QueryParam("ciHijo") Integer ci) {
        
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = :param "+
                          "order by r.aplicada asc";
        Query query = em.createQuery(consulta);
        query.setParameter("param", ci);
        
        
        
        return query.getResultList();
        
    }
    
    @GET
    @Path("/aplicada/desc")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> aplicada_desc(@QueryParam("ciHijo") Integer ci) {
        
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = :param "+
                          "order by r.aplicada desc";
        Query query = em.createQuery(consulta);
        query.setParameter("param", ci);
        
        
        
        return query.getResultList();
        
    }
    
    @GET
    @Path("/usuario")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> vacunas_aplicar(@QueryParam("email") String  email) {
        Date fecha = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(fecha); 
        c.add(Calendar.DATE, 2);
        fecha = c.getTime();
        
        String consulta = "select r from RegistroVacunas r "+
                          "where r.ciHijo = "+
                            "all (select h.ci from Hijos h where h.email = :email) "+
                          "and r.aplicada like 'No' "+
                          "and r.fechaAplicacion < :fecha";
        Query query = em.createQuery(consulta);
        query.setParameter("email", email);
        query.setParameter("fecha", fecha ,TemporalType.DATE);
        
        
        
        return query.getResultList();
        
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

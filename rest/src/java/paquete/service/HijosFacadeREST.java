/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import paquete.Hijos;

/**
 *
 * @author Acer
 */
@Stateless
@Path("paquete.hijos")
public class HijosFacadeREST extends AbstractFacade<Hijos> {

    @PersistenceContext(unitName = "restPU")
    private EntityManager em;

    public HijosFacadeREST() {
        super(Hijos.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Hijos entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Hijos entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    /*@GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Hijos find(@PathParam("id") Integer id) {
        return super.find(id);
    }*/

    @GET
    @Override
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Hijos> findAll() {
        return super.findAll();
    }

    /*@GET
    @Path("{from}/{to}")
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Hijos> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }*/

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("/hijos")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Hijos> buscar(@QueryParam("correo") String correo) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Hijos> criteriaQuery = criteriaBuilder.createQuery(Hijos.class);
        Root<Hijos> from = criteriaQuery.from(Hijos.class);
        Predicate condition = criteriaBuilder.equal(from.get("email"), correo);
        
        criteriaQuery.where(condition);
        Query query = em.createQuery(criteriaQuery);
        
        return query.getResultList();
        
    }
    
    @GET
    @Path("/hijoInfo")
    @Produces({MediaType.APPLICATION_JSON})
    public Hijos info_hijo(@QueryParam("ci") Integer ci) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Hijos> criteriaQuery = criteriaBuilder.createQuery(Hijos.class);
        Root<Hijos> from = criteriaQuery.from(Hijos.class);
        Predicate condition = criteriaBuilder.equal(from.get("ci"), ci);
        
        criteriaQuery.where(condition);
        Query query = em.createQuery(criteriaQuery);
        
        return (Hijos)query.getSingleResult();
        
    }

    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

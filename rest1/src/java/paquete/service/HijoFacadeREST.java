/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import paquete.Hijo;


/**
 *
 * @author Acer
 */
@Stateless
@Path("hijo")
public class HijoFacadeREST extends AbstractFacade<Hijo> {

    @PersistenceContext(unitName = "rest1PU")
    private EntityManager em;

    public HijoFacadeREST() {
        super(Hijo.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @POST
    @Path("/listarHijos")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public List<Hijo> listarHijos(Hijo hijo) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Hijo> criteriaQuery = criteriaBuilder.createQuery(Hijo.class);
        Root<Hijo> from = criteriaQuery.from(Hijo.class);
        Predicate condition = criteriaBuilder.equal(from.get("idPadre"), hijo.getIdPadre());
        
        criteriaQuery.where(condition);
        Query query = em.createQuery(criteriaQuery);
        
        return query.getResultList();
        
    }
    
    
    @POST
    @Path("/obtenerHijo")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Hijo obtenerHijo(Hijo hijo) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Hijo> criteriaQuery = criteriaBuilder.createQuery(Hijo.class);
        Root<Hijo> from = criteriaQuery.from(Hijo.class);
        Predicate condition = criteriaBuilder.equal(from.get("id"), hijo.getId());
        
        criteriaQuery.where(condition);
        Query query = em.createQuery(criteriaQuery);
        
        Hijo h = new Hijo();
        
        try{
             h = (Hijo) query.getSingleResult();
        }catch(NoResultException e ){
            
        }catch(Exception e){
            
        }

     return h;
        
       
    }
    
    
/*
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Hijo entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Hijo entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Hijo find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Hijo> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Hijo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    */
    
}

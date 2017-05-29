/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paquete.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
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

   /* @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public RegistroVacunas find(@PathParam("id") Integer id) {
        return super.find(id);
    }*/

    @GET
    @Override
    @Produces({ MediaType.APPLICATION_JSON})
    public List<RegistroVacunas> findAll() {
        return super.findAll();
    }

    /*@GET
    @Path("{from}/{to}")
    @Produces({ MediaType.APPLICATION_JSON})
    public List<RegistroVacunas> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }*/
    
    @GET
    @Path("/vacuna")
    @Produces({MediaType.APPLICATION_JSON})
    
    public List<RegistroVacunas> buscar(@QueryParam("ciHijo") String ci) {
        
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<RegistroVacunas> criteriaQuery = criteriaBuilder.createQuery(RegistroVacunas.class);
        Root<RegistroVacunas> from = criteriaQuery.from(RegistroVacunas.class);
        Predicate condition = criteriaBuilder.equal(from.get("ciHijo"), ci);
        
        criteriaQuery.where(condition);
        Query query = em.createQuery(criteriaQuery);
        
        
        List<RegistroVacunas> lista = (List<RegistroVacunas>)query.getResultList();
        
        
        return lista;
        
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

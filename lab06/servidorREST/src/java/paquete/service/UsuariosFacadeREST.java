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
import javax.persistence.PersistenceContextType;
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
import javax.ws.rs.core.MediaType;
import paquete.Usuarios;

/**
 *
 * @author Acer
 */
@Stateless
@Path("paquete.usuarios")
public class UsuariosFacadeREST extends AbstractFacade<Usuarios> {

    //@PersistenceContext(unitName = "servidorRESTPU",type=PersistenceContextType.TRANSACTION)
    @PersistenceContext(unitName = "servidorRESTPU")
    private EntityManager em;

    public UsuariosFacadeREST() {
        super(Usuarios.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Usuarios entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Usuarios entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    /*@GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Usuarios find(@PathParam("id") Integer id) {
 
        return super.find(id);
        
    }*/
    
    @GET
    @Path("/{correo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Usuarios buscar(@PathParam("correo") String correo) {
        System.out.println(correo);
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Usuarios> criteriaQuery = criteriaBuilder.createQuery(Usuarios.class);
        Root<Usuarios> from = criteriaQuery.from(Usuarios.class);
        Predicate condition = criteriaBuilder.equal(from.get("correo"), correo);
        
        criteriaQuery.where(condition);
        Query query = em.createQuery(criteriaQuery);
        
        Usuarios usu = new Usuarios();
        
        try{
             usu = (Usuarios) query.getSingleResult();
        }catch(NoResultException e ){
            
        }

     return usu;
        
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usuarios> findAll() {
        return super.findAll();
    }

    /*@GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usuarios> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }*/

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

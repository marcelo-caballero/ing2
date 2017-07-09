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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
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

import paquete.Vacunas;
import paquete.Usuario;


/**
 *
 * @author Acer
 */
@Stateless
@Path("vacunas")
public class VacunasFacadeREST extends AbstractFacade<Vacunas> {

    @PersistenceContext(unitName = "rest1PU")
    private EntityManager em;

    public VacunasFacadeREST() {
        super(Vacunas.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @POST
    @Path("/tablaVacunacion")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public List<Vacunas> tablaDeVacunacion(Vacunas vacuna,
                                    @QueryParam("orden") String orden,
                                    @QueryParam("campo") String campo) {

        if(campo == null || orden== null ){
            campo = "nombre";
            orden = "asc";   
        }
        
        String consulta = "select r from Vacunas r "+
                          "where r.idHijo = :param "+
                          "order by r."+campo+" "+orden+", r.dosis "+orden;
           
        List<Vacunas> lista = null;
        try{
            Query query = em.createQuery(consulta);
            query.setParameter("param", vacuna.getIdHijo());
            
            lista = (List<Vacunas>) query.getResultList();
        }catch(NoResultException e ){
            
        }catch(Exception e){
            
        }
        
        return lista;
        
    }
    
    @POST
    @Path("/aplicacionVacunas")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
   
    public List<Vacunas> vacunas_aplicar(Usuario usuario,
                                @QueryParam("dias") int cantDias) {
        
        Date fecha = new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(fecha); 
        c.add(Calendar.DATE, cantDias);
        fecha = c.getTime();
        
        String consulta = "select r from Vacunas r "+
                          "where r.idHijo = "+
                            "any (select h.id from Hijo h, Usuario u where h.idPadre = u.id and u.correo like :correo and r.idHijo = h.id) "+
                          "and r.aplicada like 'No' "+
                          "and r.fechaAplicacion <= :fecha";
        List<Vacunas> lista = null;
        try{
            System.out.println(fecha);
            
            
            Query query = em.createQuery(consulta);
            query.setParameter("correo", usuario.getCorreo());
            query.setParameter("fecha", fecha ,TemporalType.DATE);
            lista = (List<Vacunas>)query.getResultList();
        }catch(Exception e){
            
        }
        
        
        return lista;
        
    }
    
   /* @GET
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
                            "any (select h.ci from Hijos h where h.email like :email and r.ciHijo = h.ci) "+
                          "and r.aplicada like 'No' "+
                          "and r.fechaAplicacion < :fecha";
        Query query = em.createQuery(consulta);
        query.setParameter("email", email);
        query.setParameter("fecha", fecha ,TemporalType.DATE);
        
        
        
        return query.getResultList();
        
    }
/*
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Vacunas entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Vacunas entity) {
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
    public Vacunas find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vacunas> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vacunas> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

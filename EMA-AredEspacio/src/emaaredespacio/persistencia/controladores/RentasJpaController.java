/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emaaredespacio.persistencia.controladores;

import emaaredespacio.modelo.Renta;
import emaaredespacio.persistencia.controladores.exceptions.NonexistentEntityException;
import emaaredespacio.persistencia.entidad.Clientes;
import emaaredespacio.persistencia.entidad.Rentas;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Adrián Bustamante Zarate
 * @date 1/04/2018
 * @time 05:50:34 PM
 */
public class RentasJpaController {

    /**
     * Constructor sobrecargado.
     *
     * @param fabricaEntidad Referencia a la persistenacia.
     */
    public RentasJpaController(EntityManagerFactory fabricaEntidad) {
        this.fabricaEntidad = fabricaEntidad;
    }
    private EntityManagerFactory fabricaEntidad = null;

    /**
     * Getter de la variable fabricaEntidad.
     *
     * @return fabricaEntidad.
     */
    public EntityManager getEntityManager() {
        return fabricaEntidad.createEntityManager();
    }

    public boolean crearRenta(Rentas renta, Clientes cliente) {
        boolean seCreoRenta = true;
        EntityManager entidad = getEntityManager();

        try {
            entidad.getTransaction().begin();
            Rentas nuevaRenta = new Rentas();
            nuevaRenta.setFecha(renta.getFecha());
            
            nuevaRenta.setHoraFin(renta.getHoraFin());
            nuevaRenta.setHoraIni(renta.getHoraIni());
            nuevaRenta.setIdCliente(cliente);
            nuevaRenta.setIdRenta(null);
            nuevaRenta.setMonto(renta.getMonto());
            nuevaRenta.setEstado(true);
            nuevaRenta.setPagoRealizado(false);

            entidad.persist(nuevaRenta);

            entidad.getTransaction().commit();

        } catch (RollbackException ex) {
            System.out.println("Excepción en metodo crearRenta() en: " + ex.getMessage());
            if (entidad.getTransaction().isActive()) {
                entidad.getTransaction().rollback();
            }
            seCreoRenta = false;
        } finally {
            entidad.close();
        }

        return seCreoRenta;
    }

    public boolean modificarRenta(Rentas renta) throws NonexistentEntityException {
        boolean modificoRenta = true;
        Rentas rentaModificada = buscarRenta(renta.getIdRenta());
        rentaModificada.setHoraFin(renta.getHoraFin());
        rentaModificada.setFecha(renta.getFecha());
        rentaModificada.setHoraIni(renta.getHoraIni());
        rentaModificada.setPagoRealizado(renta.getPagoRealizado());
        rentaModificada.setMonto(renta.getMonto());
        rentaModificada.setEstado(renta.getEstado());
        
        EntityManager entidad = getEntityManager();
        try {
            entidad = getEntityManager();
            entidad.getTransaction().begin();
            rentaModificada = entidad.merge(rentaModificada);
            entidad.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rentaModificada.getIdRenta();
                if (buscarRenta(id) == null) {
                    throw new NonexistentEntityException("The rentas with id " + id + " no longer exists.");
                }
            }
            modificoRenta = false;
            throw ex;
        } finally {
            if (entidad != null) {
                entidad.close();
            }
        }
        
        return modificoRenta;
    }
    
    public boolean cancelarRenta(int idRenta) throws NonexistentEntityException {
        boolean canceloRenta = true;
        Rentas rentaCancelada = buscarRenta(idRenta);
        rentaCancelada.setEstado(false);
        
        EntityManager entidad = getEntityManager();
        try {
            entidad = getEntityManager();
            entidad.getTransaction().begin();
            rentaCancelada = entidad.merge(rentaCancelada);
            entidad.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rentaCancelada.getIdRenta();
                if (buscarRenta(id) == null) {
                    throw new NonexistentEntityException("The rentas with id " + id + " no longer exists.");
                }
            }
            canceloRenta = false;
            throw ex;
        } finally {
            if (entidad != null) {
                entidad.close();
            }
        }
        
        return canceloRenta;
    }

    public Rentas buscarRenta(int idRenta) {
        EntityManager entidad = getEntityManager();
        try {
            return entidad.find(Rentas.class, idRenta);
        } finally {
            entidad.close();
        }
    }
    
    public List<Rentas> buscarRentasSegunCliente(Integer idCliente){
        List<Rentas> resultadoBusqueda = buscarTodasRentas();
        List<Rentas> resultadoRentasSegunCliente = new ArrayList<>();
        
        for (Rentas rentas : resultadoBusqueda) {
            if(rentas.getIdCliente().getIdCliente().compareTo(idCliente) == 0){
                resultadoRentasSegunCliente.add(rentas);
            }
        }
        
        return resultadoRentasSegunCliente;
    }
    
    private List<Rentas> findRentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager entidad = getEntityManager();
        try {
            CriteriaQuery cq = entidad.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rentas.class));
            Query q = entidad.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            entidad.close();
        }
    }

    public List<Rentas> buscarTodasRentas() {
        return findRentasEntities(true, -1, -1);
    }

}

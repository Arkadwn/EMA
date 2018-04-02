package emaaredespacio.persistencia.controladores;

import emaaredespacio.persistencia.entidad.Alumnos;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 *
 * @author Miguel Leonardo Jimenez Jimenez
 * @date 1/04/2018
 * @time 07:08:52 PM
 */
public class AlumnosJpaController implements IControladorAlumnos{

    public AlumnosJpaController(){
        this.fabricaEntidad = Persistence.createEntityManagerFactory("EMA-AredEspacioPU", null);
    }
    private EntityManagerFactory fabricaEntidad = null;
    
    /**
     * Getter de la variable fabricaEntidad.
     *
     * @return fabricaEntidad.
     */
    private EntityManager getEntityManager() {
        return fabricaEntidad.createEntityManager();
    }
    
    @Override
    public boolean guardarAlumno(Alumnos alumno) {
        boolean validacion = true;
        
        EntityManager conexion = getEntityManager();
        EntityTransaction transaccion = null;
        try{
            transaccion  = conexion.getTransaction();
            
            transaccion.begin();
            
            conexion.persist(alumno);
            
            transaccion.commit();
        }catch(RollbackException ex){
            Logger.getLogger(AlumnosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            if(transaccion.isActive()){
                transaccion.rollback();
            }
            validacion = false;
        }finally{
            conexion.close();
        }
        
        return validacion;
    }

    @Override
    public boolean editarAlumno(Alumnos alumno) {
        boolean validacion = true;
        EntityManager conexion = getEntityManager();
        EntityTransaction transaccion = null;
        Alumnos alumnoActual;
        
        try{
            transaccion = conexion.getTransaction();
            transaccion.begin();
            alumnoActual = conexion.find(Alumnos.class, alumno.getMatricula());
            
            alumnoActual.setApellidos(alumno.getApellidos());
            alumnoActual.setNombre(alumno.getNombre());
            alumnoActual.setCorreo(alumno.getCorreo());
            alumnoActual.setDireccion(alumno.getDireccion());
            alumnoActual.setEstado(alumno.getEstado());
            alumnoActual.setImagen(alumno.getImagen());
            alumnoActual.setTelefono(alumno.getTelefono());
            
            transaccion.commit();
        }catch(RollbackException ex){
            if(transaccion.isActive()){
                transaccion.rollback();
            }
            validacion = false;
        }
        
        return validacion;
    }

    @Override
    public List<Alumnos> buscarAlumno(String palabraClave) {
        List<Alumnos> colaboradores = new ArrayList();
        
        EntityManager conexion = getEntityManager();
    
        colaboradores = conexion.createQuery("SELECT a FROM Alumnos a WHERE a.nombre LIKE '%"+palabraClave+"%' OR a.apellidos LIKE '%"+palabraClave+"%'").getResultList();
        
        return colaboradores;
    }
    
    
}
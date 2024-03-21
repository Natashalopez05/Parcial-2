package util;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.List;
public class BaseServiceDatabase<T> {
    private static EntityManagerFactory emf;
    private final Class<T> claseEntidad;
    public BaseServiceDatabase(Class<T> claseEntidad) {
        emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        this.claseEntidad = claseEntidad;

    }
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
    private Object getIdValue(T entidad){
        if(entidad == null)
            return null;

        for(Field f : entidad.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(Id.class))
                continue;

            try {
                f.setAccessible(true);
                Object valorCampo = f.get(entidad);
                return valorCampo;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    public T dbFind(Object id)  {
        try (EntityManager em = getEntityManager()) {
            return em.find(claseEntidad, id);
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<T> dbFindAll() {
        try (EntityManager em = getEntityManager()) {
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T dbCreate(T entidad) throws IllegalArgumentException, PersistenceException{
        try (EntityManager em = getEntityManager()) {
            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return entidad;
    }
    public T dbModify(T entidad){
        try (EntityManager em = getEntityManager()){
            em.getTransaction().begin();
            em.merge(entidad);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return null;
        }
        return entidad;
    }
    public boolean dbRemove(Object entidadId){
        try (EntityManager em = getEntityManager();) {
            em.getTransaction().begin();
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
package servicios;

import encapsulaciones.Formulario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import util.BaseServiceDatabase;
import java.util.List;
public class FormularioService  extends BaseServiceDatabase<Formulario>{

    public FormularioService() {
        super(Formulario.class);
    }

    public Formulario find(String id) {
        return this.dbFind(id);
    }

    public List<Formulario> findAll() {
        return this.dbFindAll();
    }
    public Formulario create(String nombre, String apellido, String educacion, String sector) {
        Formulario formulario = new Formulario();
        formulario.setNombre(nombre);
        formulario.setApellido(apellido);
        formulario.setEducacion(educacion);
        formulario.setSector(sector);
        return this.createInDatabase(formulario);
    }
    public Formulario createInDatabase(Formulario formulario) {
        return this.dbCreate(formulario);
    }
    public Formulario modify(String nombre, String apellido, String eduacion, String sector) {
        Formulario formulario = this.find(nombre);
        formulario.setApellido(apellido);
        formulario.setEducacion(eduacion);
        formulario.setSector(sector);
        return this.dbModify(formulario);
    }
    public Formulario modifyInDatabase(Formulario formulario) {
        return this.dbModify(formulario);
    }
    public void desactivar(String nombre) {
        Formulario formulario = this.find(nombre);
        this.dbModify(formulario);
    }
    public boolean delete(String nombre) {
        return this.dbRemove(nombre);
    }
    public List<Formulario> getFormulariosBySector(String sector) {
        EntityManager entityManager = this.getEntityManager();
        Query query = entityManager.createQuery("SELECT f FROM Formulario f WHERE f.sector = :sector");
        query.setParameter("sector", sector);
        List<Formulario> formularios = query.getResultList();
        entityManager.close();
        return formularios;
    }
    public List<Formulario> getFormulariosByUsuario(String username) {
        EntityManager entityManager = this.getEntityManager();
        Query query = entityManager.createQuery("SELECT f FROM Formulario f WHERE f.usuario.username = :username");
        query.setParameter("username", username);
        List<Formulario> formularios = query.getResultList();
        entityManager.close();
        return formularios;
    }
    public Formulario save(Formulario formulario) {
        if(formulario.getNombre() == null || formulario.getApellido() == null || formulario.getEducacion()==null || formulario.getSector() == null) {
            return null;
        }else {
            return this.createInDatabase(formulario);
        }
    }


}

package servicios;

import encapsulaciones.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import util.BaseServiceDatabase;
import java.util.List;

public class UsuarioService extends BaseServiceDatabase<Usuario> {

    public UsuarioService() {
        super(Usuario.class);
    }

    public Usuario find(String username) {
        return this.dbFind(username);
    }

    public List<Usuario> findAll() {
        return this.dbFindAll();
    }

    public Usuario create(String username, String nombre, String password, boolean supervisor, boolean encuestador) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setNombre(nombre);
        usuario.setPassword(password);
        usuario.setSupervisor(supervisor);
        usuario.setEncuestador(encuestador);


        return this.createInDatabase(usuario);
    }

    public Usuario createInDatabase(Usuario usuario) {
        return this.dbCreate(usuario);
    }

    public Usuario modify(String username, String nombre, String password, boolean supervisor, boolean encuestador) {
        Usuario usuario = this.find(username);
        usuario.setNombre(nombre);
        usuario.setPassword(password);
        usuario.setSupervisor(supervisor);
        usuario.setEncuestador(encuestador);

        return this.dbModify(usuario);
    }
    public Usuario modifyInDatabase(Usuario usuario) {
        return this.dbModify(usuario);
    }

    public void desactivar(String username) {
        Usuario usuario = this.find(username);
        usuario.setActive(false);
        this.dbModify(usuario);
    }

    public boolean delete(String username) {
        return this.dbRemove(username);
    }

    public boolean checkPassword(String username, String password) {
        Usuario usuario = this.find(username);
        return usuario != null && usuario.getPassword().equals(password);
    }

    public Usuario findByUsername(String username) {
        try (EntityManager em = getEntityManager()) {
            Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.username = :username");
            query.setParameter("username", username);
            return (Usuario) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public Usuario save(Usuario usuario) {
        if (usuario.getUsername() != null && find(usuario.getUsername()) != null) {
            return modifyInDatabase(usuario);
        } else {
            return createInDatabase(usuario);
        }
    }
}
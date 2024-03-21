package encapsulaciones;
import jakarta.persistence.*;
import java.io.Serializable;
import encapsulaciones.Usuario;

@Entity
public class Formulario implements Serializable {
    @Id
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String sector;
    @ManyToOne
    private Usuario usuario;

    public Formulario(String nombre, String apellido, String sector, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.sector = sector;
        this.usuario = usuario;
    }

    public Formulario() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Formulario{" + "nombre=" + nombre + ", apellido=" + apellido + ", sector=" + sector + ", usuario=" + usuario + '}';
    }
}
package encapsulaciones;
import jakarta.persistence.*;
import java.io.Serializable;
import encapsulaciones.Usuario;

@Entity
public class Formulario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String sector;
    @Column(nullable = false)
    private String educacion;
    @ManyToOne
    private Usuario usuario;

    private Ubicacion ubicacion;

    public Formulario( String nombre, String apellido, String sector, String educacion) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.sector = sector;
        this.educacion = educacion;
    }

    public Formulario() {

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public String getEducacion() {
        return educacion;
    }
    public void setEducacion(String educacion) {
        this.educacion = educacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Formulario{" + "nombre=" + nombre + ", apellido=" + apellido + ", educacion"+ educacion+ ", sector=" + sector + ", usuario=" + usuario + '}';
    }


    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion =   ubicacion;
    }

}
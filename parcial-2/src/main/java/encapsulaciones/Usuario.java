package encapsulaciones;
import jakarta.persistence.*;

import java.io.Serializable;
@Entity
public class Usuario implements Serializable {
    @Id
    private String username;
    private String nombre;
    private String password;
    private boolean supervisor;
    private boolean encuestador;
    private boolean active;

    public Usuario(String username, String nombre, String password, boolean supervisor, boolean encuestador) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.supervisor = supervisor;
        this.encuestador = encuestador;
        this.active = true;
    }

    public Usuario() {
        this.active = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   public boolean isSupervisor() {
        return supervisor;
    }
    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isEncuestador() {
        return encuestador;
    }
    public void setEncuestador(boolean encuestador) {
        this.encuestador = encuestador;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", supervisor=" + supervisor +
                ", encuestador=" + encuestador +
                '}';
    }
}
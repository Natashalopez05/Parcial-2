package encapsulaciones;

import jakarta.persistence.*;
import java.io.Serializable;
import encapsulaciones.Usuario;

@Entity
    public class Ubicacion implements Serializable{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private double latitud;
        private double longitud;

        // Constructor, getters y setters
        public Ubicacion(double latitud, double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

    public Ubicacion() {

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitud() {
            return latitud;
        }

        public void setLatitud(double latitud) {
            this.latitud = latitud;
        }
        public double getLongitud() {
            return longitud;
        }
        public void setLongitud(double longitud) {
            this.longitud = longitud;
        }
    }



package servicios;
import encapsulaciones.Ubicacion;
import util.BaseServiceDatabase;

import java.util.List;

public class UbicacionService extends BaseServiceDatabase<Ubicacion> {


    public UbicacionService() {
        super(Ubicacion.class);
    }
    public Ubicacion find(double latitud, double longitud) {
        return this.dbFind(latitud);
    }
    public Ubicacion create(double latitud, double longitud) {
        Ubicacion ubicacion = new Ubicacion(latitud, longitud);
        return this.createInDatabase(ubicacion);
    }
    public Ubicacion createInDatabase(Ubicacion ubicacion) {
        return this.dbCreate(ubicacion);
    }
    public Ubicacion modify(double latitud, double longitud) {
        Ubicacion ubicacion = this.find(latitud, longitud);
        return this.dbModify(ubicacion);
    }
    public Ubicacion modifyInDatabase(Ubicacion ubicacion) {
        return this.dbModify(ubicacion);
    }
    public boolean delete(double latitud, double longitud) {
        return this.dbRemove(latitud);
    }


}



package controladores;

import encapsulaciones.Ubicacion;
import io.javalin.Javalin;
import io.javalin.http.Context;
import servicios.UbicacionService;
import util.BaseController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UbicacionController extends BaseController {

    private final UbicacionService ubicacionService;

    public UbicacionController(Javalin app, UbicacionService ubicacionService) {
        super(app);
        this.ubicacionService = ubicacionService;
    }

    public void proteger(Context ctx) {
        //Usuario usuario = ctx.sessionAttribute("usuario");
        //if (usuario == null || !usuario.isSupervisor())
        //    ctx.redirect("/auth/login");
    }

    public void listar(Context ctx) {
        List<Ubicacion> ubicaciones = ubicacionService.dbFindAll();

        Map<String, Object> modelo = new HashMap<>();
        modelo.put("ubicaciones", ubicaciones);

        ctx.render("/public/templates/map.html", modelo);
    }

    public void listarUno(Context ctx) {
        double latitud = Double.parseDouble(ctx.pathParam("latitud"));
        double longitud = Double.parseDouble(ctx.pathParam("longitud"));
        Ubicacion ubicacion = ubicacionService.find(latitud, longitud);

        Map<String, Object> modelo = setModelo(
                "ubicacion", ubicacion);

        ctx.render("/public/templates/map.html", modelo);
    }

    public void crear(Context ctx) {
        double latitud = Double.parseDouble(ctx.formParam("latitud"));
        double longitud = Double.parseDouble(ctx.formParam("longitud"));
        if (ubicacionService.find(latitud, longitud) != null) {
            ctx.status(400);
            ctx.result("La ubicacion ya existe");
        } else {
            Ubicacion ubicacion = ubicacionService.create(latitud, longitud);
            ctx.status(201);
            ctx.json(ubicacion);
        }
    }

    public void modificar(Context ctx) {
        double latitud = Double.parseDouble(ctx.formParam("latitud"));
        double longitud = Double.parseDouble(ctx.formParam("longitud"));
        Ubicacion ubicacion = ubicacionService.find(latitud, longitud);
        if (ubicacion == null) {
            ctx.status(404);
            ctx.result("La ubicacion no existe");
        } else {
            ubicacion = ubicacionService.modify(latitud, longitud);
            ctx.json(ubicacion);
        }
    }

    public void eliminar(Context ctx) {
        double latitud = Double.parseDouble(ctx.pathParam("latitud"));
        double longitud = Double.parseDouble(ctx.pathParam("longitud"));
        if (ubicacionService.dbFind(latitud) == null) {
            ctx.status(404);
            ctx.result("La ubicacion no existe");
        } else {
            ubicacionService.delete(latitud, longitud);
            ctx.status(204);
        }
    }

    @Override
    public void applyRoutes() {
        app.routes(() -> {
            path("/ubicaciones", () -> {
                before(ctx -> this.proteger(ctx));
                get(ctx -> this.listar(ctx));
                path("/{latitud}/{longitud}", () -> {
                    get(ctx -> this.listarUno(ctx));
                    post(ctx -> this.modificar(ctx));
                    delete(ctx -> this.eliminar(ctx));
                });
                post(ctx -> this.crear(ctx));
            });
        });
    }
}
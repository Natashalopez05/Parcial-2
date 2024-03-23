package controladores;

import encapsulaciones.Formulario;
import encapsulaciones.Ubicacion;
import io.javalin.Javalin;
import io.javalin.http.Context;
import servicios.FormularioService;
import servicios.UbicacionService;
import util.BaseController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.javalin.apibuilder.ApiBuilder.*;

public class FormularioController extends BaseController {
    private final FormularioService formularioService;
    private final UbicacionService ubicacionService;

    public FormularioController(Javalin app, FormularioService formularioService, UbicacionService ubicacionService) {
        super(app);
        this.formularioService = formularioService;
        this.ubicacionService = ubicacionService;
    }

    public void listar(Context ctx) {
        List<Formulario> formularios = formularioService.findAll();
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("formularios", formularios);
        ctx.render("/public/templates/formulariosLista.html", modelo);
    }

    public void crear(Context ctx) {
        String nombre = ctx.formParam("nombre");
        String apellido = ctx.formParam("apellido");
        String sector = ctx.formParam("sector");
        String educacion = ctx.formParam("educacion");
        String latitudStr = ctx.formParam("latitud");
        String longitudStr = ctx.formParam("longitud");

        if (latitudStr == null || latitudStr.isEmpty() || longitudStr == null || longitudStr.isEmpty()) {
            ctx.status(400);
            ctx.result("Latitud y longitud son requeridos");
            return;
        }

        double latitud = Double.parseDouble(latitudStr);
        double longitud = Double.parseDouble(longitudStr);
        Formulario formularioExistente =formularioService.findByNameAndApellido(nombre, apellido);
        if(formularioExistente != null){
            ctx.status(400);
            ctx.result("El formulario ya existe");
            return;
        }

        Ubicacion ubicacion = new Ubicacion(latitud, longitud);
        ubicacionService.createInDatabase(ubicacion);
        Formulario formulario = new Formulario(nombre, apellido, educacion, sector);
        formulario.setUbicacion(ubicacion);
        formularioService.dbCreate(formulario);

        if(formulario == null || ubicacion == null){
            ctx.status(400);
            ctx.result("Error al crear el formulario y crear ubicacion");
            return;
        }

        ctx.redirect("/formularios/");
    }


    public void eliminar(Context ctx) {
        try {
            Long id = Long.valueOf(ctx.pathParam("id"));
            Formulario formulario = formularioService.dbFind(id);
            if (formulario == null) {
                ctx.status(400);
                ctx.result("El formulario no existe");
            } else {
                formularioService.dbRemove(id);
                ctx.status(201);
                ctx.result("Formulario eliminado");
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
            ctx.result("ID de formulario inválido. Debe ser un número.");
        }
    }

    public void editar(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Formulario formulario = formularioService.dbFind(id);
        if (formulario == null) {
            ctx.status(400);
            ctx.result("El formulario no existe");
        } else {
            formulario.setNombre(ctx.formParam("nombre"));
            formulario.setApellido(ctx.formParam("apellido"));
            formulario.setEducacion(ctx.formParam("educacion"));
            formulario.setSector(ctx.formParam("sector"));
            formularioService.dbModify(formulario);
            ctx.status(201);
            ctx.result("Formulario modificado");
        }
    }

    public void applyRoutes() {
        app.routes(() -> path("/formularios", () -> {
            get("/", this::listar);
            post("/", this::crear);
            post("/edit/{id}", this::editar);
            get("/{id}", this::listar);
            put("/{id}", this::editar);
            delete("/{id}", this::eliminar);
        }));
    }
}
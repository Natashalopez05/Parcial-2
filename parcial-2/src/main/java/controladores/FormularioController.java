package controladores;

import encapsulaciones.Formulario;
import io.javalin.Javalin;
import io.javalin.http.Context;
import servicios.FormularioService;
import util.BaseController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.javalin.apibuilder.ApiBuilder.*;

public class FormularioController extends BaseController {
    private final FormularioService formularioService;

    public FormularioController(Javalin app, FormularioService formularioService) {
        super(app);
        this.formularioService = formularioService;
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
        Formulario formulario = formularioService.dbCreate(new Formulario(nombre, apellido, educacion, sector));
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
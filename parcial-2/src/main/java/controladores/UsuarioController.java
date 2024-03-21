package controladores;

import encapsulaciones.Usuario;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
//import services.CockraochService;
import servicios.UsuarioService;
import util.BaseController;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class  UsuarioController extends BaseController {
    private final UsuarioService usuarioService;


    public UsuarioController(Javalin app, UsuarioService usuarioService) {
        super(app);
        this.usuarioService = usuarioService;
    }

    public void proteger(Context ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario == null || !usuario.isSupervisor())
            ctx.redirect("/auth/login");
    }

    public void listar(Context ctx) {
        String usuario_autoDelete = ctx.sessionAttribute("usuario_autoDelete");
        ctx.sessionAttribute("usuario_autoDelete", null);
        List<Usuario> usuarios = usuarioService.findAll();


        Map<String, Object> modelo = new HashMap<>();
        modelo.put("usuarios", usuarios);
        modelo.put("usuario_autoDelete", usuario_autoDelete);

        ctx.render("/public/templates/usuariosLista.html", modelo);
    }

    public void listarUno(Context ctx) {
        String username = ctx.pathParam("username");
        Usuario usuario = usuarioService.find(username);

        Map<String, Object> modelo = setModelo(
                "usuario", usuario);

        ctx.render("/public/templates/usuariosLista.html", modelo);
    }

    public void crear(Context ctx) {
        String username = ctx.formParam("username");
        if (usuarioService.find(username) != null) {
            ctx.status(400);
            ctx.result("El usuario ya existe");
            return;
        }

        String nombre = ctx.formParam("nombre");
        String password = ctx.formParam("password");
        boolean supervisor = ctx.formParam("supervisor") != null;
        boolean encuestador = ctx.formParam("encuestador") != null;


        usuarioService.create(username, nombre, password, supervisor, encuestador);
        ctx.redirect("/registrar/" + username);
    }

    public void editar(Context ctx) {
        String username = ctx.pathParam("username");
        String nombre = ctx.formParam("nombre");
        String password = ctx.formParam("password");
        boolean supervisor = ctx.formParam("supervisor") != null;
        boolean encuestador = ctx.formParam("encuestador") != null;

        usuarioService.modify(username, nombre, password, supervisor, encuestador);
        ctx.redirect("/registrar/" + username);
    }

    public void eliminar(Context ctx) {
        String username = ctx.pathParam("username");
        Usuario usuarioLogueado = ctx.sessionAttribute("usuario");

        assert usuarioLogueado != null;
        if(usuarioLogueado.getUsername().equals(username)) {
            ctx.sessionAttribute("usuario_autoDelete", "No puedes eliminarte a ti mismo.");
            ctx.redirect("/registrar");
            return;
        }

        usuarioService.desactivar(username);
        ctx.redirect("/registrar");
    }

    @Override
    public void applyRoutes() {
        app.routes(() -> path("/registrar", () -> {
            before("/", this::proteger);
            get("/", this::listar);
            post("/", this::crear);
            post("/edit/{username}", this::editar);
            before("/{username}", this::proteger);
            get("/{username}", this::listar);
            put("/{id}", this::editar);
            delete("/{username}", this::eliminar);
        }));
    }
}

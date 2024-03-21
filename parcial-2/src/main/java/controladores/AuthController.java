package controladores;
import encapsulaciones.Usuario;
import io.javalin.Javalin;
import io.javalin.http.Context;
import servicios.AuthService;
import servicios.CockraochService;
import servicios.UsuarioService;
import util.BaseController;
import static io.javalin.apibuilder.ApiBuilder.*;

public class AuthController extends BaseController {
    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final CockraochService cockraochService;

    public AuthController(Javalin app, UsuarioService usuarioService, AuthService authService, CockraochService cockraochService) {
        super(app);
        this.usuarioService = usuarioService;
        this.authService = authService;
        this.cockraochService = cockraochService;
    }

    public void login(Context ctx) {
        String usuarioEncryptado = ctx.cookie("usuario");
        if (usuarioEncryptado == null) {
            ctx.render("/public/templates/login.html");
            return;
        }

        String username = authService.decryptText(usuarioEncryptado);
        Usuario usuarioEnCookie = usuarioService.findByUsername(username);
        ctx.sessionAttribute("usuario", usuarioEnCookie);
        cockraochService.addLog(usuarioEnCookie.getUsername());

        ctx.redirect("/");
    }

    public void checkLogin(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        boolean remember = ctx.formParam("remember") != null;
        Usuario usuario = usuarioService.findByUsername(username);

        if(!usuarioService.checkPassword(username, password) || usuario == null) {
            ctx.redirect("/auth/login");
            return;
        }

        if(!usuario.isActive()){
            ctx.sessionAttribute("usuario_inactivo", "Usuario inactivo, favor contactar al administrador.");
            ctx.redirect("/auth/login");
            return;
        }


        ctx.sessionAttribute("usuario", usuario);
        if(remember)
            ctx.cookie("usuario", authService.encryptText(usuario.getUsername()), 604800);
        cockraochService.addLog(username);

        ctx.redirect("/");
    }

    public void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.removeCookie("usuario");
        ctx.redirect("/");
    }

    public void register(Context ctx) {
        System.out.println("Registrando usuario");
    }

    public void proteger(Context ctx) {
        Usuario usuario = ctx.sessionAttribute("usuario");
        if (usuario != null)
            ctx.redirect("/");
    }

    @Override
    public void applyRoutes() {
        app.routes(() -> path("/auth", () -> {
            before("/login", this::proteger);
            get("/login", this::login);
            post("/login", this::checkLogin);
            get("/logout", this::logout);
            post("/register", this::register);
        }));
    }

}
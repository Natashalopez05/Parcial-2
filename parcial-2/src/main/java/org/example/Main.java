package org.example;

import controladores.*;
import encapsulaciones.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import org.postgresql.ds.PGSimpleDataSource;
import servicios.*;

import java.util.Date;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config ->{
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;
            });
            config.plugins.enableCors(corsContainer -> corsContainer.add(CorsPluginConfig::anyHost));
        }).start(3000);

        BootstrapService bootstrapService = new BootstrapService();
        bootstrapService.startDb();
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        PGSimpleDataSource ds = new PGSimpleDataSource();

        UsuarioService usuarioService = new UsuarioService();
        AuthService authService = new AuthService(textEncryptor);
        CockraochService cockraochService = new CockraochService(ds);
        cockraochService.init();

        //------------------------------------CREATION------------------------------//
        addInfo(usuarioService);
        //--------------------------------------------------------------------------//

        new UsuarioController(app, usuarioService).applyRoutes();
        new AuthController(app, usuarioService, authService, cockraochService).applyRoutes();

        app.get("/", ctx -> ctx.redirect("/index.html"));
    }

    public static void addInfo(UsuarioService usuarioService) {

        //Supervisor
        Usuario usuario = usuarioService.create("admin", "Administrador", "admin", true, false);
        //Encuestador
        Usuario usuario1 = usuarioService.create("vladi", "Vladimir", "1234", false, true);

    }

}
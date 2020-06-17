import Encapsulación.Usuario;
import Visual.ControladorPlantilla;
import Visual.ControladorSesion;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.before;

public class Main {


    public Usuario admin = new Usuario("admin", "admin", "admin");

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/HTML");
        }).start(7000);

        new ControladorPlantilla().Rutas(app);
        //Login
        new ControladorSesion().control(app);

        app.post("agregarProduct", ctx -> {

        });
    }

}

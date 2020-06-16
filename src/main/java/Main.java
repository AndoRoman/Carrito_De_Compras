import Visual.ControladorPlantilla;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/HTML");
        }).start(7000);


        new ControladorPlantilla().Rutas(app);

    }

}

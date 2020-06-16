package Visual;

import Encapsulación.Producto;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;


public class ControladorPlantilla {
    public ControladorPlantilla() {

    }


    public void Rutas(Javalin app) {
        app.routes(() -> {
            path("/", () -> {
                get("/", ctx -> {
                    List<Producto> listaProductos = getProductos();

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Productos");
                    modelo.put("listaProducto", listaProductos);

                    //
                    ctx.render("/HTML/Productos.html", modelo);
                });
            });
        });
    }

    private List<Producto> getProductos() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto(000, "Lata de Maiz", new BigDecimal("50")));
        lista.add(new Producto(001, "Lata de Salsa", new BigDecimal("75")));
        lista.add(new Producto(002, "Espaguetis", new BigDecimal("30")));
        return lista;
    }
}

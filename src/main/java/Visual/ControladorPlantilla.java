package Visual;
import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Encapsulación.VentasProductos;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;


public class ControladorPlantilla {

    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();

    public ControladorPlantilla() {
        registrarPlantilla();
    }

    public void registrarPlantilla() {
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    public void Rutas(Javalin app) {
        app.routes(() -> {
            path("/", () -> {
                get("/", ctx -> {
                    List<Producto> listaProductos = getProductos();
                    CarroCompra aux = new CarroCompra(0, null, 0);
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Productos");
                    modelo.put("listaProducto", listaProductos);
                    modelo.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    ctx.render("/HTML/Productos.html", modelo);
                });
            });

            path("/ListCompras.html", ()-> {
                get("/", ctx -> {
                    List<VentasProductos> listaVentas = getVentas();
                    CarroCompra aux = new CarroCompra(0, null, 0);
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    view.put("ventasProductos", listaVentas);
                    view.put("listaProductos", listaVentas.get(1).getListaProductos());
                    ctx.render("/HTML/ListCompras.html", view);
                });
            });
        });
    }

    private List<Producto> getProductos() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto(000, "Lata de Maiz", new BigDecimal("50")));
        lista.add(new Producto(001, "Lata de Salsa", new BigDecimal("75")));
        lista.add(new Producto(002, "Espaguetis", new BigDecimal("30")));
        lista.addAll(servicio.getListProduct());
        return lista;
    }

    private List<VentasProductos> getVentas(){
        List<VentasProductos> lista = new ArrayList<>();
        lista.add(new VentasProductos(000, Date.from(Instant.now()), servicio.getListaUsuarios().get(0).getNombre(), servicio.getListProduct(),2));
        lista.add(new VentasProductos(001, Date.from(Instant.now()), servicio.getListaUsuarios().get(1).getNombre(), servicio.getListProduct(),4));
        return lista;
    }
}

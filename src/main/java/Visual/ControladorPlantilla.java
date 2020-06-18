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

            //HOME
            path("/", () -> {
                get("/", ctx -> {
                    List<Producto> listaProductos = getProductos();
                    CarroCompra aux = servicio.getCarro();
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Bienvenido");
                    modelo.put("listaProducto", listaProductos);
                    modelo.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    try{
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            modelo.put("admin", "Lista de Compras Realizadas");
                            modelo.put("adminProduct", "Gestion de Productos");
                        }
                    }catch(Exception e){

                    }
                    ctx.render("/HTML/Productos.html", modelo);
                });


                app.post("agregarProduct", ctx -> {

                });
            });
            //VISTA DEL ADMINISTRADOR
            path("/ListCompras.html", ()-> {
                get("/", ctx -> {
                    List<VentasProductos> listaVentas = getVentas();
                    CarroCompra aux = servicio.getCarro();
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    view.put("ventasProductos", listaVentas);
                    view.put("listaProductos", listaVentas.get(0).getListaProductos());
                    try{
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            view.put("admin", "Lista de Compras Realizadas");
                            view.put("adminProduct", "Gestion de Productos");
                        }
                    }catch(Exception e){

                    }
                    ctx.render("/HTML/ListCompras.html", view);
                });
            });


            //VISTA DE MODIFICACION DE PRODUCTOS
            path("/HTML/Gestor.html", ()-> {
                get("/", ctx -> {
                    CarroCompra aux = servicio.getCarro();
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    try{
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            view.put("admin", "Lista de Compras Realizadas");
                            view.put("adminProduct", "Gestion de Productos");
                            view.put("listaProductos", servicio.getListProduct());
                        }
                    }catch(Exception e){

                    }
                    ctx.render("/HTML/Gestor.html", view);
                });
            });
            /*
            //VISTA DE CARRITO
            path("/ListCompras.html", ()-> {
                get("/", ctx -> {
                    List<VentasProductos> listaVentas = getVentas();
                    CarroCompra aux = servicio.getCarro();
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    view.put("ventasProductos", listaVentas);
                    view.put("listaProductos", listaVentas.get(0).getListaProductos());
                    try{
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            view.put("admin", "Lista de Compras Realizadas");
                            view.put("adminProduct", "Gestion de Productos");
                        }
                    }catch(Exception e){

                    }
                    ctx.render("/HTML/ListCompras.html", view);
                });
            });*/

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
        //lista.add(new VentasProductos(001, Date.from(Instant.now()), servicio.getListaUsuarios().get(1).getNombre(), servicio.getListProduct(),4));
        return lista;
    }
}

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
                    //Dando identidad al usuario
                    if(ctx.sessionAttribute("usuario") == null){
                        ctx.sessionAttribute("usuario",ctx.cookie("JSESSIONID"));

                    }
                    List<Producto> listaProductos = getProductos();
                    //Carrito especial para el usuario
                    CarroCompra aux = servicio.getCarro(ctx.sessionAttribute("usuario"));
                    String cant = String.valueOf(aux.getCantidad());
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Bienvenido");
                    modelo.put("listaProducto", listaProductos);
                    modelo.put("item", "Carrito de Compras(" + cant + ")");
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
                    String NombreProducto = ctx.formParam("name");
                    //INTANCIA
                    new ControladorCarrito().AgregarAlCarro(NombreProducto, ctx.sessionAttribute("usuario"));
                    ctx.result("El producto ha sido anadido al carrito");
                });
            });
            //VISTA DEL ADMINISTRADOR
            path("/ListCompras.html", ()-> {
                get("/", ctx -> {
                    //Dando identidad al usuario
                    if(ctx.sessionAttribute("usuario") == null){
                        ctx.sessionAttribute("usuario",ctx.cookie("JSESSIONID"));

                    }
                    List<VentasProductos> listaVentas = getVentas();
                    CarroCompra aux = servicio.getCarro(ctx.sessionAttribute("usuario"));
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
                    //Dando identidad al usuario
                    if(ctx.sessionAttribute("usuario") == null){
                        ctx.sessionAttribute("usuario",ctx.cookie("JSESSIONID"));

                    }
                    CarroCompra aux = servicio.getCarro(ctx.sessionAttribute("usuario"));
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                        view.put("admin", "Lista de Compras Realizadas");
                        view.put("adminProduct", "Gestion de Productos");
                        view.put("listaProductos", servicio.getListProduct());
                    }
                    ctx.render("/HTML/Gestor.html", view);
                });
            });

            //VISTA DE CARRITO
            path("/Carrito.html", ()-> {
                get("/", ctx -> {
                    //Dando identidad al usuario
                    if(ctx.sessionAttribute("usuario") == null){
                        ctx.sessionAttribute("usuario",ctx.cookie("JSESSIONID"));

                    }
                    CarroCompra aux = servicio.getCarro(ctx.sessionAttribute("usuario"));
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    view.put("listaProductos", aux.getListaProductos());
                    view.put("total", "Total a Pagar: " + monto(aux.getListaProductos(), aux.getCantidad()) + "($RD)");
                    view.put("monto", aux.getCantidad()*aux.getListaProductos().get(0).getPrecio().intValue());
                    try{
                        view.put("user", "Carrito de: " +ctx.sessionAttribute("usuario"));
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            view.put("admin", "Lista de Compras Realizadas");
                            view.put("adminProduct", "Gestion de Productos");
                        }
                    }catch(Exception e){
                        view.put("user", "Tu Carrito de Compras");
                    }
                    ctx.render("/HTML/Carrito.html", view);
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
        //lista.add(new VentasProductos(001, Date.from(Instant.now()), servicio.getListaUsuarios().get(1).getNombre(), servicio.getListProduct(),4));
        return lista;
    }

    private double monto(List<Producto> P, int cant){
        double total = 0;
        for (Producto i: P){
            total = total + i.getPrecio().floatValue()*cant;
        }
        return total;
    }
}

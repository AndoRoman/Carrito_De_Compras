package Visual;
import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Encapsulación.VentasProductos;
import Servicios.BaseDatos;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;


public class ControladorPlantilla {

    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    ControladorCarrito servicioCarrito = ControladorCarrito.getInstancia();

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
                        ctx.cookie("usuario",ctx.cookie("JSESSIONID"));
                        ctx.sessionAttribute("usuario" ,ctx.cookie("JSESSIONID"));
                    }
                    List<Producto> listaProductos = BaseDatos.getInstancia().getProductosBD();
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
                            modelo.put("OUT", "Cerrar Session");
                        }
                    }catch(Exception e){

                    }
                    ctx.render("/HTML/Productos.html", modelo);
                });


                app.post("agregarProduct", ctx -> {
                    String NombreProducto = ctx.formParam("name");
                    //INSTANCIA
                    servicioCarrito.AgregarAlCarro(NombreProducto, ctx.sessionAttribute("usuario"));
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
                    //TOMANDO LISTA DE VENTAS DE LA BASE DE DATOS
                    servicio.setListVentas(BaseDatos.getInstancia().getVentaBD());
                    ///
                    CarroCompra aux = servicio.getCarro(ctx.sessionAttribute("usuario"));
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    view.put("ventasProductos", BaseDatos.getInstancia().getVentaBD());
                    try{
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            view.put("admin", "Lista de Compras Realizadas");
                            view.put("adminProduct", "Gestion de Productos");
                            view.put("OUT", "Cerrar Session");
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
                        view.put("OUT", "Cerrar Session");
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
                        ctx.cookie("usuario",ctx.cookie("JSESSIONID"));
                        ctx.sessionAttribute("usuario" ,ctx.cookie("JSESSIONID"));
                    }
                    CarroCompra aux = servicio.getCarro(ctx.sessionAttribute("usuario"));
                    Map<String, Object> view = new HashMap<>();
                    view.put("item", "Carrito de Compras(" + aux.getCantidad() + ")");
                    view.put("listaProductos", aux.getListaProductos());
                    view.put("total", "Total a Pagar: " + monto(aux.getListaProductos()) + "($RD)");
                    try {
                        view.put("user", "Carrito de: " +ctx.sessionAttribute("usuario"));
                        if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                            view.put("admin", "Lista de Compras Realizadas");
                            view.put("adminProduct", "Gestion de Productos");
                            view.put("OUT", "Cerrar Session");
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

        lista.addAll(servicio.getListProduct());
        return lista;
    }

    private double monto(List<Producto> P){
        double total = 0;
        for (Producto i: P){
            total = total + i.getPrecio().floatValue();
        }
        return total;
    }
}

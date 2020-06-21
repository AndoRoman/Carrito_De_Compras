package Visual;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Encapsulación.VentasProductos;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorCarrito {
    private static ControladorCarrito instancia;
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();

    public void ManejadorCarro(Javalin app){
        //Para Eliminar del carro
        app.post("/sacar",  ctx ->{
            String product = ctx.formParam("nombre");
            String usuario = ctx.sessionAttribute("usuario");
            SacarDelCarro(product, usuario);
            ctx.redirect("/Carrito.html");
        });
        app.get("/comprar", ctx -> {
            String user = ctx.sessionAttribute("usuario");
            System.out.println(user);
            CrearCompra(user);
            Map<String, Object> view = new HashMap<>();
            VentasProductos aux = servicio.getListVentas().get(servicio.getListVentas().size() - 1);
            view.put("listaProductos", aux.getListaProductos());
            view.put("total", "Total Pagado: " + monto(aux.getListaProductos()) + "($RD)    " + "     Realizada el: " + Date.from(Instant.now()));
            try{
                view.put("user", "Carrito de: " +ctx.sessionAttribute("usuario"));
                if(ctx.sessionAttribute("usuario").toString().matches("admin")) {
                    view.put("admin", "Lista de Compras Realizadas");
                    view.put("adminProduct", "Gestion de Productos");
                }
            }catch(Exception e){
                view.put("user", "Tu Carrito de Compras");
            }
            ctx.render("/HTML/CompraDONE.html", view);
        });


    }

    private double monto(List<Producto> P){
        double total = 0;
        for (Producto i: P){
            total = total + i.getPrecio().floatValue();
        }
        return total;
    }


    public static ControladorCarrito getInstancia(){
        if(instancia==null){
            instancia = new ControladorCarrito();
        }
        return instancia;
    }

    private void CrearCompra(String user) {
        CarroCompra i = null;
        for (CarroCompra carroComprado: servicio.getListCarros()) {
            if (carroComprado.getUser().matches(user)){
                i = carroComprado;
                servicio.getListCarros().remove(carroComprado);
                break;
            }
        }
        servicio.setVentas(new VentasProductos(servicio.getIdVentas(), Date.from(Instant.now()), i.getUser(), i.getListaProductos(), 1));
    }

    private void SacarDelCarro(String nombreProducto, String usuario){
        int index = 0;
            //BUSCO EL CARRITO QUE LE PERTENECE A ESE USUARIO
        for (CarroCompra i : servicio.getListCarros()) {
                if(i.getUser().matches(usuario)){
                    index = servicio.getListCarros().indexOf(i);
                    break;
                }
            }
            //BUSCO EL PRODUCTO
        for (Producto rechazado: servicio.getListCarros().get(index).getListaProductos()) {
            if(rechazado.getNombre().matches(nombreProducto)){
                servicio.getListCarros().get(index).getListaProductos().remove(rechazado);
                break;
            }
        }
    }

    public void AgregarAlCarro(String nombreProducto, String user){
        int indexUser = 0, indexProduct = 0;
        Producto nuevoProducto;

        //PRIMERO BUSCO EL CARRITO QUE LE PERTENECE A ESE USUARIO
        for (CarroCompra i : servicio.getListCarros()) {
            if(i.getUser().matches(user)){
                indexUser = servicio.getListCarros().indexOf(i);
                break;
            }
        }
        //SEGUNDO BUSCO EL PRODUCTO QUE SE QUIERE AGREGAR AL CARRITO
        for (Producto j: servicio.getListProduct()) {
            if (j.getNombre().matches(nombreProducto)){
                indexProduct = servicio.getListProduct().indexOf(j);
                break;
            }
        }
        //CLONO EL PRODUCTO QUE SE QUIERE AGREGAR
        nuevoProducto = servicio.getListProduct().get(indexProduct);
        //LO AGREGO AL CARRITO
        servicio.getListCarros().get(indexUser).getListaProductos().add(nuevoProducto);
    }
}

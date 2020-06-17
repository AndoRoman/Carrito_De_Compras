package Servicios;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import io.javalin.Javalin;
import java.math.BigDecimal;


public class GestorProductos {
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();

    private GestorProductos(Javalin app){
        app.post("agregar", ctx -> {
            String producto = ctx.formParam("NombreProducto");
            String precio = ctx.formParam("precioProducto");

            if(servicio.getListProduct().stream().spliterator().toString().matches(producto)){
                modificarProducto(producto, precio);
            }else{
                agregarProduct(producto, precio);
            }
            ctx.redirect("/HTTP/Productos.html", 200);
        });

        app.post("eliminar", ctx -> {
            String producto = ctx.formParam("NombreProducto");
            eliminar(producto);
            ctx.redirect("/HTTP/Productos.html", 200);
        });


    }
    public void agregarProduct(String producto, String precio){
        Producto aux = new Producto(servicio.getIdProduct(), producto, new BigDecimal(precio));
        servicio.getListProduct().add(aux);
    }
    public void eliminar(String producto){
        for (int i = 0; i <servicio.getListProduct().size() ; i++) {
            if (servicio.getListProduct().get(i).getNombre().matches(producto)){
                servicio.getListProduct().remove(i);
                break;
            }
        }

    }
    public void modificarProducto(String producto, String precio){
        for (int i = 0; i <servicio.getListProduct().size() ; i++) {
            if (servicio.getListProduct().get(i).getNombre().matches(producto)){
                servicio.getListProduct().get(i).setPrecio(new BigDecimal(precio));
                break;
            }
        }
    }
}

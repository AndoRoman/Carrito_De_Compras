package Servicios;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import io.javalin.Javalin;
import java.math.BigDecimal;
import java.util.List;


public class GestorProductos {
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();

    public GestorProductos(Javalin app){
        app.post("/HTML/Gestor.html", ctx -> {
            String producto = ctx.formParam("NombreProducto");
            String precio = ctx.formParam("precioProducto");
            boolean toke = false;
            for (Producto aux: servicio.getListProduct()) {
                if(aux.getNombre().matches(producto)) {
                    modificarProducto(producto, precio);
                    toke=true;
                }
            }
            if(!toke){
                agregarProduct(producto, precio);
            }
            ctx.redirect("/");
        });

        app.post("/delete", ctx -> {
            String producto = ctx.formParam("NombreProducto");
            eliminar(producto);
            ctx.redirect("/");
        });


    }
    public void agregarProduct(String producto, String precio){
        Producto aux = new Producto(servicio.getIdProduct(), producto, new BigDecimal(precio));
        servicio.getListProduct().add(aux);
    }
    public void eliminar(String producto){
        for (Producto i : servicio.getListProduct()) {
            if(i.getNombre().matches(producto)){
                servicio.getListProduct().remove(i);
            }
        }

    }
    public void modificarProducto(String producto, String precio){
        for (Producto i : servicio.getListProduct()) {
            if(i.getNombre().matches(producto)){
                i.setPrecio(new BigDecimal(precio));
            }
        }
    }
}

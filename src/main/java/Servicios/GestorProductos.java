package Servicios;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import io.javalin.Javalin;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GestorProductos {
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();

    public GestorProductos(Javalin app){
        app.post("/HTML/Gestor.html", ctx -> {
            String producto = ctx.formParam("NombreProducto");
            String precio = ctx.formParam("precioProducto");
            boolean toke = false;
            for (Producto aux: servicio.getListProduct()) {
                if(aux.getNombre().matches(producto)) {
                    System.out.println("El Producto: " + producto + " Ha sido Modificado: " + modificarProducto(producto, precio));
                    toke=true;
                }
            }
            if(!toke){
                System.out.println("El Producto: " + producto + " Ha sido Agregado: " + agregarProduct(producto, precio));
            }
            ctx.redirect("/");
        });

        app.post("/delete", ctx -> {
            String producto = ctx.formParam("NombreProductoEliminar");
            System.out.println("Producto: "+ producto + " Ha sido Eliminado: "+eliminar(producto));
            ctx.redirect("/");
        });


    }
    public boolean agregarProduct(String producto, String precio){
        Producto aux = new Producto(servicio.getListProduct().size() + 1, producto, new BigDecimal(precio), 1);
        servicio.getListProduct().add(aux);

        //Agregando a la BASE DE DATOS
        boolean ok =false;

        Connection con = null;
        try {

            String query = "INSERT INTO Productos(id, nombre, precio, cantidad) values(?,?,?,?)";
            con = BaseDatos.getInstancia().Conexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, aux.getId());
            prepareStatement.setString(2, aux.getNombre());
            prepareStatement.setString(3, aux.getPrecio().toString());
            prepareStatement.setInt(4, 1);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;


    }
    public boolean eliminar(String producto){
        int index = 0;
        for (Producto i : servicio.getListProduct()) {
            if (i.getNombre().matches(producto)) {
                index = servicio.getListProduct().indexOf(i);
            }
        }
        boolean ok =false;

        //ELIMINANDOLO DE FORMA PERSISTENTE
        Connection con = null;
        try {

            String query = "DELETE FROM Productos where id = ?";
            con = BaseDatos.getInstancia().Conexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, index);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        servicio.getListProduct().remove(index);
        return ok;
    }
    public boolean modificarProducto(String producto, String precio){
        int index = 0;
        for (Producto i : servicio.getListProduct()) {
            if(i.getNombre().matches(producto)){
                i.setPrecio(new BigDecimal(precio));
                index = servicio.getListProduct().indexOf(i);
            }
        }

        //MODIFICANDOLO DE FORMA PERSISTENTE
        boolean ok =false;

        Connection con = null;
        try {

            String query = "UPDATE Productos set precio = ? where id = ?";
            con = BaseDatos.getInstancia().Conexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            prepareStatement.setString(1, precio);
            //Indica el where...
            prepareStatement.setInt(2, index);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(GestorProductos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }
}

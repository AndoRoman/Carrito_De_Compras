import Servicios.BaseDatos;
import Servicios.GestorProductos;
import Visual.ControladorCarrito;
import Visual.ControladorPlantilla;
import Visual.ControladorSesion;
import io.javalin.Javalin;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/HTML");
            config.addStaticFiles("/JS");
            config.addStaticFiles("/CSS");
            config.addStaticFiles("/img");
        }).start(7000);
        //PRIMERO se enciende la base de datos
        BaseDatos.getInstancia().arrancarBD();
        //Prueba
        BaseDatos.getInstancia().PRUEBA();
        //Creando Tablas
        BaseDatos.getInstancia().TABLESBD();
        //Insertando
        System.out.println("Usuario Admin Agregado? :" + BaseDatos.getInstancia().INSERT_PORDEFECTO());
        //STOP
        //BaseDatos.getInstancia().PararBD();
        //Plantillas iniciales
        new ControladorPlantilla().Rutas(app);
        //Login
        new ControladorSesion().control(app);
        //Gestor
        new GestorProductos(app);
        //Manejador del Carrito
        new ControladorCarrito().ManejadorCarro(app);

    }

}

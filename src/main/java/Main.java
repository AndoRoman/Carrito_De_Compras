import Encapsulación.Usuario;
import Servicios.BaseDatos;
import Servicios.GestorProductos;
import Visual.ControladorCarrito;
import Visual.ControladorPlantilla;
import Visual.ControladorSesion;
import io.javalin.Javalin;
import org.sql2o.Sql2o;

import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/HTML");
            config.addStaticFiles("/JS");
            config.addStaticFiles("/CSS");
            config.addStaticFiles("/img");
        }).start(7000);
        //PRIMERO se enciende la base de datos
        BaseDatos.getInstancia().arrancarBD();
        //Prueba de conexión
        BaseDatos.getInstancia().PararBD();
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

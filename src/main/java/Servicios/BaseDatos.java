package Servicios;

import Encapsulación.Usuario;
import org.h2.engine.Database;
import org.h2.tools.Script;
import org.h2.tools.Server;
import org.sql2o.Sql2o;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class BaseDatos {

    private static BaseDatos instancia;
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    private  Server tcp;
    private Sql2o sql2o;
    private Connection conn = null;
    private Statement statement;

    private BaseDatos(){

        //REGISTANDO DRIVER
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {

        }
    }

    public void arrancarBD() throws SQLException {
        try {
            tcp = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon").start();
            sql2o = new Sql2o("jdbc:h2:tcp://localhost/~/DB_A_La_Antiagua", "sa", "");
            //CONEXION
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/DB_A_La_Antigua", "sa", "");
            //CREANDO TABLAS & Usuario y Producto por Defecto
            String CreateTABLAUSUARIOS = "CREATE TABLE IF NOT EXISTS Usuarios\n " +
                    "(usuario VARCHAR(20) PRIMARY KEY NOT NULL,\n" +
                    "nombre VARCHAR(30) NOT NULL,\n" +
                    "password VARCHAR(20) NOT NULL,\n" +
                    ");";
            String CreateUSUARIOS = "INSERT INTO Usuario (usuario, nombre, password)" +
                    "VALUES (admin, admin, admin)";

            String CreateTABLAProductos = "CREATE TABLE IF NOT EXISTS Productos\n" +
                    "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                    "nombre VARCHAR(30) NOT NULL,\n" +
                    "precio DECIMAL(10) NOT NULL,\n" +
                    ");";

            String CreateProductos = "INSERT INTO Productos (nombre, precio)" +
                    "VALUES (TICKET, 0)";
            

            String CreateVentas = "CREATE TABLE IF NOT EXISTS VentasProductos\n" +
                    "(id INT NOT NULL AUTO_INCREMENT,\n" +
                    "fechaCompra DATE NOT NULL,\n"+
                    "nombreCliente VARCHAR(20) NOT NULL,\n" +
                    "PRIMARY KEY (id)"+
                    "FOREIGN KEY (nombreCliente) REFERENCES Usuarios(usuario)"+
                    "cantidad INT NOT NULL,\n" +
                    ");";
            String CreateVentas_Productos = "CREATE TABLE IF NOT EXISTS Ventas_ListProductos\n" +
                    "(id_Ventas INT NOT NULL,\n" +
                    "id_Producto INT NOT NULL,\n"+
                    "FOREIGN KEY (id_Ventas) REFERENCES VentasProductos(id)"+
                    "FOREIGN KEY (id_Producto) REFERENCES Producto(id)" +
                     ");";

            //ESTABLECIENDO EN BASE DE DATOS
            statement = conn.createStatement();
            statement.execute(CreateTABLAUSUARIOS);
            statement.execute(CreateUSUARIOS);
            statement.execute(CreateTABLAProductos);
            statement.execute(CreateProductos);
            statement.execute(CreateVentas);
            statement.execute(CreateVentas_Productos);
            //LIMPIANDO
            statement.close();
            conn.close();
        }catch (SQLException e){
            //error de JDBC
            e.printStackTrace();
        }



    }


    public void PararBD() throws SQLException {
        //Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
        System.out.println("Conexión Realizada con Exito!!!!!");
        tcp.stop();
    }

    public static Connection Conexion() throws SQLException {
        Connection result = null;
        result = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/DB_A_La_Antigua","sa", "");
        return result;
    }
/*
    public void PRUEBA() throws SQLException {
        Conexion().close();
        System.out.println("Conexión Realizada con Exito!!!!!");
    }
*/
    public static BaseDatos getInstancia(){
        if(instancia==null){
            instancia = new BaseDatos();
        }
        return instancia;
    }
}

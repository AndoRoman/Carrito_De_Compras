package Servicios;
import org.h2.tools.Server;
import org.sql2o.Sql2o;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BaseDatos {

    private static BaseDatos instancia;
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    private Sql2o sql2o;
    private Statement statement;

    private BaseDatos(){
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public void arrancarBD() throws SQLException {
        try {
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-tcpDaemon").start();

        }catch (SQLException e){
            //error de JDBC
            e.printStackTrace();
        }

    }

    public void TABLESBD() throws SQLException {

        //sql2o = new Sql2o("jdbc:h2:tcp://localhost/~/DB_A_La_Antigua", "sa", "");
        //CONEXION
        Connection conn = Conexion();


        //CREANDO TABLAS & Usuario y Producto por Defecto
        String CreateTABLAUSUARIOS = "CREATE TABLE IF NOT EXISTS Usuarios\n " +
                "(\n" +
                " usuario VARCHAR(20) PRIMARY KEY NOT NULL,\n" +
                " nombre VARCHAR(30) NOT NULL,\n" +
                " password VARCHAR(20) NOT NULL,\n" +
                ");";
        //String CreateUSUARIOS = "INSERT INTO Usuarios (usuario, nombre, password) VALUES (admin, admin, admin)";
        /*
        if(INSERT()){
            System.out.println("Admin agregado!!");
        }*/

        String CreateTABLAProductos = "CREATE TABLE IF NOT EXISTS Productos\n" +
                "(id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,\n" +
                "nombre VARCHAR(30) NOT NULL,\n" +
                "precio DECIMAL(10) NOT NULL,\n" +
                ");";

       //String CreateProductos = "INSERT INTO Productos (nombre, precio) VALUES (TICKET, 0)";


        String CreateVentas = "CREATE TABLE IF NOT EXISTS VentasProductos\n" +
                "(id INT NOT NULL IDENTITY(1, 1) PRIMARY KEY,\n" +
                "fechaCompra VARCHAR(30) NOT NULL,\n"+
                "nombreCliente VARCHAR(20) NOT NULL,\n" +
                "cantidad INT NOT NULL,\n" +
                "FOREIGN KEY (nombreCliente) REFERENCES Usuarios(usuario),\n"+
                ");";
        String CreateVentas_Productos = "CREATE TABLE IF NOT EXISTS Ventas_ListProductos\n" +
                "(id_Ventas INT NOT NULL,\n" +
                "id_Producto INT NOT NULL,\n"+
                "FOREIGN KEY (id_Ventas) REFERENCES VentasProductos(id),"+
                "FOREIGN KEY (id_Producto) REFERENCES Productos(id)" +
                ");";

        //ESTABLECIENDO EN BASE DE DATOS


        statement = conn.createStatement();
        statement.execute(CreateTABLAUSUARIOS);
        //statement.execute(CreateUSUARIOS);
        statement.execute(CreateTABLAProductos);
        //statement.executeUpdate(CreateProductos);
        statement.execute(CreateVentas);


        //COMPROBACIÓN
        boolean test = statement.execute(CreateVentas_Productos);
        if (test)
            System.out.println("Tablas Creadas!");
        //LIMPIANDO
        statement.close();
        conn.close();
    }

    public boolean INSERT() {
        Connection con = null;
        boolean ok = false;
        try {

            String query = "INSERT INTO Usuarios (usuario, nombre, password) values(?,?,?)";
            con = Conexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, "admin");
            prepareStatement.setString(2, "admin");
            prepareStatement.setString(3, "admin");

            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ok;
    }


    public void PararBD() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
        System.out.println("Conexión Realizada con Exito!!!!!");
    }

    public static Connection Conexion() throws SQLException {
        Connection result = null;
        result = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/DB_A_La_Antigua","sa", "");
        return result;
    }

    public void PRUEBA() throws SQLException {
        Conexion().close();
        System.out.println("Conexión Realizada con Exito!!!!!");
    }

    public static BaseDatos getInstancia() {
        if(instancia==null){
            instancia = new BaseDatos();
        }
        return instancia;
    }
}

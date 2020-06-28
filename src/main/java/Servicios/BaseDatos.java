package Servicios;
import Encapsulación.Producto;
import Encapsulación.VentasProductos;
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


    public void PararBD() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
        System.out.println("Conexión Realizada con Exito!!!!!");
    }

    public static Connection Conexion() throws SQLException {
        Connection result = null;
        result = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/DB_A_La_Antigua","sa", "sa");
        return result;
    }

    public void PRUEBA() throws SQLException {
        Conexion().close();
        System.out.println("Conexión Realizada con Exito!!!!!");
    }


    //CREADOR DE TABLAS

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
        //String CreateUSUARIOS = "INSERT_PORDEFECTO INTO Usuarios (usuario, nombre, password) VALUES (admin, admin, admin)";


        String CreateTABLAProductos = "CREATE TABLE IF NOT EXISTS Productos\n" +
                "(id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,\n" +
                "nombre VARCHAR(30) NOT NULL,\n" +
                "precio DECIMAL(10) NOT NULL,\n" +
                ");";

       //String CreateProductos = "INSERT_PORDEFECTO INTO Productos (nombre, precio) VALUES (TICKET, 0)";


        String CreateVentas = "CREATE TABLE IF NOT EXISTS VentasProductos\n" +
                "(id INT NOT NULL IDENTITY(1, 1) PRIMARY KEY,\n" +
                "fechaCompra VARCHAR(30) NOT NULL,\n"+
                "nombreCliente VARCHAR(20) NOT NULL,\n" +
                "cantidad INT NOT NULL,\n" +
                "FOREIGN KEY (nombreCliente) REFERENCES Usuarios(usuario),\n"+
                ");";
        String CreateVentas_Productos = "CREATE TABLE IF NOT EXISTS Ventas_ListProductos\n" +
                "(id_Ventas INT NOT NULL IDENTITY(1, 1),\n" +
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

    public boolean INSERT_PORDEFECTO() {
        Connection con = null;
        boolean ok = false;
        try {
            //USUARIO ADMIN
            String query = "INSERT INTO Usuarios (usuario, nombre, password) values(?,?,?)";
            con = Conexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, "admin");
            prepareStatement.setString(2, "admin");
            prepareStatement.setString(3, "admin");
            int fila = prepareStatement.executeUpdate();

            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }


        try {
            //PRODUCTOS POR DEFECTO
            String sql = "INSERT INTO Productos (nombre, precio) values(?,?)";
            int fila2 = 0;
            PreparedStatement prepareStatement2 = con.prepareStatement(sql);
            for (Producto i: servicio.getListProduct()) {
                int index = servicio.getListProduct().indexOf(i);
                prepareStatement2.setString(1, servicio.getListProduct().get(index).getNombre());
                prepareStatement2.setString(2, servicio.getListProduct().get(index).getPrecio().toString());
                fila2 += prepareStatement2.executeUpdate();
            }

            if(fila2>0){
                System.out.println("Productos Por Defectos Agregados!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            //VENTAS POR DEFECTO
            String sql2 = "INSERT INTO VentasProductos(fechaCompra, nombreCliente, cantidad) values(?,?,?)";

            int fila3 = 0;
            PreparedStatement prepareStatement3 = con.prepareStatement(sql2);
            for ( VentasProductos i: servicio.getListVentas()) {
                int index = servicio.getListVentas().indexOf(i);
                prepareStatement3.setString(1, servicio.getListVentas().get(index).getFechaCompra().toString());
                prepareStatement3.setString(2, servicio.getListVentas().get(index).getNombreCliente());
                prepareStatement3.setInt(3, servicio.getListVentas().get(index).getCantidad());
                //lista de productos
                for (Producto p: servicio.getListVentas().get(index).getListaProductos()) {
                    String list = "INSERT INTO Ventas_ListProductos(id_Ventas, id_Producto) values(?,?)";
                    PreparedStatement prepareStatement4 = con.prepareStatement(list);
                    prepareStatement4.setInt(1, (int)i.getId()); //AQUI ESTA EL PROBLEMA DE LA CLAVE FORANEA!!
                    prepareStatement4.setInt(2, p.getId());
                    prepareStatement4.executeUpdate();
                }
                fila3 += prepareStatement3.executeUpdate();
            }

            if(fila3>0){
                System.out.println("Ventas Por Defecto Agregadas!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ok;
    }



    public static BaseDatos getInstancia() {
        if(instancia==null){
            instancia = new BaseDatos();
        }
        return instancia;
    }
}

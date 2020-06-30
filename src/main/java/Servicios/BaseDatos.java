package Servicios;
import Encapsulación.Producto;
import Encapsulación.Usuario;
import Encapsulación.VentasProductos;
import org.h2.tools.Server;
import org.sql2o.Sql2o;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BaseDatos {

    private static BaseDatos instancia;
    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    private Sql2o sql2o;
    private Statement statement;
    private Usuario TEST = null;

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
                "(id INT NOT NULL PRIMARY KEY,\n" +
                "nombre VARCHAR(30) NOT NULL,\n" +
                "precio DECIMAL(10) NOT NULL,\n" +
                "cantidad INT NOT NULL,\n" +
                ");";


        String CreateVentas = "CREATE TABLE IF NOT EXISTS VentasProductos\n" +
                "(id INT NOT NULL PRIMARY KEY,\n" +
                "fechaCompra VARCHAR(30) NOT NULL,\n"+
                "nombreCliente VARCHAR(20) NOT NULL,\n" +
                "FOREIGN KEY (nombreCliente) REFERENCES Usuarios(usuario),\n"+
                ");";
        String CreateVentas_Productos = "CREATE TABLE IF NOT EXISTS Ventas_ListProductos\n" +
                "(id_Ventas INT NOT NULL,\n" +
                "id_Producto INT NOT NULL,\n"+
               // "FOREIGN KEY (id_Ventas) REFERENCES VentasProductos(id),"+
               // "FOREIGN KEY (id_Producto) REFERENCES Productos(id)" +
                ");";

        String CreateVALIDACION = "CREATE TABLE IF NOT EXISTS Validacion\n"+
                "(usuario VARCHAR(30) NOT NULL,\n" +
                "HASH INT NOT NULL PRIMARY KEY,\n" +
                ")";

        //ESTABLECIENDO EN BASE DE DATOS


        statement = conn.createStatement();
        statement.execute(CreateTABLAUSUARIOS);
        //statement.execute(CreateUSUARIOS);
        statement.execute(CreateTABLAProductos);
        statement.execute(CreateVALIDACION);
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

    public boolean INSERT_PORDEFECTO() throws SQLException {
        Connection con = null;
        boolean ok = false;
        try {
            //COMPROBACION SI ADMIN EXISTE
            String look = "SELECT* FROM Usuarios";
            con = Conexion();
            //
            PreparedStatement security = con.prepareStatement(look);
            ResultSet toke = security.executeQuery();

            while (toke.next()){
                TEST = new Usuario(toke.getString("usuario"), toke.getString("nombre"), toke.getString("password"));
            }
            if(TEST == null) {

                //INSERTANDO USUARIO ADMIN
                System.out.println("CREANDO ADMIN...");
                String query = "INSERT INTO Usuarios (usuario, nombre, password) values(?,?,?)";
                //
                PreparedStatement prepareStatement = con.prepareStatement(query);
                //Antes de ejecutar seteo los parametros.
                prepareStatement.setString(1, "admin");
                prepareStatement.setString(2, "admin");
                prepareStatement.setString(3, "admin");
                int fila = prepareStatement.executeUpdate();

                ok = fila > 0;
            }

        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        //ESTABLECIENDO RELACION FORANEA EN TABLA Ventas_ListProductos
        String alter = "ALTER TABLE Ventas_ListProductos\n " +
                "ADD FOREIGN KEY (id_Ventas) REFERENCES VentasProductos(id);";
        //"ADD FOREIGN KEY (id_Producto) REFERENCES Productos(id);";
        PreparedStatement prepareStatement5 = con.prepareStatement(alter);
        prepareStatement5.executeUpdate();
        prepareStatement5.close();
        */
       //CERRANDO
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ok;
    }


    //LA QUE SE ENCARGA DE RECONSTRUIR LA Lista de VENTAS
    public ArrayList<VentasProductos> getVentaBD(){
        ArrayList<VentasProductos> lista = new ArrayList<VentasProductos>();
        ArrayList<Producto> LAlistaProducto = new ArrayList<Producto>();
        Connection con = null;
        try {
            String query = "SELECT* FROM VentasProductos";
            con = BaseDatos.getInstancia().Conexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet retorno = prepareStatement.executeQuery();

            //Extrayendo Ventas de la Base de datos
            while(retorno.next()){
                VentasProductos aux = new VentasProductos();
                aux.setId(retorno.getInt("id"));
                aux.setNombreCliente(retorno.getString("nombreCliente"));
                aux.setFechaCompra(retorno.getString("fechaCompra"));

                //LISTA DE PRODUCTO [SACANDO ID DE PRODUCTOS DE LA TABLA Ventas_ListProductos]
                String sql = "SELECT id_Producto FROM Ventas_ListProductos WHERE id_Ventas=?";
                PreparedStatement preparedStatement2 = con.prepareStatement(sql);
                preparedStatement2.setInt(1, (int) aux.getId());
                ResultSet indicesproductBD = preparedStatement2.executeQuery();
                int index;
                ArrayList<Integer> indicesProductos = new ArrayList<Integer>();
                while (indicesproductBD.next()){
                    index = indicesproductBD.getInt("id_Producto");
                    if(index != 0){
                        indicesProductos.add(index);
                    }
                }
                preparedStatement2.close();

                /*
                //BUSCANDO Productos correspondientes en la BD
                String query2 = "SELECT * FROM Productos";
                PreparedStatement preparedStatement3 = con.prepareStatement(query2);
                ResultSet lista_Productos = preparedStatement3.executeQuery();

                while (lista_Productos.next()){
                    for (int j = 0; j < indicesProductos.size(); j++) {
                        if(indicesProductos.get(j).equals(lista_Productos.getInt("id"))){
                            //AGREGANDO PRODUCTO DE LA BD A LA LISTA TEMPORAL
                            LAlistaProducto.add(new Producto(lista_Productos.getInt("id"), lista_Productos.getString("nombre"), new BigDecimal(lista_Productos.getString("precio"))));
                        }
                    }
                }

                preparedStatement3.close();
                */

                //AGREGANDO PRODUCTOS A LA LISTA TEMPORAL
                for (int i = 0; i < indicesProductos.size(); i++) {
                    for (int j = 0; j < servicio.getListProduct().size(); j++) {
                        if (servicio.getListProduct().get(j).equals(indicesProductos.get(i))){
                            LAlistaProducto.add(servicio.getListProduct().get(j));
                            break;
                        }
                    }
                }

                //Agregando lista de producto Temporal a la Venta temporal
                aux.setListaProductos(LAlistaProducto);
                //Agregando Venta temporal a la lista de ventas que la función retorna
                lista.add(aux);
                //LIMPIEZA
               //indicesProductos.clear();
               //LAlistaProducto.clear();
            }
            prepareStatement.close();

        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return lista;
    }




    public static BaseDatos getInstancia() {
        if(instancia==null){
            instancia = new BaseDatos();
        }
        return instancia;
    }
}

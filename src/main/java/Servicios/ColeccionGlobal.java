package Servicios;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Encapsulación.Usuario;
import io.javalin.core.security.Role;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ColeccionGlobal {

    private static ColeccionGlobal instancia;
    private List<Producto> listProduct = new ArrayList<>();
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<Producto> index = new ArrayList<>();
    private CarroCompra carrito;
    private int idProduct = 0;



    /**
     * Constructor privado.
     */
    private ColeccionGlobal(){
        index.add(new Producto(100, "nada",new BigDecimal("000")));
        carrito = new CarroCompra(0, index , 0);
        //Anadiendo los usuarios.
        listaUsuarios.add(new Usuario("admin", "admin", "admin"));
        listaUsuarios.add(new Usuario("pepe", "pepe", "pepe"));
        listaUsuarios.add(new Usuario("user", "user", "user"));
        //Añadiendo Productos
        listProduct.add(new Producto(003,"Pollo", new BigDecimal("250")));
        listProduct.add(new Producto(004,"Filete", new BigDecimal("200")));
        listProduct.add(new Producto(005,"Arroz", new BigDecimal("10")));
        listProduct.add(new Producto(006,"Habichuelas", new BigDecimal("300")));
        idProduct = 007;
    }


    public static ColeccionGlobal getInstancia(){
        if(instancia==null){
            instancia = new ColeccionGlobal();
        }
        return instancia;
    }

    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }
    public List<Producto> getListProduct(){
        return listProduct;
    }
    public CarroCompra getCarro() {
        return carrito;
    }
    public void setCarro(Producto aux) {
        carrito.getListaProductos().add(aux);
    }

    /*public enum MyRole implements Role {
        ANYONE,
        ADMIN
    }*/
    public int getIdProduct() {
        idProduct++;
        return idProduct - 1;
    }


}

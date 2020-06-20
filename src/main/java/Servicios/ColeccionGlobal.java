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
    private List<CarroCompra> listCarros = new ArrayList<>();
    private int idProduct = 0;
    private long idcart = 0;



    /**
     * Constructor privado.
     */
    private ColeccionGlobal(){
        index.add(new Producto(100, "ticket",new BigDecimal("000")));
        index.add(new Producto(200, "TEST",new BigDecimal("000")));
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
    //REGRESA UN CARRITO DE COMPRA
    public CarroCompra getCarro(String user) {
        CarroCompra carrito = null;
        for (CarroCompra cart: listCarros){
            if(cart.getUser().matches(user)){
                carrito = cart;
                break;
            }
        }
        if(carrito==null){
            carrito = new CarroCompra(idcart,index, index.size(), user);
            listCarros.add(carrito);
        }
        idcart++;
        return carrito;
    }

    public List<CarroCompra> getListCarros(){
        return listCarros;
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

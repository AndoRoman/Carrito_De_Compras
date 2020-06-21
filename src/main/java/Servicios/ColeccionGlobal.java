package Servicios;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Encapsulación.Usuario;
import Encapsulación.VentasProductos;
import io.javalin.core.security.Role;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ColeccionGlobal {

    private static ColeccionGlobal instancia;
    private List<Producto> listProduct = new ArrayList<>();
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<Producto> indexes = new ArrayList<>();
    private List<CarroCompra> listCarros = new ArrayList<>();
    private List<VentasProductos> listVentas = new ArrayList<>();




    private long idVentas = 2;
    private int idProduct = 0;
    private long idcart = 0;



    /**
     * Constructor privado.
     */
    private ColeccionGlobal(){
        //reñenando lista de productos por defecto en el carrito
        indexes.add(new Producto(100, "ticket",new BigDecimal("000")));
        indexes.add(new Producto(200, "Producto de Prueba",new BigDecimal("000")));
        //Anadiendo los usuarios.
        listaUsuarios.add(new Usuario("admin", "admin", "admin"));
        listaUsuarios.add(new Usuario("pepe", "pepe", "pepe"));
        listaUsuarios.add(new Usuario("user", "user", "user"));
        //Añadiendo Productos
        listProduct.add(new Producto(listProduct.size(), "Lata de Maiz", new BigDecimal("50")));
        listProduct.add(new Producto(listProduct.size(), "Lata de Salsa", new BigDecimal("75")));
        listProduct.add(new Producto(listProduct.size(), "Espaguetis", new BigDecimal("30")));
        //Venta por default
        listVentas.add(new VentasProductos(000, Date.from(Instant.now()), "admin", indexes, 2));
        listVentas.add(new VentasProductos(001, Date.from(Instant.now()), "user", indexes, 2));
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
            carrito = new CarroCompra(idcart,indexes, indexes.size(), user);
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

    public List<VentasProductos> getListVentas() {
        return listVentas;
    }

    public void setVentas(VentasProductos Venta) {
        listVentas.add(Venta);
    }
    public long getIdVentas() {
        idVentas++;
        return idVentas;
    }

}

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
    private static BaseDatos BD;
    private List<Producto> listProduct = new ArrayList<>();
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<Producto> indexes = new ArrayList<>();
    private List<CarroCompra> listCarros = new ArrayList<>();
    private List<VentasProductos> listVentas = new ArrayList<>();




    private long idVentas = 1;
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
        listProduct.add(new Producto(listProduct.size() + 1, "Lata de Maiz", new BigDecimal("50")));
        listProduct.add(new Producto(listProduct.size() + 1, "Lata de Salsa", new BigDecimal("75")));
        listProduct.add(new Producto(listProduct.size()+ 1, "Espaguetis", new BigDecimal("30")));
        listProduct.add(new Producto(listProduct.size()+ 1, "Pollo", new BigDecimal("38")));
        listProduct.add(new Producto(listProduct.size() + 1, "ticket",new BigDecimal("000")));

        //Venta por default
        VentasProductos v = new VentasProductos();
        v.setId(1);
        v.setFechaCompra(Date.from(Instant.now()).toString());
        v.setNombreCliente("admin");
        v.setListaProductos(listProduct);
        v.setCantidad(2);
        listVentas.add(v);

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

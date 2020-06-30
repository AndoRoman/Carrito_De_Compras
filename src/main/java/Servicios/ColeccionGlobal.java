package Servicios;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Encapsulación.Usuario;
import Encapsulación.VentasProductos;
import io.javalin.core.security.Role;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ColeccionGlobal {

    private static ColeccionGlobal instancia;
    private List<Producto> listProduct = new ArrayList<>();
    private List<Producto> indexes = new ArrayList<>();
    private List<CarroCompra> listCarros = new ArrayList<>();
    private List<VentasProductos> listVentas = new ArrayList<>();




    private long idVentas = 0;
    private int idProduct = 0;
    private long idcart = 0;



    /**
     * Constructor privado.
     */
    private ColeccionGlobal(){

        //reñenando lista de productos por defecto en el carrito
        indexes.add(new Producto(1, "ticket",new BigDecimal("000")));
        //Añadiendo Productos
        listProduct.add(new Producto(listProduct.size() + 1, "ticket",new BigDecimal("000")));
        listProduct.add(new Producto(listProduct.size() + 1, "Lata de Maiz", new BigDecimal("50")));
        listProduct.add(new Producto(listProduct.size() + 1, "Lata de Salsa", new BigDecimal("75")));
        listProduct.add(new Producto(listProduct.size()+ 1, "Espaguetis", new BigDecimal("30")));
        listProduct.add(new Producto(listProduct.size()+ 1, "Pollo", new BigDecimal("38")));
        listProduct.add(new Producto(listProduct.size() + 1, "Producto de Prueba",new BigDecimal("000")));

    }


    public static ColeccionGlobal getInstancia(){
        if(instancia==null){
            instancia = new ColeccionGlobal();
        }
        return instancia;
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

    public long  getIDVENTAS() {
        idVentas++;
        return idVentas - 1;
    }

    public List<VentasProductos> getListVentas() {
        return listVentas;
    }

    public void setVentas(VentasProductos Venta) {
        listVentas.add(Venta);
    }

    public void setListVentas(ArrayList<VentasProductos> ventaBD) {
        listVentas.clear();
        listVentas.addAll(ventaBD);
    }
}

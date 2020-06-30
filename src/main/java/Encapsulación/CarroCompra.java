package Encapsulación;

import java.util.List;

public class CarroCompra {
    private long id;
    private List<Producto> listaProductos;
    private int cantidad;

    private String user;

    public CarroCompra(long id, List<Producto> listaProductos, int cantidad, String user) {
        this.id = id;
        this.listaProductos = listaProductos;
        this.cantidad = cantidad;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

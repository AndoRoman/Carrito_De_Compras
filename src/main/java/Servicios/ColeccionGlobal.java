package Servicios;

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

    /**
     * Constructor privado.
     */
    private ColeccionGlobal(){
        //Anadiendo los usuarios.
        listaUsuarios.add(new Usuario("admin", "admin", "admin"));
        listaUsuarios.add(new Usuario("pepe", "pepe", "pepe"));
        listaUsuarios.add(new Usuario("user", "user", "user"));
        //Añadiendo Productos
        listProduct.add(new Producto(003,"Pollo", new BigDecimal("250")));
        listProduct.add(new Producto(004,"Filete", new BigDecimal("200")));
        listProduct.add(new Producto(005,"Arroz", new BigDecimal("10")));
        listProduct.add(new Producto(006,"Habichuelas", new BigDecimal("300")));
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

    /*public enum MyRole implements Role {
        ANYONE,
        ADMIN
    }*/

}

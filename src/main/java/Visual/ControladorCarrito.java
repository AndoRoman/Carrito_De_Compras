package Visual;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;

public class ControladorCarrito {

    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();

    public void ManejadorCarro(Javalin app){
        //Para Eliminar del carro
        app.post("/sacar",  ctx ->{
            String product = ctx.formParam("nombre");
            String usuario = ctx.sessionAttribute("usuario");
            SacarDelCarro(product, usuario);
            ctx.redirect("/Carrito.html");
        });


    }

    private void SacarDelCarro(String nombreProducto, String usuario){
            int index = 0;
            //BUSCO EL CARRITO QUE LE PERTENECE A ESE USUARIO
            for (CarroCompra i : servicio.getListCarros()) {
                if(i.getUser().matches(usuario)){
                    index = servicio.getListCarros().indexOf(i);
                }
            }
            //SACO EL PRODUCTO DEL CARRITO DEL USUARIO
            servicio.getListCarros().get(index).getListaProductos().remove(nombreProducto);
    }

    public void AgregarAlCarro(String nombreProducto, String user){
        int indexUser = 0, indexProduct = 0;
        Producto nuevoProduct = null;

        //PRIMERO BUSCO EL CARRITO QUE LE PERTENECE A ESE USUARIO
        for (CarroCompra i : servicio.getListCarros()) {
            if(i.getUser().matches(user)){
                indexUser = servicio.getListCarros().indexOf(i);
            }
        }
        //SEGUNDO BUSCO EL PRODUCTO QUE SE QUIERE AGREGAR AL CARRITO
        for (Producto j: servicio.getListProduct()) {
            if (j.getNombre().matches(nombreProducto)){
                indexProduct = servicio.getListProduct().indexOf(j);
            }
        }
        //CLONO EL PRODUCTO QUE SE QUIERE AGREGAR
        nuevoProduct = servicio.getListProduct().get(indexProduct);
        //LO AGREGO AL CARRITO
        servicio.getListCarros().get(indexUser).getListaProductos().add(nuevoProduct);
    }
}

package Visual;

import Encapsulación.CarroCompra;
import Encapsulación.Producto;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;

public class ControladorCarrito {
    private static ControladorCarrito instancia;
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

    public static ControladorCarrito getInstancia(){
        if(instancia==null){
            instancia = new ControladorCarrito();
        }
        return instancia;
    }
    private void SacarDelCarro(String nombreProducto, String usuario){
        int index = 0;
            //BUSCO EL CARRITO QUE LE PERTENECE A ESE USUARIO
        for (CarroCompra i : servicio.getListCarros()) {
                if(i.getUser().matches(usuario)){
                    index = servicio.getListCarros().indexOf(i);
                    break;
                }
            }
            //BUSCO EL PRODUCTO
        for (Producto rechazado: servicio.getListCarros().get(index).getListaProductos()) {
            if(rechazado.getNombre().matches(nombreProducto)){
                servicio.getListCarros().get(index).getListaProductos().remove(rechazado);
                break;
            }
        }
            /*servicio.getListCarros().stream().forEach(carroCompra -> {
                if (carroCompra.getUser().matches(usuario)){

                }
            });*/


    }

    public void AgregarAlCarro(String nombreProducto, String user){
        int indexUser = 0, indexProduct = 0;
        Producto nuevoProducto;

        //PRIMERO BUSCO EL CARRITO QUE LE PERTENECE A ESE USUARIO
        for (CarroCompra i : servicio.getListCarros()) {
            if(i.getUser().matches(user)){
                indexUser = servicio.getListCarros().indexOf(i);
                break;
            }
        }
        //SEGUNDO BUSCO EL PRODUCTO QUE SE QUIERE AGREGAR AL CARRITO
        for (Producto j: servicio.getListProduct()) {
            if (j.getNombre().matches(nombreProducto)){
                indexProduct = servicio.getListProduct().indexOf(j);
                break;
            }
        }
        //CLONO EL PRODUCTO QUE SE QUIERE AGREGAR
        nuevoProducto = servicio.getListProduct().get(indexProduct);
        //LO AGREGO AL CARRITO
        servicio.getListCarros().get(indexUser).getListaProductos().add(nuevoProducto);
    }
}

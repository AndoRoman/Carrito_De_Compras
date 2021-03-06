package Visual;

import Encapsulación.Usuario;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class  ControladorSesion {

    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    List<Usuario> users = servicio.getListaUsuarios();
    static Map<String, String> reservations = new HashMap<String, String>() ;

    public void control(Javalin app){

        app.get("/", ctx -> {
            Integer contador = ctx.sessionAttribute("contador");
            if(contador==null){
                contador = 0;
            }
            contador++;
            ctx.sessionAttribute("contador", contador);

        });
        //autenticación de sesión
        app.post("/login", ctx -> {
           String user = ctx.formParam("usuario");
           String pass = ctx.formParam("password");
           boolean token = false;
           int i = 0;
           int stop = users.size() - 1;
           try {
               do {
                   if (user.matches(users.get(i).getUsuario())) {
                       if (pass.matches(users.get(i).getPassword())) {
                           token = true;

                       }
                   }
                   i++;
               } while (token == false | i < stop);
           } catch (Exception e){
                token = false;
           }

           if(token){
               //creando una cookie
               ctx.cookie("usuario", user);
               ctx.sessionAttribute("usuario", user);
               ctx.redirect("/ListCompras.html");

           }else {
               ctx.redirect("/401.html");
           }
        });


    }




}

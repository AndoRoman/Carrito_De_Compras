package Visual;

import Encapsulación.Usuario;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;
import io.javalin.core.security.BasicAuthCredentials;
import io.javalin.core.security.Role;
import io.javalin.http.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


public class ControladorSesion {

    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    List<Usuario> users = servicio.getListaUsuarios();
    static Map<String, String> reservations = new HashMap<String, String>() ;

    public void control(Javalin app){

        /*app.routes(() -> {
            path("/ListCompras.html", () -> {


                before(ctx -> {
                    //recuperando el usuario de la sesión,en caso de no estar, redirecciona la pagina 401.
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    if(usuario == null){// usuario no existe
                        ctx.redirect("/Login.html");
                    }
                    //continuando con la consulta del endpoint solicitado.
                });

                //Respuesta del index.
                get("/ListCompras", ctx ->{
                    Usuario usuario = ctx.sessionAttribute("usuario");
                    ctx.result("Zona Admin por la forma clasica --- Usuario: "+usuario.getUsuario());
                } );
            });
        });*/
        //TEST
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
               ctx.redirect("/ListCompras.html");
           }else {
               ctx.html("NEGATIVO PA TI");
           }
        });


    }




}

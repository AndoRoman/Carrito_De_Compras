package Visual;

import Encapsulación.Usuario;
import Servicios.BaseDatos;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class  ControladorSesion {

    ColeccionGlobal servicio = ColeccionGlobal.getInstancia();
    List<Usuario> users = servicio.getListaUsuarios();

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
           try {/*
               do {
                   /*
                   if (user.matches(users.get(i).getUsuario())) {
                       if (pass.matches(users.get(i).getPassword())) {
                           token = true;

                       }
                   }
                   i++;


               } while (token == false);*/
               System.out.println("Autenticando....");
               token = autenticacionBD(user, pass);
           } catch (Exception e){
                token = false;
           }

           if(token){
               //creando una cookie
               ctx.cookie("usario", user);
               ctx.sessionAttribute("usuario", user);
               ctx.redirect("/ListCompras.html");

           }else {
               ctx.redirect("/401.html");
           }
        });


    }

    public boolean autenticacionBD(String user, String pass) {
        boolean result = false;
        try {
            Connection con = BaseDatos.Conexion();
            String query = "SELECT * FROM Usuarios WHERE usuario=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, user);
            ResultSet users = preparedStatement.executeQuery();

            while(users.next()){
                if(pass.matches(users.getString("password"))){
                    result = true;
                }
            }
        }catch (SQLException e) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }




}

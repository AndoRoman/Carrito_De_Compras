package Visual;

import Encapsulación.Usuario;
import Servicios.BaseDatos;
import Servicios.ColeccionGlobal;
import io.javalin.Javalin;
import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;

import java.math.BigInteger;
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


    public void control(Javalin app){

        app.get("/LoginOUT", ctx -> {
            ctx.removeCookie("userssession");
            ctx.removeCookie("usuario");
            ctx.sessionAttribute("usuario", null);
            ctx.redirect("/");
        });

        //VERIFICANDO SESSIÓN
        app.get("/Login.html", ctx -> {
            if(ctx.cookie("userssession") != null){
               String user = ValidadoCookie(ctx.cookie("userssession")).getUsuario();
               String pass = ValidadoCookie(ctx.cookie("userssession")).getPassword();
                if(autenticacionBD(user, pass)) {
                    ctx.sessionAttribute("usuario", user);
                    ctx.redirect("/ListCompras.html");
                }
            }
            else{
                ctx.render("/HTML/Login.html");
            }
        });

        //autenticación de sesión
        app.post("/login", ctx -> {
           String user = ctx.formParam("usuario");
           String pass = ctx.formParam("password");
           String boton = ctx.formParam("check");
           boolean token = false;
           try {

               System.out.println("Autenticando....");
               token = autenticacionBD(user, pass);
           } catch (Exception e) {
               token = false;
           }


           //RECORDAR USUARIO
           if(boton != null){
               int login_HASH = hashCode();

               //ENCRIPTACIÓN DEL HASH de la Cookie
               BasicIntegerNumberEncryptor numberEncryptor = new BasicIntegerNumberEncryptor();
               numberEncryptor.setPassword("secreto");
               BigInteger myEncryptedNumber = numberEncryptor.encrypt(new BigInteger(String.valueOf(login_HASH)));
                System.out.println("PLANO: " + login_HASH + " ENCRIPTADO: " + myEncryptedNumber);
               //604800 seg =  1 semana
               ctx.cookie("userssession", myEncryptedNumber.toString(), 604800);

               //GUARDANDO EN BD
               try {
                   Connection con = BaseDatos.Conexion();
                   String query = "INSERT INTO Validacion (usuario, HASH) VALUES (?,?)";
                   PreparedStatement preparedStatement = con.prepareStatement(query);
                   preparedStatement.setString(1, user);
                   preparedStatement.setInt(2, login_HASH);
                   preparedStatement.executeUpdate();

               }catch (SQLException e){
                   Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, e);
               }
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



    private Usuario ValidadoCookie(String userssession) {
        Usuario validation = null;
        //DESENCRIPTANDO HASH
        System.out.println("VALIDANDO...");
        BasicIntegerNumberEncryptor numberEncryptor = new BasicIntegerNumberEncryptor();
        numberEncryptor.setPassword("secreto");
        //HASH Limpio
        BigInteger plainLogin_hash = numberEncryptor.decrypt(new BigInteger(userssession));

        try {
            Connection con = BaseDatos.Conexion();
            String query = "SELECT* FROM Validacion WHERE HASH=?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, plainLogin_hash.intValue());
            ResultSet Table_user = preparedStatement.executeQuery();
            //USUARIO
            String user = null;
            String nombre = null;
            String pass = null;

            while (Table_user.next()){
                user = Table_user.getString("usuario");
            }
            preparedStatement.close();

            String queryUsers = "SELECT * FROM Usuarios WHERE usuario=?";
            PreparedStatement preparedStatement2 = con.prepareStatement(queryUsers);
            preparedStatement2.setString(1, user);
            ResultSet users = preparedStatement2.executeQuery();

            while (users.next()){
                nombre = users.getString("nombre");
                pass = users.getString("password");
            }

            validation = new Usuario(user, nombre, pass);

        }catch (SQLException e){
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, e);
        }


        return validation;
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

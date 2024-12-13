package com.ad03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionMySQL {
    
    //Atributos para conectar con MySQL
    // Driver de conexión con la base de datos
    private static String driver = "com.mysql.cj.jdbc.Driver";
    
    // Nombre de la base de datos
    private static String baseDatos = "dbeventos";
    
    // Host o servidor
    private static String servidor = "localhost"; //127.0.0.1
    
    // Puerto
    private static String puerto = "3306";
    
    // cadena de conexión de nuestra base de datos
    private static String cadenaConexion = "jdbc:mysql://" + servidor + ":" + puerto + "?serverTimezone=UTC&characterEncoding=utf8";
    
    // Nombre de usuario
    private static String usuario = "root";
    
    // Clave de usuario
    private static String password = "admin";
    
    
    //Metodo que realiza la conexión
    public static Connection conectar() throws SQLException {
        
        //Devolver la conexión realizada con el Driver
        return DriverManager.getConnection(cadenaConexion, usuario, password);
    }
    
    public static void usesBDMySQL(Connection conexion) throws SQLException {
        
        //En un String creamos la sentencia que crea la base de datos
        String sql = "use dbeventos";
            //Creamos el objeto que ejecutará la sentencia en la conexión
        Statement sentencia = conexion.createStatement();
        
        //Ejecutamos sentencia
        sentencia.executeUpdate(sql);
    }
}


package com.ad03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio2 {
    
    public static int ejecutarConsulta(String ubicacion) throws SQLException {

        int capacidad = -1;
         
        //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "select capacidad from ubicaciones where nombre = ?";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Sustituir el parametro por el valor de la ubicacion
        sentencia.setString(1, ubicacion);
        
        //Selecciona la base de datos para ejecutar la sentencia
        //Sin cerar la conexión, por este motivo se pasa por parametro
        //para que use lo haga con la misma conexión
        ConexionMySQL.usesBDMySQL(conexion);
        
        //Ejecutamos sentencia guardando el resultado en un ResultSet
        ResultSet resultado = sentencia.executeQuery();

        //Si el resultado tiene filas
        if (resultado.next()) {
            capacidad = resultado.getInt("capacidad");
        }   
        
        //Liberar recursos
        resultado.close();
        sentencia.close();
        conexion.close();
        
        return capacidad;
    }
    
    public static void ejecutarUpdate(int capacidad, String ubicacion) throws SQLException{
        
         //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "update ubicaciones set capacidad = ? where nombre = ?";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Sustituir el parametro ? por los valores en el orden el que se encuentran
        //en la sentencia sql
        sentencia.setInt(1, capacidad);
        sentencia.setString(2, ubicacion);
        
        //Selecciona la base de datos para ejecutar la sentencia
        //Sin cerar la conexión, por este motivo se pasa por parametro
        //para que use lo haga con la misma conexión
        ConexionMySQL.usesBDMySQL(conexion);
        
        //ejecutamos la modificacion
        sentencia.executeUpdate();
        
         //Liberar recursos
        sentencia.close();
        conexion.close();
    }
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        
        try {
            //Pedir al usuario la ubicación
            System.out.println("Introduce el nombre de la ubicación: ");
            String ubicacion = sc.nextLine();
           
            //Buscar la capacidad
            int capacidad = ejecutarConsulta(ubicacion);
            
            if (capacidad > 0) {
                System.out.println("La capacidad actual de la ubicación " + ubicacion + " es: " + capacidad);
                
                //Introduce la nueva capacidad
                System.out.println("Introduce la nueva capacidad máxima: ");
                int nuevaCapacidad = sc.nextInt();
                
                //Ejecutar la modificacion
                ejecutarUpdate(nuevaCapacidad, ubicacion);
                
                //mensaje de confirmación
                System.out.println("Capacidad actualizada correctamente");
            }
            else {
                System.out.println("No existe una ubicación con el nombre: " + ubicacion);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }  
}

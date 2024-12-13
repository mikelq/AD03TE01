
package com.ad03;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio4 {
    
    public static void mostrarEventos() throws SQLException {

        //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "select e.id_evento, nombre_evento, capacidad - count(*) disponibles " +
                        "from eventos e " +
                        "join ubicaciones u on u.id_ubicacion = e.id_ubicacion " +
                        "join asistentes_eventos av on av.id_evento = e.id_evento " +
                        "group by id_evento, nombre_evento, capacidad " +
                        "order by id_evento";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Selecciona la base de datos para ejecutar la sentencia
        //Sin cerar la conexión, por este motivo se pasa por parametro
        //para que use lo haga con la misma conexión
        ConexionMySQL.usesBDMySQL(conexion);
        
        //Ejecutamos sentencia guardando el resultado en un ResultSet
        ResultSet resultado = sentencia.executeQuery();
        
        //Recorremos el ResultSet y mostramos los datos
        while (resultado.next()){
            System.out.println(resultado.getInt("id_evento") + ". " + resultado.getString("nombre_evento"));
        }   
        //Liberar recursos
        resultado.close();
        sentencia.close();
        conexion.close();
    }
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        try {
            mostrarEventos();
            
            System.out.println("Introduce el ID del evento para consultar la cantidad de asistentes: ");
            int idEvento = sc.nextInt();
            
            Connection conexion = ConexionMySQL.conectar();
            ConexionMySQL.usesBDMySQL(conexion);
            CallableStatement callableStatement = conexion.prepareCall("{? = CALL obtener_numero_asistentes(?)}");

            // Configurar parámetros de entrada y salida
            callableStatement.registerOutParameter(1, java.sql.Types.INTEGER); // Valor de retorno
            callableStatement.setInt(2, idEvento); // Parámetro de entrada

            // Ejecutar la función
            callableStatement.execute();

            // Obtener el resultado
            int numeroAsistentes = callableStatement.getInt(1);
            
            //Muestro
            System.out.println("El número de asistentes para el evento seleccionado es: " + numeroAsistentes);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

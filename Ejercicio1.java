
package com.ad03;

import static com.ad03.ConexionMySQL.conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ejercicio1 {
    
    public static void ejecutarConsulta() throws SQLException {

        //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "select e.nombre_evento Evento, count(*) Asistentes, " +
                        "u.nombre Ubicacion, u.direccion Direccion " +
                        "from eventos e " +
                        "join ubicaciones u on u.id_ubicacion = e.id_ubicacion " +
                        "join asistentes_eventos av on av.id_evento = e.id_evento " +
                        "group by e.nombre_evento, u.nombre, u.direccion " +
                        "order by Evento desc";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Selecciona la base de datos para ejecutar la sentencia
        //Sin cerar la conexión, por este motivo se pasa por parametro
        //para que use lo haga con la misma conexión
        ConexionMySQL.usesBDMySQL(conexion);
        
        //Ejecutamos sentencia guardando el resultado en un ResultSet
        ResultSet resultado = sentencia.executeQuery();
        
        //Recorremos el ResultSet y mostramos los datos
        System.out.printf("%-32s| %-12s| %-38s| %-28s\n", "Evento", "Asistentes", "Ubicación", "Dirección");
        System.out.printf("----------------------------------------------------------------------------------------------------------\n");
        while (resultado.next()){
            System.out.printf("%-32s| %-12s| %-38s| %-28s\n", resultado.getString("Evento"), 
                    String.valueOf(resultado.getInt("Asistentes")), 
                    resultado.getString("Ubicacion"), 
                    resultado.getString("Direccion"));
        }   
        
        //Liberar recursos
        resultado.close();
        sentencia.close();
        conexion.close();
    }
    
    
    public static void main(String[] args){
        try {
            ejecutarConsulta();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }  
}

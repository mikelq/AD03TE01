package com.ad03;

import static com.ad03.Ejercicio2.ejecutarConsulta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Ejercicio3 {
    
    public static String buscarDNI(String dni) throws SQLException {

        String nombre = null;
         
        //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "select nombre from asistentes where dni = ?";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Sustituir el parametro por el valor del dni
        sentencia.setString(1, dni);
        
        //Selecciona la base de datos para ejecutar la sentencia
        //Sin cerar la conexión, por este motivo se pasa por parametro
        //para que use lo haga con la misma conexión
        ConexionMySQL.usesBDMySQL(conexion);
        
        //Ejecutamos sentencia guardando el resultado en un ResultSet
        ResultSet resultado = sentencia.executeQuery();

        //Si el resultado tiene filas
        if (resultado.next()) {
            nombre = resultado.getString("nombre");
        }   
        
        //Liberar recursos
        resultado.close();
        sentencia.close();
        conexion.close();
        
        return nombre;
    }
    
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
            System.out.println(resultado.getInt("id_evento") + ". " + resultado.getString("nombre_evento") +
                    " - Espacios disponibles: " + resultado.getInt("disponibles"));
        }   
        //Liberar recursos
        resultado.close();
        sentencia.close();
        conexion.close();
    }
    
    public static int comprobarCapacidad(int idEvento) throws SQLException {

        int disponibles = 0;
        
        //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "select e.id_evento, nombre_evento, capacidad - count(*) disponibles " +
                        "from eventos e " +
                        "join ubicaciones u on u.id_ubicacion = e.id_ubicacion " +
                        "join asistentes_eventos av on av.id_evento = e.id_evento " +
                        "where e.id_evento = ? " +
                        "group by id_evento, nombre_evento, capacidad ";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        //Asociar el parametro del id de evento
        sentencia.setInt(1, idEvento);
    
        //Selecciona la base de datos para ejecutar la sentencia
        //Sin cerar la conexión, por este motivo se pasa por parametro
        //para que use lo haga con la misma conexión
        ConexionMySQL.usesBDMySQL(conexion);
        
        //Ejecutamos sentencia guardando el resultado en un ResultSet
        ResultSet resultado = sentencia.executeQuery();
        
        //Recorremos el ResultSet y mostramos los datos
        if (resultado.next()){
            disponibles = resultado.getInt("disponibles");
        }   
        //Liberar recursos
        resultado.close();
        sentencia.close();
        conexion.close();
        
        return disponibles;
    }
    
    public static void insertarAsistente(String dni, String nombre) throws SQLException{
        
         //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "insert into asistentes values (?, ?)";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Sustituir el parametro ? por los valores en el orden el que se encuentran
        //en la sentencia sql
        sentencia.setString(1, dni);
        sentencia.setString(2, nombre);
        
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
    
    public static void registrarAsistente(String dni, int idEvento) throws SQLException{
        
         //Crear el objeto Connection que se conecta a la base de datos
        Connection conexion = ConexionMySQL.conectar();
        
        
        
        //Creamos la sentencia sql que vamos a ejecutar
        String sql = "insert into asistentes_eventos values (?, ?)";
        
        //Crear la sentencia preparada pasandole la sentencia sql que vamos a ejecutar
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        //Sustituir el parametro ? por los valores en el orden el que se encuentran
        //en la sentencia sql
        sentencia.setString(1, dni);
        sentencia.setInt(2, idEvento);
        
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
            String regExpr = "^[0-9]{8}[A-Z]{1}$";
            
            //Pedir al usuario el dni
            System.out.println("Introduce el DNI del asistente: ");
            String dni = sc.nextLine();
            
            //Si el dni es valido
            if (dni.matches(regExpr)) {
                //Buscar el nombre
                String nombre = buscarDNI(dni);

                if (nombre == null) {
                    System.out.println("No se encontró un asistente con el DNI proporcionado");
                    //Pedir los datos del nuevo asistente
                    System.out.println("Introduce nombre del asistente: ");
                    nombre = sc.nextLine();
                    //Insertar el nuevo asistente
                    insertarAsistente(dni, nombre);  
                }
                
                System.out.println("Estas realizando la reserva para: " + nombre); 
                System.out.println("Lista de eventos:");
                mostrarEventos();  
                
                //Elegir el evento al que asistir
                System.out.println("Elige el número de evento al que quiere asistir: ");
                int idEvento = sc.nextInt();
                
                //Si hay capacidad para ese evento
                if (comprobarCapacidad(idEvento) > 0) {
                    registrarAsistente(dni, idEvento);
                    System.out.println(nombre + " ha sido registrado para el evento seleccionado");
                }
                else {
                    System.out.println("No hay espacios disponibles para el evento seleccionado");
                }
            }
            else {
                System.out.println("DNI inválido");
            }
            
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

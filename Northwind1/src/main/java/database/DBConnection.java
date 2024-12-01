package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://127.0.0.1/almacen"; // Cambia "almacen" por el nombre de tu base de datos
    private static final String USER = "bianca"; // Cambia "root" por tu usuario de MySQL
    private static final String PASSWORD = "1234"; // Cambia "1234" por tu contraseña
    private static Connection connection;

    // Método para obtener la conexión a la base de datos
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Cargar el controlador de MySQL (opcional en las versiones modernas de Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el controlador de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return connection;
    }
}
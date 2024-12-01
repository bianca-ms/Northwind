package database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class JSONClass {
    public static void addProductsFromJson() {
        System.out.println("Agregando productos desde JSON...");

        String jsonUrl = "https://dummyjson.com/products";
        Connection connection = DBConnection.getConnection();

        if (connection != null) {
            try {
                connection.setAutoCommit(false); // Habilitamos commit manual

                // Leer datos desde la URL
                InputStreamReader reader = new InputStreamReader(new URL(jsonUrl).openStream());
                StringBuilder jsonContent = new StringBuilder();
                int data;
                while ((data = reader.read()) != -1) {
                    jsonContent.append((char) data);
                }
                reader.close();

                // Parsear el JSON
                JSONObject jsonObject = new JSONObject(jsonContent.toString());
                JSONArray products = jsonObject.getJSONArray("products");

                // Preparar la consulta SQL
                String insertQuery = "INSERT INTO productos (nombre, descripcion, cantidad, precio) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(insertQuery);

                for (int i = 0; i < products.length(); i++) {
                    JSONObject product = products.getJSONObject(i);
                    String nombre = product.getString("title");
                    String descripcion = product.getString("description");
                    int cantidad = product.getInt("stock");
                    double precio = product.getDouble("price");

                    statement.setString(1, nombre);
                    statement.setString(2, descripcion);
                    statement.setInt(3, cantidad);
                    statement.setDouble(4, precio);
                    statement.executeUpdate();
                }

                connection.commit(); // Confirmamos los cambios
                System.out.println("Productos agregados exitosamente.");
            } catch (Exception e) {
                System.out.println("Error al agregar productos: " + e.getMessage());
                e.printStackTrace();
                try {
                    if (connection != null) connection.rollback(); // Revertir cambios en caso de error
                } catch (Exception rollbackEx) {
                    System.out.println("Error al revertir cambios: " + rollbackEx.getMessage());
                }
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (Exception closeEx) {
                    System.out.println("Error al cerrar la conexión: " + closeEx.getMessage());
                }
            }
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
        }
    }
}
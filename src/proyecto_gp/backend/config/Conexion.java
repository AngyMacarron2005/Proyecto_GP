package proyecto_gp.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Nombre exacto de tu base de datos del SQL dump
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_gp_ap?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Ajusta tu contraseña si XAMPP tiene una

    /**
     * Método utilizado por la mayoría de los DAOs
     * @return 
     */
    public static Connection conectar() {
        Connection con = null;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                Class.forName("org.mariadb.jdbc.Driver");
            }
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC no encontrado en las librerías del proyecto. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos 'sistema_gp_ap': " + e.getMessage());
        }
        return con;
    }

    /**
     * Método alias para compatibilidad con otros DAOs
     * @return 
     * @throws java.sql.SQLException
     */
    public static Connection getConexion() throws SQLException {
        Connection con = conectar();
        if (con == null) {
            throw new SQLException("No se pudo establecer la conexión con la base de datos.");
        }
        return con;
    }
}
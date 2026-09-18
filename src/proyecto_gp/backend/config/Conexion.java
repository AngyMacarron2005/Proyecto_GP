package proyecto_gp.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Configura aquí las credenciales de tu base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/proyecto_gp?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Coloca tu contraseña si tienes una

    /**
     * Obtiene y retorna una conexión activa a la base de datos MySQL.
     * @return Connection objeto de conexión JDBC
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC de MySQL no encontrado: " + e.getMessage());
        }
    }

    static Connection conectar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
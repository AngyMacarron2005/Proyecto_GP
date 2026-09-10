
package proyecto_gp.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Parámetros de conexión para XAMPP
    private static final String URL = "jdbc:mariadb://localhost:3306/sistema_gp_ap";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Vacío por defecto en XAMPP

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("-Conexion exitosa a la base de datos 'sistema_gp_ap'!");
        } catch (SQLException e) {
            System.err.println("=================================");
            System.err.println("ERROR DE CONEXION");
            System.err.println("=================================");
            e.printStackTrace();
        }
        return conexion;
    }
}

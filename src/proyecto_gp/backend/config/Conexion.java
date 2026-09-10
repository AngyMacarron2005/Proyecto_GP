
package proyecto_gp.backend.config;

/**
 *
 * @author usuario
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {
    private static final String URL = "jdbc:mariadb://localhost:3306/sistema_gp_ap?useUnicode=true&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}

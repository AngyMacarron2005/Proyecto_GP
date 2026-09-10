package proyecto_gp.backend.config;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) throws SQLException {
        Connection con = Conexion.conectar();
        if (con != null) {
            try {
                con.close();
                System.out.println("La conexion se cerro correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

package proyecto_gp.backend.dao;

import proyecto_gp.backend.config.Conexion;
import proyecto_gp.backend.model.FormaPagoSRI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormaPagoDAO {

    public List<FormaPagoSRI> listar() {
        List<FormaPagoSRI> lista = new ArrayList<>();
        String sql = "SELECT * FROM forma_pago_sri";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FormaPagoSRI fp = new FormaPagoSRI();
                fp.setId(rs.getInt("id"));
                fp.setCodigoSri(rs.getString("codigo_sri"));
                fp.setDescripcion(rs.getString("descripcion"));
                lista.add(fp);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar formas de pago: " + e.getMessage());
        }
        return lista;
    }
}
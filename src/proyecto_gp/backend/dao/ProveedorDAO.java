
package proyecto_gp.backend.dao;

import proyecto_gp.backend.config.Conexion;
import proyecto_gp.backend.model.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public List<Proveedor> listar() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT p.*, t.descripcion AS tipo_doc_desc FROM proveedor p " +
                     "INNER JOIN tipo_documento_identidad t ON p.tipo_documento_id = t.id";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor prov = new Proveedor();
                prov.setId(rs.getInt("id"));
                prov.setTipoDocumentoId(rs.getInt("tipo_documento_id"));
                prov.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                prov.setRazonSocial(rs.getString("razon_social"));
                prov.setNombreComercial(rs.getString("nombre_comercial"));
                prov.setDireccion(rs.getString("direccion"));
                prov.setTelefono(rs.getString("telefono"));
                prov.setCorreoElectronico(rs.getString("correo_electronico"));
                prov.setNombreTipoDocumento(rs.getString("tipo_doc_desc"));
                lista.add(prov);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proveedores: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Proveedor prov) {
        String sql = "INSERT INTO proveedor (tipo_documento_id, numero_identificacion, razon_social, nombre_comercial, direccion, telefono, correo_electronico) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, prov.getTipoDocumentoId());
            ps.setString(2, prov.getNumeroIdentificacion());
            ps.setString(3, prov.getRazonSocial());
            ps.setString(4, prov.getNombreComercial());
            ps.setString(5, prov.getDireccion());
            ps.setString(6, prov.getTelefono());
            ps.setString(7, prov.getCorreoElectronico());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar proveedor: " + e.getMessage());
            return false;
        }
    }
}
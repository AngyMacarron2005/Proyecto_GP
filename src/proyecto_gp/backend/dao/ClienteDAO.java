
package proyecto_gp.backend.dao;

import proyecto_gp.backend.config.Conexion;
import proyecto_gp.backend.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // 1. LISTAR TODOS LOS CLIENTES
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, t.descripcion AS tipo_doc_desc FROM cliente c " +
                     "INNER JOIN tipo_documento_identidad t ON c.tipo_documento_id = t.id";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setTipoDocumentoId(rs.getInt("tipo_documento_id"));
                c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                c.getNombres();
                c.setNombres(rs.getString("nombres"));
                c.setApellidos(rs.getString("apellidos"));
                c.setDireccion(rs.getString("direccion"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreoElectronico(rs.getString("correo_electronico"));
                c.setNombreTipoDocumento(rs.getString("tipo_doc_desc"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // 2. INSERTAR UN CLIENTE
    public boolean registrar(Cliente cliente) {
        String sql = "INSERT INTO cliente (tipo_documento_id, numero_identificacion, nombres, apellidos, direccion, telefono, correo_electronico) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getTipoDocumentoId());
            ps.setString(2, cliente.getNumeroIdentificacion());
            ps.setString(3, cliente.getNombres());
            ps.setString(4, cliente.getApellidos());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getTelefono());
            ps.setString(7, cliente.getCorreoElectronico());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    // 3. BUSCAR POR IDENTIFICACIÓN (CÉDULA / RUC)
    public Cliente buscarPorIdentificacion(String cedulaRuc) {
        String sql = "SELECT * FROM cliente WHERE numero_identificacion = ?";
        Cliente c = null;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cedulaRuc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setTipoDocumentoId(rs.getInt("tipo_documento_id"));
                    c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreoElectronico(rs.getString("correo_electronico"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return c;
    }
}
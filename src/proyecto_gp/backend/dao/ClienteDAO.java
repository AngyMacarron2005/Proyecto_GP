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

    public boolean insertar(Cliente cliente) {
        return registrar(cliente);
    }

    public boolean registrar(Cliente cliente) {
        String sql = "INSERT INTO cliente (tipo_documento_id, numero_identificacion, nombres, apellidos, direccion, telefono, correo_electronico) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

            if (con == null) return false;

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

    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET tipo_documento_id = ?, numero_identificacion = ?, nombres = ?, apellidos = ?, " +
                     "direccion = ?, telefono = ?, correo_electronico = ? WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

            if (con == null) return false;

            ps.setInt(1, cliente.getTipoDocumentoId());
            ps.setString(2, cliente.getNumeroIdentificacion());
            ps.setString(3, cliente.getNombres());
            ps.setString(4, cliente.getApellidos());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getTelefono());
            ps.setString(7, cliente.getCorreoElectronico());
            ps.setInt(8, cliente.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

            if (con == null) return false;

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    public List listarTodos() {
        return listar();
    }

    public List<Cliente> listar() {
    List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT c.*, t.descripcion AS tipo_doc_desc FROM cliente c " +
                     "INNER JOIN tipo_documento_identidad t ON c.tipo_documento_id = t.id";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con != null ? con.prepareStatement(sql) : null;
             ResultSet rs = ps != null ? ps.executeQuery() : null) {

            if (rs == null) return lista;

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setTipoDocumentoId(rs.getInt("tipo_documento_id"));
                c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
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

    public List buscarPorCriterio(String criterio) {
        List lista = new ArrayList<>();
        String sql = "SELECT c.*, t.descripcion AS tipo_doc_desc FROM cliente c " +
                     "INNER JOIN tipo_documento_identidad t ON c.tipo_documento_id = t.id " +
                     "WHERE c.numero_identificacion LIKE ? OR c.nombres LIKE ? OR c.apellidos LIKE ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

            if (con == null) return lista;

            String filtro = "%" + criterio + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setTipoDocumentoId(rs.getInt("tipo_documento_id"));
                    c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCorreoElectronico(rs.getString("correo_electronico"));
                    c.setNombreTipoDocumento(rs.getString("tipo_doc_desc"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return lista;
    }

    public Cliente buscarPorIdentificacion(String cedulaRuc) {
        String sql = "SELECT * FROM cliente WHERE numero_identificacion = ?";
        Cliente c = null;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {

            if (con == null) return null;

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
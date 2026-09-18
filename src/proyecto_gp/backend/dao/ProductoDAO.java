package proyecto_gp.backend.dao;

import proyecto_gp.backend.config.Conexion;
import proyecto_gp.backend.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO producto (codigo_principal, nombre, precio_unitario, stock_actual, stock_minimo, tarifa_iva_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getCodigoPrincipal());
            stmt.setString(2, producto.getNombre());
            stmt.setBigDecimal(3, producto.getPrecioUnitario());
            stmt.setBigDecimal(4, producto.getStockActual());
            stmt.setBigDecimal(5, producto.getStockMinimo());
            stmt.setInt(6, producto.getTarifaIvaId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET codigo_principal = ?, nombre = ?, precio_unitario = ?, "
                   + "stock_minimo = ?, tarifa_iva_id = ? WHERE id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getCodigoPrincipal());
            stmt.setString(2, producto.getNombre());
            stmt.setBigDecimal(3, producto.getPrecioUnitario());
            stmt.setBigDecimal(4, producto.getStockMinimo());
            stmt.setInt(5, producto.getTarifaIvaId());
            stmt.setInt(6, producto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo_principal, nombre, precio_unitario, stock_actual, stock_minimo, tarifa_iva_id FROM producto";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> buscarPorCriterio(String criterio) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo_principal, nombre, precio_unitario, stock_actual, stock_minimo, tarifa_iva_id "
                   + "FROM producto WHERE codigo_principal LIKE ? OR nombre LIKE ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String filtro = "%" + criterio + "%";
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> listarProductosBajoStock() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo_principal, nombre, precio_unitario, stock_actual, stock_minimo, tarifa_iva_id "
                   + "FROM producto WHERE stock_actual <= stock_minimo";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos bajo stock: " + e.getMessage());
        }
        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setCodigoPrincipal(rs.getString("codigo_principal"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        p.setStockActual(rs.getBigDecimal("stock_actual"));
        p.setStockMinimo(rs.getBigDecimal("stock_minimo"));
        p.setTarifaIvaId(rs.getInt("tarifa_iva_id"));
        return p;
    }

    public Producto buscarPorCodigo(String proD001) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
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
        String sql = "INSERT INTO producto (codigo_principal, nombre, precio_venta, stock_actual, tipo_impuesto_id) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {

            if (conn == null) return false;

            stmt.setString(1, producto.getCodigoPrincipal());
            stmt.setString(2, producto.getNombre());
            stmt.setBigDecimal(3, producto.getPrecioUnitario());
            stmt.setBigDecimal(4, producto.getStockActual());
            stmt.setInt(5, producto.getTarifaIvaId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean registrar(Producto producto) {
        return insertar(producto);
    }

    public boolean actualizar(Producto producto) {
        String sql = "UPDATE producto SET codigo_principal = ?, nombre = ?, precio_venta = ?, "
                   + "tipo_impuesto_id = ? WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {

            if (conn == null) return false;

            stmt.setString(1, producto.getCodigoPrincipal());
            stmt.setString(2, producto.getNombre());
            stmt.setBigDecimal(3, producto.getPrecioUnitario());
            stmt.setInt(4, producto.getTarifaIvaId());
            stmt.setInt(5, producto.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {

            if (conn == null) return false;

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> listarTodos() {
    List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo_principal, nombre, precio_venta, stock_actual, tipo_impuesto_id FROM producto";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null;
             ResultSet rs = stmt != null ? stmt.executeQuery() : null) {

            if (rs == null) return lista;

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> listar() {
    return listarTodos();
}
    public Producto buscarPorCodigo(String codigo) {

        String sql = "SELECT id, codigo_principal, nombre, precio_venta, "
                   + "stock_actual, tipo_impuesto_id "
                   + "FROM producto WHERE codigo_principal = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearProducto(rs);
                }

            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }

        return null;
    }
    public List<Producto> buscarPorCriterio(String criterio) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo_principal, nombre, precio_venta, stock_actual, tipo_impuesto_id "
                   + "FROM producto WHERE codigo_principal LIKE ? OR nombre LIKE ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null) {

            if (conn == null) return lista;

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

    public List listarProductosBajoStock() {
        List lista = new ArrayList<>();
        String sql = "SELECT id, codigo_principal, nombre, precio_venta, stock_actual, tipo_impuesto_id "
                   + "FROM producto WHERE stock_actual <= 5";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn != null ? conn.prepareStatement(sql) : null;
             ResultSet rs = stmt != null ? stmt.executeQuery() : null) {

            if (rs == null) return lista;

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
        p.setPrecioUnitario(rs.getBigDecimal("precio_venta"));
        p.setStockActual(rs.getBigDecimal("stock_actual"));
        p.setTarifaIvaId(rs.getInt("tipo_impuesto_id"));
        return p;
    }
}
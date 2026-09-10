
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

    // 1. LISTAR PRODUCTOS CON SU IMPUESTO ASOCIADO
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, ti.descripcion AS imp_desc, ti.porcentaje AS imp_porcentaje " +
                     "FROM producto p " +
                     "INNER JOIN tipo_impuesto ti ON p.tipo_impuesto_id = ti.id";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setCodigoPrincipal(rs.getString("codigo_principal"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                p.setStockActual(rs.getBigDecimal("stock_actual"));
                p.setTipoImpuestoId(rs.getInt("tipo_impuesto_id"));
                p.setDescripcionImpuesto(rs.getString("imp_desc"));
                p.setPorcentajeIva(rs.getBigDecimal("imp_porcentaje"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // 2. REGISTRAR UN NUEVO PRODUCTO
    public boolean registrar(Producto p) {
        String sql = "INSERT INTO producto (codigo_principal, nombre, precio_venta, stock_actual, tipo_impuesto_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoPrincipal());
            ps.setString(2, p.getNombre());
            ps.setBigDecimal(3, p.getPrecioVenta());
            ps.setBigDecimal(4, p.getStockActual());
            ps.setInt(5, p.getTipoImpuestoId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            return false;
        }
    }

    // 3. BUSCAR PRODUCTO POR CÓDIGO PRINCIPAL
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT p.*, ti.descripcion AS imp_desc, ti.porcentaje AS imp_porcentaje " +
                     "FROM producto p " +
                     "INNER JOIN tipo_impuesto ti ON p.tipo_impuesto_id = ti.id " +
                     "WHERE p.codigo_principal = ?";
        Producto p = null;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setCodigoPrincipal(rs.getString("codigo_principal"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                    p.setStockActual(rs.getBigDecimal("stock_actual"));
                    p.setTipoImpuestoId(rs.getInt("tipo_impuesto_id"));
                    p.setDescripcionImpuesto(rs.getString("imp_desc"));
                    p.setPorcentajeIva(rs.getBigDecimal("imp_porcentaje"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return p;
    }
}
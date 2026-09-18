package proyecto_gp.backend.dao;

import proyecto_gp.backend.config.Conexion;
import proyecto_gp.backend.model.CompraCabecera;
import proyecto_gp.backend.model.CompraDetalle;
import proyecto_gp.backend.config.Conexion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    /**
     * Registra la compra a un proveedor, sus detalles,
     * AUMENTA el stock de cada producto y registra el movimiento de ENTRADA en el Kardex.
     */
    public boolean registrarCompra(CompraCabecera compra) {
        // SQL según las columnas exactas de compra_cabecera
        String sqlCabecera = "INSERT INTO compra_cabecera (proveedor_id, numero_comprobante, fecha_compra, total_compra) " +
                             "VALUES (?, ?, ?, ?)";

        // SQL según las columnas exactas de compra_detalle (compra_id, producto_id, cantidad, costo_unitario)
        String sqlDetalle = "INSERT INTO compra_detalle (compra_id, producto_id, cantidad, costo_unitario) " +
                            "VALUES (?, ?, ?, ?)";

        // Incremento de stock en la tabla producto
        String sqlAumentarStock = "UPDATE producto SET stock_actual = stock_actual + ? WHERE id = ?";

        // Registro del movimiento de inventario (ENTRADA)
        String sqlMovInventario = "INSERT INTO movimiento_inventario (producto_id, tipo_movimiento, cantidad, origen, referencia_id) " +
                                  "VALUES (?, 'ENTRADA', ?, 'COMPRA', ?)";

        Connection con = null;
        PreparedStatement psCabecera = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        PreparedStatement psMov = null;
        ResultSet rsKeys = null;

        try {
            con = Conexion.conectar();
            if (con == null) return false;

            // 1. INICIAR TRANSACCIÓN ATÓMICA
            con.setAutoCommit(false);

            // 2. INSERTAR CABECERA DE COMPRA
            psCabecera = con.prepareStatement(sqlCabecera, Statement.RETURN_GENERATED_KEYS);
            psCabecera.setInt(1, compra.getProveedorId());
            psCabecera.setString(2, compra.getNumeroComprobante());
            psCabecera.setDate(3, Date.valueOf(compra.getFechaEmision().toLocalDate()));
            psCabecera.setBigDecimal(4, compra.getImporteTotal());

            int filasAfectadas = psCabecera.executeUpdate();
            if (filasAfectadas == 0) {
                con.rollback();
                return false;
            }

            // Obtener ID generado para la compra
            rsKeys = psCabecera.getGeneratedKeys();
            int idCompraGenerada = 0;
            if (rsKeys.next()) {
                idCompraGenerada = rsKeys.getInt(1);
            } else {
                con.rollback();
                return false;
            }

            psDetalle = con.prepareStatement(sqlDetalle);
            psStock = con.prepareStatement(sqlAumentarStock);
            psMov = con.prepareStatement(sqlMovInventario);

            // 3. REGISTRAR DETALLES, AUMENTAR STOCK Y REGISTRAR ENTRADA
            for (CompraDetalle d : compra.getDetalles()) {
                // a) Insertar detalle de compra
                psDetalle.setInt(1, idCompraGenerada);
                psDetalle.setInt(2, d.getProductoId());
                psDetalle.setBigDecimal(3, d.getCantidad());
                psDetalle.setBigDecimal(4, d.getPrecioCompraUnitario()); // Mapea a 'costo_unitario'
                psDetalle.executeUpdate();

                // b) Aumentar stock del producto
                psStock.setBigDecimal(1, d.getCantidad());
                psStock.setInt(2, d.getProductoId());
                psStock.executeUpdate();

                // c) Movimiento de inventario (ENTRADA)
                psMov.setInt(1, d.getProductoId());
                psMov.setBigDecimal(2, d.getCantidad());
                psMov.setInt(3, idCompraGenerada);
                psMov.executeUpdate();
            }

            // 4. CONFIRMAR TRANSACCIÓN
            con.commit();
            compra.setId(idCompraGenerada);
            return true;

        } catch (SQLException e) {
            System.err.println("Error en transacción de registro de compra: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                    System.err.println("Transacción revertida (ROLLBACK).");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psCabecera != null) psCabecera.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                if (psMov != null) psMov.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Consulta el historial de compras realizadas.
     */
    public List<CompraCabecera> listarCompras() {
        List<CompraCabecera> lista = new ArrayList<>();
        String sql = "SELECT c.*, p.razon_social, p.numero_identificacion FROM compra_cabecera c " +
                     "INNER JOIN proveedor p ON c.proveedor_id = p.id ORDER BY c.fecha_compra DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CompraCabecera c = new CompraCabecera();
                c.setId(rs.getInt("id"));
                c.setProveedorId(rs.getInt("proveedor_id"));
                c.setNumeroComprobante(rs.getString("numero_comprobante"));
                c.setFechaEmision(rs.getDate("fecha_compra").toLocalDate().atStartOfDay());
                c.setImporteTotal(rs.getBigDecimal("total_compra"));
                c.setRazonSocialProveedor(rs.getString("razon_social"));
                c.setRucProveedor(rs.getString("numero_identificacion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return lista;
    }
}
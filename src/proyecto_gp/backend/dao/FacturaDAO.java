
package proyecto_gp.backend.dao;

import proyecto_gp.backend.config.Conexion;
import proyecto_gp.backend.model.FacturaCabecera;
import proyecto_gp.backend.model.FacturaDetalle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class FacturaDAO {

    /**
     * Registra una factura completa (Cabecera + Detalles), descuenta stock y crea el movimiento de inventario.
     * Utiliza una transacción atómica JDBC para prevenir inconsistencias.
     */
    public boolean emitirFactura(FacturaCabecera factura) {
        String sqlCabecera = "INSERT INTO factura_cabecera (cliente_id, establecimiento, punto_emision, secuencial, " +
                             "clave_acceso, fecha_emision, subtotal_sin_impuestos, total_descuento, total_iva, " +
                             "importe_total, forma_pago_id, estado_sri) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlDetalle = "INSERT INTO factura_detalle (factura_id, producto_id, cantidad, precio_unitario, " +
                            "descuento, precio_total_sin_impuesto, valor_iva) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlDescontarStock = "UPDATE producto SET stock_actual = stock_actual - ? " +
                                   "WHERE id = ? AND stock_actual >= ?";

        String sqlMovInventario = "INSERT INTO movimiento_inventario (producto_id, tipo_movimiento, cantidad, origen, referencia_id) " +
                                  "VALUES (?, 'SALIDA', ?, 'FACTURA', ?)";

        Connection con = null;
        PreparedStatement psCabecera = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        PreparedStatement psMov = null;
        ResultSet rsKeys = null;

        try {
            con = Conexion.conectar();
            if (con == null) return false;

            // 1. INICIAR TRANSACCIÓN MANUAL
            con.setAutoCommit(false);

            // 2. INSERTAR CABECERA DE FACTURA
            psCabecera = con.prepareStatement(sqlCabecera, Statement.RETURN_GENERATED_KEYS);
            psCabecera.setInt(1, factura.getClienteId());
            psCabecera.setString(2, factura.getEstablecimiento());
            psCabecera.setString(3, factura.getPuntoEmision());
            psCabecera.setString(4, factura.getSecuencial());
            psCabecera.setString(5, factura.getClaveAcceso());
            psCabecera.setTimestamp(6, Timestamp.valueOf(factura.getFechaEmision()));
            psCabecera.setBigDecimal(7, factura.getSubtotalSinImpuestos());
            psCabecera.setBigDecimal(8, factura.getTotalDescuento());
            psCabecera.setBigDecimal(9, factura.getTotalIva());
            psCabecera.setBigDecimal(10, factura.getImporteTotal());
            psCabecera.setInt(11, factura.getFormaPagoId());
            psCabecera.setString(12, factura.getEstadoSri());

            int filasAfectadas = psCabecera.executeUpdate();
            if (filasAfectadas == 0) {
                con.rollback();
                return false;
            }

            // Obtener el ID autogenerado de la factura
            rsKeys = psCabecera.getGeneratedKeys();
            int idFacturaGenerada = 0;
            if (rsKeys.next()) {
                idFacturaGenerada = rsKeys.getInt(1);
            } else {
                con.rollback();
                return false;
            }

            // Prepare statements para los bucles de detalles
            psDetalle = con.prepareStatement(sqlDetalle);
            psStock = con.prepareStatement(sqlDescontarStock);
            psMov = con.prepareStatement(sqlMovInventario);

            // 3. PROCESAR CADA DETALLE Y REDUCIR STOCK
            for (FacturaDetalle d : factura.getDetalles()) {
                // a) Insertar Detalle
                psDetalle.setInt(1, idFacturaGenerada);
                psDetalle.setInt(2, d.getProductoId());
                psDetalle.setBigDecimal(3, d.getCantidad());
                psDetalle.setBigDecimal(4, d.getPrecioUnitario());
                psDetalle.setBigDecimal(5, d.getDescuento());
                psDetalle.setBigDecimal(6, d.getPrecioTotalSinImpuesto());
                psDetalle.setBigDecimal(7, d.getValorIva());
                psDetalle.executeUpdate();

                // b) Descontar Stock con validación de inventario suficiente
                psStock.setBigDecimal(1, d.getCantidad());
                psStock.setInt(2, d.getProductoId());
                psStock.setBigDecimal(3, d.getCantidad()); // stock_actual >= cantidad

                int stockActualizado = psStock.executeUpdate();
                if (stockActualizado == 0) {
                    System.err.println("Error: Stock insuficiente para el producto ID: " + d.getProductoId());
                    con.rollback(); // Cancela toda la venta si no hay stock
                    return false;
                }

                // c) Registrar Kardex / Movimiento de Inventario
                psMov.setInt(1, d.getProductoId());
                psMov.setBigDecimal(2, d.getCantidad());
                psMov.setInt(3, idFacturaGenerada);
                psMov.executeUpdate();
            }

            // 4. CONFIRMAR TRANSACCIÓN SI TODO SALIÓ BIEN
            con.commit();
            factura.setId(idFacturaGenerada);
            return true;

        } catch (SQLException e) {
            System.err.println("Error en transacción de emisión de factura: " + e.getMessage());
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
            // Restaurar auto-commit y cerrar recursos
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
     * Genera el siguiente número secuencial de factura para un punto de emisión.
     */
    public String obtenerSiguienteSecuencial(String establecimiento, String puntoEmision) {
        String sql = "SELECT MAX(CAST(secuencial AS UNSIGNED)) AS ultimo " +
                     "FROM factura_cabecera WHERE establecimiento = ? AND punto_emision = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, establecimiento);
            ps.setString(2, puntoEmision);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long ultimo = rs.getLong("ultimo");
                    return String.format("%09d", ultimo + 1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener secuencial: " + e.getMessage());
        }
        return "000000001";
    }
}
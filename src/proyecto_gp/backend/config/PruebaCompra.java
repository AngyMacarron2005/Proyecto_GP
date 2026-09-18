
package proyecto_gp.backend.config;

import proyecto_gp.backend.dao.CompraDAO;
import proyecto_gp.backend.dao.ProductoDAO;
import proyecto_gp.backend.model.CompraCabecera;
import proyecto_gp.backend.model.CompraDetalle;
import proyecto_gp.backend.model.Producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PruebaCompra {
    public static void main(String[] args) {
        CompraDAO compraDAO = new CompraDAO();
        ProductoDAO productoDAO = new ProductoDAO();

        // 1. Verificar stock actual del producto (Laptop HP - ID PROD-001)
        Producto prod = productoDAO.buscarPorCodigo("PROD-001");
        if (prod == null) {
            System.out.println("Crea un producto 'PROD-001' primero.");
            return;
        }

        System.out.println("Stock ANTES de la compra: " + prod.getStockActual());

        // 2. Preparar cabecera de compra a Proveedor ID 1
        CompraCabecera compra = new CompraCabecera();
        compra.setProveedorId(1); // CORPORACION ELJURI S.A.
        compra.setNumeroComprobante("001-002-000045123");
        compra.setFechaEmision(LocalDateTime.now());

        // 3. Crear detalle: Comprar 5 Laptops a costo de $600 c/u con IVA 15%
        BigDecimal cantidad = new BigDecimal("5.00");
        BigDecimal costoUnitario = new BigDecimal("600.00");
        BigDecimal subtotal = costoUnitario.multiply(cantidad); // $3000.00
        BigDecimal valorIva = subtotal.multiply(new BigDecimal("0.15")); // $450.00
        BigDecimal totalCompra = subtotal.add(valorIva); // $3450.00

        CompraDetalle detalle = new CompraDetalle(
                prod.getId(), cantidad, costoUnitario, subtotal, valorIva
        );
        compra.agregarDetalle(detalle);

        compra.setSubtotalSinImpuestos(subtotal);
        compra.setTotalIva(valorIva);
        compra.setImporteTotal(totalCompra);

        // 4. Registrar la compra y reabastecer stock
        if (compraDAO.registrarCompra(compra)) {
            System.out.println("\n¡COMPRA REGISTRADA Y STOCK INCREMENTADO!");
            System.out.println("ID Compra: " + compra.getId());
            System.out.println("Comprobante Proveedor: " + compra.getNumeroComprobante());
            System.out.println("Total Pagado: $" + compra.getImporteTotal());

            // Verificar stock incrementado
            Producto prodDespues = productoDAO.buscarPorCodigo("PROD-001");
            System.out.println("Stock DESPUÉS de la compra: " + prodDespues.getStockActual());
        } else {
            System.err.println("Falló el registro de la compra.");
        }
    }
}
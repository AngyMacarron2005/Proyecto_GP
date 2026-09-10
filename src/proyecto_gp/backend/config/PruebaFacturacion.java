
package proyecto_gp.backend.config;

import proyecto_gp.backend.dao.FacturaDAO;
import proyecto_gp.backend.dao.ProductoDAO;
import proyecto_gp.backend.model.FacturaCabecera;
import proyecto_gp.backend.model.FacturaDetalle;
import proyecto_gp.backend.model.Producto;
import proyecto_gp.backend.service.SriClaveAccesoService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaFacturacion {
    public static void main(String[] args) {
        FacturaDAO facturaDAO = new FacturaDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        SriClaveAccesoService sriService = new SriClaveAccesoService();

        // 1. Consultar el producto creado en pruebas anteriores (ID 1, Laptop)
        Producto prod = productoDAO.buscarPorCodigo("PROD-001");
        if (prod == null) {
            System.out.println("Debes ejecutar primero la prueba de Producto para tener al menos 1 producto en stock.");
            return;
        }

        System.out.println("Stock ANTES de facturar: " + prod.getStockActual());

        // 2. Preparar la cabecera de la factura
        String establecimiento = "001";
        String puntoEmision = "001";
        String secuencial = facturaDAO.obtenerSiguienteSecuencial(establecimiento, puntoEmision);

        FacturaCabecera factura = new FacturaCabecera();
        factura.setClienteId(1); // 1 = Consumidor Final
        factura.setEstablecimiento(establecimiento);
        factura.setPuntoEmision(puntoEmision);
        factura.setSecuencial(secuencial);
        factura.setFormaPagoId(1); // 1 = Efectivo

        // Generar clave de acceso de 49 dígitos
        String claveAcceso = sriService.generarClaveAcceso(
                LocalDate.now(), "01", "1792143820001", "1",
                establecimiento, puntoEmision, secuencial, null, "1"
        );
        factura.setClaveAcceso(claveAcceso);

        // 3. Crear 1 detalle: Comprar 2 Laptops @ $750 c/u con IVA 15%
        BigDecimal cantidad = new BigDecimal("2.00");
        BigDecimal precioUnitario = prod.getPrecioVenta(); // 750.00
        BigDecimal subtotal = precioUnitario.multiply(cantidad); // 1500.00
        BigDecimal valorIva = subtotal.multiply(new BigDecimal("0.15")); // 225.00
        BigDecimal totalFactura = subtotal.add(valorIva); // 1725.00

        FacturaDetalle detalle1 = new FacturaDetalle(
                prod.getId(), cantidad, precioUnitario, BigDecimal.ZERO, subtotal, valorIva
        );
        factura.agregarDetalle(detalle1);

        factura.setSubtotalSinImpuestos(subtotal);
        factura.setTotalDescuento(BigDecimal.ZERO);
        factura.setTotalIva(valorIva);
        factura.setImporteTotal(totalFactura);

        // 4. Emitir la factura
        if (facturaDAO.emitirFactura(factura)) {
            System.out.println("\n¡FACTURA EMITIDA CON ÉXITO!");
            System.out.println("ID Factura: " + factura.getId());
            System.out.println("Secuencial: " + establecimiento + "-" + puntoEmision + "-" + factura.getSecuencial());
            System.out.println("Clave de Acceso: " + factura.getClaveAcceso());
            System.out.println("Total a Pagar: $" + factura.getImporteTotal());

            // Verificar que el stock se descontó
            Producto prodDespues = productoDAO.buscarPorCodigo("PROD-001");
            System.out.println("Stock DESPUÉS de facturar: " + prodDespues.getStockActual());
        } else {
            System.err.println("Falló la emisión de la factura.");
        }
    }
}
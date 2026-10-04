package proyecto_gp.backend.controller;

import proyecto_gp.backend.dao.ClienteDAO;
import proyecto_gp.backend.dao.FacturaDAO;
import proyecto_gp.backend.dao.ProductoDAO;
import proyecto_gp.backend.model.FacturaCabecera;
import proyecto_gp.backend.model.FacturaDetalle;
import proyecto_gp.backend.model.Producto;
import proyecto_gp.backend.service.SriClaveAccesoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FacturaController {

    private final FacturaDAO facturaDAO;
    private final ClienteDAO clienteDAO;
    private final ProductoDAO productoDAO;
    private final SriClaveAccesoService sriService;

    // RUC del emisor configurado para pruebas
    private static final String RUC_EMISOR_DEFECTO = "1792143820001";

    public FacturaController() {
        this.facturaDAO = new FacturaDAO();
        this.clienteDAO = new ClienteDAO();
        this.productoDAO = new ProductoDAO();
        this.sriService = new SriClaveAccesoService();
    }

    /**
     * Emite una factura completa.
     *
     * Genera la clave de acceso SRI,
     * calcula subtotales e IVA,
     * verifica stock,
     * descuenta stock y
     * registra la factura en la base de datos.
     *
     * @return "OK:secuencial:claveAcceso" si todo sale correctamente.
     */
    public String emitirFactura(
            int clienteId,
            String establecimiento,
            String puntoEmision,
            int formaPagoId,
            List<ItemFacturaDTO> items) {

        // Validar cliente
        if (clienteId <= 0) {
            return "Debe seleccionar un cliente válido para la factura.";
        }

        // Validar detalles
        if (items == null || items.isEmpty()) {
            return "La factura debe contener al menos un producto en el detalle.";
        }

        BigDecimal subtotalGeneral = BigDecimal.ZERO;
        BigDecimal totalIvaGeneral = BigDecimal.ZERO;

        List<FacturaDetalle> detalles = new ArrayList<>();

        // ==========================================
        // 1. VALIDAR PRODUCTOS Y CALCULAR TOTALES
        // ==========================================

        for (ItemFacturaDTO item : items) {

            Producto prod = productoDAO.buscarPorCodigo(
                    item.getCodigoProducto()
            );

            if (prod == null) {
                return "El producto con código '"
                        + item.getCodigoProducto()
                        + "' no existe.";
            }

            if (prod.getStockActual().compareTo(item.getCantidad()) < 0) {
                return "Stock insuficiente para '"
                        + prod.getNombre()
                        + "'. Stock actual: "
                        + prod.getStockActual();
            }

            BigDecimal subtotalItem =
                    prod.getPrecioUnitario()
                       .multiply(item.getCantidad());

            BigDecimal ivaItem =
                    subtotalItem.multiply(new BigDecimal("0.15"));

            // Crear detalle
            FacturaDetalle det = new FacturaDetalle();

            det.setProductoId(prod.getId());
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(prod.getPrecioUnitario());
            det.setDescuento(BigDecimal.ZERO);
            det.setPrecioTotalSinImpuesto(subtotalItem);
            det.setValorIva(ivaItem);

            detalles.add(det);

            subtotalGeneral =
                    subtotalGeneral.add(subtotalItem);

            totalIvaGeneral =
                    totalIvaGeneral.add(ivaItem);
        }

        BigDecimal totalFactura =
                subtotalGeneral.add(totalIvaGeneral);

        // ==========================================
        // 2. OBTENER SIGUIENTE SECUENCIAL
        // ==========================================

        String secuencial =
                facturaDAO.obtenerSiguienteSecuencial(
                        establecimiento,
                        puntoEmision
                );

        // ==========================================
        // 3. GENERAR CLAVE DE ACCESO SRI
        // ==========================================

        String claveAcceso =
                sriService.generarClaveAcceso(
                        LocalDate.now(),
                        "01",
                        RUC_EMISOR_DEFECTO,
                        "1",
                        establecimiento,
                        puntoEmision,
                        secuencial,
                        null,
                        "1"
                );

        // ==========================================
        // 4. CREAR CABECERA DE FACTURA
        // ==========================================

        FacturaCabecera factura = new FacturaCabecera();

        factura.setClienteId(clienteId);
        factura.setEstablecimiento(establecimiento);
        factura.setPuntoEmision(puntoEmision);
        factura.setSecuencial(secuencial);
        factura.setClaveAcceso(claveAcceso);
        factura.setFormaPagoId(formaPagoId);

        factura.setSubtotalSinImpuestos(subtotalGeneral);
        factura.setTotalDescuento(BigDecimal.ZERO);
        factura.setTotalIva(totalIvaGeneral);
        factura.setImporteTotal(totalFactura);

        factura.setEstadoSri("PENDIENTE");

        // ==========================================
        // 5. AGREGAR DETALLES
        // ==========================================

        for (FacturaDetalle d : detalles) {
            factura.agregarDetalle(d);
        }

        // ==========================================
        // 6. GUARDAR EN BASE DE DATOS
        // ==========================================

        boolean procesado =
                facturaDAO.emitirFactura(factura);

        if (procesado) {
            return "OK:"
                    + secuencial
                    + ":"
                    + claveAcceso;
        }

        return "Ocurrió un error al registrar "
                + "la factura en la base de datos.";
    }

    /**
     * DTO utilizado para representar un producto
     * dentro del detalle de una factura.
     */
    public static class ItemFacturaDTO {

        private String codigoProducto;
        private BigDecimal cantidad;

        public ItemFacturaDTO(
                String codigoProducto,
                BigDecimal cantidad) {

            this.codigoProducto = codigoProducto;
            this.cantidad = cantidad;
        }

        public String getCodigoProducto() {
            return codigoProducto;
        }

        public BigDecimal getCantidad() {
            return cantidad;
        }
    }
}
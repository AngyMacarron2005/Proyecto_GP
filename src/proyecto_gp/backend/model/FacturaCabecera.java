
package proyecto_gp.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FacturaCabecera {
    private int id;
    private int clienteId;
    private String establecimiento;
    private String puntoEmision;
    private String secuencial;
    private String claveAcceso;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotalSinImpuestos;
    private BigDecimal totalDescuento;
    private BigDecimal totalIva;
    private BigDecimal importeTotal;
    private int formaPagoId;
    private String estadoSri;

    // Lista de ítems o detalles
    private List<FacturaDetalle> detalles;

    public FacturaCabecera() {
        this.detalles = new ArrayList<>();
        this.fechaEmision = LocalDateTime.now();
        this.totalDescuento = BigDecimal.ZERO;
        this.estadoSri = "PENDIENTE";
    }

    public void agregarDetalle(FacturaDetalle detalle) {
        this.detalles.add(detalle);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getEstablecimiento() { return establecimiento; }
    public void setEstablecimiento(String establecimiento) { this.establecimiento = establecimiento; }

    public String getPuntoEmision() { return puntoEmision; }
    public void setPuntoEmision(String puntoEmision) { this.puntoEmision = puntoEmision; }

    public String getSecuencial() { return secuencial; }
    public void setSecuencial(String secuencial) { this.secuencial = secuencial; }

    public String getClaveAcceso() { return claveAcceso; }
    public void setClaveAcceso(String claveAcceso) { this.claveAcceso = claveAcceso; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public BigDecimal getSubtotalSinImpuestos() { return subtotalSinImpuestos; }
    public void setSubtotalSinImpuestos(BigDecimal subtotalSinImpuestos) { this.subtotalSinImpuestos = subtotalSinImpuestos; }

    public BigDecimal getTotalDescuento() { return totalDescuento; }
    public void setTotalDescuento(BigDecimal totalDescuento) { this.totalDescuento = totalDescuento; }

    public BigDecimal getTotalIva() { return totalIva; }
    public void setTotalIva(BigDecimal totalIva) { this.totalIva = totalIva; }

    public BigDecimal getImporteTotal() { return importeTotal; }
    public void setImporteTotal(BigDecimal importeTotal) { this.importeTotal = importeTotal; }

    public int getFormaPagoId() { return formaPagoId; }
    public void setFormaPagoId(int formaPagoId) { this.formaPagoId = formaPagoId; }

    public String getEstadoSri() { return estadoSri; }
    public void setEstadoSri(String estadoSri) { this.estadoSri = estadoSri; }

    public List<FacturaDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<FacturaDetalle> detalles) { this.detalles = detalles; }
}
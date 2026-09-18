
package proyecto_gp.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompraCabecera {
    private int id;
    private int proveedorId;
    private String numeroComprobante;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotalSinImpuestos;
    private BigDecimal totalIva;
    private BigDecimal importeTotal;

    // Campos auxiliares para la interfaz
    private String razonSocialProveedor;
    private String rucProveedor;

    // IMPORTANTE: Especificar <CompraDetalle> en la lista
    private List<CompraDetalle> detalles;

    public CompraCabecera() {
        this.detalles = new ArrayList<>();
        this.fechaEmision = LocalDateTime.now();
    }

    public void agregarDetalle(CompraDetalle detalle) {
        this.detalles.add(detalle);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProveedorId() { return proveedorId; }
    public void setProveedorId(int proveedorId) { this.proveedorId = proveedorId; }

    public String getNumeroComprobante() { return numeroComprobante; }
    public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public BigDecimal getSubtotalSinImpuestos() { return subtotalSinImpuestos; }
    public void setSubtotalSinImpuestos(BigDecimal subtotalSinImpuestos) { this.subtotalSinImpuestos = subtotalSinImpuestos; }

    public BigDecimal getTotalIva() { return totalIva; }
    public void setTotalIva(BigDecimal totalIva) { this.totalIva = totalIva; }

    public BigDecimal getImporteTotal() { return importeTotal; }
    public void setImporteTotal(BigDecimal importeTotal) { this.importeTotal = importeTotal; }

    public String getRazonSocialProveedor() { return razonSocialProveedor; }
    public void setRazonSocialProveedor(String razonSocialProveedor) { this.razonSocialProveedor = razonSocialProveedor; }

    public String getRucProveedor() { return rucProveedor; }
    public void setRucProveedor(String rucProveedor) { this.rucProveedor = rucProveedor; }

    // IMPORTANTE: El tipo de retorno DEBE ser List<CompraDetalle>
    public List<CompraDetalle> getDetalles() { 
        return detalles; 
    }

    public void setDetalles(List<CompraDetalle> detalles) { 
        this.detalles = detalles; 
    }
}
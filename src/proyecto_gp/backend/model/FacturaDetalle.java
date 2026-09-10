
package proyecto_gp.backend.model;

import java.math.BigDecimal;

public class FacturaDetalle {
    private int id;
    private int facturaId;
    private int productoId;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal precioTotalSinImpuesto;
    private BigDecimal valorIva;

    // Campo auxiliar para vistas
    private String nombreProducto;

    public FacturaDetalle() {
        this.descuento = BigDecimal.ZERO;
    }

    public FacturaDetalle(int productoId, BigDecimal cantidad, BigDecimal precioUnitario, 
                          BigDecimal descuento, BigDecimal precioTotalSinImpuesto, BigDecimal valorIva) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento != null ? descuento : BigDecimal.ZERO;
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
        this.valorIva = valorIva;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFacturaId() { return facturaId; }
    public void setFacturaId(int facturaId) { this.facturaId = facturaId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getPrecioTotalSinImpuesto() { return precioTotalSinImpuesto; }
    public void setPrecioTotalSinImpuesto(BigDecimal precioTotalSinImpuesto) { this.precioTotalSinImpuesto = precioTotalSinImpuesto; }

    public BigDecimal getValorIva() { return valorIva; }
    public void setValorIva(BigDecimal valorIva) { this.valorIva = valorIva; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}
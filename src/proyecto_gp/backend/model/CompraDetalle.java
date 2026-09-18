
package proyecto_gp.backend.model;

import java.math.BigDecimal;

public class CompraDetalle {
    private int id;
    private int compraId;
    private int productoId;
    private BigDecimal cantidad;
    private BigDecimal precioCompraUnitario;
    private BigDecimal precioTotalSinImpuesto;
    private BigDecimal valorIva;

    // Campo auxiliar para vistas y tablas de la interfaz
    private String nombreProducto;

    public CompraDetalle() {}

    public CompraDetalle(int productoId, BigDecimal cantidad, BigDecimal precioCompraUnitario, 
                         BigDecimal precioTotalSinImpuesto, BigDecimal valorIva) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioCompraUnitario = precioCompraUnitario;
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
        this.valorIva = valorIva;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCompraId() { return compraId; }
    public void setCompraId(int compraId) { this.compraId = compraId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioCompraUnitario() { return precioCompraUnitario; }
    public void setPrecioCompraUnitario(BigDecimal precioCompraUnitario) { this.precioCompraUnitario = precioCompraUnitario; }

    public BigDecimal getPrecioTotalSinImpuesto() { return precioTotalSinImpuesto; }
    public void setPrecioTotalSinImpuesto(BigDecimal precioTotalSinImpuesto) { this.precioTotalSinImpuesto = precioTotalSinImpuesto; }

    public BigDecimal getValorIva() { return valorIva; }
    public void setValorIva(BigDecimal valorIva) { this.valorIva = valorIva; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}
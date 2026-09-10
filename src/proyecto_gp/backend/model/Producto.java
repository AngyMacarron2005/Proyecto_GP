
package proyecto_gp.backend.model;

import java.math.BigDecimal;

public class Producto {
    private int id;
    private String codigoPrincipal;
    private String nombre;
    private BigDecimal precioVenta;
    private BigDecimal stockActual;
    private int tipoImpuestoId;

    // Campos auxiliares para vistas y cálculos
    private String descripcionImpuesto;
    private BigDecimal porcentajeIva;

    public Producto() {}

    public Producto(int id, String codigoPrincipal, String nombre, BigDecimal precioVenta, BigDecimal stockActual, int tipoImpuestoId) {
        this.id = id;
        this.codigoPrincipal = codigoPrincipal;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.tipoImpuestoId = tipoImpuestoId;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoPrincipal() { return codigoPrincipal; }
    public void setCodigoPrincipal(String codigoPrincipal) { this.codigoPrincipal = codigoPrincipal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public BigDecimal getStockActual() { return stockActual; }
    public void setStockActual(BigDecimal stockActual) { this.stockActual = stockActual; }

    public int getTipoImpuestoId() { return tipoImpuestoId; }
    public void setTipoImpuestoId(int tipoImpuestoId) { this.tipoImpuestoId = tipoImpuestoId; }

    public String getDescripcionImpuesto() { return descripcionImpuesto; }
    public void setDescripcionImpuesto(String descripcionImpuesto) { this.descripcionImpuesto = descripcionImpuesto; }

    public BigDecimal getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(BigDecimal porcentajeIva) { this.porcentajeIva = porcentajeIva; }
}
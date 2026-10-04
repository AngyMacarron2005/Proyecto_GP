package proyecto_gp.backend.model;

import java.math.BigDecimal;

public class Producto {

    private int id;
    private String codigoPrincipal;
    private String nombre;
    private BigDecimal precioUnitario;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo = BigDecimal.ZERO;
    private int tarifaIvaId;

    public Producto() {
    }

    public Producto(int id, String codigoPrincipal, String nombre, BigDecimal precioUnitario, 
                    BigDecimal stockActual, BigDecimal stockMinimo, int tarifaIvaId) {
        this.id = id;
        this.codigoPrincipal = codigoPrincipal;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.tarifaIvaId = tarifaIvaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoPrincipal() { return codigoPrincipal; }
    public void setCodigoPrincipal(String codigoPrincipal) { this.codigoPrincipal = codigoPrincipal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    // Métodos alias para pruebas antiguas
    public BigDecimal getPrecioVenta() { return precioUnitario; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioUnitario = precioVenta; }

    public BigDecimal getStockActual() { return stockActual; }
    public void setStockActual(BigDecimal stockActual) { this.stockActual = stockActual; }

    public BigDecimal getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(BigDecimal stockMinimo) { this.stockMinimo = stockMinimo; }

    public int getTarifaIvaId() { return tarifaIvaId; }
    public void setTarifaIvaId(int tarifaIvaId) { this.tarifaIvaId = tarifaIvaId; }

    // Métodos alias para pruebas antiguas
    public int getTipoImpuestoId() { return tarifaIvaId; }
    public void setTipoImpuestoId(int tipoImpuestoId) { this.tarifaIvaId = tipoImpuestoId; }

    public String getDescripcionImpuesto() { return "IVA Tarifa " + tarifaIvaId; }
}
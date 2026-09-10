
package proyecto_gp.backend.model;

import java.math.BigDecimal;

public class TipoImpuesto {
    private int id;
    private String codigoSri;
    private BigDecimal porcentaje;
    private String descripcion;

    public TipoImpuesto() {}

    public TipoImpuesto(int id, String codigoSri, BigDecimal porcentaje, String descripcion) {
        this.id = id;
        this.codigoSri = codigoSri;
        this.porcentaje = porcentaje;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoSri() { return codigoSri; }
    public void setCodigoSri(String codigoSri) { this.codigoSri = codigoSri; }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return descripcion; // Útil para despliegue en combos de la UI
    }
}
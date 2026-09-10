
package proyecto_gp.backend.model;

public class FormaPagoSRI {
    private int id;
    private String codigoSri;
    private String descripcion;

    public FormaPagoSRI() {}

    public FormaPagoSRI(int id, String codigoSri, String descripcion) {
        this.id = id;
        this.codigoSri = codigoSri;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoSri() { return codigoSri; }
    public void setCodigoSri(String codigoSri) { this.codigoSri = codigoSri; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return codigoSri + " - " + descripcion;
    }
}

package proyecto_gp.backend.model;

public class Proveedor {
    private int id;
    private int tipoDocumentoId;
    private String numeroIdentificacion;
    private String razonSocial;
    private String nombreComercial;
    private String direccion;
    private String telefono;
    private String correoElectronico;

    // Auxiliar para mostrar la descripción del documento en la UI
    private String nombreTipoDocumento;

    public Proveedor() {}

    public Proveedor(int id, int tipoDocumentoId, String numeroIdentificacion, String razonSocial, 
                     String nombreComercial, String direccion, String telefono, String correoElectronico) {
        this.id = id;
        this.tipoDocumentoId = tipoDocumentoId;
        this.numeroIdentificacion = numeroIdentificacion;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTipoDocumentoId() { return tipoDocumentoId; }
    public void setTipoDocumentoId(int tipoDocumentoId) { this.tipoDocumentoId = tipoDocumentoId; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getNombreTipoDocumento() { return nombreTipoDocumento; }
    public void setNombreTipoDocumento(String nombreTipoDocumento) { this.nombreTipoDocumento = nombreTipoDocumento; }
}
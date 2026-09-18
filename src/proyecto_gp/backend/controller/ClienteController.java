package proyecto_gp.backend.controller;

import proyecto_gp.backend.dao.ClienteDAO;
import proyecto_gp.backend.model.Cliente;
import proyecto_gp.backend.service.ValidadorIdentificacionService;

import java.util.List;

public class ClienteController {

    private final ClienteDAO clienteDAO;
    private final ValidadorIdentificacionService validadorService;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
        this.validadorService = new ValidadorIdentificacionService();
    }

    /**
     * Valida la información del cliente y lo registra en la base de datos.
     * @return Mensaje de resultado (éxito o descripción del error de validación)
     */
    public String guardarCliente(int tipoDocumentoId, String numeroIdentificacion, 
                                 String nombres, String apellidos, 
                                 String direccion, String telefono, String correo) {
        
        // 1. Validar campos obligatorios
        if (nombres == null || nombres.trim().isEmpty()) {
            return "El nombre del cliente es obligatorio.";
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return "El apellido del cliente es obligatorio.";
        }
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            return "El número de identificación es obligatorio.";
        }

        // 2. Validar Cédula o RUC según el tipo de documento
        String errorValidacion = validarIdentificacion(tipoDocumentoId, numeroIdentificacion.trim());
        if (errorValidacion != null) {
            return errorValidacion;
        }

        // 3. Validar correo electrónico si fue ingresado
        if (correo != null && !correo.trim().isEmpty() && !esCorreoValido(correo.trim())) {
            return "El formato del correo electrónico no es válido.";
        }

        // 4. Crear el modelo y persistir
        Cliente cliente = new Cliente();
        cliente.setTipoDocumentoId(tipoDocumentoId);
        cliente.setNumeroIdentificacion(numeroIdentificacion.trim());
        cliente.setNombres(nombres.trim().toUpperCase());
        cliente.setApellidos(apellidos.trim().toUpperCase());
        cliente.setDireccion(direccion != null ? direccion.trim() : "");
        cliente.setTelefono(telefono != null ? telefono.trim() : "");
        cliente.setCorreoElectronico(correo != null ? correo.trim().toLowerCase() : "");

        boolean registrado = clienteDAO.insertar(cliente);
        if (registrado) {
            return "OK";
        } else {
            return "No se pudo registrar el cliente. Es posible que el RUC/Cédula ya exista en el sistema.";
        }
    }

    /**
     * Actualiza la información de un cliente existente.
     */
    public String actualizarCliente(int id, int tipoDocumentoId, String numeroIdentificacion, 
                                    String nombres, String apellidos, 
                                    String direccion, String telefono, String correo) {
        
        if (id <= 0) {
            return "ID de cliente no válido.";
        }
        if (nombres == null || nombres.trim().isEmpty()) {
            return "El nombre del cliente es obligatorio.";
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return "El apellido del cliente es obligatorio.";
        }

        String errorValidacion = validarIdentificacion(tipoDocumentoId, numeroIdentificacion.trim());
        if (errorValidacion != null) {
            return errorValidacion;
        }

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setTipoDocumentoId(tipoDocumentoId);
        cliente.setNumeroIdentificacion(numeroIdentificacion.trim());
        cliente.setNombres(nombres.trim().toUpperCase());
        cliente.setApellidos(apellidos.trim().toUpperCase());
        cliente.setDireccion(direccion != null ? direccion.trim() : "");
        cliente.setTelefono(telefono != null ? telefono.trim() : "");
        cliente.setCorreoElectronico(correo != null ? correo.trim().toLowerCase() : "");

        boolean actualizado = clienteDAO.actualizar(cliente);
        if (actualizado) {
            return "OK";
        } else {
            return "Error al actualizar la información del cliente.";
        }
    }

    /**
     * Elimina un cliente por su ID.
     */
    public String eliminarCliente(int id) {
        if (id <= 0) {
            return "Seleccione un cliente válido para eliminar.";
        }
        boolean eliminado = clienteDAO.eliminar(id);
        if (eliminado) {
            return "OK";
        } else {
            return "No se puede eliminar el cliente. Verifique que no tenga facturas asociadas.";
        }
    }

    /**
     * Obtiene la lista completa de clientes.
     */
    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    /**
     * Busca clientes por número de cédula/RUC o nombre.
     */
    public List<Cliente> buscarClientes(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarClientes();
        }
        return clienteDAO.buscarPorCriterio(criterio.trim());
    }

    // --- MÉTODOS DE VALIDACIÓN INTERNA ---

    private String validarIdentificacion(int tipoDocId, String numero) {
        switch (tipoDocId) {
            case 1: // RUC
                if (!validadorService.validarRuc(numero)) {
                    return "El RUC ingresado no es válido para Ecuador.";
                }
                break;
            case 2: // CÉDULA
                if (!validadorService.validarCedula(numero)) {
                    return "La Cédula ingresada no es válida para Ecuador.";
                }
                break;
            case 3: // PASAPORTE
                if (numero.length() < 3 || numero.length() > 20) {
                    return "El pasaporte debe tener entre 3 y 20 caracteres.";
                }
                break;
            case 4: // CONSUMIDOR FINAL
                if (!numero.equals("9999999999999")) {
                    return "La identificación para Consumidor Final debe ser 9999999999999.";
                }
                break;
            default:
                break;
        }
        return null; // Sin errores
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return correo.matches(regex);
    }
}
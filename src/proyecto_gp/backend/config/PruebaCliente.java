
package proyecto_gp.backend.config;

import proyecto_gp.backend.dao.ClienteDAO;
import proyecto_gp.backend.model.Cliente;
import java.util.List;

public class PruebaCliente {
    public static void main(String[] args) {
        ClienteDAO dao = new ClienteDAO();

        // 1. Probar registro de cliente
        Cliente nuevo = new Cliente();
        nuevo.setTipoDocumentoId(2); // 2 = CÉDULA
        nuevo.setNumeroIdentificacion("1712345678");
        nuevo.setNombres("Juan");
        nuevo.setApellidos("Pérez");
        nuevo.setDireccion("Av. Amazonas y Colón");
        nuevo.setTelefono("0991234566");
        nuevo.setCorreoElectronico("juan.perez@example.com");

        if (dao.registrar(nuevo)) {
            System.out.println("Cliente registrado exitosamente.");
        }

        // 2. Probar listar clientes
        List<Cliente> lista = dao.listar();
        System.out.println("\n--- LISTA DE CLIENTES ---");
        for (Cliente c : lista) {
            System.out.println(c.getId() + " - " + c.getNombres() + " " + c.getApellidos() + 
                               " | Doc: " + c.getNumeroIdentificacion() + " (" + c.getNombreTipoDocumento() + ")");
        }
    }
}
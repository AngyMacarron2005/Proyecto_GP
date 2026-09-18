
package proyecto_gp.backend.config;

import proyecto_gp.backend.controller.ClienteController;

public class PruebaClienteController {
    public static void main(String[] args) {
        ClienteController controller = new ClienteController();

        // 1. Probar con Cédula INVÁLIDA
        String resp1 = controller.guardarCliente(2, "0103889390", "JUAN", "PEREZ", "CUENCA", "0999999999", "juan@test.com");
        System.out.println("Prueba Cédula Inválida: " + resp1);

        // 2. Probar con Cédula VÁLIDA (Ejemplo Cédula Azuay)
        String resp2 = controller.guardarCliente(2, "0103889391", "CARLOS", "ANDRADE", "CUENCA", "0987654321", "carlos@gmail.com");
        System.out.println("Prueba Cédula Válida: " + resp2);
    }
}
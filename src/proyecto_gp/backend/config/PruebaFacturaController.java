package proyecto_gp.backend.config;

import proyecto_gp.backend.controller.FacturaController;
import proyecto_gp.backend.controller.FacturaController.ItemFacturaDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PruebaFacturaController {

    public static void main(String[] args) {
        FacturaController controller = new FacturaController();

        // 1. Crear detalle de compra (2 teclados mecánicos)
        List items = new ArrayList<>();
        items.add(new ItemFacturaDTO("PROD-TEST01", new BigDecimal("2.00")));

        // 2. Intentar emitir la factura para Cliente ID 1 (Consumidor Final / registrado)
        String resultado = controller.emitirFactura(1, "001", "001", 1, items);

        if (resultado.startsWith("OK")) {
            String[] partes = resultado.split(":");
            System.out.println("\n¡FACTURA EMITIDA EXITOSAMENTE VIA CONTROLLER!");
            System.out.println("Secuencial Generado: 001-001-" + partes[1]);
            System.out.println("Clave de Acceso SRI: " + partes[2]);
        } else {
            System.err.println("Error al emitir factura: " + resultado);
        }
    }
}

package proyecto_gp.backend.config;

import proyecto_gp.backend.service.SriClaveAccesoService;

import java.time.LocalDate;

public class PruebaClaveAcceso {
    public static void main(String[] args) {
        SriClaveAccesoService sriService = new SriClaveAccesoService();

        LocalDate fecha = LocalDate.now(); // Fecha actual
        String tipoComprobante = "01";      // Factura
        String rucEmisor = "1792143820001"; // RUC de prueba
        String ambiente = "1";              // 1 = Pruebas
        String establecimiento = "001";     // Matriz
        String puntoEmision = "001";        // Caja 1
        String secuencial = "1";            // Factura #1
        String codigoNumerico = "12345678"; // O null para aleatorio
        String tipoEmision = "1";           // Normal

        String claveAcceso = sriService.generarClaveAcceso(
                fecha, tipoComprobante, rucEmisor, ambiente,
                establecimiento, puntoEmision, secuencial,
                codigoNumerico, tipoEmision
        );

        System.out.println("--- PRUEBA CLAVE DE ACCESO SRI ---");
        System.out.println("Clave Generada (49 dígitos): " + claveAcceso);
        System.out.println("Longitud: " + claveAcceso.length() + " dígitos");
        System.out.println("Dígito Verificador: " + claveAcceso.charAt(48));
        System.out.println("¿Es válida?: " + sriService.validarClaveAcceso(claveAcceso));
    }
}
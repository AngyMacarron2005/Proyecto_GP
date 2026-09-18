
package proyecto_gp.backend.config;

import proyecto_gp.backend.service.ValidadorIdentificacionService;

public class PruebaValidador {
    public static void main(String[] args) {
        ValidadorIdentificacionService validador = new ValidadorIdentificacionService();

        // Ejemplos de prueba
        String cedulaValida = "1712345678"; // Reemplazar por una cédula real de prueba si gustas
        String rucJuridicoValido = "1792143820001";
        String consumidorFinal = "9999999999999";
        String cedulaInvalida = "1712345679";

        System.out.println("--- PRUEBAS DE VALIDACIÓN ---");
        System.out.println("Cédula: " + cedulaValida + " -> ¿Válida?: " + validador.validarIdentificacion("CEDULA", cedulaValida));
        System.out.println("RUC Jurídico: " + rucJuridicoValido + " -> ¿Válida?: " + validador.validarIdentificacion("RUC", rucJuridicoValido));
        System.out.println("Consumidor Final: " + consumidorFinal + " -> ¿Válida?: " + validador.validarIdentificacion("RUC", consumidorFinal));
        System.out.println("Cédula Inválida: " + cedulaInvalida + " -> ¿Válida?: " + validador.validarIdentificacion("CEDULA", cedulaInvalida));
    }
}
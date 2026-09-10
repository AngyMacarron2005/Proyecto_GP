
package proyecto_gp.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SriClaveAccesoService {

    /**
     * Genera la Clave de Acceso completa de 49 dígitos según la normativa del SRI Ecuador.
     * @param fechaEmision
     * @param tipoComprobante
     * @param rucEmisor
     * @param ambiente
     * @param establecimiento
     * @param puntoEmision
     * @param secuencial
     * @param codigoNumerico
     * @param tipoEmision
     * @return 
     */
    public String generarClaveAcceso(
            LocalDate fechaEmision,
            String tipoComprobante,
            String rucEmisor,
            String ambiente,
            String establecimiento,
            String puntoEmision,
            String secuencial,
            String codigoNumerico,
            String tipoEmision
    ) {
        // 1. Formatear Fecha (ddMMyyyy -> 8 dígitos)
        String fechaStr = fechaEmision.format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        // 2. Normalizar la longitud de los campos con relleno de ceros
        tipoComprobante = String.format("%02d", Integer.parseInt(tipoComprobante));
        establecimiento = String.format("%03d", Integer.parseInt(establecimiento));
        puntoEmision = String.format("%03d", Integer.parseInt(puntoEmision));
        secuencial = String.format("%09d", Long.parseLong(secuencial));

        String serie = establecimiento + puntoEmision; // 6 dígitos

        // 3. Generar Código Numérico de 8 dígitos si no fue provisto
        if (codigoNumerico == null || codigoNumerico.trim().length() != 8) {
            codigoNumerico = generarCodigoNumerico();
        }

        // 4. Concatenar los primeros 48 dígitos
        StringBuilder clave48 = new StringBuilder();
        clave48.append(fechaStr)          // Pos 1-8
               .append(tipoComprobante)  // Pos 9-10
               .append(rucEmisor)        // Pos 11-23
               .append(ambiente)         // Pos 24
               .append(serie)            // Pos 25-30
               .append(secuencial)       // Pos 31-39
               .append(codigoNumerico)   // Pos 40-47
               .append(tipoEmision);     // Pos 48

        if (clave48.length() != 48) {
            throw new IllegalArgumentException("La cadena de la clave debe tener exactamente 48 dígitos. Actual: " + clave48.length());
        }

        // 5. Calcular el dígito verificador utilizando Módulo 11
        int digitoVerificador = calcularModulo11(clave48.toString());

        // 6. Retornar la clave de acceso de 49 dígitos
        return clave48.toString() + digitoVerificador;
    }

    /**
     * Algoritmo Módulo 11 normado por el SRI de Ecuador.
     * Recorre la cadena de 48 dígitos de derecha a izquierda aplicando factores del 2 al 7.
     * @param cadena48
     * @return 
     */
    public int calcularModulo11(String cadena48) {
        int suma = 0;
        int factor = 2;

        // Recorrido de derecha a izquierda
        for (int i = cadena48.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(cadena48.charAt(i));
            suma += digito * factor;

            factor++;
            if (factor > 7) {
                factor = 2; // Reinicia el ciclo de factores a 2
            }
        }

        int residuo = suma % 11;
        int digitoVerificador = 11 - residuo;

        return switch (digitoVerificador) {
            case 11 -> 0;
            case 10 -> 1;
            default -> digitoVerificador;
        };
    }

    /**
     * Genera un número aleatorio de 8 dígitos.
     * @return 
     */
    public String generarCodigoNumerico() {
        Random random = new Random();
        int numero = 10000000 + random.nextInt(90000000);
        return String.valueOf(numero);
    }

    /**
     * Valida si una clave de acceso de 49 dígitos es matemáticamente válida.
     * @param clave49
     * @return 
     */
    public boolean validarClaveAcceso(String clave49) {
        if (clave49 == null || clave49.length() != 49 || !clave49.matches("\\d+")) {
            return false;
        }

        String cadena48 = clave49.substring(0, 48);
        int digitoEsperado = Character.getNumericValue(clave49.charAt(48));
        int digitoCalculado = calcularModulo11(cadena48);

        return digitoEsperado == digitoCalculado;
    }
}
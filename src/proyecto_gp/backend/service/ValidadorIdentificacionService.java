
package proyecto_gp.backend.service;

public class ValidadorIdentificacionService {

    private static final int NUMERO_PROVINCIAS = 24;

    /**
     * Valida Cédula o RUC ecuatoriano según el tipo de documento.
     * @param tipoDocumento "CEDULA", "RUC", o "CONSUMIDOR_FINAL"
     * @param identificacion Número de identificación a validar
     */
    public boolean validarIdentificacion(String tipoDocumento, String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            return false;
        }

        identificacion = identificacion.trim();

        if ("CONSUMIDOR_FINAL".equalsIgnoreCase(tipoDocumento) || "9999999999999".equals(identificacion)) {
            return true;
        }

        if (!identificacion.matches("\\d+")) {
            return false; // Debe contener solo dígitos numéricos
        }

        switch (tipoDocumento.toUpperCase()) {
            case "CEDULA":
                return validarCedula(identificacion);
            case "RUC":
                return validarRuc(identificacion);
            case "PASAPORTE":
                return identificacion.length() >= 5 && identificacion.length() <= 20;
            default:
                return false;
        }
    }

    /**
     * Valida una Cédula de Identidad de 10 dígitos.
     */
    public boolean validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if ((provincia < 1 || provincia > NUMERO_PROVINCIAS) && provincia != 30) {
            return false; // Código de provincia inválido
        }

        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito >= 6) {
            return false; // Para personas naturales, el 3er dígito debe ser menor a 6
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            if (valor >= 10) {
                valor -= 9;
            }
            suma += valor;
        }

        int digitoVerificadorEsperado = (suma % 10 == 0) ? 0 : (10 - (suma % 10));
        int digitoVerificadorObtenido = Character.getNumericValue(cedula.charAt(9));

        return digitoVerificadorEsperado == digitoVerificadorObtenido;
    }

    /**
     * Valida un RUC de 13 dígitos (Persona Natural, Jurídica o Pública).
     */
    public boolean validarRuc(String ruc) {
        if (ruc == null || ruc.length() != 13) {
            return false;
        }

        if ("9999999999999".equals(ruc)) {
            return true; // Consumidor Final
        }

        int provincia = Integer.parseInt(ruc.substring(0, 2));
        if ((provincia < 1 || provincia > NUMERO_PROVINCIAS) && provincia != 30) {
            return false;
        }

        int tercerDigito = Character.getNumericValue(ruc.charAt(2));

        // 1. RUC Persona Natural (3er dígito < 6)
        if (tercerDigito < 6) {
            String establecimiento = ruc.substring(10, 13);
            if (establecimiento.equals("000")) {
                return false; // El establecimiento no puede ser 000
            }
            return validarCedula(ruc.substring(0, 10));
        }

        // 2. RUC Sociedad Pública (3er dígito == 6)
        if (tercerDigito == 6) {
            String establecimiento = ruc.substring(9, 13);
            if (establecimiento.equals("0000")) {
                return false;
            }
            return validarRucPublico(ruc);
        }

        // 3. RUC Sociedad Privada o Extranjera (3er dígito == 9)
        if (tercerDigito == 9) {
            String establecimiento = ruc.substring(10, 13);
            if (establecimiento.equals("000")) {
                return false;
            }
            return validarRucJuridico(ruc);
        }

        return false;
    }

    /**
     * Algoritmo Módulo 11 para RUC de Sociedades Privadas / Extranjeras (3er dígito = 9).
     */
    private boolean validarRucJuridico(String ruc) {
        int[] coeficientes = {4, 3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            suma += Character.getNumericValue(ruc.charAt(i)) * coeficientes[i];
        }

        int residuo = suma % 11;
        int digitoVerificadorEsperado = (residuo == 0) ? 0 : (11 - residuo);

        if (digitoVerificadorEsperado == 10) {
            return false; // Algoritmo no válido para residuo de resultado 10
        }

        int digitoVerificadorObtenido = Character.getNumericValue(ruc.charAt(9));
        return digitoVerificadorEsperado == digitoVerificadorObtenido;
    }

    /**
     * Algoritmo Módulo 11 para RUC de Instituciones Públicas (3er dígito = 6).
     */
    private boolean validarRucPublico(String ruc) {
        int[] coeficientes = {3, 2, 7, 6, 5, 4, 3, 2};
        int suma = 0;

        for (int i = 0; i < 8; i++) {
            suma += Character.getNumericValue(ruc.charAt(i)) * coeficientes[i];
        }

        int residuo = suma % 11;
        int digitoVerificadorEsperado = (residuo == 0) ? 0 : (11 - residuo);

        if (digitoVerificadorEsperado == 10) {
            return false;
        }

        int digitoVerificadorObtenido = Character.getNumericValue(ruc.charAt(8));
        return digitoVerificadorEsperado == digitoVerificadorObtenido;
    }
}
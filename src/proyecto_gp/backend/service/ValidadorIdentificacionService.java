package proyecto_gp.backend.service;

public class ValidadorIdentificacionService {

    public boolean validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || (provincia > 24 && provincia != 30)) {
            return false;
        }
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
        if (tercerDigito >= 6) {
            return false;
        }
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int val = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            suma += (val >= 10) ? val - 9 : val;
        }
        int digitoVerificador = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
        return digitoVerificador == Character.getNumericValue(cedula.charAt(9));
    }

    public boolean validarRuc(String ruc) {
        if (ruc == null || ruc.length() != 13 || !ruc.matches("\\d+")) {
            return false;
        }
        if (!ruc.endsWith("001")) {
            return false;
        }
        return validarCedula(ruc.substring(0, 10));
    }

    public String validarIdentificacion(String ruc, String rucJuridicoValido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
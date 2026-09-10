
package proyecto_gp.backend.config;

import proyecto_gp.backend.dao.FormaPagoDAO;
import proyecto_gp.backend.dao.ProveedorDAO;
import proyecto_gp.backend.model.FormaPagoSRI;
import proyecto_gp.backend.model.Proveedor;

public class PruebaModulo3 {
    public static void main(String[] args) {
        // 1. Probar FormaPagoDAO
        FormaPagoDAO fpDao = new FormaPagoDAO();
        System.out.println("--- FORMAS DE PAGO SRI ---");
        for (FormaPagoSRI fp : fpDao.listar()) {
            System.out.println(fp);
        }

        // 2. Probar ProveedorDAO
        ProveedorDAO pDao = new ProveedorDAO();
        Proveedor p = new Proveedor();
        p.setTipoDocumentoId(1); // 1 = RUC
        p.setNumeroIdentificacion("1790011223001");
        p.setRazonSocial("CORPORACION ELJURI S.A.");
        p.setNombreComercial("ELJURI");
        p.setDireccion("Cuenca, Ecuador");
        p.setTelefono("072800000");
        p.setCorreoElectronico("contacto@eljuri.com");

        if (pDao.registrar(p)) {
            System.out.println("\nProveedor registrado exitosamente.");
        }

        System.out.println("\n--- LISTA DE PROVEEDORES ---");
        for (Proveedor prov : pDao.listar()) {
            System.out.println(prov.getRazonSocial() + " | RUC: " + prov.getNumeroIdentificacion());
        }
    }
}
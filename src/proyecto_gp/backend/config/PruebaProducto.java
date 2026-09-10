
package proyecto_gp.backend.config;

import proyecto_gp.backend.dao.ProductoDAO;
import proyecto_gp.backend.model.Producto;

import java.math.BigDecimal;
import java.util.List;

public class PruebaProducto {
    public static void main(String[] args) {
        ProductoDAO dao = new ProductoDAO();

        // 1. Insertar un producto de prueba con IVA 15% (tipo_impuesto_id = 2)
        Producto prod = new Producto();
        prod.setCodigoPrincipal("PROD-001");
        prod.setNombre("Laptop HP Core i7");
        prod.setPrecioVenta(new BigDecimal("750.0000"));
        prod.setStockActual(new BigDecimal("10.00"));
        prod.setTipoImpuestoId(2); // IVA 15%

        if (dao.registrar(prod)) {
            System.out.println("Producto registrado con éxito.");
        }

        // 2. Listar productos
        List<Producto> productos = dao.listar();
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        for (Producto p : productos) {
            System.out.println("ID: " + p.getId() + " | Cod: " + p.getCodigoPrincipal() + 
                               " | " + p.getNombre() + " | Precio: $" + p.getPrecioVenta() + 
                               " | Stock: " + p.getStockActual() + " | Impuesto: " + p.getDescripcionImpuesto());
        }
    }
}
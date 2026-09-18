package proyecto_gp.backend.config;

import proyecto_gp.backend.controller.ProductoController;
import proyecto_gp.backend.model.Producto;

import java.math.BigDecimal;
import java.util.List;

public class PruebaProductoController {

    public static void main(String[] args) {
        ProductoController controller = new ProductoController();

        // 1. Probar registro con precio negativo (error esperado)
        String respErr = controller.guardarProducto("PROD-ERR", "Producto Test Error", 
                                                   new BigDecimal("-10.00"), new BigDecimal("5"), 
                                                   new BigDecimal("1"), 1);
        System.out.println("Prueba Validación Precio: " + respErr);

        // 2. Probar registro correcto (Tarifa IVA 2 = 15%)
        String respOk = controller.guardarProducto("PROD-TEST01", "TECLADO MECANICO", 
                                                   new BigDecimal("45.50"), new BigDecimal("15.00"), 
                                                   new BigDecimal("3.00"), 2);
        System.out.println("Prueba Registro Producto: " + respOk);

        // 3. Listar productos registrados
        List<Producto> lista = controller.listarProductos();
        System.out.println("\nTotal productos en sistema: " + lista.size());
        for (Producto p : lista) {
            System.out.println(" - [" + p.getCodigoPrincipal() + "] " + p.getNombre() + " | Stock: " + p.getStockActual() + " | Precio: $" + p.getPrecioUnitario());
        }
    }
}
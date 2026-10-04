package proyecto_gp.backend.config;

import proyecto_gp.backend.controller.ProductoController;
import proyecto_gp.backend.model.Producto;

import java.math.BigDecimal;
import java.util.List;

public class PruebaProductoController {

    public static void main(String[] args) {
        ProductoController controller = new ProductoController();

        String respErr = controller.guardarProducto("PROD-ERR", "Producto Test Error", 
                                                   new BigDecimal("-10.00"), new BigDecimal("5"), 2);
        System.out.println("Prueba Validación Precio: " + respErr);

        String respOk = controller.guardarProducto("PROD-TEST01", "TECLADO MECANICO", 
                                                   new BigDecimal("45.50"), new BigDecimal("15.00"), 2);
        System.out.println("Prueba Registro Producto: " + respOk);

        List<Producto> lista = controller.listarProductos();
        System.out.println("\nTotal productos en sistema: " + lista.size());
        for (Producto p : lista) {
            System.out.println(" - [" + p.getCodigoPrincipal() + "] " + p.getNombre() + " | Stock: " + p.getStockActual() + " | Precio: $" + p.getPrecioUnitario());
        }
    }
}
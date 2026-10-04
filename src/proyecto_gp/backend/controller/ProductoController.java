package proyecto_gp.backend.controller;

import proyecto_gp.backend.dao.ProductoDAO;
import proyecto_gp.backend.model.Producto;

import java.math.BigDecimal;
import java.util.List;

public class ProductoController {

    private final ProductoDAO productoDAO;

    public ProductoController() {
        this.productoDAO = new ProductoDAO();
    }

    public String guardarProducto(String codigoPrincipal, String nombre, 
                                  BigDecimal precioUnitario, BigDecimal stockActual, 
                                  int tipoImpuestoId) {

        if (codigoPrincipal == null || codigoPrincipal.trim().isEmpty()) {
            return "El código principal del producto es obligatorio.";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del producto es obligatorio.";
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            return "El precio unitario no puede ser nulo ni negativo.";
        }
        if (stockActual == null || stockActual.compareTo(BigDecimal.ZERO) < 0) {
            return "El stock actual no puede ser negativo.";
        }
        if (tipoImpuestoId <= 0) {
            return "Debe seleccionar un tipo de impuesto válido.";
        }

        Producto producto = new Producto();
        producto.setCodigoPrincipal(codigoPrincipal.trim().toUpperCase());
        producto.setNombre(nombre.trim());
        producto.setPrecioUnitario(precioUnitario);
        producto.setStockActual(stockActual);
        producto.setTarifaIvaId(tipoImpuestoId);

        boolean insertado = productoDAO.insertar(producto);
        if (insertado) {
            return "OK";
        } else {
            return "No se pudo registrar el producto. Verifique que el código principal no esté duplicado.";
        }
    }

    public List listarProductos() {
        return productoDAO.listarTodos();
    }
}
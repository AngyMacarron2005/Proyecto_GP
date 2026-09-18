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
                                  BigDecimal stockMinimo, int tarifaIvaId) {

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
        if (stockMinimo == null || stockMinimo.compareTo(BigDecimal.ZERO) < 0) {
            return "El stock mínimo no puede ser negativo.";
        }
        if (tarifaIvaId <= 0) {
            return "Debe seleccionar una tarifa de IVA válida para el producto.";
        }

        Producto producto = new Producto();
        producto.setCodigoPrincipal(codigoPrincipal.trim().toUpperCase());
        producto.setNombre(nombre.trim());
        producto.setPrecioUnitario(precioUnitario);
        producto.setStockActual(stockActual);
        producto.setStockMinimo(stockMinimo);
        producto.setTarifaIvaId(tarifaIvaId);

        boolean insertado = productoDAO.insertar(producto);
        if (insertado) {
            return "OK";
        } else {
            return "No se pudo registrar el producto. Verifique que el código principal no esté duplicado.";
        }
    }

    public String actualizarProducto(int id, String codigoPrincipal, String nombre, 
                                     BigDecimal precioUnitario, BigDecimal stockMinimo, 
                                     int tarifaIvaId) {

        if (id <= 0) {
            return "ID de producto no válido.";
        }
        if (codigoPrincipal == null || codigoPrincipal.trim().isEmpty()) {
            return "El código principal del producto es obligatorio.";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del producto es obligatorio.";
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            return "El precio unitario no puede ser nulo ni negativo.";
        }
        if (stockMinimo == null || stockMinimo.compareTo(BigDecimal.ZERO) < 0) {
            return "El stock mínimo no puede ser negativo.";
        }

        Producto producto = new Producto();
        producto.setId(id);
        producto.setCodigoPrincipal(codigoPrincipal.trim().toUpperCase());
        producto.setNombre(nombre.trim());
        producto.setPrecioUnitario(precioUnitario);
        producto.setStockMinimo(stockMinimo);
        producto.setTarifaIvaId(tarifaIvaId);

        boolean actualizado = productoDAO.actualizar(producto);
        if (actualizado) {
            return "OK";
        } else {
            return "Error al actualizar la información del producto.";
        }
    }

    public String eliminarProducto(int id) {
        if (id <= 0) {
            return "Seleccione un producto válido para eliminar.";
        }
        boolean eliminado = productoDAO.eliminar(id);
        if (eliminado) {
            return "OK";
        } else {
            return "No se puede eliminar el producto. Es posible que ya tenga ventas o movimientos de inventario asociados.";
        }
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    public List<Producto> buscarProductos(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarProductos();
        }
        return productoDAO.buscarPorCriterio(criterio.trim());
    }

    public List<Producto> listarProductosBajoStock() {
        return productoDAO.listarProductosBajoStock();
    }
}
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Service.ServicioProducto;

import static org.junit.Assert.*;

public class ProductoServiceTest2 {

    private ServicioProducto productoService;
    private ProductoDAOMock productoDAOMock;

    @Before
    public void setUp() {
        productoDAOMock = new ProductoDAOMock();
        productoService = new ServicioProducto(productoDAOMock);
    }

    @Test
    public void testBuscarProductoPorIdExistente() throws SQLException {
        Producto producto = productoService.obtenerProducto(1L);

        assertNotNull(producto);
        assertEquals("Laptop", producto.getNombre());
        assertEquals(10, producto.getStock());
    }

    @Test
    public void testBuscarProductoPorIdNoExistente() throws SQLException {
        Producto producto = productoService.obtenerProducto(99L);
        assertNull(producto);
    }

    @Test
    public void testListarProductos() throws SQLException {
        List<Producto> productos = productoService.listarProductos();
        assertEquals(3, productos.size());
        assertTrue(productos.stream().anyMatch(p -> p.getNombre().equals("Mouse")));
    }

    @Test
    public void testActualizarStockProductoExistente() throws SQLException {
        productoService.actualizarStock(2L, 60);
        Producto actualizado = productoService.obtenerProducto(2L);
        assertEquals(110, actualizado.getStock());
    }

    @Test
    public void testActualizarProductoCompleto() throws SQLException {
        Producto producto = productoService.obtenerProducto(3L);
        producto.setDescripcion("Teclado mecánico retroiluminado");
        producto.setPrecioUnitario(new BigDecimal("180.00"));
        productoService.actualizarStock(3L, 25);

        Producto actualizado = productoService.obtenerProducto(3L);
        assertEquals("Teclado mecánico retroiluminado", actualizado.getDescripcion());
        assertEquals(new BigDecimal("180.00"), actualizado.getPrecioUnitario());
        assertEquals(45, actualizado.getStock());
    }
}
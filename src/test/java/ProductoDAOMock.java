
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Model.Producto;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author acer
 */
public class ProductoDAOMock implements ProductoDAO {

    private final List<Producto> productos = new ArrayList<>();

    public ProductoDAOMock() {
        productos.add(new Producto(1L, "Laptop", "Laptop Lenovo", new BigDecimal("3000.00"), 10));
        productos.add(new Producto(2L, "Mouse", "Mouse Logitech", new BigDecimal("80.00"), 50));
        productos.add(new Producto(3L, "Teclado", "Teclado Redragon", new BigDecimal("150.00"), 20));
    }

    @Override
    public Producto buscarPorId(Long id) throws SQLException {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Producto> listarTodos() throws SQLException {
        return new ArrayList<>(productos);
    }

    @Override
    public void actualizar(Producto producto) throws SQLException {
        Optional<Producto> existente = productos.stream()
                .filter(p -> p.getId().equals(producto.getId()))
                .findFirst();

        existente.ifPresent(p -> {
            p.setNombre(producto.getNombre());
            p.setDescripcion(producto.getDescripcion());
            p.setPrecioUnitario(producto.getPrecioUnitario());
            p.setStock(producto.getStock());
        });
    }

    // Método adicional para acceder al estado interno en tests
    public List<Producto> getProductosInternos() {
        return productos;
    }
}

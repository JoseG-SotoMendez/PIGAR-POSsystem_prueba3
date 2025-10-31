package org.PosPrueba.View.Ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Model.Producto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {

    @FXML
    private Label lblVentasHoy;

    @FXML
    private Label lblTicketsHoy;

    @FXML
    private Label lblProductosBajoStock;

    @FXML
    private Label lblTotalIngresos;

    @FXML
    private VBox vboxAccionesRapidas;

    private ServicioProducto servicioProducto;

    @FXML
    public void initialize() {
        // Inicialización básica
    }

    public void setServicioProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            // Ejemplo: contar productos con stock bajo
            List<Producto> productos = servicioProducto.listarProductos();
            long productosBajoStock = productos.stream()
                    .filter(p -> p.getStock() < 10)
                    .count();

            lblProductosBajoStock.setText(String.valueOf(productosBajoStock));

            // Los demás KPIs se cargarían desde servicios de ventas
            lblVentasHoy.setText("$0.00");
            lblTicketsHoy.setText("0");
            lblTotalIngresos.setText("$0.00");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNuevaVenta() {
        // Navegar a vista de venta
        System.out.println("Abrir nueva venta");
    }

    @FXML
    private void onAgregarProducto() {
        // Navegar a formulario de producto
        System.out.println("Agregar producto");
    }

    @FXML
    private void onReponerStock() {
        // Navegar a inventario
        System.out.println("Reponer stock");
    }
}

package org.PosPrueba.View.Ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.PosPrueba.Model.Service.ServicioCliente;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Service.ServicioVenta;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {
    // Cambiar de VBox a HBox
    @FXML
    private HBox hboxAccionesRapidas;  // ← Antes: vboxAccionesRapidas

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

    private Label lblTotalVentas;
    @FXML
    private Label lblTotalClientes;
    @FXML
    private Label lblStockProductos;

    private ServicioCliente servicioCliente;
    private ServicioVenta servicioVenta;
    private ServicioProducto servicioProducto;

    @FXML
    public void initialize() {
        // Inicialización básica
    }

    public void setServicioProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
        cargarDatos();
    }

    public void setServicios(ServicioProducto sp, ServicioCliente sc, ServicioVenta sv) {
        this.servicioProducto = sp;
        this.servicioCliente = sc;
        this.servicioVenta = sv;
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            int totalClientes = servicioCliente.listarClientes().size();
            int totalProductos = servicioProducto.listarProductos().size();
            // sumar total de ventas
            BigDecimal totalVentas = servicioVenta.obtenerTotalVentas();
            lblTotalClientes.setText(String.valueOf(totalClientes));
            lblStockProductos.setText(String.valueOf(totalProductos));
            lblTotalVentas.setText("$" + totalVentas);
        } catch (Exception e) {
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

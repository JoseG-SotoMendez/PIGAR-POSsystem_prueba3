package org.PosPrueba.View.Ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.ItemCarrito;
import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Service.ServicioProducto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CatalogoControllerFX {

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, Long> colId;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colDescripcion;

    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    private ServicioProducto servicioProducto;
    private ControladorPOS controladorPOS;
    private CarritoController carritoController;

    public void setCarritoController(CarritoController carritoController) {
        this.carritoController = carritoController;
    }

    @FXML
    public void initialize() {
        // Configura cellValueFactory para enlazar columnas a propiedades del POJO Producto
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // tablaProductos puede inicializarse vacía hasta que inyectemos el servicio
        tablaProductos.setItems(FXCollections.observableArrayList());
    }

    /**
     * Inyectar servicio desde MainApp
     */
    public void setServicioProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
        cargarProductos();
    }

    /**
     * Inyectar controlador de aplicación (orquestador)
     */
    public void setControladorPOS(ControladorPOS controladorPOS) {
        this.controladorPOS = controladorPOS;
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = servicioProducto.listarProductos();
            ObservableList<Producto> data = FXCollections.observableArrayList(productos);
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            tablaProductos.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error cargando productos: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onAgregar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            System.out.println("Seleccione un producto primero.");
            return;
        }

        try {
            if (carritoController != null) {
                // Crear un ItemCarrito con 1 unidad
                ItemCarrito item = new ItemCarrito(
                        seleccionado.getId(),
                        seleccionado.getNombre(),
                        1,
                        seleccionado.getPrecioUnitario()
                );

                carritoController.agregarItem(item);
            }

            // Actualizar stock visual (opcional)
            if (controladorPOS != null) {
                controladorPOS.onAgregar(seleccionado.getId(), 1);
            }
            tablaProductos.refresh();

            cargarProductos(); // refrescar la tabla
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

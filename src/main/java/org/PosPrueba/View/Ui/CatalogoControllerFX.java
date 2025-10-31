package org.PosPrueba.View.Ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.Producto;
import org.PosPrueba.Model.Service.ServicioProducto;

import java.util.List;

public class CatalogoControllerFX {

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, Long> colId;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    private ControladorPOS controladorPOS;
    private ServicioProducto servicioProducto; // opcional para pruebas directas

    @FXML
    public void initialize() {
        // definir columnas si no están en FXML
        if (colId != null) {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        }
        if (colNombre != null) {
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        }
        if (colStock != null) {
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        }
        // la carga real de datos la hace setControladorPOS o setServicioProducto
    }

    public void setControladorPOS(ControladorPOS controladorPOS) {
        this.controladorPOS = controladorPOS;
        // si puedes acceder al servicio, cargar productos
        try {
            // intentar cargar vía reflection del controlador (si expone servicio)
            // Mejor: en MainApp pasamos también el servicioProducto al controller si lo tenemos
        } catch (Exception ignored) {
        }
    }

    public void setServicioProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            List<Producto> lista = servicioProducto.listarProductos();
            ObservableList<Producto> obs = FXCollections.observableArrayList(lista);
            tablaProductos.setItems(obs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAgregar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null && controladorPOS != null) {
            try {
                controladorPOS.onAgregar(seleccionado.getId(), 1); // agregar 1 unidad
                // recargar tabla para ver stock actualizado
                if (servicioProducto != null) {
                    cargarProductos();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No hay producto seleccionado o controlador no inyectado.");
        }
    }
}

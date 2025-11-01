package org.PosPrueba.View.Ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.ItemCarrito;
import org.PosPrueba.Model.Service.ServicioCliente;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Model.Service.ServicioUsuario;
import org.PosPrueba.Model.Service.ServicioVenta;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CarritoController {

    @FXML
    private TableView<ItemCarrito> tblCarrito;

    @FXML
    private TableColumn<ItemCarrito, String> colNombre;

    @FXML
    private TableColumn<ItemCarrito, Integer> colCantidad;

    @FXML
    private TableColumn<ItemCarrito, BigDecimal> colPrecio;

    @FXML
    private TableColumn<ItemCarrito, BigDecimal> colSubtotal;


    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblImpuestos;

    @FXML
    private Label lblTotal;

    @FXML
    private TextField txtDescuento;

    @FXML
    private Button btnCobrar;

    @FXML
    private Button btnCancelar;



    private ServicioProducto servicioProducto;
    private ServicioUsuario servicioUsuario;
    private ServicioCliente servicioCliente;
    private ServicioVenta servicioVenta;
    private ControladorPOS controladorPOS;

    private ObservableList<ItemCarrito> items= FXCollections.observableArrayList();
    private static final BigDecimal TASA_IMPUESTO = new BigDecimal("0.19"); // 13% IVA



    @FXML
    public void initialize() {
        items = FXCollections.observableArrayList();

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tblCarrito.setItems(items);

        // Listener para actualizar totales cuando cambia el carrito
        items.addListener((javafx.collections.ListChangeListener.Change<? extends ItemCarrito> c) -> {
            actualizarTotales();
        });

        txtDescuento.setText("0.00");
        actualizarTotales();
    }

    public void agregarItem(ItemCarrito item) {
        // Verificar si el producto ya está en el carrito
        for (ItemCarrito existente : items) {
            if (existente.getProductoId().equals(item.getProductoId())) {
                existente.incrementarCantidad();
                tblCarrito.refresh();
                return;
            }
        }
        items.add(item);
    }

    @FXML
    private void onQuitarItem() {
        ItemCarrito seleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar");
            confirmacion.setHeaderText("¿Quitar item del carrito?");
            confirmacion.setContentText(seleccionado.getNombre());

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                items.remove(seleccionado);
            }
        }
    }

    @FXML
    private void onCobrar() {
        if (items.isEmpty()) { return; }
        try {
            // Aquí podrías mostrar diálogo para elegir cliente / método de pago; por ahora usar clienteId=null
            Long ventaId = controladorPOS.onCobrar(new ArrayList<>(items), null);
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Venta registrada. ID = " + ventaId);
            info.showAndWait();
            items.clear();
        } catch (Exception e) {
            e.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR, "Error al procesar la venta: " + e.getMessage());
            err.showAndWait();
        }
    }


    @FXML
    private void onCancelar() {
        if (items.isEmpty()) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar venta");
        confirmacion.setHeaderText("¿Está seguro de cancelar la venta?");
        confirmacion.setContentText("Se perderán todos los items del carrito");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            items.clear();
        }
    }

    @FXML
    private void onAplicarDescuento() {
        actualizarTotales();
    }

    private void actualizarTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemCarrito item : items) {
            subtotal = subtotal.add(item.getSubtotal());
        }

        BigDecimal descuento = BigDecimal.ZERO;
        try {
            String descuentoStr = txtDescuento.getText().trim();
            if (!descuentoStr.isEmpty()) {
                descuento = new BigDecimal(descuentoStr);
            }
        } catch (NumberFormatException e) {
            descuento = BigDecimal.ZERO;
        }

        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        if (subtotalConDescuento.compareTo(BigDecimal.ZERO) < 0) {
            subtotalConDescuento = BigDecimal.ZERO;
        }

        BigDecimal impuestos = subtotalConDescuento.multiply(TASA_IMPUESTO);
        BigDecimal total = subtotalConDescuento.add(impuestos);

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblImpuestos.setText(String.format("$%.2f", impuestos));
        lblTotal.setText(String.format("$%.2f", total));
    }

    public ObservableList<ItemCarrito> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        String totalStr = lblTotal.getText().replace("$", "").trim();
        return new BigDecimal(totalStr);
    }


    public void setControladorPOS(ControladorPOS controladorPOS) {
        this.controladorPOS = controladorPOS;
    }

    public void setServicioProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    public void setServicioUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    public void setServicioCliente(ServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
    }

    public void setServicioVenta(ServicioVenta servicioVenta) {
        this.servicioVenta = servicioVenta;
    }

}

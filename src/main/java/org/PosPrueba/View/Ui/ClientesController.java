package org.PosPrueba.View.Ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.PosPrueba.Model.Cliente;
import org.PosPrueba.Model.Service.ServicioCliente;

import java.sql.SQLException;
import java.util.List;

public class ClientesController {

    @FXML
    private TextField txtBusqueda;

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente, Long> colId;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colApellido;

    @FXML
    private TableColumn<Cliente, String> colDocumento;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private TableColumn<Cliente, String> colEmail;

    // Campos del formulario
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtDocumento;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextArea txtDireccion;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnEliminar;

    private ServicioCliente servicioCliente;
    private ObservableList<Cliente> clientes;
    private Cliente clienteSeleccionado;

    @FXML
    public void initialize() {
        clientes = FXCollections.observableArrayList();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tblClientes.setItems(clientes);

        // Listener para selección
        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarClienteEnFormulario(newVal);
            }
        });

        // Búsqueda en tiempo real
        txtBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            buscarClientes(newVal);
        });

        limpiarFormulario();
    }

    public void setServicioCliente(ServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
        cargarClientes();
    }

    private void cargarClientes() {
        try {
            List<Cliente> lista = servicioCliente.listarClientes();
            ObservableList<Cliente> data = FXCollections.observableArrayList(lista);
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            tblClientes.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarClientes(String texto) {
        if (servicioCliente == null) return;

        try {
            if (texto == null || texto.trim().isEmpty()) {
                cargarClientes();
            } else {
                List<Cliente> lista = servicioCliente.buscarPorNombre(texto);
                clientes.setAll(lista);
            }
        } catch (SQLException e) {
            mostrarError("Error al buscar clientes", e.getMessage());
        }
    }

    @FXML
    private void onNuevo() {
        limpiarFormulario();
        txtNombre.requestFocus();
    }

    @FXML
    private void onGuardar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Cliente cliente = new Cliente();
            if (clienteSeleccionado != null) {
                cliente.setId(clienteSeleccionado.getId());
            }

            cliente.setNombre(txtNombre.getText().trim());
            cliente.setApellido(txtApellido.getText().trim());
            cliente.setDocumento(txtDocumento.getText().trim());
            cliente.setTelefono(txtTelefono.getText().trim());
            cliente.setEmail(txtEmail.getText().trim());
            cliente.setDireccion(txtDireccion.getText().trim());

            if (clienteSeleccionado == null) {
                // Nuevo cliente
                Long id = servicioCliente.guardarCliente(cliente);
                cliente.setId(id);
                mostrarExito("Cliente guardado exitosamente");
            } else {
                // Actualizar cliente
                servicioCliente.actualizarCliente(cliente);
                mostrarExito("Cliente actualizado exitosamente");
            }

            cargarClientes();
            limpiarFormulario();

        } catch (SQLException e) {
            mostrarError("Error al guardar cliente", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onEliminar() {
        Cliente seleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Seleccione un cliente para eliminar");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar cliente?");
        confirmacion.setContentText(seleccionado.getNombreCompleto() + "\nEsta acción no se puede deshacer.");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                servicioCliente.eliminarCliente(seleccionado.getId());
                mostrarExito("Cliente eliminado exitosamente");
                cargarClientes();
                limpiarFormulario();
            } catch (SQLException e) {
                mostrarError("Error al eliminar cliente", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cargarClienteEnFormulario(Cliente cliente) {
        clienteSeleccionado = cliente;
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDocumento.setText(cliente.getDocumento());
        txtTelefono.setText(cliente.getTelefono());
        txtEmail.setText(cliente.getEmail());
        txtDireccion.setText(cliente.getDireccion());
    }

    private void limpiarFormulario() {
        clienteSeleccionado = null;
        txtNombre.clear();
        txtApellido.clear();
        txtDocumento.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtDireccion.clear();
    }

    private boolean validarFormulario() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarAdvertencia("El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }
        if (txtApellido.getText().trim().isEmpty()) {
            mostrarAdvertencia("El apellido es obligatorio");
            txtApellido.requestFocus();
            return false;
        }
        if (txtDocumento.getText().trim().isEmpty()) {
            mostrarAdvertencia("El documento es obligatorio");
            txtDocumento.requestFocus();
            return false;
        }
        return true;
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

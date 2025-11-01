package org.PosPrueba.View.Ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.Service.ServicioCliente;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Model.Usuario;
import org.PosPrueba.Model.Service.ServicioUsuario;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label lblError;

    private ServicioUsuario servicioUsuario;

    private ServicioProducto servicioProducto;
    private ServicioCliente servicioCliente;
    private ControladorPOS controladorPOS;


    @FXML
    public void initialize() {
        lblError.setVisible(false);

        // Enter key para login
        txtPassword.setOnAction(e -> onLogin());
    }

    public void setServicioUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    public void setServicioCliente(ServicioCliente servicioCliente) {this.servicioCliente = servicioCliente;
    }
    public void setServicioProducto(ServicioProducto servicioProducto) {  // ← AGREGAR
        this.servicioProducto = servicioProducto;
    }

    public void setControladorPOS(ControladorPOS controladorPOS) {
        this.controladorPOS = controladorPOS;
    }

    @FXML
    private void onLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor ingrese usuario y contraseña");
            return;
        }

        try {
            Usuario usuario = servicioUsuario.autenticar(username, password);

            if (usuario != null) {
                // Login exitoso - abrir ventana principal
                abrirVentanaPrincipal();
            } else {
                mostrarError("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            mostrarError("Error al autenticar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void abrirVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Parent root = loader.load();

            MainControllerFX mainController = loader.getController();
            mainController.setServicioProducto(servicioProducto);
            mainController.setControladorPOS(controladorPOS);
            // Aquí inyectarías todos los servicios necesarios
            // mainController.setServicios(...);

            Stage stage = new Stage();
            stage.setTitle("POS - Sistema de Ventas");
            stage.setScene(new Scene(root, 1200, 800));
            stage.setMaximized(true);
            stage.show();

            // Cerrar ventana de login
            Stage loginStage = (Stage) btnLogin.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            mostrarError("Error al abrir ventana principal");
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    @FXML
    private void onCancelar() {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }


}

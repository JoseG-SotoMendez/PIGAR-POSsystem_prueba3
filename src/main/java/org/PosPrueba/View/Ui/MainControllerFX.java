package org.PosPrueba.View.Ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.Service.ServicioCliente;
import org.PosPrueba.Model.Service.ServicioProducto;

public class MainControllerFX {

    public MainControllerFX() {
        try {
            FXMLLoader loaderCarrito = new FXMLLoader(getClass().getResource("/fxml/Carrito.fxml"));
            carritoRoot = loaderCarrito.load();
            carritoController = loaderCarrito.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private BorderPane rootPane; // debe coincidir con fx:id="rootPane" en Main.fxml

    private ServicioProducto servicioProducto;
    private ControladorPOS controladorPOS;
    private ServicioCliente servicioCliente;
    private CarritoController carritoController;
    private Node carritoRoot;

    public void setServicioCliente(ServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
    }

    public void setServicioProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    public void setControladorPOS(ControladorPOS controladorPOS) {
        this.controladorPOS = controladorPOS;
    }

    /**
     * Permite que MainApp (o cualquier otro) establezca un nodo central.
     */
    public void setCenterNode(Node center) {
        if (rootPane != null) {
            rootPane.setCenter(center);
        }
    }

    @FXML
    private void onSalir(ActionEvent event) {
        if (rootPane != null && rootPane.getScene() != null) {
            rootPane.getScene().getWindow().hide();
        }
    }

    /**
     * Acción del menú: carga Catalogo.fxml, obtiene su controller y le inyecta dependencias.
     */
    @FXML
    private void onAbrirCatalogo(ActionEvent event) {
        try {
            FXMLLoader loaderCatalogo = new FXMLLoader(getClass().getResource("/fxml/Catalogo.fxml"));
            Node catalogoRoot = loaderCatalogo.load();
            CatalogoControllerFX catalogoController = loaderCatalogo.getController();

            // Inyectar dependencias
            catalogoController.setServicioProducto(servicioProducto);
            catalogoController.setControladorPOS(controladorPOS);
            catalogoController.setCarritoController(carritoController); // ✅ usar el mismo carrito

            // Mostrar catálogo en el centro y carrito a la derecha
            setCenterNode(catalogoRoot);
            rootPane.setRight(carritoRoot); // ✅ ahora el carrito se ve y se actualiza

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAbrirClientes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Clientes.fxml"));
            Node root = loader.load();

            ClientesController clientesController = loader.getController();
            if (this.servicioCliente != null) {
                clientesController.setServicioCliente(this.servicioCliente);
            }

            setCenterNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAbrirCarrito(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Carrito.fxml"));
            Node root = loader.load();
            CarritoController carritoController = loader.getController();
            if (this.controladorPOS != null) {
                carritoController.setControladorPOS(this.controladorPOS);
            }
            if (this.servicioProducto != null) {
                carritoController.setServicioProducto(this.servicioProducto);
            }


            setCenterNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAbrirDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Node root = loader.load();

            DashboardController dashboardController = loader.getController();
            // dashboardController.setServicioProducto(servicioProducto);

            setCenterNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.PosPrueba.View.Ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.Service.ServicioProducto;

public class MainControllerFX {

    @FXML
    private BorderPane rootPane; // debe coincidir con fx:id="rootPane" en Main.fxml

    private ServicioProducto servicioProducto;
    private ControladorPOS controladorPOS;

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
            URL fxmlUrl = getClass().getResource("/fxml/Catalogo.fxml");
            if (fxmlUrl == null) {
                System.err.println("No se encontró /fxml/Catalogo.fxml en classpath");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node catalogoRoot = loader.load();

            Object ctrl = loader.getController();
            if (ctrl instanceof CatalogoControllerFX) {
                CatalogoControllerFX catalogoController = (CatalogoControllerFX) ctrl;
                if (this.servicioProducto != null) {
                    catalogoController.setServicioProducto(this.servicioProducto);
                }
                if (this.controladorPOS != null) {
                    catalogoController.setControladorPOS(this.controladorPOS);
                }
            }

            // colocar la vista cargada en el centro del BorderPane
            setCenterNode(catalogoRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

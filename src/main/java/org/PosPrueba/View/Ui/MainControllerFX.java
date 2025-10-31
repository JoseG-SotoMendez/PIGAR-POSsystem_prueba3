package org.PosPrueba.View.Ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MainControllerFX {

    @FXML
    private BorderPane rootPane; // inyectar con fx:id="rootPane" en Main.fxml

    public void setCenterNode(Node center) {
        if (rootPane != null) {
            rootPane.setCenter(center);
        }
    }

    @FXML
    private void onSalir(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}

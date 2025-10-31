package org.PosPrueba.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Controller.Command.InvocadorComandos;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Model.Persistence.Factory.FabricaDAO;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.View.Ui.MainControllerFX;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // crear DAOs / servicios
        ProductoDAO productoDAO = FabricaDAO.crearProductoDAO();
        ServicioProducto servicioProducto = new ServicioProducto(productoDAO);
        InvocadorComandos invocador = new InvocadorComandos();
        ControladorPOS controladorPOS = new ControladorPOS(invocador, servicioProducto);

        // cargar Main.fxml
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent mainRoot = mainLoader.load();

        // inyectar servicios en el controller principal
        Object ctrl = mainLoader.getController();
        if (ctrl instanceof MainControllerFX) {
            MainControllerFX mainController = (MainControllerFX) ctrl;
            mainController.setServicioProducto(servicioProducto);
            mainController.setControladorPOS(controladorPOS);

            // opcional: abrir catálogo al inicio
            // mainController.onAbrirCatalogo(null);
        }

        primaryStage.setTitle("POS - Demo");
        primaryStage.setScene(new Scene(mainRoot, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

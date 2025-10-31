package org.PosPrueba.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Model.Persistence.Factory.FabricaDAO;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Controller.Command.InvocadorComandos;
import org.PosPrueba.View.Ui.CatalogoControllerFX;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializar capas (fábricas/servicios/controlador)
        ProductoDAO productoDAO = FabricaDAO.crearProductoDAO();
        ServicioProducto servicioProducto = new ServicioProducto(productoDAO);
        InvocadorComandos invocador = new InvocadorComandos();
        ControladorPOS controladorPOS = new ControladorPOS(invocador, servicioProducto);

        // Cargar Main.fxml
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent mainRoot = mainLoader.load();

        // cargar Catalogo.fxml y pasarle el controladorPOS
        FXMLLoader catLoader = new FXMLLoader(getClass().getResource("/fxml/Catalogo.fxml"));
        Parent catalogoRoot = catLoader.load();
        CatalogoControllerFX catalogoController = catLoader.getController();
        catalogoController.setControladorPOS(controladorPOS); // inyectar backend

        // insertar el catálogo dentro del main (si el BorderPane tiene fx:id "rootPane")
        Object mainController = mainLoader.getController();
        if (mainController instanceof org.PosPrueba.View.Ui.MainControllerFX) {
            ((org.PosPrueba.View.Ui.MainControllerFX) mainController).setCenterNode(catalogoRoot);
        }

        primaryStage.setTitle("POS - Demo");
        primaryStage.setScene(new Scene(mainRoot, 1000, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

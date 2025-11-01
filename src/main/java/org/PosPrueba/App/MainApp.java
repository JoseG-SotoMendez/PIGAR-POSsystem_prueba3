package org.PosPrueba.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.PosPrueba.Controller.ControladorPOS;
import org.PosPrueba.Controller.Command.InvocadorComandos;
import org.PosPrueba.Model.Persistence.Dao.ProductoDAO;
import org.PosPrueba.Model.Persistence.Dao.UsuarioDAO;
import org.PosPrueba.Model.Persistence.Factory.FabricaDAO;
import org.PosPrueba.Model.Service.ServicioProducto;
import org.PosPrueba.Model.Service.ServicioUsuario;
import org.PosPrueba.View.Ui.LoginController;
import org.PosPrueba.View.Ui.MainControllerFX;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear TODOS los servicios necesarios
        ProductoDAO productoDAO = FabricaDAO.crearProductoDAO();
        UsuarioDAO usuarioDAO = FabricaDAO.crearUsuarioDAO();

        ServicioProducto servicioProducto = new ServicioProducto(productoDAO);
        ServicioUsuario servicioUsuario = new ServicioUsuario(usuarioDAO);

        InvocadorComandos invocador = new InvocadorComandos();
        ControladorPOS controladorPOS = new ControladorPOS(invocador, servicioProducto);

        // Cargar Login con servicios inyectados
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent loginRoot = loginLoader.load();

        // INYECTAR ServicioUsuario en el LoginController
        LoginController loginController = loginLoader.getController();
        loginController.setServicioProducto(servicioProducto);
        loginController.setServicioUsuario(servicioUsuario);
        loginController.setControladorPOS(controladorPOS);

        Scene scene = new Scene(loginRoot);
        primaryStage.setTitle("PIGAR POS - Sistema de Ventas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

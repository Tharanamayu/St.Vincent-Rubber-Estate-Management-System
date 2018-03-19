/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.ModuleDao;
import entity.Employee;
import entity.Module;
import entity.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import static ui.LoginUIController.lblCapsLock;
import static ui.LoginUIController.privilege;
import static ui.LoginUIController.logedUser;
import static ui.Main.stage;
import util.HibernateUtil;
import util.SystemAlert;

/**
 *
 * @author Sahan Rid
 */
public class Main extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

//<editor-fold defaultstate="collapsed" desc="Original login window">
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
        Scene scene = new Scene(root);

        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Icon.jpg")));
        stage.setTitle(" St. Vincent Estaete ");

        String css = Main.class.getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);

        stage.initStyle(StageStyle.UNDECORATED);
//        scene.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
//            @Override
//            public void handle(javafx.scene.input.KeyEvent event) {
//                if (event.getCode() == KeyCode.CAPS) {
//                    lblCapsLock.setText("Capslock is on");
//                }
//            }
//        });
        stage.setScene(scene);
        stage.show();
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Testing Main window without loading">
//        privilege = new HashMap<String, Boolean>();
//
//        ObservableList<Module> x = ModuleDao.getAll();
//
//        for (Module module : x) {
//            privilege.put(module.getName() + "_select", true);
//            privilege.put(module.getName() + "_insert", true);
//            privilege.put(module.getName() + "_update", true);
//            privilege.put(module.getName() + "_delete", true);
//        }
//
//        logedUser = new User();
//        Employee emp = new Employee();
//        logedUser.setUsername("Admin");
//        emp.setId(0);
//        logedUser.setEmployeeId(emp);
//        emp.setName("Admin");
//
//        if (privilege != null) {
//
//            Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
//
//            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//
//            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
//            String css = Main.class.getResource("/css/style.css").toExternalForm();
//            scene.getStylesheets().clear();
//            scene.getStylesheets().add(css);
//
//            stage.close();
//            stage = new Stage();
//
//            stage.setHeight(screenBounds.getHeight());
//            stage.setWidth(screenBounds.getWidth());
//            stage.setMaxHeight(screenBounds.getHeight());
//            stage.setMaxWidth(screenBounds.getWidth());
//            stage.setMinHeight(screenBounds.getHeight());
//            stage.setMinWidth(screenBounds.getWidth());
//
//            stage.setScene(scene);
//            stage.setTitle("St. Vincent Estate");
//            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Icon.jpg")));
//            stage.initStyle(StageStyle.DECORATED);
//            stage.setResizable(false);
//
//            stage.show();
//        }
//</editor-fold>

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.getSessionFactory().close();
        System.exit(0);
    }
}

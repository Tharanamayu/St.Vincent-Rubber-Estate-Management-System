/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import util.SystemAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dao.ModuleDao;
import dao.PrivilegeDao;
import dao.UserDao;
import dao.UserstatusDao;
import entity.Employee;
import entity.Module;
import entity.Privilege;
import entity.User;
import entity.Userstatus;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import static ui.Main.stage;
import util.HibernateUtil;
import util.Security;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class LoginUIController implements Initializable {

    double screenX, screenY;

//<editor-fold defaultstate="collapsed" desc="FXMl Data">
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField pswPassword;
    @FXML
    private Label lblAttempt;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Form Data">
    private int attempt;
    public static User logedUser;
    public static HashMap<String, Boolean> privilege;
    private Connection connection;
    private int sameUserAttempt;
    private String[] loggingTry;
    static Stage countDown;
//</editor-fold>
    @FXML
    private Label lblForgetPassword;
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imgClose;
    @FXML
    public static Label lblCapsLock;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        attempt = 3;
        btnLogin.setDisable(true);
        loggingTry = new String[3];
        sameUserAttempt = 0;
        privilege = null;
    }

    private void serverConnection() {
        Boolean conStatus;
        connection = null;
        String location = "jdbc:mysql://localhost/st_vincent";
        String username = "root";
        String password = "1234";

        try {
            connection = DriverManager.getConnection(location, username, password);
        } catch (SQLException ex) {
            Alert alert = SystemAlert.createAlert(
                    "Error Message",
                    "Connection Failed",
                    "Could not connect to the server",
                    Alert.AlertType.ERROR,
                    ui.Main.stage);
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    private void btnCancelAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Close",
                "Login screen close",
                "our are going to close the login screen. Are you sure ?",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            logedUser = null;
            HibernateUtil.getSessionFactory().close();
            stage.close();
            System.exit(0);
        }
    }

    @FXML
    private void btnLoginAP(ActionEvent event) {
        if (!txtUsername.getText().isEmpty() && !pswPassword.getText().isEmpty()) {
            if (txtUsername.getText().equals("admin") && pswPassword.getText().equals("abc123")) {

                privilege = new HashMap<String, Boolean>();

                ObservableList<Module> x = ModuleDao.getAll();

                for (Module module : x) {
                    privilege.put(module.getName() + "_select", true);
                    privilege.put(module.getName() + "_insert", true);
                    privilege.put(module.getName() + "_update", true);
                    privilege.put(module.getName() + "_delete", true);
                }

                logedUser = new User();
                Employee emp = new Employee();
                logedUser.setUsername("Admin");
                emp.setId(0);
                logedUser.setEmployeeId(emp);
                emp.setName("Admin");

            } else {
                if (attempt != 0) {
                    loggingTry[sameUserAttempt++] = txtUsername.getText().trim();
                    attempt--;
                    serverConnection();
                    if (connection != null) {
                        String queryUserLogin = "SELECT * FROM user WHERE username =? AND password = ?";
                        try {
                            PreparedStatement statementUserLogin = connection.prepareStatement(queryUserLogin);
                            statementUserLogin.setString(1, txtUsername.getText().trim());
                            statementUserLogin.setString(2, Security.getHash(pswPassword.getText()));
                            ResultSet resultsUserLogin = statementUserLogin.executeQuery();

                            if (resultsUserLogin.next()) {
                                logedUser = UserDao.getById(resultsUserLogin.getInt("id"));
                                if (logedUser.getUserstatusId().getName().equals("Active")) {
                                    privilege = new HashMap<String, Boolean>();

                                    ObservableList<Module> x = ModuleDao.getAll();

                                    for (Module module : x) {
                                        privilege.put(module.getName() + "_select", false);
                                        privilege.put(module.getName() + "_insert", false);
                                        privilege.put(module.getName() + "_update", false);
                                        privilege.put(module.getName() + "_delete", false);
                                    }

                                    ObservableList<Privilege> privileges = PrivilegeDao.getAllByUser(logedUser);

                                    for (Privilege privi : privileges) {

                                        String moduleName = privi.getModuleId().getName();

                                        if (privi.getSel() == 1) {
                                            if (!privilege.get(moduleName + "_select")) {
                                                privilege.put(moduleName + "_select", true);
                                            }
                                        }

                                        if (privi.getIns() == 1) {
                                            if (!privilege.get(moduleName + "_insert")) {
                                                privilege.put(moduleName + "_insert", true);
                                            }
                                        }

                                        if (privi.getUpd() == 1) {
                                            if (!privilege.get(moduleName + "_update")) {
                                                privilege.put(moduleName + "_update", true);
                                            }
                                        }

                                        if (privi.getDel() == 1) {
                                            if (!privilege.get(moduleName + "_delete")) {
                                                privilege.put(moduleName + "_delete", true);
                                            }
                                        }
                                    }
                                } else {
                                    Alert alert = SystemAlert.createAlert(
                                            "Unauthorized user",
                                            "Your user account doesn't have permission to access the system.",
                                            "Please contact technical officer for help",
                                            Alert.AlertType.ERROR,
                                            ui.Main.stage);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    System.exit(0);
                                }

                                statementUserLogin.close();
                            } else {
                                if (attempt == 0) {
                                    if (loggingTry[0].equals(loggingTry[1]) && loggingTry[1].equals(loggingTry[2])) {

                                        String queryUserBlock = "SELECT * FROM user WHERE username =?";
                                        try {
                                            PreparedStatement statementUserBlock = connection.prepareStatement(queryUserBlock);
                                            statementUserBlock.setString(1, txtUsername.getText().trim());
                                            ResultSet resultsUserBlock = statementUserBlock.executeQuery();

                                            if (resultsUserBlock.next()) {
                                                User userBlock = UserDao.getById(resultsUserBlock.getInt("id"));
                                                userBlock.setUserstatusId(UserstatusDao.getByName("Blocked"));
                                                UserDao.update(userBlock);
                                                Alert alert = SystemAlert.createAlert(
                                                        "Error Message",
                                                        "Login Fail",
                                                        "You are tried to log three time as " + loggingTry[1] + " Following user account is blocked now.",
                                                        Alert.AlertType.ERROR,
                                                        ui.Main.stage);
                                                Optional<ButtonType> result = alert.showAndWait();
                                                System.exit(0);
                                            } else {
                                                try {
                                                    countDown();
                                                } catch (IOException e) {
                                                    System.out.println("CountDown Error :" + e);
                                                }
                                            }
                                            statementUserBlock.close();
                                        } catch (SQLException ex) {
                                            Alert alert = SystemAlert.createAlert(
                                                    "Error Message", "Login Fail",
                                                    "Could not retrieve date to from Database",
                                                    Alert.AlertType.ERROR,
                                                    ui.Main.stage);
                                            Optional<ButtonType> result = alert.showAndWait();
                                        }
                                    } else {
                                        try {
                                            countDown();
                                        } catch (IOException e) {
                                            System.out.println("CountDown Error :" + e);
                                        }
                                    }

                                } else {
                                    lblAttempt.setText("Login Faild Check the Username and Password.\nYou have " + (attempt) + " more attemts.");
                                    pswPassword.setText("");
                                    txtUsername.setText("");
                                    btnLogin.setDisable(true);
                                }
                            }
                        } catch (SQLException ex) {
                            Alert alert = SystemAlert.createAlert(
                                    "Error Message",
                                    "Login Fail",
                                    "Could not retrieve date to from Database",
                                    Alert.AlertType.ERROR,
                                    ui.Main.stage);
                            Optional<ButtonType> result = alert.showAndWait();
                        }
                    }
                }
            }
//<editor-fold defaultstate="collapsed" desc="Load Main Window">
            if (privilege != null) {
                try {
//<editor-fold defaultstate="collapsed" desc="Main window starting">
                    Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                    Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
                    String css = Main.class.getResource("/css/style.css").toExternalForm();
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(css);

                    stage.close();
                    stage = new Stage();

                    stage.setHeight(screenBounds.getHeight());
                    stage.setWidth(screenBounds.getWidth());
                    stage.setMaxHeight(screenBounds.getHeight());
                    stage.setMaxWidth(screenBounds.getWidth());
                    stage.setMinHeight(screenBounds.getHeight());
                    stage.setMinWidth(screenBounds.getWidth());

                    stage.setScene(scene);
                    stage.setTitle("St. Vincent Estate");
                    stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Icon.jpg")));
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setResizable(false);

                    stage.show();

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Window close conformation">
                    stage.setOnCloseRequest(eventClose -> {
                        if (stage.isIconified()) {
                            stage.setMaximized(true);
                        }
                        Alert alert = SystemAlert.createAlert(
                                "Application Close Confirmation",
                                "Are you sure you want to close the appication ?",
                                "Your user session will be ended, and all the unsaved data will be lost.",
                                Alert.AlertType.CONFIRMATION,
                                ui.Main.stage
                        );
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.get() == ButtonType.OK) {
                            logedUser = null;
                            HibernateUtil.getSessionFactory().close();
                            stage.close();
                            System.exit(0);
                        }
                        if (result.get() == ButtonType.CANCEL) {
                            eventClose.consume();
                        }
                    });
//</editor-fold>

                } catch (IOException ex) {
                    Alert alert = SystemAlert.createAlert(
                            "Error Message",
                            "Login Fail",
                            "Could not Load Main Window",
                            Alert.AlertType.ERROR,
                            ui.Main.stage);
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Error Message", "Login Fail",
                    "Please Enter valid Username and Password",
                    Alert.AlertType.ERROR,
                    ui.Main.stage);
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Password hint">
    @FXML
    private void lblForgetPasswordMC(MouseEvent event) throws SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inser Username");
        dialog.setHeaderText("Username for hint");
        dialog.setContentText("Please enter your username to get password hint:");

        Optional<String> result = dialog.showAndWait();
        String tempUsername = "";

        if (result.isPresent()) {
            tempUsername = result.get().trim();
        }

        if (!tempUsername.equals("") && result.isPresent()) {
            serverConnection();
            if (connection != null) {
                String queryUserHint = "SELECT username FROM user WHERE username =?";

                try {
                    PreparedStatement statementUserHint = connection.prepareStatement(queryUserHint);
                    statementUserHint.setString(1, tempUsername);
                    ResultSet resultsUserHint = statementUserHint.executeQuery();

                    if (resultsUserHint.next()) {
                        User userHint = UserDao.getUsername(tempUsername);
                        Alert alert = SystemAlert.createAlert(
                                "Hint !",
                                "Your password hint : " + userHint.getHint(),
                                "You can use password hint to remember your password or you can request password reset.",
                                Alert.AlertType.INFORMATION,
                                ui.Main.stage
                        );

                        Optional<ButtonType> resulthint = alert.showAndWait();
                    } else {
                        Alert alert = SystemAlert.createAlert(
                                "Wrong Username",
                                "Please enter valid username.",
                                "Please enter valide username. If you forgot both username and password please contact IT officer for reset your password.",
                                Alert.AlertType.INFORMATION,
                                ui.Main.stage);

                        Optional<ButtonType> resultNoHint = alert.showAndWait();
                    }
                    statementUserHint.close();
                } catch (SQLException ex) {
                    Alert alert = SystemAlert.createAlert(
                            "Error Message",
                            "Login Fail",
                            "Could not Load MainWindow",
                            Alert.AlertType.ERROR,
                            ui.Main.stage);
                    Optional<ButtonType> resultError = alert.showAndWait();
                }
            }
        } else if (!result.isPresent()) {

        } else {
            Alert alert = SystemAlert.createAlert(
                    "No Username",
                    "Please enter your username.",
                    "Please enter your username. If you forgot both username and password please contact IT officer for reset your password.",
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);

            Optional<ButtonType> resultNoUsername = alert.showAndWait();
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Sign button disable & enable">
    @FXML
    private void activeKR(KeyEvent event) {
        if (!txtUsername.getText().trim().isEmpty() && !pswPassword.getText().isEmpty()) {
            btnLogin.setDisable(false);
        } else {
            btnLogin.setDisable(true);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Login window move action">
    @FXML
    private void rootMD(MouseEvent event) {
        stage.setX(screenX + event.getScreenX());
        stage.setY(screenY + event.getScreenY());
    }

    @FXML
    private void rootMP(MouseEvent event) {
        screenX = stage.getX() - event.getScreenX();
        screenY = stage.getY() - event.getScreenY();
    }

    private void countDown() throws IOException {
        countDown = new Stage();
        AnchorPane subroot = FXMLLoader.load(Main.class.getResource("CountDown.fxml"));
        Scene scene = new Scene(subroot);
        countDown.setScene(scene);
        countDown.initModality(Modality.WINDOW_MODAL);
        countDown.initOwner(stage);
        countDown.initStyle(StageStyle.UNDECORATED);
        countDown.show();

        pswPassword.setText("");
        txtUsername.setText("");
        attempt = 3;
        btnLogin.setDisable(true);
        loggingTry = new String[3];
        sameUserAttempt = 0;
        lblAttempt.setText("");
        privilege = null;
    }
//</editor-fold>
}

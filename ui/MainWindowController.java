/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static ui.LoginUIController.logedUser;
import static ui.Main.stage;
import util.HibernateUtil;
import util.SystemAlert;

/**
 *
 * @author SahanRid
 */
public class MainWindowController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Static variables">
    public static File lastDirectory;
    public static String selectedModule;
    public static String selectedModuleCategory;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML Data">
    private ToggleGroup tglAdmin;
    private ToggleGroup tglProduction;
    private ToggleGroup tglSupportive;
    @FXML
    private ToggleButton mtmEmployee;
    @FXML
    private AnchorPane apnMain;
    @FXML
    private ImageView imgHomeBackground;
    @FXML
    private ToggleButton mtmUser;
    @FXML
    private ToggleButton mtmPrililege;
    @FXML
    private ToggleButton mtmLoginReset;
    @FXML
    private ToggleButton mtmAssignment;
    @FXML
    private ToggleButton mtmTapping;
    @FXML
    private ToggleButton mtmProduction;
    @FXML
    private ToggleButton mtmSales;
    @FXML
    private ToggleButton mtmComplain;
    @FXML
    private ToggleButton mtmTreatment;
    @FXML
    private ToggleButton mtmTree;
    @FXML
    private ToggleButton mtmFertilize;
    @FXML
    private ImageView imgUserphoto;
    @FXML
    private Label lblUsername;
    @FXML
    private ToggleButton tglHome;
    @FXML
    private ToggleButton tglLogout;
    @FXML
    private Label lblAccountSetting;
    @FXML
    private ToggleButton mtmTreeBlock;
    @FXML
    private ToggleButton tglDailyTappingDash;
    @FXML
    private ToggleButton tglProductionDash;
    @FXML
    private ToggleButton tglTreatmentDash;
    @FXML
    private ToggleButton tglAssignmentDash;
    @FXML
    private ToggleButton tglComplainDash;
    @FXML
    private ToggleButton tglReportDash;
    @FXML
    private Pane paneDash;
    @FXML
    private TitledPane tpAdmin;
    @FXML
    private TitledPane tpProduction;
    @FXML
    private TitledPane tpSupportive;
    @FXML
    private TitledPane tpReport;
    @FXML
    private ToggleGroup tggModule;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Initialize">
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (logedUser.getEmployeeId().getImage() != null) {
            imgUserphoto.setImage(new Image(new ByteArrayInputStream(logedUser.getEmployeeId().getImage())));
        } else {
            imgUserphoto.setImage(new Image("/images/avatar.jpg"));
        }

        if (logedUser.getEmployeeId().getName() != null) {
            lblUsername.setText(logedUser.getEmployeeId().getName());
        } else {
            lblUsername.setText("");
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Logout & Home">
    @FXML
    private void tglHomeMC(MouseEvent event) throws IOException {
        apnMain.getChildren().clear();
        apnMain.getChildren().add(paneDash);
        tglHome.setSelected(false);
        tpAdmin.setExpanded(false);
        tpProduction.setExpanded(false);
        tpSupportive.setExpanded(false);
        tpReport.setExpanded(false);
        tggModule.selectToggle(null);
    }

    @FXML
    private void tglLogoutMC(MouseEvent event) throws IOException {
        Alert alert = SystemAlert.createAlert(
                "Logout",
                "Logout from current user session",
                "Are you sure you want to logout ?",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            logedUser = null;
            stage.close();

            stage = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
            Scene scene = new Scene(root);

            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Icon.jpg")));
            stage.setTitle(" St. Vincent Estaete ");

            String css = Main.class.getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(css);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        }
        tglLogout.setSelected(false);
        tglLogout.setFocusTraversable(true);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Module Buttons">
    private void titlePaneAction(String moduleName, TitledPane titledPane, ToggleButton MenuButton) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource(moduleName));
        apnMain.getChildren().clear();
        apnMain.getChildren().add(root);
        titledPane.setExpanded(true);
        tggModule.selectToggle(MenuButton);
    }

    @FXML
    private void mtmEmployeeMC(MouseEvent event) throws IOException {
        titlePaneAction("EmployeeUI.fxml", tpAdmin, mtmEmployee);
    }

    @FXML
    private void mtmUserMC(MouseEvent event) throws IOException {
        titlePaneAction("UserUI.fxml", tpAdmin, mtmUser);
    }

    @FXML
    private void mtmPrililegeMC(MouseEvent event) throws IOException {
        titlePaneAction("PrivilegeUI.fxml", tpAdmin, mtmPrililege);
    }

    @FXML
    private void mtmLoginResetMC(MouseEvent event) throws IOException {
        titlePaneAction("LoginResetUI.fxml", tpAdmin, mtmLoginReset);
    }

    @FXML
    private void mtmAssignmentMC(MouseEvent event) throws IOException {
        titlePaneAction("AssignmentUI.fxml", tpProduction, mtmAssignment);
    }

    @FXML
    private void mtmTappingMC(MouseEvent event) throws IOException {
        titlePaneAction("DailyTappingUI.fxml", tpProduction, mtmTapping);
    }

    @FXML
    private void mtmProductionMC(MouseEvent event) throws IOException {
        titlePaneAction("ProductionUI.fxml", tpProduction, mtmProduction);
    }

    @FXML
    private void mtmSalesMC(MouseEvent event) throws IOException {
        titlePaneAction("SalesUI.fxml", tpSupportive, mtmSales);
    }

    @FXML
    private void mtmComplainMC(MouseEvent event) throws IOException {
        titlePaneAction("ComplainUI.fxml", tpSupportive, mtmComplain);
    }

    @FXML
    private void mtmTreatmentMC(MouseEvent event) throws IOException {
        titlePaneAction("TreatmentUI.fxml", tpSupportive, mtmTreatment);
    }

    @FXML
    private void mtmTreeMC(MouseEvent event) throws IOException {
        titlePaneAction("TreeUI.fxml", tpSupportive, mtmTree);

    }

    @FXML
    private void mtmFertilizeMC(MouseEvent event) throws IOException {
        titlePaneAction("FertilizeUI.fxml", tpSupportive, mtmFertilize);
    }

    @FXML
    private void mtmTreeBlockMC(MouseEvent event) throws IOException {
        titlePaneAction("TreeBlockUI.fxml", tpSupportive, mtmTreeBlock);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Account Settings">
    private void accountSettings() throws IOException {
        if (!"Admin".equals(logedUser.getUsername())) {
            AnchorPane root = FXMLLoader.load(getClass().getResource("AccountSettingUI.fxml"));
            apnMain.getChildren().clear();
            apnMain.getChildren().add(root);
            tpAdmin.setExpanded(false);
            tpProduction.setExpanded(false);
            tpSupportive.setExpanded(false);
            tpReport.setExpanded(false);
            tggModule.selectToggle(null);
        } else {
            Alert alert = SystemAlert.createAlert(
                    "User Settings",
                    "Admin",
                    "Your are logged as the 'System Admin'.\nAdmin doesn't have any user account setting",
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    private void lblAccountSettingMC(MouseEvent event) throws IOException {
        accountSettings();
    }

    @FXML
    private void imgUserMC(MouseEvent event) throws IOException {
        accountSettings();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Dasboard Actions">
    private void dashAction(ToggleButton menuButton, String moduleName, TitledPane titledPane, ToggleButton dashButton) throws IOException {
        selectedModule = menuButton.getText();
        AnchorPane root = FXMLLoader.load(getClass().getResource(moduleName));
        apnMain.getChildren().clear();
        apnMain.getChildren().add(root);
        titledPane.setExpanded(true);
        tggModule.selectToggle(menuButton);
        dashButton.setSelected(false);
    }

    @FXML
    private void tglDailyTappingDashMC(MouseEvent event) throws IOException {
        dashAction(mtmTapping, "DailyTappingUI.fxml", tpProduction, tglDailyTappingDash);
    }

    @FXML
    private void tglProductionDashMC(MouseEvent event) throws IOException {
        dashAction(mtmProduction, "ProductionUI.fxml", tpProduction, tglProductionDash);
    }

    @FXML
    private void tglTreatmentDashMC(MouseEvent event) throws IOException {
        dashAction(mtmTreatment, "TreatmentUI.fxml", tpSupportive, tglTreatmentDash);

    }

    @FXML
    private void tglAssignmentDashMC(MouseEvent event) throws IOException {
        dashAction(mtmAssignment, "AssignmentUI.fxml", tpProduction, tglAssignmentDash);
    }

    @FXML
    private void tglComplainDashMC(MouseEvent event) throws IOException {
        dashAction(mtmComplain, "ComplainUI.fxml", tpSupportive, tglComplainDash);
    }

    @FXML
    private void tglReportDashMC(MouseEvent event) {
        //dashAction(mtmComplainTreatment, "ComplainTreatmentUI.fxml", tpSupportive, tglComplainDash);
    }

}
//</editor-fold>

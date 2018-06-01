/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CivilstatusDao;
import dao.DesignationDao;
import dao.EmployeestatusDao;
import dao.GenderDao;
import dao.UserDao;
import entity.Civilstatus;
import entity.Designation;
import entity.Employee;
import entity.Employeestatus;
import entity.Gender;
import entity.Role;
import entity.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import static ui.LoginUIController.logedUser;
import util.Security;
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class AccountSettingUIController implements Initializable {

    static Stage subUI;

//<editor-fold defaultstate="collapsed" desc="Form Data">
    User tempUser;
    User oldTempUser;

    String initial;
    String valid;
    String invalid;
    String updated;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML Data">
    @FXML
    private Label lblUsername;
    @FXML
    private TextField txtUsername;
    @FXML
    private ListView<Role> lstRoles;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<Civilstatus> cmbCivilstatus;
    @FXML
    private DatePicker dtpDob;
    @FXML
    private TextField txtNic;
    @FXML
    private ComboBox<Gender> cmbGender;
    @FXML
    private TextArea txtAddress;
    @FXML
    private TextField txtMobile;
    @FXML
    private TextField txtLand;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<Designation> cmbDesignation;
    @FXML
    private DatePicker dtpAssign;
    @FXML
    private ComboBox<Employeestatus> cmbEmployeestatus;
//</editor-fold>

    /**
     * Initializes the controller class.
     */
//<editor-fold defaultstate="collapsed" desc="Initialize">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        loadForm();
        loadDetails();
        lblUsername.setText(logedUser.getUsername() + " (" + logedUser.getEmployeeId().getName() + ")");
    }

    private void loadForm() {
        tempUser = (User) UserDao.getUsername(LoginUIController.logedUser.getUsername());
        oldTempUser = (User) UserDao.getUsername(LoginUIController.logedUser.getUsername());

        txtName.setText(tempUser.getEmployeeId().getName());
        txtUsername.setText(tempUser.getUsername());
//
//        pswOldpassword.setText("");
//        pswPassword.setText("");
//        pswConfirmPassword.setText("");
//        txtHint.setText("");

        if (tempUser.getEmployeeId().getImage() != null) {
            imgPhoto.setImage(new Image(new ByteArrayInputStream(tempUser.getEmployeeId().getImage())));
        } else {
            imgPhoto.setImage(new Image("/images/avatar.jpg"));
        }

        lstRoles.setItems(FXCollections.observableArrayList(tempUser.getRoleList()));

//        disableButtons(true);
//        setStyle(initial);
    }

    private void loadDetails() {

        Employee employee;
        employee = tempUser.getEmployeeId();

        cmbGender.getSelectionModel().select((Gender) employee.getGenderId());
        cmbCivilstatus.getSelectionModel().select((Civilstatus) employee.getCivilstatusId());
        cmbDesignation.getSelectionModel().select((Designation) employee.getDesignationId());
        cmbEmployeestatus.getSelectionModel().select((Employeestatus) employee.getEmployeestatusId());

        txtAddress.setText(employee.getAddress());
        txtNic.setText(employee.getNic());
        txtMobile.setText(employee.getMobile());
        txtLand.setText(employee.getLand());
        txtEmail.setText(employee.getEmail());

        dtpDob.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob())));
        dtpDob.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob()));
        dtpDob.setEditable(false);
        dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned())));
        dtpAssign.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned()));
        dtpAssign.setEditable(false);

    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Binding Methods">
    @FXML
    private void txtUsernameKR(KeyEvent event) {
    }
//</editor-fold>

    @FXML
    private void lblChangePasswordMC(MouseEvent event) {
        try {
            subUI = new Stage();
            subUI.initOwner(ui.Main.stage);
            AnchorPane root = FXMLLoader.load(Main.class.getResource("ChangePassword.fxml"));
            Scene scene = new Scene(root);
            subUI.setScene(scene);
            subUI.initModality(Modality.WINDOW_MODAL);
            subUI.initStyle(StageStyle.UNDECORATED);
            subUI.initOwner(ui.Main.stage);
            subUI.showAndWait();
        } catch (IOException e) {
            System.out.println("Report Error");
        }
    }

}

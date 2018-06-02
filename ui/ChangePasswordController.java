/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.UserDao;
import entity.User;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import static ui.AccountSettingUIController.subUI;
import static ui.LoginUIController.logedUser;
import static ui.Main.stage;
import util.Security;
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class ChangePasswordController implements Initializable {

    double screenX, screenY;

    User tempUser;
    User oldTempUser;

    String initial;
    String valid;
    String invalid;
    String updated;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private PasswordField pswOldpassword;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private PasswordField pswConfirmPassword;
    @FXML
    private TextField txtHint;
    @FXML
    private Pane root;
    @FXML
    private ImageView imgClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        btnAdd.setDisable(true);

        tempUser = (User) UserDao.getUsername(LoginUIController.logedUser.getUsername());
        oldTempUser = (User) UserDao.getUsername(LoginUIController.logedUser.getUsername());
    }

    private void setStyle(String style) {

        pswConfirmPassword.setStyle(style);
        pswOldpassword.setStyle(style);
        pswPassword.setStyle(style);
        txtHint.setStyle(style);
    }

    private void disableButtons(boolean insert) {
        btnAdd.setDisable(insert);
    }

    @FXML
    private void pswOldpasswordKR(KeyEvent event) {
        if (!pswOldpassword.getText().isEmpty()) {
            if (logedUser.getPassword().equals(Security.getHash(pswOldpassword.getText()))) {
                disableButtons(false);
            } else {
                disableButtons(true);
            }

            if (!pswPassword.getText().isEmpty() && !pswConfirmPassword.getText().isEmpty()) {
                pswConfirmPasswordKR(event);
            }
        } else {
            disableButtons(true);
        }
    }

    @FXML
    private void pswPasswordKR(KeyEvent event) {
        if (!pswPassword.getText().isEmpty()) {
            if (tempUser.setPassword(pswPassword.getText().trim())) {
                if (oldTempUser != null && !tempUser.getPassword().equals(oldTempUser.getPassword())) {
                    pswPassword.setStyle(valid);
                } else {
                    pswPassword.setStyle(invalid);
                }
            } else {
                pswPassword.setStyle(invalid);
            }
        } else {
            pswPassword.setStyle(initial);
        }
    }
	

    @FXML
    private void pswConfirmPasswordKR(KeyEvent event) {
        if (!pswConfirmPassword.getText().isEmpty()) {
            if (pswPassword.getText().trim().equals(pswConfirmPassword.getText().trim())) {
                pswConfirmPassword.setStyle(valid);
                disableButtons(false);
            } else {
                pswConfirmPassword.setStyle(invalid);
                disableButtons(true);
            }
        } else {
            pswConfirmPassword.setStyle(initial);
            disableButtons(true);
        }
    }

    @FXML
    private void txtHintKR(KeyEvent event) {
//        if (tempUser.setHint(txtHint.getText())) {
//            if (oldTempUser != null && oldTempUser.getHint() != null && tempUser.getHint() != null && oldTempUser.getHint().equals(tempUser.getHint())) {
//                txtLand.setStyle(valid);
//            } else if (oldTempUser != null && oldTempUser.getHint() != tempUser.getHint()) {
//                txtLand.setStyle(updated);
//            } else {
//                txtLand.setStyle(valid);
//            }
//        } else {
//            txtLand.setStyle(invalid);
//        }
    }

//<editor-fold defaultstate="collapsed" desc="Operation Methods">
    @FXML
    private void btnAddAP(ActionEvent event) {
        String error = getErrors();

        if (error.isEmpty()) {
            String updates = getUpdates();

            if (updates.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update User");
                alert.setHeaderText("Nothing updates");
                alert.setContentText(updates);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update User");
                alert.setHeaderText("Are you sure you want to update your user details ?");
                alert.setContentText(updates);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    UserDao.update(tempUser);
                    //loadForm();
                    logedUser = (User) UserDao.getUsername(LoginUIController.logedUser.getUsername());
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add - Error");
            alert.setHeaderText("Correct the error");
            alert.setContentText(error);
            alert.showAndWait();
        }
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
    }

    private String getErrors() {
        String errors = "";

        if (!logedUser.getPassword().equals(Security.getHash(pswOldpassword.getText()))) {
            errors = errors + "Please enter current password correctly.\n";
        }
        if (oldTempUser.getPassword().equals(tempUser.getPassword()) && !pswPassword.getText().isEmpty()) {
            errors = errors + "Please type valide password as new one. New password should not be the current password.\n";
        }
        if (pswPassword.getText().isEmpty() || pswConfirmPassword.getText().isEmpty()) {
            errors = errors + "Enter and confirm new password.\n";
        }
        if (!pswPassword.getText().equals(pswConfirmPassword.getText())) {
            errors = errors + "Retype new password correctly to confirm.\n";
        }

        return errors;
    }

    private String getUpdates() {
        String updates = "";

        try {

            if (oldTempUser != null) {
                if (!tempUser.getPassword().isEmpty()) {
                    updates = updates + "New password " + pswPassword.getText() + "\n";
                }
                if (tempUser.getHint().equals(oldTempUser.getHint())) {
                    updates = updates + " Hint is not changed\n";
                }
                if (tempUser.getHint() != null && !tempUser.getHint().equals(oldTempUser.getHint())) {
                    updates = updates + oldTempUser.getHint() + " chnaged to " + tempUser.getHint() + "\n";
                }
            }
        } catch (Exception e) {
            System.out.println("\n\n----------------------Update Checking Error---------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Update checking Error", JOptionPane.ERROR_MESSAGE);
        }

        return updates;
    }

//</editor-fold>
    @FXML
    private void rootMD(MouseEvent event) {
        subUI.setX(screenX + event.getScreenX());
        subUI.setY(screenY + event.getScreenY());
    }

    @FXML
    private void rootMP(MouseEvent event) {
        screenX = subUI.getX() - event.getScreenX();
        screenY = subUI.getY() - event.getScreenY();
    }

    @FXML
    private void imgCloseMC(MouseEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Password",
                "Change Password",
                "Your password haven't changed Are you sure that you want to close the dialog ?.",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            subUI.close();
        }
    }
}

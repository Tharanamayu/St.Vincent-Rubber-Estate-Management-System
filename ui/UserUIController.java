/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.EmployeeDao;
import dao.RoleDao;
import dao.UserDao;
import dao.UserstatusDao;
import entity.Employee;
import entity.Role;
import entity.User;
import entity.Userstatus;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class UserUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    User user;
    User oldUser;

    String initial;
    String valid;
    String invalid;
    String updated;

    int page;
    int row;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML Data">
    @FXML
    private ComboBox<Employee> cmbEmployee;
    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Label lblRoleNew;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private PasswordField pswConfirmPassword;
    @FXML
    private ListView<Role> lstLeft;
    @FXML
    private ListView<Role> lstRight;
    @FXML
    private Button btnRight;
    @FXML
    private Button btnRightAll;
    @FXML
    private Button btnLeft;
    @FXML
    private Button btnLeftAll;
    @FXML
    private TableView<User> tblUser;
    @FXML
    private TableColumn<Employee, String> colEmployeeName;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colUserStatus;
    @FXML
    private Pagination pagination;
    @FXML
    private TextField txtSearchName;
    @FXML
    private ComboBox<Role> cmbSearchRole;
    @FXML
    private Button btnSearchClear;
    @FXML
    private TextField txtSearchUsername;
    @FXML
    private ImageView imgPhoto;
    @FXML
    private ComboBox<Userstatus> cmbUserStatus;

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
        loadTable();

    }

    private void loadForm() {
        user = new User();
        oldUser = null;

        user.setUserstatusId((Userstatus) UserstatusDao.getAll().get(0));

        cmbEmployee.setItems(EmployeeDao.getAllExceptUser());
        cmbEmployee.getSelectionModel().clearSelection();

        txtUsername.setText("");
        pswPassword.setText("");
        pswConfirmPassword.setText("");

        cmbUserStatus.setItems(UserstatusDao.getAll());
        cmbUserStatus.getSelectionModel().clearSelection();

        lstLeft.setItems(RoleDao.getAll());
        lstRight.getItems().clear();

        imgPhoto.setImage(new Image("/images/avatar.jpg"));

        setStyle(initial);
        disableButtons(true, false, true, true);

        validateList();
    }

    private void setStyle(String style) {
        cmbEmployee.setStyle(style);
        txtUsername.setStyle(style);
        pswPassword.setStyle(style);
        pswConfirmPassword.setStyle(style);
        cmbUserStatus.setStyle(style);

        lstLeft.setStyle(style);
        lstRight.setStyle(style);

    }

    private void disableButtons(boolean select, boolean add, boolean update, boolean delete) {
        btnAdd.setDisable(add);
        btnUpdate.setDisable(update);
        btnDelete.setDisable(delete);
    }

    private void loadTable() {
        cmbSearchRole.setItems(RoleDao.getAll());
        cmbSearchRole.getSelectionModel().clearSelection();
        txtSearchName.setText("");
        txtSearchUsername.setText("");

        colEmployeeName.setCellValueFactory(new PropertyValueFactory("employeeId"));
        colUsername.setCellValueFactory(new PropertyValueFactory("username"));
        colUserStatus.setCellValueFactory(new PropertyValueFactory("userstatusId"));

        fillTable(UserDao.getAll());
    }

    private void fillTable(ObservableList<User> users) {
        if (users != null && users.size() != 0) {

            int rowsCount = 6;
            int pageCount = ((users.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? users.size() : pageIndex * rowsCount + rowsCount;
                    tblUser.getItems().clear();
                    tblUser.setItems(FXCollections.observableArrayList(users.subList(start, end)));
                    return tblUser;
                }
            });

        } else {

            pagination.setPageCount(1);
            tblUser.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblUser.getSelectionModel().select(row);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Binding">
    @FXML
    private void cmbEmployeeAP(ActionEvent event) {
        boolean validity = user.setEmployeeId(cmbEmployee.getSelectionModel().getSelectedItem());
        if (validity) {
            if (oldUser != null && !oldUser.getEmployeeId().equals(user.getEmployeeId())) {
                cmbEmployee.setStyle(updated);
            } else {
                cmbEmployee.setStyle(valid);
            }
        } else {
            cmbEmployee.setStyle(invalid);
        }
    }

    @FXML
    private void txtUsernameKR(KeyEvent event) {
        if (user.setUsername(txtUsername.getText().trim()) && UserDao.getUsername(txtUsername.getText().trim()) == null) {
            if (oldUser != null && !user.getUsername().equals(oldUser.getUsername())) {
                txtUsername.setStyle(updated);
            } else {
                txtUsername.setStyle(valid);
            }
        } else {
            txtUsername.setStyle(invalid);
        }
    }

    @FXML
    private void pswPasswordKR(KeyEvent event) {
        if (user.setPassword(pswPassword.getText().trim())) {
            if (oldUser != null && !user.getPassword().equals(oldUser.getPassword())) {
                pswPassword.setStyle(updated);
            } else {
                pswPassword.setStyle(valid);
            }
        } else {
            pswPassword.setStyle(invalid);
        }
    }

    @FXML
    private void pswConfirmPasswordKR(KeyEvent event) {
        if (pswPassword.getText().trim().equals(pswConfirmPassword.getText().trim())) {
            pswConfirmPassword.setStyle(valid);
        } else {
            pswConfirmPassword.setStyle(invalid);
        }
    }

    @FXML
    private void cmbUserStatusAP(ActionEvent event) {
        boolean validity = user.setUserstatusId(cmbUserStatus.getSelectionModel().getSelectedItem());
        if (validity) {
            if (oldUser != null && !oldUser.getUserstatusId().equals(user.getUserstatusId())) {
                cmbUserStatus.setStyle(updated);
            } else {
                cmbUserStatus.setStyle(valid);
            }
        } else {
            cmbUserStatus.setStyle(invalid);
        }
    }

    private void validateList() {
        if (user.setRoleList(lstRight.getItems())) {
            lstRight.setStyle(valid);
        } else {
            lstRight.setStyle(invalid);

        }
        if (lstLeft.getItems().isEmpty()) {
            btnRight.setDisable(true);
            btnRightAll.setDisable(true);
            btnLeft.setDisable(false);
            btnLeftAll.setDisable(false);
        } else if (lstRight.getItems().isEmpty()) {
            btnRight.setDisable(false);
            btnRightAll.setDisable(false);
            btnLeft.setDisable(true);
            btnLeftAll.setDisable(true);
        } else {
            btnRight.setDisable(false);
            btnRightAll.setDisable(false);
            btnLeft.setDisable(false);
            btnLeftAll.setDisable(false);

        }
    }

    @FXML
    private void lblRoleNewMC(MouseEvent event) {
    }

    @FXML
    private void btnrightAP(ActionEvent event) {
        lstRight.getItems().addAll(lstLeft.getSelectionModel().getSelectedItems());
        lstLeft.getItems().removeAll(lstLeft.getSelectionModel().getSelectedItems());
        validateList();
    }

    @FXML
    private void btnRightAllAP(ActionEvent event) {
        lstRight.setItems(RoleDao.getAll());
        lstLeft.getItems().clear();
        validateList();
    }

    @FXML
    private void btnLeftAP(ActionEvent event) {
        lstLeft.getItems().addAll(lstRight.getSelectionModel().getSelectedItems());
        lstRight.getItems().removeAll(lstRight.getSelectionModel().getSelectedItems());
        validateList();
    }

    @FXML
    private void btnLeftAllAP(ActionEvent event) {
        lstLeft.setItems(RoleDao.getAll());
        lstRight.getItems().clear();
        validateList();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Operation">
    @FXML
    private void btnClearAP(ActionEvent event) {
        loadForm();
    }

    private String getError() {
        String errors = "";

        if (user.getEmployeeId() == null) {
            errors = errors + "Employee \t\tis Not Selected\n";
        }
        if (user.getUsername() == null) {
            errors = errors + "Username \t\tis Invalid\n";
        }
        if (user.getPassword() == null) {
            errors = errors + "Password \t\tis Invalid\n";
        }
        if (!pswPassword.getText().equals(pswConfirmPassword.getText())) {
            errors = errors + "Confirm Password \t\tis Mismatch\n";
        }
        if (user.getUserstatusId() == null) {
            errors = errors + "User status \t\tis Not Selected\n";
        }
        if (user.getRoleList() == null) {
            errors = errors + "Roles \tare Not Selected\n";
        }
        return errors;
    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        String errors = getError();

        if (errors.isEmpty()) {
            String details = "\nEmployee name\t:\t" + user.getEmployeeId().getName()
                    + "\nUser Name\t\t:\t" + user.getUsername()
                    + "\nUser status  \t\t: \t" + user.getUserstatusId().getName()
                    + "\nRoles\t\t\t:\t" + user.getRoleList();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add User");
            alert.setHeaderText("Are you sure you want to add the following User ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                UserDao.add(user);
                loadForm();
                fillTable(UserDao.getAll());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add - Error");
            alert.setHeaderText("Correct the error");
            alert.setContentText(errors);
            alert.showAndWait();
        }
    }

    private void fillForm() {
        if (tblUser.getSelectionModel().getSelectedItem() != null) {

            oldUser = UserDao.getById(tblUser.getSelectionModel().getSelectedItem().getId());
            user = UserDao.getById(tblUser.getSelectionModel().getSelectedItem().getId());

            cmbEmployee.getItems().clear();
            cmbEmployee.getItems().add(oldUser.getEmployeeId());
            cmbEmployee.getSelectionModel().select(oldUser.getEmployeeId());

            txtUsername.setText(oldUser.getUsername());
            pswPassword.setText(oldUser.getPassword());
            pswConfirmPassword.setText(oldUser.getPassword());

            cmbUserStatus.getSelectionModel().select(oldUser.getUserstatusId());

            lstRight.setItems(FXCollections.observableArrayList(oldUser.getRoleList()));
            lstLeft.setItems(RoleDao.getAll());
            lstLeft.getItems().removeAll(FXCollections.observableArrayList(oldUser.getRoleList()));

            if (user.getEmployeeId().getImage() != null) {
                imgPhoto.setImage(new Image(new ByteArrayInputStream(oldUser.getEmployeeId().getImage())));
            } else {
                imgPhoto.setImage(new Image("/images/avatar.jpg"));
            }

            setStyle(valid);
            disableButtons(true, true, false, false);

            validateList();

        }
    }

    @FXML
    private void tblEmployeeMC(MouseEvent event) {
        fillForm();
    }

    @FXML
    private void tblEmployeeKR(KeyEvent event) {
        fillForm();
    }

    private String getUpdates() {
        String updates = "";

        try {

            if (oldUser != null) {

                if (user.getUsername() != null && !user.getUsername().equals(oldUser.getUsername())) {
                    updates = updates + "Current user name " + oldUser.getUsername() + " chnaged to new user name " + user.getUsername() + "\n";
                }
                if (user.getPassword() != null && !user.getPassword().equals(oldUser.getPassword())) {
                    updates = updates + "Current password changed to new password\n";
                }
                if (user.getUserstatusId().getName() != null && !user.getUserstatusId().equals(oldUser.getUserstatusId())) {
                    updates = updates + "Current user status " + oldUser.getUserstatusId().getName() + " chnaged to new user status " + user.getUserstatusId().getName() + "\n";
                }
                if (user.getRoleList() != null && !user.getRoleList().equals(oldUser.getRoleList())) {
                    updates = updates + "Curent role list " + oldUser.getRoleList() + "\nchnaged to new role list" + user.getRoleList() + "\n";
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

    @FXML
    private void btnUpdateAP(ActionEvent event) {
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
            alert.setHeaderText("Are you sure you want to update the following User ?");
            alert.setContentText(updates);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                UserDao.update(user);
                loadForm();
                fillTable(UserDao.getAll());
            }
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (tblUser.getSelectionModel().getSelectedItem() != null) {
            String details = "\nEmployee name\t:\t" + user.getEmployeeId().getName()
                    + "\nUser Name\t\t:\t" + user.getUsername()
                    + "\nRoles\t\t\t:\t" + user.getRoleList();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete User");
            alert.setHeaderText("Are you sure you want to delete the following User ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                UserDao.delete(user);
                loadForm();
                fillTable(UserDao.getAll());
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search">
    @FXML
    private void txtSearchNameKR(KeyEvent event) {
        if (txtSearchName.getText() != null) {
            fillTable(UserDao.getAllByName(txtSearchName.getText()));
            txtSearchUsername.setText("");
            cmbSearchRole.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void txtSearchUsernameKR(KeyEvent event) {
        if (txtSearchUsername.getText() != null) {
            fillTable(UserDao.getAllByUsername(txtSearchUsername.getText()));
            txtSearchName.setText("");
            cmbSearchRole.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void cmbSearchRoleAP(ActionEvent event) {
        if (cmbSearchRole.getSelectionModel().getSelectedItem() != null) {
            fillTable(UserDao.getAllByRole(cmbSearchRole.getSelectionModel().getSelectedItem()));
            txtSearchName.setText("");
            txtSearchUsername.setText("");
        }
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
    }
//</editor-fold>

}

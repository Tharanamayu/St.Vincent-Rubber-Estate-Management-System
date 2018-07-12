/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.ModuleDao;
import dao.PrivilegeDao;
import dao.RoleDao;
import entity.Module;
import entity.Privilege;
import entity.Role;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class PrivilegeUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    Privilege privilege;
    Privilege oldPrivilege;

    String initial;
    String valid;
    String invalid;
    String updated;

    int page;
    int row;

    boolean photoSelected;
    boolean lock;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML Data">
    @FXML
    private ComboBox<Role> cmbRole;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private ComboBox<Module> cmbModule;
    @FXML
    private CheckBox cbxSelect;
    @FXML
    private CheckBox cbxInsert;
    @FXML
    private CheckBox cbxUpdate;
    @FXML
    private CheckBox cbxDelete;
    @FXML
    private TableView<Privilege> tblPrivilege;
    @FXML
    private TableColumn<Privilege, Role> colRole;
    @FXML
    private TableColumn<Privilege, Module> colModule;
    @FXML
    private TableColumn<Privilege, Integer> colSelect;
    @FXML
    private TableColumn<Privilege, Integer> colInsert;
    @FXML
    private TableColumn<Privilege, Integer> colUpdate;
    @FXML
    private TableColumn<Privilege, Integer> colDelete;
    @FXML
    private Pagination pagination;
    @FXML
    private ComboBox<Role> cmbSearchRole;
    @FXML
    private Button btnSearchClear;
    @FXML
    private ComboBox<Module> cmbSearchModule;
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
        privilege = new Privilege();
        oldPrivilege = null;

        cmbRole.setItems(RoleDao.getAll());
        cmbRole.getSelectionModel().clearSelection();
        cmbModule.setItems(/*dao.ModulaDao.getAll()*/null);
        cmbModule.getSelectionModel().clearSelection();

        //cbxSelect.setSelected(true);
        //cbxSelect.setDisable(true);
        cbxSelect.setSelected(false);
        cbxInsert.setSelected(false);
        cbxUpdate.setSelected(false);
        cbxDelete.setSelected(false);

        dissableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void setStyle(String style) {
        cmbRole.setStyle(style);
        cmbModule.setStyle(style);
    }

    private void dissableButtons(boolean select, boolean add, boolean update, boolean delete) {
        btnAdd.setDisable(add);
        btnUpdate.setDisable(update);
        btnDelete.setDisable(delete);
    }

    private void loadTable() {
        cmbSearchRole.setItems(RoleDao.getAll());
        cmbSearchRole.getSelectionModel().clearSelection();
        cmbSearchModule.setItems(ModuleDao.getAll());
        cmbSearchModule.getSelectionModel().clearSelection();

        colRole.setCellValueFactory(new PropertyValueFactory("roleId"));
        colModule.setCellValueFactory(new PropertyValueFactory("modulaId"));
        colSelect.setCellValueFactory(new PropertyValueFactory("sel"));
        colInsert.setCellValueFactory(new PropertyValueFactory("ins"));
        colUpdate.setCellValueFactory(new PropertyValueFactory("upd"));
        colDelete.setCellValueFactory(new PropertyValueFactory("del"));

        fillTable(PrivilegeDao.getAll());

    }

    private void fillTable(ObservableList<Privilege> privileges) {
        if (privileges != null && privileges.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((privileges.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? privileges.size() : pageIndex * rowsCount + rowsCount;
                    tblPrivilege.getItems().clear();
                    tblPrivilege.setItems(FXCollections.observableArrayList(privileges.subList(start, end)));
                    return tblPrivilege;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblPrivilege.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblPrivilege.getSelectionModel().select(row);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Binding">
    @FXML
    private void cmbRoleAP(ActionEvent event) {
        if (cmbRole.getSelectionModel().getSelectedItem() != null) {
            privilege.setRoleId(cmbRole.getSelectionModel().getSelectedItem());
            cmbModule.setItems(ModuleDao.getAllUnssignedToRole(cmbRole.getSelectionModel().getSelectedItem()));
        }
    }

    @FXML
    private void cmbModuleAP(ActionEvent event) {
        if (cmbModule.getSelectionModel().getSelectedItem() != null) {
            privilege.setModuleId(cmbModule.getSelectionModel().getSelectedItem());
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CRUD">
    @FXML
    private void btnClearAP(ActionEvent event) {
        loadForm();
    }

    public String getErrors() {

        String errors = "";

        if (privilege.getRoleId() == null) {
            errors = errors + "Role \t\tis Not Selected\n";
        }
        if (privilege.getModuleId() == null) {
            errors = errors + "Module \tis Not Selected\n";
        }
        if (!(cbxSelect.isSelected() || cbxInsert.isSelected() || cbxUpdate.isSelected() || cbxDelete.isSelected())) {
            errors = errors + "Privilege \t\tis Not Selected\n";
        }

        return errors;

    }

    @FXML
    private void btnAddAP(ActionEvent event) {
        privilege.setSel(cbxSelect.isSelected() ? 1 : 0);
        privilege.setIns(cbxInsert.isSelected() ? 1 : 0);
        privilege.setUpd(cbxUpdate.isSelected() ? 1 : 0);
        privilege.setDel(cbxDelete.isSelected() ? 1 : 0);

        String error = getErrors();

        if (error.isEmpty()) {
            String details = "\nRole :  \t\t" + privilege.getRoleId().getName()
                    + "\nModule :  \t\t" + privilege.getModuleId().getName()
                    + "\nSlect :   \t\t" + privilege.getSel()
                    + "\nInsert :       \t\t" + privilege.getIns()
                    + "\nUpdate :  \t\t" + privilege.getUpd()
                    + "\nDelete :      \t\t" + privilege.getDel();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Privileges");
            alert.setHeaderText("Are you sure you want to add the following Privilages ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                PrivilegeDao.add(privilege);
                loadForm();
                fillTable(PrivilegeDao.getAll());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add - Error");
            alert.setHeaderText("Correct the error");
            alert.setContentText(error);
            alert.showAndWait();
        }
    }

    private void fillForm() {
        dissableButtons(true, true, false, false);
        setStyle(valid);

        oldPrivilege = PrivilegeDao.getById(tblPrivilege.getSelectionModel().getSelectedItem().getId());
        privilege = PrivilegeDao.getById(tblPrivilege.getSelectionModel().getSelectedItem().getId());

        cmbRole.getItems().clear();
        cmbRole.setItems(FXCollections.observableArrayList((Role) privilege.getRoleId()));
        cmbRole.getSelectionModel().select(oldPrivilege.getRoleId());

        cmbModule.getItems().clear();
        cmbModule.setItems(FXCollections.observableArrayList((Module) privilege.getModuleId()));
        cmbModule.getSelectionModel().select(oldPrivilege.getModuleId());

        cbxSelect.setSelected(privilege.getSel() == 1);
        cbxInsert.setSelected(privilege.getIns() == 1);
        cbxUpdate.setSelected(privilege.getUpd() == 1);
        cbxDelete.setSelected(privilege.getDel() == 1);
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

        privilege.setSel(cbxSelect.isSelected() ? 1 : 0);
        privilege.setIns(cbxInsert.isSelected() ? 1 : 0);
        privilege.setUpd(cbxUpdate.isSelected() ? 1 : 0);
        privilege.setDel(cbxDelete.isSelected() ? 1 : 0);

        String confermation = "";

        if (!privilege.getSel().equals(oldPrivilege.getSel())) {
            updates = updates + "Select Changed " + oldPrivilege.getSel() + " to " + privilege.getSel() + "\n";
        }

        if (!privilege.getIns().equals(oldPrivilege.getIns())) {
            updates = updates + "Insert Changed " + oldPrivilege.getIns() + " to " + privilege.getIns() + "\n";
        }

        if (!privilege.getUpd().equals(oldPrivilege.getUpd())) {
            updates = updates + "Update Changed " + oldPrivilege.getUpd() + " to " + privilege.getUpd() + "\n";
        }
        if (!privilege.getDel().equals(oldPrivilege.getDel())) {
            updates = updates + "Delete Changed " + oldPrivilege.getDel() + " to " + privilege.getDel() + "\n";
        }

        return updates;
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String updates = getUpdates();

        if (updates.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Module");
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
                PrivilegeDao.update(privilege);
                loadForm();
                fillTable(PrivilegeDao.getAll());
            }
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (tblPrivilege.getSelectionModel().getSelectedItem() != null) {
            String details = "\nRole  :  \t\t\t" + privilege.getRoleId().getName()
                    + "\nModule :  \t\t\t" + privilege.getModuleId().getName()
                    + "\nPrililge :   \t\t" + privilege.getSel() + privilege.getIns() + privilege.getUpd() + privilege.getDel();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Privilege");
            alert.setHeaderText("Are you sure you want to delete the following Privilege ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                PrivilegeDao.delete(privilege);
                loadForm();
                fillTable(PrivilegeDao.getAll());
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search">
    @FXML
    private void cmbSearchRoleAP(ActionEvent event) {
        if (cmbSearchRole.getSelectionModel().getSelectedItem() != null) {
            fillTable(PrivilegeDao.getAllByRole(cmbSearchRole.getSelectionModel().getSelectedItem()));
            cmbSearchModule.getSelectionModel().clearSelection();
        }
        //PrivilegeDao.getAllByRole();
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
    }

    @FXML
    private void cmbSearchModuleAP(ActionEvent event) {
        if (cmbSearchModule.getSelectionModel().getSelectedItem() != null) {
            fillTable(PrivilegeDao.getAllByModule(cmbSearchModule.getSelectionModel().getSelectedItem()));
            cmbSearchRole.getSelectionModel().clearSelection();
        }
        //PrivilegeDao.getAllByModule();
    }

//</editor-fold>
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.EmployeeDao;
import dao.TappingassignmentDao;
import dao.TreeblockDao;
import dao.WeedingassignmentDao;
import entity.Employee;
import entity.Tappingassignment;
import entity.Treeblock;
import entity.Weedingassignment;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import static ui.LoginUIController.privilege;
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class AssignmentUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    Tappingassignment tappingAssignment;
    Tappingassignment oldTappingAssignment;
    Tappingassignment exchangingTappingAssignment;

    Weedingassignment weedingAssignment;
    Weedingassignment oldWeedingAssignment;
    Weedingassignment exchangingWeedingAssignment;

    String initial;
    String valid;
    String invalid;
    String updated;

    int pageTP;
    int rowTP;

    int pageWD;
    int rowWD;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML Data">
    @FXML
    private TableView<Tappingassignment> tblTPTreeBlock;
    @FXML
    private TableColumn<Tappingassignment, Treeblock> colTPBlock;
    @FXML
    private TableColumn<Employee, Employee> colTPEmployee;
    @FXML
    private Pagination paginationTP;
    @FXML
    private ComboBox<Employee> cmbTPEmployee;
    @FXML
    private ComboBox<Treeblock> cmbTPBlock;
    @FXML
    private Button btnTPInsert;
    @FXML
    private Button btnTPUpdate;
    @FXML
    private Button btnTPDelete;
    @FXML
    private Button btnTPClear;
    @FXML
    private TableColumn<Tappingassignment, Date> colTPData;
    @FXML
    private TextField txtTPCurrentBlock;
    @FXML
    private TextField txtTPCurrentEmployee;
    @FXML
    private ComboBox<Treeblock> cmbTPNewBlock;
    @FXML
    private ComboBox<Employee> cmbTPNewEmployee;
    @FXML
    private Button btnTPExchange;
    @FXML
    private Button btnTPExchangeClear;
    @FXML
    private TableView<Weedingassignment> tblWDTreeBlock;
    @FXML
    private TableColumn<Weedingassignment, Treeblock> colWDBlock;
    @FXML
    private TableColumn<Weedingassignment, Employee> colWDEmployee;
    @FXML
    private TableColumn<Weedingassignment, String> colWDData;
    @FXML
    private Pagination paginationWD;
    @FXML
    private ComboBox<Employee> cmbWDEmployee;
    @FXML
    private ComboBox<Treeblock> cmbWDBlock;
    @FXML
    private Button btnWDInsert;
    @FXML
    private Button btnWDUpdate;
    @FXML
    private Button btnWDDelete;
    @FXML
    private Button btnWDClear;
    @FXML
    private TextField txtWDCurrentBlock;
    @FXML
    private TextField txtWDCurrentEmployee;
    @FXML
    private ComboBox<Treeblock> cmbWDNewBlock;
    @FXML
    private ComboBox<Employee> cmbWDNewEmployee;
    @FXML
    private Button btnWDExchange;
    @FXML
    private Button btnWDExchangeClear;
//</editor-fold>

    /**
     * Initializes the controller class.
     */
    @Override
//<editor-fold defaultstate="collapsed" desc="Initialise Methods">
    public void initialize(URL url, ResourceBundle rb) {
        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        loadTableTP();
        loadFormTP();
        loadTableWD();
        loadFormWD();
        btnTPExchange.setDisable(true);
        btnWDExchange.setDisable(true);
    }

    private void loadFormTP() {
        tappingAssignment = new Tappingassignment();
        oldTappingAssignment = null;
        exchangingTappingAssignment = null;

        cmbTPBlock.getItems().clear();
        cmbTPBlock.setItems(TreeblockDao.getAllExceptAssignedBlockTP());
        cmbTPBlock.getSelectionModel().clearSelection();

        cmbTPEmployee.getSelectionModel().clearAndSelect(0);
        cmbTPEmployee.getItems().clear();
        cmbTPEmployee.setItems(EmployeeDao.getAllAssignableEmployeeTP("Tapper"));
        cmbTPEmployee.getSelectionModel().clearSelection();

        txtTPCurrentBlock.setText("");
        txtTPCurrentEmployee.setText("");
        cmbTPNewEmployee.getItems().clear();
        cmbTPNewBlock.getItems().clear();

        disableButtonsTP(false, false, true, true);
        setStyleTP(initial);
    }

    private void setStyleTP(String style) {
        cmbTPBlock.setStyle(style);
        cmbTPEmployee.setStyle(style);
    }

    private void disableButtonsTP(boolean select, boolean insert, boolean update, boolean delete) {
        btnTPInsert.setDisable(insert || !privilege.get("TappingAssignment_insert"));
        btnTPUpdate.setDisable(update || !privilege.get("TappingAssignment_update"));
        btnTPDelete.setDisable(delete || !privilege.get("TappingAssignment_delete"));
    }

    private void loadTableTP() {
        colTPBlock.setCellValueFactory(new PropertyValueFactory("treeblockId"));
        colTPEmployee.setCellValueFactory(new PropertyValueFactory("employeeId"));
        colTPData.setCellValueFactory(new PropertyValueFactory("date"));

        fillTableTP(TappingassignmentDao.getAll());
        paginationTP.setCurrentPageIndex(0);
    }

    private void fillTableTP(ObservableList<Tappingassignment> tappingAssignment) {
        if (tappingAssignment != null && tappingAssignment.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((tappingAssignment.size() - 1) / rowsCount) + 1;
            paginationTP.setPageCount(pageCount);

            paginationTP.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? tappingAssignment.size() : pageIndex * rowsCount + rowsCount;
                    tblTPTreeBlock.getItems().clear();
                    tblTPTreeBlock.setItems(FXCollections.observableArrayList(tappingAssignment.subList(start, end)));
                    return tblTPTreeBlock;
                }
            });

        } else {
            paginationTP.setPageCount(1);
            tblTPTreeBlock.getItems().clear();
        }

        paginationTP.setCurrentPageIndex(pageTP);
        tblTPTreeBlock.getSelectionModel().select(rowTP);
    }

    private void loadFormWD() {
        weedingAssignment = new Weedingassignment();
        oldWeedingAssignment = null;
        exchangingWeedingAssignment = null;

        cmbWDBlock.getItems().clear();
        cmbWDBlock.setItems(TreeblockDao.getAllExceptAssignedBlockWD());
        cmbWDBlock.getSelectionModel().clearSelection();

        cmbWDEmployee.getSelectionModel().clearAndSelect(0);
        cmbWDEmployee.getItems().clear();
        cmbWDEmployee.setItems(EmployeeDao.getAllAssignableEmployeeWD("Weeder"));
        cmbWDEmployee.getSelectionModel().clearSelection();

        txtWDCurrentBlock.setText("");
        txtWDCurrentEmployee.setText("");
        cmbWDNewEmployee.getItems().clear();
        cmbWDNewBlock.getItems().clear();

        disableButtonsWD(false, false, true, true);
        setStyleWD(initial);

    }

    private void setStyleWD(String style) {
        cmbWDBlock.setStyle(style);
        cmbWDEmployee.setStyle(style);
    }

    private void disableButtonsWD(boolean select, boolean insert, boolean update, boolean delete) {
        btnWDInsert.setDisable(insert || !privilege.get("WeedingAssignment_insert"));
        btnWDUpdate.setDisable(update || !privilege.get("WeedingAssignment_update"));
        btnWDDelete.setDisable(delete || !privilege.get("WeedingAssignment_delete"));
    }

    private void loadTableWD() {
        colWDBlock.setCellValueFactory(new PropertyValueFactory("treeblockId"));
        colWDEmployee.setCellValueFactory(new PropertyValueFactory("employeeId"));
        colWDData.setCellValueFactory(new PropertyValueFactory("date"));

        fillTableWD(WeedingassignmentDao.getAll());
        paginationWD.setCurrentPageIndex(0);
    }

    private void fillTableWD(ObservableList<Weedingassignment> weedingAssignment) {
        if (weedingAssignment != null && weedingAssignment.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((weedingAssignment.size() - 1) / rowsCount) + 1;
            paginationWD.setPageCount(pageCount);

            paginationWD.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? weedingAssignment.size() : pageIndex * rowsCount + rowsCount;
                    tblWDTreeBlock.getItems().clear();
                    tblWDTreeBlock.setItems(FXCollections.observableArrayList(weedingAssignment.subList(start, end)));
                    return tblWDTreeBlock;
                }
            });

        } else {
            paginationWD.setPageCount(1);
            tblWDTreeBlock.getItems().clear();
        }

        paginationWD.setCurrentPageIndex(pageWD);
        tblWDTreeBlock.getSelectionModel().select(rowWD);
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Binding Methods">
    @FXML
    private void cmbTPBlockAP(ActionEvent event) {
        Boolean validity = tappingAssignment.setTreeblockId(cmbTPBlock.getSelectionModel().getSelectedItem());
        if (validity) {
            if (oldTappingAssignment != null && !tappingAssignment.getTreeblockId().equals(oldTappingAssignment.getTreeblockId())) {
                cmbTPBlock.setStyle(updated);
            } else {
                cmbTPBlock.setStyle(valid);
            }
        } else {
            cmbTPBlock.setStyle(invalid);
        }
    }
	
    @FXML
    private void cmbTPEmployeeAP(ActionEvent event) {
        Boolean validity = tappingAssignment.setEmployeeId(cmbTPEmployee.getSelectionModel().getSelectedItem());
        if (validity) {
            if (oldTappingAssignment != null && !tappingAssignment.getEmployeeId().equals(oldTappingAssignment.getEmployeeId())) {
                cmbTPEmployee.setStyle(updated);
            } else {
                cmbTPEmployee.setStyle(valid);
            }
        } else {
            cmbTPEmployee.setStyle(invalid);
        }
    }

    @FXML
    private void cmbTPNewEmployeeAP(ActionEvent event) {
        if (!cmbTPNewEmployee.getSelectionModel().isEmpty()) {
            cmbTPNewBlock.getItems().clear();
            cmbTPNewBlock.setItems(TreeblockDao.getAllByEmployeeTP(cmbTPNewEmployee.getSelectionModel().getSelectedItem()));
        }
    }
	

    @FXML
    private void cmbTPNewBlockAP(ActionEvent event) {
        if (!cmbTPNewBlock.getSelectionModel().isEmpty()) {
            exchangingTappingAssignment = TappingassignmentDao.getByTreeBlockAndEmployee(cmbTPNewBlock.getSelectionModel().getSelectedItem(), cmbTPNewEmployee.getSelectionModel().getSelectedItem());
            btnTPExchange.setDisable(false);
        }
    }
	    @FXML
    private void cmbWDBlockAP(ActionEvent event) {
        Boolean validity = weedingAssignment.setTreeblockId(cmbWDBlock.getSelectionModel().getSelectedItem());
        if (validity) {
            if (oldWeedingAssignment != null && !weedingAssignment.getTreeblockId().equals(oldWeedingAssignment.getTreeblockId())) {
                cmbWDBlock.setStyle(updated);
            } else {
                cmbWDBlock.setStyle(valid);
            }
        } else {
            cmbWDBlock.setStyle(invalid);
        }
    }
	

    @FXML
    private void cmbWDEmployeeAP(ActionEvent event) {
        Boolean validity = weedingAssignment.setEmployeeId(cmbWDEmployee.getSelectionModel().getSelectedItem());
        if (validity) {
            if (oldWeedingAssignment != null && !weedingAssignment.getEmployeeId().equals(oldWeedingAssignment.getEmployeeId())) {
                cmbWDEmployee.setStyle(updated);
            } else {
                cmbWDEmployee.setStyle(valid);
            }
        } else {
            cmbWDEmployee.setStyle(invalid);
        }
    }


//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="CRUD Operations">
    @FXML
    private void btnTPClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear Assignment form",
                "Are you sure you want to clear the form ?",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadFormTP();
        }
    }

    private String getErrorTP() {
        String errors = "";
        try {
            if (tappingAssignment.getTreeblockId() == null) {
                errors = errors + "Tree Block \tis not selected\n";
            }
            if (tappingAssignment.getEmployeeId() == null) {
                errors = errors + "Employee \tis not selected\n";
            }
            if (tappingAssignment.getDate() == null) {
                errors = errors + "Assign date \tis not auto entered\n";
            }

        } catch (Exception e) {
            System.out.println("\n\n----------------------Error Checking Error---------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Error checking Error", JOptionPane.ERROR_MESSAGE);
        }

        return errors;
    }

    @FXML
    private void btnTPInsertAP(ActionEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        tappingAssignment.setDate(date);

        String error = getErrorTP();
        if (error.isEmpty()) {
            String details = "\nTree block :  \t\t" + tappingAssignment.getTreeblockId().getNo()
                    + "\nEmployee :  \t\t" + tappingAssignment.getEmployeeId().getName()
                    + "\nAssigned date :   \t" + tappingAssignment.getDate().toString();

            Alert alert = SystemAlert.createAlert(
                    "Add Employee",
                    "Are you sure you want to add the following Employee ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TappingassignmentDao.insert(tappingAssignment);
                loadFormTP();
                fillTableTP(TappingassignmentDao.getAll());
            }
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Insert - Error",
                    "Correct the follwing error",
                    error,
                    Alert.AlertType.ERROR,
                    ui.Main.stage);
            alert.showAndWait();
        }
    }

    private void fillFormTP() {
        if (tblTPTreeBlock.getSelectionModel().getSelectedItem() != null) {
            loadFormTP();
            disableButtonsTP(false, true, false, false);
            setStyleTP(valid);

            oldTappingAssignment = TappingassignmentDao.getById(tblTPTreeBlock.getSelectionModel().getSelectedItem().getId());
            tappingAssignment = TappingassignmentDao.getById(tblTPTreeBlock.getSelectionModel().getSelectedItem().getId());

            cmbTPBlock.getItems().clear();
            cmbTPBlock.getItems().add(oldTappingAssignment.getTreeblockId());
            cmbTPBlock.getSelectionModel().select(oldTappingAssignment.getTreeblockId());

            cmbTPEmployee.getSelectionModel().clearSelection();
            cmbTPEmployee.getSelectionModel().select(oldTappingAssignment.getEmployeeId());

            txtTPCurrentBlock.setText(tappingAssignment.getTreeblockId().getNo());
            txtTPCurrentEmployee.setText(tappingAssignment.getEmployeeId().getName());

            cmbTPNewEmployee.getItems().clear();
            cmbTPNewEmployee.setItems(EmployeeDao.getAllExchangebleEmployeeTP(oldTappingAssignment.getEmployeeId()));
            cmbTPNewBlock.getItems().clear();

            pageTP = paginationTP.getCurrentPageIndex();
            rowTP = tblTPTreeBlock.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblTPTreeBlockMC(MouseEvent event) {
        fillFormTP();
    }

    @FXML
    private void tblTPTreeBlockKR(KeyEvent event) {
        fillFormTP();
    }

    private String getUpdatesTP() {
        String updates = "";
        try {

            if (oldTappingAssignment != null) {

                if (tappingAssignment.getTreeblockId() != null && !tappingAssignment.getTreeblockId().equals(oldTappingAssignment.getTreeblockId())) {
                    updates = updates + oldTappingAssignment.getTreeblockId().getNo() + " chnaged to " + tappingAssignment.getTreeblockId().getNo() + "\n";
                }

                if (tappingAssignment.getEmployeeId() != null && !tappingAssignment.getEmployeeId().equals(oldTappingAssignment.getEmployeeId())) {
                    updates = updates + oldTappingAssignment.getEmployeeId().getName() + " chnaged to " + tappingAssignment.getEmployeeId().getName() + "\n";
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
    private void btnTPUpdateAP(ActionEvent event) {
        String updates = getUpdatesTP();

        if (updates.isEmpty()) {
            Alert alert = SystemAlert.createAlert(
                    "Update Tapping Assignment",
                    "Nothing updates",
                    updates,
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);
            alert.showAndWait();
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Update Tapping Assignment",
                    "Are you sure you want to update the following assignment ?",
                    updates,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                tappingAssignment.setDate(date);
                TappingassignmentDao.update(tappingAssignment);
                loadFormTP();
                fillTableTP(TappingassignmentDao.getAll());
            }
        }
    }

    @FXML
    private void btnTPDeleteAP(ActionEvent event) {
        if (tblTPTreeBlock.getSelectionModel().getSelectedItem() != null) {
            String details = "\nTree block :  \t\t" + tappingAssignment.getTreeblockId().getNo()
                    + "\nEmployee :  \t\t" + tappingAssignment.getEmployeeId().getName()
                    + "\nAssigned Date :   \t\t" + tappingAssignment.getDate().toString();

            Alert alert = SystemAlert.createAlert(
                    "Delete Assignment",
                    "Are you sure you want to delete the following Assignment ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TappingassignmentDao.delete(tappingAssignment);
                loadFormTP();
                fillTableTP(TappingassignmentDao.getAll());
            }
        }
    }

    @FXML
    private void btnWDClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear employee form",
                "Are you sure you want to clear employee the form ?",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadFormWD();
        }
    }

    private String getErrorWD() {
        String errors = "";
        try {
            if (weedingAssignment.getTreeblockId() == null) {
                errors = errors + "Tree Block \tis not selected\n";
            }
            if (weedingAssignment.getEmployeeId() == null) {
                errors = errors + "Employee \tis not selected\n";
            }
            if (weedingAssignment.getDate() == null) {
                errors = errors + "Assign date \tis not auto entered\n";
            }

        } catch (Exception e) {
            System.out.println("\n\n----------------------Error Checking Error---------------------------------------------------\n");
            System.out.println(e.getClass());
            System.out.println("\n-------------------------------------------------------------------------\n\n");
            JOptionPane.showMessageDialog(null, e.getClass(), "Error checking Error", JOptionPane.ERROR_MESSAGE);
        }

        return errors;
    }

    @FXML
    private void btnWDInsertAP(ActionEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        weedingAssignment.setDate(date);

        String error = getErrorWD();
        if (error.isEmpty()) {
            String details = "\nTree block :  \t\t" + weedingAssignment.getTreeblockId().getNo()
                    + "\nEmployee :  \t\t" + weedingAssignment.getEmployeeId().getName()
                    + "\nAssigned date :   \t" + weedingAssignment.getDate().toString();

            Alert alert = SystemAlert.createAlert(
                    "Add Employee",
                    "Are you sure you want to add the following Employee ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                WeedingassignmentDao.insert(weedingAssignment);
                loadFormWD();
                fillTableWD(WeedingassignmentDao.getAll());
            }
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Insert - Error",
                    "Correct the follwing error",
                    error,
                    Alert.AlertType.ERROR,
                    ui.Main.stage);
            alert.showAndWait();
        }
    }

    private void fillFormWD() {
        if (tblWDTreeBlock.getSelectionModel().getSelectedItem() != null) {
            loadFormWD();
            disableButtonsWD(false, true, false, false);
            setStyleWD(valid);

            oldWeedingAssignment = WeedingassignmentDao.getById(tblWDTreeBlock.getSelectionModel().getSelectedItem().getId());
            weedingAssignment = WeedingassignmentDao.getById(tblWDTreeBlock.getSelectionModel().getSelectedItem().getId());

            cmbWDBlock.getItems().clear();
            cmbWDBlock.getItems().add(oldWeedingAssignment.getTreeblockId());
            cmbWDBlock.getSelectionModel().select(oldWeedingAssignment.getTreeblockId());

            cmbWDEmployee.getSelectionModel().clearSelection();
            cmbWDEmployee.getSelectionModel().select(oldWeedingAssignment.getEmployeeId());

            txtWDCurrentBlock.setText(weedingAssignment.getTreeblockId().getNo());
            txtWDCurrentEmployee.setText(weedingAssignment.getEmployeeId().getName());

            cmbWDNewEmployee.getItems().clear();
            cmbWDNewEmployee.setItems(EmployeeDao.getAllExchangebleEmployeeWD(oldWeedingAssignment.getEmployeeId()));
            cmbWDNewBlock.getItems().clear();

            pageWD = paginationWD.getCurrentPageIndex();
            rowWD = tblWDTreeBlock.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblWDTreeBlockMC(MouseEvent event) {
        fillFormWD();
    }

    @FXML
    private void tblWDTreeBlockKR(KeyEvent event) {
        fillFormWD();
    }

    private String getUpdatesWD() {
        String updates = "";
        try {

            if (oldWeedingAssignment != null) {

                if (weedingAssignment.getTreeblockId() != null && !weedingAssignment.getTreeblockId().equals(oldWeedingAssignment.getTreeblockId())) {
                    updates = updates + oldWeedingAssignment.getTreeblockId().getNo() + " chnaged to " + weedingAssignment.getTreeblockId().getNo() + "\n";
                }

                if (weedingAssignment.getEmployeeId() != null && !weedingAssignment.getEmployeeId().equals(oldWeedingAssignment.getEmployeeId())) {
                    updates = updates + oldWeedingAssignment.getEmployeeId().getName() + " chnaged to " + weedingAssignment.getEmployeeId().getName() + "\n";
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
    private void btnWDUpdateAP(ActionEvent event) {
        String updates = getUpdatesWD();

        if (updates.isEmpty()) {
            Alert alert = SystemAlert.createAlert(
                    "Update Weeding Assignment",
                    "Nothing updates",
                    updates,
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);
            alert.showAndWait();
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Update Weeding Assignment",
                    "Are you sure you want to update the following assignment ?",
                    updates,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                weedingAssignment.setDate(date);
                WeedingassignmentDao.update(weedingAssignment);
                loadFormWD();
                fillTableWD(WeedingassignmentDao.getAll());
            }
        }
    }

    @FXML
    private void btnWDDeleteAP(ActionEvent event) {
        if (tblWDTreeBlock.getSelectionModel().getSelectedItem() != null) {
            String details = "\nTree block :  \t\t" + weedingAssignment.getTreeblockId().getNo()
                    + "\nEmployee :  \t\t" + weedingAssignment.getEmployeeId().getName()
                    + "\nAssigned Date :   \t\t" + weedingAssignment.getDate().toString();

            Alert alert = SystemAlert.createAlert(
                    "Delete Assignment",
                    "Are you sure you want to delete the following Assignment ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                WeedingassignmentDao.delete(weedingAssignment);
                loadFormWD();
                fillTableWD(WeedingassignmentDao.getAll());
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Exchanging Button Action">
    @FXML
    private void btnTPExchangeAP(ActionEvent event) {
        if (getUpdatesTP().isEmpty()) {
            if (exchangingTappingAssignment != null) {
                Treeblock tappingAssignmentTreeId = tappingAssignment.getTreeblockId();
                Treeblock exchangingTappingAssignmentTreeId = exchangingTappingAssignment.getTreeblockId();
                tappingAssignment.setTreeblockId(exchangingTappingAssignmentTreeId);
                exchangingTappingAssignment.setTreeblockId(tappingAssignmentTreeId);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                tappingAssignment.setDate(date);
                exchangingTappingAssignment.setDate(date);

                Alert alert = SystemAlert.createAlert("Exchange",
                        "Exchange Tree Block",
                        "Tree Block " + tappingAssignmentTreeId.getNo() + " will be assigned to " + exchangingTappingAssignment.getEmployeeId().getName() + ". \nTree Block " + exchangingTappingAssignmentTreeId.getNo() + " will be assigned to " + tappingAssignment.getEmployeeId().getName() + ".",
                        Alert.AlertType.CONFIRMATION,
                        ui.Main.stage);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    TappingassignmentDao.update(tappingAssignment);
                    TappingassignmentDao.update(exchangingTappingAssignment);
                    loadFormTP();
                    fillTableTP(TappingassignmentDao.getAll());
                }
            }
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Exchange Error",
                    "Update and Exchange Confliction",
                    "Before exhange tree block between employees update the tree block",
                    Alert.AlertType.ERROR,
                    ui.Main.stage);
            alert.show();

            cmbTPNewEmployee.getItems().clear();
            cmbTPNewBlock.getItems().clear();
            exchangingTappingAssignment = null;
            btnTPExchange.setDisable(true);
        }
    }

    @FXML
    private void btnTPExchangeClearAP(ActionEvent event) {
        cmbTPNewEmployee.getItems().clear();
        cmbTPNewBlock.getItems().clear();
        exchangingTappingAssignment = null;
        btnTPExchange.setDisable(true);
    }

    @FXML
    private void btnWDExchangeAP(ActionEvent event) {
        if (getUpdatesWD().isEmpty()) {
            if (exchangingWeedingAssignment != null) {
                Treeblock weedingAssignmentTreeId = weedingAssignment.getTreeblockId();
                Treeblock exchangingWeedingAssignmentTreeId = exchangingWeedingAssignment.getTreeblockId();
                weedingAssignment.setTreeblockId(exchangingWeedingAssignmentTreeId);
                exchangingWeedingAssignment.setTreeblockId(weedingAssignmentTreeId);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                weedingAssignment.setDate(date);
                exchangingWeedingAssignment.setDate(date);

                Alert alert = SystemAlert.createAlert("Exchange",
                        "Exchange Tree Block",
                        "Tree Block " + weedingAssignmentTreeId.getNo() + " will be assigned to " + exchangingWeedingAssignment.getEmployeeId().getName() + ". \nTree Block " + exchangingWeedingAssignmentTreeId.getNo() + " will be assigned to " + weedingAssignment.getEmployeeId().getName() + ".",
                        Alert.AlertType.CONFIRMATION,
                        ui.Main.stage);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    WeedingassignmentDao.update(weedingAssignment);
                    WeedingassignmentDao.update(exchangingWeedingAssignment);
                    loadFormWD();
                    fillTableWD(WeedingassignmentDao.getAll());
                }
            }
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Exchange Error",
                    "Update and Exchange Confliction",
                    "Before exhange tree block between employees update the tree block",
                    Alert.AlertType.ERROR,
                    ui.Main.stage);
            alert.show();

            cmbWDNewEmployee.getItems().clear();
            cmbWDNewBlock.getItems().clear();
            exchangingWeedingAssignment = null;
            btnWDExchange.setDisable(true);
        }
    }

    @FXML
    private void btnWDExchangeClearAP(ActionEvent event) {
        cmbWDNewEmployee.getItems().clear();
        cmbWDNewBlock.getItems().clear();
        exchangingWeedingAssignment = null;
        btnWDExchange.setDisable(true);
    }
//</editor-fold>

}

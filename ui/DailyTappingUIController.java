/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CloneDao;
import dao.DailytappingDao;
import dao.EmployeeDao;
import dao.TappinglayerDao;
import dao.TreeDao;
import dao.TreeblockDao;
import dao.TreeblockstatusDao;
import entity.Dailytapping;
import entity.Employee;
import entity.Tappinglayer;
import entity.Treeblock;
import entity.Treeblockstatus;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import static ui.LoginUIController.privilege;
import static ui.Style.initial;
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class DailyTappingUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form data">
    Dailytapping oldDailyTapping;
    Dailytapping dailyTapping;

    String initial;
    String valid;
    String invalid;
    String updated;

    int page;
    int row;

    boolean lock;
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="FXML Data">
    @FXML
    private DatePicker dtpTappingDate;
    @FXML
    private TextField txtVolume;
    @FXML
    private TextField txtMetrolac;
    @FXML
    private TextField txtDryWeight;
    @FXML
    private ComboBox<Treeblock> cmbTreeBlock;
    @FXML
    private ComboBox<Employee> cmbEmployee;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Dailytapping> tblTapping;
    @FXML
    private TableColumn<Dailytapping, String> colDate;
    @FXML
    private TableColumn<Dailytapping, String> colVolume;
    @FXML
    private TableColumn<Dailytapping, String> colMetrolac;
    @FXML
    private TableColumn<Dailytapping, String> colDryWeight;
    @FXML
    private TableColumn<Dailytapping, Treeblock> colTreeBlock;
    @FXML
    private TableColumn<Dailytapping, Employee> colEmpoyee;
    @FXML
    private ComboBox<Treeblock> cmbSearchTreeBlock;
    @FXML
    private ComboBox<Employee> cmbSearchEmployee;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Button btnSearchClear;
    @FXML
    private DatePicker dtpSearchDate;
//</editor-fold>

    /**
     * Initializes the controller class.
     */
//<editor-fold defaultstate="collapsed" desc="Initialize Method">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        loadTable();
        loadForm();
    }

    private void loadForm() {
        dailyTapping = new Dailytapping();
        oldDailyTapping = null;

        dtpTappingDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        //dtpTappingDate.setValue(null);
        txtVolume.setText("");
        txtMetrolac.setText("");
        txtDryWeight.setText("");
        cmbTreeBlock.setItems(TreeblockDao.getAll());
        cmbTreeBlock.getSelectionModel().clearSelection();
        cmbEmployee.setItems(EmployeeDao.getAll());
        cmbEmployee.getSelectionModel().clearSelection();

        disableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void setStyle(String style) {
        dtpTappingDate.getEditor().setStyle(style);
        txtVolume.setStyle(style);
        txtMetrolac.setStyle(style);
        txtDryWeight.setStyle(style);
        cmbTreeBlock.setStyle(style);
        cmbEmployee.setStyle(style);
    }

    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnInsert.setDisable(insert || !privilege.get("Tapping_insert"));
        btnUpdate.setDisable(update || !privilege.get("Tapping_update"));
        btnDelete.setDisable(delete || !privilege.get("Tapping_delete"));
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colVolume.setCellValueFactory(new PropertyValueFactory("volume"));
        colMetrolac.setCellValueFactory(new PropertyValueFactory("metrolac"));
        colDryWeight.setCellValueFactory(new PropertyValueFactory("dryweight"));
        colTreeBlock.setCellValueFactory(new PropertyValueFactory("treeblockId"));
        colEmpoyee.setCellValueFactory(new PropertyValueFactory("employeeId"));

        dtpSearchDate.setValue(null);
        cmbSearchTreeBlock.setItems(TreeblockDao.getAll());
        cmbSearchTreeBlock.getSelectionModel().clearSelection();
        cmbSearchEmployee.setItems(EmployeeDao.getAll());
        cmbSearchEmployee.getSelectionModel().clearSelection();

        fillTable(DailytappingDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void fillTable(ObservableList<Dailytapping> tapping) {
        if (tapping != null && tapping.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((tapping.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? tapping.size() : pageIndex * rowsCount + rowsCount;
                    tblTapping.getItems().clear();
                    tblTapping.setItems(FXCollections.observableArrayList(tapping.subList(start, end)));
                    return tblTapping;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblTapping.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblTapping.getSelectionModel().select(row);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Binding">
    @FXML
    private void dtpTappingDateAP(ActionEvent event) {
        if (dtpTappingDate.getValue() != null) {
            Date today = new Date();
            Date dop = java.sql.Date.valueOf(dtpTappingDate.getValue());
            if (dop.before(today) || dop.equals(today)) {
                dailyTapping.setDate(dop);
                if (oldDailyTapping != null && !dailyTapping.getDate().equals(oldDailyTapping.getDate())) {
                    dtpTappingDate.getEditor().setStyle(updated);
                } else {
                    dtpTappingDate.getEditor().setStyle(valid);
                }
            } else {
                dtpTappingDate.getEditor().setStyle(invalid);
                dailyTapping.setDate(null);
            }
        } else {
            dtpTappingDate.getEditor().setStyle(initial);
        }
    }

    @FXML
    private void txtVolumeKR(KeyEvent event) {
        if (!txtVolume.getText().isEmpty()) {
            if (dailyTapping.setVolume(new BigDecimal(txtVolume.getText().trim()))) {
                if (oldDailyTapping != null && !dailyTapping.getVolume().equals(oldDailyTapping.getVolume())) {
                    txtVolume.setStyle(updated);
                } else {
                    txtVolume.setStyle(valid);
                }
            } else {
                txtVolume.setStyle(invalid);
            }
        } else {
            txtVolume.setStyle(initial);
        }
    }

    @FXML
    private void txtMetrolacKR(KeyEvent event) {
        if (!txtMetrolac.getText().isEmpty()) {
            if (dailyTapping.setMetrolac(txtMetrolac.getText().trim())) {
                if (oldDailyTapping != null && !dailyTapping.getMetrolac().equals(oldDailyTapping.getMetrolac())) {
                    txtMetrolac.setStyle(updated);
                } else {
                    txtMetrolac.setStyle(valid);
                }
            } else {
                txtMetrolac.setStyle(invalid);
            }
        } else {
            txtMetrolac.setStyle(initial);
        }
    }

    @FXML
    private void txtDryWeightKR(KeyEvent event) {
        if (!txtDryWeight.getText().isEmpty()) {
            if (dailyTapping.setDryweight(txtDryWeight.getText().trim())) {
                if (oldDailyTapping != null && !dailyTapping.getDryweight().equals(oldDailyTapping.getDryweight())) {
                    txtDryWeight.setStyle(updated);
                } else {
                    txtDryWeight.setStyle(valid);
                }
            } else {
                txtDryWeight.setStyle(invalid);
            }
        } else {
            txtDryWeight.setStyle(initial);
        }
    }

    @FXML
    private void cmbTreeBlockAP(ActionEvent event) {
        dailyTapping.setTreeblockId(cmbTreeBlock.getSelectionModel().getSelectedItem());
        if (oldDailyTapping != null && !dailyTapping.getTreeblockId().equals(oldDailyTapping.getTreeblockId())) {
            cmbTreeBlock.setStyle(updated);
        } else {
            cmbTreeBlock.setStyle(valid);
        }
    }

    @FXML
    private void cmbEmployeeAP(ActionEvent event) {
        dailyTapping.setEmployeeId(cmbEmployee.getSelectionModel().getSelectedItem());
        if (oldDailyTapping != null && !dailyTapping.getEmployeeId().equals(oldDailyTapping.getEmployeeId())) {
            cmbEmployee.setStyle(updated);
        } else {
            cmbEmployee.setStyle(valid);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CRUD Operations">
    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear Tapping form",
                "Are you sure you want to clear the form ?",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadForm();
        }
    }

    private String getError() {
        String errors = "";
        try {
            if (dailyTapping.getDate() == null) {
                errors = errors + "Tapping Date\tis not entered\n";
            }
            if (dailyTapping.getVolume() == null) {
                errors = errors + "Volume \tis not entered\n";
            }
            if (dailyTapping.getMetrolac() == null) {
                errors = errors + "Metrolac \tis not entered\n";
            }
            if (dailyTapping.getDryweight() == null) {
                errors = errors + "Dry Weight \tis not entered\n";
            }
            if (dailyTapping.getTreeblockId() == null) {
                errors = errors + "Tree Block \tis not selected\n";
            }
            if (dailyTapping.getEmployeeId() == null) {
                errors = errors + "Employee \tis not selected\n";
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
    private void btnInsertAP(ActionEvent event) {
        String error = getError();
        if (error.isEmpty()) {
            String details = "Tapping Date :  \t\t" + dailyTapping.getDate().toString()
                    + "\nVolume :  \t\t" + dailyTapping.getVolume()
                    + "\nMetrolac :  \t\t" + dailyTapping.getMetrolac()
                    + "\nDry Weigth :  \t\t" + dailyTapping.getDryweight()
                    + "\nTree Block :  \t\t" + dailyTapping.getTreeblockId().getNo()
                    + "\nEmployee :  \t\t" + dailyTapping.getEmployeeId().getName();

            Alert alert = SystemAlert.createAlert(
                    "Add Tapping Record",
                    "Are you sure you want to add the following Tapping Record ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DailytappingDao.insert(dailyTapping);
                loadForm();
                fillTable(DailytappingDao.getAll());
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

    private void fillFrom() {
        if (tblTapping.getSelectionModel().getSelectedItem() != null) {
            disableButtons(false, true, false, false);
            setStyle(valid);

            oldDailyTapping = DailytappingDao.getById(tblTapping.getSelectionModel().getSelectedItem().getId());
            dailyTapping = DailytappingDao.getById(tblTapping.getSelectionModel().getSelectedItem().getId());

            dtpTappingDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(dailyTapping.getDate())));
            dtpTappingDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(dailyTapping.getDate()));
            txtVolume.setText(dailyTapping.getVolume().toString());
            txtMetrolac.setText(dailyTapping.getMetrolac());
            txtDryWeight.setText(dailyTapping.getDryweight());
            cmbTreeBlock.getSelectionModel().select(dailyTapping.getTreeblockId());
            cmbEmployee.getSelectionModel().select(dailyTapping.getEmployeeId());

            page = pagination.getCurrentPageIndex();
            row = tblTapping.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblTappingMC(MouseEvent event) {
        fillFrom();
    }

    @FXML
    private void tblTappingKR(KeyEvent event) {
        fillFrom();
    }

    private String getUpdates() {
        String updates = "";
        try {

            if (oldDailyTapping != null) {

                if (dailyTapping.getDate() != null && !dailyTapping.getDate().equals(oldDailyTapping.getDate())) {
                    updates = updates + oldDailyTapping.getDate() + " chnaged to " + dailyTapping.getDate() + "\n";
                }

                if (dailyTapping.getVolume() != null && !dailyTapping.getVolume().equals(oldDailyTapping.getVolume())) {
                    updates = updates + oldDailyTapping.getVolume() + " chnaged to " + dailyTapping.getVolume() + "\n";
                }

                if (dailyTapping.getMetrolac() != null && !dailyTapping.getMetrolac().equals(oldDailyTapping.getMetrolac())) {
                    updates = updates + oldDailyTapping.getMetrolac() + " chnaged to " + dailyTapping.getMetrolac() + "\n";
                }

                if (dailyTapping.getDryweight() != null && !dailyTapping.getDryweight().equals(oldDailyTapping.getDryweight())) {
                    updates = updates + oldDailyTapping.getDryweight() + " chnaged to " + dailyTapping.getDryweight() + "\n";
                }

                if (dailyTapping.getTreeblockId() != null && !dailyTapping.getTreeblockId().equals(oldDailyTapping.getTreeblockId())) {
                    updates = updates + oldDailyTapping.getTreeblockId() + " chnaged to " + dailyTapping.getTreeblockId() + "\n";
                }

                if (dailyTapping.getEmployeeId() != null && !dailyTapping.getEmployeeId().equals(oldDailyTapping.getEmployeeId())) {
                    updates = updates + oldDailyTapping.getEmployeeId() + " chnaged to " + dailyTapping.getEmployeeId() + "\n";
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
            Alert alert = SystemAlert.createAlert(
                    "Update Daily Tapping",
                    "Nothing updates",
                    updates,
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);
            alert.showAndWait();
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Update Daily Tapping",
                    "Are you sure you want to update the following Daily Tapping ?",
                    updates,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DailytappingDao.update(dailyTapping);
                loadForm();
                fillTable(DailytappingDao.getAll());
            }
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (tblTapping.getSelectionModel().getSelectedItem() != null) {
            String details = "\nTapping Date :  \t\t\t" + dailyTapping.getDate()
                    + "\nVolume :  \t\t\t" + dailyTapping.getVolume()
                    + "\nMetrolac :   \t\t" + dailyTapping.getMetrolac()
                    + "\nDry Weight :       \t\t" + dailyTapping.getDryweight()
                    + "\nTree Block :  \t\t" + dailyTapping.getTreeblockId().getNo()
                    + "\nEmployee :      \t\t" + dailyTapping.getEmployeeId().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Daily Tapping");
            alert.setHeaderText("Are you sure you want to delete the following Daily Tapping ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DailytappingDao.delete(dailyTapping);
                loadForm();
                fillTable(DailytappingDao.getAll());
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search Operations">
    private void updateTable() {
        java.sql.Date date = null;
        if (dtpSearchDate.getValue() != null) {
            date = java.sql.Date.valueOf(dtpSearchDate.getValue());
        }
        boolean sDate = date != null;
        Treeblock treeBlock = cmbSearchTreeBlock.getSelectionModel().getSelectedItem();
        boolean sTreeBlock = cmbSearchTreeBlock.getSelectionModel().getSelectedIndex() != -1;
        Employee employee = cmbSearchEmployee.getSelectionModel().getSelectedItem();
        boolean sEmployee = cmbSearchEmployee.getSelectionModel().getSelectedIndex() != -1;

        if (!sDate && !sTreeBlock && !sEmployee) {
            fillTable(DailytappingDao.getAll());
        }
        if (sDate && !sTreeBlock && !sEmployee) {
            fillTable(DailytappingDao.getAllByTappingDate(date));
        }
        if (!sDate && !sTreeBlock && sEmployee) {
            fillTable(DailytappingDao.getAllByEmployee(employee));
        }
        if (!sDate && sTreeBlock && !sEmployee) {
            fillTable(DailytappingDao.getAllByTreeBlock(treeBlock));
        }
        if (sDate && sTreeBlock && !sEmployee) {
            fillTable(DailytappingDao.getAllByTappingDateTreeBlock(date, treeBlock));
        }
        if (sDate && !sTreeBlock && sEmployee) {
            fillTable(DailytappingDao.getAllByTappingDateEmployee(date, employee));
        }
        if (!sDate && sTreeBlock && sEmployee) {
            fillTable(DailytappingDao.getAllByTreeBlockEmployee(treeBlock, employee));
        }
        if (sDate && sTreeBlock && sEmployee) {
            fillTable(DailytappingDao.getAllByTappingDateTreeBlockEmployee(date, treeBlock, employee));
        }
    }

    @FXML
    private void btnSearchLockAP(ActionEvent event) {
        if (lock) {
            btnSearchLock.setText("Lock");
            lock = false;
        } else {
            btnSearchLock.setText("Unlock");
            lock = true;
        }
    }

    @FXML
    private void dtpSearchDateAP(ActionEvent event) {
        if (dtpSearchDate.getValue() != null) {
            if (!lock) {
                cmbSearchEmployee.getSelectionModel().clearSelection();
                cmbSearchTreeBlock.getSelectionModel().clearSelection();
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchTreeBlockAP(ActionEvent event) {
        if (cmbSearchTreeBlock.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchEmployee.getSelectionModel().clearSelection();
                dtpSearchDate.setValue(null);
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchEmployeeAP(ActionEvent event) {
        if (cmbSearchEmployee.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchTreeBlock.getSelectionModel().clearSelection();
                dtpSearchDate.setValue(null);
            }
            updateTable();
        }
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
    }
//</editor-fold>

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.ComplainDao;
import dao.ComplainstatusDao;
import dao.ComplaintypeDao;
import dao.TreeDao;
import dao.TreeblockDao;
import entity.Complain;
import entity.Complainstatus;
import entity.Complaintype;
import entity.Disease;
import entity.Tree;
import entity.Treeblock;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
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
public class ComplainUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    Complain complain;
    Complain oldComplain;

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
    private DatePicker dtpComplainDate;
    @FXML
    private ComboBox<Treeblock> cmbTreeBlock;
    @FXML
    private ComboBox<Complaintype> cmbComplainType;
    @FXML
    private ComboBox<Disease> cmbDisease;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<Complainstatus> cmbComplainStatus;
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
    private TableView<Complain> tblComplain;
    @FXML
    private TableColumn<Complain, Date> colDate;
    @FXML
    private TableColumn<Complain, String> colTreeNo;
    @FXML
    private TableColumn<Complain, Complaintype> colComplainType;
    @FXML
    private TableColumn<Complain, Disease> colDsease;
    @FXML
    private TableColumn<Complain, Complainstatus> colComplainStatus;
    @FXML
    private ComboBox<Complaintype> cmbSearchComplainType;
    @FXML
    private ComboBox<Complainstatus> cmbSearchComplainStatus;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Button btnSearchClear;
    @FXML
    private DatePicker dtpSearchComplainDate;
    @FXML
    private ComboBox<Tree> cmbTreeNo;
//</editor-fold>

    /**
     * Initializes the controller class.
     */
//<editor-fold defaultstate="collapsed" desc="Initialize methods">
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
        complain = new Complain();
        oldComplain = null;

        dtpComplainDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        cmbTreeBlock.setItems(TreeblockDao.getAll());
        cmbTreeBlock.getSelectionModel().clearSelection();
        cmbTreeNo.getSelectionModel().clearSelection();
        cmbTreeNo.setDisable(true);
        cmbComplainType.setItems(ComplaintypeDao.getAll());
        cmbComplainType.getSelectionModel().clearSelection();
        cmbDisease.getSelectionModel().clearSelection();
        cmbDisease.setDisable(true);
        txtDescription.setText("");
        cmbComplainStatus.setItems(dao.ComplainstatusDao.getAll());
        cmbComplainStatus.getSelectionModel().clearSelection();

        disableButtons(false, false, true, true);
        setStyle(initial);
        //dtpComplainDate.getEditor().setStyle(valid);
    }

    private void setStyle(String style) {
        dtpComplainDate.getEditor().setStyle(style);
        cmbTreeBlock.setStyle(style);
        cmbTreeNo.setStyle(style);
        cmbComplainType.setStyle(style);
        cmbDisease.setStyle(style);
        if (!txtDescription.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txtDescription.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }
        cmbComplainStatus.setStyle(style);
    }

    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnInsert.setDisable(insert || !privilege.get("Complain_insert"));
        btnUpdate.setDisable(update || !privilege.get("Complain_update"));
        btnDelete.setDisable(delete || !privilege.get("Complain_delete"));
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colTreeNo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Complain, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Complain, String> param) {
                return new SimpleObjectProperty(param.getValue().getTreeId() + " - (" + param.getValue().getTreeId().getBlockId().toString() + ")");
            }
        });
        colComplainType.setCellValueFactory(new PropertyValueFactory("complaintypeId"));
        colDsease.setCellValueFactory(new PropertyValueFactory("diseaseId"));
        colComplainStatus.setCellValueFactory(new PropertyValueFactory("complainstatusId"));

        dtpSearchComplainDate.setValue(null);
        cmbSearchComplainType.setItems(ComplaintypeDao.getAll());
        cmbSearchComplainType.getSelectionModel().clearSelection();
        cmbSearchComplainStatus.setItems(ComplainstatusDao.getAll());
        cmbSearchComplainStatus.getSelectionModel().clearSelection();

        fillTable(ComplainDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void fillTable(ObservableList<Complain> complain) {
        if (complain != null && complain.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((complain.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? complain.size() : pageIndex * rowsCount + rowsCount;
                    tblComplain.getItems().clear();
                    tblComplain.setItems(FXCollections.observableArrayList(complain.subList(start, end)));
                    return tblComplain;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblComplain.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblComplain.getSelectionModel().select(row);
    }
//</editor-fold>



//<editor-fold defaultstate="collapsed" desc="CRUD Methods">
    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear Complain",
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
            if (complain.getDate() == null) {
                errors = errors + "Complain date \tis not selected\n";
            }
            if (complain.getTreeId() != null) {
                if (complain.getTreeId().getBlockId() == null) {
                    errors = errors + "Tree \tis not entered\n";
                }
            }
            if (complain.getTreeId() == null) {
                errors = errors + "Tree \tis not entered\n";
            }
            if (complain.getComplaintypeId() == null) {
                errors = errors + "Complain type \tis not selected\n";
            }
            if (complain.getDiseaseId() == null && (complain.getComplaintypeId() != null ? complain.getComplaintypeId().getName().equalsIgnoreCase("Ill Tree") : false)) {
                errors = errors + "Disease \tis not selected\n";
            }
            if (complain.getComplainstatusId() == null) {
                errors = errors + "Complain status \tis not selected\n";
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
            String details = "Complain Date :  \t\t" + complain.getDate()
                    + "\nTree Block :  \t\t" + complain.getTreeId().getBlockId().getNo()
                    + "\nTree :  \t\t" + complain.getTreeId().getNo()
                    + "\nComplain Type :   \t" + complain.getComplaintypeId().getName()
                    + "\nDisease :   \t" + ((complain.getDiseaseId() != null) ? complain.getDiseaseId().getName() : "Not a Diseas")
                    + "\nDescription :   \t" + ((complain.getDescription() != null) ? complain.getDescription() : "[Not Enterde]")
                    + "\nComplain Status :   \t" + complain.getComplainstatusId().getName();

            Alert alert = SystemAlert.createAlert(
                    "Add Complain",
                    "Are you sure you want to add the following Complain ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                ComplainDao.insert(complain);
                loadForm();
                fillTable(ComplainDao.getAll());
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
        if (tblComplain.getSelectionModel().getSelectedItem() != null) {
            disableButtons(false, true, false, false);
            setStyle(valid);

            oldComplain = ComplainDao.getById(tblComplain.getSelectionModel().getSelectedItem().getId());
            complain = ComplainDao.getById(tblComplain.getSelectionModel().getSelectedItem().getId());

            dtpComplainDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(complain.getDate())));
            dtpComplainDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(complain.getDate()));
            cmbTreeBlock.getSelectionModel().select(complain.getTreeId().getBlockId());
            cmbTreeNo.setDisable(false);
            cmbTreeNo.getSelectionModel().select(complain.getTreeId());
            cmbComplainType.getSelectionModel().select(complain.getComplaintypeId());
            if (complain.getComplaintypeId().getName().equalsIgnoreCase("Ill Tree") && complain.getDiseaseId() != null) {
                cmbDisease.setDisable(false);
                cmbDisease.getSelectionModel().select(complain.getDiseaseId());
            } else {
                cmbDisease.setDisable(true);
            }
            txtDescription.setText(complain.getDescription());
            cmbComplainStatus.getSelectionModel().select(complain.getComplainstatusId());

            page = pagination.getCurrentPageIndex();
            row = tblComplain.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblComplainMC(MouseEvent event) {
        fillFrom();
    }

    @FXML
    private void tblComplainKR(KeyEvent event) {
        fillFrom();
    }

    private String getUpdates() {
        String updates = "";
        try {

            if (oldComplain != null) {

                if (complain.getDate() != null && !complain.getDate().equals(oldComplain.getDate())) {
                    updates = updates + oldComplain.getDate().toString() + " chnaged to " + complain.getDate().toString() + "\n";
                }

                if (complain.getTreeId() != null && oldComplain.getTreeId() != null) {
                    if (complain.getTreeId().getBlockId() != null && !complain.getTreeId().getBlockId().equals(oldComplain.getTreeId().getBlockId())) {
                        updates = updates + oldComplain.getTreeId().getBlockId().getNo() + " chnaged to " + complain.getTreeId().getBlockId().getNo() + "\n";
                    }
                }

                if (complain.getTreeId() != null && !complain.getTreeId().equals(oldComplain.getTreeId())) {
                    updates = updates + oldComplain.getTreeId().getNo() + " chnaged to " + complain.getTreeId().getNo() + "\n";
                }

                if (complain.getComplaintypeId() != null && !complain.getComplaintypeId().equals(oldComplain.getComplaintypeId())) {
                    updates = updates + oldComplain.getComplaintypeId().getName() + " chnaged to " + complain.getComplaintypeId().getName() + "\n";
                }

                if (complain.getDiseaseId() != null && oldComplain.getDiseaseId() != null) {
                    if (!complain.getDiseaseId().equals(oldComplain.getDiseaseId())) {
                        updates = updates + oldComplain.getDiseaseId().getName() + " chnaged to " + complain.getDiseaseId().getName() + "\n";
                    }
                }
                if (complain.getDiseaseId() != null && oldComplain.getDiseaseId() == null) {
                    updates = updates + " Disease :" + complain.getDiseaseId().getName() + "\n";
                }

                if (!(oldComplain.getDescription() != null
                        && complain.getDescription() != null
                        && oldComplain.getDescription().equals(complain.getDescription()))) {
                    if (oldComplain.getDescription() != complain.getDescription()) {
                        updates = updates + oldComplain.getDescription()
                                + " chnaged to " + complain.getDescription() + "\n";
                    }
                }

                if (complain.getComplainstatusId() != null && !complain.getComplainstatusId().equals(oldComplain.getComplainstatusId())) {
                    updates = updates + oldComplain.getComplainstatusId().getName() + " chnaged to " + complain.getComplainstatusId().getName() + "\n";
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
        String error = getError();

        if (!error.isEmpty()) {
            Alert alert = SystemAlert.createAlert(
                    "Invalid Update",
                    "Missing Selection.",
                    error,
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);
            alert.showAndWait();
        } else {
            if (updates.isEmpty()) {
                Alert alert = SystemAlert.createAlert(
                        "Update Complain",
                        "Nothing updates",
                        updates,
                        Alert.AlertType.INFORMATION,
                        ui.Main.stage);
                alert.showAndWait();
            } else {
                Alert alert = SystemAlert.createAlert(
                        "Update Complain",
                        "Are you sure you want to update the following Complain ?",
                        updates,
                        Alert.AlertType.CONFIRMATION,
                        ui.Main.stage);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    ComplainDao.update(complain);
                    loadForm();
                    fillTable(ComplainDao.getAll());
                }
            }
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (tblComplain.getSelectionModel().getSelectedItem() != null) {
            String details = "Complain Date :  \t\t" + complain.getDate()
                    + "\nTree Block :  \t\t" + complain.getTreeId().getBlockId().getNo()
                    + "\nTree :  \t\t" + complain.getTreeId().getNo()
                    + "\nComplain Type :   \t" + complain.getComplaintypeId().getName()
                    + "\nDisease :   \t" + ((complain.getDiseaseId() != null) ? complain.getDiseaseId().getName() : "Not a Diseas")
                    + "\nDescription :   \t" + ((complain.getDescription() != null) ? complain.getDescription() : "[Not Enterde]")
                    + "\nComplain Status :   \t" + complain.getComplainstatusId().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Complain");
            alert.setHeaderText("Are you sure you want to delete the following Complain ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                ComplainDao.delete(complain);
                loadForm();
                fillTable(ComplainDao.getAll());
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search Methods">
    private void updateTable() {
        java.sql.Date date = null;
        if (dtpSearchComplainDate.getValue() != null) {
            date = java.sql.Date.valueOf(dtpSearchComplainDate.getValue());
        }
        boolean sDate = date != null;
        Complaintype complainType = cmbSearchComplainType.getSelectionModel().getSelectedItem();
        boolean sComplainType = cmbSearchComplainType.getSelectionModel().getSelectedIndex() != -1;
        Complainstatus complainStatus = cmbSearchComplainStatus.getSelectionModel().getSelectedItem();
        boolean sComplainStatus = cmbSearchComplainStatus.getSelectionModel().getSelectedIndex() != -1;
        
        if (!sDate && !sComplainType && !sComplainStatus) {
            fillTable(ComplainDao.getAll());
        }
        if (sDate && !sComplainType && !sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainDate(date));
        }
        if (!sDate && !sComplainType && sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainStatus(complainStatus));
        }
        if (!sDate && sComplainType && !sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainType(complainType));
        }
        if (sDate && sComplainType && !sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainDateComplainType(date, complainType));
        }
        if (sDate && !sComplainType && sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainDateComplainStatus(date, complainStatus));
        }
        if (!sDate && sComplainType && sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainTypeComplainStatus(complainType, complainStatus));
        }
        if (sDate && sComplainType && sComplainStatus) {
            fillTable(ComplainDao.getAllByComplainDateComplainTypeComplainStatus(date, complainType, complainStatus));
        }
    }
    
    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
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
    private void dtpSearchComplainDateAP(ActionEvent event) {
        if (dtpSearchComplainDate.getValue() != null) {
            if (!lock) {
                cmbSearchComplainType.getSelectionModel().clearSelection();
                cmbSearchComplainStatus.getSelectionModel().clearSelection();
            }
            updateTable();
        }
    }
    
    @FXML
    private void cmbSearchComplainTypeAP(ActionEvent event) {
        if (cmbSearchComplainType.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchComplainStatus.getSelectionModel().clearSelection();
                dtpSearchComplainDate.setValue(null);
            }
            updateTable();
        }
    }
    
    @FXML
    private void cmbSearchComplainStatusAP(ActionEvent event) {
        if (cmbSearchComplainStatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchComplainType.getSelectionModel().clearSelection();
                dtpSearchComplainDate.setValue(null);
            }
            updateTable();
        }
    }
//</editor-fold>
}

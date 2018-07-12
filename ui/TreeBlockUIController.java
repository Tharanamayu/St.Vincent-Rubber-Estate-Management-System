/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CloneDao;
import dao.EmployeeDao;
import dao.TappingassignmentDao;
import dao.TappinglayerDao;
import dao.TreeblockDao;
import dao.TreeblockstatusDao;
import dao.TreestatusDao;
import entity.Civilstatus;
import entity.Clone;
import entity.Designation;
import entity.Employeestatus;
import entity.Gender;
import entity.Tappingassignment;
import entity.Tappinglayer;
import entity.Treeblock;
import entity.Treeblockstatus;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.swing.JOptionPane;
import static ui.LoginUIController.privilege;
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class TreeBlockUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    Treeblock oldTreeBlock;
    Treeblock treeBlock;

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
    private TextField txtEstimatedLifeTime;
    @FXML
    private TextField txtBlockNo;
    @FXML
    private TextField txtArea;
    @FXML
    private DatePicker dtpPlantedDate;
    @FXML
    private ComboBox<Treeblockstatus> cmbStatus;
    @FXML
    private ComboBox<Tappinglayer> cmbTappingLayer;
    @FXML
    private ComboBox<Clone> cmbClone;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnTPInsert;
    @FXML
    private Button btnTPUpdate;
    @FXML
    private Button btnTPDelete;
    @FXML
    private Button btnTPClear;
    @FXML
    private ToggleButton tglBlockB;
    @FXML
    private ToggleGroup tblMap;
    @FXML
    private ToggleButton tglBlockA;
    @FXML
    private ToggleButton tglBlockC;
    @FXML
    private ToggleButton tglBlockD;
    @FXML
    private ToggleButton tglBlockE;
    @FXML
    private ToggleButton tglBlockF;
    @FXML
    private ToggleButton tglBlockGAP;
    @FXML
    private ToggleButton tglBlockH;
    @FXML
    private ToggleButton tglBlockI;
    @FXML
    private ToggleButton tglBlockJ;
    @FXML
    private ToggleButton tglBlockK;
    @FXML
    private ToggleButton tglBlockL;
    @FXML
    private ToggleButton tglBlockM;
    @FXML
    private ToggleButton tglBlockN;
    @FXML
    private ToggleButton tglBlockO;
    @FXML
    private ToggleButton tglBlockP;
    @FXML
    private ToggleButton tglBlockQ;
    @FXML
    private ToggleButton tglBlockR;
    @FXML
    private ToggleButton tglBlockS;
    @FXML
    private ToggleButton tglBlockT;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Treeblock> tblTreeBlock;
    @FXML
    private TableColumn<Treeblock, String> colBlockNo;
    @FXML
    private TableColumn<Treeblock, String> colTrees;
    @FXML
    private TableColumn<Treeblock, Treeblockstatus> colStatus;
    @FXML
    private TableColumn<Treeblock, Tappinglayer> colLayer;
    @FXML
    private TableColumn<Treeblock, String> colPlantedDate;
    @FXML
    private TableColumn<Treeblock, String> colLifeTime;
    @FXML
    private TableColumn<Treeblock, String> colArea;
    @FXML
    private TableColumn<Treeblock, Clone> colClone;
    @FXML
    private ComboBox<Treeblockstatus> cmbSearchStatus;
    @FXML
    private ComboBox<Tappinglayer> cmbSearchLayer;
    @FXML
    private TextField txtSearchBlockNo;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Button btnSearchClear;
    @FXML
    private TextField txtTrees;
//</editor-fold>

    /**
     * Initializes the controller class.
     */
//<editor-fold defaultstate="collapsed" desc="Initialize Methods">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initial = Style.initial;
        valid = Style.valid;
        invalid = Style.invalid;
        updated = Style.updated;

        loadTable();
        loadForm();

//<editor-fold defaultstate="collapsed" desc="Number of trees Text Formatter">
        StringConverter stringConverterTrees = new StringConverter<String>() {
            @Override
            public String toString(String string) {
                if (string == null) {
                    return "";
                }
                return string;
            }

            @Override
            public String fromString(String string) {
                if (string.matches("\\d{3}")) {
                    return string;
                }
                return "";

            }
        };

        UnaryOperator unaryOperationTrees = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {
                for (int i = 0; i < t.getText().length(); i++) {
                    if (t.getText().substring(0, i + 1).matches("\\d")) {
                        return t;
                    } else {
                        return null;
                    }
                }
                return t;
            }
        };

        TextFormatter textFormatterTrees = new TextFormatter<String>(stringConverterTrees, null, unaryOperationTrees);
        txtTrees.setTextFormatter(textFormatterTrees);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Estimated Life Text Formatter">
        StringConverter stringConverterLife = new StringConverter<String>() {
            @Override
            public String toString(String string) {
                if (string == null) {
                    return "";
                }
                return string;
            }

            @Override
            public String fromString(String string) {
                if (string.matches("\\d{2}")) {
                    return string;
                }
                return "";

            }
        };

        UnaryOperator unaryOperationLife = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {
                for (int i = 0; i < t.getText().length(); i++) {
                    if (t.getText().substring(0, i + 1).matches("\\d")) {
                        return t;
                    } else {
                        return null;
                    }
                }
                return t;
            }
        };

        TextFormatter textFormatterLife = new TextFormatter<String>(stringConverterLife, null, unaryOperationLife);
        txtTrees.setTextFormatter(textFormatterLife);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Area Text Formatter">
        StringConverter stringConverterArea = new StringConverter<String>() {
            @Override
            public String toString(String string) {
                if (string == null) {
                    return "";
                }
                return string;
            }

            @Override
            public String fromString(String string) {
                if (string.matches("1.0|1.1|1.2|1.3|1.4|1.5|1.6|1.7|1.8|1.9|2.0")) {
                    return string;
                }
                return "";

            }
        };

        UnaryOperator unaryOperationArea = new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {
                for (int i = 0; i < t.getText().length(); i++) {
                    if (t.getText().substring(0, i + 1).matches("\\d|\\.")) {
                        return t;
                    } else {
                        return null;
                    }
                }
                return t;
            }
        };

        TextFormatter textFormatterArea = new TextFormatter<String>(stringConverterArea, null, unaryOperationArea);
        txtArea.setTextFormatter(textFormatterArea);
//</editor-fold>

    }

    private void loadForm() {
        treeBlock = new Treeblock();
        oldTreeBlock = null;

        txtBlockNo.setText("");
        txtTrees.setText("");
        cmbStatus.setItems(TreeblockstatusDao.getAll());
        cmbStatus.getSelectionModel().clearSelection();
        cmbTappingLayer.setItems(TappinglayerDao.getAll());
        cmbTappingLayer.getSelectionModel().clearSelection();
        dtpPlantedDate.setValue(null);
        txtEstimatedLifeTime.setText("");
        txtArea.setText("");
        cmbClone.setItems(CloneDao.getAll());
        cmbClone.getSelectionModel().clearSelection();
        txtDescription.setText("");

        disableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void setStyle(String style) {
        txtBlockNo.setStyle(style);
        txtTrees.setStyle(style);
        cmbStatus.setStyle(style);
        cmbTappingLayer.setStyle(style);
        dtpPlantedDate.getEditor().setStyle(style);
        txtEstimatedLifeTime.setStyle(style);
        txtArea.setStyle(style);
        cmbClone.setStyle(style);

        if (!txtDescription.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txtDescription.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }
    }

    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnTPInsert.setDisable(insert || !privilege.get("TreeBlock_insert"));
        btnTPUpdate.setDisable(update || !privilege.get("TreeBlock_update"));
        btnTPDelete.setDisable(delete || !privilege.get("TreeBlock_delete"));
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        colBlockNo.setCellValueFactory(new PropertyValueFactory("no"));
        colTrees.setCellValueFactory(new PropertyValueFactory("numberoftrees"));
        colStatus.setCellValueFactory(new PropertyValueFactory("treeblockstatusId"));
        colLayer.setCellValueFactory(new PropertyValueFactory("tappinglayerId"));
        colPlantedDate.setCellValueFactory(new PropertyValueFactory("planteddate"));
        colLifeTime.setCellValueFactory(new PropertyValueFactory("estimatedlifetime"));
        colArea.setCellValueFactory(new PropertyValueFactory("area"));
        colClone.setCellValueFactory(new PropertyValueFactory("cloneId"));

        txtSearchBlockNo.setText("");
        cmbSearchStatus.setItems(TreeblockstatusDao.getAll());
        cmbSearchStatus.getSelectionModel().clearSelection();
        cmbSearchLayer.setItems(TappinglayerDao.getAll());
        cmbSearchLayer.getSelectionModel().clearSelection();

        fillTable(TreeblockDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void fillTable(ObservableList<Treeblock> treeBlock) {
        if (treeBlock != null && treeBlock.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((treeBlock.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? treeBlock.size() : pageIndex * rowsCount + rowsCount;
                    tblTreeBlock.getItems().clear();
                    tblTreeBlock.setItems(FXCollections.observableArrayList(treeBlock.subList(start, end)));
                    return tblTreeBlock;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblTreeBlock.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblTreeBlock.getSelectionModel().select(row);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Binding Methods">
    @FXML
    private void txtBlockNoKR(KeyEvent event) {
        if (!txtBlockNo.getText().isEmpty()) {
            if (treeBlock.setNo(txtBlockNo.getText().trim())) {
                if (oldTreeBlock != null && !treeBlock.getNo().equals(oldTreeBlock.getNo())) {
                    txtBlockNo.setStyle(updated);
                } else {
                    txtBlockNo.setStyle(valid);
                }
            } else {
                txtBlockNo.setStyle(invalid);
            }
        } else {
            txtBlockNo.setStyle(initial);
        }
    }

    @FXML
    private void txtTreesKR(KeyEvent event) {
        if (!txtTrees.getText().isEmpty()) {
            if (treeBlock.setNumberoftrees(Integer.parseInt(txtTrees.getText().trim()))) {
                if (oldTreeBlock != null && !treeBlock.getNumberoftrees().equals(oldTreeBlock.getNumberoftrees())) {
                    txtTrees.setStyle(updated);
                } else {
                    txtTrees.setStyle(valid);
                }
            } else {
                txtTrees.setStyle(invalid);
            }
        } else {
            txtTrees.setStyle(initial);
        }
    }

    @FXML
    private void cmbStatusAP(ActionEvent event) {
        treeBlock.setTreeblockstatusId(cmbStatus.getSelectionModel().getSelectedItem());
        if (oldTreeBlock != null && !treeBlock.getTreeblockstatusId().equals(oldTreeBlock.getTreeblockstatusId())) {
            cmbStatus.setStyle(updated);
        } else {
            cmbStatus.setStyle(valid);
        }
    }

    @FXML
    private void cmbTappingLayerAP(ActionEvent event) {
        treeBlock.setTappinglayerId(cmbTappingLayer.getSelectionModel().getSelectedItem());
        if (oldTreeBlock != null && !treeBlock.getTappinglayerId().equals(oldTreeBlock.getTappinglayerId())) {
            cmbTappingLayer.setStyle(updated);
        } else {
            cmbTappingLayer.setStyle(valid);
        }
    }

    @FXML
    private void dtpPlantedDateAP(ActionEvent event) {
        if (dtpPlantedDate.getValue() != null) {
            Date today = new Date();
            Date dop = java.sql.Date.valueOf(dtpPlantedDate.getValue());
            if (dop.before(today)) {
                treeBlock.setPlanteddate(dop);
                if (oldTreeBlock != null && !treeBlock.getPlanteddate().equals(oldTreeBlock.getPlanteddate())) {
                    dtpPlantedDate.getEditor().setStyle(updated);
                } else {
                    dtpPlantedDate.getEditor().setStyle(valid);
                }
            } else {
                dtpPlantedDate.getEditor().setStyle(invalid);
                treeBlock.setPlanteddate(null);
            }
        } else {
            dtpPlantedDate.setStyle(initial);
        }
    }

    @FXML
    private void txtEstimatedLifeTimeKR(KeyEvent event) {
        if (!txtEstimatedLifeTime.getText().trim().isEmpty()) {
            if (treeBlock.setEstimatedlifetime(Integer.parseInt(txtEstimatedLifeTime.getText().trim()))) {
                if (oldTreeBlock != null && !treeBlock.getEstimatedlifetime().equals(oldTreeBlock.getEstimatedlifetime())) {
                    txtEstimatedLifeTime.setStyle(updated);
                } else {
                    txtEstimatedLifeTime.setStyle(valid);
                }
            } else {
                txtEstimatedLifeTime.setStyle(invalid);
            }
        } else {
            txtEstimatedLifeTime.setStyle(initial);
        }
    }

    @FXML
    private void txtAreaKR(KeyEvent event) {
        if (!txtArea.getText().trim().isEmpty()) {
            if (treeBlock.setArea(txtArea.getText().trim())) {
                if (oldTreeBlock != null && !treeBlock.getArea().equals(oldTreeBlock.getArea())) {
                    txtArea.setStyle(updated);
                } else {
                    txtArea.setStyle(valid);
                }
            } else {
                txtArea.setStyle(invalid);
            }
        } else {
            txtArea.setStyle(initial);
        }
    }

    @FXML
    private void cmbCloneAP(ActionEvent event) {
        treeBlock.setCloneId(cmbClone.getSelectionModel().getSelectedItem());
        if (oldTreeBlock != null && !treeBlock.getCloneId().equals(oldTreeBlock.getCloneId())) {
            cmbClone.setStyle(updated);
        } else {
            cmbClone.setStyle(valid);
        }
    }

    @FXML
    private void txtDescription(KeyEvent event) {
        if (!txtDescription.getText().isEmpty()) {
            if (treeBlock.setDescription(txtDescription.getText().trim())) {
                if (oldTreeBlock != null && !treeBlock.getDescription().equals(oldTreeBlock.getDescription())) {
                    ((ScrollPane) txtDescription.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
                } else {
                    ((ScrollPane) txtDescription.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
                }
            } else {
                ((ScrollPane) txtDescription.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
            }
        } else {
            ((ScrollPane) txtDescription.getChildrenUnmodifiable().get(0)).getContent().setStyle(initial);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CRUD Operation">
    @FXML
    private void btnTPClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear Tree Block form",
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
            if (treeBlock.getNo() == null) {
                errors = errors + "Tree Block No \tis not entered\n";
            }
            if (treeBlock.getNumberoftrees() == null) {
                errors = errors + "Number of trees \tis not entered\n";
            }
            if (treeBlock.getTreeblockstatusId() == null) {
                errors = errors + "Status \tis not selected\n";
            }
            if (treeBlock.getTappinglayerId() == null) {
                errors = errors + "Tapping layer \tis not selected\n";
            }
            if (treeBlock.getPlanteddate() == null) {
                errors = errors + "Planted date \tis not selected\n";
            }
            if (treeBlock.getEstimatedlifetime() == null) {
                errors = errors + "Estimated life time \tis not entered\n";
            }
            if (treeBlock.getCloneId() == null) {
                errors = errors + "Clone \tis not selected\n";
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
        String error = getError();
        if (error.isEmpty()) {
            String details = "Block no :  \t\t" + treeBlock.getNo()
                    + "\nTrees :  \t\t" + treeBlock.getNumberoftrees()
                    + "\nStatus :   \t" + treeBlock
                    + "\nTapping Layer :   \t" + treeBlock.getTappinglayerId().getName()
                    + "\nPlanted date :   \t" + treeBlock.getPlanteddate()
                    + "\nArea :   \t" + treeBlock.getArea()
                    + "\nClone :   \t" + treeBlock.getCloneId().getName()
                    + "\nDescription :   \t" + treeBlock.getDescription();

            Alert alert = SystemAlert.createAlert(
                    "Add Tree Block",
                    "Are you sure you want to add the following Tree Block ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TreeblockDao.insert(treeBlock);
                loadForm();
                fillTable(TreeblockDao.getAll());
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
        if (tblTreeBlock.getSelectionModel().getSelectedItem() != null) {
            disableButtons(false, true, false, false);
            setStyle(valid);

            oldTreeBlock = TreeblockDao.getById(tblTreeBlock.getSelectionModel().getSelectedItem().getId());
            treeBlock = TreeblockDao.getById(tblTreeBlock.getSelectionModel().getSelectedItem().getId());

            txtBlockNo.setText(treeBlock.getNo());
            txtTrees.setText(treeBlock.getNumberoftrees().toString());
            cmbStatus.getSelectionModel().select(treeBlock.getTreeblockstatusId());
            cmbTappingLayer.getSelectionModel().select(treeBlock.getTappinglayerId());
            dtpPlantedDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(treeBlock.getPlanteddate())));
            dtpPlantedDate.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(treeBlock.getPlanteddate()));
            txtEstimatedLifeTime.setText(treeBlock.getEstimatedlifetime().toString());
            txtArea.setText(treeBlock.getArea().toString());
            cmbClone.getSelectionModel().select(treeBlock.getCloneId());
            txtDescription.setText(treeBlock.getDescription());

            page = pagination.getCurrentPageIndex();
            row = tblTreeBlock.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblTreeBlockMC(MouseEvent event) {
        fillFrom();
    }

    @FXML
    private void tblTreeBlockKR(KeyEvent event) {
        fillFrom();
    }

    private String getUpdates() {
        String updates = "";
        try {

            if (oldTreeBlock != null) {

                if (treeBlock.getNo() != null && !treeBlock.getNo().equals(oldTreeBlock.getNo())) {
                    updates = updates + oldTreeBlock.getNo() + " chnaged to " + treeBlock.getNo() + "\n";
                }

                if (treeBlock.getNumberoftrees() != null && !treeBlock.getNumberoftrees().equals(oldTreeBlock.getNumberoftrees())) {
                    updates = updates + oldTreeBlock.getNumberoftrees() + " chnaged to " + treeBlock.getNumberoftrees() + "\n";
                }

                if (treeBlock.getTreeblockstatusId() != null && !treeBlock.getTreeblockstatusId().equals(oldTreeBlock.getTreeblockstatusId())) {
                    updates = updates + oldTreeBlock.getTreeblockstatusId().getName() + " chnaged to " + treeBlock.getTreeblockstatusId().getName() + "\n";
                }

                if (treeBlock.getTappinglayerId() != null && !treeBlock.getTappinglayerId().equals(oldTreeBlock.getTappinglayerId())) {
                    updates = updates + oldTreeBlock.getTappinglayerId().getName() + " chnaged to " + treeBlock.getTappinglayerId().getName() + "\n";
                }

                if (treeBlock.getPlanteddate() != null && !treeBlock.getPlanteddate().equals(oldTreeBlock.getPlanteddate())) {
                    updates = updates + oldTreeBlock.getPlanteddate().toString() + "(dop)" + " chnaged to " + treeBlock.getPlanteddate().toString() + "\n";
                }

                if (treeBlock.getEstimatedlifetime() != null && !treeBlock.getEstimatedlifetime().equals(oldTreeBlock.getEstimatedlifetime())) {
                    updates = updates + oldTreeBlock.getEstimatedlifetime() + " chnaged to " + treeBlock.getEstimatedlifetime() + "\n";
                }

                if (treeBlock.getArea() != null && !treeBlock.getArea().equals(oldTreeBlock.getArea())) {
                    updates = updates + oldTreeBlock.getArea() + " chnaged to " + treeBlock.getArea() + "\n";
                }

                if (treeBlock.getCloneId() != null && !treeBlock.getCloneId().equals(oldTreeBlock.getCloneId())) {
                    updates = updates + oldTreeBlock.getCloneId().getName() + " chnaged to " + treeBlock.getCloneId().getName() + "\n";
                }

                if (!(oldTreeBlock.getDescription() != null
                        && treeBlock.getDescription() != null
                        && oldTreeBlock.getDescription().equals(treeBlock.getDescription()))) {
                    if (oldTreeBlock.getDescription() != treeBlock.getDescription()) {
                        updates = updates + oldTreeBlock.getDescription()
                                + " chnaged to " + treeBlock.getDescription() + "\n";
                    }
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
        String updates = getUpdates();

        if (updates.isEmpty()) {
            Alert alert = SystemAlert.createAlert(
                    "Update Tree Block",
                    "Nothing updates",
                    updates,
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);
            alert.showAndWait();
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Update Tree Block",
                    "Are you sure you want to update the following Tree Block ?",
                    updates,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TreeblockDao.update(treeBlock);
                loadForm();
                fillTable(TreeblockDao.getAll());
            }
        }
    }

    @FXML
    private void btnTPDeleteAP(ActionEvent event) {
        if (tblTreeBlock.getSelectionModel().getSelectedItem() != null) {
            String details = "\nName :  \t\t\t" + treeBlock.getNo()
                    + "\nGender :  \t\t\t" + treeBlock.getNumberoftrees().toString()
                    + "\nBirth Date :   \t\t" + treeBlock.getTappinglayerId().getName()
                    + "\nNIC No :       \t\t" + treeBlock.getTappinglayerId().getName()
                    + "\nCivilstatus :  \t\t" + treeBlock.getPlanteddate().toString()
                    + "\nAddress :      \t\t" + treeBlock.getEstimatedlifetime().toString()
                    + "\nMobile No :    \t\t" + treeBlock.getArea().toString()
                    + "\nLand :         \t\t" + treeBlock.getCloneId().getName()
                    + "\nEmail :        \t\t" + treeBlock.getDescription();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Tree Block");
            alert.setHeaderText("Are you sure you want to delete the following Tree Block ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TreeblockDao.delete(treeBlock);
                loadForm();
                fillTable(TreeblockDao.getAll());
            }
        }
    }

//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Search Methods">
    private void updateTable() {
        String blockNo = txtSearchBlockNo.getText().trim();
        boolean sBlockNo = !blockNo.isEmpty();
        Treeblockstatus status = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean sStatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;
        Tappinglayer tappingLayer = cmbSearchLayer.getSelectionModel().getSelectedItem();
        boolean sTappingLayer = cmbSearchLayer.getSelectionModel().getSelectedIndex() != -1;

        if (!sBlockNo && !sStatus && !sTappingLayer) {
            fillTable(TreeblockDao.getAll());
        }
        if (sBlockNo && !sStatus && !sTappingLayer) {
            fillTable(TreeblockDao.getAllByBlockNo(blockNo));
        }
        if (!sBlockNo && !sStatus && sTappingLayer) {
            fillTable(TreeblockDao.getAllByTappingLayer(tappingLayer));
        }
        if (!sBlockNo && sStatus && !sTappingLayer) {
            fillTable(TreeblockDao.getAllByStatus(status));
        }
        if (sBlockNo && sStatus && !sTappingLayer) {
            fillTable(TreeblockDao.getAllByBlockNoStatus(blockNo, status));
        }
        if (sBlockNo && !sStatus && sTappingLayer) {
            fillTable(TreeblockDao.getAllByBlockNoTappingLayer(blockNo, tappingLayer));
        }
        if (!sBlockNo && sStatus && sTappingLayer) {
            fillTable(TreeblockDao.getAllByStatusTappingLayer(status, tappingLayer));
        }
        if (sBlockNo && sStatus && sTappingLayer) {
            fillTable(TreeblockDao.getAllByBlockNoStatusTappingLayer(blockNo, status, tappingLayer));
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
    private void txtSearchBlockNoKR(KeyEvent event) {
        if (txtSearchBlockNo.getText() != null) {
            if (!lock) {
                cmbSearchStatus.getSelectionModel().clearSelection();
                cmbSearchLayer.getSelectionModel().clearSelection();
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {
        if (cmbSearchStatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                txtSearchBlockNo.setText("");
                cmbSearchLayer.getSelectionModel().clearSelection();
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchLayerAP(ActionEvent event) {
        if (cmbSearchLayer.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchStatus.getSelectionModel().clearSelection();
                txtSearchBlockNo.setText("");
            }
            updateTable();
        }
    }

    @FXML
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Blocks on map">
    @FXML
    private void tglBlockAAP(ActionEvent event
    ) {
        System.out.println(tglBlockA.getText());
    }

    @FXML
    private void tglBlockBAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockCAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockDAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockEAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockFAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockGAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockHAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockIAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockJAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockKAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockLAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockMAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockNAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockOAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockPAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockQAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockRAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockSAP(ActionEvent event
    ) {
    }

    @FXML
    private void tglBlockTAP(ActionEvent event
    ) {
    }

//</editor-fold>
}

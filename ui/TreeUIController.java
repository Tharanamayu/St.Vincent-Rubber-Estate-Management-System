/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CloneDao;
import dao.TappinglayerDao;
import dao.TreeDao;
import dao.TreeblockDao;
import dao.TreestatusDao;
import entity.Tappinglayer;
import entity.Tree;
import entity.Treeblock;
import entity.Treeblockstatus;
import entity.Treestatus;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class TreeUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    Tree tree;
    Tree oldTree;

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
    private TextField txtTreeNo;
    @FXML
    private ComboBox<Treestatus> cmbTreeStatus;
    @FXML
    private ComboBox<Treeblock> cmbTreeBlock;
    @FXML
    private Button btnTPInsert;
    @FXML
    private Button btnTPUpdate;
    @FXML
    private Button btnTPDelete;
    @FXML
    private Button btnTPClear;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Tree> tblTree;
    ;
    @FXML
    private ComboBox<Treestatus> cmbSearchStatus;
    @FXML
    private ComboBox<Treeblock> cmbSearchBlock;
    @FXML
    private TextField txtSearchTreeNo;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Button btnSearchClear;
    @FXML
    private TableColumn<Tree, String> colTreeNo;
    @FXML
    private TableColumn<Tree, Treestatus> colTreeStatus;
    @FXML
    private TableColumn<Tree, Treeblock> colTreeBlock;
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
    }

    private void loadForm() {
        tree = new Tree();
        oldTree = null;

        txtTreeNo.setText("");
        cmbTreeStatus.setItems(TreestatusDao.getAll());
        cmbTreeStatus.getSelectionModel().clearSelection();
        cmbTreeBlock.setItems(TreeblockDao.getAll());
        cmbTreeBlock.getSelectionModel().clearSelection();

        disableButtons(false, false, true, true);
        setStyle(initial);
    }

    private void setStyle(String style) {
        txtTreeNo.setStyle(style);
        cmbTreeStatus.setStyle(style);
        cmbTreeBlock.setStyle(style);
    }

    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnTPInsert.setDisable(insert || !privilege.get("Tree_insert"));
        btnTPUpdate.setDisable(update || !privilege.get("Tree_update"));
        btnTPDelete.setDisable(delete || !privilege.get("Tree_delete"));
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        colTreeNo.setCellValueFactory(new PropertyValueFactory("no"));
        colTreeStatus.setCellValueFactory(new PropertyValueFactory("treestatusId"));
        colTreeBlock.setCellValueFactory(new PropertyValueFactory("blockId"));

        txtSearchTreeNo.setText("");
        cmbSearchStatus.setItems(TreestatusDao.getAll());
        cmbSearchStatus.getSelectionModel().clearSelection();
        cmbSearchBlock.setItems(TreeblockDao.getAll());
        cmbSearchBlock.getSelectionModel().clearSelection();

        fillTable(TreeDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void fillTable(ObservableList<Tree> tree) {
        if (tree != null && tree.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((tree.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? tree.size() : pageIndex * rowsCount + rowsCount;
                    tblTree.getItems().clear();
                    tblTree.setItems(FXCollections.observableArrayList(tree.subList(start, end)));
                    return tblTree;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblTree.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblTree.getSelectionModel().select(row);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Binding Methods">
    @FXML
    private void txtTreeNoKR(KeyEvent event) {
        if (!txtTreeNo.getText().isEmpty()) {
            if (tree.setNo(txtTreeNo.getText().trim())) {
                if (oldTree != null && !tree.getNo().equals(oldTree.getNo())) {
                    txtTreeNo.setStyle(updated);
                } else {
                    txtTreeNo.setStyle(valid);
                }
            } else {
                txtTreeNo.setStyle(invalid);
            }
        } else {
            txtTreeNo.setStyle(initial);
        }
    }

    @FXML
    private void cmbTreeStatusAP(ActionEvent event) {
        tree.setTreestatusId(cmbTreeStatus.getSelectionModel().getSelectedItem());
        if (oldTree != null && !tree.getTreestatusId().equals(oldTree.getTreestatusId())) {
            cmbTreeStatus.setStyle(updated);
        } else {
            cmbTreeStatus.setStyle(valid);
        }
    }

    @FXML
    private void cmbTreeBlockAP(ActionEvent event) {
        tree.setBlockId(cmbTreeBlock.getSelectionModel().getSelectedItem());
        if (oldTree != null && !tree.getBlockId().equals(oldTree.getBlockId())) {
            cmbTreeBlock.setStyle(updated);
        } else {
            cmbTreeBlock.setStyle(valid);
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="CRUD Operations">
    @FXML
    private void btnTPClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear Tree form",
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
            if (tree.getNo() == null) {
                errors = errors + "Tree No \tis not entered\n";
            }
            if (tree.getTreestatusId() == null) {
                errors = errors + "Tree Status \tis not selected\n";
            }
            if (tree.getBlockId() == null) {
                errors = errors + "Tree Block \tis not selected\n";
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
            String details = "Tree No :  \t\t" + tree.getNo()
                    + "\nTree Status :  \t\t" + tree.getTreestatusId().getName()
                    + "\nTree Block :   \t" + tree.getBlockId().getNo();

            Alert alert = SystemAlert.createAlert(
                    "Add Tree",
                    "Are you sure you want to add the following Tree ?",
                    details,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage
            );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TreeDao.insert(tree);
                loadForm();
                fillTable(TreeDao.getAll());
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
        if (tblTree.getSelectionModel().getSelectedItem() != null) {
            disableButtons(false, true, false, false);
            setStyle(valid);

            oldTree = TreeDao.getById(tblTree.getSelectionModel().getSelectedItem().getId());
            tree = TreeDao.getById(tblTree.getSelectionModel().getSelectedItem().getId());

            txtTreeNo.setText(tree.getNo());
            cmbTreeStatus.getSelectionModel().select(tree.getTreestatusId());
            cmbTreeBlock.getSelectionModel().select(tree.getBlockId());

            page = pagination.getCurrentPageIndex();
            row = tblTree.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblTreeMC(MouseEvent event) {
        fillFrom();
    }

    @FXML
    private void tblTreeKR(KeyEvent event) {
        fillFrom();
    }

    private String getUpdates() {
        String updates = "";
        try {

            if (oldTree != null) {

                if (tree.getNo() != null && !tree.getNo().equals(oldTree.getNo())) {
                    updates = updates + oldTree.getNo() + " chnaged to " + tree.getNo() + "\n";
                }

                if (tree.getTreestatusId() != null && !tree.getTreestatusId().equals(oldTree.getTreestatusId())) {
                    updates = updates + oldTree.getTreestatusId() + " chnaged to " + tree.getTreestatusId() + "\n";
                }

                if (tree.getBlockId() != null && !tree.getBlockId().equals(oldTree.getBlockId())) {
                    updates = updates + oldTree.getBlockId() + " chnaged to " + tree.getBlockId() + "\n";
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
                    "Update Tree",
                    "Nothing updates",
                    updates,
                    Alert.AlertType.INFORMATION,
                    ui.Main.stage);
            alert.showAndWait();
        } else {
            Alert alert = SystemAlert.createAlert(
                    "Update Tree",
                    "Are you sure you want to update the following Tree ?",
                    updates,
                    Alert.AlertType.CONFIRMATION,
                    ui.Main.stage);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TreeDao.update(tree);
                loadForm();
                fillTable(TreeDao.getAll());
            }
        }
    }

    @FXML
    private void btnTPDeleteAP(ActionEvent event) {
        if (tblTree.getSelectionModel().getSelectedItem() != null) {
            String details = "\nName :  \t\t\t" + tree.getNo()
                    + "\nGender :  \t\t\t" + tree.getTreestatusId().getName()
                    + "\nBirth Date :   \t\t" + tree.getBlockId().getNo();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Tree");
            alert.setHeaderText("Are you sure you want to delete the following Tree ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                TreeDao.delete(tree);
                loadForm();
                fillTable(TreeDao.getAll());
            }
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Search Methods">
    private void updateTable() {
        String treeNo = txtSearchTreeNo.getText().trim();
        boolean sTreeNo = !treeNo.isEmpty();
        Treestatus status = cmbSearchStatus.getSelectionModel().getSelectedItem();
        boolean sStatus = cmbSearchStatus.getSelectionModel().getSelectedIndex() != -1;
        Treeblock treeBlock = cmbSearchBlock.getSelectionModel().getSelectedItem();
        boolean sTreeBlock = cmbSearchBlock.getSelectionModel().getSelectedIndex() != -1;

        if (!sTreeNo && !sStatus && !sTreeBlock) {
            fillTable(TreeDao.getAll());
        }
        if (sTreeNo && !sStatus && !sTreeBlock) {
            fillTable(TreeDao.getAllByTreekNo(treeNo));
        }
        if (!sTreeNo && !sStatus && sTreeBlock) {
            fillTable(TreeDao.getAllByTreeBlock(treeBlock));
        }
        if (!sTreeNo && sStatus && !sTreeBlock) {
            fillTable(TreeDao.getAllByStatus(status));
        }
        if (sTreeNo && sStatus && !sTreeBlock) {
            fillTable(TreeDao.getAllByTreeNoStatus(treeNo, status));
        }
        if (sTreeNo && !sStatus && sTreeBlock) {
            fillTable(TreeDao.getAllByTreeNoTreeBlock(treeNo, treeBlock));
        }
        if (!sTreeNo && sStatus && sTreeBlock) {
            fillTable(TreeDao.getAllByStatusTreeBlock(status, treeBlock));
        }
        if (sTreeNo && sStatus && sTreeBlock) {
            fillTable(TreeDao.getAllByTreeNoStatusTreeBlock(treeNo, status, treeBlock));
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
    private void txtSearchTreeNoKR(KeyEvent event) {
        if (txtSearchTreeNo.getText() != null) {
            if (!lock) {
                cmbSearchStatus.getSelectionModel().clearSelection();
                cmbSearchBlock.getSelectionModel().clearSelection();
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchStatusAP(ActionEvent event) {
        if (cmbSearchStatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchBlock.getSelectionModel().clearSelection();
                txtSearchTreeNo.setText("");
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchBlockAP(ActionEvent event) {
        if (cmbSearchBlock.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchStatus.getSelectionModel().clearSelection();
                txtSearchTreeNo.setText("");
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.CivilstatusDao;
import dao.DesignationDao;
import dao.EmployeeDao;
import dao.EmployeestatusDao;
import dao.GenderDao;
import entity.Civilstatus;
import entity.Designation;
import entity.Employee;
import entity.Employeestatus;
import entity.Gender;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import report.ReportView;
import static ui.LoginUIController.privilege;
import static ui.Main.stage;
import static ui.MainWindowController.lastDirectory;
import util.SystemAlert;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class EmployeeUIController implements Initializable {

//<editor-fold defaultstate="collapsed" desc="Form Data">
    Employee employee;
    Employee oldEmployee;

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
    private ImageView imgPhoto;
    @FXML
    private ComboBox<Designation> cmbDesignation;
    @FXML
    private DatePicker dtpAssign;
    @FXML
    private ComboBox<Employeestatus> cmbEmployeestatus;
    @FXML
    private TextField txtSearchName;
    @FXML
    private ComboBox<Employeestatus> cmbSearchEmployeestatus;
    @FXML
    private ComboBox<Designation> cmbSearchDesignation;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSearchLock;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<Employee> tblEmployee;
    @FXML
    private TableColumn<Employee, String> colName;
    @FXML
    private TableColumn<Employee, Employee> colStatus;
    @FXML
    private TableColumn<Employee, Designation> colDesignation;
    @FXML
    private TableColumn<Employee, String> colMobile;
    @FXML
    private TableColumn<Employee, String> colEmail;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnPhotoSelect;
    @FXML
    private Button btnPhotoClear;
    @FXML
    private Button btnSearchClear;
    @FXML
    private Label lblNew;
    @FXML
    private Button btnEmpRepDemo;
//</editor-fold>

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
        employee = new Employee();
        oldEmployee = null;

        cmbGender.setItems(GenderDao.getAll());
        cmbGender.getSelectionModel().clearSelection();

        cmbCivilstatus.setItems(CivilstatusDao.getAll());
        cmbCivilstatus.getSelectionModel().clearSelection();

        cmbDesignation.setItems(DesignationDao.getAll());
        cmbDesignation.getSelectionModel().clearSelection();

        cmbEmployeestatus.setItems(EmployeestatusDao.getAll());
        cmbEmployeestatus.getSelectionModel().clearSelection();

        cmbEmployeestatus.getSelectionModel().select(0);
        cmbEmployeestatus.setDisable(true);
        employee.setEmployeestatusId(cmbEmployeestatus.getSelectionModel().getSelectedItem());

        dtpAssign.setDisable(true);
        dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        Date assign = java.sql.Date.valueOf(dtpAssign.getValue());
        employee.setAssigned(assign);

        dtpDob.setDisable(true);
        //cmbGender.setDisable(true);

        txtName.setText("");
        txtAddress.setText("");
        txtNic.setText("");
        txtMobile.setText("");
        txtLand.setText("");
        txtEmail.setText("");
        dtpDob.setValue(null);
        dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

        imgPhoto.setImage(new Image("/images/avatar.jpg"));
        photoSelected = false;

        disableButtons(false, false, true, true);
        setStyle(initial);

    }

    private void setStyle(String style) {
        cmbGender.setStyle(style);
        cmbCivilstatus.setStyle(style);
        cmbDesignation.setStyle(style);
        cmbEmployeestatus.setStyle(style);

        txtName.setStyle(style);
        txtNic.setStyle(style);
        txtMobile.setStyle(style);
        txtLand.setStyle(style);
        txtEmail.setStyle(style);

        if (!txtAddress.getChildrenUnmodifiable().isEmpty()) {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(style);
        }

        dtpDob.getEditor().setStyle(style);
        dtpAssign.getEditor().setStyle(style);
    }

    private void disableButtons(boolean select, boolean insert, boolean update, boolean delete) {
        btnAdd.setDisable(insert || !privilege.get("Employee_insert"));
        btnUpdate.setDisable(update || !privilege.get("Employee_update"));
        btnDelete.setDisable(delete || !privilege.get("Employee_delete"));
    }

    private void loadTable() {
        lock = false;
        btnSearchLock.setText("Lock");

        cmbSearchEmployeestatus.setItems(EmployeestatusDao.getAll());
        cmbSearchEmployeestatus.getSelectionModel().clearSelection();
        cmbSearchDesignation.setItems(DesignationDao.getAll());
        cmbSearchDesignation.getSelectionModel().clearSelection();
        txtSearchName.setText("");

        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory("employeestatusId"));
        colMobile.setCellValueFactory(new PropertyValueFactory("mobile"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colDesignation.setCellValueFactory(new PropertyValueFactory("designationId"));

        fillTable(EmployeeDao.getAll());
        pagination.setCurrentPageIndex(0);
    }

    private void fillTable(ObservableList<Employee> employees) {
        if (employees != null && employees.size() != 0) {

            int rowsCount = 8;
            int pageCount = ((employees.size() - 1) / rowsCount) + 1;
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer pageIndex) {
                    int start = pageIndex * rowsCount;
                    int end = pageIndex == pageCount - 1 ? employees.size() : pageIndex * rowsCount + rowsCount;
                    tblEmployee.getItems().clear();
                    tblEmployee.setItems(FXCollections.observableArrayList(employees.subList(start, end)));
                    return tblEmployee;
                }
            });

        } else {
            pagination.setPageCount(1);
            tblEmployee.getItems().clear();
        }

        pagination.setCurrentPageIndex(page);
        tblEmployee.getSelectionModel().select(row);
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Binding">
    @FXML
    private void txtNameKR(KeyEvent event) {
        if (!txtName.getText().isEmpty()) {
            if (employee.setName(txtName.getText().trim())) {
                if (oldEmployee != null && !employee.getName().equals(oldEmployee.getName())) {
                    txtName.setStyle(updated);
                } else {
                    txtName.setStyle(valid);
                }
            } else {
                txtName.setStyle(invalid);
            }
        } else {
            txtName.setStyle(initial);
        }
    }

    @FXML
    private void cmbCivilstatusAP(ActionEvent event) {
        employee.setCivilstatusId(cmbCivilstatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
            cmbCivilstatus.setStyle(updated);
        } else {
            cmbCivilstatus.setStyle(valid);
        }
    }

    @FXML
    private void dtpDobAP(ActionEvent event) {
        if (dtpDob.getValue() != null) {
            Date today = new Date();
            today.setYear(today.getYear() - 18);
            Date dob = java.sql.Date.valueOf(dtpDob.getValue());
            if (dob.before(today)) {
                employee.setDob(dob);
                if (oldEmployee != null && !employee.getDob().equals(oldEmployee.getDob())) {
                    dtpDob.getEditor().setStyle(updated);
                } else {
                    dtpDob.getEditor().setStyle(valid);
                }
            } else {
                dtpDob.getEditor().setStyle(invalid);
                employee.setDob(null);
            }
        }
    }

    @FXML
    private void txtNicKR(KeyEvent event) {
        if ((txtNic.getText().matches("\\d{4}[1|2|3|5|6|7|8]{1}\\d{7}") || txtNic.getText().matches("\\d{2}[1|2|3|5|6|7|8]{1}\\d{6}[V|v|x|X]")) && txtNic.getText() != null) {

            String tempNic = txtNic.getText().trim();
            ObservableList<Employee> employeeNic = EmployeeDao.checkNic(tempNic);

            if (employeeNic.isEmpty() && employee.setNic(tempNic)) {
                if (oldEmployee != null && !employee.getNic().equals(oldEmployee.getNic())) {
                    txtNic.setStyle(updated);
                    nicCrossValidation();
                } else {
                    txtNic.setStyle(valid);
                    nicCrossValidation();
                }
            } else if (employeeNic.size() == 1 && oldEmployee == null) {
                txtNic.setStyle(invalid);
            } else if (employeeNic.size() == 1 && employeeNic.get(0).getNic().equals(tempNic) && oldEmployee.getNic().equals(tempNic)) {
                //can be simplified
                if (employee.setNic(tempNic)) {
                    txtNic.setStyle(valid);
                    nicCrossValidation();

                } else {
                    txtNic.setStyle(invalid);
                }
                /////////////////////////////
            } else {
                txtNic.setStyle(invalid);
            }
        } else {
            txtNic.setStyle(invalid);
        }
    }

    private void nicCrossValidation() {
        int year = 0;
        int days = 0;
        int month = 0;
        int day = 0;
        String gender = "";
        int months[] = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

//<editor-fold defaultstate="collapsed" desc="9 digit NIC">
        if (txtNic.getText().matches("\\d{2}[1|2|3|5|6|7|8]{1}\\d{6}[V|v|x|X]") && txtNic.getText() != null) {
            String testNic = txtNic.getText();
            year = Integer.parseInt("19" + testNic.substring(0, 2));
            days = Integer.parseInt(testNic.substring(2, 5));
        }//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="12 digit NIC">
        else if (txtNic.getText().matches("\\d{12}") && txtNic.getText() != null) {
            String testNic = txtNic.getText();
            year = Integer.parseInt(testNic.substring(0, 4));
            days = Integer.parseInt(testNic.substring(4, 7));
        } //</editor-fold>
        else {
            //System.out.println("Check the NIC, Unknown formate");
        }

        if (days > 500 && days < 865) {
            gender = "Female";
            days = days - 500;

        } else if (days > 0 && days < 366) {
            gender = "Male";
        }

        for (int i = 0; i < months.length; i++) {
            if (days < months[i]) {
                month = i + 1;
                day = days;
                break;
            } else {
                days = days - months[i];
            }
        }

        dtpDob.setValue(LocalDate.of(year, month, day));
        Date Bday = java.sql.Date.valueOf(dtpDob.getValue());
        employee.setDob(Bday);

        if (gender.equalsIgnoreCase("Female")) {
            cmbGender.getSelectionModel().clearAndSelect(1);
        } else if (gender.equalsIgnoreCase("Male")) {
            cmbGender.getSelectionModel().clearAndSelect(0);
        }
    }

    @FXML
    private void cmbGenderAP(ActionEvent event) {
        employee.setGenderId(cmbGender.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getGenderId().equals(oldEmployee.getGenderId())) {
            cmbGender.setStyle(updated);
        } else {
            cmbGender.setStyle(valid);
        }
    }

    @FXML
    private void txtAddressKR(KeyEvent event) {
        if (employee.setAddress(txtAddress.getText().trim())) {
            if (oldEmployee != null && !employee.getAddress().equals(oldEmployee.getAddress())) {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(updated);
            } else {
                ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(valid);
            }
        } else {
            ((ScrollPane) txtAddress.getChildrenUnmodifiable().get(0)).getContent().setStyle(invalid);
        }
    }

    @FXML
    private void txtMobileKR(KeyEvent event) {
        if (employee.setMobile(txtMobile.getText().trim())) {
            if (oldEmployee != null && !employee.getMobile().equals(oldEmployee.getMobile())) {
                txtMobile.setStyle(updated);
            } else {
                txtMobile.setStyle(valid);
            }
        } else {
            txtMobile.setStyle(invalid);
        }
    }

    @FXML
    private void txtLandKR(KeyEvent event) {
        if (employee.setLand(txtLand.getText())) {
            if (oldEmployee != null && oldEmployee.getLand() != null && employee.getLand() != null && oldEmployee.getLand().equals(employee.getLand())) {
                txtLand.setStyle(valid);
            } else if (oldEmployee != null && oldEmployee.getLand() != employee.getLand()) {
                txtLand.setStyle(updated);
            } else {
                txtLand.setStyle(valid);
            }
        } else {
            txtLand.setStyle(invalid);
        }
    }

    @FXML
    private void txtEmailKR(KeyEvent event) {
        if (employee.setEmail(txtEmail.getText().trim())) {
            if (oldEmployee != null && oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail())) {
                txtEmail.setStyle(valid);
            } else if (oldEmployee != null && oldEmployee.getEmail() != employee.getEmail()) {
                txtEmail.setStyle(updated);
            } else {
                txtEmail.setStyle(valid);
            }

        } else {
            txtEmail.setStyle(invalid);
        }
    }

    @FXML
    private void cmbDesignationAP(ActionEvent event) {
        employee.setDesignationId(cmbDesignation.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
            cmbDesignation.setStyle(updated);
        } else {
            cmbDesignation.setStyle(valid);
        }
    }

    @FXML
    private void dtpAssignAP(ActionEvent event) {
        if (dtpAssign.getValue() != null) {
            Date today = new Date();
            Date assign = java.sql.Date.valueOf(dtpAssign.getValue());
            if (assign.before(today)) {
                employee.setAssigned(assign);
                if (oldEmployee != null && !employee.getAssigned().equals(oldEmployee.getAssigned())) {
                    dtpAssign.getEditor().setStyle(updated);
                } else {
                    dtpAssign.getEditor().setStyle(valid);
                }
            } else {
                dtpAssign.getEditor().setStyle(invalid);
                employee.setAssigned(null);
            }
        }
    }

    @FXML
    private void cmbEmployeestatusAP(ActionEvent event) {
        employee.setEmployeestatusId(cmbEmployeestatus.getSelectionModel().getSelectedItem());
        if (oldEmployee != null && !employee.getEmployeestatusId().equals(oldEmployee.getEmployeestatusId())) {
            cmbEmployeestatus.setStyle(updated);
        } else {
            cmbEmployeestatus.setStyle(valid);
        }
    }

    @FXML
    private void btnPhotoSelectAP(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            lastDirectory = file.getParentFile();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] image = new byte[(int) file.length()];
                DataInputStream dataIs = new DataInputStream(new FileInputStream(file));
                dataIs.readFully(image);

                ImageIcon img = new ImageIcon(image);
                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.'));
                if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".gif")) {

                    if (img.getIconHeight() <= 100 && img.getIconWidth() <= 100) {
                        Image photo = new Image(fis);
                        imgPhoto.setImage(photo);
                        employee.setImage(image);
                        photoSelected = true;

                    } else {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error - Employee add");
                        alert.setHeaderText("Photo select error");
                        alert.setContentText("Image should be smaller than 100x100");
                        alert.showAndWait();
                        photoSelected = false;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error - Employee add");
                    alert.setHeaderText("Photo select error");
                    alert.setContentText("Image should be JPG, GIF or PNG");
                    alert.showAndWait();
                    photoSelected = false;
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(EmployeeUIController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EmployeeUIController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            photoSelected = false;
        }
    }

    @FXML
    private void btnPhotoClearAP(ActionEvent event) {
        if (employee.getImage() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add employee");
            alert.setHeaderText("Clear Epmloyee");
            alert.setContentText("Are you sure ?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                imgPhoto.setImage(new Image("/image/user.png"));
                if (oldEmployee != null && oldEmployee.getImage() != null) {
                    photoSelected = true;
                } else {
                    photoSelected = false;
                }
                employee.setImage(null);
            }
        }
    }

    @FXML
    private void lblNewDesignationMC(MouseEvent event) {
        Dialog dlg = new Dialog();
        dlg.setResizable(true);

        try {
            AnchorPane x = FXMLLoader.load(Main.class.getResource("DesignationUI.fxml"));
            dlg.setGraphic(x);
        } catch (IOException ex) {
            System.out.println("Can't load Designation " + ex.getMessage());
        }
        dlg.show();

        cmbDesignation.setItems(DesignationDao.getAll());
        cmbDesignation.getSelectionModel().clearSelection();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Operation">
    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert alert = SystemAlert.createAlert(
                "Clear",
                "Clear employee form",
                "Are you sure you want to clear employee the form ?",
                Alert.AlertType.CONFIRMATION,
                ui.Main.stage
        );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            loadForm();
        }
    }

    private String getErrors() {
        String errors = "";
        try {
            if (employee.getName() == null) {
                errors = errors + "Name \t\tis Invalid\n";
            }
            if (employee.getGenderId() == null) {
                errors = errors + "Gender \t\tis Not Selected\n";
            }
            if (employee.getCivilstatusId() == null) {
                errors = errors + "Civilstatus \tis Not Selected\n";
            }

            if (employee.getAddress() == null) {
                errors = errors + "Address \t\tis Invalid\n";
            }

            if (employee.getDob() == null) {
                if (dtpDob.getValue() == null) {
                    errors = errors + "Birth Date \tis not selected\n";
                } else {
                    errors = errors + "Age \tis above 18\n";
                }
            }

            if (employee.getNic() == null) {
                if (txtNic.getText() == null || "".equals(txtNic.getText())) {
                    errors = errors + "NIC No. \t\tis not entered\n";
                } else if (!txtNic.getText().matches("\\d{9}[V|v|x|X]")) {
                    errors = errors + "NIC No. \t\tis not complete or invalide\n";
                } else {
                    errors = errors + "NIC No. \t\tDuplicate NIC\n";
                }
            }

            if (employee.getMobile() == null) {
                errors = errors + "Mobile No. \tis Invalid\n";
            }

            if (txtLand.getText() != null && !employee.setLand(txtLand.getText().trim())) {
                errors = errors + "Land No. \t\tis Invalid\n";
            }
            if (txtEmail.getText() != null && !employee.setEmail(txtEmail.getText().trim())) {
                errors = errors + "Email \t\tis Invalid\n";
            }

            if (employee.getDesignationId() == null) {
                errors = errors + "Designation \tis Not Selected\n";
            }

            if (employee.getAssigned() == null) {
                errors = errors + "Assign Date \tis Invalid\n";
            }

            if (employee.getEmployeestatusId() == null) {
                errors = errors + "Status \t\tis Not Selected\n";
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
    private void btnAddAP(ActionEvent event) {
        String error = getErrors();

        if (error.isEmpty()) {
            String details = "\nName :  \t\t" + employee.getName()
                    + "\nGender :  \t\t" + employee.getGenderId().getName()
                    + "\nBirth Date :   \t\t" + employee.getDob().toString()
                    + "\nNIC No :       \t\t" + employee.getNic()
                    + "\nCivilstatus :  \t\t" + employee.getCivilstatusId().getName()
                    + "\nAddress :      \t\t" + employee.getAddress()
                    + "\nMobile No :    \t\t" + employee.getMobile()
                    + "\nLand :         \t\t" + employee.getLand()
                    + "\nEmail :        \t\t" + employee.getEmail()
                    + "\nDesignation :  \t\t" + employee.getDesignationId().getName()
                    + "\nAssigned Date :  \t" + employee.getAssigned().toString()
                    + "\nPhoto :  \t\t\t" + (employee.getImage() == null ? "Not Selected" : "Selected")
                    + "\nStatus :  \t\t" + employee.getEmployeestatusId().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Employee");
            alert.setHeaderText("Are you sure you want to add the following Employee ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                EmployeeDao.add(employee);
                loadForm();
                fillTable(EmployeeDao.getAll());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add - Error");
            alert.setHeaderText("Correct the error");
            alert.setContentText(error);
            alert.showAndWait();
        }
    }

    private void fillFrom() {
        if (tblEmployee.getSelectionModel().getSelectedItem() != null) {
            disableButtons(false, true, false, false);
            setStyle(valid);
            cmbEmployeestatus.setDisable(false);

            oldEmployee = EmployeeDao.getById(tblEmployee.getSelectionModel().getSelectedItem().getId());
            employee = EmployeeDao.getById(tblEmployee.getSelectionModel().getSelectedItem().getId());

            cmbGender.getSelectionModel().select((Gender) employee.getGenderId());
            cmbCivilstatus.getSelectionModel().select((Civilstatus) employee.getCivilstatusId());
            cmbDesignation.getSelectionModel().select((Designation) employee.getDesignationId());
            cmbEmployeestatus.getSelectionModel().select((Employeestatus) employee.getEmployeestatusId());

            txtName.setText(employee.getName());
            txtAddress.setText(employee.getAddress());
            txtNic.setText(employee.getNic());
            txtMobile.setText(employee.getMobile());
            txtLand.setText(employee.getLand());
            txtEmail.setText(employee.getEmail());

            dtpDob.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob())));
            dtpDob.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getDob()));
            dtpAssign.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned())));
            dtpAssign.getEditor().setText(new SimpleDateFormat("yyyy-MM-dd").format(employee.getAssigned()));

            if (employee.getImage() != null) {
                imgPhoto.setImage(new Image(new ByteArrayInputStream(employee.getImage())));
            } else {
                imgPhoto.setImage(new Image("/images/avatar.jpg"));
            }

            page = pagination.getCurrentPageIndex();
            row = tblEmployee.getSelectionModel().getSelectedIndex();
        }
    }

    @FXML
    private void tblEmployeeMC(MouseEvent event) {
        fillFrom();
    }

    @FXML
    private void tblEmployeeKR(KeyEvent event) {
        fillFrom();
    }

    private String getUpdates() {
        String updates = "";
        try {

            if (oldEmployee != null) {

                if (employee.getName() != null && !employee.getName().equals(oldEmployee.getName())) {
                    updates = updates + oldEmployee.getName() + " chnaged to " + employee.getName() + "\n";
                }

                if (employee.getAddress() != null && !employee.getAddress().equals(oldEmployee.getAddress())) {
                    updates = updates + oldEmployee.getAddress() + " chnaged to " + employee.getAddress() + "\n";
                }

                if (employee.getNic() != null && !employee.getNic().equals(oldEmployee.getNic())) {
                    updates = updates + oldEmployee.getNic() + " chnaged to " + employee.getNic() + "\n";
                }

                if (!(oldEmployee.getLand() != null
                        && employee.getLand() != null
                        && oldEmployee.getLand().equals(employee.getLand()))) {
                    if (oldEmployee.getLand() != employee.getLand()) {
                        updates = updates + oldEmployee.getLand()
                                + " chnaged to " + employee.getLand() + "\n";
                    }
                }

                if (!(oldEmployee.getEmail() != null && employee.getEmail() != null && oldEmployee.getEmail().equals(employee.getEmail()))) {
                    if (oldEmployee.getEmail() != employee.getEmail()) {
                        updates = updates + oldEmployee.getEmail() + " chnaged to " + employee.getEmail() + "\n";
                    }
                }

                if (employee.getMobile() != null && !employee.getMobile().equals(oldEmployee.getMobile())) {
                    updates = updates + oldEmployee.getMobile() + " chnaged to " + employee.getMobile() + "\n";
                }

                if (employee.getGenderId() != null && !employee.getGenderId().equals(oldEmployee.getGenderId())) {
                    updates = updates + oldEmployee.getGenderId() + " chnaged to " + employee.getGenderId() + "\n";
                }

                if (employee.getCivilstatusId() != null && !employee.getCivilstatusId().equals(oldEmployee.getCivilstatusId())) {
                    updates = updates + oldEmployee.getCivilstatusId() + " chnaged to " + employee.getCivilstatusId() + "\n";
                }

                if (employee.getDesignationId() != null && !employee.getDesignationId().equals(oldEmployee.getDesignationId())) {
                    updates = updates + oldEmployee.getDesignationId() + " chnaged to " + employee.getDesignationId() + "\n";
                }

                if (employee.getDob() != null && !employee.getDob().equals(oldEmployee.getDob())) {
                    updates = updates + oldEmployee.getDob().toString() + "(dob)" + " chnaged to " + employee.getDob().toString() + "\n";
                }

                if (employee.getAssigned() != null && !employee.getAssigned().equals(oldEmployee.getAssigned())) {
                    updates = updates + oldEmployee.getAssigned().toString() + " chnaged to " + employee.getAssigned().toString() + "\n";
                }

                if (photoSelected) {
                    updates = updates + "Photo Changed\n";
                }

                if (employee.getEmployeestatusId() != null && !employee.getEmployeestatusId().equals(oldEmployee.getEmployeestatusId())) {
                    updates = updates + oldEmployee.getEmployeestatusId() + " chnaged to " + employee.getEmployeestatusId() + "\n";
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
            alert.setTitle("Update Module");
            alert.setHeaderText("Nothing updates");
            alert.setContentText(updates);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Module");
            alert.setHeaderText("Are you sure you want to update the following Module ?");
            alert.setContentText(updates);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                EmployeeDao.update(employee);
                loadForm();
                fillTable(EmployeeDao.getAll());
            }
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        if (tblEmployee.getSelectionModel().getSelectedItem() != null) {
            String details = "\nName :  \t\t\t" + employee.getName()
                    + "\nGender :  \t\t\t" + employee.getGenderId().getName()
                    + "\nBirth Date :   \t\t" + employee.getDob().toString()
                    + "\nNIC No :       \t\t" + employee.getNic()
                    + "\nCivilstatus :  \t\t" + employee.getCivilstatusId().getName()
                    + "\nAddress :      \t\t" + employee.getAddress()
                    + "\nMobile No :    \t\t" + employee.getMobile()
                    + "\nLand :         \t\t" + employee.getLand()
                    + "\nEmail :        \t\t" + employee.getEmail()
                    + "\nPhoto :  \t\t\t" + (employee.getImage() == null ? "Not Selected" : "Selected")
                    + "\nDesignation :  \t\t" + employee.getDesignationId().getName()
                    + "\nAssigned Date :  \t" + employee.getAssigned().toString()
                    + "\nStatus :  \t\t\t" + employee.getEmployeestatusId().getName();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Module");
            alert.setHeaderText("Are you sure you want to delete the following Module ?");
            alert.setContentText(details);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                EmployeeDao.delete(employee);
                loadForm();
                fillTable(EmployeeDao.getAll());
            }
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Search">
    @FXML
    private void txtSearchNameKR(KeyEvent event) {
        if (txtSearchName.getText() != null) {
            if (!lock) {
                cmbSearchEmployeestatus.getSelectionModel().clearSelection();
                cmbSearchDesignation.getSelectionModel().clearSelection();
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchEmployeestatusAP(ActionEvent event) {
        if (cmbSearchEmployeestatus.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchDesignation.getSelectionModel().clearSelection();
                txtSearchName.setText("");
            }
            updateTable();
        }
    }

    @FXML
    private void cmbSearchDesignationAP(ActionEvent event) {
        if (cmbSearchDesignation.getSelectionModel().getSelectedItem() != null) {
            if (!lock) {
                cmbSearchEmployeestatus.getSelectionModel().clearSelection();
                txtSearchName.setText("");
            }
            updateTable();
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
    private void btnSearchClearAP(ActionEvent event) {
        loadTable();
    }

    private void updateTable() {
        String name = txtSearchName.getText().trim();
        boolean sname = !name.isEmpty();
        Employeestatus status = cmbSearchEmployeestatus.getSelectionModel().getSelectedItem();
        boolean sstatus = cmbSearchEmployeestatus.getSelectionModel().getSelectedIndex() != -1;
        Designation designation = cmbSearchDesignation.getSelectionModel().getSelectedItem();
        boolean sdesignation = cmbSearchDesignation.getSelectionModel().getSelectedIndex() != -1;

        if (!sname && !sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAll());
        }
        if (sname && !sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByName(name));
        }
        if (!sname && !sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByDesignation(designation));
        }
        if (!sname && sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByStatus(status));
        }
        if (sname && sstatus && !sdesignation) {
            fillTable(EmployeeDao.getAllByNameStatus(name, status));
        }
        if (sname && !sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByNameDesignation(name, designation));
        }
        if (!sname && sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByStatusDesignation(status, designation));
        }
        if (sname && sstatus && sdesignation) {
            fillTable(EmployeeDao.getAllByNameStatusDesignation(name, status, designation));
        }
    }
//</editor-fold>

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import entity.Gender;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import report.ReportView;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class EmpGenController implements Initializable {

    @FXML
    private RadioButton rdoMale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private RadioButton rdoFemale;
    @FXML
    private Button btnReportGen;

    String gen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnReportGen.setDisable(true);
        gen = null;

    }

    @FXML
    private void btnReportGenAP(ActionEvent event) throws Exception {
        try {
            HashMap hashmap = new HashMap();

            System.out.println(gen);

            hashmap.put("gender", gen);

            new ReportView("/report/employeeByGender.jasper", hashmap);

        } catch (Exception e) {
        }

    }

    @FXML
    private void rdoMaleAP(ActionEvent event) {
        btnReportGen.setDisable(false);
        gen = "Male";
    }

    @FXML
    private void rdoFemaleAP(ActionEvent event) {
        btnReportGen.setDisable(false);
        gen = "Female";
    }

}

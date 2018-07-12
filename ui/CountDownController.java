/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import static ui.LoginUIController.countDown;

/**
 * FXML Controller class
 *
 * @author SahanRid
 */
public class CountDownController implements Initializable {

    int seconds;

    @FXML
    private Label lblMessaege;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        seconds = 60;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (seconds-- > 0) {
                        lblMessaege.setText(" "
                                + ""
                                + seconds
                                + " seconds.");
                    } else {
                        timer.cancel();
                        countDown.close();
                    }
                });
            }
        }, 1000, 1000);
    }

}

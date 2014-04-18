package com.nerdinand.jeopardy.controllers;

import com.nerdinand.jeopardy.models.Frame;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Ferdinand Niedermann
 */
public class MainWindowController implements Initializable {

    @FXML
    private Label statusBar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        System.out.println(event.getText());
    }
    
    public void handleFrameButtonClick(Frame frame) {
        statusBar.setText(frame.getPoints()+" was clicked!");
    }
}

/*
 * The MIT License
 *
 * Copyright 2014 Ferdinand Niedermann.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.nerdinand.jeopardy.view;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.controllers.MainWindowController;
import com.nerdinand.jeopardy.models.Category;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Round;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Ferdinand Niedermann
 */
public class MainWindow {
    private static Round round;

    private BorderPane root;
    private MainWindowController controller;

    public MainWindow(Round round) {
        MainWindow.round = round;
    }

    public void initialise() throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader();
        BorderPane root = fXMLLoader.load(getClass().getResource(Assets.MAIN_WINDOW_FXML).openStream());
        this.root = root;
        this.controller = fXMLLoader.getController();

        Map<Frame, Button> frameButtons = initializeGridMainWindow(root);
        controller.setFrameButtons(frameButtons);
        controller.setAudio(round.getAudio());
    }

    private Button initializeFrameButton(final Frame frame) {
        Button frameButton = new Button(""+frame.getPoints());
        frameButton.setPrefHeight(150);
        frameButton.setPrefWidth(500);
        
        frameButton.setOnMouseClicked(event -> controller.handleFrameButtonClick(frame));
        
        return frameButton;
    }

    public Parent getRoot() {
        return root;
    }

    private Map<Frame, Button> initializeGridMainWindow(BorderPane root) {
        GridPane gridPain = (GridPane) root.getCenter();

        Map<Frame, Button> frameButtons = new HashMap<>();
        
        int categoryIndex = 0;
        
        for (Category category : round.getCategories()) {
            int frameIndex = 0;
            
            final Label label = new Label(category.getName());
            label.setStyle("-fx-font-size: 20pt;");
            label.setPrefHeight(150);
            label.setPrefWidth(200);
            label.setWrapText(true);

            gridPain.add(label, categoryIndex, frameIndex);
            
            for (Frame frame : category.getFrames()) {
                final Button frameButton = initializeFrameButton(frame);
                
                // somehow, stylesheets don't compile correctly on my machine, therefore we set the style manually
                setControlStyle(frameButton);
                frameButtons.put(frame, frameButton);
                gridPain.add(frameButton, categoryIndex, frameIndex + 1);
                frameIndex++;
            }
            
            categoryIndex++;
        }
        
        root.setCenter(gridPain);
        
        return frameButtons;
    }    

    public static void setControlStyle(Control control) {
        control.setStyle("-fx-font-size: 25pt;");
    }
    
    public static boolean isGameOver() {
        for (Frame frame : round.getAllFrames()) {
            if (!frame.isClosed()) {
                return false;
            }
        }
        
        return true;
    }
}

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

import com.nerdinand.jeopardy.controllers.MainWindowController;
import com.nerdinand.jeopardy.models.Category;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Round;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ferdinand Niedermann
 */
public class MainWindow {
    private final Round round;
    private BorderPane root;
    private MainWindowController controller;
    
    public MainWindow(Round round) {
        this.round = round;
    }

    public void initialize() throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader();
        BorderPane root = fXMLLoader.load(getClass().getResource("/com/nerdinand/jeopardy/fxml/MainWindow.fxml").openStream());
        this.root = root;
        this.controller = (MainWindowController) fXMLLoader.getController();
        initializeMainWindow(root);
    }

    private void initializeMainWindow(BorderPane borderPane) {
        HBox categoriesContainer = new HBox();
        categoriesContainer.setSpacing(10);

        categoriesContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        for (Category category : round.getCategories()) {
            categoriesContainer.getChildren().add(initializeCategoryLayout(category));
        }
        
        borderPane.setCenter(categoriesContainer);
    }

    private VBox initializeCategoryLayout(Category category) {
        VBox frameContainer = new VBox();
        frameContainer.setSpacing(10);
        Label categoryName = new Label(category.getName());
        frameContainer.getChildren().add(categoryName);
        
        for (Frame frame : category.getFrames()) {
            frameContainer.getChildren().add(initializeFrameLayout(frame));
        }
        
        return frameContainer;
    }
    
    private VBox initializeFrameLayout(Frame frame) {
        VBox frameLayout = new VBox();

        Button frameButton = new Button(""+frame.getPoints());
        frameButton.setPrefHeight(150);
        frameButton.setPrefWidth(500);
        frameLayout.getChildren().add(frameButton);
        
        frameButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.handleFrameButtonClick(frame);
            }
        });
        
        return frameLayout;
    }

    public Parent getRoot() {
        return root;
    }
    
}

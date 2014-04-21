/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nerdinand.jeopardy.services;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.models.Answer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

/**
 *
 * @author ferdi
 */
public class SceneFactory {

    private static SceneFactory instance;

    public static SceneFactory getInstance() {
        if (instance == null) {
            instance = new SceneFactory();
        }
        return instance;
    }

    public Scene sceneForAnswer(Answer answer) {
        switch (answer.getMediaType()) {
            case TEXT:
                return createTextScene(answer.getValue());
            // TODO add more media types
        }

        return null;
    }

    private Scene createTextScene(String value) {
        HBox root = null;

        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            root = fXMLLoader.load(getClass().getResource(Assets.ANSWER_WINDOW_FXML).openStream());
        } catch (IOException ex) {
            Logger.getLogger(SceneFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Scene(root);
    }

}

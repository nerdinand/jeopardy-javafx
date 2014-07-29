/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nerdinand.jeopardy.services;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.controllers.AnswerWindowController;
import com.nerdinand.jeopardy.controllers.listeners.FrameKeyEventListener;
import com.nerdinand.jeopardy.models.Typeable;
import com.nerdinand.jeopardy.view.AnswerQuestionWindow;
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

    public Scene sceneForTypeable(Typeable typeable, AnswerQuestionWindow answerQuestionWindow) {
        switch (typeable.getMediaType()) {
            case TEXT:
                return createTextScene(answerQuestionWindow);
            case IMAGE:
                return createImageScene(answerQuestionWindow);
            case SOUND:
                return createSoundScene(answerQuestionWindow);
        }

        return null;
    }

    private Scene createTextScene(AnswerQuestionWindow answerQuestionWindow) {
        return createScene(answerQuestionWindow, Assets.TEXT_ANSWER_WINDOW_FXML);
    }
    
    private Scene createImageScene(AnswerQuestionWindow answerQuestionWindow) {
        return createScene(answerQuestionWindow, Assets.IMAGE_ANSWER_WINDOW_FXML);
    }

    private Scene createSoundScene(AnswerQuestionWindow answerQuestionWindow) {
        return createScene(answerQuestionWindow, Assets.SOUND_ANSWER_WINDOW_FXML);
    }
        
    private Scene createScene(AnswerQuestionWindow answerQuestionWindow, String fxmlAssetPath) {
        HBox root = null;
        
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            root = fXMLLoader.load(getClass().getResource(fxmlAssetPath).openStream());
            final AnswerWindowController controller = (AnswerWindowController) fXMLLoader.getController();

            controller.setFrameKeyEventListener(new FrameKeyEventListener(answerQuestionWindow.getFrame()));

            answerQuestionWindow.setController(controller);
            
        } catch (IOException ex) {
            Logger.getLogger(SceneFactory.class.getName()).log(Level.SEVERE, "Couldn't load or initialize FXML file from source "+fxmlAssetPath, ex);
        }
        
        return new Scene(root);
    }

}

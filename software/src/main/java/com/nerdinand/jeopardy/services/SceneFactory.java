/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nerdinand.jeopardy.services;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.controllers.AnswerWindowController;
import com.nerdinand.jeopardy.controllers.listeners.FrameKeyEventListener;
import com.nerdinand.jeopardy.models.Answer;
import com.nerdinand.jeopardy.view.AnswerWindow;
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

    public Scene sceneForAnswer(Answer answer, AnswerWindow answerWindow) {
        switch (answer.getMediaType()) {
            case TEXT:
                return createTextScene(answerWindow);
            case IMAGE:
                return createImageScene(answerWindow);
            case SOUND:
                return createSoundScene(answerWindow);
        }

        return null;
    }

    private Scene createTextScene(AnswerWindow answerWindow) {
        return createScene(answerWindow, Assets.TEXT_ANSWER_WINDOW_FXML);
    }
    
    private Scene createImageScene(AnswerWindow answerWindow) {
        return createScene(answerWindow, Assets.IMAGE_ANSWER_WINDOW_FXML);
    }

    private Scene createSoundScene(AnswerWindow answerWindow) {
        return createScene(answerWindow, Assets.SOUND_ANSWER_WINDOW_FXML);
    }
        
    private Scene createScene(AnswerWindow answerWindow, String fxmlAssetPath) {
        HBox root = null;
        
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            root = fXMLLoader.load(getClass().getResource(fxmlAssetPath).openStream());
            final AnswerWindowController controller = (AnswerWindowController) fXMLLoader.getController();

            controller.setFrameKeyEventListener(new FrameKeyEventListener(answerWindow.getFrame()));

            answerWindow.setController(controller);
            
        } catch (IOException ex) {
            Logger.getLogger(SceneFactory.class.getName()).log(Level.SEVERE, "Couldn't load or initialize FXML file from source "+fxmlAssetPath, ex);
        }
        
        return new Scene(root);
    }

}

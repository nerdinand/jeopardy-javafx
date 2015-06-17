/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nerdinand.jeopardy.services;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.controllers.AnswerWindowController;
import com.nerdinand.jeopardy.controllers.QuestionWindowController;
import com.nerdinand.jeopardy.controllers.listeners.FrameKeyEventListener;
import com.nerdinand.jeopardy.models.Answer;
import com.nerdinand.jeopardy.models.Question;
import com.nerdinand.jeopardy.view.AnswerWindow;
import com.nerdinand.jeopardy.view.QuestionWindow;
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
                return createTextAnswerScene(answerWindow);
            case IMAGE:
                return createImageAnswerScene(answerWindow);
            case SOUND:
                return createSoundAnswerScene(answerWindow);
        }

        return null;
    }

    private Scene createTextAnswerScene(AnswerWindow answerWindow) {
        return createAnswerScene(answerWindow, Assets.TEXT_ANSWER_WINDOW_FXML);
    }

    private Scene createImageAnswerScene(AnswerWindow answerWindow) {
        return createAnswerScene(answerWindow, Assets.IMAGE_ANSWER_WINDOW_FXML);
    }

    private Scene createSoundAnswerScene(AnswerWindow answerWindow) {
        return createAnswerScene(answerWindow, Assets.SOUND_ANSWER_WINDOW_FXML);
    }

    public Scene sceneForQuestion(Question question, QuestionWindow questionWindow) {
        switch (question.getMediaType()) {
            case TEXT:
                return createTextQuestionScene(questionWindow);
            case IMAGE:
                return createImageQuestionScene(questionWindow);
            case SOUND:
                return createSoundQuestionScene(questionWindow);
        }

        return null;
    }

    private Scene createTextQuestionScene(QuestionWindow questionWindow) {
        return createQuestionScene(questionWindow, Assets.TEXT_QUESTION_WINDOW_FXML);
    }

    private Scene createImageQuestionScene(QuestionWindow questionWindow) {
        return createQuestionScene(questionWindow, Assets.IMAGE_QUESTION_WINDOW_FXML);
    }

    private Scene createSoundQuestionScene(QuestionWindow questionWindow) {
        return createQuestionScene(questionWindow, Assets.SOUND_QUESTION_WINDOW_FXML);
    }

    private Scene createAnswerScene(AnswerWindow answerWindow, String fxmlAssetPath) {
        HBox root = null;

        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            root = fXMLLoader.load(getClass().getResource(fxmlAssetPath).openStream());
            final AnswerWindowController controller = fXMLLoader.getController();

            controller.setFrameKeyEventListener(new FrameKeyEventListener(answerWindow.getFrame()));

            answerWindow.setController(controller);

        } catch (IOException ex) {
            Logger.getLogger(SceneFactory.class.getName()).log(Level.SEVERE, "Couldn't load or initialise FXML file from source " + fxmlAssetPath, ex);
        }

        return new Scene(root);
    }

    private Scene createQuestionScene(QuestionWindow questionWindow, String fxmlAssetPath) {
        HBox root = null;

        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            root = fXMLLoader.load(getClass().getResource(fxmlAssetPath).openStream());
            final QuestionWindowController controller = fXMLLoader.getController();

            questionWindow.setController(controller);

        } catch (IOException ex) {
            Logger.getLogger(SceneFactory.class.getName()).log(Level.SEVERE, "Couldn't load or initialise FXML file from source " + fxmlAssetPath, ex);
        }

        return new Scene(root);
    }

}

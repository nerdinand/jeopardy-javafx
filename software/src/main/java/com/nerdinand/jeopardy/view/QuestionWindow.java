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

import com.nerdinand.jeopardy.controllers.QuestionWindowController;
import com.nerdinand.jeopardy.interfaces.FrameAnsweredListener;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Question;
import com.nerdinand.jeopardy.services.SceneFactory;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Ferdinand Niedermann
 */
public class QuestionWindow {

    private final Question question;
    private QuestionWindowController controller;
    private final Frame frame;
    private AudioClip questionSound;

    public QuestionWindow(Frame frame, FrameAnsweredListener frameAnsweredListener) {
        this.frame = frame;
        this.question = frame.getQuestion();
    }

    public Scene initialize() {
        Scene scene = SceneFactory.getInstance().sceneForQuestion(getQuestion(), this);

        final File mediaPath = getQuestion().getMediaPath();

        switch (getQuestion().getMediaType()) {
            case TEXT:
                setTextFromValue(getQuestion().getValue());
                break;
            case IMAGE:
                setImageFromFile(mediaPath);
                break;
            case SOUND:
                setSoundFromFile(mediaPath);
                break;
        }

        return scene;
    }

    public QuestionWindowController getController() {
        return controller;
    }

    private void setTextFromValue(String value) {
        getController().showText(value);
    }

    private void setImageFromFile(File mediaPath) {
        final String uri = mediaPath.toURI().toString();
        try {
            Image answerImage = new Image(uri);
            getController().showImage(answerImage);
        } catch (Exception ex) {
            Logger.getLogger(AnswerWindow.class.getName()).log(Level.SEVERE, "Image file " + uri + " could not be read.", ex);
        }
    }
    
    private void setSoundFromFile(File mediaPath) {
        final String uri = mediaPath.toURI().toString();
        try {
            AudioClip questionSound = new AudioClip(uri);
            getController().setSound(questionSound);
        } catch (Exception ex) {
            Logger.getLogger(AnswerWindow.class.getName()).log(Level.SEVERE, "Sound file " + uri + " could not be read.", ex);
        }
    }

    public Frame getFrame() {
        return frame;
    }

    private Question getQuestion() {
        return question;
    }

    public void setController(QuestionWindowController controller) {
        this.controller = controller;
    }
}

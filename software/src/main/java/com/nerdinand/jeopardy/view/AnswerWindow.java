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

import com.nerdinand.jeopardy.controllers.AnswerWindowController;
import com.nerdinand.jeopardy.models.Answer;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.services.SceneFactory;
import com.nerdinand.jeopardy.services.Util;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 *
 * @author Ferdinand Niedermann
 */
public class AnswerWindow {

    private final Answer answer;
    private AnswerWindowController controller;
    private final Frame frame;

    public AnswerWindow(Frame frame) {
        this.frame = frame;
        this.answer = frame.getAnswer();
    }

    public Scene initialize() {
        Scene scene = SceneFactory.getInstance().sceneForAnswer(getAnswer(), this);

        final File mediaPath = getAnswer().getMediaPath();

        switch (getAnswer().getMediaType()) {
            case TEXT:
                setTextFromFile(mediaPath);
                break;
            case IMAGE:
                setImageFromFile(mediaPath);
                break;
        }

        return scene;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setController(AnswerWindowController answerWindowController) {
        this.controller = answerWindowController;
    }

    public AnswerWindowController getController() {
        return controller;
    }

    private void setTextFromFile(File mediaPath) {
        try {
            String answerString = Util.readUTF8File(mediaPath);
            getController().showText(answerString);
        } catch (IOException ex) {
            Logger.getLogger(AnswerWindow.class.getName()).log(Level.SEVERE, "Text file " + mediaPath + " could not be read.", ex);
        }
    }

    private void setImageFromFile(File mediaPath) {
        final String uri = mediaPath.toURI().toString();
        try {
            Image answerImage = new Image(uri);
            getController().showImage(answerImage);
        } catch(Exception ex) {
            Logger.getLogger(AnswerWindow.class.getName()).log(Level.SEVERE, "Image file " + uri + " could not be read.", ex);
        }
    }

    public Frame getFrame() {
        return frame;
    }

}

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
import com.nerdinand.jeopardy.interfaces.FrameAnsweredListener;
import com.nerdinand.jeopardy.models.*;
import com.nerdinand.jeopardy.services.SceneFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ChoiceDialog;

/**
 *
 * @author Ferdinand Niedermann
 */
public class AnswerWindow {

    private final Answer answer;
    private AnswerWindowController controller;
    private final Frame frame;
    private final FrameAnsweredListener frameAnsweredListener;

    public AnswerWindow(Frame frame, FrameAnsweredListener frameAnsweredListener) {
        this.frame = frame;
        this.answer = frame.getAnswer();
        this.frameAnsweredListener = frameAnsweredListener;

        if (getFrame().hasDoubleJeopardy()) {
            handleDoubleJeopardy();
        }
    }

    public Scene initialise() {
        Scene scene = SceneFactory.getInstance().sceneForAnswer(getAnswer(), this);

        final File mediaPath = getAnswer().getMediaPath();

        switch (getAnswer().getMediaType()) {
            case TEXT:
                setTextFromValue(getAnswer().getValue());
                break;
            case IMAGE:
                setImageFromFile(mediaPath);
                break;
            case SOUND:
                break;
        }

        return scene;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setController(AnswerWindowController answerWindowController) {
        this.controller = answerWindowController;
        getController().setFrameAnsweredListener(frameAnsweredListener);
    }

    public AnswerWindowController getController() {
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

    public Frame getFrame() {
        return frame;
    }

    public FrameAnsweredListener getUpdateable() {
        return frameAnsweredListener;
    }

    private void handleDoubleJeopardy() {
        Player player = askForPlayer();
        Integer wager = askForWager(player);

        Players.setDoubleJeopardyPlayer(player);
        getFrame().setDoubleJeopardyWager(wager);
    }

    private Player askForPlayer() {
        Optional<Player> player;
        
        do {
            ChoiceDialog dialog = new ChoiceDialog<>(Players.getPlayerList().get(0), Players.getPlayerList());
            dialog.setTitle("Double Jeopardy!");
            dialog.setContentText("You have encountered a Double Jeopardy!\nWhich player are you?");
            player = dialog.showAndWait();
        } while (!player.isPresent());
                
        return player.get();
    }

    private Integer askForWager(Player player) {
        final List<Integer> doubleJeopardyChoices = getDoubleJeopardyChoices(getFrame());
        Optional<Integer> wager;
        do {
            ChoiceDialog dialog = new ChoiceDialog<>(doubleJeopardyChoices.get(0), doubleJeopardyChoices);
            dialog.setTitle("Double Jeopardy!");
            dialog.setContentText("Player " + player + "!\nHow many points do you want to set?");
            wager = dialog.showAndWait();
        } while (!wager.isPresent());
        return wager.get();
    }

    private List<Integer> getDoubleJeopardyChoices(Frame frame) {
        List<Integer> choices = new ArrayList<>();

        int amount = 100;

        do {
            choices.add(amount);
            amount += 100;
        } while (amount <= frame.getPoints() * 2);

        return choices;
    }

}

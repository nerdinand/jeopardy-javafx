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
package com.nerdinand.jeopardy.controllers;

import com.nerdinand.jeopardy.Jeopardy;
import com.nerdinand.jeopardy.controllers.listeners.FrameKeyEventListener;
import com.nerdinand.jeopardy.interfaces.FrameAnsweredListener;
import com.nerdinand.jeopardy.interfaces.FrameAnsweredListener.FrameState;
import com.nerdinand.jeopardy.models.*;
import com.nerdinand.jeopardy.services.Keymap;
import com.nerdinand.jeopardy.services.ScoreFactory;
import com.nerdinand.jeopardy.services.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;

/**
 *
 * @author Ferdinand Niedermann
 */
public class AnswerWindowController implements Initializable {

    @FXML
    private HBox hBox;

    @FXML
    private Label textLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Button playButton;

    private FrameKeyEventListener frameKeyEventListener;
    private FrameAnsweredListener frameAnsweredListener;

    private Audio audio;

    private Player answeredPlayer;

    private AudioClip answerSound;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rearmPlayers();
    }

    public void showText(String text) {
        textLabel.setText(text);
    }

    public void showImage(Image image) {
        imageView.setImage(image);
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    private static Players getPlayers() {
        return Jeopardy.getPlayers();
    }

    @FXML
    private void onPlayButtonAction() {
        if (getFrame().getAnswer().getMediaType() == MediaType.SOUND) {
            if (answerSound == null) {
                final String uri = getFrame().getAnswer().getMediaPath().toURI().toString();
                this.answerSound = new AudioClip(uri);
            }

            if (answerSound.isPlaying()) {
                answerSound.stop();
            } else {
                answerSound.play();
            }
        }
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        final Keymap.Action action = Keymap.getInstance().getAction(event);

        if (action == Keymap.Action.PLAYER) {
            handlePlayerKeyPressed(event);
        } else if (action == Keymap.Action.CANCEL) {
            cancelIfNoDoubleJeopardy();
        } else if (event.getCode() == KeyCode.ENTER) {
            handleFrame();
        }
    }

    private void cancelIfNoDoubleJeopardy() {
        if (!isDoubleJeopardyFrame()) {
            cancelFrame();
        }
    }

    private void handlePlayerKeyPressed(KeyEvent event) {
        playBuzzerSound();

        handlePlayerAndDisarm(getPlayers().getArmedPlayerForKey(event.getCode()));

        if (isDoubleJeopardyFrame()) {
            if (isDoubleJeopardyPlayer(answeredPlayer)) {
                disarmPlayers();
            } else {
                reopenAnswer();
            }
        }
    }

    private void rearmPlayers() {
        getPlayers().setPlayersArmed(true);
    }

    private void disarmPlayers() {
        getPlayers().setPlayersArmed(false);
    }

    private boolean isDoubleJeopardyPlayer(Player player) {
        return Players.getDoubleJeopardyPlayer() == player;
    }

    private void handleFrame() {
        if (answeredPlayer != null) {
            if (isDoubleJeopardyFrame()) {
                handleDoubleJeopardyFrame(answeredPlayer);
            } else {
                handleNormalFrame(answeredPlayer);
            }
        }
    }

    private boolean isDoubleJeopardyFrame() {
        return getFrame().hasDoubleJeopardy();
    }

    private void handlePlayerAndDisarm(Player player) {
        handlePlayer(player);
        disarmPlayers();
    }

    private void handlePlayer(Player player) {
        if (player != null) {
            answeredPlayer = player;
            changeBackgroundColor(player.getColor());
        }
    }

    private void playBuzzerSound() {
        audio.buzzer();
    }

    private void changeBackgroundColor(Color color) {
        hBox.getScene().getRoot().setStyle("-fx-background-color: " + Util.toRGBCode(color) + ";");
    }

    private void handleNormalFrame(Player player) {
        ButtonType answer = frameKeyEventListener.onPlayerKeyPressed(player);
        Frame frame = getFrame();

        switch (answer.getText()) {
            case FrameKeyEventListener.YES:
                // player has given the correct solution
                audio.yes();
                ScoreFactory.getInstance().createScore(player, frame, frame.getPoints());
                closeWindow();
                frameAnsweredListener.frameAnswered(frame, FrameState.ANSWERED_CORRECT);
                break;
            case FrameKeyEventListener.YOU_TRIED:
                audio.youTried();
                ScoreFactory.getInstance().createScore(player, frame, frame.getYouTriedPoints());
                reopenAnswer();
                break;
            case FrameKeyEventListener.NO:
                // player has given the wrong solution
                audio.no();
                ScoreFactory.getInstance().createScore(player, frame, frame.getWrongPoints());
                frameAnsweredListener.frameAnswered(frame, FrameState.ANSWERED_WRONG);
                reopenAnswer();
                break;
            default:
                reopenAnswer();
                break;
        }
    }

    private void reopenAnswer() {
        rearmPlayers();
        changeBackgroundColor(Color.WHITE);
    }

    private Frame getFrame() {
            return frameKeyEventListener.getFrame();
    }

    public void setFrameKeyEventListener(FrameKeyEventListener frameKeyEventListener) {
        this.frameKeyEventListener = frameKeyEventListener;
    }

    public void setFrameAnsweredListener(FrameAnsweredListener frameAnsweredListener) {
        this.frameAnsweredListener = frameAnsweredListener;
    }

    private void handleDoubleJeopardyFrame(Player player) {
        // check if the player who wants to answer is the double jeopardy player 
        // (otherwise ignore the buzzer press)
        if (isDoubleJeopardyPlayer(player)) {
            ButtonType answer = frameKeyEventListener.onPlayerKeyPressed(player);
            Frame frame = getFrame();

            if (answer.getText().equals(FrameKeyEventListener.YES)) { // player has given the correct solution
                audio.yes();
                ScoreFactory.getInstance().createScore(player, frame, frame.getDoubleJeopardyWager());
                closeWindow();

                frameAnsweredListener.frameAnswered(frame, FrameState.ANSWERED_CORRECT);
            } else if (answer.getText().equals(FrameKeyEventListener.NO)) { // player has given the wrong solution
                audio.no();
                ScoreFactory.getInstance().createScore(player, frame, -frame.getDoubleJeopardyWager());
                closeWindow();

                frameAnsweredListener.frameAnswered(frame, FrameState.ANSWERED_WRONG);
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) hBox.getScene().getWindow();
        stage.close();

        if (answerSound != null) {
            answerSound.stop();
        }

        audio.stopBackgroundMusic();
    }

    private void cancelFrame() {
        Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION, "Really cancel this frame?");
        alertDialog.setTitle("Cancel");
        alertDialog.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(
                response -> {
                    getFrame().setClosed(true);
                    frameAnsweredListener.frameAnswered(getFrame(), FrameState.CANCELED);
                    closeWindow();
                }
            );
    }

}

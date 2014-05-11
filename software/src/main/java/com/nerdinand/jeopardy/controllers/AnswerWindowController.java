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
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Player;
import com.nerdinand.jeopardy.models.Players;
import com.nerdinand.jeopardy.services.ScoreFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

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

    private FrameKeyEventListener frameKeyEventListener;
    private FrameAnsweredListener frameAnsweredListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getPlayers().setPlayersArmed(true);
    }

    public void showText(String text) {
        textLabel.setText(text);
    }

    public void showImage(Image image) {
        imageView.setImage(image);
    }

    private static Players getPlayers() {
        return Jeopardy.getPlayers();
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        Player player = getPlayers().getArmedPlayerForKey(event.getCode());

        if (player != null) {
            if (getFrame().hasDoubleJeopardy()) {
                handleDoubleJeopardyFrame(player);
            } else {
                handleNormalFrame(player);
            }
        }
    }

    private void handleNormalFrame(Player player) {
        Action action = frameKeyEventListener.onPlayerKeyPressed(player);
        Frame frame = getFrame();

        if (action == Dialog.Actions.YES) { // player has given the correct solution
            ScoreFactory.getInstance().createScore(player, frame, frame.getPoints());
            Stage stage = (Stage) hBox.getScene().getWindow();
            stage.close();

            frameAnsweredListener.frameAnswered(frame, true);
        } else if (action == Dialog.Actions.NO) { // player has given the wrong solution
            ScoreFactory.getInstance().createScore(player, frame, -frame.getPoints());

            frameAnsweredListener.frameAnswered(frame, false);
        }
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
        if (Players.getDoubleJeopardyPlayer() == player) {
            Action action = frameKeyEventListener.onPlayerKeyPressed(player);
            Frame frame = getFrame();

            if (action == Dialog.Actions.YES) { // player has given the correct solution
                ScoreFactory.getInstance().createScore(player, frame, frame.getDoubleJeopardyWager());
                Stage stage = (Stage) hBox.getScene().getWindow();
                stage.close();

                frameAnsweredListener.frameAnswered(frame, true);
            } else if (action == Dialog.Actions.NO) { // player has given the wrong solution
                ScoreFactory.getInstance().createScore(player, frame, -frame.getDoubleJeopardyWager());
                Stage stage = (Stage) hBox.getScene().getWindow();
                stage.close();
                
                frameAnsweredListener.frameAnswered(frame, false);
            }
        }
    }

}

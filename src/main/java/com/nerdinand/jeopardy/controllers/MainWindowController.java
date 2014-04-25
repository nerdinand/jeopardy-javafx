package com.nerdinand.jeopardy.controllers;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.Jeopardy;
import com.nerdinand.jeopardy.controllers.listeners.FrameKeyEventListener;
import com.nerdinand.jeopardy.controllers.listeners.PlayerKeyEventListener;
import com.nerdinand.jeopardy.controllers.listeners.SetPlayerNameKeyEventListener;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Player;
import com.nerdinand.jeopardy.models.Players;
import com.nerdinand.jeopardy.services.SceneFactory;
import com.nerdinand.jeopardy.view.AnswerWindow;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Ferdinand Niedermann
 */
public class MainWindowController implements Initializable {

    @FXML
    private Label statusBarLabel;
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label player3Label;
    @FXML
    private Label player4Label;

    private Label[] playerLabels;
    private PlayerKeyEventListener playerKeyEventListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerLabels = new Label[]{player1Label, player2Label, player3Label, player4Label};

        initializeScoreDisplay();

        setPlayerKeyEventListener(new SetPlayerNameKeyEventListener());
        getPlayers().setPlayersArmed(true);
    }

    private static Players getPlayers() {
        return Jeopardy.getPlayers();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        Player player = getPlayers().getArmedPlayerForKey(event.getCode());

        if (player != null) {
            System.out.println(player.getName() +" pressed.");

            playerKeyEventListener.onPlayerKeyPressed(player);
            updatePlayerStatus(player);
        }
    }

    public void handleFrameButtonClick(Frame frame) {
        getPlayers().setPlayersArmed(true);

        setPlayerKeyEventListener(new FrameKeyEventListener(frame));

        playJeopardyMusic();

        openAnswerWindow(frame);
    }

    private void openAnswerWindow(Frame frame) {
        AnswerWindow answerWindow = new AnswerWindow(frame.getAnswer());

        Scene scene = answerWindow.initialize();
        
        if (scene != null) {
            Stage stage = new Stage();

            stage.setTitle(frame.toString());
            stage.setScene(scene);
            stage.show();
            
            // we need to do the following in order to receive keyboard events in the new window...
            stage.getScene().getRoot().setFocusTraversable(true);
            stage.getScene().getRoot().requestFocus();
        }
    }

    private void playJeopardyMusic() {
        if (Assets.jeopardyMusic != null) {
            Assets.jeopardyMusic.stop();
            Assets.jeopardyMusic.play();
        }
    }

    private void showStatus(String string) {
        statusBarLabel.setText(string);
    }

    private void updatePlayerStatus(Player player) {
        Label playerLabel = playerLabels[player.getId() - 1];
        playerLabel.setTextFill(player.getColor());
        playerLabel.setText(player.getName() + ": " + player.getPoints());
    }

    private void initializeScoreDisplay() {
        for (Player player : getPlayers().getPlayerList()) {
            updatePlayerStatus(player);
        }
    }

    private void setPlayerKeyEventListener(PlayerKeyEventListener playerKeyEventListener) {
        this.playerKeyEventListener = playerKeyEventListener;
    }
}

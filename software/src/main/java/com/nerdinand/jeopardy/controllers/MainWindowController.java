package com.nerdinand.jeopardy.controllers;

import com.nerdinand.jeopardy.Assets;
import com.nerdinand.jeopardy.Jeopardy;
import com.nerdinand.jeopardy.controllers.listeners.PlayerKeyEventListener;
import com.nerdinand.jeopardy.controllers.listeners.SetPlayerNameKeyEventListener;
import com.nerdinand.jeopardy.interfaces.FrameAnsweredListener;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.MediaType;
import com.nerdinand.jeopardy.models.Player;
import com.nerdinand.jeopardy.models.Players;
import com.nerdinand.jeopardy.models.Score;
import com.nerdinand.jeopardy.services.Util;
import com.nerdinand.jeopardy.view.AnswerWindow;
import com.nerdinand.jeopardy.view.MainWindow;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Ferdinand Niedermann
 */
public class MainWindowController implements Initializable, FrameAnsweredListener {

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
    private Map<Frame, Button> frameButtons;

    @FXML
    private GridPane gridPane;

    private AudioClip answerSound;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerLabels = new Label[]{player1Label, player2Label, player3Label, player4Label};

        updatePlayerStatuses();

        setPlayerKeyEventListener(new SetPlayerNameKeyEventListener());
        getPlayers().setPlayersArmed(true);

        MainWindow.setControlStyle(statusBarLabel);

        for (Label label : playerLabels) {
            MainWindow.setControlStyle(label);
        }
    }

    private static Players getPlayers() {
        return Jeopardy.getPlayers();
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        Player player = getPlayers().getArmedPlayerForKey(event.getCode());

        if (player != null) {
            playerKeyEventListener.onPlayerKeyPressed(player);
            updatePlayerStatus(player);
        }
    }

    public void handleFrameButtonClick(Frame frame) {
        if (frame.getAnswer().getMediaType() != MediaType.SOUND) {
            playJeopardyMusic();
        }

        openAnswerWindow(frame);
    }

    private void openAnswerWindow(Frame frame) {
        AnswerWindow answerWindow = new AnswerWindow(frame, this);

        Button buttonForFrame = getButtonForFrame(frame);
        buttonForFrame.setDisable(true);

        Scene scene = answerWindow.initialize();

        if (scene != null) {
            Stage stage = new Stage();
            stage.setOnCloseRequest(createOnCloseHandler());
            stage.setTitle(frame.toString());
            stage.setScene(scene);
            stage.show();

            if (frame.getAnswer().getMediaType() == MediaType.SOUND) {
                final String uri = frame.getAnswer().getMediaPath().toURI().toString();
                this.answerSound = new AudioClip(uri);
                this.answerSound.play();
            }

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

    private void updatePlayerStatuses() {
        for (Player player : getPlayers().getPlayerList()) {
            updatePlayerStatus(player);
        }
    }

    private void setPlayerKeyEventListener(PlayerKeyEventListener playerKeyEventListener) {
        this.playerKeyEventListener = playerKeyEventListener;
    }

    @Override
    public void frameAnswered(Frame frame, boolean correct) {
        updatePlayerStatuses();

        if (correct) {
            frame.setClosed(true);

            final String color = Util.toRGBCode(frame.getLastScore().getPlayer().getColor());
            Button button = getButtonForFrame(frame);
            button.setStyle("-fx-background-color: " + color + ";");
        }

        if (MainWindow.isGameOver()) {
            showGameOverWindow();
        }
    }

    public void setFrameButtons(Map<Frame, Button> frameButtons) {
        this.frameButtons = frameButtons;
    }

    private Button getButtonForFrame(Frame frame) {
        return frameButtons.get(frame);
    }

    private void showGameOverWindow() {
        Dialogs.create()
                .owner(null)
                .title("Game over!")
                .masthead(null)
                .message("The game is over and the winner is " + Players.getWinner() + "!")
                .showInformation();
    }

    private EventHandler<WindowEvent> createOnCloseHandler() {
        return (WindowEvent event) -> {
            stopSounds();
        };
    }

    private void stopSounds() {
        if (answerSound != null) {
            answerSound.stop();
        }

        stopJeopardySound();
    }

    @FXML
    private void onPlayerLabel1Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(0));
    }

    @FXML
    private void onPlayerLabel2Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(1));
    }

    @FXML
    private void onPlayerLabel3Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(2));
    }

    @FXML
    private void onPlayerLabel4Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(3));
    }

    private void editPlayerScore(Player player) {
        final int oldScore = player.getPoints();
        String newScore = Dialogs.create()
                .owner(null)
                .title(player.getName())
                .masthead(null)
                .message("Edit " + player.getName() + "'s score:")
                .showTextInput("" + oldScore);

        player.addScore(new Score(player, null, Integer.parseInt(newScore) - oldScore));

        updatePlayerStatuses();
    }

    public static void stopJeopardySound() {
        Assets.jeopardyMusic.stop();
    }
}

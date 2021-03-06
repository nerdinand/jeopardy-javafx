package com.nerdinand.jeopardy.controllers;

import com.nerdinand.jeopardy.Jeopardy;
import com.nerdinand.jeopardy.controllers.listeners.PlayerKeyEventListener;
import com.nerdinand.jeopardy.controllers.listeners.SetPlayerNameKeyEventListener;
import com.nerdinand.jeopardy.interfaces.FrameAnsweredListener;
import com.nerdinand.jeopardy.models.*;
import com.nerdinand.jeopardy.services.Util;
import com.nerdinand.jeopardy.view.AnswerWindow;
import com.nerdinand.jeopardy.view.MainWindow;
import com.nerdinand.jeopardy.view.QuestionWindow;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Ferdinand Niedermann
 */
public class MainWindowController implements Initializable, FrameAnsweredListener {

    @FXML
    private Label player1NameLabel;
    @FXML
    private Label player2NameLabel;
    @FXML
    private Label player3NameLabel;
    @FXML
    private Label player4NameLabel;

    @FXML
    private Label player1ScoreLabel;
    @FXML
    private Label player2ScoreLabel;
    @FXML
    private Label player3ScoreLabel;
    @FXML
    private Label player4ScoreLabel;

    private Label[] playerNameLabels;
    private Label[] playerScoreLabels;
    private PlayerKeyEventListener playerKeyEventListener;
    private Map<Frame, Button> frameButtons;

    @FXML
    private GridPane gridPane;

    // if the media type is audio, this will hold the audio clip being played
    private AudioClip answerSound;

    private Audio audio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerNameLabels = new Label[]{player1NameLabel, player2NameLabel, player3NameLabel, player4NameLabel};
        playerScoreLabels = new Label[]{player1ScoreLabel, player2ScoreLabel, player3ScoreLabel, player4ScoreLabel};

        updatePlayerStatuses();

        setPlayerKeyEventListener(new SetPlayerNameKeyEventListener());
        getPlayers().setPlayersArmed(true);

        for (Label label : playerNameLabels) {
            MainWindow.setControlStyle(label);
        }
        for (Label label : playerScoreLabels) {
            MainWindow.setControlStyle(label);
        }
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
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

        openAnswerWindow(audio, frame);
    }

    private void openAnswerWindow(Audio audio, Frame frame) {
        AnswerWindow answerWindow = new AnswerWindow(frame, this);

        Button buttonForFrame = getButtonForFrame(frame);
        buttonForFrame.setDisable(true);

        Scene scene = answerWindow.initialise();

        answerWindow.getController().setAudio(audio);

        if (scene != null) {
            Stage stage = new Stage();
            stage.setOnCloseRequest(createOnCloseHandler());
            stage.setTitle(frame.toString());
            stage.setScene(scene);
            stage.show();

            // we need to do the following in order to receive keyboard events in the new window...
            stage.getScene().getRoot().setFocusTraversable(true);
            stage.getScene().getRoot().requestFocus();
        }
    }

    private void playJeopardyMusic() {
        audio.playBackgroundMusic();
    }

    private void updatePlayerStatus(Player player) {
        Label playerNameLabel = playerNameLabels[player.getId() - 1];
        playerNameLabel.setTextFill(player.getColor());
        playerNameLabel.setText(player.getName());

        Label playerScoreLabel = playerScoreLabels[player.getId() - 1];
        playerScoreLabel.setTextFill(player.getColor());
        playerScoreLabel
                .setText("" + player.getPoints());
    }

    private void updatePlayerStatuses() {
        Players.getPlayerList().forEach(this::updatePlayerStatus);
    }

    private void setPlayerKeyEventListener(PlayerKeyEventListener playerKeyEventListener) {
        this.playerKeyEventListener = playerKeyEventListener;
    }

    @Override
    public void frameAnswered(Frame frame, FrameState frameState) {
        updatePlayerStatuses();

        if (frame.hasDoubleJeopardy()) {
            frame.setClosed(true);
        }
        
        if (frameState == FrameState.ANSWERED_CORRECT) {
            frame.setClosed(true);

            final String color = Util.toRGBCode(frame.getLastScore().getPlayer().getColor());
            Button button = getButtonForFrame(frame);
            button.setStyle(button.getStyle() + " -fx-background-color: " + color + ";");
        }
        
        if (frameState != FrameState.ANSWERED_WRONG) {
            showQuestion(frame);
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
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION, "The game is over and the winner is " + Players.getWinner() + "!");
        alertDialog.setTitle("Game over!");
        alertDialog.showAndWait();
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
    private void onPlayerScoreLabel1Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(0));
    }

    @FXML
    private void onPlayerScoreLabel2Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(1));
    }

    @FXML
    private void onPlayerScoreLabel3Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(2));
    }

    @FXML
    private void onPlayerScoreLabel4Click(MouseEvent e) {
        editPlayerScore(Players.getPlayerList().get(3));
    }

    private void editPlayerScore(Player player) {
        final int oldScore = player.getPoints();
        
        TextInputDialog dialog = new TextInputDialog("" + oldScore);
        dialog.setTitle("Name");
        dialog.setContentText("Edit " + player.getName() + "'s score:");
        Optional<String> newScore = dialog.showAndWait();

        if (newScore.isPresent()) {
            player.addScore(new Score(player, null, Integer.parseInt(newScore.get()) - oldScore));
        }
        
        updatePlayerStatuses();
    }

    public void stopJeopardySound() {
        audio.stopBackgroundMusic();
    }

    private void showQuestion(Frame frame) {
        QuestionWindow questionWindow = new QuestionWindow(frame, this);

        Scene scene = questionWindow.initialize();

        questionWindow.getController().setAudio(audio);

        if (scene != null) {
            Stage stage = new Stage();
            stage.setOnCloseRequest(createOnCloseHandler());
            stage.setTitle(frame.toString());
            stage.setScene(scene);
            stage.show();
            
            // we need to do the following in order to receive keyboard events in the new window...
            stage.getScene().getRoot().setFocusTraversable(true);
            stage.getScene().getRoot().requestFocus();
        }
    }
}

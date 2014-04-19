package com.nerdinand.jeopardy.controllers;

import com.nerdinand.jeopardy.Jeopardy;
import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Player;
import com.nerdinand.jeopardy.models.Players;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import org.controlsfx.dialog.Dialogs;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerLabels = new Label[]{player1Label, player2Label, player3Label, player4Label};

        initializeScoreDisplay();
        
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
            player.setArmed(false);
            
            String name = Dialogs.create()
                    .owner(null)
                    .title("Name")
                    .masthead(null)
                    .message("Player " + player.getId() + ", please give us your name!")
                    .showTextInput(null);

            player.setName(name);
            
            updatePlayerStatus(player);
        }
    }

    public void handleFrameButtonClick(Frame frame) {
        showStatus(frame.getPoints() + " was clicked!");
    }

    private void showStatus(String string) {
        statusBarLabel.setText(string);
    }

    private void updatePlayerStatus(Player player) {
        Label playerLabel = playerLabels[player.getId() - 1];
        playerLabel.setTextFill(player.getColor());
        playerLabel.setText(player.getName() + ": " + player.getScore());
    }

    private void initializeScoreDisplay() {
        for (Player player : getPlayers().getPlayerList()) {
            updatePlayerStatus(player);
        }
    }
}

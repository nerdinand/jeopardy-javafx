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
package com.nerdinand.jeopardy;

import com.nerdinand.jeopardy.models.Players;
import com.nerdinand.jeopardy.models.Round;
import com.nerdinand.jeopardy.view.MainWindow;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Jeopardy extends Application {

    private ResourceBundle bundle;

    private static String roundPath;
    private static Round round;
    private static Players players;

    @Override
    public void start(Stage stage) throws Exception {

        bundle = ResourceBundle
                .getBundle("i18n.locale", new Locale("en"));

        MainWindow mainWindow = new MainWindow(round);
        mainWindow.initialise();
        stage.setTitle(bundle.getString("app_name"));
        final Parent root = mainWindow.getRoot();
        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION, "Really quit?");
            alertDialog.setTitle("Quitting...");
            alertDialog.showAndWait()
                .filter(response -> (response == ButtonType.NO || response == ButtonType.CANCEL))
                .ifPresent(response -> event.consume());
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int argCount = args.length;

        if (argCount == 1) {
            players = new Players();

            roundPath = args[0];
            round = loadRound();

            launch(args);

        } else {
            Logger.getLogger(Jeopardy.class
                    .getName()).log(Level.SEVERE, "Pass the round YAML file as an argument.");
            System.exit(
                    -1);
        }
    }

    private static Round loadRound() {
        Loader loader = new Loader();
        try {
            return loader.load(roundPath);

        } catch (JeopardyLoaderException ex) {
            Logger.getLogger(Jeopardy.class
                    .getName()).log(Level.SEVERE, "Round YAML file could not be loaded.", ex);
        }

        return null;
    }

    public static Round getRound() {
        return round;
    }

    public static Players getPlayers() {
        return players;
    }
}

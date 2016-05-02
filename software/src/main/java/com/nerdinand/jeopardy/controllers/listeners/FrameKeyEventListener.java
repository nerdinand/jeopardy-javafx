/*
 * The MIT License
 *
 * Copyright 2014 ferdi.
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
package com.nerdinand.jeopardy.controllers.listeners;

import com.nerdinand.jeopardy.models.Frame;
import com.nerdinand.jeopardy.models.Player;
import javafx.scene.control.ButtonType;
import org.controlsfx.dialog.CommandLinksDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ferdi
 */
public class FrameKeyEventListener {

    public static final String YES = "Yes";
    public static final String YOU_TRIED = "You tried...";
    public static final String NO = "No";
    public static final String OOPS = "Oops!";

    private final Frame frame;

    public FrameKeyEventListener(Frame frame) {
        this.frame = frame;
    }

    public ButtonType onPlayerKeyPressed(Player player) {
        if (getFrame().hasDoubleJeopardy()) {
            return onDoubleJeopardyFramePlayerKeyPressed(player);
        } else {
            return onFramePlayerKeyPressed(player);
        }
    }

    private ButtonType onDoubleJeopardyFramePlayerKeyPressed(Player player) {
        List<CommandLinksDialog.CommandLinksButtonType> choices = new ArrayList<>();
        
        choices.add(new CommandLinksDialog.CommandLinksButtonType(YES, null, true));
        choices.add(new CommandLinksDialog.CommandLinksButtonType(NO, null, false));

        Optional<ButtonType> answer;

        do {
            CommandLinksDialog dialog = new CommandLinksDialog(choices);
            dialog.setTitle("Question");
            dialog.setContentText("Player " + player.getName() + ", your solution please?");
            answer = dialog.showAndWait();

        } while (!answer.isPresent());

        return answer.get();
    }
    
    private ButtonType onFramePlayerKeyPressed(Player player) {
        List<CommandLinksDialog.CommandLinksButtonType> choices = new ArrayList<>();

        choices.add(new CommandLinksDialog.CommandLinksButtonType(YES, "Award " + player.getName() + " " + getFrame().getPoints() + " points.", true));
        choices.add(new CommandLinksDialog.CommandLinksButtonType(YOU_TRIED, "Award " + player.getName() + " " + getFrame().getYouTriedPoints() + " points.", false));
        choices.add(new CommandLinksDialog.CommandLinksButtonType(NO, "Punish " + player.getName() + " with -" + getFrame().getPoints() + " points.", false));
        choices.add(new CommandLinksDialog.CommandLinksButtonType(OOPS, "This was a mistake...", false));

        Optional<ButtonType> answer;

        do {
            CommandLinksDialog dialog = new CommandLinksDialog(choices);
            dialog.setTitle("Question");
            dialog.setContentText("Player " + player.getName() + ", your solution please?");
            answer = dialog.showAndWait();

        } while (!answer.isPresent());

        return answer.get();
    }

    public Frame getFrame() {
        return frame;
    }

}

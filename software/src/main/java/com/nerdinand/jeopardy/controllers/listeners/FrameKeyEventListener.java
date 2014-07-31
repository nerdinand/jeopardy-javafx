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
import java.util.ArrayList;
import java.util.List;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.dialog.Dialogs.CommandLink;

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

    public CommandLink onPlayerKeyPressed(Player player) {
        if (getFrame().hasDoubleJeopardy()) {
            return onDoubleJeopardyFramePlayerKeyPressed(player);
        } else {
            return onFramePlayerKeyPressed(player);
        }
    }

    private CommandLink onDoubleJeopardyFramePlayerKeyPressed(Player player) {
        List<Dialogs.CommandLink> choices = new ArrayList<>();
        
        choices.add(new Dialogs.CommandLink(YES, null));
        choices.add(new Dialogs.CommandLink(NO, null));

        Action answer = null;

        do {
            answer = Dialogs.create()
                    .owner(null)
                    .title("Question")
                    .masthead(null)
                    .message("Player " + player.getName() + ", your solution please?")
                    .showCommandLinks(choices);
        } while (!(answer instanceof CommandLink));

        return (CommandLink) answer;    }
    
    private CommandLink onFramePlayerKeyPressed(Player player) {
        List<Dialogs.CommandLink> choices = new ArrayList<>();
        
        choices.add(new Dialogs.CommandLink(YES, "Award " + player.getName() + " " + getFrame().getPoints() + " points."));
        choices.add(new Dialogs.CommandLink(YOU_TRIED, "Award " + player.getName() + " " + getFrame().getYouTriedPoints() + " points."));
        choices.add(new Dialogs.CommandLink(NO, "Punish " + player.getName() + " with -" + getFrame().getPoints() + " points."));
        choices.add(new Dialogs.CommandLink(OOPS, "This was a mistake..."));

        Action answer = null;

        do {
            answer = Dialogs.create()
                    .owner(null)
                    .title("Question")
                    .masthead(null)
                    .message("Player " + player.getName() + ", your solution please?")
                    .showCommandLinks(choices);
        } while (!(answer instanceof CommandLink));

        return (CommandLink) answer;
    }

    public Frame getFrame() {
        return frame;
    }

}

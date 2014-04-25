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
import com.nerdinand.jeopardy.models.Score;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author ferdi
 */
public class FrameKeyEventListener implements PlayerKeyEventListener {
    private final Frame frame;
    private Score score;

    public FrameKeyEventListener(Frame frame) {
        this.frame = frame;
    }

    @Override
    public void onPlayerKeyPressed(Player player) {
        player.setArmed(false);

        Action action = Dialogs.create()
                .owner(null)
                .title("Question")
                .masthead(null)
                .message("Player " + player.getName() + ", your solution please?")
                .showConfirm();
                
        System.out.println(action.toString());
    }
    
}

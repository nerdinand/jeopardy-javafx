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
package com.nerdinand.jeopardy.controllers.listeners;

import com.nerdinand.jeopardy.models.Player;
import org.controlsfx.dialog.Dialogs;

import java.util.Optional;

/**
 *
 * @author Ferdinand Niedermann
 */
public class SetPlayerNameKeyEventListener implements PlayerKeyEventListener {

    public SetPlayerNameKeyEventListener() {
    }

    @Override
    public void onPlayerKeyPressed(Player player) {
        player.setArmed(false);

        String name = askForPlayerName(player);

        player.setName(name);
    }

    private String askForPlayerName(Player player) {
        Optional<String> name;
        do {
            name = Dialogs.create()
                    .owner(null)
                    .title("Name")
                    .masthead(null)
                    .message("Player " + player.getId() + ", please give us your name!")
                    .showTextInput(player.getName());
        } while (name == null);
        
        return name.get();
    }

}

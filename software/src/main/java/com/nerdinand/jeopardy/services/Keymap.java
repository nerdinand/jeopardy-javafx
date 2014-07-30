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
package com.nerdinand.jeopardy.services;

import com.nerdinand.jeopardy.models.Players;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Keymap {

    private final KeyCode CANCEL_KEYCODE = KeyCode.ESCAPE;
    
    public enum Action {
        PLAYER,
        CANCEL
    }

    private static Keymap instance;

    public static Keymap getInstance() {
        if (instance == null) {
            instance = new Keymap();
        }

        return instance;
    }

    public Action getAction(KeyEvent event) {
        KeyCode code = event.getCode();

        if (Players.VALID_KEYS.contains(code)) {
            return Action.PLAYER;
        } else if (code.equals(CANCEL_KEYCODE)) {
            return Action.CANCEL;
        }
        
        return null;
    }

}

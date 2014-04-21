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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Assets {

    public static final String JEOPARDY_MP3 = "/com/nerdinand/jeopardy/media/jeopardy.mp3";

    public static final String MAIN_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/MainWindow.fxml";
    public static final String ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/AnswerWindow.fxml";

    public static AudioClip jeopardyMusic;

    public static void load() {
        loadAudio();
    }

    private static void loadAudio() {
        try {
            String source = Assets.class.getResource(Assets.JEOPARDY_MP3).toString();
            jeopardyMusic = new AudioClip(source);
        } catch (Exception ex) {
            Logger.getLogger(Assets.class.getName()).log(Level.WARNING, "Jeopardy theme music file not found at src" + JEOPARDY_MP3);
        }
    }
}

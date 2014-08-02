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

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Assets {

    public static final String BUZZER_SOUND = "/com/nerdinand/jeopardy/media/Comical Metal Gong.wav";

    public static final String[] YES_SOUNDS = {
        "/com/nerdinand/jeopardy/media/aj-yeehaw.mp3",
        "/com/nerdinand/jeopardy/media/bigm-eeyup.mp3",
        "/com/nerdinand/jeopardy/media/flutters-you-rock-woohoo.mp3"
    };

    public static final String[] NO_SOUNDS = {
        "/com/nerdinand/jeopardy/media/bigm-nope.mp3",
        "/com/nerdinand/jeopardy/media/pp-wrong.wav"
    };

    public static final String[] YOU_TRIED_SOUNDS = {
        "/com/nerdinand/jeopardy/media/aj-nah.mp3"
    };

    public static final String JEOPARDY_MP3 = "/com/nerdinand/jeopardy/media/jeopardy.mp3";

    public static final String MAIN_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/MainWindow.fxml";

    public static final String TEXT_ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/answer/TextWindow.fxml";
    public static final String IMAGE_ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/answer/ImageWindow.fxml";
    public static final String SOUND_ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/answer/SoundWindow.fxml";

    public static final String TEXT_QUESTION_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/question/TextWindow.fxml";
    public static final String IMAGE_QUESTION_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/question/ImageWindow.fxml";
    public static final String SOUND_QUESTION_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/question/SoundWindow.fxml";

    public static AudioClip jeopardyMusic;
    public static AudioClip buzzerSound;

    public static AudioClip[] yesSounds = new AudioClip[YES_SOUNDS.length];
    public static AudioClip[] noSounds = new AudioClip[NO_SOUNDS.length];
    public static AudioClip[] youTriedSounds = new AudioClip[YOU_TRIED_SOUNDS.length];

    public static void load() {
        loadAudio();
    }

    private static void loadAudio() {
        jeopardyMusic = tryLoading(Assets.JEOPARDY_MP3);

        buzzerSound = tryLoading(Assets.BUZZER_SOUND);

        int i = 0;
        for (String path : YES_SOUNDS) {
            yesSounds[i++] = tryLoading(path);
        }

        i = 0;
        for (String path : NO_SOUNDS) {
            noSounds[i++] = tryLoading(path);
        }

        i = 0;
        for (String path : YOU_TRIED_SOUNDS) {
            youTriedSounds[i++] = tryLoading(path);
        }
    }

    private static AudioClip tryLoading(String path) {
        AudioClip sound = null;

        try {
            String source = Assets.class
                    .getResource(path).toString();
            sound = new AudioClip(source);
        } catch (Exception ex) {
            Logger.getLogger(Assets.class
                    .getName()).log(Level.WARNING, "Sound file not found at src{0}", path);
        }

        return sound;
    }

    public static void playRandomSound(AudioClip[] sounds) {
        AudioClip sound = sounds[new Random().nextInt(sounds.length)];

        if (sound != null) {
            sound.play();
        }
    }
}

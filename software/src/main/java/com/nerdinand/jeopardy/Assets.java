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

    public static Assets instance;

    public static Assets getInstance() {
        return instance;
    }

    private Config config;

    public class FXML {

        public static final String MAIN_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/MainWindow.fxml";

        public static final String TEXT_ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/answer/TextWindow.fxml";
        public static final String IMAGE_ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/answer/ImageWindow.fxml";
        public static final String SOUND_ANSWER_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/answer/SoundWindow.fxml";

        public static final String TEXT_QUESTION_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/question/TextWindow.fxml";
        public static final String IMAGE_QUESTION_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/question/ImageWindow.fxml";
        public static final String SOUND_QUESTION_WINDOW_FXML = "/com/nerdinand/jeopardy/fxml/question/SoundWindow.fxml";
    }

    public AudioClip jeopardyMusic;
    public AudioClip[] buzzerSounds;
    public AudioClip[] yesSounds;
    public AudioClip[] noSounds;
    public AudioClip[] youTriedSounds;

    public static void load(String configPath) {
        Assets assets = new Assets();
        assets.loadConfiguration(configPath);
        assets.loadAudio();
        instance = assets;
    }

    private void loadConfiguration(String configPath) {
        Logger.getLogger(Jeopardy.class.getName()).log(Level.INFO, "Loading configuration from {0}", configPath);

        this.config = Config.load(configPath);
    }

    private void loadAudio() {
        jeopardyMusic = tryLoading(config.sounds.jeopardy);
        buzzerSounds = loadSoundArray(config.sounds.buzzer);
        yesSounds = loadSoundArray(config.sounds.yes);
        noSounds = loadSoundArray(config.sounds.no);
        youTriedSounds = loadSoundArray(config.sounds.youTried);
    }

    private AudioClip[] loadSoundArray(String[] paths) {
        AudioClip[] sounds = new AudioClip[paths.length];

        int i = 0;
        for (String path : paths) {
            sounds[i++] = tryLoading(path);
        }

        return sounds;
    }

    private AudioClip tryLoading(String path) {
        AudioClip sound = null;

        try {
            String source = Assets.class
                    .getResource(path).toString();
            sound = new AudioClip(source);
        } catch (Exception ex) {
            Logger.getLogger(Assets.class.getName()).log(Level.WARNING, "Sound file not found at src{0}", path);
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

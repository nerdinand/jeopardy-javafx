package com.nerdinand.jeopardy.models;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alex Murray
 */
public class Audio {
    // must be public so the yaml loader sees this class as a bean
    public String pathPrefix;
    public String backgroundMusicFile;
    public List<String> buzzerSoundFiles;
    public List<String> yesSoundFiles;
    public List<String> noSoundFiles;
    public List<String> youTriedSoundFiles;

    private AudioClip backgroundMusic = null;
    private ArrayList<AudioClip> buzzerSounds = null;
    private ArrayList<AudioClip> yesSounds = null;
    private ArrayList<AudioClip> noSounds = null;
    private ArrayList<AudioClip> youTriedSounds = null;

    public void loadAudio(File rootPath) {
        pathPrefix = new File(rootPath, pathPrefix).toString();

        loadBackgroundMusic();
        buzzerSounds = loadSoundCollection(buzzerSoundFiles);
        yesSounds = loadSoundCollection(yesSoundFiles);
        noSounds = loadSoundCollection(noSoundFiles);
        youTriedSounds = loadSoundCollection(youTriedSoundFiles);
    }

    public void playBackgroundMusic() {
        if(backgroundMusic == null)
            return;
        backgroundMusic.stop();
        backgroundMusic.play();
    }

    public void stopBackgroundMusic() {
        if(backgroundMusic == null)
            return;
        backgroundMusic.stop();
    }

    public void buzzer() {
        playRandomSound(buzzerSounds);
    }

    public void yes() {
        playRandomSound(yesSounds);
    }

    public void no() {
        playRandomSound(noSounds);
    }

    public void youTried() {
        playRandomSound(youTriedSounds);
    }

    private static void playRandomSound(ArrayList<AudioClip> sounds) {
        if (sounds.size() == 0) {
            return;
        }

        AudioClip sound = sounds.get(new Random().nextInt(sounds.size()));

        if (sound != null) {
            sound.play();
        }
    }

    private void loadBackgroundMusic() {
        if(backgroundMusicFile != null) {
            backgroundMusic = tryLoading(backgroundMusicFile);
        }
    }

    private ArrayList<AudioClip> loadSoundCollection(List<String> fileNames) {
        ArrayList<AudioClip> audioClips = new ArrayList<>();

        if(fileNames != null) {
            for(String soundFile : fileNames) {
                AudioClip audioClip = tryLoading(soundFile);
                if(audioClip != null) {
                    audioClips.add(audioClip);
                }
            }
        }

        return audioClips;
    }

    private AudioClip tryLoading(String path) {
        AudioClip sound = null;
        String absolutePath;
        try {
            absolutePath = new File(pathPrefix, path).getCanonicalPath();
        } catch(IOException e) {
            Logger.getLogger(Audio.class.getName()).log(Level.WARNING, "Failed to create canonical path to \"{0}\"", path);
            return null;
        }

        Logger.getLogger(Audio.class.getName()).log(Level.INFO, "Loading sound file \"{0}\"", absolutePath);

        try {
            sound = new AudioClip("file://" + absolutePath);
        } catch (Exception ex) {
            Logger.getLogger(Audio.class
                    .getName()).log(Level.WARNING, "Sound file not found in \"{0}\"", absolutePath);
        }

        return sound;
    }
}

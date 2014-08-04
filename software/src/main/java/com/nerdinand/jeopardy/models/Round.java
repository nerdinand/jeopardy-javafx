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
package com.nerdinand.jeopardy.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Round {
    private String name;
    private File rootPath;
    private List<Category> categories;
    private int version;

    private int doubleJeopardyCount = 2; // default is 2, can be overwritten by round.yml file
    
    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRootPath(File rootPath) {
        this.rootPath = rootPath;

        for (Category category : getCategories()) {
            category.setRootPath(new File(getRootPath(), "media"));
        }
    }

    public File getRootPath() {
        return rootPath;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getDoubleJeopardyCount() {
        return doubleJeopardyCount;
    }

    public void setDoubleJeopardyCount(int doubleJeopardyCount) {
        this.doubleJeopardyCount = doubleJeopardyCount;
    }
    
    public void generateDoubleJeopardies() {
        int count = 0;

        while (count < getDoubleJeopardyCount()) {
            addDoubleJeopardy();
            count++;
        }
    }

    private void addDoubleJeopardy() {
        Frame randomFrame;

        do {
            randomFrame = getRandomFrame();
        } while (randomFrame.hasDoubleJeopardy());

        Logger.getLogger(Round.class.getName()).log(Level.INFO, "adding double Jeopardy to {0}", randomFrame);

        randomFrame.setHasDoubleJeopardy(true);
    }

    private Frame getRandomFrame() {
        Random random = new Random();

        int categoryIndex = random.nextInt(getCategories().size());
        final Category category = getCategories().get(categoryIndex);
        int frameIndex = random.nextInt(category.getFrames().size());

        return category.getFrames().get(frameIndex);
    }

    public List<Frame> getAllFrames() {
        List<Frame> frames = new ArrayList<>();
        
        for (Category category : getCategories()) {
            frames.addAll(category.getFrames());
        }
        
        return frames;
    }
}

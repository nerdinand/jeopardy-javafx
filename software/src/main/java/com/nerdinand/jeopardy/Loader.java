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

import com.nerdinand.jeopardy.models.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Loader {

    public Round load(String path) throws JeopardyLoaderException {
        File roundFile = new File(path);
        File roundDir = roundFile.getParentFile();

        Round round;

        try {
            Yaml yaml = new Yaml(new Constructor(Round.class));
            round = (Round) yaml.load(new FileInputStream(path));
            round.setRootPath(roundDir);

            for (Category category : round.getCategories()) {
                for (Frame frame : category.getFrames()) {
                    frame.setCategoryName(category.getName());
                }
            }

            round.generateDoubleJeopardies();
            checkAssets(round);

        } catch (Exception e) {
            throw new JeopardyLoaderException(e);
        }

        return round;
    }

    private void checkAssets(Round round) {
        for (Category category : round.getCategories()) {
            for (Frame frame : category.getFrames()) {
                if (frame.getAnswer().getMediaType() != MediaType.TEXT) {
                    checkAsset(frame.getAnswer().getMediaPath());
                }
                if (frame.getQuestion().getMediaType() != MediaType.TEXT) {
                    checkAsset(frame.getQuestion().getMediaPath());
                }
            }
        }
    }

    private void checkAsset(File mediaPath) {
        if (!mediaPath.exists()) {
            Logger.getLogger(Jeopardy.class.getName()).log(Level.WARNING, "Asset was not found: {0}", mediaPath);
        }
    }
}

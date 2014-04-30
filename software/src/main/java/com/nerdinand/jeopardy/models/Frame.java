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

/**
 *
 * @author Ferdinand Niedermann
 */
public class Frame {
    
    private int points;
    private Answer answer;
    private Question question;
    private File rootPath;
    
    private final List<Score> scores = new ArrayList<Score>();
    private String categoryName;
    
    public void setPoints(int points) {
        this.points = points;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
    
    public Answer getAnswer() {
        return answer;
    }
    
    public void setQuestion(Question question) {
        this.question = question;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    public void setRootPath(File rootPath) {
        this.rootPath = rootPath;
        
        getAnswer().setRootPath(new File(rootPath, String.valueOf(getPoints())));
        getQuestion().setRootPath(new File(rootPath, String.valueOf(getPoints())));
    }
    
    public File getRootPath() {
        return rootPath;
    }
    
    public void addScore(Score score) {
        scores.add(score);
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public String toString() {
        return getCategoryName() + " for " + getPoints();
    }
    
    public Score getLastScore() {
        return scores.get(scores.size() - 1);
    }
    
}

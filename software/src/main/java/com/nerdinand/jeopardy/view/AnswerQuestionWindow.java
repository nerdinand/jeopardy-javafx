/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nerdinand.jeopardy.view;

import com.nerdinand.jeopardy.controllers.AnswerWindowController;
import com.nerdinand.jeopardy.models.Frame;

/**
 *
 * @author Ferdinand Niedermann
 */
public interface AnswerQuestionWindow {
    public void setController(AnswerWindowController answerWindowController);
    public Frame getFrame();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nerdinand.jeopardy.interfaces;

import com.nerdinand.jeopardy.models.Frame;

/**
 *
 * @author Ferdinand Niedermann
 */
public interface FrameAnsweredListener {
    enum FrameState {
        ANSWERED_CORRECT,
        ANSWERED_WRONG,
        CANCELED
    }
    
    void frameAnswered(Frame frame, FrameState state);
}

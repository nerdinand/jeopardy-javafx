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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Players {
    private static List<Player> playerList;
    private static Player doubleJeopardyPlayer;

    public static final List<KeyCode> VALID_KEYS = Arrays.asList(new KeyCode[]{KeyCode.A, KeyCode.B, KeyCode.C, KeyCode.D});

    public Players() {
        playerList = initializePlayerList();
    }

    private static List<Player> initializePlayerList() {
        List<Player> playerList = new ArrayList<Player>(4);

        Player player;
        player = new Player(1);
        player.setColor(javafx.scene.paint.Color.MEDIUMTURQUOISE);
        player.setName("Foo");
        playerList.add(player);

        player = new Player(2);
        player.setColor(javafx.scene.paint.Color.ORANGE);
        player.setName("Bar");
        playerList.add(player);

        player = new Player(3);
        player.setColor(javafx.scene.paint.Color.FIREBRICK);
        player.setName("Baz");
        playerList.add(player);

        player = new Player(4);
        player.setColor(javafx.scene.paint.Color.PURPLE);
        player.setName("Barz");
        playerList.add(player);

        return playerList;
    }

    public static List<Player> getPlayerList() {
        return playerList;
    }
    
    public void setPlayersArmed(boolean armed) {
        for (Player player : playerList) {
            player.setArmed(armed);
        }
    }

    public Player getArmedPlayerForKey(KeyCode key) {
        int playerIndex = VALID_KEYS.indexOf(key);

        if (playerIndex != -1) {
            final Player player = getPlayerList().get(playerIndex);
            
            if (player.isArmed()) {
                return player;
            } else {
                return null;
            }
        }

        return null;
    }

    public static void setDoubleJeopardyPlayer(Player doubleJeopardyPlayer) {
        Players.doubleJeopardyPlayer = doubleJeopardyPlayer;
    }

    public static Player getDoubleJeopardyPlayer() {
        return doubleJeopardyPlayer;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author didim
 */
public class State implements Serializable{
    List<String> nickName;
    ArrayList<Card> currentRound;
    ArrayList<Integer> playerScores;
    HumanPlayer player;
    boolean hasHeartsBroken; 	// Keep track of whether hears has broken or not
    int playerIndex;		// To help remember which player # this is

    public State() {
        
    }

    public State(List<String> nickName, ArrayList<Card> currentRound, ArrayList<Integer> playerScores, boolean hasHeartsBroken, int playerIndex) {
        this.nickName = nickName;
        this.currentRound = currentRound;
        this.playerScores = playerScores;
        this.hasHeartsBroken = hasHeartsBroken;
        this.playerIndex = playerIndex;
    }

    public HumanPlayer getPlayer() {
        return player;
    }

    public void setPlayer(HumanPlayer player) {
        this.player = player;
    }
    
    public List<String> getNickName() {
        return nickName;
    }

    public void setNickName(List<String> nickName) {
        this.nickName = nickName;
    }
    
    public ArrayList<Card> getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(ArrayList<Card> currentRound) {
        this.currentRound = currentRound;
    }

    public ArrayList<Integer> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(ArrayList<Integer> playerScores) {
        this.playerScores = playerScores;
    }

    public boolean isHasHeartsBroken() {
        return hasHeartsBroken;
    }

    public void setHasHeartsBroken(boolean hasHeartsBroken) {
        this.hasHeartsBroken = hasHeartsBroken;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }    
}
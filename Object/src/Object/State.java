/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author didim
 */
public class State {
    ArrayList<Card> currentRound;
    ArrayList<Integer> playerScores;
    boolean hasHeartsBroken; 	// Keep track of whether hears has broken or not
    Random rng = new Random();
    int playerIndex;		// To help remember which player # this is
}

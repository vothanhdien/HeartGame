/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author DaDa Wind
 */
public class HumanPlayer extends Player implements Serializable {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public Card pickCard(Round cur, boolean isHeartsBroken) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public List<Integer> getExchangeCards() {
        return null;
    }
}

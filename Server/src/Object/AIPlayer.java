/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;

/**
 *
 * @author didim
 */
public class AIPlayer extends Player implements Serializable{
    
    public AIPlayer(String name) {
        super(name);
    }

    @Override
    public Card pickCard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

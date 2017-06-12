/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhearts;

/**
 *
 * @author DaDa Wind
 */
public class HumanPlayer extends Player {

    @Override
    Card pickCard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    HumanPlayer (String name)
    { 
        super(name);
    }
}

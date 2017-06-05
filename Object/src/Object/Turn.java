/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.util.ArrayList;

/**
 *
 * @author didim
 */
//đối tượng quản lý 1 lượt chơi
public class Turn {
    
    private ArrayList<Card> listCard = new ArrayList<>();
    private int Score;
    
    public Turn(){
        Score = 0;
    };
    
    public void addCard(Card card){
        listCard.add(card);
    }

    public ArrayList<Card> getListCard() {
        return listCard;
    }

    public int getScore() {
        listCard.forEach((c) -> {
            Score +=c.getScore();
        });
        return Score;
    }
}

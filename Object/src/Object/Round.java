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
public class Round {
    
    private ArrayList<Card> listCard;
    private int Score;
    
    public Round(){
        Score = 0;
        listCard = new ArrayList<Card>();
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
    
    public CardType getRoundType(){
        return listCard.get(0).getType();
    }
    
    public Card getCardAt(int i){
        
        return listCard.get(i);
        
    }
    
    public int getMaxCard(){
        for(int i= 0; i < listCard.size(); i++){
            Card c = listCard.get(i);
            if(c.getType().equals(getRoundType()) 
                    && c.getValue().equals(getMaxValue())){
                return i;
            }
        }
        return 0;
    }
    
    public Value getMaxValue(){
        Value max= listCard.get(0).getValue();
        
        for(Card c: listCard){
            if(c.getType().equals(getRoundType())){
                if(max.compareTo(c.getValue()) < 0){
                    max = c.getValue();
                }
            }
        }
        
        return max;
    }
}

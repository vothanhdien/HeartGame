/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author didim
 */
public abstract class Player {
    
    String name;
    ArrayList<Card> hand = new ArrayList<>();
    int score;
    
    public Player(String name){
        score = 0;
        this.name = name;
    }
    
    //----------------
    void addCard(Card newCard){
        hand.add(newCard);
    }
    void sortHand(){ hand.sort(new Comparator<Card>() {
        @Override
        public int compare(Card t, Card t1) {
            return t.compareTo(t1);
        }
    });}
    
    boolean checkType(CardType type){
        return hand.stream().anyMatch((c) -> (c.getType() == type));
    }
    //Nếu có con 2 rô => được đánh trước.
    boolean hasTwoOfClubs () { 
        if (hand.isEmpty()) return false;
        Card holder = new Card( Value.TWO, CardType.CLUBS);
        return holder.equals(hand.get(0));
    }
    
    //Đánh bài:
    abstract Card pickCard();
    
    //----------getter, setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }
    
    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/**
 *
 * @author didim
 */
public abstract class Player implements Serializable {

    String name;
    ArrayList<Card> hand = new ArrayList<>();
    int score;

    public Player(String name) {
        score = 0;
        this.name = name;
    }

    //----------------
    public void addCard(Card newCard) {
        hand.add(newCard);
    }

    public void sortHand() {
        hand.sort(new Comparator<Card>() {
            @Override
            public int compare(Card t, Card t1) {
                return t.compareTo(t1);
            }
        });
    }

    public boolean checkType(CardType type) {
        return hand.stream().anyMatch((c) -> (c.getType() == type));
    }

    //Nếu có con 2 rô => được đánh trước.
    public boolean hasTwoOfClubs() {
        if (hand.isEmpty()) {
            return false;
        }
        Card holder = new Card(Value.TWO, CardType.CLUBS);
        return holder.isEqual(hand.get(0));
    }

    public void clearPlayer() {
        clearHand();
        score = 0;
    }

    private void clearHand() {
        hand.clear();
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

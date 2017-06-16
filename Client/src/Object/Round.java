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
 * @author didim
 */
//đối tượng quản lý 1 lượt chơi
public class Round implements Serializable {

    private List<Card> listCard;
    private int Score;
    private int firstPlayer;

    public Round() {
        Score = 0;
        firstPlayer = -1;
        listCard = new ArrayList<Card>();
    }
    
    public void setFirstPlayer(int firstPlayer) {
        this.firstPlayer = firstPlayer;
    }
    
    public int getFirstPlayer()
    {
        return firstPlayer;
    }
    
    public void addCard(Card card) {
        listCard.add(card);
    }
    
    public void setListCard(List<Card> listCard)
    {
        this.listCard = listCard;
    }

    public List<Card> getListCard() {
        return listCard;
    }

    public int getScore() {
        listCard.forEach((c) -> {
            Score += c.getScore();
        });
        return Score;
    }

    public CardType getRoundType() {
        if(firstPlayer == -1)
            return null;
        return listCard.get(firstPlayer).getType();
    }

    public Card getCardAt(int i) {

        return listCard.get(i);
    }

    public int getMaxCard() {
        for (int i = 0; i < listCard.size(); i++) {
            Card c = listCard.get(i);
            if (c.getType().equals(getRoundType())
                    && c.getValue().equals(getMaxValue())) {
                return i;
            }
        }
        return 0;
    }

    public Value getMaxValue() {
        Value max = listCard.get(0).getValue();

        for (Card c : listCard) {
            if (c.getType().equals(getRoundType())) {
                if (max.compareTo(c.getValue()) < 0) {
                    max = c.getValue();
                }
            }
        }
        return max;
    }

    public boolean hasHeart() {
        for (Card c : listCard) {
            if (c.getType().equals(CardType.HEARTS)) {
                return true;
            }
        }
        return false;
    }

    public void renew() {
        listCard = new ArrayList<Card>();
        listCard.add(null);
        listCard.add(null);
        listCard.add(null);
        listCard.add(null);
        firstPlayer = -1;
        Score = 0;
    }
}

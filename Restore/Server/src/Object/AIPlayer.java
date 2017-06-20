/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.util.*;
import java.io.Serializable;

/**
 *
 * @author didim
 */
public class AIPlayer extends Player implements Serializable {

    public AIPlayer(String name) {
        super(name);
    }

    @Override
    public Card pickCard(Round cur, boolean isHeartsBroken) {
        Card c;
        if (hasTwoOfClubs() || hasAllHeart()) {
            c = hand.get(0);
        } else {
            List<Card> validCards = new ArrayList<>();

            CardType firstCardType = cur.getRoundType();
            int size = hand.size();
            //mở những con được đánh
            for (int i = 0; i < size; i++) {
                if (firstCardType == null || hand.get(i).getType() == firstCardType) {
                    validCards.add(hand.get(i));
                }
            }

            if (validCards.size() == size) {
                //Nếu bất đắc dĩ phải đánh cơ đầu tiên thì không cho đánh
                if (!hasAllHeart() && !isHeartsBroken) {
                    for (int i = 0; i < size; i++) {
                        if (hand.get(i).getType() == CardType.HEARTS) {
                            validCards.remove(hand.get(i));
                        }
                    }
                }
            } else if (validCards.isEmpty()) //Nếu không có quân cùng nước
            {
                for (int i = 0; i < size; i++) {
                    validCards.add(hand.get(i));
                }
            }
            if (firstCardType == null) {
                c = validCards.get(0);
            } else {
                c = validCards.get(validCards.size() - 1);
            }
        }
        hand.remove(c);
        return c;
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public List<Integer> getExchangeCards() {
        List<Card> temp = new ArrayList<Card>();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getType() != CardType.HEARTS) {
                temp.add(hand.get(i));
            }
        }
        List<Integer> list = new ArrayList<>();
        int max = findMaxValueCardOfList(temp);
        list.add(max);
        temp.set(max, new Card(Value.TWO, CardType.CLUBS));
        max = findMaxValueCardOfList(temp);
        list.add(max);
        temp.set(max, new Card(Value.TWO, CardType.CLUBS));
        max = findMaxValueCardOfList(temp);
        list.add(max);
        return list;
    }

    int findMaxValueCardOfList(List<Card> list) {
        if (list.isEmpty()) {
            return -1;
        }
        int max = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getValue().compareTo(list.get(max).getValue()) > 0) {
                max = i;
            }
        }
        return max;
    }
}

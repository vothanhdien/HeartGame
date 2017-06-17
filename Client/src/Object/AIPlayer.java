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
            //Không đánh được con nào hay đánh được hết(thằng đánh đầu tiên)
            if (validCards.size() == size) {
                for (int i = 0; i < size; i++) {
                    if (!isHeartsBroken && hand.get(i).getType() == CardType.HEARTS) {
                        validCards.remove(hand.get(i));
                    }
                }
            } else if (validCards.isEmpty()) {
                for (int i = 0; i < size; i++) {
                    if (!isHeartsBroken && hand.get(i).getType() == CardType.HEARTS) {
                    } else {
                        validCards.add(hand.get(i));
                    }
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
}

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
public class Card implements Serializable{
    private Value value;
    private int Score;
    private CardType type;
    
    public Card(Value value, CardType type){
        this.value = value;
        this.type = type;
        setScore();
    }

    public Value getValue() {return value;}

    public void setValue(Value value) {this.value = value;}

    public CardType getType() {return type;}

    public void setType(CardType type) {this.type = type;}

    private void setScore() {
        if(value == Value.QUEEN && type == CardType.SPADES)
            Score = 13;
        else if(type == CardType.HEARTS)
            Score = 1;
        else
            Score = 0;
    }
    
    public int getScore(){return Score;}
    
    boolean isEqual(Card card){
        return this.type == card.type && this.value == card.value;
    }
    public int compareTo (Card other) {
        if (type.compareTo(other.type) == 0)
                return value.compareTo(other.value);
        return type.compareTo(other.type);
    }
    
    //Lay so hieu hinh
    public int getCode(){
        Value[] v = new Value[]{Value.ACE,Value.TWO,Value.THREE,Value.FOUR,Value.FIVE,Value.SIX,Value.SEVEN,
            Value.EIGHT,Value.NINE,Value.TEN,Value.JACK,Value.QUEEN,Value.KING};
        int vi = 0;
        CardType[] t = new CardType[]{CardType.SPADES,CardType.CLUBS,CardType.DIAMONDS,CardType.HEARTS};
        int ti = 0;
        for(vi = 0; vi < 13 ; vi++){
            if(v[vi].equals(getValue()))
                break;
        }
        for(ti = 0;ti < 4;ti++){
            if(t[ti].equals(getType()))
                break;
        }
        
        System.out.println(ti*3 + vi);
        return ti*13 + vi;
    }
}

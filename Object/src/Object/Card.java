/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

/**
 *
 * @author didim
 */
public class Card {
    Value value;
    int Score;
    CardType type;
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
}

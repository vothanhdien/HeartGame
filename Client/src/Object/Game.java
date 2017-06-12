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
public class Game {
    
//    ArrayList<Turn> allTurn = new ArrayList<>(); //
    ArrayList<Card> allCard;
    
    ArrayList<Player> playerOrder; // Dan sách người chơi
    
    int firstPlayer; // vị trí người chơi đầu tiên
    
    ArrayList<Integer> playerScore = new ArrayList<>();// Lưu số điểm ứng theo playerOrder
    
//    ArrayList<Card> currentRound;// 4 la bai trên bàn
    Round currentRound; //4 lá bài trên bàn
    
    boolean twoClubsPlayer;
    //Kiểm tra trạng thái heart break;
    boolean isHeartBreak = false;

    public Game() {
        initCard();
        
    }
    
    int findTaker(int firstPlayer){
        return (firstPlayer + currentRound.getMaxCard())%playerOrder.size();
    }
    
    void initCard(){
        Value v[] = {Value.TWO,Value.THREE,Value.FOUR,Value.FIVE,Value.SIX,Value.SEVEN,
            Value.EIGHT,Value.NINE,Value.TEN,Value.JACK,Value.QUEEN,Value.KING,Value.ACE};
        int vi = 0;
        CardType t[] = {CardType.CLUBS,CardType.DIAMONDS,CardType.SPADES,CardType.HEARTS};
        int ti = 0;
        for(int i = 0; i < 52; i++){
            Card c = new Card(v[vi++],t[ti++]);
            allCard.add(c);
        }
    }
}

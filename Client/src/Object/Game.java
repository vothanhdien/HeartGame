/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didim
 */
public class Game {
    
//    ArrayList<Turn> allTurn = new ArrayList<>(); //
    ArrayList<Card> allCards;
    
    ArrayList<Player> playerOrder; // Dan sách người chơi
    
    int firstPlayer; // vị trí người chơi đầu tiên
    
    ArrayList<Integer> playerScores = new ArrayList<>();// Lưu số điểm ứng theo playerOrder
    ArrayList<Socket> listSockets = new ArrayList<>();
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
            allCards.add(c);
        }
    }
    public void startGame() {
        for(int index = 0; index < playerOrder.size(); index++)
        {
            try {
               InputStream is = listSockets.get(index).getInputStream();
               ObjectInputStream ois = new ObjectInputStream(is);

               OutputStream os = listSockets.get(index).getOutputStream();
               ObjectOutputStream oos = new ObjectOutputStream(os);

//               do {                
                   HumanPlayer player = (HumanPlayer)ois.readObject();
                   playerOrder.add(index, player);
//               } while (true);

           } catch (Exception ex) {
//               Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
           }   
        }
        
        createNewAllCards();
        randomAllCards();
        deal4AllPlayer();
        
        //gui bai cho client
        send_infor_to_all_player();
        
        Thread play_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 13 ;i++){
                    int a = firstPlayer;
                    for(int j =0 ; j < 4; j++){
                        //Cho nguoi chon bai
                        Card c = player_pick_card(a);
                        currentRound.addCard(c);
                        
                        
                        //Gui thong tin cho client
                         
                        
                        a = (a + 1)%playerOrder.size();
                    }
                    //Tim nguoi choi an het bai
                    firstPlayer = findTaker(firstPlayer);
                    //Gan diem cho nguoi choi
                    int score = currentRound.getScore();
                    playerOrder.get(firstPlayer).addScore(score);
                }
                //In nguoi chien thang
                ArrayList<Integer> winnner = findwinner();
                //gui ket qua ve toan bo nguoi choi
                
            }

        });
    }
    
    void createNewAllCards()
    {
        Value[] a = new Value[]{Value.TWO, Value.THREE, Value.FOUR, Value.FIVE, Value.SIX, Value.SEVEN,
                    Value.EIGHT, Value.NINE, Value.TEN, Value.JACK, Value.QUEEN, Value.KING, Value.ACE};
        CardType temp = CardType.SPADES;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
        
        temp = CardType.CLUBS;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
        
        temp = CardType.DIAMONDS;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
        
        temp = CardType.HEARTS;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
    }
    
    void randomAllCards()
    {
        ArrayList<Card> kq = new ArrayList<>();
        Random rd = new Random();
        for(int i = allCards.size(); i > 0;i--){
            int index = rd.nextInt(i);
            kq.add(allCards.get(index));
            allCards.remove(index);
        }
        allCards = kq;
    }
    
    void deal4AllPlayer()
    {
//        List<Player> temp = new ArrayList<>();
        HumanPlayer p1 = (HumanPlayer)playerOrder.get(0);
        HumanPlayer p2 = (HumanPlayer)playerOrder.get(1);
        HumanPlayer p3 = (HumanPlayer)playerOrder.get(2);
        HumanPlayer p4 = (HumanPlayer)playerOrder.get(3);
        for(int i = 0; i < 13; i++)
        {
            p1.addCard(allCards.get(i*4));
            p2.addCard(allCards.get(i*4 + 1));
            p3.addCard(allCards.get(i*4 + 2));
            p4.addCard(allCards.get(i*4 + 3));
        }
//        playerOrder.add(p1);
//        temp.add(p2);
//        temp.add(p3);
//        temp.add(p4);
//        playerOrder = temp;
        for(int i = 0; i < playerOrder.size(); i++){
            if(playerOrder.get(i).hasTwoOfClubs()){
                firstPlayer = i;
                break;
            }
        }
            
    }
    void shotTheMoon () {
        int index = -1;
        for (int i = 0; i < playerScores.size(); i++) {
            if (playerScores.get(i) == 26) {
                System.out.println(playerOrder.get(i).getName() + " shot the moon!");
                index = i;
            }
        }
        // Old Moon: Player does not gain points this round, all others gain 26 points
        if (index > -1) {
            for (int i = 0; i < playerOrder.size(); i++) {
                    if (i != index) {
                            playerOrder.get(i).addScore(26);
                            playerScores.set(i, 26);
                    }
                    // Remove the 26 points that this player received this round
                    else {
                            playerOrder.get(i).addScore(-26);
                            playerScores.set(i,0);
                    }
            }
            if (playerOrder.get(index).getScore()< 0)
                    playerOrder.get(index).clearPlayer();
        }
    }
    ArrayList<Integer> findwinner(){
        ArrayList<Integer> winner = new ArrayList<>();
        int minScore = findMinScore();
        for(int i = 0; i< playerOrder.size();i++)
        {
            if(playerOrder.get(i).getScore() == minScore){
                winner.add(i);
            }
        }
        return winner;
    }

    private int findMinScore() {
        int minScore = playerOrder.get(0).getScore();
        for(int i = 0; i< playerOrder.size(); i++){
            if(minScore < playerOrder.get(i).getScore()){
                minScore = playerOrder.get(i).getScore();
            }
        }
        return minScore;
    }
    private void send_infor_to_all_player() {
//        try {
//            for(int index = 0; index < listSockets.size(); index++)
//            {
//                listOos.get(index).writeObject(playerOrder.get(index));
//                listOos.get(index).flush();
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    private Card player_pick_card(int a) {
        //gui thong bao va nhan object card tu client
        
        return new Card(Value.ACE, CardType.CLUBS);
    }
}

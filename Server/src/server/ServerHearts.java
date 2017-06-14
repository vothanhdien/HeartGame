/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Object.Card;
import Object.CardType;
import Object.HumanPlayer;
import Object.Player;
import Object.Round;
import Object.SocketController;
import Object.State;
import Object.Value;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DaDa Wind
 */
public class ServerHearts {

    /**
     * @param args the command line arguments
     */
    static List<HumanPlayer> listPlayers = new ArrayList<>();
    static int countConnectedClient = 0;
    static List<Card> allCards = new ArrayList<>();
    static List<Socket> listSockets = new ArrayList<>();
    static ServerSocket ss;
    static List<ObjectOutputStream> listOos = new ArrayList<>();
    static List<ObjectInputStream> listOis = new ArrayList<>();

    ArrayList<Card> allCard;

    static int firstPlayer; // vị trí người chơi đầu tiên

    ArrayList<Integer> playerScores = new ArrayList<>();// Lưu số điểm ứng theo playerOrder

//    ArrayList<Card> currentRound;// 4 la bai trên bàn
    static Round currentRound; //4 lá bài trên bàn

    //kiểm tra xem đã đánh có 2 rô chưa
    static boolean twoClubsPlayer;
    //Kiểm tra trạng thái heart break;
    static boolean isHeartBreak = false;

    static int findTaker(int firstPlayer) {
        return (firstPlayer + currentRound.getMaxCard()) % listPlayers.size();
    }

    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ss = new ServerSocket(3200);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.print("");

                        if (listSockets.size() == 4) {
                            startGame();
                            break;
                        }
                    }
                }
            });
            //thread.start();

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        System.out.println("1");
                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
                        listSockets.add(s);

                        s = ss.accept();
                        System.out.println("2");
                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
                        listSockets.add(s);

                        s = ss.accept();
                        System.out.println("3");
                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
                        listSockets.add(s);

                        s = ss.accept();
                        System.out.println("4");
                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
                        listSockets.add(s);

                        startGame();
                    } catch (Exception ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread1.start();

            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        System.out.println("2");
                        listSockets.add(s);
//                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
//                        System.out.println("Đã có: " + listPlayers.size() + " người chơi chuẩn bị.");
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            //thread2.start();

            Thread thread3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        System.out.println("3");
                        listSockets.add(s);
//                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
//                        System.out.println("Đã có: " + listPlayers.size() + " người chơi chuẩn bị.");
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            //thread3.start();

            Thread thread4 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        System.out.println("4");
                        listSockets.add(s);
//                        listPlayers.add((HumanPlayer) SocketController.get_object_from_socket(s));
//                        System.out.println("Đã có: " + listPlayers.size() + " người chơi chuẩn bị.");
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            //thread4.start();

        } catch (IOException ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void startGame() {
        createNewAllCards();
        randomAllCards();
        deal4AllPlayer();
        
        //gui bai cho client
        sendInforToAllPlayer();

        currentRound = new Round();
        
        Thread playing_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 13; i++) {
                    int a = firstPlayer;
                    for (int j = 0; j < 4; j++) {
                        //Cho nguoi chon bai
                        Card c = player_pick_card(a);
                        currentRound.addCard(c);

                        //Gui thong tin cho client
                        sendUpdateInforToAllClient(firstPlayer);

                        a = (a + 1) % listPlayers.size();
                    }
                    //Tim nguoi choi an het bai
                    firstPlayer = findTaker(firstPlayer);
                    //Gan diem cho nguoi choi
                    int score = currentRound.getScore();
                    listPlayers.get(firstPlayer).addScore(score);
                    if(currentRound.hasHeart())
                        isHeartBreak = true;
                    currentRound.renew();
                    //
                    sendUpdateScoreToAllClient();
                }
                //Kiểm tra shot the moon
                
                
                //In nguoi chien thang
                ArrayList<Integer> winnner = findwinner();
                //gui ket qua ve toan bo nguoi choi

            }//run

            
        });//playing thread
        try {
            //playing_thread.start();
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentRound.addCard(new Card(Value.ACE, CardType.CLUBS));
        currentRound.addCard(new Card(Value.ACE, CardType.DIAMONDS));
        currentRound.addCard(new Card(Value.ACE, CardType.HEARTS));
//        currentRound.addCard(new Card(Value.ACE, CardType.SPADES));
        sendUpdateInforToAllClient(firstPlayer);
            
    }

    static void createNewAllCards() {
        Value[] a = new Value[]{Value.TWO, Value.THREE, Value.FOUR, Value.FIVE, Value.SIX, Value.SEVEN,
            Value.EIGHT, Value.NINE, Value.TEN, Value.JACK, Value.QUEEN, Value.KING, Value.ACE};
        CardType temp = CardType.SPADES;
        for (int i = 0; i < 13; i++) {
            allCards.add(new Card(a[i], temp));
        }

        temp = CardType.CLUBS;
        for (int i = 0; i < 13; i++) {
            allCards.add(new Card(a[i], temp));
        }

        temp = CardType.DIAMONDS;
        for (int i = 0; i < 13; i++) {
            allCards.add(new Card(a[i], temp));
        }

        temp = CardType.HEARTS;
        for (int i = 0; i < 13; i++) {
            allCards.add(new Card(a[i], temp));
        }
    }

    static void randomAllCards() {
        ArrayList<Card> kq = new ArrayList<>();
        Random rd = new Random();
        for (int i = allCards.size(); i > 0; i--) {
            int index = rd.nextInt(i);
            kq.add(allCards.get(index));
            allCards.remove(index);
        }
        allCards = kq;
    }

    static void deal4AllPlayer() {
        HumanPlayer p1 = listPlayers.get(0);
        HumanPlayer p2 = listPlayers.get(1);
        HumanPlayer p3 = listPlayers.get(2);
        HumanPlayer p4 = listPlayers.get(3);
        for (int i = 0; i < 13; i++) {
            p1.addCard(allCards.get(i * 4));
            p2.addCard(allCards.get(i * 4 + 1));
            p3.addCard(allCards.get(i * 4 + 2));
            p4.addCard(allCards.get(i * 4 + 3));
        }

        for (int i = 0; i < listPlayers.size(); i++) {
            listPlayers.get(i).sortHand();
            if (listPlayers.get(i).hasTwoOfClubs()) {
                firstPlayer = i;
                break;
            }
        }

    }

    void shotTheMoon() {
        int index = -1;
        for (int i = 0; i < playerScores.size(); i++) {
            if (playerScores.get(i) == 26) {
                System.out.println(listPlayers.get(i).getName() + " shot the moon!");
                index = i;
            }
        }
        // Old Moon: Player does not gain points this round, all others gain 26 points
        if (index > -1) {
            for (int i = 0; i < listPlayers.size(); i++) {
                if (i != index) {
                    listPlayers.get(i).addScore(26);
                    playerScores.set(i, 26);
                } // Remove the 26 points that this player received this round
                else {
                    listPlayers.get(i).addScore(-26);
                    playerScores.set(i, 0);
                }
            }
            if (listPlayers.get(index).getScore() < 0) {
                listPlayers.get(index).clearPlayer();
            }
        }
    }

    static ArrayList<Integer> findwinner() {
        ArrayList<Integer> winner = new ArrayList<>();
        int minScore = findMinScore();
        for (int i = 0; i < listPlayers.size(); i++) {
            if (listPlayers.get(i).getScore() == minScore) {
                winner.add(i);
            }
        }
        return winner;
    }

    private static int findMinScore() {
        int minScore = listPlayers.get(0).getScore();
        for (int i = 0; i < listPlayers.size(); i++) {
            if (minScore < listPlayers.get(i).getScore()) {
                minScore = listPlayers.get(i).getScore();
            }
        }
        return minScore;
    }

    private static void sendInforToAllPlayer() {
//        System.out.println("send information to all client");
        List<String> listName = new ArrayList<>();
        listPlayers.forEach((hp) -> {
                    listName.add(hp.getName());
                });
        try {
            for (int index = 0; index < listSockets.size(); index++) {
//                List<String> temp = null;
//                SocketController.send_object_to_socket(listSockets.get(index), listPlayers.get(index));
//                temp = (List<String>)SocketController.get_object_from_socket(listSockets.get(index));
//                temp = listName;
//                SocketController.send_object_to_socket(listSockets.get(index), temp);
                State state = new State();
                state.setPlayerIndex(index);
                state.setNickName(listName);
                state.setPlayer(listPlayers.get(index));
                SocketController.send_object_to_socket(listSockets.get(index), state);
            }
        } catch (Exception ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    //Người chơi chọn bài
    private static Card player_pick_card(int a) {
        //gui thong bao va nhan object card tu client
        SocketController.send_object_to_socket(listSockets.get(a), new Card(Value.ACE, currentRound.getRoundType()));

        Card c = (Card) SocketController.get_object_from_socket(listSockets.get(a));
        if (c != null) {
            return c;
        }

        return null;
    }

    //Gửi thông tin update về cho tất cả người chơi
    private static void sendUpdateInforToAllClient(int firstPlayer) {
//        Card[] listCard = new Card[]{null,null,null,null};
//        int a = firstPlayer;
//        for(Card c: currentRound.getListCard()){
//            listCard[a % 4] = c;
//            a++;
//        }
        ArrayList<Card> listCards = new ArrayList<Card>();
        for(int i = 0 ; i< 4; i++){
            listCards.add(null);
        }
        int a = firstPlayer;
        for(Card c: currentRound.getListCard()){
            listCards.set(a, c);
            a = (a + 1) % 4;
        }
//        for (Socket s : listSockets) {
//            SocketController.send_object_to_socket(s, listCard);
//        }
        System.out.println("askdhvaskdbhaksdnkasd");
        for (int index = 0; index < listSockets.size(); index++) {
                State state = new State();
                state.setPlayerIndex(index);
                state.setCurrentRound(listCards);
                state.setHasHeartsBroken(isHeartBreak);
                SocketController.send_object_to_socket(listSockets.get(index), state);
                System.out.print("State " + (index + 1) + ": " + state.toString());
        }
    }
    //Gửi thông tin update điểm tới toàn bộ người hơi
    private static void sendUpdateScoreToAllClient() {
        ArrayList<Integer> listScore = new ArrayList<>();
        for(HumanPlayer hp : listPlayers){
            listScore.add(hp.getScore());
        }
        for (int index = 0; index < listSockets.size(); index++) {
                State state = new State();
                state.setPlayerIndex(index);
                state.setPlayerScores(listScore);
                state.setHasHeartsBroken(isHeartBreak);
                SocketController.send_object_to_socket(listSockets.get(index), state);
        }
    }    
}

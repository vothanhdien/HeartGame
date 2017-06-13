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
import Object.Value;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.AWTAutoShutdown;

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
    boolean twoClubsPlayer;
    //Kiểm tra trạng thái heart break;
    boolean isHeartBreak = false;

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
                            try {
                                Thread.sleep(10000000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        }
                    }
                }
            });
            thread.start();

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        System.out.println("...");
                        listSockets.add(s);
                    } catch (IOException ex) {
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
                        System.out.println("...");
                        listSockets.add(s);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread2.start();

            Thread thread3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        listSockets.add(s);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread3.start();

            Thread thread4 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        System.out.println("...");
                        listSockets.add(s);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread4.start();

        } catch (IOException ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void startGame() {
        for (int index = 0; index < listSockets.size(); index++) {
            try {
                InputStream is = listSockets.get(index).getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                listOis.add(ois);

                OutputStream os = listSockets.get(index).getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                listOos.add(oos);

                HumanPlayer player = (HumanPlayer) ois.readObject();
                listPlayers.add(index, player);
            } catch (Exception ex) {
                Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        createNewAllCards();
        randomAllCards();
        deal4AllPlayer();

        //gui bai cho client
        send_infor_to_all_player();

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
                        a = (a + 1) % listPlayers.size();
                    }
                    //Tim nguoi choi an het bai
                    firstPlayer = findTaker(firstPlayer);
                    //Gan diem cho nguoi choi
                    int score = currentRound.getScore();
                    listPlayers.get(firstPlayer).addScore(score);
                }
                //In nguoi chien thang
                ArrayList<Integer> winnner = findwinner();
                //gui ket qua ve toan bo nguoi choi

            }

        });
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

    private static void send_infor_to_all_player() {
        try {
            for (int index = 0; index < listSockets.size(); index++) {
                listOos.get(index).writeObject(listPlayers.get(index));
                listOos.get(index).flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Card player_pick_card(int a) {
        //gui thong bao va nhan object card tu client

        return new Card(Value.ACE, CardType.CLUBS);
    }
}

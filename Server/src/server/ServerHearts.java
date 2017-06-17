/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Object.AIPlayer;
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
    static List<Player> listPlayers = new ArrayList<>();
    static int countConnectedClient = 0;
    static List<Card> allCards = new ArrayList<>();
    static List<Socket> listSockets = new ArrayList<>();
    static ServerSocket ss;
    //Đã gửi thông tin xong hay chưa
    static boolean isSentInfo = false;
    static int numberOfPlayers = 4;
    static int firstPlayer; // vị trí người chơi đầu tiên

    private static void create4AIPlayers() {
        listPlayers = new ArrayList<Player>();
        AIPlayer temp = new AIPlayer("Master Teihk");
        listPlayers.add(temp);
        temp = new AIPlayer("Nhat Linh Doan");
        listPlayers.add(temp);
        temp = new AIPlayer("Dien Vo Thanh");
        listPlayers.add(temp);
        temp = new AIPlayer("DaDa Wind");
        listPlayers.add(temp);
    }

    ArrayList<Integer> playerScores = new ArrayList<>();// Lưu số điểm ứng theo playerOrder

//    ArrayList<Card> currentRound;// 4 la bai trên bàn
    static Round currentRound; //4 lá bài trên bàn

    //kiểm tra xem đã đánh có 2 rô chưa
    static boolean twoClubsPlayer;
    //Kiểm tra trạng thái heart break;
    static boolean isHeartsBroken = false;

    static int findTaker(int firstPlayer) {
        return currentRound.getMaxCard();
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
                    create4AIPlayers();
                    try {

                        Socket s = ss.accept();
                        System.out.println("1");
                        numberOfPlayers = (int) SocketController.get_object_from_socket(s);
                        System.out.println(numberOfPlayers);
                        listPlayers.set(0, (Player) SocketController.get_object_from_socket(s));
                        listSockets.add(s);
                        if (numberOfPlayers == 1) {
                            startGame();
                            return;
                        }

                        s = ss.accept();
                        System.out.println("2");
                        SocketController.get_object_from_socket(s);
                        listPlayers.set(1, (Player) SocketController.get_object_from_socket(s));
                        listSockets.add(s);
                        if (numberOfPlayers == 2) {
                            startGame();
                            return;
                        }

                        s = ss.accept();
                        System.out.println("3");
                        SocketController.get_object_from_socket(s);
                        listPlayers.set(2, (Player) SocketController.get_object_from_socket(s));
                        listSockets.add(s);
                        if (numberOfPlayers == 3) {
                            startGame();
                            return;
                        }

                        s = ss.accept();
                        System.out.println("4");
                        int temp = (int) SocketController.get_object_from_socket(s);
                        listPlayers.set(3, (Player) SocketController.get_object_from_socket(s));
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
        currentRound.renew();

        Thread playing_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(!isSentInfo)
                    {
                        continue;
                    }
                    isSentInfo = false;
                    for (int i = 0; i < 13; i++) {
                        try {
                            int a = firstPlayer;
                            sendUpdateInforToAllClient(firstPlayer, firstPlayer);
                            for (int j = 0; j < 4; j++) {
                                //Cho nguoi chon bai
                                Card c = player_pick_card(a);
                                currentRound.setFirstPlayer(firstPlayer);
                                currentRound.getListCard().set(a, c);
                                //Gui thong tin cho client
                                sendUpdateInforToAllClient(firstPlayer, (a + 1) % 4);

                                a = (a + 1) % listPlayers.size();
                            }
                            //Tim nguoi choi an het bai
                            firstPlayer = findTaker(firstPlayer);
                            //Gan diem cho nguoi choi
                            int score = currentRound.getScore();
                            listPlayers.get(firstPlayer).addScore(score);
                            if (currentRound.hasHeart()) {
                                isHeartsBroken = true;
                            }
                            //
                            currentRound.renew();
                            currentRound.setFirstPlayer(firstPlayer);

                            sendUpdateScoreToAllClient();
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    //Kiểm tra shot the moon
                    //In nguoi chien thang
                    ArrayList<Integer> winnner = findwinner();
                    SendEndScoreToAllPlayer();
                    currentRound = new Round();
                    currentRound.renew();
                    randomAllCards();
                    deal4AllPlayer();
                    isHeartsBroken = false;
                    for (int i = 0; i < listSockets.size(); i++) {
                        SocketController.send_object_to_socket(listSockets.get(i), "New round");
                    }
                    //gui bai cho client
                    sendInforToAllPlayer();
                }

            }//run

        });
        playing_thread.start();
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
        for (int i = 0; i < listPlayers.size(); i++) {
            System.out.println(listPlayers.size());
            List<Card> list = new ArrayList<Card>();
            listPlayers.get(i).setHand(list);
        }

        for (int i = 0; i < 13; i++) {
            listPlayers.get(0).addCard(allCards.get(i * 4));
            listPlayers.get(1).addCard(allCards.get(i * 4 + 1));
            listPlayers.get(2).addCard(allCards.get(i * 4 + 2));
            listPlayers.get(3).addCard(allCards.get(i * 4 + 3));
        }

        for (int i = 0; i < listPlayers.size(); i++) {
            listPlayers.get(i).sortHand();
            if (listPlayers.get(i).hasTwoOfClubs()) {
                firstPlayer = i;
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
        List<String> listName = new ArrayList<>();
        listPlayers.forEach((hp) -> {
            listName.add(hp.getName());
        });
        try {
            for (int index = 0; index < listSockets.size(); index++) {
                State state = new State();
                List<Integer> listScores = new ArrayList<>();
                listScores.add(0);
                listScores.add(0);
                listScores.add(0);
                listScores.add(0);
                state.setHasHeartsBroken(isHeartsBroken);
                state.setIPlayPlaying(firstPlayer);
                state.setPlayerScores(listScores);
                state.setNickName(listName);
                state.setPlayerIndex(index);
                state.setPlayer(listPlayers.get(index));
                SocketController.send_object_to_socket(listSockets.get(index), state);
            }
            isSentInfo = true;
        } catch (Exception ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Người chơi chọn bài
    private static Card player_pick_card(int a) {
        if (listPlayers.get(a).isHuman()) {
            //gui thong bao va nhan object card tu client
            SocketController.send_object_to_socket(listSockets.get(a), "Your innings came");
            Card c = (Card) SocketController.get_object_from_socket(listSockets.get(a));
            return c;
        } else {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listPlayers.get(a).pickCard(currentRound, isHeartsBroken);
        }
    }

    //Gửi thông tin update về cho tất cả người chơi
    private static void sendUpdateInforToAllClient(int firstPlayer, int i) {
//        int a = firstPlayer;
        for (int index = 0; index < listPlayers.size(); index++) {
            if (listPlayers.get(index).isHuman()) {
                State state = new State();
                state.setIPlayPlaying(i);
                state.setCurrentRound(currentRound);
                state.setHasHeartsBroken(isHeartsBroken);
                state.setPlayerIndex(index);
                SocketController.send_object_to_socket(listSockets.get(index), "Update info");
                SocketController.send_object_to_socket(listSockets.get(index), state);
            }
        }
    }

    //Gửi thông tin update điểm tới toàn bộ người hơi
    private static void sendUpdateScoreToAllClient() {
        List<Integer> listScore = new ArrayList<>();
        for (Player hp : listPlayers) {
            listScore.add(hp.getScore());
        }
        for (int index = 0; index < listPlayers.size(); index++) {
            if (listPlayers.get(index).isHuman()) {
                State state = new State();
                state.setCurrentRound(currentRound);
                state.setPlayerScores(listScore);
                state.setHasHeartsBroken(isHeartsBroken);
                state.setPlayerIndex(index);
                SocketController.send_object_to_socket(listSockets.get(index), "Update score");
                SocketController.send_object_to_socket(listSockets.get(index), state);
            } else {

            }
        }
    }

    //gửi điểm tổng kết
    private static void SendEndScoreToAllPlayer() {
        List<Integer> listScore = new ArrayList<>();
        for (Player hp : listPlayers) {
            listScore.add(hp.getScore());
        }
        for (int index = 0; index < listSockets.size(); index++) {
            State state = new State();
            state.setCurrentRound(currentRound);
            state.setPlayerScores(listScore);
            state.setHasHeartsBroken(isHeartsBroken);
            state.setPlayerIndex(index);
            SocketController.send_object_to_socket(listSockets.get(index), "Show result");
            SocketController.send_object_to_socket(listSockets.get(index), state);
        }
    }
}

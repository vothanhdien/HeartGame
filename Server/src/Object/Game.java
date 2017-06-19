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
import java.net.SocketException;
import java.util.*;
import java.util.logging.*;
import server.ServerHearts;

/**
 *
 * @author didim
 */
public class Game {

//    ArrayList<Turn> allTurn = new ArrayList<>(); //
    List<Card> allCards;
    List<Player> listPlayers; // Dan sách người chơi
    List<Socket> listSockets;
    List<Integer> playerScores;

    int firstPlayer; // vị trí người chơi đầu tiên
    int numberOfPlayers;
    boolean isSentInfo;
    boolean isHeartsBroken;
    Round currentRound; 

    public Game(int numberOfPlayers) {
        allCards = new ArrayList<Card>();
        listSockets = new ArrayList<>();
        create4AIPlayers();
        firstPlayer = -1;
        this.numberOfPlayers = numberOfPlayers;
        isSentInfo = false;
        isHeartsBroken = false;
        playerScores = Arrays.asList(0, 0, 0, 0);
    }

    //Trả về số lượng người chơi trong game
    public int addPlayer(Player player, Socket s) {
        listPlayers.set(listSockets.size(), player);
        listSockets.add(s);
        return listSockets.size();
    }

    public void setNumberOfPlayers(int number) {
        numberOfPlayers = number;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isFull() {
        return listSockets.size() == numberOfPlayers;
    }

    private void create4AIPlayers() {
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

    private int findTaker(int firstPlayer) {
        return currentRound.getMaxCard();
    }

    public void startGame() {
        createNewAllCards();
        randomAllCards();
        deal4AllPlayer();

        //gui bai cho client
        sendInforToAllPlayer();

        currentRound = new Round();
        currentRound.renew();

//        Thread playing_thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//Có người chơi đạt > 99 điểm thì dừng
        while (true) {
            //Đã gửi bài về cho tất cả người chơi hay chưa, nếu chưa đợi
            if (!isSentInfo) {
                isSentInfo = false;
                continue;
            }
            //Bat dau doi bai
            ExchangePlayersCard();
            for (int i = 0; i < 13; i++) {
                try {
                    int a = firstPlayer;
                    currentRound.setFirstPlayer(firstPlayer);
                    
                    sendUpdateInforToAllClient(firstPlayer);
                    Card c = null;
                    for (int j = 0; j < 4; j++) {
                        //Cho nguoi chon bai
                        if (listPlayers.get(a).isHuman()) {
                            c = player_pick_card(a);
                            if (c == null) {
                                State state = new State();
                                state.setCommand(Command.SOCKET_CLOSED);
                                state.setIPlayPlaying(a);
                                for (int k = 0; k < listSockets.size(); k++) {
                                    if (k != a) {
                                        SocketController.send_object_to_socket(listSockets.get(k), state);
                                    }
                                }
                                return;
                            }
                        } else {
                            c = player_pick_card(a);
                        }
                        
                        //gán lá bài vào currentRound
                        currentRound.getListCard().set(a, c);
                        //Gui thong tin cho client
                        sendUpdateInforToAllClient((a + 1) % 4);
                        
                        a = (a + 1) % listPlayers.size();
                    }
                    //Tim nguoi choi an het bai
                    firstPlayer = findTaker(firstPlayer);
                    //Gan diem cho nguoi choi
                    int score = currentRound.getScore();
//                    listPlayers.get(firstPlayer).addScore(score);
                    int newScore = playerScores.get(firstPlayer) + score;
                    playerScores.set(firstPlayer,newScore);
                    
                    if (currentRound.hasHeart()) {
                        isHeartsBroken = true;
                    }
                    //
                    currentRound.renew();
//                    currentRound.setFirstPlayer(firstPlayer);

                    sendUpdateScoreToAllClient();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            //Kiểm tra shot the moon
            shotTheMoon();
            //In nguoi chien thang
            ArrayList<Integer> winnner = findwinner();
            SendEndScoreToAllPlayer();
//            currentRound = new Round();
            currentRound.renew();
            randomAllCards();
            deal4AllPlayer();
            SaveAndResetScore();
            isHeartsBroken = false;
            //gui bai cho client
            //Ván tiếp theo gửi thông tin bài mới cho người chơi
            sendInforToAllPlayer();
        }
//
//            }//run
//
//        });
//        playing_thread.start();
    }

    void createNewAllCards() {
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

    void randomAllCards() {
        ArrayList<Card> kq = new ArrayList<>();
        Random rd = new Random();
        for (int i = allCards.size(); i > 0; i--) {
            int index = rd.nextInt(i);
            kq.add(allCards.get(index));
            allCards.remove(index);
        }
        allCards = kq;
    }

    void deal4AllPlayer() {
        for (int i = 0; i < listPlayers.size(); i++) {
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
        }
    }

    private void shotTheMoon() {
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
//                    listPlayers.get(i).addScore(26);
                    playerScores.set(i, 26);
                } // Remove the 26 points that this player received this round
                else {
//                    listPlayers.get(i).addScore(-26);
                    playerScores.set(i, 0);
                }
            }
            if (listPlayers.get(index).getScore() < 0) {
                listPlayers.get(index).clearPlayer();
            }
        }
    }

    private ArrayList<Integer> findwinner() {
        ArrayList<Integer> winner = new ArrayList<>();
        int minScore = findMinScore();
        for (int i = 0; i < listPlayers.size(); i++) {
            if (playerScores.get(i) == minScore) {
                winner.add(i);
            }
        }
        return winner;
    }

    private int findMinScore() {
        int minScore = playerScores.get(0);
        for (int i = 0; i < playerScores.size(); i++) {
            if (minScore < playerScores.get(i)) {
                minScore = playerScores.get(i);
            }
        }
        return minScore;
    }

    private void sendInforToAllPlayer() {
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

                state.setCommand(Command.INIT);
                SocketController.send_object_to_socket(listSockets.get(index), state);
            }
            isSentInfo = true;
        } catch (Exception ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Người chơi chọn bài
    private Card player_pick_card(int a) {
        if (listPlayers.get(a).isHuman()) {
            //gui thong bao va nhan object card tu client
            State state = new State();
            state.setCommand(Command.PICK_CARD);
            //nó trả ra mảng có thứ tự ----> mục đích chỉ là lấy kiểu con bài đầu tiên
            state.setCurrentRound(currentRound);
            state.setHasHeartsBroken(isHeartsBroken);

            //Nếu socket đã bị đóng
            if (SocketController.send_object_to_socket(listSockets.get(a), state) == false) {
                return null;
            }

            Card c = (Card) SocketController.get_object_from_socket(listSockets.get(a));

            return c;

        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
            }
            return listPlayers.get(a).pickCard(currentRound, isHeartsBroken);
        }
    }

    //Gửi thông tin update về cho tất cả người chơi
    private void sendUpdateInforToAllClient(int i) {
//        int a = firstPlayer;
        for (int index = 0; index < listPlayers.size(); index++) {
            if (listPlayers.get(index).isHuman()) {
                State state = new State();
                state.setIPlayPlaying(i);
                state.setCurrentRound(currentRound);
                state.setPlayerScores(playerScores);
                state.setHasHeartsBroken(isHeartsBroken);
                state.setPlayerIndex(index);

                state.setCommand(Command.UPDATE_VIEW);
                SocketController.send_object_to_socket(listSockets.get(index), state);
            }
        }
    }

    //Gửi thông tin update điểm tới toàn bộ người hơi
    private void sendUpdateScoreToAllClient() {
//        List<Integer> listScore = new ArrayList<>();
//        for (Player hp : listPlayers) {
//            listScore.add(hp.getScore());
//        }
        for (int index = 0; index < listPlayers.size(); index++) {
            if (listPlayers.get(index).isHuman()) {
                State state = new State();
                state.setCurrentRound(currentRound);
                state.setPlayerScores(playerScores);
                state.setPlayerIndex(index);

                state.setCommand(Command.UPDATE_SCORE);
                SocketController.send_object_to_socket(listSockets.get(index), state);
            } else {

            }
        }
    }

    //gửi điển tổng kết
    private void SendEndScoreToAllPlayer() {
//        List<Integer> listScore = new ArrayList<>();
//        for (Player hp : listPlayers) {
//            listScore.add(hp.getScore());
//        }
        for (int index = 0; index < listSockets.size(); index++) {
            State state = new State();
            state.setPlayerScores(playerScores);
            state.setPlayerIndex(index);

            state.setCommand(Command.SHOW_RESULT);
            SocketController.send_object_to_socket(listSockets.get(index), state);
        }
    }

    private void ExchangePlayersCard() {
        ArrayList<Card> exchangeCards_player0 = new ArrayList<Card>();
        ArrayList<Card> exchangeCards_player1 = new ArrayList<Card>();
        ArrayList<Card> exchangeCards_player2 = new ArrayList<Card>();
        ArrayList<Card> exchangeCards_player3 = new ArrayList<Card>();

        //Nhận những lá bài gửi về
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                List<Integer> exchangeCards = null;
                //Loai bo bai da doi
                if (listPlayers.get(0).isHuman()) {
                    exchangeCards = (List) SocketController.get_object_from_socket(listSockets.get(0));
                } else //Nếu người chơi là máy
                {
                    exchangeCards = listPlayers.get(0).getExchangeCards();
                }
                for (int i = 0; i < 3; i++) {
                    int x = exchangeCards.get(i);
                    exchangeCards_player0.add(listPlayers.get(0).getHand().get(x));
                }
                exchangeCards_player0.forEach((c) -> {
                    listPlayers.get(0).getHand().remove(c);
                });
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                //Loai bo bai da doi
                List<Integer> exchangeCards = null;
                //Loai bo bai da doi
                if (listPlayers.get(1).isHuman()) {
                    exchangeCards = (List) SocketController.get_object_from_socket(listSockets.get(1));
                } else //Nếu người chơi là máy
                {
                    exchangeCards = listPlayers.get(1).getExchangeCards();
                }
                for (int i = 0; i < 3; i++) {
                    int x = exchangeCards.get(i);
                    exchangeCards_player1.add(listPlayers.get(1).getHand().get(x));
                }
                exchangeCards_player1.forEach((c) -> {
                    listPlayers.get(1).getHand().remove(c);
                });
//            listPlayers.get(1).setHand(newHand);
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {

                //Loai bo bai da doi
                List<Integer> exchangeCards = null;
                //Loai bo bai da doi
                if (listPlayers.get(2).isHuman()) {
                    exchangeCards = (List) SocketController.get_object_from_socket(listSockets.get(2));
                } else //Nếu người chơi là máy
                {
                    exchangeCards = listPlayers.get(2).getExchangeCards();
                }
                for (int i = 0; i < 3; i++) {
                    int x = exchangeCards.get(i);
                    exchangeCards_player2.add(listPlayers.get(2).getHand().get(x));
                }
                exchangeCards_player2.forEach((c) -> {
                    listPlayers.get(2).getHand().remove(c);
                });
//            listPlayers.get(2).setHand(newHand);
            }
        });
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {

                //Loai bo bai da doi
                List<Integer> exchangeCards = null;
                //Loai bo bai da doi
                if (listPlayers.get(3).isHuman()) {
                    exchangeCards = (List) SocketController.get_object_from_socket(listSockets.get(3));
                } else //Nếu người chơi là máy
                {
                    exchangeCards = listPlayers.get(3).getExchangeCards();
                }
                for (int i = 0; i < 3; i++) {
                    int x = exchangeCards.get(i);
                    exchangeCards_player3.add(listPlayers.get(3).getHand().get(x));
                }
                exchangeCards_player3.forEach((c) -> {
                    listPlayers.get(3).getHand().remove(c);
                });
//            listPlayers.get(3).setHand(newHand);
            }
        });
        thread4.start();
        thread3.start();
        thread2.start();
        thread1.start();
        
        for (int index = 0; index < listSockets.size(); index++) {
            State state = new State();
            state.setCommand(Command.EXCHANGE_CARD);
            SocketController.send_object_to_socket(listSockets.get(index), state);
        }

        int total = 0;
        while (total < 12) {
            total = exchangeCards_player0.size() + exchangeCards_player1.size()
                    + exchangeCards_player2.size() + exchangeCards_player3.size();
            // không được xóa dòng này, vì phải cập nhật biến total liên tục
            System.out.println(total);
            try {
                Thread.sleep(0);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (total == 12) {
            if (true) {
                listPlayers.get(3).addCard(exchangeCards_player0);
                listPlayers.get(2).addCard(exchangeCards_player1);
                listPlayers.get(1).addCard(exchangeCards_player2);
                listPlayers.get(0).addCard(exchangeCards_player3);
            }

            for (int i = 0; i < listPlayers.size(); i++) {
                listPlayers.get(i).sortHand();
                if (listPlayers.get(i).hasTwoOfClubs()) {
                    firstPlayer = i;
                }
            }
            //Gui bai moi cho all client
            sendInforToAllPlayer();
            //Cho toan bo nguoi cho doi card
        }
    }

    private void SaveAndResetScore() {
        for(int i =0; i< listPlayers.size(); i++){
            listPlayers.get(i).addScore(playerScores.get(i));
            playerScores.set(i, 0);
        }
    }
}

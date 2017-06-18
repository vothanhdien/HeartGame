/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Object.*;
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
//    static List<List<Player>> listPlayers = new ArrayList<>();
//    static int countConnectedClient = 0;
//    static List<Card> allCards = new ArrayList<>();
//    static List<List<Socket>> listSockets = new ArrayList<>();
//    static ServerSocket ss;
//    //Đã gửi thông tin xong hay chưa
//    static boolean isSentInfo = false;
//    static int numberOfPlayers = 4;
//    static int firstPlayer; // vị trí người chơi đầu tiên
//
//    //Kiểm tra trạng thái heart break;
//    static boolean isHeartsBroken = false;
    static ServerSocket ss;
    static List<Game> listGames = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ss = new ServerSocket(3200);

            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Socket s = ss.accept();
                            System.out.println("Số lượng người chơi: " + listGames.size());
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int numberOfPlayers = (int) SocketController.get_object_from_socket(s);
                                    HumanPlayer player = (HumanPlayer) SocketController.get_object_from_socket(s);
                                    int indexGame = findRoom(numberOfPlayers);
                                    Game game = null;
                                    if (indexGame == -1) {
                                        game = new Game(numberOfPlayers);
                                        listGames.add(game);
                                    } else {
                                        game = listGames.get(indexGame);
                                    }
                                    game.addPlayer(player, s);
                                    if (game.isFull()) {
                                        game.startGame();
                                    }
                                }

                                private int findRoom(int numberOfPlayers) {
                                    for (int i = 0; i < listGames.size(); i++) {
                                        Game game = listGames.get(i);
                                        if (!game.isFull() && game.getNumberOfPlayers() == numberOfPlayers) {
                                            return i;
                                        }
                                    }
                                    return -1;
                                }
                            });
                            thread.start();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread1.start();
        } catch (Exception ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
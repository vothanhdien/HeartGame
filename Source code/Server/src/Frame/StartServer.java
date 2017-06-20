/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.Game;
import Object.HumanPlayer;
import Object.SocketController;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import server.ServerHearts;

/**
 *
 * @author DaDa Wind
 */
public class StartServer extends JFrame {

    JButton btnStart = new JButton("Start Server");
    JButton btnStop = new JButton("Stop Server");
    ServerSocket ss;
    List<Game> listGames = new ArrayList<>();
    Thread thread1 = null;

    public StartServer() {
        setTitle("Server:127.0.0.1 Port: 3200");
        setPreferredSize(new Dimension(400, 100));
        Container container = getContentPane();

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }

            private void startServer() {
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);
                try {
                    // TODO code application logic here
                    ss = new ServerSocket(3200);

                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (true) {
                                    Socket s = ss.accept();
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
                                
                            } catch (Exception ex) {
                                Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    thread1.start();
                } catch (Exception ex) {
                    Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopServer();
            }
        });

        container.setLayout(
                new GridLayout(2, 1));
        container.add(btnStart);

        container.add(btnStop);

        btnStop.setEnabled(
                false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(
                null);
        pack();

        setVisible(
                true);
    }

    private void stopServer() {
        listGames.forEach((t) -> {
            t.stopGame();
        });
        try {
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(StartServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread1 = null;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
    }
}

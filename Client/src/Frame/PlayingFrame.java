/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.Card;
import Object.HumanPlayer;
import Object.ImageController;
import Object.Round;
import Object.SocketController;
import Object.State;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author HP
 */
public class PlayingFrame extends JFrame implements ActionListener{

    Socket socket;
    List<Card> listCards = new ArrayList<>();
    int score = 0;
    boolean hasHeartsBroken = false;
    List<JButton> listButtonCards = new ArrayList<>();
    int playerIndex;
    //component
    
    JLabel jlPlayerScore;
    JLabel jlTopPlayerScore;
    JLabel jlLeftPlayerScore;
    JLabel jlRightPlayerScore;
    
    JLabel jlTopCard;
    JLabel jlRightCard;
    JLabel jlBottomCard;
    JLabel jlLeftCard;
    
    
    
    public PlayingFrame(Socket s, HumanPlayer player, List<String> listNickName, int playerIndex) throws HeadlessException {
        this.socket = s;
        this.playerIndex = playerIndex;
        Container container = this.getContentPane();
//        System.out.println(player.getName());
        this.setTitle(player.getName());
        //container.add(new Game(player, listNickName));
        
        jlTopPlayerScore = new JLabel("0");
        jlLeftPlayerScore = new JLabel("0");
        jlRightPlayerScore = new JLabel("0");
        
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane1 = new JPanel(new GridBagLayout());
        ImageIcon ii = ImageController.getImageByName("person.png", 100, 100);
        JPanel leftPerson = getPanelPerson(listNickName.get(1), 0, ii, jlLeftPlayerScore);
        c.gridx = 1;
        c.gridy = 2;
        pane1.add(leftPerson, c);
        
        JPanel topPerson = getPanelPerson(listNickName.get(2), 0, ii, jlTopPlayerScore);
        c.insets = new Insets(0, 200, 30, 200);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 6;
        pane1.add(topPerson, c);
        c.insets = new Insets(0, 0, 30, 0);

        JPanel rightPerson = getPanelPerson(listNickName.get(3), 0, ii, jlRightPlayerScore);
        c.gridx = 9;
        c.gridy = 2;
        c.gridwidth = 1;
        pane1.add(rightPerson, c);

        ii = ImageController.getImageByName("back.png", 30, 30);
        JButton btnBack = new JButton(ii);
        btnBack.addActionListener(this);
        btnBack.setActionCommand("Back");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setPreferredSize(new Dimension(30, 30));

        ii = ImageController.getImageByName("history.png", 30, 30);
        JButton btnHistory = new JButton(ii);
        btnHistory.addActionListener(this);
        btnHistory.setActionCommand("History");
        btnHistory.setBorderPainted(false);
        btnHistory.setContentAreaFilled(false);
        btnHistory.setPreferredSize(new Dimension(30, 30));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        pane1.add(btnBack, c);

        c.gridx = 10;
        c.gridy = 0;
        c.gridwidth = 1;
        pane1.add(btnHistory, c);

        GridBagConstraints c1 = new GridBagConstraints();
        //nơi 4 lá bài được đánh ra
        JPanel pane4cards = new JPanel(new GridBagLayout());

        jlLeftCard = new JLabel();
        jlTopCard = new JLabel();
        jlRightCard = new JLabel();
        jlBottomCard = new JLabel();
        c1.gridx = 0;
        c1.gridy = 1;
        c1.gridheight = 2;
        pane4cards.add(jlLeftCard, c1);

        c1.gridx = 1;
        c1.gridy = 0;
        c1.gridheight = 2;
        pane4cards.add(jlTopCard, c1);

        c1.gridx = 2;
        c1.gridy = 1;
        c1.gridheight = 2;
        pane4cards.add(jlRightCard, c1);

        c1.gridx = 1;
        c1.gridy = 2;
        c1.gridheight = 2;
        pane4cards.add(jlBottomCard, c1);

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 6;
        pane1.add(pane4cards, c);

        //So diem cua nguoi choi
        String tmp = String.format("%d", player.getScore());
        jlPlayerScore = new JLabel(tmp);
        
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        pane1.add(jlPlayerScore, c);
        
        //Cac la bai cua nguoi choi
        JPanel allCardOfPalyer = new JPanel(new GridBagLayout());
        createAllButtonCards(allCardOfPalyer, player);
        
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 6;
        pane1.add(allCardOfPalyer, c);

        container.add(pane1);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setPreferredSize(new Dimension(1000,600));
        this.pack();
        this.setVisible(true);
        
        //GameStart();
    }

    private JPanel getPanelPerson(String name, int score, ImageIcon ii, JLabel jlScore) {
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        pane.add(jlScore, c);

        c.gridx = 0;
        c.gridy = 1;
        pane.add(new JLabel(ii), c);

        c.gridx = 0;
        c.gridy = 2;
        pane.add(new JLabel(name), c);

        return pane;
    }
    
    private void createAllButtonCards(JPanel allCardOfPalyer, HumanPlayer player) {
        int wFull = 60;
        int h = 90;
        Dimension d = new Dimension(wFull / 2, h);

        for (int i = 0; i < 12; i++) {
            ImageIcon ii = ImageController.getHalfImageIcon(player.getHand().get(i), wFull / 2, h);
            JButton btnCard = new JButton(ii);
            btnCard.setPreferredSize(d);
            allCardOfPalyer.add(btnCard);
            listButtonCards.add(btnCard);
        }

        ImageIcon ii = ImageController.getFullImageIcon(player.getHand().get(12), wFull, h);
        JButton btnCard13 = new JButton(ii);
        btnCard13.setPreferredSize(new Dimension(wFull, h));
        allCardOfPalyer.add(btnCard13);
        listButtonCards.add(btnCard13);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Back")) {
            JFrame.setDefaultLookAndFeelDecorated(true);

            //Create and set up the window.
            JFrame frame = new JFrame("Heart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocation(getLocationOnScreen().x, getLocationOnScreen().y);
            JComponent comp = null;
            comp = new Hearts();
            comp.setOpaque(true);
            frame.setContentPane(comp);
            frame.setPreferredSize(new Dimension(getWidth(), getHeight()));
            //Display the window.
            frame.pack();
            frame.setVisible(true);
            JFrame.getFrames()[JFrame.getFrames().length - 2].dispose();
            return;
        }
    }
    
    
    public void updateViewPlayerScore(int score){
        String tmp = String.format("%d", score);
        jlPlayerScore.setText(tmp);
    }

    public void updatePane4Card(ArrayList<Card> listCard){
        int a = playerIndex;
        //con bai cua người chơi
        if(listCard.get(a)!= null){
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), 50, 75);
            jlBottomCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên trái người chơi
        if(listCard.get(a)!=null){
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), 50, 75);
            jlLeftCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người đối diện người chơi
        if(listCard.get(a)!=null){
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), 50, 75);
            jlTopCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên phải người chơi
        if(listCard.get(a)!=null){
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), 50, 75);
            jlRightCard.setIcon(ii);
        }
        
        invalidate();
        repaint();
    }
    
    public void updateAllPlayerScore(ArrayList<Integer> listScores){
        
        jlPlayerScore.setText(String.valueOf(listScores.get(playerIndex)));
        
        jlLeftPlayerScore.setText(String.valueOf(listScores.get((playerIndex + 1) % 4)));
        
        jlTopPlayerScore.setText(String.valueOf(listScores.get((playerIndex + 2) % 4)));
        
        jlRightPlayerScore.setText(String.valueOf(listScores.get((playerIndex + 3) % 4)));
                
        invalidate();
        repaint();
    }

    public void GameStart() {
<<<<<<< HEAD
        State state = (State)SocketController.get_object_from_socket(socket);
        if(state == null)
        {
            JOptionPane.showMessageDialog(null, "Can't read object at socket port: " + socket.getPort());
            return;
        }
        updatePane4Card(state.getCurrentRound());
//        Thread Listen_Thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                State state = (State)SocketController.get_object_from_socket(socket);
//                if(state != null){
//                    updatePane4Card(state.getCurrentRound());
//                }
//            }
//        });
//        Listen_Thread.start();
=======
        Thread Listen_Thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(PlayingFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
                State state = (State)SocketController.get_object_from_socket(socket);
                if(state != null){
                    System.out.println("da nhan");
                    for(Card c: state.getCurrentRound()){
                        if(c!= null)
                            System.out.println(c.getType() + "  " + c.getValue());
                        else
                            System.out.println("null");
                    }
                    updatePane4Card(state.getCurrentRound());
                }
            }
        });
        Listen_Thread.start();
>>>>>>> a1276a86008badac4d680597f25c6ddc7bbfa637
    }
}

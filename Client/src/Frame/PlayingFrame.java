/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.Card;
import Object.CardType;
import Object.Command;
import Object.HumanPlayer;
import Object.ImageController;
import Object.Round;
import Object.SocketController;
import Object.State;
import Object.Value;
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
public class PlayingFrame extends JFrame implements ActionListener {

    Socket socket;
    int score = 0;
    boolean hasHeartsBroken = false;
    List<JButton> listButtonCards = new ArrayList<>();
    int playerIndex;
    HumanPlayer player;
    //Lượt đánh bài
    boolean isMyInnings = false;
    //chiều rộng bài full
    int wFullCard = 60;
    //chiều cao bài
    int hCard = 90;
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
        this.player = player;
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
        this.setPreferredSize(new Dimension(1000, 600));
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
        Dimension d = new Dimension(wFullCard / 2, hCard);

        for (int i = 0; i < 12; i++) {
            ImageIcon ii = ImageController.getHalfImageIcon(player.getHand().get(i), wFullCard / 2, hCard);
            JButton btnCard = new JButton(ii);
            btnCard.setPreferredSize(d);
            btnCard.addActionListener(this);
            btnCard.setActionCommand("Button " + (i + 1));
            allCardOfPalyer.add(btnCard);
            listButtonCards.add(btnCard);
        }

        ImageIcon ii = ImageController.getFullImageIcon(player.getHand().get(12), wFullCard, hCard);
        JButton btnCard = new JButton(ii);
        btnCard.setPreferredSize(new Dimension(wFullCard, hCard));
        btnCard.addActionListener(this);
        btnCard.setActionCommand("Button 13");
        allCardOfPalyer.add(btnCard);
        listButtonCards.add(btnCard);
    }

    //Hàm overrid actionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        String cm = e.getActionCommand();
        if (cm.equals("Back")) {
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
        if (cm.equals("History")) {

        }

        if (isMyInnings) {
            for (int i = 0; i < 13; i++) {
                if (cm.equals("Button " + (i + 1))) {
                    //Wtff
                    Card playedCard = playCardAt(i);
                    SocketController.send_object_to_socket(socket, playedCard);
                    isMyInnings = false;
                    listButtonCards.forEach((t) -> {
                        t.setEnabled(true);
                    });
                }
            }
        }
    }
    
    public void updateViewPlayerScore(int score) {
        String tmp = String.format("%d", score);
        jlPlayerScore.setText(tmp);
    }
    //Cập nhật các lá bài trên bàn
    public void updatePane4Card(ArrayList<Card> listCard) {
        int a = playerIndex;
        //con bai cua người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlBottomCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên trái người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlLeftCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người đối diện người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlTopCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên phải người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlRightCard.setIcon(ii);
        }

        invalidate();
        repaint();
    }
    //Cap nhat so diem cua tat ca nguoi choi
    public void updateAllPlayerScore(ArrayList<Integer> listScores) {

        jlPlayerScore.setText(String.valueOf(listScores.get(playerIndex)));

        jlLeftPlayerScore.setText(String.valueOf(listScores.get((playerIndex + 1) % 4)));

        jlTopPlayerScore.setText(String.valueOf(listScores.get((playerIndex + 2) % 4)));

        jlRightPlayerScore.setText(String.valueOf(listScores.get((playerIndex + 3) % 4)));

        invalidate();
        repaint();
    }
    //cap nhat cai ?????
    //cur là các lá bài sắp theo thứ tự được đánh ra
    public void updateStateOfPaneAllCards(List<Card> cur, int firstPlayerIndex) {
        isMyInnings = true;
        if (player.hasTwoOfClubs()) {
            listButtonCards.forEach((t) -> {
                t.setEnabled(false);
            });
            listButtonCards.get(0).setEnabled(true);
            return;

        }
        Card firstCard = cur.get(firstPlayerIndex);
        List<Card> list = player.getHand();
        int size = list.size();
        Card c = new Card(Value.TWO, CardType.CLUBS);
        if (player.checkType(firstCard.getType())) {
            listButtonCards.forEach((t) -> {
                t.setEnabled(false);
            });
            for (int i = 0; i < size; i++) {
                if (list.get(i).getType() == firstCard.getType()) {
                    listButtonCards.get(13 - size + i).setEnabled(true);
                }
            }
        } else {
            if (!hasHeartsBroken) {
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getType() != CardType.HEARTS) {
                        listButtonCards.get(13 - size + i).setEnabled(true);
                    }
                }
            }
        }

        invalidate();
        repaint();
    }
    // trò chơi bắt đầu
    public void GameStart() {
//        State state = (State) SocketController.get_object_from_socket(socket);
//        updatePane4Card(state.getCurrentRound());
//        updateStateOfPaneAllCards(state.getCurrentRound(), 1);
        Thread Listen_Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    State state = (State) SocketController.get_object_from_socket(socket);
                    
                    //Nếu nhận được gói tin khác rỗng    
                    if (state != null) {
                        System.out.println("da nhan");
//                        for (Card c : state.getCurrentRound()) {
//                            if (c != null) {
//                                System.out.println(c.getType() + "  " + c.getValue());
//                            } else {
//                                System.out.println("null");
//                            }
//                        }
                        //Đọc command để xử lý
                        switch(state.getCommand()){
                            case INIT:
                                System.out.println(player.getName() + " is init game ");
                                updatePane4Card(state.getCurrentRound());
                                break;
                            case UPDATE_SCORE:
                                System.out.println(player.getName() + " is updating all player score ");
                                updateAllPlayerScore(state.getPlayerScores());
                                break;
                            case UPDATE_VIEW:
                                System.out.println(player.getName() + " is updating all card ");
                                updatePane4Card(state.getCurrentRound());
                                break;
                            case PICK_CARD:
                                isMyInnings = true;
                                System.out.println(player.getName() + " is picking card ");
                                if(state.getCurrentRound().size() == 0)
                                    System.out.println(player.getName() + " can pick any card ");
                                else
                                    System.out.println(player.getName() + " can pick card of" +
                                            state.getCurrentRound().get(0).getType());
                        
//                                try {
//                                    //wtff
//    //                                updateStateOfPaneAllCards(state.getCurrentRound(), playerIndex);
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException ex) {
//                                    Logger.getLogger(PlayingFrame.class.getName()).log(Level.SEVERE, null, ex);
//                                }   
                                //Set haveBrokenHeart
                                hasHeartsBroken = state.isHasHeartsBroken();
                                
                                
//                                Card c = pickCard(state.getCurrentRound());
                                updateStateOfPaneAllCards(state.getCurrentRound(), 0);
//                                SocketController.send_object_to_socket(socket, c);
                                break;
                            
                        }
                    }
                }
                
            }
            //cho người chơi chọn card
            private Card pickCard(ArrayList<Card> currentRound) {
                return new Card(Value.TWO, CardType.CLUBS);
            }
        });
        Listen_Thread.start();
    }
    
    //Chơi lá bài thứ i
    private Card playCardAt(int i) {
        int firstIndexOfListButton = 13 - player.getHand().size();
        Card playedCard = player.getHand().get(i - firstIndexOfListButton);
        player.getHand().remove(playedCard);
        listButtonCards.get(firstIndexOfListButton).setVisible(false);
        updateAllButtonCards();
        jlBottomCard.setIcon(ImageController.getFullImageIcon(playedCard, wFullCard, hCard));
        
        //cap nhat GUI
        repaint();
        validate();
        
        return playedCard;
    }
    //Cập nhật các lá bài
    private void updateAllButtonCards() {
        int firstIndexOfListButton = 13 - player.getHand().size();
        List<Card> list = player.getHand();
        for (int i = 0; i < list.size() - 1; i++) {
            listButtonCards.get(firstIndexOfListButton + i)
                    .setIcon(ImageController.getHalfImageIcon(list.get(i), wFullCard / 2, hCard));
        }
        if (list.size() > 1) {
            listButtonCards.get(12)
                    .setIcon(ImageController.getFullImageIcon(list.get(list.size() - 1), wFullCard, hCard));
        }
    }
}

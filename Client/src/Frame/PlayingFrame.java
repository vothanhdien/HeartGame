/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.Card;
import Object.CardType;
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
    State state = null;
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

    public PlayingFrame(Socket s, State state) throws HeadlessException {
        this.socket = s;
        this.state = state;
        Container container = this.getContentPane();
//        System.out.println(player.getName());
        this.setTitle(state.getPlayer().getName());
        //container.add(new Game(player, listNickName));

        jlTopPlayerScore = new JLabel("0");
        jlLeftPlayerScore = new JLabel("0");
        jlRightPlayerScore = new JLabel("0");

        GridBagConstraints c = new GridBagConstraints();
        JPanel pane1 = new JPanel(new GridBagLayout());
        ImageIcon ii = ImageController.getImageByName("person.png", 100, 100);
        JPanel leftPerson = getPanelPerson(state.getNickName().get(1), 0, ii, jlLeftPlayerScore);
        c.gridx = 1;
        c.gridy = 4;
        pane1.add(leftPerson, c);

        JPanel topPerson = getPanelPerson(state.getNickName().get(2), 0, ii, jlTopPlayerScore);
        c.insets = new Insets(0, 200, 30, 200);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 6;
        pane1.add(topPerson, c);
        c.insets = new Insets(0, 0, 30, 0);

        JPanel rightPerson = getPanelPerson(state.getNickName().get(3), 0, ii, jlRightPlayerScore);
        c.gridx = 9;
        c.gridy = 4;
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
        c.gridheight = 3;
        pane1.add(pane4cards, c);
        c.gridheight = 1;

        //So diem cua nguoi choi
        String tmp = String.format("%d", state.getPlayer().getScore());
        jlPlayerScore = new JLabel(tmp);

        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 1;
        pane1.add(jlPlayerScore, c);

        //Cac la bai cua nguoi choi
        JPanel allCardOfPalyer = new JPanel(new GridBagLayout());
        createAllButtonCards(allCardOfPalyer, state.getPlayer());

        c.gridx = 2;
        c.gridy = 6;
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
        GameStart();
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
                    playCardAt(i);
                    isMyInnings = false;
                    listButtonCards.forEach((t) -> {
                        t.setEnabled(true);
                    });
                    break;
                }
            }
        }
    }

    public void updateViewPlayerScore(int score) {
        String tmp = String.format("%d", score);
        jlPlayerScore.setText(tmp);
    }

    public void updatePane4Card(Round currentRound) {
        if (currentRound == null) {
            return;
        }
        jlLeftCard.setVisible(false);
        jlTopCard.setVisible(false);
        jlRightCard.setVisible(false);
        jlBottomCard.setVisible(false);
        int a = state.getPlayerIndex();
        List<Card> listCard = currentRound.getListCard();
        //con bai cua người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlBottomCard.setVisible(true);
            jlBottomCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên trái người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlLeftCard.setVisible(true);
            jlLeftCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người đối diện người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlTopCard.setVisible(true);
            jlTopCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên phải người chơi
        if (listCard.get(a) != null) {
            ImageIcon ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlRightCard.setVisible(true);
            jlRightCard.setIcon(ii);
        }

        invalidate();
        repaint();
    }

    public void updateAllPlayerScore(List<Integer> listScores) {

        jlPlayerScore.setText(String.valueOf(listScores.get(state.getPlayerIndex())));

        jlLeftPlayerScore.setText(String.valueOf(listScores.get((state.getPlayerIndex() + 1) % 4)));

        jlTopPlayerScore.setText(String.valueOf(listScores.get((state.getPlayerIndex() + 2) % 4)));

        jlRightPlayerScore.setText(String.valueOf(listScores.get((state.getPlayerIndex() + 3) % 4)));

        invalidate();
        repaint();
    }

    public void updateStateOfPaneAllCards(Round currentRound) {
        isMyInnings = true;
        if (state.getPlayer().hasTwoOfClubs()) {
            listButtonCards.forEach((t) -> {
                t.setEnabled(false);
            });
            listButtonCards.get(0).setEnabled(true);
            return;
        }
        if (currentRound == null) {
            return;
        }
        CardType firstCardType = currentRound.getRoundType();
        System.out.println(currentRound.getFirstPlayer());
        if (currentRound.getFirstPlayer() == -1) {
            List<Card> list = state.getPlayer().getHand();
            int size = list.size();
            if (!hasHeartsBroken) {
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getType() == CardType.HEARTS) {
                        listButtonCards.get(13 - size + i).setEnabled(false);
                    }
                }
                return;
            }
        }
        List<Card> list = state.getPlayer().getHand();
        int size = list.size();
        if (state.getPlayer().checkType(firstCardType)) {
            listButtonCards.forEach((t) -> {
                t.setEnabled(false);
            });
            for (int i = 0; i < size; i++) {
                if (list.get(i).getType() == firstCardType) {
                    listButtonCards.get(13 - size + i).setEnabled(true);
                }
            }
        } else {
            if (!hasHeartsBroken) {
                listButtonCards.forEach((t) -> {
                    t.setEnabled(false);
                });
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

    private void GameStart() {
        Thread Listen_Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                State temp = new State();
                while (true) {
                    Object info = SocketController.get_object_from_socket(socket);
                    System.out.println(info);
                    if (info != null && info.equals("Your innings came")) {
                        updateStateOfPaneAllCards(state.getCurrentRound());
                    } else {
                        if (info != null) {
                            temp = (State) SocketController.get_object_from_socket(socket);

                            if (info.equals("Update info")) {
                                state.setCurrentRound(temp.getCurrentRound());
                                state.setHasHeartsBroken(temp.isHasHeartsBroken());
                                updatePane4Card(state.getCurrentRound());
                            } else if (info.equals("Update score")) {
                                state.setPlayerScores(temp.getPlayerScores());
                                state.setHasHeartsBroken(temp.isHasHeartsBroken());
                                updateAllPlayerScore(state.getPlayerScores());
                            }
                        }
                    }
                }
            }
        });
        Listen_Thread.start();
    }

    private void playCardAt(int i) {
        int firstIndexOfListButton = 13 - state.getPlayer().getHand().size();
        Card playedCard = state.getPlayer().getHand().get(i - firstIndexOfListButton);
        SocketController.send_object_to_socket(socket, playedCard);
        state.getPlayer().getHand().remove(playedCard);
        listButtonCards.get(firstIndexOfListButton).setVisible(false);
        updateAllButtonCards();
        jlBottomCard.setIcon(ImageController.getFullImageIcon(playedCard, wFullCard, hCard));
        repaint();
        validate();
    }

    private void updateAllButtonCards() {
        int firstIndexOfListButton = 13 - state.getPlayer().getHand().size();
        List<Card> list = state.getPlayer().getHand();
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

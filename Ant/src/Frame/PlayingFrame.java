/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.*;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sun.awt.image.SunWritableRaster;

/**
 *
 * @author HP
 */
public class PlayingFrame extends JFrame implements ActionListener {

    Socket socket;
    int score = 0;
    List<String> howToExchangeCard = Arrays.asList("No pass", "Pass left", "Pass right", "Pass across");
    List<JButton> listButtonCards = new ArrayList<>();
    List<Integer> listCardExchange = new ArrayList<>();
    State state = null;
    //Lượt đánh bài của người chơi
    boolean isMyInnings = false;

    //có phải là đang chọn bài để đổi
    boolean isSwitching = false;

    //chiều rộng bài full
    int wFullCard = 60;
    //chiều cao bài
    int hCard = 90;
    //component

    JLabel jlHeart;
    JLabel jlPlayerScore;
    JLabel jlTopPlayerScore;
    JLabel jlLeftPlayerScore;
    JLabel jlRightPlayerScore;

    JLabel jlTopCard;
    JLabel jlRightCard;
    JLabel jlBottomCard;
    JLabel jlLeftCard;

    JLabel jlTopArrow;
    JLabel jlRightArrow;
    JLabel jlBottomArrow;
    JLabel jlLeftArrow;

    JButton jbExchange;
    //ket qua

    int indexRound = 1;

    String winners = "";
    JPanel jpResult;

    public PlayingFrame(Socket s, State state) throws HeadlessException {
        this.socket = s;
        this.state = state;
        jpResult = new JPanel(new GridLayout(0, 4));
        for (String str : state.getNickName()) {
//            result = result.concat("\t" + str + " \t|");
            JLabel tmp = new JLabel("<html>"
                    + "<div style=\"text-align: center;\">"
                    + str
                    + "</div></html>");
            tmp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jpResult.add(tmp);
        };

        state.setNickName(arrageListNickName(state.getNickName(), state.getPlayerIndex()));

        Container container = this.getContentPane();
        this.setTitle(state.getPlayer().getName());

        jlTopPlayerScore = new JLabel("0");
        jlLeftPlayerScore = new JLabel("0");
        jlRightPlayerScore = new JLabel("0");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        JPanel panel = new JPanel(new GridBagLayout());
        ImageIcon ii = ImageController.getImageByName("person.png", 100, 100);
        JPanel leftPerson = getPanelPerson(state.getNickName().get(1), 0, ii, jlLeftPlayerScore);
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(leftPerson, c);

        JPanel topPerson = getPanelPerson(state.getNickName().get(2), 0, ii, jlTopPlayerScore);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 1;
        panel.add(topPerson, c);

        JPanel rightPerson = getPanelPerson(state.getNickName().get(3), 0, ii, jlRightPlayerScore);
        c.gridx = 5;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(rightPerson, c);

        ii = ImageController.getImageByName("brokeHeart.png", 50, 50);
        jlHeart = new JLabel(ii);
        jlHeart.setEnabled(false);
        c.gridx = 6;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(jlHeart, c);

        ii = ImageController.getImageByName("arrowToRight.png", 30, 30);
        jlTopArrow = new JLabel(ii);
        jlTopArrow.setEnabled(false);
        ii = ImageController.getImageByName("arrowToLeft.png", 30, 30);
        jlRightArrow = new JLabel(ii);
        jlRightArrow.setEnabled(false);
        ii = ImageController.getImageByName("arrowToLeft.png", 30, 30);
        jlBottomArrow = new JLabel(ii);
        jlBottomArrow.setEnabled(false);
        ii = ImageController.getImageByName("arrowToRight.png", 30, 30);
        jlLeftArrow = new JLabel(ii);
        jlLeftArrow.setEnabled(false);

        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(jlTopArrow, c);

        c.gridx = 6;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(jlRightArrow, c);

        c.gridx = 5;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(jlBottomArrow, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        panel.add(jlLeftArrow, c);

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

        ii = ImageController.getImageByName("question.png", 30, 30);
        JButton btnHelp = new JButton(ii);
        btnHelp.addActionListener(this);
        btnHelp.setActionCommand("Help");
        btnHelp.setBorderPainted(false);
        btnHelp.setContentAreaFilled(false);
        btnHelp.setPreferredSize(new Dimension(30, 30));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(btnBack, c);

        c.gridx = 6;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(btnHistory, c);

        c.gridx = 7;
        c.gridy = 0;
        c.gridwidth = 1;
        panel.add(btnHelp, c);

        GridBagConstraints c1 = new GridBagConstraints();
        //nơi 4 lá bài được đánh ra
        JPanel pane4cards = new JPanel(new GridBagLayout());

        ii = ImageController.getImageByName("53.png", wFullCard, hCard);
        jlLeftCard = new JLabel(ii);
        jlTopCard = new JLabel(ii);
        jlRightCard = new JLabel(ii);
        jlBottomCard = new JLabel(ii);
        c1.gridx = 0;
        c1.gridy = 1;
        c.gridwidth = 1;
        c1.gridheight = 2;
        pane4cards.add(jlLeftCard, c1);

        c1.gridx = 1;
        c1.gridy = 0;
        pane4cards.add(jlTopCard, c1);

        c1.gridx = 2;
        c1.gridy = 1;
        pane4cards.add(jlRightCard, c1);

        c1.gridx = 1;
        c1.gridy = 2;
        pane4cards.add(jlBottomCard, c1);

        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 3;
        c.gridheight = 3;
        c.insets = new Insets(20, 150, 20, 150);
        panel.add(pane4cards, c);
        c.insets = new Insets(5, 5, 5, 5);

        //So diem cua nguoi choi
        String tmp = String.format("%d", state.getPlayer().getScore());
        jlPlayerScore = new JLabel(tmp);

        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        panel.add(jlPlayerScore, c);

        //nut doi bai
        jbExchange = new JButton("Pass left");
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 3;
        c.gridheight = 1;
        panel.add(jbExchange, c);
        jbExchange.setActionCommand("Button exchange");
        jbExchange.addActionListener(this);

        //Cac la bai cua nguoi choi
        JPanel allCardOfPalyer = new JPanel(new GridBagLayout());
        createAllButtonCards(allCardOfPalyer, (HumanPlayer) state.getPlayer());

        c.gridx = 2;
        c.gridy = 7;
        c.gridwidth = 3;
        c.gridheight = 1;
        panel.add(allCardOfPalyer, c);

        container.add(panel);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new MainFrame();
            }
        });
        this.setPreferredSize(new Dimension(1100, 700));
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

    private void updateArrow() {
        jlTopArrow.setEnabled(false);
        jlRightArrow.setEnabled(false);
        jlBottomArrow.setEnabled(false);
        jlLeftArrow.setEnabled(false);
        int x = 30;
        if (isMyInnings) {
            jlBottomArrow.setEnabled(true);
            ImageIcon ii = ImageController.getImageByName("arrowToLeft.png", x, x);
            jlBottomArrow.setIcon(ii);
            return;
        }
        if (state == null) {
            return;
        }
        int iPlayerPlaying = (state.getIPlayPlaying() - state.getPlayerIndex() + 4) % 4;
        if (iPlayerPlaying == 0) {
            jlBottomArrow.setEnabled(true);
            ImageIcon ii = ImageController.getImageByName("arrowToLeft.png", x, x);
            jlBottomArrow.setIcon(ii);
        } else if (iPlayerPlaying == 1) {
            jlLeftArrow.setEnabled(true);
            ImageIcon ii = ImageController.getImageByName("arrowToRight.png", x, x);
            jlLeftArrow.setIcon(ii);
        } else if (iPlayerPlaying == 2) {
            jlTopArrow.setEnabled(true);
            ImageIcon ii = ImageController.getImageByName("arrowToRight.png", x, x);
            jlTopArrow.setIcon(ii);
        } else if (iPlayerPlaying == 3) {
            jlRightArrow.setEnabled(true);
            ImageIcon ii = ImageController.getImageByName("arrowToLeft.png", x, x);
            jlRightArrow.setIcon(ii);
        }
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
            MainFrame mf = new MainFrame();
            dispose();
            return;
        }
        if (cm.equals("History")) {
            JOptionPane.showConfirmDialog(null, jpResult, "Result", JOptionPane.YES_NO_OPTION);
        }
        if (cm.equals("Help")) {
            try {
                String text = new String(Files.readAllBytes(Paths.get("help.txt")), StandardCharsets.UTF_8);
                JScrollPane jsp = new JScrollPane();

                JOptionPane.showMessageDialog(null, text, "Help", 1);
            } catch (IOException ex) {
                Logger.getLogger(PlayingFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (isMyInnings) {
            for (int i = 0; i < 13; i++) {
                if (cm.equals("Button " + (i + 1))) {
                    isMyInnings = false;
                    listButtonCards.forEach((t) -> {
                        t.setEnabled(true);
                    });
                    playCardAt(i);
                    break;
                }
            }
        }
        if (isSwitching) {
            if (cm.equals("Button exchange")) {
                //do something
                if (listCardExchange.size() < 3) {
                    JOptionPane.showMessageDialog(null, "Choose at lease 3 cards");
                } else {
                    SocketController.send_object_to_socket(socket, listCardExchange);
                    jbExchange.setEnabled(false);
                    isSwitching = false;
//                    listButtonCards.forEach((t) -> {
//                        t.setEnabled(true);
//                    });
                }

            } else {
                for (int i = 0; i < 13; i++) {
                    if (cm.equals("Button " + (i + 1))) {

                        chooseCardToExchange(i);

                        break;
                    }
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
        ImageIcon ii = ImageController.getImageByName("53.png", wFullCard, hCard);
        jlLeftCard.setIcon(ii);
        jlTopCard.setIcon(ii);
        jlRightCard.setIcon(ii);
        jlBottomCard.setIcon(ii);
        int a = state.getPlayerIndex();
        List<Card> listCard = currentRound.getListCard();
        //con bai cua người chơi
        if (listCard.get(a) != null) {
            ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlBottomCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên trái người chơi
        if (listCard.get(a) != null) {
            ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlLeftCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người đối diện người chơi
        if (listCard.get(a) != null) {
            ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
            jlTopCard.setIcon(ii);
        }
        a = (a + 1) % 4;
        //con bài của người bên phải người chơi
        if (listCard.get(a) != null) {
            ii = ImageController.getFullImageIcon(listCard.get(a), wFullCard, hCard);
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

    public void updateStateOfAllPaneCards(Round currentRound) {
        isMyInnings = true;
        //khóa hết
        listButtonCards.forEach((t) -> {
            t.setEnabled(false);
        });

        //có 2 chuồn -> đánh 2 chuồn
        if (state.getPlayer().hasTwoOfClubs()) {
            listButtonCards.get(0).setEnabled(true);
            return;
        }

        CardType firstCardType = currentRound.getRoundType();
        List<Card> list = state.getPlayer().getHand();
        int size = list.size();

        if (firstCardType == null) {
            for (int i = 0; i < size; i++) {
                //xét con cơ
                if ((list.get(i).getType() == CardType.HEARTS && state.isHasHeartsBroken())
                        || state.getPlayer().hasAllHeart()) {
                    listButtonCards.get(13 - size + i).setEnabled(true);
                } else if (list.get(i).getType() != CardType.HEARTS) {// những con bài khác
                    listButtonCards.get(13 - size + i).setEnabled(true);
                }
            }
        } else { // trường hợp đánh theo
            if (state.getPlayer().checkType(firstCardType)) {// nếu có con để đánh
                for (int i = 0; i < size; i++) {
                    if (list.get(i).getType() == firstCardType) {
                        listButtonCards.get(13 - size + i).setEnabled(true);
                    }
                }
            } else// nếu không có con để dánh
            {
                for (int i = 0; i < size; i++) {
                    //quân cơ không được chơi khi người chơi còn 13 lá
                    if (list.get(i).getType() == CardType.HEARTS && size < 13) {
                        listButtonCards.get(13 - size + i).setEnabled(true);
                    } else if (list.get(i).getType() != CardType.HEARTS) {//Quân khác
                        listButtonCards.get(13 - size + i).setEnabled(true);
                    }
                }
            }
        }

        invalidate();
        repaint();
    }

    private void playCardAt(int i) {
        int firstIndexOfListButton = 13 - state.getPlayer().getHand().size();
        Card playedCard = state.getPlayer().getHand().get(i - firstIndexOfListButton);
        //Loại card ra khỏi hand
        state.getPlayer().getHand().remove(playedCard);
        listButtonCards.get(firstIndexOfListButton).setVisible(false);
        jlBottomCard.setIcon(ImageController.getFullImageIcon(playedCard, wFullCard, hCard));
        updateAllButtonCards();
        SocketController.send_object_to_socket(socket, playedCard);

        repaint();
        validate();
    }

    private void chooseCardToExchange(int index) {

        listCardExchange.add(index);
        listButtonCards.get(index).setEnabled(false);

        //Trường hợp chọn lá bài thứ 4 -> xóa lá bài đầu tiên
        if (listCardExchange.size() > 3) {
            int i = listCardExchange.get(0);
            listCardExchange.remove(0);

            listButtonCards.get(i).setEnabled(true);
        }
        repaint();
        validate();

    }

    //Cập nhật hình ảnh cho toàn bộ các botton
    private void updateAllButtonCards() {
        int firstIndexOfListButton = 13 - state.getPlayer().getHand().size();
        if (firstIndexOfListButton == 0) {
            listButtonCards.forEach((t) -> {
                t.setVisible(true);
                t.setEnabled(true);
            });
        }
        List<Card> list = state.getPlayer().getHand();
        for (int i = 0; i < list.size() - 1; i++) {
            listButtonCards.get(firstIndexOfListButton + i)
                    .setIcon(ImageController.getHalfImageIcon(list.get(i), wFullCard / 2, hCard));
        }
        if (list.size() > 0) {
            listButtonCards.get(12)
                    .setIcon(ImageController.getFullImageIcon(list.get(list.size() - 1), wFullCard, hCard));
        }
    }

    private void ShowResult(List<Integer> playerScores) {
        int index = state.getPlayerIndex();
        for (int i = 0; i < playerScores.size(); i++) {
            JLabel tmp = new JLabel("<html>"
                    + "<div style='text-align: center;'>"
                    + playerScores.get(i).toString()
                    + "</div></html>");
            jpResult.add(tmp);
        }

        JOptionPane.showConfirmDialog(null, jpResult, "Result", JOptionPane.YES_NO_OPTION);
    }

    private List<String> arrageListNickName(List<String> listNickName, int playerIndex) {
        List<String> kq = new ArrayList<>();
        int a = playerIndex;
        for (int i = 0; i < listNickName.size(); i++) {
            kq.add(listNickName.get(a % 4));
            a++;
        }
        return kq;
    }

    private void GameStart() {
        Thread Listen_Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                State receive_state = new State();
                while (true) {
                    receive_state = (State) SocketController.get_object_from_socket(socket);
                    switch (receive_state.getCommand()) {
                        case GAME_OVER:
                            state.setWinners(receive_state.getWinnners());
                            state.getWinnners().forEach((t) -> {
                                winners += state.getNickName().get(t) + "\n";
                            });
                            JOptionPane.showMessageDialog(null, "Game over, winners:\n" + winners);
                            return;
                        case INIT:
                            //do somethings
                            jbExchange.setText(howToExchangeCard.get(indexRound % 4));
                            jlHeart.setEnabled(false);
                            state = receive_state;
                            updateAllButtonCards();
                            break;
                        case SHOW_RESULT:
                            indexRound++;
                            ShowResult(receive_state.getPlayerScores());
                            updatePane4Card(state.getCurrentRound());
                            break;
                        case EXCHANGE_CARD:
                            //do somethings
                            listCardExchange.removeAll(listCardExchange);
                            jbExchange.setEnabled(true);
                            isMyInnings = false;
                            isSwitching = true;
                            break;
                        case PICK_CARD:
                            state.setHasHeartsBroken(receive_state.isHasHeartsBroken());
                            updateStateOfAllPaneCards(state.getCurrentRound());
                            updateArrow();
                            break;

                        case UPDATE_SCORE:
//                            state.setCurrentRound(receive_state.getCurrentRound());
                            state.setPlayerScores(receive_state.getPlayerScores());
                            state.setIPlayPlaying(receive_state.getCurrentRound().getFirstPlayer());
//                            state.setHasHeartsBroken(receive_state.isHasHeartsBroken());
                            updateAllPlayerScore(state.getPlayerScores());
                            updateArrow();
                            break;

                        case UPDATE_VIEW:
                            state.setCurrentRound(receive_state.getCurrentRound());
                            state.setHasHeartsBroken(receive_state.isHasHeartsBroken());
                            state.setIPlayPlaying(receive_state.getIPlayPlaying());
                            state.setPlayerScores(receive_state.getPlayerScores());
                            if (state.isHasHeartsBroken()) {
                                jlHeart.setEnabled(true);
                            }
                            updatePane4Card(state.getCurrentRound());
                            updateAllPlayerScore(state.getPlayerScores());
                            updateArrow();
                            break;
                        case SOCKET_CLOSED:
                            JOptionPane.showMessageDialog(null, "Sory, " + state.getNickName().get(state.getIPlayPlaying())
                                    + " disconected. Game is stopped.");
                            return;
                        case SERVER_STOPPED:
                            JOptionPane.showMessageDialog(null, "Sorry, server is stopped!");
                            return;
                    }
                }
            }
        });
        Listen_Thread.start();
    }
}

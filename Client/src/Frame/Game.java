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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author DaDa Wind
 */
public class Game extends JPanel implements ActionListener {

    List<Card> listCards = new ArrayList<>();
    int score = 0;
    boolean hasHeartsBroken = false;
    List<JButton> listButtonCards = new ArrayList<>();

    public Game(HumanPlayer player, List<String> listNickName) {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane1 = new JPanel(new GridBagLayout());
        ImageIcon ii = ImageController.getImageByName("person.png", 100, 100);
        JPanel leftPerson = getPanelPerson(listNickName.get(0), 0, ii);
        c.gridx = 1;
        c.gridy = 2;
        pane1.add(leftPerson, c);
        
        JPanel topPerson = getPanelPerson(listNickName.get(1), 0, ii);
        c.insets = new Insets(0, 200, 30, 200);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 6;
        pane1.add(topPerson, c);
        c.insets = new Insets(0, 0, 30, 0);

        JPanel rightPerson = getPanelPerson(listNickName.get(2), 0, ii);
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
//        ii = ImageController.getFullImageIcon(round.getCardAt(0), 50, 75);
//        JLabel leftCard = new JLabel(ii);
//        ii = ImageController.getFullImageIcon(round.getCardAt(1), 50, 75);
//        JLabel topCard = new JLabel(ii);
//        ii = ImageController.getFullImageIcon(round.getCardAt(2), 50, 75);
//        JLabel rightCard = new JLabel(ii);
//        ii = ImageController.getFullImageIcon(round.getCardAt(3), 50, 75);
//        JLabel bottomCard = new JLabel(ii);
//        c1.gridx = 0;
//        c1.gridy = 1;
//        c1.gridheight = 2;
//        pane4cards.add(leftCard, c1);
//
//        c1.gridx = 1;
//        c1.gridy = 0;
//        c1.gridheight = 2;
//        pane4cards.add(topCard, c1);
//
//        c1.gridx = 2;
//        c1.gridy = 1;
//        c1.gridheight = 2;
//        pane4cards.add(rightCard, c1);
//
//        c1.gridx = 1;
//        c1.gridy = 2;
//        c1.gridheight = 2;
//        pane4cards.add(bottomCard, c1);

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 6;
        pane1.add(pane4cards, c);

        JPanel allCardOfPalyer = new JPanel(new GridBagLayout());

        createAllButtonCards(allCardOfPalyer, player);

        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 6;
        pane1.add(allCardOfPalyer, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(pane1, c);
    }

    private JPanel getPanelPerson(String name, int score, ImageIcon ii) {
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        pane.add(new JLabel(String.valueOf(score)), c);

        c.gridx = 0;
        c.gridy = 1;
        pane.add(new JLabel(ii), c);

        c.gridx = 0;
        c.gridy = 2;
        pane.add(new JLabel(name), c);

        return pane;
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

    private void createAllButtonCards(JPanel allCardOfPalyer, HumanPlayer player) {
        Dimension d = new Dimension(30, 75);

        for (int i = 0; i < 12; i++) {
            ImageIcon ii = ImageController.getHalfImageIcon(player.getHand().get(i), 25, 75);
            System.out.println(player.getHand().get(i).getValue() + " " + player.getHand().get(i).getType());
            JButton btnCard = new JButton(ii);
            btnCard.setPreferredSize(d);
            allCardOfPalyer.add(btnCard);
            listButtonCards.add(btnCard);
        }

        ImageIcon ii = ImageController.getFullImageIcon(player.getHand().get(12), 50, 75);
        JButton btnCard13 = new JButton(ii);
        btnCard13.setPreferredSize(new Dimension(50, 75));
        allCardOfPalyer.add(btnCard13);
        listButtonCards.add(btnCard13);
    }
}

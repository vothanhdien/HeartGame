/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Object.Card;
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
    int	score = 0;
    boolean hasHeartsBroken = false;
    HumanPlayer player = new HumanPlayer("");
    
    public Game()
    {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel pane1 = new JPanel(new GridBagLayout());
        ImageIcon ii = getImageIcon("person.png", 100, 100);
        JPanel leftPerson = getPanelPerson("Left person", 0, ii);
        c.gridx = 1;
        c.gridy = 2;
        pane1.add(leftPerson, c);

        JPanel topPerson = getPanelPerson("Top person", 0, ii);
        c.insets = new Insets(0, 200, 30, 200);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 6;
        pane1.add(topPerson, c);
        c.insets = new Insets(0, 0, 30, 0);
        
        JPanel rightPerson = getPanelPerson("Right person", 0, ii);
        c.gridx = 9;
        c.gridy = 2;
        c.gridwidth = 1;
        pane1.add(rightPerson, c);
        
        ii = getImageIcon("back.png", 30, 30);
        JButton btnBack = new JButton(ii);
        btnBack.addActionListener(this);
        btnBack.setActionCommand("Back");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setPreferredSize(new Dimension(30, 30));
        
        ii = getImageIcon("history.png", 30, 30);
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
        ii = getImageIcon("2Co.gif", 50, 75);
        JLabel leftCard = new JLabel(ii);
        JLabel topCard = new JLabel(ii);
        JLabel rightCard = new JLabel(ii);
        JLabel bottomCard = new JLabel(ii);
        c1.gridx = 0;
        c1.gridy = 1;
        c1.gridheight = 2;
        pane4cards.add(leftCard, c1);
        
        c1.gridx = 1;
        c1.gridy = 0;
        c1.gridheight = 2;
        pane4cards.add(topCard, c1);
        
        c1.gridx = 2;
        c1.gridy = 1;
        c1.gridheight = 2;
        pane4cards.add(rightCard, c1);
        
        c1.gridx = 1;
        c1.gridy = 2;
        c1.gridheight = 2;
        pane4cards.add(bottomCard, c1);
        
        
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 6;
        pane1.add(pane4cards, c);
        
        JPanel allCardOfPalyer = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        ii = getImageIcon("2Co.gif", 50, 75);
        Dimension d = new Dimension(30, 75);
        
        JButton btnCard1 = new JButton(ii);
        btnCard1.setPreferredSize(d);
        allCardOfPalyer.add(btnCard1);
        
        JButton btnCard2 = new JButton(ii);
        btnCard2.setPreferredSize(d);
        allCardOfPalyer.add(btnCard2);
        
        JButton btnCard3 = new JButton(ii);
        btnCard3.setPreferredSize(d);
        allCardOfPalyer.add(btnCard3);
        
        JButton btnCard4 = new JButton(ii);
        btnCard4.setPreferredSize(d);
        allCardOfPalyer.add(btnCard4);
        
        JButton btnCard5 = new JButton(ii);
        btnCard5.setPreferredSize(d);
        allCardOfPalyer.add(btnCard5);
        
        JButton btnCard6 = new JButton(ii);
        btnCard6.setPreferredSize(d);
        allCardOfPalyer.add(btnCard6);
        
        JButton btnCard7 = new JButton(ii);
        btnCard7.setPreferredSize(d);
        allCardOfPalyer.add(btnCard7);
        
        JButton btnCard8 = new JButton(ii);
        btnCard8.setPreferredSize(d);
        allCardOfPalyer.add(btnCard8);
        
        JButton btnCard9 = new JButton(ii);
        btnCard9.setPreferredSize(d);
        allCardOfPalyer.add(btnCard9);
        
        JButton btnCard10 = new JButton(ii);
        btnCard10.setPreferredSize(d);
        allCardOfPalyer.add(btnCard10);
        
        JButton btnCard11 = new JButton(ii);
        btnCard11.setPreferredSize(d);
        allCardOfPalyer.add(btnCard11);
        
        JButton btnCard12 = new JButton(ii);
        btnCard12.setPreferredSize(d);
        allCardOfPalyer.add(btnCard12);
        
        JButton btnCard13 = new JButton(getImageIcon("2Co.gif", 50, 75));
        btnCard13.setPreferredSize(new Dimension(50, 75));
        allCardOfPalyer.add(btnCard13);
        
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 6;
        pane1.add(allCardOfPalyer, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(pane1, c);
    }
    
    private ImageIcon getImageIcon(String name, int width, int height)
    {
        ImageIcon temp = new ImageIcon("src//images//" + name);
        Image i = temp.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(i);
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
        if(e.getActionCommand().equals("Back"))
        {
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
}

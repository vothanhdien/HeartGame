/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.HumanPlayer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author DaDa Wind
 */
public class Hearts extends JPanel implements ActionListener {

    /**
     * @param args the command line arguments
     */
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final int X = 200;
    private static final int Y = 50;
    private static JButton btnNewGame;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        createAndShowGUI();
//        try {
//            Socket s = new Socket("localhost", 3200);
//
//            OutputStream os = s.getOutputStream();
//            ObjectOutputStream oos = new ObjectOutputStream(os);
//            
//            InputStream is = s.getInputStream();
//            ObjectInputStream ois = new ObjectInputStream(is);
//            
//            HumanPlayer p = new HumanPlayer("Client 1");
//            oos.flush();
//            
//            oos.writeObject(p);
//            oos.flush();
//            
//            HumanPlayer p1 = (HumanPlayer) ois.readObject();
//            p1.sortHand();
//            for (int i = 0; i < 13; i++) {
//                System.out.println(p1.getHand().get(i).getValue() + " " + p1.getHand().get(i).getType());
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(Hearts.class.getName()).log(Level.SEVERE, null, ex);
//        }
        createAndShowGUI();
        btnNewGame.doClick();
    }

    public Hearts() {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        btnNewGame = new JButton("New game");
        btnNewGame.addActionListener(this);
        btnNewGame.setActionCommand("New game");

        c.gridx = 0;
        c.gridy = 0;
        add(btnNewGame, c);

        JButton btnResume = new JButton("Resume");
        btnResume.addActionListener(this);
        btnResume.setActionCommand("Resume");

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        add(btnResume, c);

        JButton btnStatistics = new JButton("Statistics");
        btnStatistics.addActionListener(this);
        btnStatistics.setActionCommand("Statistics");

        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        add(btnStatistics, c);

        JButton btnHelp = new JButton("Help");
        btnHelp.addActionListener(this);
        btnHelp.setActionCommand("Help");

        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        add(btnHelp, c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().beep();
        JFrame.getFrames()[JFrame.getFrames().length - 1].dispose();
        JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        JFrame frame = new JFrame("Heart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(X, Y);
        //JComponent comp = null;
        if (e.getActionCommand().equals("New game")) {
            LoginFrame lg = new LoginFrame();
        } else if (e.getActionCommand().equals("Resume")) {

        } else if (e.getActionCommand().equals("Statistics")) {

        } else if (e.getActionCommand().equals("Help")) {

        }

//        
//        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
        return;
    }

    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("Hearts");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(X, Y);
        JComponent comp = new Hearts();
        comp.setOpaque(true);
        frame.setContentPane(comp);
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

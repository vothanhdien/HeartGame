/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.*;
import Object.HumanPlayer;
import java.awt.*;
//
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.*;

/**
 *
 * @author DaDa Wind
 */
public class Hearts extends JPanel implements ActionListener {

    /**
     * @param args the command line arguments
     */
    private static final String PLAY_WITH_HUMAN = "Play with human";
    private static final String PLAY_WITH_AIPLAYER = "Play with AIPlayer";
    private static final String STATICTIS = "Statictis";
    private static final String HELP = "Help";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final int X = 200;
    private static final int Y = 50;
    private static JButton btnPlayVsHuman;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        createAndShowGUI();
        //btnPlayVsHuman.doClick();
    }

    public Hearts() {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        btnPlayVsHuman = new JButton(PLAY_WITH_HUMAN);
        btnPlayVsHuman.addActionListener(this);
        btnPlayVsHuman.setActionCommand(PLAY_WITH_HUMAN);

        c.gridx = 0;
        c.gridy = 0;
        add(btnPlayVsHuman, c);

        JButton btnPlayVsAI = new JButton(PLAY_WITH_AIPLAYER);
        btnPlayVsAI.addActionListener(this);
        btnPlayVsAI.setActionCommand(PLAY_WITH_AIPLAYER);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        add(btnPlayVsAI, c);

        JButton btnStatistics = new JButton(STATICTIS);
        btnStatistics.addActionListener(this);
        btnStatistics.setActionCommand(STATICTIS);

        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        add(btnStatistics, c);

        JButton btnHelp = new JButton(HELP);
        btnHelp.addActionListener(this);
        btnHelp.setActionCommand(HELP);

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
        if (e.getActionCommand().equals(PLAY_WITH_HUMAN)) {
            LoginFrame lg = new LoginFrame(4);
        } else if (e.getActionCommand().equals(PLAY_WITH_AIPLAYER)) {
            String temp = JOptionPane.showInputDialog(this, "Number of players", "Input",
                    JOptionPane.QUESTION_MESSAGE);
            int numberOfPlayer = 0;
            numberOfPlayer = Integer.valueOf(temp);
            
            if (numberOfPlayer > 0 && numberOfPlayer < 4) {
                new LoginFrame(numberOfPlayer);
            }

        } else if (e.getActionCommand().equals(STATICTIS)) {

        } else if (e.getActionCommand().equals(HELP)) {

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

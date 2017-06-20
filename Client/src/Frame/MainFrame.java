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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author DaDa Wind
 */
public class MainFrame extends JFrame implements ActionListener {

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
    public MainFrame() {
        this.setTitle("Hearts");
        Container container = this.getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        btnPlayVsHuman = new JButton(PLAY_WITH_HUMAN);
        btnPlayVsHuman.addActionListener(this);
        btnPlayVsHuman.setActionCommand(PLAY_WITH_HUMAN);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(btnPlayVsHuman, c);

        JButton btnPlayVsAI = new JButton(PLAY_WITH_AIPLAYER);
        btnPlayVsAI.addActionListener(this);
        btnPlayVsAI.setActionCommand(PLAY_WITH_AIPLAYER);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        container.add(btnPlayVsAI, c);


        JButton btnHelp = new JButton(HELP);
        btnHelp.addActionListener(this);
        btnHelp.setActionCommand(HELP);

        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 0, 0);
        container.add(btnHelp, c);
        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(X, Y);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals(PLAY_WITH_HUMAN)) {
             String temp = JOptionPane.showInputDialog(this, "Number of players", "Input",
                    JOptionPane.QUESTION_MESSAGE);
            int numberOfPlayer = 0;
            if(temp != null && !"".equals(temp)){
                numberOfPlayer = Integer.valueOf(temp);
                if (numberOfPlayer > 1 && numberOfPlayer <= 4) {
                    new LoginFrame(numberOfPlayer);
                }
                else
                    new LoginFrame(4);
                dispose();
            }
        } else if (e.getActionCommand().equals(PLAY_WITH_AIPLAYER)) {
            LoginFrame lg = new LoginFrame(1);
            dispose();
           
        } else if (e.getActionCommand().equals(STATICTIS)) {

        } else if (e.getActionCommand().equals(HELP)) {
            try {
                String text = new String(Files.readAllBytes(Paths.get("help.txt")), StandardCharsets.UTF_8);
//                System.out.println(text);

                JOptionPane.showMessageDialog(null, text, "Help", 1);
            } catch (IOException ex) {
                Logger.getLogger(PlayingFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return;
    }
}

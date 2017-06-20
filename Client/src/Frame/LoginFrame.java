/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.HumanPlayer;
import Object.SocketController;
import Object.State;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class LoginFrame extends JFrame {

    JTextField jtfName;
    JTextField jtfServer;
    JTextField jtfPort;
    JProgressBar jpbWaiting;
    JLabel jlmsg;
    State state = null;
    int number = 4;
//    JDialog waitingdlg;
    public LoginFrame(int number) throws HeadlessException {
        this.setTitle("login to server");

        this.number = number;
        Container container = this.getContentPane();

        container.setLayout(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(2, 2, 2, 2);

        //lable
        JLabel jlName = new JLabel("NickName: ");
        JLabel jlServer = new JLabel("Server: ");
        JLabel jlPort = new JLabel("Port: ");

        //Text field
        jtfName = new JTextField(15);
        jtfName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    login();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        jtfServer = new JTextField(15);
        jtfServer.setText("127.0.0.1");
        jtfPort = new JTextField(15);
        jtfPort.setText("3200");

        //button
        JButton jbConnect = new JButton("Connect");
        jbConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = jtfName.getText();
                String server = jtfServer.getText();
                int port = Integer.parseInt(jtfPort.getText());
                if (name.isEmpty() || server.isEmpty() || port < 0) {
                    JOptionPane.showMessageDialog(null, "Fill all blank");
                    return;
                }
                ShowProgressDialog();
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }).start();
            }
        });
        JButton jbExit = new JButton("Cancel");
        jbExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainFrame mf = new MainFrame();
                dispose();
                
            }
        });

        //add to container
        //thêm vào JPanel
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        container.add(jlName, constraint);

        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.gridwidth = 2;
        container.add(jtfName, constraint);

        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 1;
        container.add(jlServer, constraint);

        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.gridwidth = 2;
        container.add(jtfServer, constraint);

        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth = 1;
        container.add(jlPort, constraint);

        constraint.gridx = 1;
        constraint.gridy = 2;
        constraint.gridwidth = 2;
        container.add(jtfPort, constraint);

        constraint.gridx = 1;
        constraint.gridy = 3;
        constraint.gridwidth = 1;
        container.add(jbConnect, constraint);

        constraint.gridx = 2;
        constraint.gridy = 3;
        constraint.gridwidth = 1;
        container.add(jbExit, constraint);

        //Thong bao doi nguoi choi
        jlmsg = new JLabel("Waiting another player");
//        jlmsg.setVisible(false);
        jpbWaiting = new JProgressBar();
        jpbWaiting.setIndeterminate(true);

        constraint.gridx = 1;
        constraint.gridy = 4;
        constraint.gridwidth = 1;
        container.add(jlmsg, constraint);

        constraint.gridx = 1;
        constraint.gridy = 5;
        constraint.gridwidth = 1;
        container.add(jpbWaiting, constraint);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
        jpbWaiting.setVisible(false);
        jlmsg.setVisible(false);
    }

    private void login() {
        String name = jtfName.getText();
        String server = jtfServer.getText();
        int port = Integer.parseInt(jtfPort.getText());
        try {

            Socket socket = new Socket(server, port);
            HumanPlayer hp = new HumanPlayer(name);
            SocketController.send_object_to_socket(socket, number);
            SocketController.send_object_to_socket(socket, hp);

            state = (State) SocketController.get_object_from_socket(socket);
            hp = (HumanPlayer) state.getPlayer();

//            state.setNickName(arrageListNickName(state.getNickName(), state.getPlayerIndex()));
            PlayingFrame playingFrame = new PlayingFrame(socket, state);
            dispose();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void ShowProgressDialog() {

        final JDialog waitingdlg = new JDialog(this, "Waiting another player", true);
        JProgressBar dpb = new JProgressBar(0);
        waitingdlg.add(BorderLayout.CENTER, dpb);
        waitingdlg.add(BorderLayout.NORTH, new JLabel("Waiting another player..."));
//        dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitingdlg.setLocationRelativeTo(null);
        waitingdlg.setSize(300, 75);
        dpb.setIndeterminate(true);

        Thread t = new Thread(new Runnable() {
            public void run() {
                waitingdlg.setVisible(true);
            }
        });
        t.start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.HumanPlayer;
import Object.SocketController;
import Object.State;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class LoginFrame extends JFrame {

    public LoginFrame() throws HeadlessException {
        this.setTitle("login to server");

        Container container = this.getContentPane();

        container.setLayout(new GridBagLayout());

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(2, 2, 2, 2);

        //lable
        JLabel jlName = new JLabel("NickName: ");
        JLabel jlServer = new JLabel("Server: ");
        JLabel jlPort = new JLabel("Port: ");

        //Text field
        JTextField jtfName = new JTextField(15);
        JTextField jtfServer = new JTextField(15);
        jtfServer.setText("127.0.0.1");
        JTextField jtfPort = new JTextField(15);
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

                try {
                    Socket socket = new Socket(server, port);

                    HumanPlayer hp = new HumanPlayer(name);
                    SocketController.send_object_to_socket(socket, hp);

                    List<String> listNickName = new ArrayList<>();

                    State state = (State) SocketController.get_object_from_socket(socket);

                    hp = state.getPlayer();
                    listNickName = state.getNickName();
                    listNickName = arrageListNickName(listNickName, state.getPlayerIndex());

                    PlayingFrame playingFrame = new PlayingFrame(socket, hp, listNickName, state.getPlayerIndex());
                    playingFrame.GameStart();
                    dispose();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
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

        constraint.gridx = 2;
        constraint.gridy = 3;
        constraint.gridwidth = 1;
        container.add(jbConnect, constraint);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }
}

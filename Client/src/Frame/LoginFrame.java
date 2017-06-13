/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.HumanPlayer;
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
import java.util.List;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class LoginFrame extends JFrame{

    public LoginFrame() throws HeadlessException {
        this.setTitle("login to server");
        
        Container container = this.getContentPane();
        
        container.setLayout(new GridBagLayout());
        
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(2,2,2,2);
        
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
                if(name.isEmpty() || server.isEmpty() || port < 0) {
                    JOptionPane.showMessageDialog(null, "Fill all blank");
                    return;
                }
                
                try {
<<<<<<< HEAD
                    Socket s = new Socket(server, port);

                    OutputStream os = s.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);

                    InputStream is = s.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    HumanPlayer p = new HumanPlayer(name);
                    oos.flush();
                    oos.writeObject(p);
                    oos.flush();
                    p = (HumanPlayer)ois.readObject();
                    p.sortHand();
                    List<String> listNickName = (List<String>)ois.readObject();
                    int i;
                    for(i = 0; i < listNickName.size(); i++)
                    {
                        if(listNickName.get(i).equals(name))
                        {
                            listNickName.remove(i);
                            
                            break;
                        }
                    }
                    PlayingFrame cf = new PlayingFrame(s, p, listNickName);
=======
                    Socket socket = new Socket(server, port);
                    System.out.println(socket.getPort());
                    //send_string_to_server(socket,name);
                    HumanPlayer hp = new HumanPlayer(name);
                    send_object_to_server(socket, hp);
                    hp = (HumanPlayer)get_object_from_server(socket);
                    List<String> listNickName = (List<String>)get_object_from_server(socket);
                    PlayingFrame cf = new PlayingFrame(socket, hp, listNickName);
>>>>>>> 9ed2dd9e17939f72e685fb5b2f4e398f4c16c4a8
                    dispose();
                } catch (Exception ex) {
                    //Logger.getLogger(Hearts.class.getName()).log(Level.SEVERE, null, ex);
                }
                
//                try {
//                    Socket socket = new Socket(server, port);
//                    System.out.println(socket.getPort());
//                    //send_string_to_server(socket,name);
//                    HumanPlayer hp = new HumanPlayer(name);
//                    //send_object_to_server(socket, hp);
//                    hp = (HumanPlayer)get_object_from_server(socket);
//                    List<String> listNickName = (List<String>)//get_object_from_server(socket);
//                    PlayingFrame cf = new PlayingFrame(socket, hp, listNickName);
//                    dispose();
//                    
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(null,"Can't connect to server");
//                }
            }
        });
        
        //add to container
        //thêm vào JPanel
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        container.add(jlName,constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.gridwidth = 2;
        container.add(jtfName,constraint);
        
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 1;
        container.add(jlServer,constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.gridwidth = 2;
        container.add(jtfServer,constraint);

        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth = 1;
        container.add(jlPort,constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 2;
        constraint.gridwidth = 2;
        container.add(jtfPort,constraint);        
        
        constraint.gridx = 2;
        constraint.gridy = 3;
        constraint.gridwidth = 1;
        container.add(jbConnect,constraint);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);

    }
    
    private void send_object_to_server(Socket socket, Object obj){
        try {
            java.io.OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.flush();
            oos.writeObject(obj);
            oos.flush();
            
            oos.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Can't send object");
        }
    }
    // lấy object từ server
    private Object get_object_from_server(Socket s) {
        try {
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            
            Object obj = ois.readObject();
            
<<<<<<< HEAD
            ois.close();
=======
//            ois.close();
//            is.close();
>>>>>>> 9ed2dd9e17939f72e685fb5b2f4e398f4c16c4a8
            return obj;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Can't read object");
            System.out.println(ex.getMessage());
        }
        return null;
    }
}

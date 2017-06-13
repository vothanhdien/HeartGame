/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 *
 * @author HP
 */
public class PlayingFrame extends JFrame{

    Socket socket;
    
    
    public PlayingFrame(Socket s) throws HeadlessException {
        this.socket = s;
        
        Container container = this.getContentPane();
        container.add(new Game());
        this.pack();
        this.setVisible(true);
//        
//        container.setLayout(new GridLayout(3,1));
//        
//        JButton btExit = new JButton("exit");
//        btExit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    socket.close();
//                    
//                    LoginFrame lg = new LoginFrame();
//                    dispose();
//                } catch (IOException ex) {
//                    Logger.getLogger(PlayingFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        
//        JScrollPane scr = new JScrollPane();
//        
//        JTextArea chat_area = new JTextArea();
//        
//        scr.setViewportView(chat_area);
//        
//        JTextArea text_input = new JTextArea();
//        
//        container.add(chat_area);
//        container.add(text_input);
//        container.add(btExit);
//        
//        this.setVisible(true);
//        Thread receive_thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                InputStream is;
//                try {
//                    is = s.getInputStream();
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                    while(true){
//
//                            System.out.println("server: " + br.readLine());
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        });
//        receive_thread.start();
    }
    
}

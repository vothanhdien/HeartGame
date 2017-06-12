/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class StartFrame extends JFrame{
    ServerSocket server_socket;
    JButton btStop = new JButton("Stop server");
    JButton btStart = new JButton("Start server");
    boolean running = false;

    private void start_server(){
        try {
            server_socket = new ServerSocket(3200);
            btStart.setEnabled(false);
            btStart.setText("server is running");
            btStop.setEnabled(true);
            running = true;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(running)
                    {
                        System.out.println("Waiting for a Client");

                        java.net.Socket s;
                        try {
                            s = server_socket.accept(); //synchronous
                            String name = get_string_from_client(s);
                            System.out.println("Client " + name + " connect");
                        } catch (IOException ex) {
                            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            thread.start();
        } catch (IOException ex) {
           JOptionPane.showMessageDialog(null, "Can't start server");
        }
    }
    private void stop_server(){
        try {
            server_socket.close();
            running = false;
            btStart.setText("Start server");
            btStart.setEnabled(true);
            btStop.setEnabled(false);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Can't stop server");
        }
    }
    
    public StartFrame() throws HeadlessException {
        this.setTitle("Server:127.0.0.1 Port: 3200");
        this.setPreferredSize(new Dimension(400,100));
        Container container = this.getContentPane();
        
        container.setLayout(new GridLayout(2,1));
        
        btStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                start_server();
                
            }
        });
        btStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                stop_server();
                
            }
        });
        btStop.setEnabled(false);
        
        
        container.add(btStart);
        container.add(btStop);
        
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        this.pack();
        this.setVisible(true);
    }
    
    private String get_string_from_client(Socket s) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            return br.readLine();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Something error");
        }
        return "";
    }
}

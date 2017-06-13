/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.HumanPlayer;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    ArrayList<Socket> listSocket = new ArrayList<>();
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
//                            String name = get_string_from_client(s);
//                            System.out.println("Client " + name + " connect at port " + s.getPort());
                            try {
                                HumanPlayer obj = (HumanPlayer)get_object_from_client(s);
                                
                                listSocket.add(s);

                                System.out.println("Client " + obj.getName() + " connect at port " + s.getPort());
                                
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                            Thread test = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(s.isClosed())
//                                        System.out.println("Client " + name +
//                                                " connect at port " + s.getPort() + " have been close");
//                                }
//                            });
//                            test.start();
                            
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
                listSocket.forEach((s) -> {
                    send_string_to_client(s, "Server have been stoped!");
                });
                System.exit(0);
            }
        });
        
        this.pack();
        this.setVisible(true);
    }
    
    private String get_string_from_client(Socket s) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            String str = br.readLine();
            br.close();
            return str;
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Something error");
        }
        return "";
    }
    private void send_string_to_client(Socket socket, String str) {
        try {
            java.io.OutputStream os = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(str + "\n");
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Something error");
        
        }
    }
    private void send_object_to_server(Socket socket, Object obj){
        try {
            java.io.OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            oos.writeObject(obj);
            oos.flush();
            
            
            os.close();
            oos.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Can't send object");
        }
    }
    // lấy object từ server
    private Object get_object_from_client(Socket s) throws ClassNotFoundException {
        try {
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            
            Object obj = ois.readObject();
            
            ois.close();
            is.close();
            return obj;
        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(null, "Can't read object");
            System.out.println(ex.getMessage());
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Object.HumanPlayer;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import com.sun.xml.internal.ws.api.message.Message;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
        JLabel jlName = new JLabel("Name: ");
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
                    Socket socket = new Socket(server, port);
                    System.out.println(socket.getPort());
                    //send_string_to_server(socket,name);
                    HumanPlayer hp = new HumanPlayer(name);
                    send_object_to_server(socket, hp);
                    PlayingFrame cf = new PlayingFrame(socket);
//                    Game game = new Game();
                    dispose();
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"Can't connect to server");
                }
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
        this.pack();
        this.setVisible(true);

    }
    
     private String get_string_from_server(Socket s) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            return br.readLine();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Something error");
        }
        return "";
    }
    
    private void send_string_to_server(Socket socket, String str) {
        try {
            java.io.OutputStream os = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(str + "\n");
            bw.flush();
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
    private Object get_object_from_server(Socket s) throws ClassNotFoundException {
        try {
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            
            Object obj = ois.readObject();
            
            ois.close();
            is.close();
            return obj;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Can't read object");
        }
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import javafx.scene.effect.Light;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class SocketController {
    public static void send_object_to_socket(Socket s, Object obj) {
        try {
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);

            oos.writeObject(obj);
            oos.flush();
            
//            oos.close();
        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(null, "Can't send object");
            System.out.println(ex.getMessage());
        }
    }

    // lấy object từ server
    public static Object get_object_from_socket(Socket s) {
        try {         
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            
            
            Object obj = ois.readObject();
            
//            ois.close();
            return obj;
        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Can't read object at socket port: " + s.getPort());
            System.out.println(ex.getMessage());
        }
        return null;
    }
}

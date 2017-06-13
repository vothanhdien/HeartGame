/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Frame.StartFrame;
import Object.testobject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didim
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
//        try {
//            ServerSocket ss = new ServerSocket(3200);
//            Socket s = ss.accept();
//            InputStream is = s.getInputStream();
//            ObjectInputStream ois = new ObjectInputStream(is);
//            testobject to = (testobject)ois.readObject();
////            Object to = ois.readObject();
//            if (to!=null){System.out.println(to.toString());}
//            System.out.println((String)ois.readObject());
//            is.close();
//            s.close();
//            ss.close();
//        }catch(Exception e){System.out.println(e);}
          
        StartFrame st = new StartFrame();
//        try
//        {
//            ServerSocket s = new ServerSocket(3200);
//
//            while(true)
//            {
//                System.out.println("Waiting for a Client");
//                
//                java.net.Socket server_socket = s.accept(); //synchronous
//                
//                System.out.println("Client connect");
//                System.out.println(server_socket.getPort());
//                    
//                Thread receive_thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        InputStream is;
//                        try {
//                            is = server_socket.getInputStream();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                            while(true){
//                                
//                                System.out.println("client: " + br.readLine());
//                            }
//                        } catch (IOException ex) {
//                            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        
//                    }
//                });
//                receive_thread.start();
//                
//                OutputStream os = server_socket.getOutputStream();
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//                
//                Thread sent_thead = new Thread(new Runnable() {
//                    @Override
//                    public void run() {                        
//                        DataInputStream din =new DataInputStream(System.in);
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(din));
//                        while(true){
//                           
//                            try {
////                                if(!reader.readLine().isEmpty()){
//                                    String k = reader.readLine();
//                                    bw.write(k);
//                                    bw.newLine();
//                                    bw.flush();
////                                };
//                            } catch (IOException ex) {
//                                Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                         
//                        }
//                    }
//                });
//                sent_thead.start();
//                
////                InputStream is = server_socket.getInputStream();
////                BufferedReader br=new BufferedReader(new InputStreamReader(is));
//
//
////
////                String receivedMessage;
////
////                while(true)
////                {
////                        receivedMessage=br.readLine();
////                        System.out.println("Received : " + receivedMessage);
////                        if (receivedMessage.equalsIgnoreCase("quit"))
////                        {
////                                System.out.println("Client has left !");
////                                break;
////                        }
////                        else
////                        {
////                                DataInputStream din =new DataInputStream(System.in);
////                                String k=din.readLine();
////                                bw.write(k);
////                                bw.newLine();
////                                bw.flush();
////                        }
////                }
////                bw.close();
////                br.close();
//            }
//        }
//        catch(IOException e)
//        {
//                System.out.println("There're some error");
//        }
    }
    
}

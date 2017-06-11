/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Frame.LoginFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didim
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LoginFrame lg = new LoginFrame();
//        try
//        {
//            Socket s = new Socket("127.0.0.1",3200);
//            System.out.println(s.getPort());
//            Thread receive_thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        InputStream is;
//                        try {
//                            is = s.getInputStream();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                            while(true){
//                                
//                                    System.out.println("server: " + br.readLine());
//                            }
//                        } catch (IOException ex) {
//                            Logger.getLogger(Socket.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        
//                    }
//                });
//                receive_thread.start();
//                
//                OutputStream os = s.getOutputStream();
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
////            InputStream is = s.getInputStream();
////            BufferedReader br = new BufferedReader(new InputStreamReader(is));
////
////            OutputStream os = s.getOutputStream();
////            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
////
////            String sentMessage="";
////            String receivedMessage;
////
////            System.out.println("Talking to Server");
////
////            do
////            {
////                DataInputStream din = new DataInputStream(System.in);
////                sentMessage=din.readLine();
////                bw.write(sentMessage);
////                bw.newLine();
////                bw.flush();
////
////                if (sentMessage.equalsIgnoreCase("quit"))
////                        break;
////                else
////                {
////                        receivedMessage=br.readLine();
////                        System.out.println("Received : " + receivedMessage);					
////                }
////
////            }
////            while(true);
////
////                bw.close();
////                br.close();
//        }
//        catch(IOException e)
//        {
//                System.out.println("There're some error");
//        }
    }
    
}

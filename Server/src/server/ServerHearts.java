/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhearts;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DaDa Wind
 */
public class ServerHearts {

    /**
     * @param args the command line arguments
     */
    static List<HumanPlayer> listPlayers = new ArrayList<>();
    static int countConnectedClient = 0;
    static List<Card> allCards = new ArrayList<>();
    static List<Socket> listSockets = new ArrayList<>();
    static ServerSocket ss;
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ss = new ServerSocket(1260);
            
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(listSockets.size() < 4)
                    {
                    }
                    startGame();
                }
            });
            thread.start();
            
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        listSockets.add(s);
//                        startGame(0);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread1.start();
            
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        listSockets.add(s);
//                        startGame(1);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread2.start();
            
            Thread thread3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept();
                        listSockets.add(s);
//                        startGame(2);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread3.start();
            
            Thread thread4 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket s = ss.accept(); 
                        listSockets.add(s);
//                        startGame(3);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            thread4.start();

        } catch (IOException ex) {
            Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void startGame() {
        for(int index = 0; index < listPlayers.size(); index++)
        {
            try {
               InputStream is = listSockets.get(index).getInputStream();
               ObjectInputStream ois = new ObjectInputStream(is);

               OutputStream os = listSockets.get(index).getOutputStream();
               ObjectOutputStream oos = new ObjectOutputStream(os);

               do {                
                   HumanPlayer player = (HumanPlayer)ois.readObject();
                   listPlayers.add(index, player);
               } while (true);

           } catch (Exception ex) {
               Logger.getLogger(ServerHearts.class.getName()).log(Level.SEVERE, null, ex);
           }   
        }
        
        createNewAllCards();
        randomAllCards();
        deal4AllPlayer();
    }
    
    static void createNewAllCards()
    {
        Value[] a = new Value[]{Value.TWO, Value.THREE, Value.FOUR, Value.FIVE, Value.SIX, Value.SEVEN,
                    Value.EIGHT, Value.NINE, Value.TEN, Value.JACK, Value.QUEEN, Value.KING, Value.ACE};
        CardType temp = CardType.SPADES;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
        
        temp = CardType.CLUBS;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
        
        temp = CardType.DIAMONDS;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
        
        temp = CardType.HEARTS;
        for(int i = 0; i < 13; i++)
        {
            allCards.add(new Card(a[i], temp));
        }
    }
    
    static void randomAllCards()
    {
        ArrayList<Card> kq = new ArrayList<>();
        Random rd = new Random();
        for(int i = allCards.size(); i > 0;i--){
            int index = rd.nextInt(i);
            kq.add(allCards.get(index));
            allCards.remove(index);
        }
        allCards = kq;
    }
    
    static void deal4AllPlayer()
    {
        List<HumanPlayer> temp = new ArrayList<>();
        HumanPlayer p1 = listPlayers.get(0);
        HumanPlayer p2 = listPlayers.get(1);
        HumanPlayer p3 = listPlayers.get(2);
        HumanPlayer p4 = listPlayers.get(3);
        for(int i = 0; i < 13; i++)
        {
            p1.hand.add(allCards.get(i*4));
            p2.hand.add(allCards.get(i*4 + 1));
            p3.hand.add(allCards.get(i*4 + 2));
            p4.hand.add(allCards.get(i*4 + 3));
        }
        temp.add(p1);
        temp.add(p2);
        temp.add(p3);
        temp.add(p4);
        listPlayers = temp;
    }
}
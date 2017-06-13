/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heartgame;

import Object.CardType;
import Object.HumanPlayer;
import Object.Value;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author didim
 */
public class HeartGame {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        System.out.println(Value.ACE);
        ArrayList<HumanPlayer> array = new ArrayList<>();
//        for(int i =0 ;i < 52;i++)
//            array.add(i);
//        for(int i =0; i<10;i++)
//            mess(array);

        array.add(new HumanPlayer("dien"));
        array.get(0).addScore(100);
        
        System.out.println(array.get(0).getScore());
        
        // TODO code application logic here
//        CardType type1 = CardType.SPADES;
//        CardType type2 = CardType.CLUBS;
//        CardType type3 = CardType.DIAMONDS;
//        CardType type4 = CardType.HEARTS;
//        if(type1.compareTo(type2) < 0)
//            System.out.println("this message appear if clubs > spades");
//        if(type3.compareTo(type4) < 0)
//            System.out.println("this message appear if hearts > diamonds");
//        if(type1.compareTo(type4) < 0)
//            System.out.println("this message appear if hearts > spades");
//        if(type1.compareTo(type3) < 0)
//            System.out.println("this message appear if diamonds > spades");
//        
//        if(type2.compareTo(type1) < 0)
//            System.out.println("this message appear if spades > clubs");
//        if(type4.compareTo(type3) < 0)
//            System.out.println("this message appear if diamonds > hearts");
//        if(type4.compareTo(type1) < 0)
//            System.out.println("this message appear if spades > hearts");
//        if(type3.compareTo(type1) < 0)
//            System.out.println("this message appear if spades > diamonds");
    }

    private static void mess(ArrayList<Integer> array) {
        ArrayList<Integer> kq = new ArrayList<>();
        Random rd = new Random();
        for(int i = array.size(); i > 0;i--){
            int index = rd.nextInt(i);
            kq.add(array.get(index));
            array.remove(index);
        }
        System.out.println(kq.toString());
        kq.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer t, Integer t1) {
                return t.compareTo(t1);
            }
        });
        System.out.println(kq.toString());
        array = kq;
    }

}

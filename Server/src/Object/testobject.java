/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Object;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class testobject implements Serializable {
    int value ;
    String id;
    public  testobject(int v, String s ){
        this.value=v;
        this.id=s;
    }   
}

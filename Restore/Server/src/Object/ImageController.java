package Object;

import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HP
 */
public class ImageController {
    public static ImageIcon getFullImageIcon(Card card, int width, int height){
        
        ImageIcon temp = new ImageIcon("images//full-images//" + card.getCode() + ".png");
        java.awt.Image i = temp.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(i);
    }
    public static ImageIcon getHalfImageIcon(Card card, int width, int height){   
        ImageIcon temp = new ImageIcon("images//half-images//" + card.getCode() + ".png");
        java.awt.Image i = temp.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(i);
    }
    
    public static ImageIcon getImageByName(String name, int width, int height)
    {
        ImageIcon temp = new ImageIcon("images//full-images//" + name);
        java.awt.Image i = temp.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        
        return new ImageIcon(i);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author VPC
 */
class Item {
    BufferedImage image;
    private final int ITEM_WIDTH = 50;
    private int x,y;
    private int numbOrder;
    private int HP,MP;
    private int ATK,DEF;
    private double speed;
    public Item(){
        x = 0;
        y = 0;
        numbOrder = 0;
        HP = 0;
        MP = 0;
        ATK = 0;
        DEF = 0;
        speed = 0;  
    }

    public int getNumbOrder() {
        return numbOrder;
    }

    public int getHP() {
        return HP;
    }

    public int getMP() {
        return MP;
    }

    public int getATK() {
        return ATK;
    }

    public int getDEF() {
        return DEF;
    }

    public double getSpeed() {
        return speed;
    }
    public void setItem(String url,int numbOrder, int HP, int MP, int ATK, int DEF, double speed){
        try{
            URL urlImage = this.getClass().getResource(url);
            image = ImageIO.read(urlImage);
        }catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.numbOrder = numbOrder;
        this.HP = HP;
        this.MP = MP;
        this.ATK = ATK;
        this.DEF = DEF;
        this.speed = speed;
    }
    
    public void DrawOnMap(Graphics2D g2d){
        g2d.drawImage(image, x,y, null);
    }
    public void DrawInBag(Graphics2D g2d){
        g2d.drawImage(image, Bag.X+this.numbOrder*ITEM_WIDTH,Bag.Y, null);
    }
}

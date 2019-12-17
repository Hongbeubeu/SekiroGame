/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package rpg;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Bag {
    public static final int X = 100;
    public static final int Y = 300;
    private ArrayList<Item> items;
    private int HP,MP;
    private int ATK,DEF;
    private double speed;
    
    public Bag(){
        items = new ArrayList<>();
        items.add(new Item());
        HP = 0;
        MP = 0;
        ATK = 0;
        DEF = 0;
        speed = 0;      
    }
    public void add(Item newItem){
        items.add(newItem);
    }
    public Item getItem(int index){
        return items.get(index);
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
    public void update(){
        for (Item temp : items){
            this.ATK += temp.getATK();
            this.DEF += temp.getDEF();
            this.HP += temp.getHP();
            this.MP += temp.getMP();
            this.speed += temp.getSpeed();
        }
    }
    public void Draw(Graphics2D g2d){
        for (Item temp : items){
            temp.DrawInBag(g2d);
        }
    }
}
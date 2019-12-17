package rpg;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

class Player extends Entity {

    private final int MAXLEVEL = 8;
    private int HP, MP;
    private int HPMax, MPMax;
    private int EXP;
    private int level;
    private int ATK;
    private int ATK_FIREBALL;
    private int DEF;
    private double speed;
    private double att_x = 0;
    private double att_y = 0;
    private double att_fireball_x = 0;
    private double att_fireball_y = 0;
    private Entity.Direction dir_fire;
    private int index_att;
    private int countSword;
    private int countFireball;
    private int countRegen;
    public Bag bag;
    private Point range_Att1 = new Point();
    private Point range_Att2 = new Point();
    private Point range_Att3 = new Point();
    private Point range_fireball = new Point();
    private int countdownImmortal, countAtRest, countTime;
    private int index_img;
    private int index_fireball;
    /* img for player
    sang phai
    sang trai
    di len
    di xuong
    hien tai
     */

    private BufferedImage character;
    private Image[][] Character = new Image[4][3];//new
    private BufferedImage character_spritesheet;//new
    private int width, height;
    private int Width, Height;//new
    /* img for att
    array of att sword img
    att current img
     */
    private BufferedImage[][] sword = new BufferedImage[4][3];
    private BufferedImage current_sword_att;

    /*img for skill att
    array of fire ball att
    fire ball current img
     */
    private Image[][] fire_ball = new Image[4][8];
    private BufferedImage fireball_sheet;
    private BufferedImage current_fireball;

    private boolean att = false;// attack sword
    private boolean fireball_att = false;

    //private Item items[];
    public void Load_image() {
        try {
            //load img of character

            URL character_sheetUrl = this.getClass().getResource("/rpg/resources/character/full_character.png");
            character_spritesheet = ImageIO.read(character_sheetUrl);
            URL fireball_sheetUrl = this.getClass().getResource("/rpg/resources/character/attack/fireball.png");
            fireball_sheet = ImageIO.read(fireball_sheetUrl);

            //load img of att
            for (int i = 0; i < 3; i++) {
                URL att_leftUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_left_" + (i + 1) + ".png");
                sword[0][i] = ImageIO.read(att_leftUrl);
            }
            for (int i = 0; i < 3; i++) {
                URL att_rightUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_right_" + (i + 1) + ".png");
                sword[1][i] = ImageIO.read(att_rightUrl);
            }
            for (int i = 0; i < 3; i++) {
                URL att_upUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_up_" + (i + 1) + ".png");
                sword[2][i] = ImageIO.read(att_upUrl);
            }
            for (int i = 0; i < 3; i++) {
                URL att_downUrl = this.getClass().getResource("/rpg/resources/character/attack/sword_down_" + (i + 1) + ".png");
                sword[3][i] = ImageIO.read(att_downUrl);
            }
            this.Width = character_spritesheet.getWidth() / 12;
            this.Height = character_spritesheet.getHeight() / 8;

            //load img of character
            int x = this.Width * 3, y = this.Height * 4;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    Character[i][j] = character_spritesheet.getSubimage(x, y, this.Width, this.Height);
                    x += this.Width;
                }
                x = this.Width * 3;
                y += this.Height;
            }

            //load img of fireball | height and width = 64
            x = 0;
            y = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 8; j++) {
                    fire_ball[i][j] = fireball_sheet.getSubimage(x, y, 64, 64);
                    x += 64;
                }
                x = 0;
                y += 64 * 2;
            }
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Set toa do mac dinh cho nhan vat, set Speed
    public Player() {
        this.bag = new Bag();
        this.Load_image();
        character = (BufferedImage) Character[0][1];
        current_sword_att = sword[3][2];
        current_fireball = (BufferedImage) fire_ball[0][1];
        this.visible = true;
        this.HP = 100;
        this.HPMax = 100;
        this.MP = 100;
        this.MPMax = 100;
        this.level = 1;
        this.EXP = 0;
        this.ATK = 100;
        this.ATK_FIREBALL = 50;
        this.DEF = 2;
        x = 6 * Map.TILE_WIDTH;
        y = 30 * Map.TILE_HEIGHT;
        speed_base = 3;
        index_att = 0;
        this.index_img = 0;
        this.index_fireball = 0;
        this.countTime = 0;
        this.countRegen = 0;
        this.direction = Direction.DOWN;
        dir_fire = this.direction;
    }

    public int get_ATK_FIREBALL() {
        return this.ATK_FIREBALL;
    }

    public void set_Xfireball(double x) {
        this.att_fireball_x = x - 32;
    }

    public void set_Yfireball(double y) {
        this.att_fireball_y = y - 32;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getEXP() {
        return EXP;
    }

    public void setEXP(int EXP) {
        this.EXP = EXP;
    }

    public void addEXP(int aEXP) {
        int newEXP = this.EXP + aEXP;
        if (levelUp(this.EXP, newEXP)) {
            this.level++;
            this.HP_base = 100 * level;
            this.MP_base = 80 * level;
            this.DEF_base = 3 * level;
            this.attack_base = 4 * level;
            UpdatePlayer();
            HP = HPMax;
            MP = MPMax;
        }
        this.EXP += aEXP;
    }

    private boolean levelUp(int oldEXP, int newEXP) {
        for (int i = 0; i <= MAXLEVEL; i++) {
            int cotmoc = (int) Math.pow(2, i) * 100;
            if (((oldEXP - cotmoc) * (newEXP - cotmoc)) < 0 || (newEXP == cotmoc)) {
                return true;
            }
        }
        return false;
    }

    public void UpdatePlayer() {
        HPMax = this.HP_base + bag.getHP();
        MPMax = this.MP_base + bag.getMP();
        this.ATK = this.attack_base + bag.getATK();
        this.DEF = this.DEF_base + bag.getDEF();
        this.speed = this.speed_base + bag.getSpeed();

    }

    public Player(double x, double y, int attack_base, int DEF_base, int HP_base, int speed) {
        this.setX(x);
        this.setY(y);
        this.setSpeed(speed);
        this.setHP_base(HP_base);
        this.setDEF_base(DEF_base);
        this.setAttack_base(attack_base);
    }

    public void attack(Direction direction) {

    }

    public void decreaseHP(int dame) {
        if (countdownImmortal == 0) {
            if(dame>this.DEF){
            this.HP -= (dame - this.DEF);System.out.println("Player HP = " + HP);
            countAtRest = 10;
            countdownImmortal = 40;
            }
        }
    }

    public boolean immortal() {
        return (countdownImmortal > 0);
    }

    public boolean die() {
        return this.HP < 0;
    }

    public void skill() {

    }

    public void move(double nx, double ny) {
        this.x = nx;
        this.y = ny;
        countTime++;
    }

    public void player_be_attacked() {
        if (countdownImmortal > 0) {
            countdownImmortal--;
        }
        if (countAtRest > 0) {
            countAtRest--;
        }
    }

    public void setSwordAni() {
        if (att == true) {
            if (countSword == 5) {
                index_att = 1;
            }
            if (countSword == 10) {
                index_att = 2;
            }
            countSword++;
        } else {
            countSword = 0;
            index_att = 0;
        }
    }

    public void setFireballAni() {
        if (fireball_att == true) {
            switch (countFireball) {
                case 0:
                    dir_fire = this.direction;
                case 5:
                    index_fireball = 1;
                    break;
                case 10:
                    index_fireball = 2;
                    break;
                case 15:
                    index_fireball = 3;
                    break;
                case 20:
                    index_fireball = 4;
                    break;
                case 25:
                    index_fireball = 5;
                    break;
                case 30:
                    index_fireball = 6;
                    break;
                case 35:
                    index_fireball = 7;
                    break;
                default:
                    break;
            }
            switch (dir_fire) {
                case UP:
                    current_fireball = (BufferedImage) fire_ball[1][index_fireball];
                    att_fireball_y -= 5;
                    range_fireball.setLocation(att_fireball_x + 32, att_fireball_y);
                    break;
                case DOWN:
                    current_fireball = (BufferedImage) fire_ball[3][index_fireball];
                    att_fireball_y += 5;
                    range_fireball.setLocation(att_fireball_x + 32, att_fireball_y + 64);
                    break;
                case LEFT:
                    current_fireball = (BufferedImage) fire_ball[0][index_fireball];
                    att_fireball_x -= 5;
                    range_fireball.setLocation(att_fireball_x, att_fireball_y + 32);
                    break;
                case RIGHT:
                    current_fireball = (BufferedImage) fire_ball[2][index_fireball];
                    att_fireball_x += 5;
                    range_fireball.setLocation(att_fireball_x + 64, att_fireball_y + 32);
                    break;
            }
            countFireball++;
        } else {
            countFireball = 0;
            index_fireball = 0;
            att_fireball_x = 0;
            att_fireball_y = 0;
        }
    }

    public void set_att(boolean att) {
        this.att = att;
    }

    public boolean get_att() {
        return this.att;
    }

    public void set_fireball_att(boolean att) {

        if (att) {
            if (MP >= 45) {
                this.fireball_att = att;
                this.MP -= 45;
            } else {
                this.fireball_att = false;
            }
        } else {
            this.fireball_att = att;
        }

    }

    public void regen() {
        if (countRegen % 40 == 0) {
            if (HP < HPMax - 2) {
                HP += 1;
            }
            if (MP < MPMax - 5) {
                MP += 1;
            }
        }
        countRegen++;
    }

    public boolean get_fireball_att() {
        return this.fireball_att;
    }

    public int getATK() {
        return this.ATK;
    }

    public int getLevel() {
        return this.level;
    }

    public BufferedImage get_att_range() // lay anh current att
    {
        return current_sword_att;
    }

    public Point get_fireball_att_point() {
        return range_fireball;
    }

    public Point get_att_point1() // tra ve diem thu nhat de xet toa do att
    {
        return range_Att1;
    }

    public Point get_att_point2()// tra ve diem thu 2 de xet toa do att
    {
        return range_Att2;
    }

    public Point get_att_point3()// tra ve diem thu 3 de xet toa do att
    {
        return range_Att3;
    }

    void draw_direction() {
        if (countAtRest > 0) {
            countAtRest--;
        }
        if (countAtRest == 0) {
            if (countTime * speed_base % 30 == 0) {
                if (index_img > 0) {
                    index_img--;
                } else if (index_img == 0) {
                    index_img = 2;
                }
            }
        }
        if (direction == Direction.LEFT) {
            character = (BufferedImage) Character[1][index_img];
            current_sword_att = sword[0][index_att];

            att_x = x - 25;
            att_y = y - 27;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x, att_y + current_sword_att.getHeight());
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }
        if (direction == Direction.RIGHT) {
            character = (BufferedImage) Character[2][index_img];
            current_sword_att = sword[1][index_att];
            att_x = x + 30;
            att_y = y - 29;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x + current_sword_att.getWidth(), att_y + current_sword_att.getHeight());
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }
        if (direction == Direction.UP) {
            character = (BufferedImage) Character[3][index_img];
            current_sword_att = sword[2][index_att];
            att_x = x - 10;
            att_y = y - 50;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x + current_sword_att.getWidth(), att_y);
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }
        if (direction == Direction.DOWN) {
            character = (BufferedImage) Character[0][index_img];
            current_sword_att = sword[3][index_att];

            att_x = x - 8;
            att_y = y + 20;
            range_Att1.setLocation(att_x, att_y);
            range_Att3.setLocation(att_x + current_sword_att.getWidth(), att_y + current_sword_att.getHeight());
            range_Att2.setLocation((range_Att1.getX() + range_Att3.getX()) / 2, (range_Att1.getY() + range_Att3.getY()) / 2);
        }

    }

    void paint(Graphics2D g2d) {
        if (visible) {
            g2d.drawImage(character, (int) (x - 8), (int) (y - 23), this.Width, this.Height, null);
            drawHPbar(g2d);
        }
        if (att) {
            g2d.drawImage(current_sword_att, (int) att_x, (int) att_y, current_sword_att.getWidth(), current_sword_att.getHeight(), null);
        }
        if (fireball_att) {
            g2d.drawImage(current_fireball, (int) att_fireball_x, (int) att_fireball_y, 100, 100, null);
        }
        draw_direction();

    }

    void drawHPbar(Graphics2D g2d) {
        g2d.setColor(Color.green);
        g2d.fillOval((int) x - 21, (int) y - 41, 15, 15);
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(new Font("Verdana", Font.BOLD, 15));
        g2d.drawString(String.valueOf(this.level), (int) x - 19, (int) y - 28);
        g2d.setColor(Color.green);
        g2d.drawRect((int) x - 6, (int) y - 38, 35, 5);
        g2d.setColor(Color.yellow);
        g2d.fillRect((int) x - 5, (int) y - 37, (int) 34 * HP / HPMax, 4);
        g2d.setColor(Color.blue);
        g2d.drawRect((int) x - 6, (int) y - 32, 35, 4);
        g2d.fillRect((int) x - 5, (int) y - 31, (int) 34 * this.MP / this.MPMax, 3);
    }
}

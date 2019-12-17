package rpg;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class submap {

    /*
    Quy ước đặt tên các biến:
    Vùng không đi  được các biến nhỏ hơn 10
    Vùng đi được có giá trị từ 10 - 20
    Vùng đi bị vẽ lại khi player đi vào có giá trị 20 -30
     */
    public int width;
    public int height;
    public static final int BLOCK = 0;//Quy dinh vung khong duoc phep di vao
    public static final int CLEAR = 10;// Quy dinh vung di duoc
    public static final int REDRAW = 21;// QUy dinh vung cay di vao phai ve lai
    public static final int CHUYENMAP = 15;//Quy dinh vung di vao co the chuyen map
    public static final int GAIDOC = 12;//DI vao duoc nhung toc di cham va bi tru mau
    
    private int index;
    public static final int START = 30;
    public int STARTx;// Toa do xua hien cua nhan vat toa do x
    public int STARTy;// Toa do xuat hien y cua nhan vat
    // Vung player xuat hien
    public static final int END = 35;
//    public int index_submap;
    // Vung chuyen map, Khi player vao vung nay se chuyen map 
    File map,map_monster,monster,monster_1,monster_2;
    Scanner scan;

    String[][] stringTileNumbers = new String[100][100];
    int[][] data = new int[100][100];
    BufferedImage spriteSheet,spriteSheet_1,spriteSheet_2;
    Image[][] monsterSprites = new Image[4][3];
    Image[][] monsterSprites_1 = new Image[4][3];
    Image[][] monsterSprites_2 = new Image[4][3];
    Image[][] monsterSprites_3 = new Image[4][3];
    public Monster conSoi,tempMonster,xaHoiDen,girl;
    public ArrayList<Monster> arrayMonster;
    public int numberOfMonster;
    
    //Khởi tạo có tham số là 1 file .txt
    public submap(int index_submap) {
        this.index = index_submap;
        arrayMonster = new ArrayList<>();
        map = new File("src/rpg/resources/mapdata/map"+this.index+".txt");
        map_monster = new File("src/rpg/resources/mapdata/map"+this.index+"_monster.txt");
        loadMap();
        loadMapMonster();
        width = 0;
        height = 0;
    }

    //Khởi tạo mặc định với map demo
    public submap() {
        map = new File("src/rpg/resources/mapdata/map1.txt");    
    }

    public void loadMap() {
        try {

            scan = new Scanner(map);
            String temp;
            temp = scan.next();
            this.width = Integer.parseInt(temp);
            temp = scan.next();
            this.height = Integer.parseInt(temp);
            temp = scan.next();
            this.STARTx = Integer.parseInt(temp);
            temp = scan.next();
            this.STARTy = Integer.parseInt(temp);
            System.out.println("width = "+width+" . Height = "+height);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    stringTileNumbers[i][j] = scan.next();
                }
            }
            System.out.println("DOne!");
            for (int i = 0; i < stringTileNumbers.length; i++) {

                for (int j = 0; j < stringTileNumbers[i].length; j++) {

                    if (stringTileNumbers[i][j] == null) {

                        data[i][j] = -1;

                    } else {

                        data[i][j] = Integer.parseInt(stringTileNumbers[i][j]);

                    }

                }

            }

        } catch (IOException ex) {

            ex.printStackTrace();

        }
    }
    public void loadMapMonster(){
        double x, y;
        int exp, ATK,  DEF,  HP;
        double speed;
        double xLeft,  yTop,  xRight,  yBottom;
        Entity.Direction dir;
        try{
            scan = new Scanner(map_monster);
            
            String temp;
            temp = scan.next();
            this.numberOfMonster = Integer.parseInt(temp);
            for (int i=0;i<this.numberOfMonster;i++){
                tempMonster = new Monster(new File(scan.next()));
                x = Integer.parseInt(scan.next());
                y = Integer.parseInt(scan.next());
                exp = Integer.parseInt(scan.next());
                ATK = Integer.parseInt(scan.next());
                DEF = Integer.parseInt(scan.next());
                HP = Integer.parseInt(scan.next());
                speed = Double.parseDouble(scan.next());
                xLeft = Integer.parseInt(scan.next());
                yTop = Integer.parseInt(scan.next());
                xRight = Integer.parseInt(scan.next());
                yBottom = Integer.parseInt(scan.next());
                switch(Integer.parseInt(scan.next())){
                    case 1: dir = Entity.Direction.LEFT; break;
                    case 2: dir = Entity.Direction.UP; break;
                    case 3: dir = Entity.Direction.RIGHT; break;
                    case 4: dir = Entity.Direction.DOWN; break;
                    default: dir = Entity.Direction.DOWN; break;
                }
                tempMonster.setProperties(x*Map.TILE_WIDTH, y*Map.TILE_HEIGHT, exp, ATK, DEF, HP, speed, xLeft*Map.TILE_WIDTH, yTop*Map.TILE_HEIGHT, xRight*Map.TILE_WIDTH, yBottom*Map.TILE_HEIGHT, dir);
                arrayMonster.add(tempMonster);
                System.out.println(temp+", "+x+","+y+","+exp+","+ATK+","+DEF+","+HP+","+speed+","+xLeft+","+yTop);
            }
        
        } catch (IOException ex) {

            ex.printStackTrace();

        }
    }
    
    public void monster_move_onMap(Monster monster){
            monster.move_auto();
//            System.out.println("Ran =("+temp.getRanX()+", "+temp.getRanY()+")");
            if(valid_location(monster.getRanX(),monster.getRanY())!=0){
                monster.move(monster.getRanX(), monster.getRanY());
            }
            else{
                monster.randomDirec();
            }
    }
    
    public void monster_catch_player(Monster monster){
        
    }
    
    public int valid_location(double nx, double ny) {
        if (data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 0
                || data[(int) ((ny+30) / Map.TILE_HEIGHT )][(int) (nx / Map.TILE_WIDTH)] == 0
                || data[(int) (ny / Map.TILE_HEIGHT)][(int) ((nx+30) / Map.TILE_WIDTH )] == 0
                || data[(int) ((ny+30) / Map.TILE_HEIGHT)][(int) ((nx+30) / Map.TILE_WIDTH )] == 0) {
            return 0;
        } else {
            if (data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 10
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 10
                    || data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 10
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 10) {
                return 10;
            }

            else if (data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 21
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 21
                    || data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 21
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 21) {
                return 21;
            }

            else if (data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 30
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 30
                    || data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 30
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 30) {
                return 30;
            }

            else if (data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 35
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 35
                    || data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 35
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 35) {
                return 35;
            }
            else if (data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 15
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 15
                    || data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 15
                    || data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 15) {
                return 15;
            }
        }
        return 0;

    }
    public void removeMonster(Monster monster){
        int index_delete = arrayMonster.indexOf(monster);
        arrayMonster.remove(index_delete);
    }
    
    
    public void paint(Graphics2D g2d) {
        g2d.drawImage(Map.imgs.get(this.index), 4 * 24, 3 * 18, null);
        for (Monster temp : arrayMonster){
            temp.paint(g2d);
        }
    }
    

}

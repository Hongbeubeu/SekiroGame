package rpg;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import rpg.Player;

public class Game {

    // Map chính của game
    private Map map;

    //Nhân vật
    private Player player;

    private long time_start_sword;
    private long time_start_fireball;
    private final int COOLDOWN_ATTACK = 60;
    private int cooldown;
    private int fireball_cooldown;

    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }


    /**
     * Set variables and objects for the game.
     */
    private void Initialize() {
        map = new Map();
        player = new Player();
        cooldown = 0;
        fireball_cooldown = 0;
    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {

    }

    /**
     * Restart game - reset some variables.
     */
    public void RestartGame() {

    }

    /**
     * Update game logic.
     *
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */

    public void UpdateGame(long gameTime, Point mousePosition) {
        player_move();
        playerAction(gameTime);
        monsterAction();

    }

    public int valid_location(double nx, double ny) {
        if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 0
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 0
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 0
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 0) {
            return 0;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 10
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 10
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 10
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 10) {
            return 10;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 21
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 21
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 21
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 21) {
            return 21;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 30
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 30
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 30
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 30) {
            return 30;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 35
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 35
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 35
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 35) {
            return 35;
        } else if (map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH)] == 15
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH)] == 15
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT)][(int) (nx / Map.TILE_WIDTH + 1)] == 15
                || map.getCurrentMap().data[(int) (ny / Map.TILE_HEIGHT + 1)][(int) (nx / Map.TILE_WIDTH + 1)] == 15) {
            return 15;
        }
        return 0;

    }

    public void monster_move()//logic move's monster
    {
        for (int i = 0; i < map.getCurrentMap().numberOfMonster; i++) {
            double v = map.getCurrentMap().arrayMonster.get(i).getSpeed_base();
            double nx = map.getCurrentMap().arrayMonster.get(i).getX();
            double ny = map.getCurrentMap().arrayMonster.get(i).getY();
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.RIGHT) {
                nx = nx + v;
            }
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.LEFT) {
                nx = nx - v;
            }
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.UP) {
                ny = ny - v;
            }
            if (map.getCurrentMap().arrayMonster.get(i).getDirection() == Entity.Direction.DOWN) {
                ny = ny + v;
            }

            if (valid_location(nx, ny) == submap.BLOCK) {
                return;
            } else {
                map.getCurrentMap().arrayMonster.get(i).move(nx, ny);
            }

        }
    }

    public void player_move()// logic move's character
    {
        double dx = 0, dy = 0; //new speed_base
        double nx = player.getX(), ny = player.getY(); //new location

        if (Canvas.keyboardKeyState(KeyEvent.VK_LEFT) || Canvas.keyboardKeyState(KeyEvent.VK_A)) {
            dx = -player.speed_base;
            player.setDirection(Entity.Direction.LEFT);
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_RIGHT) || Canvas.keyboardKeyState(KeyEvent.VK_D)) {
            dx = player.speed_base;
            player.setDirection(Entity.Direction.RIGHT);
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_UP) || Canvas.keyboardKeyState(KeyEvent.VK_W)) {
            dy = -player.speed_base * 4 / 3;
            player.setDirection(Entity.Direction.UP);
        }
        if (Canvas.keyboardKeyState(KeyEvent.VK_DOWN) || Canvas.keyboardKeyState(KeyEvent.VK_S)) {
            dy = player.speed_base * 4 / 3;
            player.setDirection(Entity.Direction.DOWN);
        }
        nx += dx;
        ny += dy;

        if (valid_location(nx, ny) == submap.CLEAR)//vung bt
        {
            player.setVisible(true);
            player.move(nx, ny);
        }
        if (valid_location(nx, ny) == submap.REDRAW)//vung ve lai
        {
            player.setVisible(false);
            player.move(nx, ny);
        }
        if (valid_location(nx, ny) == submap.END)//vung ket thuc
        {
            map.nextMap();
            player.move(map.getCurrentMap().STARTx * Map.TILE_WIDTH, map.getCurrentMap().STARTy * Map.TILE_HEIGHT);
        }
        if (valid_location(nx, ny) == submap.START)//vung xp
        {
            map.backMap();
            player.move(map.getCurrentMap().STARTx * Map.TILE_WIDTH, map.getCurrentMap().STARTy * Map.TILE_HEIGHT);
        }
        if (valid_location(nx, ny) == submap.BLOCK)//vung k di dc
        {
            return;
        }
    }

    //Hanh dong cua quai vat
    public void monsterAction() {
        for (int i = 0; i < map.getCurrentMap().arrayMonster.size(); i++) {
            Monster temp = map.getCurrentMap().arrayMonster.get(i);
            if (temp.die()) {
                player.addEXP(temp.getExp());
                map.getCurrentMap().arrayMonster.remove(temp);
            }
            {
                Rectangle shapeMonter = new Rectangle((int) temp.getX(), (int) temp.getY(), temp.getWidth(), temp.getHeight());
                if (shapeMonter.contains(new Point((int) player.getX(), (int) player.getY()))
                        || shapeMonter.contains(new Point((int) player.getX() + player.getWidth() - 20, (int) player.getY()))
                        || shapeMonter.contains(new Point((int) player.getX(), (int) player.getY() + player.getHeight() - 20))
                        || shapeMonter.contains(new Point((int) player.getX() + player.getWidth() - 20, (int) player.getY() + player.getHeight() - 20))) {

                    double nx = player.getX();
                    double ny = player.getY();
                    if (player.immortal() == false) {
                        switch (player.getDirection()) {
                            case LEFT:
                                nx += 15 * player.getSpeed_base();
                                break;
                            case RIGHT:
                                nx -= 15 * player.getSpeed_base();
                                break;
                            case UP:
                                ny += 15 * player.getSpeedY();
                                break;
                            case DOWN:
                                ny -= 15 * player.getSpeedY();
                                break;
                            default:
                                break;
                        }
                        if (valid_location(nx, ny) == submap.CLEAR) {
                            player.move(nx, ny);
                        }
                        player.decreaseHP(temp.getAttack_base());

                    } else {

                    }
                }

                if (Math.sqrt(Math.pow(temp.getX() - player.getX(), 2) + Math.pow(temp.getY() - player.getY(), 2)) < temp.AREA_DETEC) {
                    double dx = temp.getX();
                    double dy = temp.getY();
                    if (Math.abs(temp.getX() - player.getX()) > Math.abs(temp.getY() - player.getY())) {
                        if (temp.getX() >= player.getX()) {
                            temp.setDirection(Entity.Direction.LEFT);
                            dx -= temp.getSpeed_base();
                        } else {
                            temp.setDirection(Entity.Direction.RIGHT);
                            dx += temp.getSpeed_base();
                        }
                    } else if (temp.getY() < player.getY()) {
                        temp.setDirection(Entity.Direction.DOWN);
                        dy += temp.getSpeedY();
                    } else if (temp.getY() >= player.getY()) {
                        temp.setDirection(Entity.Direction.UP);
                        dy -= temp.getSpeedY();
                    }
                    if (map.getCurrentMap().valid_location(dx, dy) != 0) {
                        temp.move(dx, dy);
                    }
                } else {
                    map.getCurrentMap().monster_move_onMap(temp);
                }
            }
        }
    }

    public void playerAction(long gameTime) {
        if (player.die()) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
        if (cooldown == 0) {
            if (Canvas.keyboardKeyState(KeyEvent.VK_SPACE)) {
                cooldown = COOLDOWN_ATTACK;
                player_att(gameTime);
            }
        }
        if (player.getLevel() >= 2) {
            if (fireball_cooldown == 0) {
                if (Canvas.keyboardKeyState(KeyEvent.VK_F)) {
                    player.set_Xfireball(player.getX());
                    player.set_Yfireball(player.getY());
                    fireball_cooldown = COOLDOWN_ATTACK;
                    player_fireball(gameTime);
                }
            }
        }
        player.setSwordAni();
        player.setFireballAni();
        if (cooldown > 0) {
            cooldown--;
        }
        if (fireball_cooldown > 0) {
            fireball_cooldown--;
        }

        if (time_start_sword != 0 && gameTime - time_start_sword > Framework.secInNanosec / 4) {
            player.set_att(false);
        }
        if (time_start_fireball != 0 && gameTime - time_start_fireball > Framework.secInNanosec) {
            player.set_fireball_att(false);
        }

        player_att_monster();
        player.player_be_attacked();
        player.regen();
    }

    public void player_att(long gameTime) {
        player.set_att(true);
        time_start_sword = gameTime;
    }

    public void player_fireball(long gameTime) {
        player.set_fireball_att(true);
        time_start_fireball = gameTime;

    }

    public void player_att_monster() // khi don tan cong cua player cham vao monster
    {
        double monster_x = 0;
        double monster_y = 0;
        double monster_width = 0;
        double monster_height = 0;
        for (int i = 0; i < map.getCurrentMap().arrayMonster.size(); i++) {
            monster_x = map.getCurrentMap().arrayMonster.get(i).getX();
            monster_y = map.getCurrentMap().arrayMonster.get(i).getY();
            monster_width = map.getCurrentMap().arrayMonster.get(i).getWidth();
            monster_height = map.getCurrentMap().arrayMonster.get(i).getHeight();
            if (player.get_att() == true
                    && (new Rectangle((int) monster_x, (int) monster_y, (int) monster_width, (int) monster_height).contains(player.get_att_point1())
                    || new Rectangle((int) monster_x, (int) monster_y, (int) monster_width, (int) monster_height).contains(player.get_att_point2())
                    || new Rectangle((int) monster_x, (int) monster_y, (int) monster_width, (int) monster_height).contains(player.get_att_point3()))) {
                map.getCurrentMap().arrayMonster.get(i).decreaseHP(player.getATK()); // chua co max att nen dung tam att base

                if (player.getX() < map.getCurrentMap().arrayMonster.get(i).getX()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.LEFT);
                } else if (player.getX() > map.getCurrentMap().arrayMonster.get(i).getX()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.RIGHT);
                } else if (player.getY() < map.getCurrentMap().arrayMonster.get(i).getY()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.UP);
                } else if (player.getY() > map.getCurrentMap().arrayMonster.get(i).getY()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.DOWN);
                }
            }
            if (player.get_fireball_att() == true
                    && (new Rectangle((int) monster_x, (int) monster_y, (int) monster_width, (int) monster_height).contains(player.get_fireball_att_point()))) {
                map.getCurrentMap().arrayMonster.get(i).decreaseHP(player.get_ATK_FIREBALL()); // chua co max att nen dung tam att base

                if (player.getX() < map.getCurrentMap().arrayMonster.get(i).getX()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.LEFT);
                } else if (player.getX() > map.getCurrentMap().arrayMonster.get(i).getX()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.RIGHT);
                } else if (player.getY() < map.getCurrentMap().arrayMonster.get(i).getY()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.UP);
                } else if (player.getY() > map.getCurrentMap().arrayMonster.get(i).getY()) {
                    map.getCurrentMap().arrayMonster.get(i).setDirection(Entity.Direction.DOWN);
                }
            }
        }
    }

    public int xPlayer() {
        return (int) player.getX();
    }

    public int yPlayer() {
        return (int) player.getY();
    }

    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.translate(-player.x + 512, -player.y + 384);
        map.paint(g2d);
        player.paint(g2d);
    }

}

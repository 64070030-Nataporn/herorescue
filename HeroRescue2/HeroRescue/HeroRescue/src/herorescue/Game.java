/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herorescue;

import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import java.awt.*;

public class Game implements Runnable {
    private GameWindow gameWindow; //หน้าต่างเกม
    private GamePanel gamePanel; //panel ของเกม
    private Thread gameThread; //thread ที่ใช้สำหรับเล่นเกม
    private final int FPS_SET = 144; //ภาพต่อวิที่ใช้
    private final int UPS_SET = 200; //อัพเดตต่อวิ

    private Playing playing;
    private Menu menu;

    public final static int tiles_defalut_size = 32;
    public final static float scale = 1.5f;
    public final static int tileswidth = 26;
    public final static int tilesheight = 14;
    public final static int tilessize = (int) (tiles_defalut_size * scale);
    public final static int gamewidth = tilessize * tileswidth;
    public final static int gameheight = tilessize * tilesheight;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {

        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        //เริ่มใช้ method run ที่อยู่ใน runnable เพื่อให้เกมสามารถเล่นได้ต่อเนื่อง
        //และโหลดภาพได้ต่อเนื่อง
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {

        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }

    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            //การ run โดยจะเช็คเวลาปัจจุบันและดูความต่างของเวลา และดูเรื่อง frame ต่อวิ
            //และการ upload ภาพต่อวิ และจะรันเช็คเรื่อยๆ
            //มีไว้เพื่อใช้ในการโหลดภาพที่กำลังเล่นอยู่
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }

        }
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING) {
            playing.getHero().resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Playing getPlaying() {
        return this.playing;
    }
}

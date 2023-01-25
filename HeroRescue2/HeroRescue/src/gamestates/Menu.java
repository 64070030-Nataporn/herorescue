package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import herorescue.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends States implements Statemethods {

    //array เก็บปุ่มที่ใช้
    private MenuButton[] buttons = new MenuButton[2];
    private BufferedImage backgroundImg, bgforest; //bg ที่ใช้
    //ตำแหน่งแกน x, y และความกว้างกับความสูงของเมนู
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons(); //โหลดการใช้ button
        loadBackground(); //โหลด background menu
        //โหลด ิbackground ด้านหลัง
        bgforest = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground() {
        //เมธอดที่โหลดรูป menu และกำหนดจุดที่ตั้งกับความกว้างยาวของเมนู
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.scale);
        menuHeight = (int) (backgroundImg.getHeight() * Game.scale);
        menuX = Game.gamewidth / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.scale);
    }
    private void loadButtons() {
        //นำรูปปุ่มใน array มาสร้างโดยกำหนดขนาดและจุดที่ตั้ง
        buttons[0] = new MenuButton(Game.gamewidth / 2, 
        (int) (170 * Game.scale), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.gamewidth / 2, 
        (int) (250 * Game.scale), 1, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        //เมธอดที่วาดรูปพื้นหลังม วาดเมนู, วาดปุ่ม
        g.drawImage(bgforest, 0, 0, Game.gamewidth, 
        Game.gameheight, null);
        g.drawImage(backgroundImg, menuX, menuY, 
        menuWidth, menuHeight,  null);

        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.applyGamestate();
                }
                break;
            }
        }
        resetButtons();
    }
    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.state = Gamestate.PLAYING;
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {        
    }
}

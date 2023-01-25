package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import herorescue.Game;
import static utilz.Constants.UI.URMButtons.URM_SIZE;
import utilz.LoadSave;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameOverOverlay {
    
    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgW, imgH;
    private UrmButton menu, play;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (335 * Game.scale);
        int playX = (int) (445 * Game.scale);
        int y = (int) (210 * Game.scale);
        play = new UrmButton(playX, y, URM_SIZE, URM_SIZE, 1);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.scale);
		imgH = (int) (img.getHeight() * Game.scale);
		imgX = Game.gamewidth / 2 - imgW / 2;
		imgY = (int) (75* Game.scale);
    }

    private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.gamewidth, Game.gameheight);
        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        play.draw(g);
    }

    public void update() {
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }

    public void mouseMoved(MouseEvent e){
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(menu, e))
            menu.setMouseOver(true);
        else if(isIn(play, e))
            play.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e){
        if(isIn(menu, e)){
            if(menu.isMousePressed()) {
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        }else if(isIn(play, e))
            if(play.isMousePressed()) {
                playing.resetAll();
            }

        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e){
        if(isIn(menu, e))
            menu.setMousePressed(true);
        else if(isIn(play, e))
            play.setMousePressed(true);
    }
}

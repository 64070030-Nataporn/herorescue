package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import entities.EnemyManager;
import entities.Hero;
import levels.LevelManager;
import objects.ObjectManager;
import herorescue.Game;

public class Playing extends States implements Statemethods{
    private Hero hero;
    private LevelManager levelManager;
    private GameOverOverlay gameOverOverlay;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private LevelCompletedOverlay LevelCompletedOverlay;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.gamewidth); //ตอนเลื่อนด่านตามที่อัศวินเดินเวลาไปทางซ้าย
    private int rightBorder = (int) (0.8 * Game.gamewidth); //ตอนเลื่อนด่านตามที่อัศวินเดินเวลาไปทางขวา
    private int maxLvlOffsetX; //ขนาดด่านทั้งหมด

    private BufferedImage backgroundImg;

    private boolean gameOver;
    private boolean lvlCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();
        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
	}

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObject(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        hero = new Hero(200, 200, 32, 32, this);
        hero.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        LevelCompletedOverlay = new LevelCompletedOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
    }

    @Override
    public void update() {
        if (lvlCompleted) {
            LevelCompletedOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        }
        else {
            levelManager.update();
            objectManager.update();
            hero.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), hero);
            checkCloseToBorder(); // ตอนเช็คว่าตำแหน่งของอัศวินอยู่ตรงไหนของหน้าจอเพื่อเลื่อนหน้าจอตาม
        }

    }

    private void checkCloseToBorder() {
        int heroX = (int) hero.getHitbox().x;
        int diff = heroX - xLvlOffset; //ความต่างระหว่างตำแหน่งจุด x ของอัศวินกับหน้าจอ
        
        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder; //เวลาตำแหน่งอยู่ด้านขวามากกว่า
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.gamewidth, Game.gameheight, null);

        levelManager.draw(g, xLvlOffset); //draw level
        hero.render(g, xLvlOffset); 
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);

        if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvlCompleted) {
            LevelCompletedOverlay.draw(g);
        }
        
    }

    public void resetAll() {
        //reset hero enemy เลือด เลเวล
        gameOver = false;
        lvlCompleted = false;
        hero.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObject();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){
        objectManager.checkObjectHit(attackbox);
    }

    public void checkEnemyHit(Rectangle2D.Float attackbox) {
        enemyManager.checkEnemyHit(attackbox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                hero.setAttacking(true);
            }
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (lvlCompleted) {
                LevelCompletedOverlay.mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (lvlCompleted) {
                LevelCompletedOverlay.mouseReleased(e);
            }
        } else {
            gameOverOverlay.mouseReleased(e);
        }
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (lvlCompleted) {
                LevelCompletedOverlay.mouseMoved(e);
            }
        } else {
            gameOverOverlay.mouseMoved(e);
        }
        
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
    }

    public void setMaxLvlOffset(int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                hero.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                hero.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                hero.setJump(true);
                break;
                case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
            }
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                hero.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                hero.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                hero.setJump(false);
                break;
            }
        }
    }

    public void windowFocusLost() {
        hero.resetDirBooleans();
    }

    public Hero getHero() {
        return hero;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }
}

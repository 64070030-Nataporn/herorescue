package entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import gamestates.Playing;
import levels.Levels;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] lizardArr;
    private ArrayList<Lizard> lizards = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Levels level) {
        lizards = level.getLizards();
    }

    public void update(int[][] lvlData, Hero hero){
        boolean isAnyActive = false; //เป็นตัวแปรที่ไว้เช็คศัตรูว่ายังมีอยู่ในด่านไหม
        for(Lizard c : lizards) {
            if (c.isActive()) {
                c.update(lvlData, hero);
                isAnyActive = true;
            }
        }
        if (!isAnyActive) { //ถ้าไม่มีศัตรูอยู่ในด่านแล้ว
            if (playing.getLevelManager().getLevelIndex() == 0 
            && playing.getHero().getPower() == 30) {
                playing.setLevelCompleted(true);
            }
            else if (playing.getLevelManager().getLevelIndex() == 1 
            && playing.getHero().getMaxHealth() == 120) {
                playing.setLevelCompleted(true);
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawLizards(g, xLvlOffset);
    }


    private void drawLizards(Graphics g, int xLvlOffset){ //วาดศัตรูจากarrayออกมาทีละอัน
        for(Lizard c : lizards) {
            if (c.isActive()) {
                g.drawImage(lizardArr[c.getEnemyState()][c.getAniIndex()], 
                (int) c.getHitbox().x - 47 - xLvlOffset + c.flipx(),
                (int) c.getHitbox().y - 14, 
                LIZARD_WIDTH * c.flipw(), 
                LIZARD_HEIGHT, null);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackbox) {
        for (Lizard c : lizards) {
            if (c.isActive()) {
                if (attackbox.intersects(c.getHitbox())) {
                    c.hurt(playing.getHero().getPower()); //ตอนศัตรูโดนฮีโร่โจมตีจะเรียกใช้ method นี้
                    return;
                }
            }
        }
    }

    private void loadEnemyImgs(){
        lizardArr = new BufferedImage[5][6]; //โหลดรูปศัตรูเก็บเป็นarray
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.LIZARD_SPRITE);
        for(int j = 0; j < lizardArr.length;j++)
            for(int i = 0; i < lizardArr[j].length;i++)
                lizardArr[j][i] = temp.getSubimage(i*LIZARD_WIDTH_DEFAULT, 
                j*LIZARD_HEIGHT_DEFAULT, LIZARD_WIDTH_DEFAULT, 
                LIZARD_HEIGHT_DEFAULT);
    }

    public void resetAllEnemies() {
        for (Lizard c : lizards) {
            c.resetEnemy();
        }
    }
}

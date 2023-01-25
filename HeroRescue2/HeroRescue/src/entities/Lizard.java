package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import java.awt.geom.Rectangle2D;
import herorescue.Game;

public class Lizard extends Enemy{

    //attackbox
    private int attackboxOffsetX;

    public Lizard(float x, float y) {
        super(x, y, LIZARD_WIDTH, LIZARD_HEIGHT, LIZARD);
        initHitbox(32, 30);
        initAttackBox();
    }
    
    private void initAttackBox() {
        attackbox = new Rectangle2D.Float(x, y, (int) (82 * Game.scale), (int) (19 * Game.scale));
        attackboxOffsetX = (int) (Game.scale * 30); //attackbox
    }

    public void update(int[][] lvlData, Hero hero){
        updateBehavior(lvlData, hero);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackbox.x = hitbox.x - attackboxOffsetX;
        attackbox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Hero hero){
        if(firstUpdate) 
            firstUpdateCheck(lvlData);
        
        if(inAir)
            updateInAir(lvlData);
        
        else{
            switch(state){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData, hero)) {
                        turnTowardsPlayer(hero);
                    if(isPlayerCloseForAttack(hero))
                        newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }

                    if (aniIndex == 2 && !attackChecked) { //ตอนกำหนด sprite ว่าจะให้ตำแหน่งไหนและรูปไหนสามารถโจมตีได้
                        checkHeroHit(attackbox, hero);
                    }
                    return;
                case HIT:
                    return;
            }
        }
    }

    public int flipx() {
        if (walkDir == RIGHT) {
            return 0;
        } else {
            return width;
        }
    }
    
    public int flipw() {
        if (walkDir == RIGHT) {
            return 1;
        } else {
            return -1;
        }

    }
}

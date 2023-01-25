package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.*;


import herorescue.Game;

public abstract class Enemy extends Entity{
    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = herorescue.Game.tilessize;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType){
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = 0.35f * Game.scale;
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if(!IsEntityOnFloor(hitbox,lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData){
        if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.tilessize);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed = 0;
        if(walkDir == LEFT) {
            xSpeed = -walkSpeed;
        }
        else {
            xSpeed = walkSpeed;
        }
                    
        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if(IsFloor(hitbox, xSpeed, lvlData)){
                hitbox.x += xSpeed;
                return;
            }
        changeWalkDir();
    }

    protected void turnTowardsPlayer(Hero hero){
        if(hero.hitbox.x > hitbox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    protected boolean canSeePlayer(int[][] lvlData, Hero hero){
        int playerTileY = (int)(hero.getHitbox().y / Game.tilessize);
        if(playerTileY == tileY)
            if(isPlayerInRange(hero)){
                if(IsSightClear(lvlData, hitbox, hero.hitbox, tileY))
                    return true;
            }
        return false;
    }

	protected boolean isPlayerInRange(Hero hero) {
        int absValue = (int) Math.abs(hero.hitbox.x  - hitbox.x);
		return absValue <= attackDistance * 5;
	}

    protected boolean isPlayerCloseForAttack(Hero hero){
        int absValue = (int) Math.abs(hero.hitbox.x  - hitbox.x);
		return absValue <= attackDistance;
    }

	protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount) {
        currentHealth -= amount; //check เลือด enemy

        if (currentHealth <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    protected void checkHeroHit(Rectangle2D.Float attackbox, Hero hero) {
        if (attackbox.intersects(hero.hitbox)) {
            hero.changeHealth(-GetEnemyDmg(enemyType)); //เรียกใช้ method ในตอนที่ฮีโร่โดนโจมตี
        }
        attackChecked = true;
    }

    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(enemyType, state)){
                aniIndex = 0;

                switch (state) {
                   case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }

            }
        }
    }

    
     public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAnimationTick(); 
    }
    
    private void updateMove(int[][] lvlData){
        if(firstUpdate) {
            if(!IsEntityOnFloor(hitbox,lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }
        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
            }else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            }
        }
        else{
            switch(state){
                case IDLE:
                    state = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;

                    if(walkDir == LEFT)
                        xSpeed = -walkSpeed;
                    else
                        xSpeed = walkSpeed;
                    
                    if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
                        if(IsFloor(hitbox, xSpeed, lvlData)){
                            hitbox.x += xSpeed;
                            return;
                        }
                    
                    changeWalkDir();
                    break;
            }
        }
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}

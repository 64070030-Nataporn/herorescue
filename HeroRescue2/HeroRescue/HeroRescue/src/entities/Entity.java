package entities;

import java.awt.geom.Rectangle2D;
import herorescue.Game;

public abstract class Entity {
    
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed; //เก็บค่าความเร็วเวลาอยู่บนอากาศ
    protected boolean inAir = false;
    protected int maxHealth; //เลือดตอนเริ่มเกม
    protected int currentHealth; //เลือดในตอนปัจจุบัน
    protected Rectangle2D.Float attackbox;
    protected float walkSpeed = 1.0f * Game.scale;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.scale), (int) (height * Game.scale));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getEnemyState() {
        return state;
    }

    public int getAniIndex(){
        return aniIndex;
    }
}

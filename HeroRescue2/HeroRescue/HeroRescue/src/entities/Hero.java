package entities;

import java.awt.image.BufferedImage;
import java.awt.*;
import static utilz.Constants.GRAVITY;
import gamestates.Playing;
import herorescue.Game;
import utilz.LoadSave;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import java.awt.geom.Rectangle2D;


public class Hero extends Entity {

    private BufferedImage[][] anihero; //array เก็บ sprite อัศวิน
    private int aniSpeed = 30; //ความเร็วการเดินอัศวิน
    private boolean moving = false, attacking = false; //ท่าทางว่าตอนนี้ทำอะไรอยู่
    private boolean left, right, jump; //การหันและกระโดด
    private int[][] lvlData; //ด่านทั้งหมด
    private float yDrawOffset = 4 * Game.scale; //มุมมองแกน y

    // กระโดด
    private float jumpSpeed = -2.5f * Game.scale; // ความเร็วการกระโดดแกน y
    private float fallSpeedAfterCollision = 0.5f * Game.scale; //ความเร็วตอนร่วง

    // เลือด
    private BufferedImage statusbarimg; //รูป status ของอัศวิน

    private int statusBarWidth = (int) (192 * Game.scale); //
    private int statusBarHeight = (int) (58 * Game.scale);
    private int statusBarX = (int) (10 * Game.scale); //ตำแหน่ง health bar แกน x
    private int statusBarY = (int) (10 * Game.scale); //ตำแหน่ง health bar แกน y

    private int healthBarWidth = (int) (157 * Game.scale); // ความกว้างของหลอดเลือด
    private int healthBarHeight = (int) (7 * Game.scale); // ความสูงหลอดเลือด
    private int healthBarXStart = (int) (25 * Game.scale); // จุดเริ่มต้นที่วางในแกน x ของหลอดเลือด
    private int healthBarYStart = (int) (16 * Game.scale); // จุดเริ่มต้นที่วางในแกน y ของหลอดเลือด
    private int healthWidth = healthBarWidth;

    private int power; //พลัง
    private int flipx = 0; //เวลาหันแกน x
    private int flipw = 1; //แกน x ในตอน flip

    private boolean attackChecked; //เช็คตอนโจมตี
    private Playing playing; //ตัวแปรที่ดูภาพรวมเกมทั้งหมด

    public Hero(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height); //กำหนดความสูงความกว้างและจุดที่ตั้ง
        this.playing = playing;
        this.state = IDLE; //state อัศวินในตอนเริ่ม
        this.maxHealth = 100; //ค่าชีวิตเริ่มต้น
        this.power = 10; //ค่าพลังเริ่มต้น
        this.currentHealth = maxHealth; //เลือดปัจจุบันในตอนเริ่ม
        this.walkSpeed = 1.0f * Game.scale; //ความเร็วในการเดิน
        loadAnimations(); //เรียกใช้เพื่อเตรียมโหลดรูป
        initHitbox((int) (16), (int) (26)); // scale hitbox ของอัศวิน
        initAttackBox(); //สร้าง attackbox
    }

    private void initAttackBox() {
        attackbox = new Rectangle2D.Float(x, y, 
        (int) (20 * Game.scale), (int) (20*Game.scale)); //attackhitbox
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            //เช็คเลือดปัจจุบันว่าถ้าน้อยกว่า 0 เกมจบ
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();

        updatePos();
        //เช็คว่าถ้าตัวละครวิ่งอยู่ให้ดูว่าชนกับของอะไรไหม
        if(moving)
            checkPotionTouched();
        
        //เช็คว่าตัวละครโจมตีอะไรอยู่
        if (attacking) {
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();

    }

    private void checkPotionTouched() {
        //เช็คว่าอัศวินวิ่งชนกับ potion ไหม
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        //เช็คตอนอัศวินต่อสู้โดยจะดูทั้งกับตอนสู้ศัตรูกับตีเอาของ
        if (attackChecked || aniIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackbox);
        playing.checkObjectHit(attackbox);
    }

    private void updateAttackBox() {
        if (right) {
            attackbox.x = hitbox.x + hitbox.width 
            + (int) (Game.scale * 5); //ตำแหน่ง attackbox เวลาหันทางขวา
        } else if (left) {
            attackbox.x = hitbox.x - hitbox.width 
            - (int) (Game.scale * 5); //ตำแหน่ง attackbox เวลาหันทางซ้าย
        }
        attackbox.y = hitbox.y + (int) (Game.scale * 6); //ความสูงของ attackbox
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) 
        * healthBarWidth); //ตอนแสดงหลอดเลือดในปัจจุบัน
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(anihero[state][aniIndex], 
        (int) (hitbox.x) - lvlOffset - 5 + flipx, 
        (int) (hitbox.y - yDrawOffset), 
        (int) (width * Game.scale * flipw), 
        (int) (height * Game.scale), null); // render อัศวิน
        drawUI(g); //วาดหลอดเลือด
    }

    private void drawUI(Graphics g) {
        //วาดหลอดเลือดที่เป็นสีแดงโดยวาดเป็นสี่เหลื่ยมผืนผ้า
        g.drawImage(statusbarimg, statusBarX, statusBarY, 
        statusBarWidth, statusBarHeight,  null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, 
        healthBarYStart + statusBarY, 
        healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= GetSpriteAmount(state)) { // animation
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {

        int startAni = state;
        if (moving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (inAir) {
            state = JUMP;
        }

        if (attacking) {
            state = ATTACK;

            if (startAni != ATTACK) {
                aniIndex = 0;
                aniTick = 0;
                return;
            }
        }

        if (startAni != state) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {

        moving = false;

        if (jump) {
            jump();
        }

        if (!inAir) {
            if ((!left && !right) || (right && left)) {
                return;
            }
        }

        float xSpeed = 0;

        if (left) {
            xSpeed -= walkSpeed;
            flipx = width;
            flipw = -1;
        }
        if (right) {
            xSpeed += walkSpeed;
            flipx = 0;
            flipw = 1;
        }

        if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        if (inAir) {

            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, 
            hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }

        } else {
            updateXPos(xSpeed);
        }

        moving = true;

    }

    private void jump() {
        if (inAir) {
            System.out.println("jump");
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
        System.out.println("fall");
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, 
        hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value) {
        currentHealth += value; //method ตอนเช็คเลือด

        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameover();
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    public void changeMaxHealth(int value) {
        maxHealth = value;
        updateHealthBar();
    }

    public void changePower(int value){
        power = value;
    }

    private void loadAnimations() {

        //โหลดรูปเพื่อมาเก็บใน array
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Hero_Atlas);
        //ตัวแปรที่เก็บรูป sprite ของอัศวินทั้งหมด
        anihero = new BufferedImage[4][6]; // อนิเมชั่นของฮีโร่

        for (int j = 0; j < anihero.length; j++) {
            for (int i = 0; i < anihero[j].length; i++) {
                anihero[j][i] = img.getSubimage(i * 32, 
                j * 33, 32, 33); // ตอนนำมาแบ่งเป็น sprite ย่อยๆจากทั้งหมดเพื่อทำอนิเมชั่น
            }
        }

        statusbarimg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR); //load status bar

    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean getLeft() {
        return this.left;
    }

    public int getPower() {
        return power;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean getRight() {
        return this.right;
    }

    public boolean getJump() {
        return this.jump;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }
}

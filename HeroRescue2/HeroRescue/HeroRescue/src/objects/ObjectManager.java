package objects;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.geom.Rectangle2D;
import gamestates.Playing;
import levels.Levels;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing; //ตัวแปรที่ดูภาพรวมเกมทั้งหมด
    //ตัวแปรที่เก็บรูปภาพ sprite แต่ละอย่าง
    private BufferedImage[][] potionImgs, containerImgs, itemImags;
    private ArrayList<Potion> potions; //array ของ potion
    private ArrayList<Item> items; //array ของ item
    private ArrayList<GameContainer> containers; //array ของหีบ

    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox){
        //ลูปสำหรับเช็คตอนที่อัศวินวิ่งมาชนทั้ง potion กับ itemโดยอิงจาก hitbox
        for(Potion p : potions) {
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }

        for(Item i : items) {
            if(i.isActive()){
                if(hitbox.intersects(i.getHitbox())){
                    i.setActive(false);
                    applyItemToPlayer(i);
                }
            }
        }
    }
    private void applyItemToPlayer(Item i) {
        //เมธอดที่เอาไอเทมไปใช้กับอัศวินโดยจะกำหนดไว้
        if(i.getObjType() == SHIELD)
            //ได้โล่จะเพิ่มความจุเลือดของอัศวิน
            playing.getHero().changeMaxHealth(SHIELD_VALUE);
        else
            //ได้ดาบจะได้พลังโจมตีเพิ่มขึ้น
            playing.getHero().changePower(SWORD_VALUE);
    }
    public void applyEffectToPlayer(Potion p){
        //เมธอดที่เอาpotionไปใช้กับอัศวินโดยจะกำหนดไว้
        if(p.getObjType() == RED_POTION)
            //ได้ขวดแดงจะเพิ่มเลือด
            playing.getHero().changeHealth(RED_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){
        for(GameContainer gc : containers)
            if(gc.isActive()){
                if(gc.getHitbox().intersects(attackbox)){
                    gc.setAnimation(true);
                    int type = 0;
                    if(gc.getObjType() == BARREL && 
                    playing.getLevelManager().getLevelIndex() == 0) {
                        type = 4;
                        items.add(new Item((int)(gc.getHitbox().x 
                        + gc.getHitbox().width / 2), 
                        (int)(gc.getHitbox().y - gc.getHitbox().height), 
                        type));
                        return;
                    } 
                    else if (gc.getObjType() == BARREL && 
                    playing.getLevelManager().getLevelIndex() == 1) {
                        type = 5;
                        items.add(new Item((int)(gc.getHitbox().x 
                        + gc.getHitbox().width / 2), 
                        (int)(gc.getHitbox().y - gc.getHitbox().height), 
                        type));
                        return;
                    }
                    
                    else {
                        potions.add(new Potion((int)(gc.getHitbox().x 
                        + gc.getHitbox().width / 2), 
                        (int)(gc.getHitbox().y - gc.getHitbox().height / 2), 
                        type));
                        return;
                    }
                }
            }
    }

    public void loadObject(Levels newLevel) {
        //โหลด potion, item, หีบเพื่อใช้ด่านต่อไป
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
        items = newLevel.getItems();
    }

    public void loadImgs(){
        //เป็นเมธอดที่จะโหลดรูปโดยนำรูปจาก sprite ทั้งของ potion, หีบ, item มาแบ่งเป็นทีละรูป
        //โดยจะเก็บแต่ละรูปไว้ใน array แต่ละประเภท
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for(int j = 0; j < potionImgs.length; j++)
            for(int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12*i, 16*j, 12, 16);
        
        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][7];

        for(int j = 0; j < containerImgs.length; j++)
            for(int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(64*i, 64*j, 64, 64);

        BufferedImage itemSprite = LoadSave.GetSpriteAtlas(LoadSave.SWORD_SHIELD_ATLAS);
        itemImags = new BufferedImage[2][7];
        
        for(int j = 0; j < itemImags.length; j++)
            for(int i = 0; i < itemImags[j].length; i++)
                itemImags[j][i] = itemSprite.getSubimage(64*i, 64*j, 64, 64);
    }

    public void update(){
        //เป็นการเช็คว่าแต่ละอย่างยังมีในด่านไหมหรือหมดไปแล้ว
        for(Potion p : potions)
            if(p.isActive())
                p.update();
        for(GameContainer gc : containers)
            if(gc.isActive())
                gc.update();
        for(Item i : items)
            if(i.isActive())
                i.update();
    }

    public void draw(Graphics g, int xLvlOffset){
        //เป็นเมธอดที่ไว้วาดรูป potion, item, หีบ
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawItems(g, xLvlOffset);
    }

    private void drawItems(Graphics g, int xLvlOffset) {
        //เป็นเมธอดที่ลูปเพื่อวาดไอเทมจาก sprite ที่เก็บไว้ใน array
        for(Item i : items)
            if(i.isActive()){
                int type = 0;
                if(i.getObjType() == SHIELD)
                    type = 1;
                g.drawImage(itemImags[type][i.getAniIndex()], 
                (int) (i.getHitbox().x - i.getxDrawOffset() - xLvlOffset),
                (int) (i.getHitbox().y - i.getyDrawOffset()),
                ITEM_WIDTH,
                ITEM_HEIGHT,
                null);
            }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        //เป็นเมธอดที่วาดหีบสมบัติตาม sprite และดึงมาใช้ทีละรูปตาม array
        for(GameContainer gc : containers)
            if(gc.isActive()){
                int type = 0;
                if(gc.getObjType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][gc.getAniIndex()], 
                (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                (int) (gc.getHitbox().y - gc.getyDrawOffset()), 
                CONTAINER_WIDTH, 
                CONTAINER_HEIGHT,
                null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        //เป็นเมธอดที่ลูปเพื่อวาด potion จากที่เป็น array ออกมาทีละอัน
        for(Potion p : potions)
            if(p.isActive()){
                int type = 0;
                if(p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()], 
                (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                (int) (p.getHitbox().y - p.getyDrawOffset()),
                POTION_WIDTH,
                POTION_HEIGHT,
                null);
            }
        
    }

    public void resetAllObject(){
        for(Potion p : potions)
            p.reset();
        for(GameContainer gc : containers)
            gc.reset();
        for(Item i : items)
            i.reset();
    }
}

package objects;

import herorescue.Game;
 
public class Item extends GameObject{
    
    private float hoverOffset;
    private int maxHoeverOffset, hoverDir = 1;

    public Item(int x, int y, int objType){
        super(x, y, objType);
        doAnimation = true;
        initHitbox(10, 32);
        xDrawOffset = (int) (18*Game.scale);
        yDrawOffset = (int) (2*Game.scale);

        maxHoeverOffset = (int) (10 * Game.scale);
    }

    public void update(){
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075f * Game.scale * hoverDir);

        if(hoverOffset >= maxHoeverOffset)
            hoverDir = -1;
        else if(hoverOffset < 0)
            hoverDir = 1;
        
        hitbox.y = y + hoverOffset;
    }
}

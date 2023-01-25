package objects;

import static utilz.Constants.ObjectConstants.*;

import herorescue.Game;

public class GameContainer extends GameObject{
    public GameContainer(int x, int y, int objType){
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if(objType == BOX){
            initHitbox(25, 18);

            xDrawOffset = (int) (7 * Game.scale);
            yDrawOffset = (int) (12 * Game.scale);
        } else{
            initHitbox(23, 25);

            xDrawOffset = (int) (8 * Game.scale);
            yDrawOffset = (int) (8 * Game.scale);
        }

        hitbox.y += yDrawOffset + (int)(Game.scale * 2);
        hitbox.x += xDrawOffset / 2;
    }

    public void update(){
        if (doAnimation)
            updateAnimationTick();
    }
}
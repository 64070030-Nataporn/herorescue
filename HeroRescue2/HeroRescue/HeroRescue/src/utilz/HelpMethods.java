package utilz;
import herorescue.Game;
import objects.GameContainer;
import objects.Item;
import objects.Potion;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.Constants.EnemyConstants.LIZARD;
import static utilz.Constants.ObjectConstants.*;
import entities.Lizard;
import java.awt.*;
import java.awt.geom.Rectangle2D;
public class HelpMethods {
    
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {

        if (!IsSolid(x, y, lvlData)) {
            if (!IsSolid(x + width, y + height, lvlData)) {
                if (!IsSolid(x + width, y, lvlData)) {
                    if (!IsSolid(x, y + height, lvlData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.tilessize; //ความยาวของ map
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.gameheight) {
            return true;
        }

        float xIndex = x / Game.tilessize;
        float yIndex = y / Game.tilessize;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData){
		int value = lvlData[yTile][xTile];

        if (value < 27) { //tiles map
            return true;
        }
        return false;
        
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.tilessize);

        if (xSpeed > 0) {
            //right
            int tileXPos = currentTile * Game.tilessize;
            int xOffset = (int) (Game.tilessize - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            //left
            return currentTile * Game.tilessize;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.tilessize);

        if (airSpeed > 0) {
            //falling
            int tileYPos = currentTile * Game.tilessize;
            int yOffset = (int) (Game.tilessize - hitbox.height); // เช็คตำแหน่งการยืนว่ายืนลอยจากพื้นไหม
            return tileYPos + yOffset - 1;
        } else {
            //jump
            return currentTile * Game.tilessize;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        // check pixel ที่อยู่ด้านล่าง
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i = 0; i < xEnd - xStart; i++) {
            if(IsSolid(xStart + i, y, lvlData))
                    return false;
                if(!IsSolid(xStart + i, y+1, lvlData))
                    return false;
        }
                
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int) (firstHitbox.x / Game.tilessize);
        int secondXTile = (int) (secondHitbox.x / Game.tilessize);

        if(firstXTile > secondXTile)
            return IsAllTileWalkable(secondXTile, firstXTile,  yTile, lvlData);
        else
           return IsAllTileWalkable(firstXTile, secondXTile,  yTile, lvlData);
        

    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];//ขนาดกว้างxยาว เป็นPixel

        for (int j=0; j < img.getHeight(); j++) {
            for (int i =0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));//เอาค่าRGBจากรูป
                int value = color.getRed(); //เลือกส่วนสีแดง
                if (value >= 48) {
                    value = 0;
                }//
                lvlData[j][i] = value;
            }//ใส่tilesที่มีค่าสีแดงตรงตามtile sprite ของแต่ละindex
        }
        return lvlData;
    }

    public static ArrayList<Lizard> GetLizards(BufferedImage img){
        ArrayList<Lizard> list = new ArrayList<>();

        for (int j=0; j < img.getHeight(); j++) {
            for (int i=0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == LIZARD) {
                   list.add(new Lizard(i * Game.tilessize, j * Game.tilessize));  
                }  
            }
        }
        return list;
    }

    public static Point GetHeroSpawn(BufferedImage img) {
        for (int j=0; j < img.getHeight(); j++) {
            for (int i=0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100) {
                    return new Point(i * Game.tilessize, j * Game.tilessize);
                }  
            }
        }
        return new Point(1 * Game.tilessize, 1 * Game.tilessize);
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img){
        ArrayList<Potion> list = new ArrayList<>();

        for (int j=0; j < img.getHeight(); j++) {
            for (int i=0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == RED_POTION || value == BLUE_POTION) 
                   list.add(new Potion(i * Game.tilessize, j * Game.tilessize, value));  
    
            }
        }
        return list;
    }

    public static ArrayList<Item> GetItems(BufferedImage img){
        ArrayList<Item> list = new ArrayList<>();

        for (int j=0; j < img.getHeight(); j++) {
            for (int i=0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SWORD || value == SHIELD) 
                   list.add(new Item(i * Game.tilessize, j * Game.tilessize, value));  
    
            }
        }
        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img){
        ArrayList<GameContainer> list = new ArrayList<>();

        for (int j=0; j < img.getHeight(); j++) {
            for (int i=0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == BOX || value == BARREL) 
                   list.add(new GameContainer(i * Game.tilessize, j * Game.tilessize, value));  
    
            }
        }
        return list;
    }
}


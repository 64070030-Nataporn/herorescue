package levels;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import gamestates.Gamestate;
import java.awt.*;
import herorescue.Game;
import utilz.LoadSave;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Levels> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            Gamestate.state = Gamestate.MENU;
        }

        Levels newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getHero().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObject(newLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (int i = 0; i < allLevels.length; i++) {
            levels.add(new Levels(allLevels[i]));
        }
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.Level_Atlas); //โหลดimgที่เป็นtile สำหรับด่าน
        levelSprite = new BufferedImage[48]; //sprite ด่านโดยมีขนาด 48

        for(int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
        }//เก็บรูปtileแต่ละsprite ลงในarray
    }

    public void draw(Graphics g, int lvlOffset){
        BufferedImage[] allbgs = LoadSave.GetAllbglvl();//array ของ bg แต่ละด่าน
        g.drawImage(allbgs[lvlIndex], 0, 0, Game.gamewidth, Game.gameheight, null); //วาดbgสำหรับด่านนั้นๆ
        for (int j =0; j < Game.tilesheight; j++) {
            for (int i=0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);//indexของtile ของด่านนั้นๆ
                g.drawImage(levelSprite[index], Game.tilessize * i - lvlOffset, Game.tilessize * j, Game.tilessize, Game.tilessize, null);
            }
        }//วาดtileแต่ละindexของlvlนั้นๆ
    }

    public void update() {

    }

    public Levels getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
		return levels.size();
	}

	public int getLevelIndex() {
		return lvlIndex;
	}

}

package levels;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetLizards;
import static utilz.HelpMethods.GetHeroSpawn;
import entities.Lizard;
import herorescue.Game;
import objects.GameContainer;
import objects.Item;
import objects.Potion;
import utilz.HelpMethods;

import java.awt.*;

public class Levels {
    
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Lizard> lizards;
    private ArrayList<Potion> potions;
    private ArrayList<Item> items;
    private ArrayList<GameContainer> containers;
    private int lvlTilesWide; //max �?วามยาวด�?า�?
    private int maxTilesOffset; //ส�?ว�?ที�?อยู�?�?อ�?�?อที�?�?สด�?
    private int maxLvlOffsetX;
    private Point heroSpawn;

    public Levels(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createItems();
        createContainers();
        calcLvlOffsets();
        calcHeroSpawn();
    }

    private void createContainers() {
        containers = HelpMethods.GetContainers(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    private void createItems() {
        items = HelpMethods.GetItems(img);
    }

    private void calcHeroSpawn() {
        heroSpawn = GetHeroSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.tileswidth;
		maxLvlOffsetX = Game.tilessize * maxTilesOffset;
    }

    private void createEnemies() {
        lizards = GetLizards(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Lizard> getLizards() {
		return lizards;
	}

    public Point getHeroSpawn() {
        return heroSpawn;
    }

    public ArrayList<Potion> getPotions(){
        return potions;
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public ArrayList<GameContainer> getContainers(){
        return containers;
    }
}

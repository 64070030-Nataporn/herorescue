package utilz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.URISyntaxException;

public class LoadSave {

    public static final String Hero_Atlas = "hero_sprites.png";
    public static final String Level_Atlas = "all_lvlTiles.png"; //sprite outside
    public static final String MENU_BUTTONS = "pixil-frame-0_20.png";
    public static final String MENU_BACKGROUND = "pixil-frame-0_1.png";
    public static final String MENU_BACKGROUND_IMG = "bg.png";
    public static final String LIZARD_SPRITE = "lizard_sprites.png"; //enemy
    public static final String STATUS_BAR = "hpbar3.png";
    public static final String COMPLETED_IMG = "completed_screen.png"; //level
    public static final String URM_BUTTONS = "urm_bt.png";
    public static final String DEATH_SCREEN = "death_screen2.png";
    public static final String POTION_ATLAS = "potion_sprites.png"; //object
    public static final String CONTAINER_ATLAS = "chest_sprites.png";
	public static final String SWORD_SHIELD_ATLAS = "sword_shield3.png";


    public static BufferedImage GetSpriteAtlas(String filename) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
        try {
            img = ImageIO.read(is);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return img;
    }

    public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls"); //load image level �?�?�?�?ลเดอร�?ทั�?�?หมด
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles(); //array file ด�?า�?
		File[] filesSorted = new File[files.length]; //array file ทั�?�?หมดที�?�?ะเรีย�?

		for (int i = 0; i < filesSorted.length; i++) {
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals("lvl" + (i + 1) + ".png"))
					filesSorted[i] = files[j];
			}
        }

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}

    public static BufferedImage[] GetAllbglvl() {
		URL url = LoadSave.class.getResource("/backgroundlvl"); //load image level �?�?�?�?ลเดอร�?ทั�?�?หมด
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles(); //array file ด�?า�?
		File[] filesSorted = new File[files.length]; //array file ทั�?�?หมดที�?�?ะเรีย�?

		for (int i = 0; i < filesSorted.length; i++) {
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals("bg" + (i + 1) + ".png"))
					filesSorted[i] = files[j];
			}
        }

		BufferedImage[] imgs = new BufferedImage[filesSorted.length];

		for (int i = 0; i < imgs.length; i++)
			try {
				imgs[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return imgs;
	}

}

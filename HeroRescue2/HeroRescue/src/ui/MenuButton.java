package ui;

import java.awt.image.BufferedImage;
import gamestates.Gamestate;
import static utilz.Constants.UI.Buttons.*;
import utilz.LoadSave; 
import java.awt.Rectangle;
import java.awt.Graphics;

public class MenuButton {
    private int xPos, yPos, rowIndex, index; //กำหนดแกน x, y และตำแหน่ง
    private int xoffsetCenter = B_WIDTH/2;
    private Gamestate state; //ดู state เกมว่าตอนนี้อยู่ในช่วงไหน
    private BufferedImage[] imgs; //ตัวแปรไว้เก็บรูป
    private boolean mouseOver, mousePressed;
    private Rectangle bounds; //กรอบของปุ่ม

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos; //จุดตั้งแกน x
        this.yPos = yPos; //จุดตั้งแกน y
        this.rowIndex = rowIndex; //แถวที่ไหนไว้สำหรับเลือกรูป
        this.state = state; //state เกมตอนนี้
        loadImgs();
        initBounds();
    }

    public void initBounds() {
        //ไว้วาดกรอบของปุ่มที่จะสร้าง
        bounds = new Rectangle(xPos-xoffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    public void loadImgs() {
        //โหลดรูปปุ่มที่จะใช้จาก sprite ทั้งหมดมาแบ่งเป็นย่อยๆ
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for(int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, 
            rowIndex * B_HEIGHT_DEFAULT, 
            B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT); //scale button
        }
    }

    public void draw(Graphics g) {
        //วาดปุ่มโดยเอารูปที่เก็บไว้ใน array ที่ได้จากการแบ่ง
        g.drawImage(imgs[index], xPos-xoffsetCenter, yPos, 
        B_WIDTH, B_HEIGHT, null);
    }
    public void update() {
        //เช็คเมาส์ว่าตอนนี้ active กับปุ่มอย่างไรและกำหนดรูปุ่มที่ต้องการแสดงตาม index ของรูป
        index = 0;
        if(mouseOver) {
            index = 1;
        }
        if(mousePressed){
            index = 2;
        }
    } 
    public boolean isMouseOver() {
        return mouseOver;
    }
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
    public boolean isMousePressed() {
        return mousePressed;
    }
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGamestate() {
        Gamestate.state = state;
    }
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}

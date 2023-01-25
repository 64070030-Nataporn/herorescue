package gamestates;

import java.awt.*;
import java.awt.event.*;

public interface Statemethods {
    
    abstract void update();
	abstract void draw(Graphics g);
	abstract void mouseClicked(MouseEvent e);
	abstract void mousePressed(MouseEvent e);
	abstract void mouseReleased(MouseEvent e);
	abstract void mouseMoved(MouseEvent e);
	abstract void keyPressed(KeyEvent e);
	abstract void keyReleased(KeyEvent e);
}

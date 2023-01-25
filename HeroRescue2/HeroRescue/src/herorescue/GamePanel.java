/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herorescue;

import inputs.*;
import javax.swing.*;
import java.awt.*;
import static herorescue.Game.gameheight;
import static herorescue.Game.gamewidth;

public class GamePanel extends JPanel{
    
    private Game game;
    public GamePanel(Game game){
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(new MouseInputs(this));
        addMouseMotionListener(new MouseInputs(this));
    }

    private void setPanelSize() {
        Dimension size = new Dimension(gamewidth, gameheight);
        setPreferredSize(size);
    }
    
    public void updateGame() {
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}

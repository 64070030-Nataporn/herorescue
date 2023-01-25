package herorescue;

import java.awt.event.*;
import javax.swing.*;

public class GameWindow{
    private JFrame fr;
    public GameWindow(GamePanel gamepanel){
        fr = new JFrame();
        
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.add(gamepanel);
        fr.setResizable(false);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setVisible(true);
        fr.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                gamepanel.getGame().windowFocusLost();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                
            }

        });
    }
}

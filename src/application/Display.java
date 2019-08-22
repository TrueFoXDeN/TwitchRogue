package application;

import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setColor(Color.BLACK);

        g.fillRect(0,0,100,100);


    }
}

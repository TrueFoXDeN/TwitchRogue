package drawing;

import application.Main;
import game.algorithms.OpenSimplexNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

public class BattleBackground implements Drawable {

    private final OpenSimplexNoise noise = new OpenSimplexNoise(System.nanoTime());
    private AtomicReference<Double> t = new AtomicReference<>(0.0);

    private final double TIME_OFFSET = 0.001;
    private final double SPACE_OFFSET = 0.01;

    public BattleBackground() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                t.set(t.get() + TIME_OFFSET);
            }
        }, 0, 1);
    }


    @Override
    public void draw(Graphics g) {
        BufferedImage back = new BufferedImage(Main.display.w0, Main.display.h0, BufferedImage.TYPE_3BYTE_BGR);
        double dx = 0.0;
        double dy = 0.0;
        for (int x = 0; x < back.getWidth(); x++, dx += SPACE_OFFSET) {
            dy = 0.0;
            for (int y = 0; y < back.getHeight(); y++, dy += SPACE_OFFSET) {
                int redValue = (int) (125 * (noise.eval(dx, dy, t.get()) + 1) / 2 );
                Color pixelColor = new Color(redValue, 0, 0);
                back.setRGB(x, y, pixelColor.getRGB());
            }
        }

        g.drawImage(back, 0, 0, null);
    }
}

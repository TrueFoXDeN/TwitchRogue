package drawing;

import application.Main;
import game.algorithms.OpenSimplexNoise;
import io.ImageLoader;

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

    private final BufferedImage battleback = ImageLoader.sprites.get("background_battle");

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
        BufferedImage back = new BufferedImage(1080, 720, BufferedImage.TYPE_3BYTE_BGR);
        double dx = 0.0;
        double dy = 0.0;
        final Color noiseColor = Color.BLUE.darker();
        for (int x = 0; x < back.getWidth(); x++, dx += SPACE_OFFSET) {
            dy = 0.0;
            for (int y = 0; y < back.getHeight(); y++, dy += SPACE_OFFSET) {
                double colorScalar = (noise.eval(dx, dy, t.get()) + 1) / 2;
                Color pixelColor = multCol(noiseColor, colorScalar);
                back.setRGB(x, y, pixelColor.getRGB());
            }
        }

        g.drawImage(back, 0, 0, null);
        g.drawImage(battleback, 0, 0, 1080, 720, null);
    }

    private Color multCol(Color color, double scalar) {
        return new Color((int)Math.round(color.getRed() * scalar),
                (int)Math.round(color.getGreen() * scalar),
                (int)Math.round(color.getBlue() * scalar));
    }
}

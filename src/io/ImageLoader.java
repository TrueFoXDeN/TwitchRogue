package io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    public static final Map<String, BufferedImage> sprites = new HashMap<>();

    private ImageLoader() throws IOException {
        sprites.put("cobble",
                ImageIO.read(Files.newInputStream(Paths.get("res", "world", "bg_cobble.png"))));
    }

    public static void load() {
        try {
            new ImageLoader();
        } catch (IOException e) {
            System.err.println("Cant load images!");
        }
    }


}

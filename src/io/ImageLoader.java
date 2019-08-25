package io;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader {

    public static final Map<String, BufferedImage> sprites = new HashMap<>();

    private ImageLoader() {

    }

    public static void load() {
        new ImageLoader();
    }


}

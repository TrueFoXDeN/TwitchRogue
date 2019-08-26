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
        sprites.put("wall", ImageIO.read(Files.newInputStream(Paths.get("res", "world", "bg_wall.png"))));

        sprites.put("player_shadow_idle", ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_shadow.png"))).getSubimage(0,0,14,21));

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_down_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_down.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_left_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_down.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_right_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_down.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_up_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_down.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 7; i++) {
            sprites.put("player_attack_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_attack.png"))).getSubimage(i * 35,0,35,21));
        }

        sprites.put("player_idle_down", sprites.get("player_walk_down_0"));
        sprites.put("player_idle_left", sprites.get("player_walk_left_0"));
        sprites.put("player_idle_right", sprites.get("player_walk_right_0"));
        sprites.put("player_idle_up", sprites.get("player_walk_up_0"));

    }

    public static void load() {
        try {
            new ImageLoader();
        } catch (IOException e) {
            System.err.println("Cant load images!");
        }
    }


}

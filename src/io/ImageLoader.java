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
        sprites.put("cobble",ImageIO.read(Files.newInputStream(Paths.get("res", "world", "bg_cobble.png"))));

        sprites.put("wall", ImageIO.read(Files.newInputStream(Paths.get("res", "world", "bg_wall.png"))));

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_down_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_down.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_left_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_left.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_right_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_right.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_walk_up_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_up.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 7; i++) {
            sprites.put("player_attack_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_attack.png"))).getSubimage(i * 35,0,35,21));
        }

        for (int i = 0; i < 6; i++) {
            sprites.put("player_shadow_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "player", "spr_player_shadow.png"))).getSubimage(i * 14,0,14,21));
        }

        for (int i = 0; i < 5; i++) {
            sprites.put("arrow_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "ui", "arrow_"+i+".png"))));
            sprites.put("arrow_active_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "ui", "arrow_active_"+i+".png"))));
        }

        for (int i = 0; i < 5; i++) {
            sprites.put("action_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "ui", "action_"+i+".png"))));
            sprites.put("action_active_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "ui", "action_active_"+i+".png"))));
        }

        sprites.put("fackel", ImageIO.read(Files.newInputStream(Paths.get("res", "items", "spr_fackel.png"))));
        sprites.put("potion", ImageIO.read(Files.newInputStream(Paths.get("res", "items", "spr_potion.png"))));

        for (int i = 0; i < 4; i++) {
            sprites.put("slime_" + i, ImageIO.read(Files.newInputStream(Paths.get("res", "enemies", "spr_slime.png"))).getSubimage(i * 16,0,16,16));
        }

        sprites.put("background_battle", ImageIO.read(Files.newInputStream(Paths.get("res", "world", "background_battle.png"))));

    }

    public static void load() {
        try {
            new ImageLoader();
        } catch (IOException e) {
            System.err.println("Cant load images!");
        }
    }


}

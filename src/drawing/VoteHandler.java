package drawing;

import game.engine.GameState;
import game.engine.GamestateHandler;
import io.ImageLoader;

import java.awt.*;

public class VoteHandler implements Drawable {

    public static int[] dirVoting = new int[4];
    public int leadingDir = 0;

    private String[] options = new String[4];
    private String[] pictures = new String[4];
    private String[] picturesActive = new String[4];

    public VoteHandler() {
        dirVoting[0] = 1;
        update();
    }

    public static void resetVoting() {
        for (int i = 0; i < dirVoting.length; i++) {
            dirVoting[i] = 0;
        }
    }

    private boolean isNull() {
        for (int i = 0; i < dirVoting.length; i++) {
            if (dirVoting[i] != 0) return false;
        }
        return true;
    }

    public void update() {
        leadingDir = highest();
    }

    private int highest() {
        int max = 0;
        int last = 0;
        for (int i = 0; i < dirVoting.length; i++) {
            if (last < dirVoting[i]) {
                last = dirVoting[i];
                max = i;
            }
        }

        return max;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier New", Font.BOLD, 30));
        switch (GamestateHandler.gameState) {
            case EXPLORE:
                options[0] = "UP: ";
                options[1] = "RIGHT: ";
                options[2] = "DOWN: ";
                options[3] = "LEFT: ";

                for (int i = 0; i < pictures.length; i++) {
                    pictures[i] = "arrow_" + i;
                }
                for (int i = 0; i < picturesActive.length; i++) {
                    picturesActive[i] = "arrow_active_" + i;
                }
                break;
            case BATTLE:
                options[0] = "ATTACK: ";
                options[1] = "DODGE: ";
                options[2] = "BLOCK: ";
                options[3] = "FLEE: ";

                for (int i = 0; i < pictures.length; i++) {
                    pictures[i] = "action_" + i;
                }
                for (int i = 0; i < picturesActive.length; i++) {
                    picturesActive[i] = "action_active_" + i;
                }
                break;
        }
        g.drawString(options[0], 1100, 200);
        g.drawString(options[1], 1100, 230);
        g.drawString(options[2], 1100, 260);
        g.drawString(options[3], 1100, 290);

        g.drawString("" + dirVoting[0], 1225, 200);
        g.drawString("" + dirVoting[2], 1225, 230);
        g.drawString("" + dirVoting[3], 1225, 260);
        g.drawString("" + dirVoting[1], 1225, 290);

        g.drawImage(ImageLoader.sprites.get(pictures[0]), 1150, 10, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[1]), 1210, 65, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[2]), 1150, 120, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[3]), 1090, 65, 50, 50, null);

        if (!isNull()) {
            switch (leadingDir) {
                case 0:
                    g.drawImage(ImageLoader.sprites.get(picturesActive[0]), 1150, 10, 50, 50, null);
                    break;
                case 1:
                    g.drawImage(ImageLoader.sprites.get(picturesActive[1]), 1210, 65, 50, 50, null);
                    break;
                case 2:
                    g.drawImage(ImageLoader.sprites.get(picturesActive[2]), 1150, 120, 50, 50, null);
                    break;
                case 3:
                    g.drawImage(ImageLoader.sprites.get(picturesActive[3]), 1090, 65, 50, 50, null);
                    break;
            }
        }

    }
}

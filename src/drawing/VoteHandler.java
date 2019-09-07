package drawing;

import application.GameLoop;
import game.engine.GamestateHandler;
import io.Commands;
import io.ImageLoader;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static game.engine.GamestateHandler.twitchConnection;

public class VoteHandler implements Drawable, Runnable {

    public static int[] dirVoting = new int[5];
    public int leadingDir = 0;

    private String[] options = new String[5];
    private String[] pictures = new String[5];
    private String[] picturesActive = new String[5];

    private Map<String, Commands> votes = new HashMap<>();
    private Thread voteThread = new Thread(this);

    public VoteHandler() {
        dirVoting[0] = 1;
        update();

        voteThread.setName("voteThread-1");
        voteThread.setDaemon(true);
        voteThread.start();
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
                options[4] = "Potion: ";

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
        g.drawString(options[4], 1100, 320);

        g.drawString("" + dirVoting[0], 1225, 200);
        g.drawString("" + dirVoting[1], 1225, 230);
        g.drawString("" + dirVoting[2], 1225, 260);
        g.drawString("" + dirVoting[3], 1225, 290);
        g.drawString("" + dirVoting[4], 1225, 320);

        g.drawImage(ImageLoader.sprites.get(pictures[0]), 1150, 10, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[1]), 1210, 65, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[2]), 1150, 120, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[3]), 1090, 65, 50, 50, null);
        g.drawImage(ImageLoader.sprites.get(pictures[4]), 1150, 65, 50, 50, null);

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
                case 4:
                    g.drawImage(ImageLoader.sprites.get(picturesActive[4]), 1150, 65, 50, 50, null);
                    break;
            }
        }

        g.drawImage(ImageLoader.sprites.get("potion"), 1100, 350,35,45, null);
        g.drawString(":", 1140,380);
        g.drawString(GameLoop.gHandler.getPlayer().getPotions().size()+"", 1160, 382);
        if(GameLoop.gHandler.getPlayer().useTorch){
            g.drawImage(ImageLoader.sprites.get("fackel"), 1100, 420,25,70, null);
        }


    }

    @Override
    public void run() {
        while(!voteThread.isInterrupted()) {
            if(twitchConnection != null) {
                try {
                    String data = twitchConnection.getMessages().take();
                    int begin = data.indexOf(":") + 1;
                    int end = data.indexOf("!");

                    String name = data.substring(begin, end);
                    String dataParts[] = data.split(" ");
                    String msg = dataParts[dataParts.length - 1];

                    Commands cmd = Commands.checkCommand(msg);
                    if(cmd != null) votes.put(name, cmd);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

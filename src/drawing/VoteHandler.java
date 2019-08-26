package drawing;

import game.engine.GamestateHandler;
import io.ImageLoader;

import java.awt.*;

public class VoteHandler implements Drawable{

    public static int[] dirVoting = new int[4];
    public int leadingDir = 0;

    public VoteHandler(){
        dirVoting[0] = 1;
        update();
    }

    public void update(){
        leadingDir = highest();
    }

    private int highest(){
        int max = 0;
        int last = 0;
        for (int i = 0; i < dirVoting.length; i++) {
            if(last < dirVoting[i]){
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
        g.drawString("UP: ", 1100, 200);
        g.drawString("DOWN: ", 1100, 230);
        g.drawString("LEFT: ", 1100, 260);
        g.drawString("RIGHT: ", 1100, 290);

        g.drawString(""+dirVoting[0], 1220, 200);
        g.drawString(""+dirVoting[2], 1220, 230);
        g.drawString(""+dirVoting[3], 1220, 260);
        g.drawString(""+dirVoting[1], 1220, 290);

        g.drawImage(ImageLoader.sprites.get("arrow_0"), 1150,10, null);
        g.drawImage(ImageLoader.sprites.get("arrow_1"), 1190,50, null);
        g.drawImage(ImageLoader.sprites.get("arrow_2"), 1150,90, null);
        g.drawImage(ImageLoader.sprites.get("arrow_3"), 1110,50, null);

        switch (leadingDir){
            case 0: g.drawImage(ImageLoader.sprites.get("arrow_active_0"), 1150,10, null);
                break;
            case 1: g.drawImage(ImageLoader.sprites.get("arrow_active_1"), 1190,50, null);
                break;
            case 2: g.drawImage(ImageLoader.sprites.get("arrow_active_2"), 1150,90, null);
                break;
            case 3: g.drawImage(ImageLoader.sprites.get("arrow_active_3"), 1110,50, null);
                break;
        }

    }
}

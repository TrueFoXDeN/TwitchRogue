package application;

import drawing.VoteHandler;
import game.Player;
import game.engine.GamestateHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        Player p = GameLoop.gHandler.getPlayer();
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            GameLoop.gHandler.movePlayer(0, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            GameLoop.gHandler.movePlayer(-1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            GameLoop.gHandler.movePlayer(1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            GameLoop.gHandler.movePlayer(0, -1);
        }

        if(e.getKeyCode() == KeyEvent.VK_W){
            VoteHandler.dirVoting[0] ++;
            GamestateHandler.voteHandler.update();
        }else if(e.getKeyCode() == KeyEvent.VK_D){
            VoteHandler.dirVoting[1] ++;
            GamestateHandler.voteHandler.update();
        }else if(e.getKeyCode() == KeyEvent.VK_S){
            VoteHandler.dirVoting[2] ++;
            GamestateHandler.voteHandler.update();
        }else if(e.getKeyCode() == KeyEvent.VK_A){
            VoteHandler.dirVoting[3] ++;
            GamestateHandler.voteHandler.update();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

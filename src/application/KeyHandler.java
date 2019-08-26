package application;

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
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            p.move(0,1);
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            p.move(-1,0);
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            p.move(1,0);
        }else if(e.getKeyCode() == KeyEvent.VK_UP){
            p.move(0,-1);
        }

       GamestateHandler.currentMaze.updateVision(p.getPos());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

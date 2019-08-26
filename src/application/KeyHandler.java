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
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            GameLoop.gHandler.movePlayer(0, 1);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            GameLoop.gHandler.movePlayer(-1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            GameLoop.gHandler.movePlayer(1, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            GameLoop.gHandler.movePlayer(0, -1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

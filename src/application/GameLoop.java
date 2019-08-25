package application;

import game.engine.GamestateHandler;

public class GameLoop extends Thread{

    // handles the state of the game
    private final GamestateHandler gHandler = new GamestateHandler();

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while(!isInterrupted()){
            long now = System.nanoTime();
            delta += (lastTime - now) / ns;
            lastTime = now;

            if(delta >= 1){
                delta--;
            }

            update();

            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                // System.out.println(updates + " Ticks, FPS: "+ frames);
                updates = 0;
                frames = 0;
            }
        }

    }

    private void update(){
        gHandler.update();
    }

    public void stopThread(){
        interrupt();
    }
}


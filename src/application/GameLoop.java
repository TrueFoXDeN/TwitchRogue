package application;

import game.engine.GamestateHandler;
import io.ImageLoader;

public class GameLoop extends Thread{

    // handles the state of the game
    public static final GamestateHandler gHandler = new GamestateHandler();
    double delta = 0;
    @Override
    public void run() {

        init();

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        long lastFpsTime = 0;

        while(!isInterrupted()){
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            lastFpsTime += updateLength;
            if(lastFpsTime >= 1000000000){
                lastFpsTime = 0;
            }

            update(delta);

            try{
                Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
            }catch(Exception e){
            }
        }

    }

    private void update(double data){
        gHandler.update(delta);
    }

    private void init() {
        ImageLoader.load();
    }

    public void stopThread(){
        interrupt();
    }
}


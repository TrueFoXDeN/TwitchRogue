package application;

public class GameLoop extends Thread{

    boolean running;

    long lastTime = System.nanoTime();
    final double ammountOfTicks = 60.0;
    double ns = 1000000000 / ammountOfTicks;
    double delta = 0;

    int updates = 0;
    int frames = 0;
    long timer = System.currentTimeMillis();

    @Override
    public void run() {

        while(running){
            long now = System.nanoTime();
            delta += (lastTime - now) / ns;
            lastTime = now;

            if(delta >= 1){
                update();
                delta --;
            }

            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + "Ticks, FPS: "+ frames);
                updates = 0;
                frames = 0;
            }
        }

    }

    public void update(){

    }

    public void render(){
        Main.frame.repaint();
    }

    public void stopThread(){
        running = false;
    }
}


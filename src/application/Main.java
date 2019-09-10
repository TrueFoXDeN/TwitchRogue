package application;

import drawing.Display;
import game.algorithms.OpenSimplexNoise;
import io.ImageLoader;

public class Main {

    public static final boolean DEBUGGING_VISION = true;
    public static final Display display = new Display(1280, 750, "Twitch Roguelike by FoX and GertrundeltHD");

    public static void main(String[] args) {
        ImageLoader.load();

        GameLoop loop = new GameLoop();
        loop.start();
    }

}

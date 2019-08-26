package application;

import drawing.Display;
import io.ImageLoader;

public class Main {

    public static final Display display = new Display(1280, 750, "Twitch Roguelike by FoX and GertrundeltHD");

    public static void main(String[] args) {
        ImageLoader.load();

        GameLoop loop = new GameLoop();
        loop.start();
    }

}

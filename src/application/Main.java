package application;

import drawing.Display;

public class Main {

    public static final Display display = new Display(1280, 750, "Twitch Roguelike by FoX and GertrundeltHD");

    public static void main(String[] args) {
        GameLoop loop = new GameLoop();
        loop.start();
    }

}

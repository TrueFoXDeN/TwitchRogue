package application;

import drawing.Display;

public class Main {

    public static final Display display = new Display(1280, 720, "Twitch Roguelike by FoX and KoKoKotlin");

    public static void main(String[] args) {
        GameLoop loop = new GameLoop();
        loop.start();
    }

}

package application;

import drawing.Display;

public class Main {

    public static void main(String[] args) {

        Display display = new Display(1280, 720, "Twitch Roguelike by FoX and KoKoKotlin");
        GameLoop loop = new GameLoop();
        loop.start();
    }

}

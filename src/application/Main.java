package application;

import drawing.Display;

public class Main {

    public static final Display display = new Display(1280, 720, "Twitch Roguelike by FoX and GertrundeltHD");

    // getrundelthd
    // oauth:diuxe1i0k8c6wsz7egej21asnx83pq
    // linkus7

    public static void main(String[] args) {
        GameLoop loop = new GameLoop();
        loop.start();
    }

}

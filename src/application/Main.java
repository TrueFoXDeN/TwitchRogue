package application;

import gui.MainFrame;

public class Main {
    public static MainFrame frame;
    public static void main(String[] args) {


        frame = new MainFrame();
        GameLoop loop = new GameLoop();
        loop.start();
    }

}

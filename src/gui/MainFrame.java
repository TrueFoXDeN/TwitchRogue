package gui;

import application.Display;

import javax.swing.*;

public class MainFrame extends JFrame{

    public MainFrame(){
        setTitle("Twitch Roguelike by FoX DeN and KoKoKotlin");
        setBounds(0,0,1280,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Display d = new Display();

        setVisible(true);
    }

}

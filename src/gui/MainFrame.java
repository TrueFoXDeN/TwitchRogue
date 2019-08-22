package gui;

import application.Display;

import javax.swing.*;

public class MainFrame extends JFrame{

    public MainFrame(){
        setTitle("Twitch Roguelike by FoX DeN and KoKoKotlin");
        setSize(1280,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Display d = new Display();
        d.setBounds(0,0,100,100);
        d.setVisible(true);
        add(d);

        setVisible(true);
    }

}

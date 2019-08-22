package application;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Display extends Canvas implements Runnable {

    private JFrame window;
    private JPanel contentPane = new JPanel();
    private Thread drawThread = new Thread(this);

    private int w0, h0;

    private List<Drawable> drawables = new CopyOnWriteArrayList<>();

    public Display(int w0, int h0, String title) {

        this.w0 = w0;
        this.h0 = h0;

        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Insets insets = window.getInsets();
        contentPane.setSize(new Dimension(w0 + insets.left + insets.right, h0 + insets.bottom + insets.top));

        window.setContentPane(contentPane);
        setBackground(Color.WHITE);
        setSize(new Dimension(w0, h0));
        contentPane.add(this);
        window.pack();
        window.setLocationRelativeTo(null);

        window.setResizable(false);
        window.setVisible(true);

        createBufferStrategy(3);

        drawThread.setDaemon(true);
        drawThread.start();
    }

    public void draw() {
        Graphics g = getBufferStrategy().getDrawGraphics();
        g.clearRect(0, 0, w0, h0);

        for (Drawable d : drawables) {
            d.draw(g);
        }

        getBufferStrategy().show();
        g.dispose();
    }

    @Override
    public void run() {
        while (drawThread.isAlive()) {
            draw();

            // slow down drawing to gain better performance
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }
}

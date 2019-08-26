package drawing;

import application.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Display extends Canvas implements Runnable {

    private JFrame window;
    private JPanel contentPane = new JPanel();
    private Thread drawThread = new Thread(this);

    private int w0, h0;

    private List<Drawable> drawables = new CopyOnWriteArrayList<>();
    private Drawable background;

    public Display(int w0, int h0, String title) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        JMenuBar menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        JMenu mnDatei = new JMenu("Connection");
        menuBar.add(mnDatei);

        JMenuItem mntmBeenden = new JMenuItem("Login...");
        mntmBeenden.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                LoginFrame login = new LoginFrame();
                login.setVisible(true);

            }
        });
        mnDatei.add(mntmBeenden);

        window.setResizable(false);
        window.setVisible(true);

        createBufferStrategy(3);

        drawThread.setName("draw-01");
        drawThread.setDaemon(true);
        drawThread.start();
    }

    public void draw() {
        Graphics g = getBufferStrategy().getDrawGraphics();

        g.clearRect(0, 0, w0, h0);

        if (background != null) background.draw(g);

        for (Drawable d : drawables) {
            d.draw(g);
        }

        getBufferStrategy().show();
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

    public Drawable getBackground_() {
        return this.background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }
}

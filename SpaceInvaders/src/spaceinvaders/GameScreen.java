/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author mzijlstra
 */
public class GameScreen extends JFrame {

    public static final int GWIDTH = 640;
    public static final int GHEIGHT = 480;

    private final BufferedImage buff;
    private final Canvas canvas;
    private final Graphics2D bufferG;

    public GameScreen(KeyboardInput k) {
        addKeyListener(k);

        buff = new BufferedImage(GWIDTH, GHEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        bufferG = buff.createGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        bufferG.addRenderingHints(hints);
        bufferG.setBackground(Color.white);

        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(GWIDTH, GHEIGHT);
        canvas.addKeyListener(k);

        getContentPane().add(canvas);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIgnoreRepaint(true);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void clearBuffer() {
        bufferG.clearRect(0, 0, GWIDTH, GHEIGHT);
    }

    // has a display-able object draw itself onto the buffer
    public void display(Displayable d) {
        d.display(bufferG);
    }

    public void showBuff() {
        Graphics onscreen = canvas.getGraphics();
        onscreen.drawImage(buff, 0, 0, null);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author mzijlstra
 */
public class SpaceInvaders {

    private final GameScreen screen;
    private final KeyboardInput keyboard;
    private final ArrayList<Tank> tanks = new ArrayList<>();
    private final AbstractAlienGroup aliens = new StandardAlienGroup();
    private final Tank tank = new Tank();

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new SpaceInvaders();
    }

    public SpaceInvaders() throws InterruptedException {
        keyboard = new KeyboardInput();
        screen = new GameScreen(keyboard);
        tanks.add(tank);

        // variables needed for game loop
        long fps = 60;
        long startTime;
        long execTime;
        long sleepTime;
        long frameTime = 1000 / fps;
        boolean running = true;

        // game loop
        while (running) {
            startTime = System.currentTimeMillis();

            running = checkInput();
            updatePositions();
            drawGraphics();

            // use a properly timed game loop
            execTime = System.currentTimeMillis() - startTime;
            sleepTime = frameTime - execTime;
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }
        }
        System.exit(0);
    }

    private boolean checkInput() {
        if (keyboard.keyDown(KeyEvent.VK_SPACE)) {
            tank.fire();
        }
        if (keyboard.keyDown(KeyEvent.VK_A)) {
            tank.goLeft();
        }
        if (keyboard.keyDown(KeyEvent.VK_D)) {
            tank.goRight();
        }
        if (keyboard.keyDown(KeyEvent.VK_LEFT)) {
            tank.goLeft();
        }
        if (keyboard.keyDown(KeyEvent.VK_RIGHT)) {
            tank.goRight();
        }
        return !keyboard.keyDown(KeyEvent.VK_ESCAPE);
    }

    private void updatePositions() {
        tanks.stream().forEach((Tank t) -> {
            t.update();
            aliens.acceptVisitor(t);
        });
        aliens.update();
    }

    private void drawGraphics() {
        screen.clearBuffer();
        for (Tank t : tanks) {
            screen.display(t);
        }
        screen.display(aliens);
        screen.showBuff();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author mzijlstra
 */
public class Tank extends AbstractMovable implements Visitor {

    public boolean left;
    public boolean right;
    public boolean fire;

    private static BufferedImage image;
    private String id;
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private int reload = 0;

    static {
        try {
            image = ImageIO.read(new File("res/tank.png"));
        } catch (IOException ex) {
            System.err.println("Cannot load res/tank.png");
        }
    }

    public Tank() {
        this("1");
    }

    public Tank(String id) {
        super(GameScreen.GWIDTH / 2 - 16, GameScreen.GHEIGHT - 20, 32, 10);
        this.id = id;
        w = 32;
        h = 20;
        x = GameScreen.GWIDTH / 2 - w / 2;
        y = GameScreen.GHEIGHT - h;
        bullets.add(new Bullet(this));
        bullets.add(new Bullet(this));
        bullets.add(new Bullet(this));
    }

    @Override
    public void display(Graphics2D g) {
//        g.fillRect(x, y, w, h);
//        g.fillRect(x + w / 2 -2, y - 4, 4, 4);
        g.drawImage(image, x, y - 3, null);
        bullets.stream().forEach((b) -> {
            b.display(g);
        });
    }

    public void update() {
        if (left && right) {
            // do nothing
        } else if (left && canMoveLeft()) {
            moveLeft();
        } else if (right && canMoveRight()) {
            moveRight();
        }
        left = false;
        right = false;

        bullets.stream().forEach((b) -> {
            if (!b.isActive() && reload == 0 && fire) {
                b.fire();
                fire = false;
                reload = 25;
            } else if (b.isActive()) {
                b.update();
            }
        });
        fire = false;
        if (reload != 0) {
            reload--;
        }

    }

    public void goLeft() {
        left = true;
    }

    public void goRight() {
        right = true;
    }

    public void fire() {
        fire = true;
    }

    @Override
    public void visit(Alien a) {
        bullets.stream().filter((b) -> (b.isActive())).forEach((b) -> {
            b.visit(a);
        });
        if (a.collides(this)) {
            System.out.println("Collided with an Alien");
            System.exit(0);
        }
    }

    @Override
    public void visit(Bullet b) {
        if (collides(b)) {
            System.out.println("Hit by an Alien Bomb");
            System.exit(0);
        }
    }
}

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
public class Alien extends AbstractMovable implements AlienComponent {

    static {
        try {
            image = ImageIO.read(new File("res/ufo.png"));
        } catch (IOException ex) {
            System.err.println("Cannot load res/ufo.png");
        }
    }

    private static BufferedImage image;

    private boolean active = true;
    private final AbstractAlienGroup parent;
    private AlienComponent next = null;
    private AlienComponent prev = null;
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private double dropChance = 0.05;

    public Alien(int x, int y, AbstractAlienGroup p) {
        super(x, y, 20, 20);
        parent = p;
        this.x = x;
        this.y = y;
        w = 20;
        h = 20;
        speed = 1.2;
        bullets.add(new Bullet(this));
    }

    @Override
    public void moveDown() {
        y += 5;
    }

    @Override
    public boolean canMoveUp() {
        if (!active) {
            if (next != null) {
                return next.canMoveUp();
            } else {
                return true;
            }
        }

        boolean can = super.canMoveUp();
        if (next != null) {
            return can && next.canMoveUp();
        }
        return can;
    }

    @Override
    public boolean canMoveDown() {
        if (!active) {
            if (next != null) {
                return next.canMoveDown();
            } else {
                return true;
            }
        }

        boolean can = super.canMoveDown();
        if (next != null) {
            return can && next.canMoveDown();
        }
        return can;
    }

    @Override
    public boolean canMoveLeft() {
        if (!active) {
            if (next != null) {
                return next.canMoveLeft();
            } else {
                return true;
            }
        }

        boolean can = super.canMoveLeft();
        if (next != null) {
            return can && next.canMoveLeft();
        }
        return can;
    }

    @Override
    public boolean canMoveRight() {
        if (!active) {
            if (next != null) {
                return next.canMoveRight();
            } else {
                return true;
            }
        }

        boolean can = super.canMoveRight();
        if (next != null) {
            return can && next.canMoveRight();
        }
        return can;
    }

    @Override
    public AlienComponent getNext() {
        return next;
    }

    @Override
    public void setNext(AlienComponent next) {
        this.next = next;
    }

    @Override
    public void display(Graphics2D g) {
        if (active) {
//            g.setColor(Color.green);
//            g.fillOval(x, y + h / 2, w, h / 2);
//            g.drawOval(x + w / 4, y + 3, w / 2, h / 2);
            g.drawImage(image, x, y, null);
        }
        bullets.stream().filter((b) -> (b.isActive())).forEach((b) -> {
            b.display(g);
        });
    }

    @Override
    public AlienComponent getPrev() {
        return prev;
    }

    @Override
    public void setPrev(AlienComponent p) {
        this.prev = p;
    }

    @Override
    public void update() {
        double rand = Math.random() * 100;
        if (active && rand <= dropChance) {
            for (Bullet b : bullets) {
                if (!b.isActive()) {
                    b.drop();
                    break;
                }
            }
        }

        bullets.stream().filter((b) -> (b.isActive())).forEach((b) -> {
            b.update();
        });
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        parent.reportDeath(this);
        this.active = active;
    }

    public double getDropChance() {
        return dropChance;
    }

    public void setDropChance(double dropChance) {
        this.dropChance = dropChance;
    }

    public void addBullet(Bullet b) {
        bullets.add(b);
    }

    @Override
    public void acceptVisitor(Visitor v) {
        bullets.stream().filter((b) -> (b.isActive())).forEach((b) -> {
            v.visit(b);
        });

        if (active) {
            v.visit(this);
        }
    }
}

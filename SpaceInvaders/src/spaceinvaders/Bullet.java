/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author mzijlstra
 */
public class Bullet extends AbstractMovable implements Visitor {

    private final Movable parent;
    private boolean active;
    private int exploding = -1;

    public Bullet(Movable p) {
        super(0, 0, 5, 5);
        parent = p;
        active = false;
    }

    public void fire() {
        active = true;
        speed = 3.5;
        x = parent.getX() + parent.getW() / 2;
        y = parent.getY() - 5;
        resetMovable();
    }

    public void drop() {
        active = true;
        speed = -3.5;
        x = parent.getX() + parent.getW() / 2;
        y = parent.getY() + parent.getH() + 5;
        resetMovable();
    }

    public boolean isActive() {
        return active;
    }

    public void update() {
        if (active && canMoveUp() && canMoveDown()) {
            moveUp(); // will move down if 'dropped' due to neg speed
        } else {
            if (exploding < 0) {
                explode();
            }
        }

        if (exploding > 0) {
            exploding--;
        } else if (exploding == 0) {
            active = false;
            x = -10;
            y = -10;
            w = 5;
            h = 5;
            exploding--;
        }
    }

    public void explode() {
        if (exploding < 0) {
            speed = 0;
            exploding = 10;
            x -= 4;
            y -= 4;
            w += 8;
            h += 8;
        }
    }

    @Override
    public void display(Graphics2D g) {
        if (active && exploding < 0) {
            g.setColor(Color.gray);
            g.fillOval(x, y, w, h);
        } else if (exploding > 0) {
            g.setColor(Color.red);
            g.fillOval(x - 3, y - 3, 11, 11);
        }
    }

    @Override
    public void visit(Alien a) {
        if (a.collides(this) && exploding < 0) {
            a.setActive(false);
            explode();
        }
    }

    @Override
    public void visit(Bullet b) {
        if (b.collides(this)) {
            b.explode();
            this.explode();
        }
    }

}

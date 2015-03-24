/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

/**
 *
 * @author mzijlstra
 */
public abstract class AbstractMovable extends AbstractCollidable implements Movable {

    protected double speed = 1;
    private double rx;
    private double ry;

    public AbstractMovable(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.rx = x;
        this.ry = y;
    }

    public void resetMovable() {
        rx = x;
        ry = y;
    }

    @Override
    public boolean canMoveUp() {
        return ry > 0 + speed;
    }

    @Override
    public void moveUp() {
        ry -= speed;
        y = (int) ry;
    }

    @Override
    public boolean canMoveDown() {
        return ry < GameScreen.GHEIGHT - h - speed;
    }

    @Override
    public void moveDown() {
        ry += speed;
        y = (int) ry;
    }

    @Override
    public boolean canMoveLeft() {
        return rx > 0 + speed;
    }

    @Override
    public void moveLeft() {
        rx -= speed;
        x = (int) rx;
    }

    @Override
    public boolean canMoveRight() {
        return rx < GameScreen.GWIDTH - w - speed;
    }

    @Override
    public void moveRight() {
        rx += speed;
        x = (int) rx;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }

}

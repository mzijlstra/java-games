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
public abstract class AbstractCollidable implements Collidable {

    protected int x;
    protected int y;
    protected int w;
    protected int h;

    @Override
    public boolean collides(Collidable other) {
        return contains(other.getX(), other.getY())
                || contains(other.getX() + other.getW(), other.getY())
                || contains(other.getX(), other.getY() + other.getH())
                || contains(other.getX() + other.getW(),
                        other.getY() + other.getH());
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= this.x
                && x <= this.x + this.w
                && y >= this.y
                && y <= this.y + this.h;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getW() {
        return w;
    }

    @Override
    public void setW(int w) {
        this.w = w;
    }

    @Override
    public int getH() {
        return h;
    }

    @Override
    public void setH(int h) {
        this.h = h;
    }

}

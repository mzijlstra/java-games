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
public interface Movable extends Collidable {

    boolean canMoveDown();

    boolean canMoveLeft();

    boolean canMoveRight();

    boolean canMoveUp();

    void moveDown();

    void moveLeft();

    void moveRight();

    void moveUp();

    public double getSpeed();

    public void setSpeed(double s);
}

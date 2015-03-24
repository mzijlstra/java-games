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
public interface Collidable extends Displayable {

    boolean collides(Collidable other);

    boolean contains(int x, int y);

    int getH();

    int getW();

    int getX();

    int getY();

    void setH(int h);

    void setW(int w);

    void setX(int x);

    void setY(int y);

}

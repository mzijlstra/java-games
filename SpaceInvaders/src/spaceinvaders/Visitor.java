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
public interface Visitor extends Collidable {

    void visit(Alien a);

    void visit(Bullet b);

}

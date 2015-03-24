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
public interface AlienComponent extends Movable {

    public void acceptVisitor(Visitor v);

    public AlienComponent getNext();

    public void setNext(AlienComponent n);

    public AlienComponent getPrev();

    public void setPrev(AlienComponent p);

    public void update();
}

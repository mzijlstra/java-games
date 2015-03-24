/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import java.awt.Graphics2D;

/**
 *
 * @author mzijlstra
 */
public abstract class AbstractAlienGroup implements AlienComponent {

    protected AlienComponent first;
    protected AlienComponent next;
    protected AlienComponent prev;
    protected int activeCount = 0;

    public AbstractAlienGroup() {
    }

    public AbstractAlienGroup(AlienComponent first) {
        this.first = first;
    }

    public AbstractAlienGroup(AlienComponent first, AlienComponent next) {
        this.first = first;
        this.next = next;
    }

    @Override
    public void acceptVisitor(Visitor v) {
        AlienComponent current = first;
        while (current != null) {
            current.acceptVisitor(v);
            current = current.getNext();
        }
    }

    @Override
    public boolean canMoveUp() {
        return first.canMoveUp();
    }

    @Override
    public boolean canMoveDown() {
        return first.canMoveDown();
    }

    @Override
    public boolean canMoveLeft() {
        return first.canMoveLeft();
    }

    @Override
    public boolean canMoveRight() {
        return first.canMoveRight();
    }

    @Override
    public void moveUp() {
        AlienComponent cur = first;
        while (cur != null) {
            cur.moveUp();
            cur = cur.getNext();
        }
    }

    @Override
    public void moveDown() {
        AlienComponent cur = first;
        while (cur != null) {
            cur.moveDown();
            cur = cur.getNext();
        }
    }

    @Override
    public void moveLeft() {
        AlienComponent cur = first;
        while (cur != null) {
            cur.moveLeft();
            cur = cur.getNext();
        }
    }

    @Override
    public void moveRight() {
        AlienComponent cur = first;
        while (cur != null) {
            cur.moveRight();
            cur = cur.getNext();
        }
    }

    @Override
    public void display(Graphics2D g) {
        AlienComponent cur = first;
        while (cur != null) {
            cur.display(g);
            cur = cur.getNext();
        }
    }

    public void add(AlienComponent a) {
        if (first == null) {
            first = a;
            activeCount++;
            return;
        }

        AlienComponent cur = first;
        while (cur.getNext() != null) {
            cur = cur.getNext();
        }
        cur.setNext(a);
        activeCount++;
    }

    public void reportDeath(AlienComponent a) {
        activeCount--;
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
    public AlienComponent getPrev() {
        return prev;
    }

    @Override
    public void setPrev(AlienComponent p) {
        this.prev = p;
    }

    @Override
    public double getSpeed() {
        return first.getSpeed();
    }

    @Override
    public void setSpeed(double s) {
        AlienComponent current = first;
        while (current != null) {
            current.setSpeed(s);
            current = current.getNext();
        }
    }

}

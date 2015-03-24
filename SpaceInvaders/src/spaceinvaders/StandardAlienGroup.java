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
public class StandardAlienGroup extends AbstractAlienGroup {

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private int mode = RIGHT;

    public StandardAlienGroup() {
        for (int x = 100; x < GameScreen.GWIDTH - 100; x += 30) {
            for (int y = 50; y < GameScreen.GHEIGHT - 250; y += 30) {
//            for (int y = 50; y < 80; y += 30) {
                add(new Alien(x, y, this));
            }
        }
    }

    @Override
    public void update() {
        if (mode == RIGHT && first.canMoveRight()) {
            AlienComponent current = first;
            while (current != null) {
                current.moveRight();
                current.update();
                current = current.getNext();
            }
        } else if (mode == LEFT && first.canMoveLeft()) {
            AlienComponent current = first;
            while (current != null) {
                current.moveLeft();
                current.update();
                current = current.getNext();
            }
        } else {
            mode++;
            mode = mode % 2;
            AlienComponent current = first;
            while (current != null) {
                current.moveDown();
                current.update();
                current = current.getNext();
            }
        }
    }

    @Override
    public void reportDeath(AlienComponent a) {
        super.reportDeath(a);
        if (activeCount == 0) {
            System.out.println("You've won!");
            System.exit(0);
        } else if (activeCount == 3 || activeCount == 1) {
            AlienComponent current = first;
            while (current != null) {
                current.setSpeed(current.getSpeed() * 2);
                if (current instanceof Alien) {
                    Alien al = (Alien) current;
                    al.setDropChance(al.getDropChance() * 5);
                }
                current = current.getNext();
            }
        }
    }

    @Override
    public boolean contains(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean collides(Collidable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getH() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getW() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setH(int h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setW(int w) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setX(int x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setY(int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

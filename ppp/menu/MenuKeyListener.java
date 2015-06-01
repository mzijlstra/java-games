package ppp.menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ppp.GameState;
import ppp.PPP;

public class MenuKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		PPP ppp = PPP.getInstance();
		if (ppp.getState() == GameState.MENU) {
			Menu menu = ppp.getCurrentMenu();
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				menu.up();
				break;
			case KeyEvent.VK_DOWN:
				menu.down();
				break;
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_SPACE:
				menu.enter();
				break;
			case KeyEvent.VK_ESCAPE:
				menu.escape();
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}

package snake.basic;

import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SnakeApp extends KeyAdapter {
	public final static int blocksize = 32;

	public static void main(String[] args) throws InterruptedException {
		new SnakeApp().gameLoop();
	}

	private Snake playerSnake;

	public void gameLoop() throws InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		playerSnake = new Snake(10, 5);
		int items = 5;

		Level level = new Level(playerSnake, items, 640, 480);
		level.addKeyListener(this);
		frame.add(level);
		frame.pack();
		frame.setIgnoreRepaint(true);
		frame.setVisible(true);
		level.requestFocus();

		int interval = 500;
		long runstart, runstop;
		int hit = 0;
		while (hit < 1) {
			while (hit == 0) {
				runstart = System.currentTimeMillis();
				// do game updates
				level.doUpdate();

				// check collisions
				hit = level.checkCollisions();

				// show updates
				frame.repaint();
				runstop = System.currentTimeMillis();

				// sleep
				Thread.sleep(interval - (runstop - runstart));
			}

			if (hit < 0) {
				items += 10;
				if (interval > 50) {
					interval -= 50;
				}
				frame.remove(level);
				level.removeKeyListener(this);
				level = new Level(playerSnake, items, 640, 480);
				level.addKeyListener(this);
				frame.add(level);
				frame.validate();
				level.requestFocus();
				hit = 0;
			}
		}

		frame.remove(level);
		JPanel panel = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.CENTER);
		panel.setLayout(flow);
		panel.add(new JLabel("Snake " + hit + " Game Over"));
		frame.add(panel);
		frame.validate();
		frame.setIgnoreRepaint(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (playerSnake.getDirection() != 2) {
				playerSnake.setDirection(0);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (playerSnake.getDirection() != 3) {
				playerSnake.setDirection(1);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (playerSnake.getDirection() != 0) {
				playerSnake.setDirection(2);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (playerSnake.getDirection() != 1) {
				playerSnake.setDirection(3);
			}
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
		}
	}
}

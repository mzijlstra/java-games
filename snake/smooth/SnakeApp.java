package snake.smooth;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class SnakeApp extends KeyAdapter {
	public final static int blocksize = 16;

	public static void main(String[] args) throws InterruptedException {
		new SnakeApp().gameLoop();
	}

	private Snake playerSnake;

	public void gameLoop() throws InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		playerSnake = new Snake((640 / blocksize) / 2 , (480 / blocksize) / 2);
		int items = 10;

		Level level = new Level(playerSnake, items, 640, 480);
		level.addKeyListener(this);
		frame.add(level);
		frame.pack();
		frame.setIgnoreRepaint(true);
		frame.setVisible(true);
		frame.setTitle("Smooth Snake is Smooth");
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

				// do transition steps to make things smooth
				for (int i=0; i< 10; i++) {
					if (runstart == 0) {
						runstart = System.currentTimeMillis();
					}
					level.doStep();
					
					// show updates
					frame.repaint();
	
					// sleep
					runstop = System.currentTimeMillis();
					Thread.sleep(interval / 10 - (runstop - runstart));
					runstart = 0;
				}
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

//		frame.remove(level);
//		JPanel panel = new JPanel();
//		FlowLayout flow = new FlowLayout();
//		flow.setAlignment(FlowLayout.CENTER);
//		panel.setLayout(flow);
//		panel.add(new JLabel("Snake " + hit + " Game Over"));
//		frame.add(panel);
//		frame.validate();
		
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

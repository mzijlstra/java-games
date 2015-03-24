package snake.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;

public class Level extends JComponent {
	private static final long serialVersionUID = 1L;

	private static Color myGray = new Color(225, 225, 225);

	private int width;
	private int height;
	private LinkedList<Snake> snakes = new LinkedList<Snake>();
	private LinkedList<Pickup> pickups = new LinkedList<Pickup>();

	public Level(Snake player, int items, int pixW, int pixH) {
		snakes.add(player);
		this.width = pixW / SnakeApp.blocksize;
		this.height = pixH / SnakeApp.blocksize;
		setPreferredSize(new Dimension(pixW, pixH));
		
		for (int i=0; i < items; i++) {
			int x = (int)(Math.random() * this.width);
			int y = (int)(Math.random() * this.height);
			pickups.add(new Pickup(x, y, i));
		}
	}

	public void doUpdate() {
		for (Snake snake : snakes) {
			snake.doUpdate();
		}
	}

	public int checkCollisions() {
		int snakeCount = 1;
		for (Snake snake : snakes) {
			// check boundaries
			if (snake.getX() > width || snake.getX() < 0
					|| snake.getY() > height || snake.getY() < 0) {
				return snakeCount;
			}

			// check collision with other snakes / segments
			for (Snake other : snakes) {
				boolean hit = snake.checkCollision(other);
				if (hit) {
					return snakeCount;
				}
			}

			// check pickups
			if (pickups.size() == 0) {
				return -1;
			}
			Iterator<Pickup> it = pickups.iterator();
			while (it.hasNext()) {
				Pickup pickup = it.next();
				boolean consume = pickup.checkCollision(snake);
				if (consume) {
					it.remove();
				}
			}
			snakeCount++;
		}
		return 0;
	}

	@Override
	public void paint(Graphics g) {
		int block = SnakeApp.blocksize;
		int w = width * block;
		int h = height * block;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.setColor(myGray);
		int size = SnakeApp.blocksize;
		for (int i = size; i < w; i += size) {
			g.drawLine(i, 0, i, h);
		}
		for (int i = size; i < h; i += size) {
			g.drawLine(0, i, w, i);
		}

		for (Snake snake : snakes) {
			snake.paint(g);
		}

		for (Pickup pickup : pickups) {
			pickup.paint(g);
		}
	}

}

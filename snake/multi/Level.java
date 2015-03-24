package snake.multi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

public class Level extends JComponent {
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	private List<Snake> snakes = new LinkedList<Snake>();
	private List<Pickup> pickups = new LinkedList<Pickup>();

	public Level(int pixW, int pixH) {
		this.width = pixW;
		this.height = pixH;
		setPreferredSize(new Dimension(pixW, pixH));
	}

	public void doUpdate() {
		for (Snake snake : snakes) {
			snake.doUpdate();
		}
	}

	public void doStep() {
		for (Snake snake : snakes) {
			snake.doStep();
		}
	}

	public synchronized List<String> checkCollisions() {
		List<String> collisions = new LinkedList<String>();

		for (Snake snake : snakes) {
			// check boundaries
			if (snake.getX() >= width || snake.getX() < 0
					|| snake.getY() >= height || snake.getY() < 0) {
				collisions.add("CRASH: " + snake.getName());
			}

			// check collision with other snakes / segments
			for (Snake other : snakes) {
				boolean hit = snake.checkCollision(other);
				if (hit) {
					collisions.add("CRASH: " + snake.getName());
				}
			}

			// check pickups
			if (pickups.size() == 0) {
				collisions.add("LEVEL COMPLETED");
			}
			Iterator<Pickup> it = pickups.iterator();
			while (it.hasNext()) {
				Pickup pickup = it.next();
				boolean consume = pickup.checkCollision(snake);
				if (consume) {
					it.remove();
					collisions.add("CONSUME: " + snake.getName() + " "
							+ pickup.getNum());
				}
			}
		}
		return collisions;
	}

	@Override
	public synchronized void paint(Graphics g) {
		int w = width;
		int h = height;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		for (Snake snake : snakes) {
			snake.paint(g);
		}

		for (Pickup pickup : pickups) {
			pickup.paint(g);
		}

		// FIXME this code is obsolete, client should show game over other way
//		if (status != RUNNING) {
//			String text = "";
//			Font myFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
//			if (status == CRASH) {
//				text += " YOU ATE YOURSELF!";
//			} else if (status == OUTOFBOUNDS) {
//				text += " YOU FELL OFF THE LEVEL!";
//			}
//
//			Graphics2D g2 = (Graphics2D) g;
//			Rectangle2D rec = myFont.getStringBounds(text,
//					g2.getFontRenderContext());
//			double textW = rec.getWidth();
//
//			g.setFont(myFont);
//			g.setColor(Color.RED);
//			g.drawString(text, (int) (width / 2 - textW / 2), height / 2);
//		}
	}

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}

	public synchronized void addPickup(Pickup pickup) {
		pickups.add(pickup);
	}
	
	public List<Snake> getSnakes() {
		return snakes;
	}

	public synchronized Pickup removePickup(int number) {
		Iterator<Pickup> it = pickups.iterator();
		Pickup removed = null;
		while(it.hasNext()) {
			removed = it.next();
			if (removed.getNum() == number) {
				it.remove();
				break;
			}
		}
		return removed;
	}
}
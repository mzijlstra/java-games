package snake.smooth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;

public class Level extends JComponent {
	private static final long serialVersionUID = 1L;
//	private static Color myGray = new Color(225, 225, 225);

	private static final int RUNNING = 0;
	private static final int OUTOFBOUNDS = 1;
	private static final int CRASH = 2;

	private int width;
	private int height;
	private int status;
	private LinkedList<Snake> snakes = new LinkedList<Snake>();
	private LinkedList<Pickup> pickups = new LinkedList<Pickup>();

	public Level(Snake player, int items, int pixW, int pixH) {
		status = RUNNING;
		snakes.add(player);
		this.width = pixW;
		this.height = pixH;
		setPreferredSize(new Dimension(pixW, pixH));

		for (int i = 0; i < items; i++) {
			int x = (int) (Math.random() * width / SnakeApp.blocksize);
			int y = (int) (Math.random() * height / SnakeApp.blocksize);
			pickups.add(new Pickup(x, y, i));
		}
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

	public int checkCollisions() {
		int snakeCount = 1;
		for (Snake snake : snakes) {
			// check boundaries
			if (snake.getX() >= width || snake.getX() < 0
					|| snake.getY() >= height || snake.getY() < 0) {
				status = OUTOFBOUNDS;
				return snakeCount;
			}

			// check collision with other snakes / segments
			for (Snake other : snakes) {
				boolean hit = snake.checkCollision(other);
				if (hit) {
					status = CRASH;
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
		int w = width;
		int h = height;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

//		g.setColor(myGray);
//		int size = SnakeApp.blocksize;
//		for (int i = size; i < w; i += size) {
//			g.drawLine(i, 0, i, h);
//		}
//		for (int i = size; i < h; i += size) {
//			g.drawLine(0, i, w, i);
//		}

		for (Snake snake : snakes) {
			snake.paint(g);
		}

		for (Pickup pickup : pickups) {
			pickup.paint(g);
		}

		if (status != RUNNING) {
			String text = "";
			Font myFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
			if (status == CRASH) {
				text += " YOU ATE YOURSELF!";
			} else if (status == OUTOFBOUNDS) {
				text += " YOU FELL OFF THE LEVEL!";
			}
			
			Graphics2D g2 = (Graphics2D) g;
			Rectangle2D rec = myFont.getStringBounds(text, g2.getFontRenderContext());
			double textW = rec.getWidth();
			
			g.setFont(myFont);
			g.setColor(Color.RED);
			g.drawString(text, (int) (width/2 - textW/2), height/2);
		}
	}
}
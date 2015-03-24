package snake.multi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Segment implements Collidable {
	private int x;
	private int y;
	private int num;
	private Segment next;
	private Segment prev;
	private Snake snake;
	private Color myColor;

	// internal usage variables
	private Font myFont;
	private boolean justConnected;
	private int targetX;
	private int targetY;
	private int stepX;
	private int stepY;

	public Segment(Pickup p, Snake owner) {
		snake = owner;
		myColor = snake.getColor();
		num = p.getNum();
		x = targetX = p.getX();
		y = targetY = p.getY();
		myFont = new Font(Font.MONOSPACED, Font.BOLD, SnakeApp.blocksize / 2);
		justConnected = true;
	}

	public void doUpdate(int x, int y) {
		// set our new target
		targetX = x;
		targetY = y;

		// make the mouth move ahead if we just connected
		if (justConnected) {
			justConnected = false;
			return;
		}
		
		if (next != null) {
			// have the update propagate to the other segments
			next.doUpdate(this.x, this.y);
			
			// swaps with next segment if next.getNum() > num			
			if (next.getNum() > num) {
				Segment oldPrev = prev;
				Segment first = this;
				Segment second = next;
				int oldTargetX = targetX;
				int oldTargetY = targetY;

				if (prev != null) {
					prev.next = next;
				}
				
				if (next.next != null) {
					next.next.prev = this;
				}
				
				if (snake.getTail() == this) {
					snake.setTail(second);
				}

				// update local references
				first.prev = second;
				first.next = next.next;
				
				// somewhat redundant, this will make it stay in place
				first.targetX = second.targetX;
				first.targetY = second.targetY;
				
				// update second object's references
				second.prev = oldPrev;
				second.next = first;
				
				second.targetX = oldTargetX;
				second.targetY = oldTargetY;
			}			
		}
	}

	public void doStep() {
		if (x != targetX) {
			if (stepX == 0) {
				stepX = (targetX - x) / 8;
			}
			x += stepX;
		} else {
			stepX = 0;
		}

		if (y != targetY) {
			if (stepY == 0) {
				stepY = (targetY - y) / 8;
			}
			y += stepY;
		} else {
			stepY = 0;
		}

		if (next != null) {
			next.doStep();
		}

	}

	@Override
	public boolean checkCollision(Snake snake) {
		if (snake.getTargetX() == targetX && snake.getTargetY() == targetY) {
			myColor = Color.RED;
			return true;
		}
		if (next != null) {
			return next.checkCollision(snake);
		}
		return false;
	}

	public void paint(Graphics g) {
		g.setFont(myFont);
		int block = SnakeApp.blocksize;
		int half = block / 2;
		int onethird = block / 3;
		int threefourth = 3 * (block / 4);
		g.setColor(myColor);
		g.drawRoundRect(x, y, block, block, half, half);
		g.drawString("" + num, x + onethird, y + threefourth);
		if (next != null) {
			next.paint(g);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Segment getNext() {
		return next;
	}

	public void setNext(Segment next) {
		this.next = next;
	}

	public Segment getPrev() {
		return prev;
	}

	public void setPrev(Segment prev) {
		this.prev = prev;
	}
}

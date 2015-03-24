package snake.multi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Snake implements Collidable {
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public static final int GOING = 0;
	public static final int CRASHED = 1;

	private String name;
	private Color color;
	private int status;
	private int x;
	private int y;
	private int direction;
	private Segment tail = null;

	// internal variables
	private int mouthSize;
	private int mouthUpd;
	private int mouthMax;
	private int mouthMin;
	private int targetX;
	private int targetY;
	private int stepX;
	private int stepY;

	// paint related
	private int near;
	private int far;

	public Snake(String name, Color color) {
		this.name = name;
		this.color = color;
		this.direction = DOWN;
		this.status = GOING;
		mouthSize = mouthMax = SnakeApp.blocksize / 2;
		mouthUpd = mouthMin = SnakeApp.blocksize / 8;
		tail = null;
		near = SnakeApp.blocksize / 8;
		far = SnakeApp.blocksize - near;
	}

	public void setStartPosition(int blockX, int blockY) {
		this.x = targetX = blockX * SnakeApp.blocksize;
		this.y = targetY = blockY * SnakeApp.blocksize;
	}

	public void doUpdate() {
		if (status == GOING) {

			if (tail != null) {
				tail.doUpdate(x, y);
			}

			switch (direction) {
			case UP:// up
				targetY -= SnakeApp.blocksize;
				break;
			case RIGHT: // right
				targetX += SnakeApp.blocksize;
				break;
			case DOWN: // down
				targetY += SnakeApp.blocksize;
				break;
			case LEFT: // left
				targetX -= SnakeApp.blocksize;
				break;
			}
		}
	}

	public void doStep() {
		if (status == GOING) {
			if (mouthSize > mouthMax || mouthSize <= mouthMin) {
				mouthUpd = -mouthUpd;
			}
			mouthSize += mouthUpd;

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

			if (tail != null) {
				tail.doStep();
			}
		}
	}

	public void paint(Graphics g) {
		int mouthDist = ((SnakeApp.blocksize - mouthSize) / 2);

		Point baseLeft = new Point();
		Point baseRight = new Point();
		Point frontLeft = new Point();
		Point frontRight = new Point();

		for (int i = -1; i < 2; i++) {
			// gives mouth tripple thinkness
			far += i;
			near += i;
			mouthDist += i;

			switch (direction) {
			case 0: // up
				baseLeft.x = x + mouthDist;
				baseLeft.y = y + far;
				baseRight.x = x + mouthDist + mouthSize;
				baseRight.y = y + far;
				frontLeft.x = x + mouthDist;
				frontLeft.y = y + near + mouthSize;
				frontRight.x = x + mouthDist + mouthSize;
				frontRight.y = y + near + mouthSize;
				break;
			case 1: // right
				baseLeft.x = x + near;
				baseLeft.y = y + mouthDist;
				baseRight.x = x + near;
				baseRight.y = y + mouthDist + mouthSize;
				frontLeft.x = x + far - mouthSize;
				frontLeft.y = y + mouthDist;
				frontRight.x = x + far - mouthSize;
				frontRight.y = y + mouthDist + mouthSize;
				break;
			case 2: // down
				baseLeft.x = x + mouthDist + mouthSize;
				baseLeft.y = y + near;
				baseRight.x = x + mouthDist;
				baseRight.y = y + near;
				frontLeft.x = x + mouthDist + mouthSize;
				frontLeft.y = y + far - mouthSize;
				frontRight.x = x + mouthDist;
				frontRight.y = y + far - mouthSize;
				break;
			case 3: // left
				baseLeft.x = x + far;
				baseLeft.y = y + mouthDist + mouthSize;
				baseRight.x = x + far;
				baseRight.y = y + mouthDist;
				frontLeft.x = x + near + mouthSize;
				frontLeft.y = y + mouthDist + mouthSize;
				frontRight.x = x + near + mouthSize;
				frontRight.y = y + mouthDist;
				break;
			}
			g.setColor(color);
			// g.setColor(Color.GREEN);
			g.drawLine(baseLeft.x, baseLeft.y, baseRight.x, baseRight.y);
			g.drawLine(baseLeft.x, baseLeft.y, frontLeft.x, frontLeft.y);
			g.drawLine(baseRight.x, baseRight.y, frontRight.x, frontRight.y);
		}

		if (tail != null) {
			tail.paint(g);
		}
	}

	@Override
	public boolean checkCollision(Snake snake) {
		if (snake != this && snake.targetX == targetX
				&& snake.targetY == targetY) {
			return true;
		}
		if (tail != null) {
			return tail.checkCollision(snake);
		}
		return false;
	}

	public void consume(Pickup pickup) {
		Segment s = new Segment(pickup, this);
		s.setNext(tail);
		if (tail != null) {
			tail.setPrev(s);
		}
		tail = s;
	}

	// getters and setters
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int d) {
		switch (d) {
		case UP:
			if (direction != DOWN) {
				direction = UP;
			}
			break;
		case RIGHT:
			if (direction != LEFT) {
				direction = RIGHT;
			}
			break;
		case DOWN:
			if (direction != UP) {
				direction = DOWN;
			}
			break;
		case LEFT:
			if (direction != RIGHT) {
				direction = LEFT;
			}
			break;
		}
	}

	public Segment getTail() {
		return tail;
	}

	public void setTail(Segment tail) {
		this.tail = tail;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public String getName() {
		return name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Color getColor() {
		return color;
	}
}

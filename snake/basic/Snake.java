package snake.basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Snake implements Collidable {
	private int x;
	private int y;

	private int direction;
	private int mouthSize;
	private int mouthUpd;
	private Segment tail = null;

	// paint related utility variables
	private int near;
	private int far; 

	public Snake(int x, int y) {
		this.x = x;
		this.y = y;
		mouthSize = 16;
		mouthUpd = 4;
		tail = null;
		near = SnakeApp.blocksize / 8;
		far = SnakeApp.blocksize - near;
	}

	public void doUpdate() {
		if (tail != null) {
			tail.doUpdate(x, y);
		}

		switch (direction) {
		case 0:// up
			y--;
			break;
		case 1: // right
			x++;
			break;
		case 2: // down
			y++;
			break;
		case 3: // left
			x--;
			break;
		}

		if (mouthSize > 16 || mouthSize < 9) {
			mouthUpd = -mouthUpd;
		}
		mouthSize += mouthUpd;
	}

	public void paint(Graphics g) {
		int blocksize = SnakeApp.blocksize;
		int startX = x * blocksize;
		int startY = y * blocksize;

		Point baseLeft = new Point();
		Point baseRight = new Point();
		Point frontLeft = new Point();
		Point frontRight = new Point();

		int mouthDist = ((blocksize - mouthSize) / 2);

		switch (direction) {
		case 0: // up
			baseLeft.x = startX + mouthDist;
			baseLeft.y = startY + far;
			baseRight.x = startX + mouthDist + mouthSize;
			baseRight.y = startY + far;
			frontLeft.x = startX + mouthDist;
			frontLeft.y = startY + near + mouthSize;
			frontRight.x = startX + mouthDist + mouthSize;
			frontRight.y = startY + near + mouthSize;
			break;
		case 1: // right
			baseLeft.x = startX + near;
			baseLeft.y = startY + mouthDist;
			baseRight.x = startX + near;
			baseRight.y = startY + mouthDist + mouthSize;
			frontLeft.x = startX + far - mouthSize;
			frontLeft.y = startY + mouthDist;
			frontRight.x = startX + far - mouthSize;
			frontRight.y = startY + mouthDist + mouthSize;
			break;
		case 2: // down
			baseLeft.x = startX + mouthDist + mouthSize;
			baseLeft.y = startY + near;
			baseRight.x = startX + mouthDist;
			baseRight.y = startY + near;
			frontLeft.x = startX + mouthDist + mouthSize;
			frontLeft.y = startY + far - mouthSize;
			frontRight.x = startX + mouthDist;
			frontRight.y = startY + far - mouthSize;
			break;
		case 3: // left
			baseLeft.x = startX + far;
			baseLeft.y = startY + mouthDist + mouthSize;
			baseRight.x = startX + far;
			baseRight.y = startY + mouthDist;
			frontLeft.x = startX + near + mouthSize;
			frontLeft.y = startY + mouthDist + mouthSize;
			frontRight.x = startX + near + mouthSize;
			frontRight.y = startY + mouthDist;
			break;
		}
		g.setColor(Color.GREEN);
		g.drawLine(baseLeft.x, baseLeft.y, baseRight.x, baseRight.y);
		g.drawLine(baseLeft.x, baseLeft.y, frontLeft.x, frontLeft.y);
		g.drawLine(baseRight.x, baseRight.y, frontRight.x, frontRight.y);
		
		if (tail != null) {
			tail.paint(g);
		}
	}
	
	@Override
	public boolean checkCollision(Snake snake) {
		if (snake != this && snake.getX() == x && snake.getY() == y) {
			return true;
		} 
		if (tail != null) {
			return tail.checkCollision(snake);
		}
		return false;
	}
	
	public void consume(Pickup pickup) {
		Segment s = new Segment(pickup);
		s.setNext(tail);
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

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Segment getTail() {
		return tail;
	}

	public void setTail(Segment tail) {
		this.tail = tail;
	}
}

package snake.basic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Segment implements Collidable{
	private int x;
	private int y;
	private int num;	
	private Segment next;
	private Font myFont;
	private boolean justConnected;
	
	public Segment(Pickup p) {
		num = p.getNum();
		x = p.getX();
		y = p.getY();
		myFont = new Font(Font.MONOSPACED, Font.BOLD, SnakeApp.blocksize / 2);
		justConnected = true;
	}
	
	public void doUpdate(int x, int y) {
		if (justConnected) {
			justConnected = false;
			return;
		}
		if (next != null) {
			next.doUpdate(this.x, this.y);
			if (next.getNum() > num) {
				int temp = num;
				num = next.getNum();
				next.setNum(temp);
			}
		}
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean checkCollision(Snake snake) {
		if (snake.getX() == x && snake.getY() == y) {
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
		int twosixth = 2 * (block / 6);
		int threefourth = 3 * (block / 4);
		g.setColor(Color.GREEN);
		g.drawString("" + num, x * block + twosixth, y * block + threefourth);
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

}

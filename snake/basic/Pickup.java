package snake.basic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Pickup implements Collidable{
	private Font myFont;
	private int x;
	private int y;
	private int num;
	
	public Pickup(int x, int y, int num) {
		this.x = x;
		this.y = y;
		this.num = num;
		myFont = new Font(Font.MONOSPACED, Font.BOLD, SnakeApp.blocksize / 2);
	}
	
	public void paint(Graphics g) {
		g.setFont(myFont);
		g.setColor(Color.BLACK);
		int block = SnakeApp.blocksize;
		int twosixth = 2 * (block / 6);
		int threefourth = 3 * (block / 4);
		g.drawString("" + num, x * block + twosixth, y * block + threefourth);
	}

	@Override
	public boolean checkCollision(Snake snake) {
		if (snake.getX() == x && snake.getY() == y) {
			snake.consume(this);
			return true;
		}
		return false;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getNum() {
		return num;
	}

}

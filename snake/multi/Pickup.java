package snake.multi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Pickup implements Collidable{
	private Font myFont;
	private int x;
	private int y;
	private int num;
	
	public Pickup(int blockX, int blockY, int num) {
		this.x = blockX * SnakeApp.blocksize;
		this.y = blockY * SnakeApp.blocksize;
		this.num = num;
		myFont = new Font(Font.MONOSPACED, Font.BOLD, SnakeApp.blocksize / 2);
	}
	
	public void paint(Graphics g) {
		g.setFont(myFont);
		g.setColor(Color.BLACK);
		int block = SnakeApp.blocksize;
		int onethird = block / 3;
		int threefourth = 3 * (block / 4);
		g.drawString("" + num, x + onethird, y + threefourth);
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

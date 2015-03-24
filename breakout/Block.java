package breakout;

public class Block {
	public int x;
	public int y;
	public int w;
	public int h;
	
	public Block(int x, int y) {
		this(x, y, 20, 10);
	}
	
	public Block(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public boolean contains(int x, int y) {
		boolean contains = false;
		if (x > this.x && x < this.x + this.w && y > this.y
				&& y < this.y + this.h) {
			contains = true;
		}
		return contains;
	}
}

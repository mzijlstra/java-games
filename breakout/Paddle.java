package breakout;

public class Paddle {
	public double x;
	public double y;
	public double w;
	public double h;
	
	public Paddle(double x, double y) {
		this(x, y, 10, 40);
	}
	
	public Paddle(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public boolean contains(double x, double y) {
		boolean contains = false;
		if (x > this.x && x < this.x + this.w && y > this.y
				&& y < this.y + this.h) {
			contains = true;
		}
		return contains;
	}
}

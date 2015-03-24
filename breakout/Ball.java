package breakout;

public class Ball {
	public double x;
	public double y;
	public double r;
	public double vX;
	public double vY;
	
	public Ball(double x, double y, double r, double vX, double vY) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.vX = vX;
		this.vY = vY;
	}
	
	public void move() { 
		x += vX;
		y += vY;
	}	

	public double getSpeed() {
		// will always be a positive number due to math
		return Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));
	}
}

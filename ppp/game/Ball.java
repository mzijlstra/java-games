package ppp.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.image.BufferedImage;

import ppp.PPP;

public class Ball extends Double {
	private static final long serialVersionUID = 1L;

	public double vX;
	public double vY;
	public double r;
	public double maxV;
	private String lastHit;
	private BufferedImage image;

	public Ball() {
		this((double) (PPP.GAME_W / 2) - 6, // ball.x
				(double) (PPP.GAME_H / 2) - 6, // ball.y
				(double) 6, // ball.r (radius)
				(double) 2, // ball.vX
				(double) 0, // ball.vY
				(double) 5 // ball.maxV
		);
	}

	public Ball(double x, double y, double r, double vX, double vY, double maxV) {
		super(0, 0, 2 * r, 2 * r);
		this.vX = vX;
		this.vY = vY;
		this.r = r;
		this.maxV = maxV;

		image = new BufferedImage((int) x, (int) y,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.addRenderingHints(hints);

		Point2D center = new Point2D.Double(r, r);
		Point2D focus = new Point2D.Double(2 * r, 2 * r);
		float[] dist = { 0.0f, 1.0f };
		Color[] colors = { new Color(200, 200, 200), new Color(170, 170, 170) };
		RadialGradientPaint rgp = new RadialGradientPaint(center, (float) r,
				focus, dist, colors, CycleMethod.NO_CYCLE);
		graphics.setPaint(rgp);
		graphics.fill(this);
		graphics.draw(this);

		this.x = x;
		this.y = y;
	}

	public void move() {
		this.x += vX;
		this.y += vY;
	}

	public double getSpeed() {
		return Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));
	}

	public void setLastHit(String lastHit) {
		this.lastHit = lastHit;
	}

	public String getLastHit() {
		return lastHit;
	}

	public BufferedImage getImage() {
		return image;
	}
}

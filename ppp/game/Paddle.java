package ppp.game;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import ppp.PPP;

public class Paddle extends RoundRectangle2D.Double {
	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	public Paddle(int x, int y, int w, int h, Point2D pL1, Point2D pL2) {
		super(x, y, w, h, 10, 10);
		image = new BufferedImage((int) x, (int) y,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = image.createGraphics();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.addRenderingHints(hints);

		Color c1 = new Color(21, 161, 186);
		Color c2 = new Color(205, 205, 205);
		graphics.setPaint(new GradientPaint(pL1, c2, pL2, c1));
		graphics.fill(this);
	}

	public BufferedImage getImage() {
		return image;
	}

	public static class Top extends Paddle {
		private static final long serialVersionUID = 1L;

		public Top() {
			super((PPP.GAME_W / 2) - 20, // x
					10, // y
					40, // width
					10, // height
					new Point2D.Double(0, 0), // gradient start
					new Point2D.Double(0, 10) // gradient stop
			);
		}
	}

	public static class Bottom extends Paddle {
		private static final long serialVersionUID = 1L;

		public Bottom() {
			super((PPP.GAME_W / 2) - 20, // x
					PPP.GAME_H - 20, // y
					40, // width
					10, // height
					new Point2D.Double(0, 10), // gradient start
					new Point2D.Double(0, 0) // gradient stop
			);
		}
	}

	public static class Left extends Paddle {
		private static final long serialVersionUID = 1L;

		public Left() {
			super(10, // x
					(PPP.GAME_H / 2) - 20, // y
					10, // width
					40, // height
					new Point2D.Double(0, 0), // gradient start
					new Point2D.Double(10, 0) // gradient stop
			);
		}
	}

	public static class Right extends Paddle {
		private static final long serialVersionUID = 1L;

		public Right() {
			super(PPP.GAME_W - 20, // x
					(PPP.GAME_H / 2) - 20, // y
					10, // width
					40, // height
					new Point2D.Double(10, 0), // gradient start
					new Point2D.Double(0, 0) // gradient stop
			);
		}
	}
}

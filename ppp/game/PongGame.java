package ppp.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import ppp.GameState;
import ppp.PPP;

public class PongGame {
	private BufferedImage buffer;
	private Graphics2D graphics;

	private Ball ball;
	private Paddle paddleLeft;
	private Paddle paddleRight;

	private int gameW;
	private int gameH;
	private int scoreLeft = 0;
	private int scoreRight = 0;

	public PongGame() {
		this.gameH = PPP.GAME_H;
		this.gameW = PPP.GAME_W;

		buffer = new BufferedImage(PPP.GAME_W, PPP.GAME_H,
				BufferedImage.TYPE_3BYTE_BGR);
		graphics = buffer.createGraphics();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.addRenderingHints(hints);
		graphics.setFont(new Font("Monospaced", Font.BOLD, 16));
		graphics.setBackground(Color.WHITE);
		graphics.setColor(new Color(125, 125, 125));

		ball = new Ball((gameW / 2) - 6, // ball.x
				(gameH / 2) - 6, // ball.y
				6, // ball.r (radius)
				2, // ball.vX
				0, // ball.vY
				5 // ball.maxV
		);
		
		paddleLeft = new Paddle.Left();
		paddleRight = new Paddle.Right();		
	}

	public void checkInput() {
		PPP ppp = PPP.getInstance();
		KeyboardState keyboard = ppp.getKeyboard();
		if (keyboard.keyDown(KeyEvent.VK_ESCAPE)) {
			ppp.setState(GameState.MENU);
		} else {
			if (keyboard.keyDown(KeyEvent.VK_S)
					&& paddleLeft.y + paddleLeft.height < gameH) {
				paddleLeft.y += 1.5;
			}
			if (keyboard.keyDown(KeyEvent.VK_W) && paddleLeft.y > 0) {
				paddleLeft.y -= 1.5;
			}
			if (keyboard.keyDown(KeyEvent.VK_DOWN)
					&& paddleRight.y + paddleRight.height < gameH) {
				paddleRight.y += 1.5;
			}
			if (keyboard.keyDown(KeyEvent.VK_UP) && paddleRight.y > 0) {
				paddleRight.y -= 1.5;
			}
		}
	}

	public void checkCollision() {
		RoundRectangle2D hitPaddle = null;
		if (paddleLeft.contains(new Point2D.Double(ball.x, ball.y + ball.r))) {
			ball.vX = -ball.vX;
			ball.setLastHit("Left");
			hitPaddle = paddleLeft;
			ball.vX += 0.5;
		} else if (paddleRight.contains(new Point2D.Double(ball.x + ball.width,
				ball.y + ball.r))) {
			ball.vX = -ball.vX;
			ball.setLastHit("Right");
			hitPaddle = paddleRight;
			ball.vX -= 0.5;
		} else if (ball.y <= 0 || ball.y + ball.height >= gameH) {
			ball.vY = -ball.vY;
		} else if (ball.x <= 0) {
			scoreRight++;
			ball.x = gameW / 2 - ball.r;
			ball.y = gameH / 2 - ball.r;
			ball.vX = 2;
		} else if (ball.x >= gameW) {
			scoreLeft++;
			ball.x = gameW / 2 - ball.r;
			ball.y = gameH / 2 - ball.r;
			ball.vX = -2;
		}

		if (hitPaddle != null) {
			// Y speed and direction determined by location on paddle
			double maxBallYV = 2;
			double distance = ball.y - hitPaddle.getY();
			double halfPaddle = hitPaddle.getHeight() / 2;
			double newVY = 0;
			if (distance < halfPaddle) {
				// make ball go left, set vX < 0
				newVY = halfPaddle - distance;
				newVY = -newVY;
			} else {
				// make ball go right, set vX > 0
				newVY = distance - halfPaddle;
			}
			// adjust it down to a reasonable speed
			newVY = (newVY / halfPaddle) * maxBallYV;
			ball.vY = newVY;
		}
	}

	public void moveObjs() {
		ball.move();
	}

	public BufferedImage getImage() {
		graphics.clearRect(0, 0, buffer.getWidth(), buffer.getHeight());
		Paint drawPaint = graphics.getPaint();

		// draw paddles
		Point2D pL1 = new Point2D.Double(paddleLeft.x, paddleLeft.y);
		Point2D pL2 = new Point2D.Double(paddleLeft.x + paddleLeft.width,
				paddleLeft.y);
		Color c1 = new Color(21, 161, 186);
		Color c2 = new Color(205, 205, 205);
		graphics.setPaint(new GradientPaint(pL1, c2, pL2, c1));
		graphics.fill(paddleLeft);
		Point2D pR1 = new Point2D.Double(paddleRight.x + paddleRight.width,
				paddleRight.y);
		Point2D pR2 = new Point2D.Double(paddleRight.x, paddleRight.y);
		graphics.setPaint(new GradientPaint(pR1, c2, pR2, c1));
		graphics.fill(paddleRight);
		graphics.setPaint(drawPaint);
		graphics.draw(paddleLeft);
		graphics.draw(paddleRight);

		// draw center line and circle
		double radius = 30.5;
		graphics.drawLine(gameW / 2, 0, gameW / 2, gameH);
		graphics.setColor(Color.WHITE);
		graphics.fill(new Ellipse2D.Double(gameW / 2 - radius, gameH / 2
				- radius, radius * 2, radius * 2));
		graphics.setPaint(drawPaint);
		graphics.draw(new Ellipse2D.Double(gameW / 2 - radius, gameH / 2
				- radius, radius * 2, radius * 2));

		// show ball speed
		String displaySpeed = new Integer((int) (Math.abs(ball.vX) * 2 - 4))
				.toString();
		graphics.drawString(displaySpeed, (int) Math.round(gameW / 2) - 5,
				(int) Math.round(gameH / 2) + 5);

		// show scores
		int scoreOffset = 30;
		graphics.drawString(Integer.toString(scoreLeft), scoreOffset, 20);
		graphics.drawString(Integer.toString(scoreRight), gameW
				- (int) (scoreOffset + paddleRight.width), 20);

		// draw ball
		graphics.drawImage(ball.getImage(), (int) ball.x, (int) ball.y, null);

		return buffer;
	}

}

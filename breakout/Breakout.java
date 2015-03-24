package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Timer;

import breakout.GraphicsWindow;
import breakout.KeyboardInput;

public class Breakout {
	private Paddle paddle;
	private Ball ball;
	private Block[][] blocks;
	int ballScore = 1;

	private GraphicsWindow graphics;
	private KeyboardInput keyboard;

	private int gameW;
	private int gameH;
	private int blockW;
	private int blockH;
	private int blockRows;
	private int blockCols;

	private int currentFrame = 0;
	private int fps = 0;

	public Breakout() {
		gameW = 640;
		gameH = 480;
		blockW = 20;
		blockH = 10;
		blockRows = 4;
		blockCols = gameW / blockW;

		// basic hardware setup
		graphics = new GraphicsWindow(gameW, gameH);
		Font font = new Font("Monospaced", Font.BOLD, 16);
		graphics.setFont(font);
		keyboard = new KeyboardInput();
		graphics.addKeyListener(keyboard);

		// game items setup
		paddle = new Paddle(gameW / 2 - 20, gameH - 20, 40, 10);
		ball = new Ball((gameW / 2), (gameH - 40), 6, randomV(), -7);

		blocks = new Block[blockRows][blockCols];
		for (int i = 0; i < blockRows; i++) {
			for (int j = 0; j < blockCols; j++) {
				blocks[i][j] = new Block(j * blockW, i * blockH + 60, blockW,
						blockH);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Breakout bo = new Breakout();
		Timer fpsTimer = new Timer("fpsTimer", true);
		fpsTimer.scheduleAtFixedRate(new FpsTask(bo), 1000, 1000);
		bo.mainLoop();
	}

	public void mainLoop() throws InterruptedException {

		// variables needed for game loop
		long fps = 60;
		long startTime = 0;
		long execTime = 0;
		long sleepTime = 0;
		long frameTime = 1000 / fps;
		boolean running = true;

		// game loop
		while (running) {
			startTime = System.currentTimeMillis();

			running = checkInput();
			checkCollision();
			ball.move();
			updateGraphics();

			currentFrame++;

			// use a properly timed game loop
			execTime = System.currentTimeMillis() - startTime;
			sleepTime = frameTime - execTime;
			if (sleepTime > 0) {
				Thread.sleep(sleepTime);
			}
		}

		// exit when loop is done
		System.exit(0);
	}

	public void setFPS() {
		fps = currentFrame;
		currentFrame = 0;
	}

	private boolean checkInput() {
		boolean esc = false;

		if (keyboard.keyDown(KeyEvent.VK_LEFT) && paddle.x > 0) {
			paddle.x -= 2.0;
		}
		if (keyboard.keyDown(KeyEvent.VK_RIGHT) && paddle.x + paddle.w < gameW) {
			paddle.x += 2.0;
		}
		if (keyboard.keyDown(KeyEvent.VK_ESCAPE)) {
			esc = true;
		}

		return !esc;
	}

	private void checkCollision() {
		boolean collision = false;
		int maxSpeed = 5;
		double speedIncrease = 0.5;
		Block block = null;

		// ball's top point
		int tX = (int) Math.round(ball.x);
		int tY = (int) Math.round(ball.y - ball.r);

		// ball's bottom point
		int bX = (int) Math.round(ball.x);
		int bY = (int) Math.round(ball.y + ball.r);

		// ball's left point
		int lX = (int) Math.round(ball.x - ball.r);
		int lY = (int) Math.round(ball.y + ball.vY);

		// ball's right point
		int rX = (int) Math.round(ball.x + ball.r);
		int rY = (int) Math.round(ball.y);

		// always check left and right screen border
		if (ball.x + ball.r + ball.vX > gameW || ball.x - ball.r + ball.vX < 0) {
			ball.vX = -ball.vX;
			collision = true;
		}

		// check lower half of the screen if the ball is there
		if (!collision && ball.y > gameH / 2) {
			// check for paddle collision
			if (!collision
					&& (paddle.contains(bX, bY) || paddle.contains(lX, lY) || paddle
							.contains(rX, rY))) { // no need to check bX,bY
				ball.vY = -ball.vY;
				// TODO set ball.vX based on where the ball hit the paddle
				ball.vX = randomV();
			}
			// check for screen bottom
			else if (ball.y + ball.vY > gameH) {
				ballScore++;
				ball.x = (gameW / 2);
				ball.y = (gameH - 40);
				ball.vX = randomV();
				ball.vY = -7;
			}
		}

		// else check upper half of the screen
		else {
			// check for screen top
			if (ball.y - ball.r + ball.vY < 0) {
				ball.vY = -ball.vY;
			}
			// check the blocks
			else {
				// check for ball top collision in blocks
				block = insideABlock(tX, tY);
				if (block != null) {
					// change direction, adding speed
					ball.vY = -ball.vY;
					if (ball.getSpeed() < maxSpeed) {
						ball.vY = ball.vY + speedIncrease;
					}
					collision = true;
					System.out.println(System.currentTimeMillis()
							+ " collision with ball top - block " + block.x
							+ " " + block.y);
				}

				block = insideABlock(bX, bY);
				if (!collision && block != null) {
					// change direction, adding speed
					ball.vY = -ball.vY;
					if (ball.getSpeed() < maxSpeed) {
						ball.vY = ball.vY - speedIncrease;

					}
					collision = true;
					System.out.println(System.currentTimeMillis()
							+ " collision with ball bottom - block " + block.x
							+ " " + block.y);
				}

				block = insideABlock(lX, lY);
				if (!collision && block != null) {
					int distance = block.x + block.w - (int) (ball.x - ball.r);
					ball.x += distance;

					// change direction, adding speed
					ball.vX = -ball.vX;
					if (ball.getSpeed() < maxSpeed) {
						ball.vX = ball.vX + speedIncrease;
					}
					collision = true;
					System.out.println(System.currentTimeMillis()
							+ " collision with ball left - block " + block.x
							+ " " + block.y);
				}

				block = insideABlock(rX, rY);
				if (!collision && block != null) {
					int distance = (int) (ball.x + ball.r) - block.x;
					ball.x -= distance;

					// change ball direction, adding speed
					ball.vX = -ball.vX;
					if (ball.getSpeed() < maxSpeed) {
						ball.vX = ball.vX - speedIncrease;
					}
					collision = true;
					System.out.println(System.currentTimeMillis()
							+ " collision with ball right - block " + block.x
							+ " " + block.y);
				}
			}
		}
	}

	private Block insideABlock(int x, int y) {
		Block block = null;
		boolean collision = false;

		// check for ball top collision in blocks
		out: for (int i = 0; i < blockRows; i++) {
			for (int j = 0; j < blockCols; j++) {
				block = blocks[i][j];
				if (block != null && block.contains(x, y)) {
					blocks[i][j] = null;
					collision = true;
					break out; // oh! what a pun!
				}
			}
		}
		if (collision) {
			return block;
		} else {
			return null;
		}
	}

	private double randomV() {
		return (Math.random() * 3) - 1.5;
	}

	private void updateGraphics() {
		graphics.clearBuffer();

		// draw paddle
		graphics.fillRoundedRectangle(paddle.x, paddle.y, paddle.w, paddle.h,
				10.0, 10.0);

		// draw blocks
		Block block = null;
		Color normal = graphics.getDrawColor();
		Color other = new Color(100, 100, 100);
		for (int i = 0; i < blockRows; i++) {
			for (int j = 0; j < blockCols; j++) {
				block = blocks[i][j];
				if (block != null) {
					graphics.fillRectangle(block.x, block.y, block.w, block.h);
					graphics.setDrawColor(other);
					graphics.drawRectangle(block.x, block.y, block.w, block.h);
					graphics.setDrawColor(normal);
				}
			}
		}

		// draw ball score
		String text = "Ball# " + ballScore + " speed: "
				+ NumberFormat.getInstance().format(ball.getSpeed());
		graphics.drawText(text, 5, 20);

		// draw ball
		graphics.fillEllipse(ball.x, ball.y, ball.r * 2, ball.r * 2);

		// show fps
		if (fps != 0) {
			graphics.drawText("FPS: " + fps, gameW - 80, 20);
		}

		graphics.showOnScreen();
	}
}

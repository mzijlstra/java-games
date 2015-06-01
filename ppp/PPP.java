package ppp;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import ppp.game.KeyboardState;
import ppp.game.PongGame;
import ppp.menu.MainMenu;
import ppp.menu.Menu;
import ppp.menu.MenuKeyListener;

public class PPP {
	public static final int GAME_W = 640;
	public static final int GAME_H = 480;
	private static PPP instance;

	private JFrame frame;
	private Canvas canvas;
	private KeyboardState keyboard;
	private MenuKeyListener menuKeyListener;
	private PongGame game;
	private Menu currentMenu;
	private GameState state;
	private boolean fullscreen;
	private int currentFrame;
	private int fps = 45;

	public static void main(String[] args) throws InterruptedException {
		instance = new PPP();
		instance.gameLoop();
	}

	public static PPP getInstance() {
		return instance;
	}

	private PPP() {
		canvas = new Canvas();
		canvas.setSize(GAME_W, GAME_H);

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setIgnoreRepaint(true);

		menuKeyListener = new MenuKeyListener();
		currentMenu = new MainMenu();
		fullscreen = false;
		state = GameState.MENU;

		frame.addKeyListener(menuKeyListener);
		frame.setVisible(true);
	}

	public void gameLoop() {
		// variables needed for game loop
		long startTime = 0;
		long execTime = 0;
		long sleepTime = 0;
		long frameTime = 1000 / fps;

		// setup default background before game begins
		BufferedImage buffer = new BufferedImage(GAME_W, GAME_H,
				BufferedImage.TYPE_4BYTE_ABGR);

		while (true) {
			startTime = System.currentTimeMillis();

			
			if (state == GameState.GAME) {
				game.checkInput();
				game.checkCollision();
				game.moveObjs();
				canvas.getGraphics().drawImage(game.getImage(), 0, 0, null);
			} else if (state == GameState.MENU) {
				Graphics2D g2d = (Graphics2D) buffer.getGraphics();
				g2d.setBackground(Color.WHITE);
				g2d.clearRect(0, 0, GAME_W, GAME_H);
				if (game != null) {
					g2d.drawImage(game.getImage(), 0, 0, null);
				}
				g2d.drawImage(currentMenu.getImage(), 0, 0, null);
				canvas.getGraphics().drawImage(buffer, 0, 0, null);
			}

			// use a properly timed game loop
			currentFrame++;
			execTime = System.currentTimeMillis() - startTime;
			sleepTime = frameTime - execTime;
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
		if (state == GameState.MENU) {
//			frame.setIgnoreRepaint(false);
			frame.removeKeyListener(keyboard);
			menuKeyListener = new MenuKeyListener();
			frame.addKeyListener(menuKeyListener);
		} else if (state == GameState.GAME) {
//			frame.setIgnoreRepaint(true);
			frame.removeKeyListener(menuKeyListener);
			keyboard = new KeyboardState();
			frame.addKeyListener(keyboard);
		}
	}

	public Menu getCurrentMenu() {
		return currentMenu;
	}

	public void setCurrentMenu(Menu currentMenu) {
		this.currentMenu = currentMenu;
	}

	public PongGame getGame() {
		return game;
	}

	public void setGame(PongGame game) {
		this.game = game;
	}

	public KeyboardState getKeyboard() {
		return keyboard;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
}

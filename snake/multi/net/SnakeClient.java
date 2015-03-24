package snake.multi.net;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import snake.multi.Level;
import snake.multi.Pickup;
import snake.multi.Snake;

public class SnakeClient extends KeyAdapter {
	public final static int blocksize = 16;

	// GUI Stuff
	private JFrame frame;
	private JPanel cards;
	private JPanel playerList;

	// Client related stuff
	private Socket socket;
	private SynchronizedReader reader;
	private SynchronizedWriter writer;

	// Game related stuff
	private boolean running = false;
	private String name;
	private Snake playerSnake;
	private Level level;
	private HashMap<String, Snake> snakes = new HashMap<String, Snake>();

	public static void main(String[] args) throws InterruptedException {
		SnakeClient client = new SnakeClient();
		client.setup();
	}

	public void setup() {
		JPanel connectPanel = new JPanel();
		connectPanel.setLayout(new BorderLayout());
		connectPanel.setPreferredSize(new Dimension(640, 480));

		JLabel enterData = new JLabel("Please Enter:");
		enterData.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 32));
		connectPanel.add(enterData, BorderLayout.NORTH);

		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		connectPanel.add(fieldsPanel, BorderLayout.CENTER);

		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		namePanel.add(new JLabel("Your Name:"));
		JTextField nameField = new JTextField(45);
		nameField.setText("Michael");
		namePanel.add(nameField);
		fieldsPanel.add(namePanel);

		JPanel ipPanel = new JPanel();
		ipPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		ipPanel.add(new JLabel("Server IP:"));
		JTextField ipField = new JTextField(45);
		ipField.setText("127.0.0.1");
		ipPanel.add(ipField);
		fieldsPanel.add(ipPanel);

		JColorChooser colors = new JColorChooser(Color.BLUE);
		colors.setPreviewPanel(new SnakePreview(colors));
		AbstractColorChooserPanel[] colorPanels = colors.getChooserPanels();
		colors.removeChooserPanel(colorPanels[0]);
		colors.removeChooserPanel(colorPanels[2]);
		fieldsPanel.add(colors);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton connectBtn = new JButton("Connect");
		connectBtn.addActionListener(new ConnectBtnListener(nameField, ipField,
				colors));
		btnPanel.add(connectBtn);
		connectPanel.add(btnPanel, BorderLayout.SOUTH);

		JPanel gameRoom = new JPanel();
		gameRoom.setPreferredSize(new Dimension(640, 480));
		gameRoom.setLayout(new BorderLayout());
		gameRoom.add(new JLabel("Waiting for Server"), BorderLayout.NORTH);

		playerList = new JPanel();
		playerList.setLayout(new BoxLayout(playerList, BoxLayout.Y_AXIS));
		gameRoom.add(playerList, BorderLayout.CENTER);

		cards = new JPanel();
		cards.setLayout(new CardLayout());
		cards.add(gameRoom, "GameRoom");
		cards.add(connectPanel, "Connect");
		((CardLayout) cards.getLayout()).show(cards, "Connect");

		frame = new JFrame();
		frame.getContentPane().add(cards);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Snake Client");
		frame.pack();
		frame.setVisible(true);
	}

	public class SnakePreview extends JComponent implements ChangeListener {
		private static final long serialVersionUID = 1L;
		private JColorChooser colorChooser;
		private Color color;
		private Snake snake;

		public SnakePreview(JColorChooser colorChooser) {
			this.colorChooser = colorChooser;
			this.color = Color.BLUE;
			this.snake = new Snake("Preview", color);
			snake.setStartPosition(4, 0);
			snake.consume(new Pickup(96, 0, 0));
			snake.consume(new Pickup(80, 0, 1));
			snake.consume(new Pickup(64, 0, 2));
			setPreferredSize(new Dimension(128, 64));
			colorChooser.getSelectionModel().addChangeListener(this);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			color = colorChooser.getColor();
		}

		@Override
		public void paint(Graphics g) {
			snake.paint(g);
		}
	}

	public class ConnectBtnListener implements ActionListener {
		private JTextField nameField;
		private JTextField ipField;
		private JColorChooser colorChooser;

		public ConnectBtnListener(JTextField nameField, JTextField ipField,
				JColorChooser colorChooser) {
			this.nameField = nameField;
			this.ipField = ipField;
			this.colorChooser = colorChooser;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			name = nameField.getText();
			String ip = ipField.getText();
			Color color = colorChooser.getColor();

			try {
				socket = new Socket(ip, 9000);
				writer = new SynchronizedWriter(new PrintWriter(
						socket.getOutputStream()));
				reader = new SynchronizedReader(new BufferedReader(
						new InputStreamReader(socket.getInputStream())));

				// tell the server our basic details
				writer.println("Player: " + name + " " + color.getRGB());
				((CardLayout) cards.getLayout()).show(cards, "GameRoom");
				Thread thread = new Thread(new NetworkThread());
				thread.start();
				// gameLoop();

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				System.exit(1);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(1);
			}
		}
	}

	public class NetworkThread implements Runnable {

		@Override
		public void run() {
			try {
				level = new Level(640, 480);				
				String line = null;
				String[] tokens = null;

				// first get info about the players
				line = reader.readLine();
				while (!line.equals("GAME START")) {
					System.err.println("Invalid Waiting Command: " + line);
					reader.readLine();
				}

				tokens = reader.readTokens();
				if (tokens[0].equals("PLAYERS:")) {
					int amount = Integer.parseInt(tokens[1]);
					for (int i = 0; i < amount; i++) {
						tokens = reader.readTokens();
						if (tokens[0].toUpperCase().equals("PLAYER:")) {
							String name = tokens[1];
							Color color = new Color(Integer.parseInt(tokens[2]));
							int x = Integer.parseInt(tokens[3]);
							int y = Integer.parseInt(tokens[4]);

							Snake snake = new Snake(name, color);
							snake.setStartPosition(x, y);
							level.addSnake(snake);
							snakes.put(name, snake);
							
							if (name.equals(SnakeClient.this.name)) {
								SnakeClient.this.playerSnake = snake;
							}
						} 						
					}
				} else {
					System.err.println("Invalid Player List");
				}
				
				// game is starting read level pickup locations
				tokens = reader.readTokens();
				if (tokens[0].toUpperCase().equals("ITEMS:")) {
					int amount = Integer.parseInt(tokens[1]);
					for (int i = 0; i < amount; i++) {
						tokens = reader.readTokens();
						if (tokens[0].equals("PICKUP:")) {
							int x = Integer.parseInt(tokens[1]);
							int y = Integer.parseInt(tokens[2]);
							int n = Integer.parseInt(tokens[3]);
							level.addPickup(new Pickup(x, y, n));
						}
					}
				} else {
					System.err.println("Invalid pickup list");
				}

				cards.add(level, "Level");
				((CardLayout) cards.getLayout()).show(cards, "Level");
				level.addKeyListener(SnakeClient.this);
				level.requestFocus();
				frame.setIgnoreRepaint(true);
				
				running = true;
				Thread gameThread = new Thread(new GameThread());
				gameThread.start();
	
				// read snake updates while game is running
				while (true) {
					line = reader.readLine();
					tokens = line.split("\\s+");

					if (tokens[0].toUpperCase().equals("DIRECTION:")) {
						snakes.get(tokens[1]).setDirection(
								Integer.parseInt(tokens[2]));
					} else if (tokens[0].toUpperCase().equals("CONSUME:")) {
						snakes.get(tokens[1])
								.consume(
										level.removePickup(Integer
												.parseInt(tokens[2])));
					} else if (tokens[0].toUpperCase().equals("CRASH:")) {
						snakes.get(tokens[1]).setStatus(Snake.CRASHED);
					} else if (tokens[0].equals("SHUTDOWN")) {
						((CardLayout) cards.getLayout()).show(cards, "Connect");
						frame.setIgnoreRepaint(false);
						running = false; // set game thread running to false
						try {
							gameThread.join(); // make sure the thread stops
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					} else {
						System.err.println("Invalid in Game Command: " + line);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public class GameThread implements Runnable {

		@Override
		public void run() {
			int interval = 500;
			long runstart, runstop;
			while (running) {
				runstart = System.currentTimeMillis();
				
				// do game updates
				level.doUpdate();

				// do transition steps to make things smooth
				for (int i = 0; i < 10; i++) {
					if (runstart == 0) {
						runstart = System.currentTimeMillis();
					}
					level.doStep();

					// show updates
					frame.repaint();

					// sleep
					runstop = System.currentTimeMillis();
					try {
						Thread.sleep(interval / 10 - (runstop - runstart));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					runstart = 0;
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (playerSnake.getDirection() != Snake.DOWN) {
				writer.println("DIRECTION: " + name + " " + Snake.UP);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (playerSnake.getDirection() != Snake.LEFT) {
				writer.println("DIRECTION: " + name + " " + Snake.RIGHT);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (playerSnake.getDirection() != Snake.UP) {
				writer.println("DIRECTION: " + name + " " + Snake.DOWN);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (playerSnake.getDirection() != Snake.RIGHT) {
				writer.println("DIRECTION: " + name + " " + Snake.LEFT);
			}
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
		}
	}
}

package snake.multi.net;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import snake.multi.Level;
import snake.multi.Pickup;
import snake.multi.Snake;
import snake.multi.SnakeApp;

public class SnakeServer {
	private List<SynchronizedReader> readers;
	private List<SynchronizedWriter> writers;
	private Map<String, ClientHandler> clients;
	private JFrame frame;
	private JPanel connectionsPanel;
	private JPanel cards;

	public static void main(String[] args) throws IOException,
			InterruptedException {
		new SnakeServer().acceptConnections();
	}

	public SnakeServer() {
		readers = new ArrayList<SynchronizedReader>();
		writers = new ArrayList<SynchronizedWriter>();
		clients = new HashMap<String, ClientHandler>();

		connectionsPanel = new JPanel();
		connectionsPanel.setLayout(new BoxLayout(connectionsPanel,
				BoxLayout.Y_AXIS));
		connectionsPanel.setPreferredSize(new Dimension(640, 480));

		JLabel label = new JLabel("Connected Clients:");

		JButton startGameBtn = new JButton("Start Game");
		startGameBtn.addActionListener(new StartGameBtnListener());

		cards = new JPanel();
		cards.setLayout(new CardLayout());
		cards.add(connectionsPanel, "ConnectionsPanel");
		((CardLayout) cards.getLayout()).show(cards, "ConnectionsPanel");

		frame = new JFrame();
		frame.setTitle("Snake Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(cards, BorderLayout.CENTER);
		frame.getContentPane().add(label, BorderLayout.NORTH);
		frame.getContentPane().add(startGameBtn, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}

	public void acceptConnections() throws IOException, InterruptedException {
		ServerSocket serversock = new ServerSocket(9000);
		do {
			Socket socket = serversock.accept();
			SynchronizedWriter writer = new SynchronizedWriter(new PrintWriter(
					socket.getOutputStream()));
			SynchronizedReader reader = new SynchronizedReader(
					new BufferedReader(new InputStreamReader(
							socket.getInputStream())));
			readers.add(reader);
			writers.add(writer);
			ClientHandler client = new ClientHandler(reader);
			Thread thread = new Thread(client);
			thread.start();
		} while (!serversock.isClosed());
	}

	public class GameThread implements Runnable {

		public void run() {
			tellAll("GAME START");

			// setup game loop
			int width = 640;
			int height = 480;
			int items = 10;
			Level level = new Level(width, height);


			tellAll("PLAYERS: " + clients.size());
			Iterator<ClientHandler> it = clients.values().iterator();
			for (int i = 0; i < clients.size(); i++) {
				ClientHandler client = it.next();
				Snake snake = client.getSnake();
				level.addSnake(snake);

				snake.setStartPosition(8 * i, 8);
				tellAll("PLAYER: " + snake.getName() + " "
						+ snake.getColor().getRGB() + " " + (8 * i) + " " + 8);
			}

			tellAll("ITEMS: " + items);
			for (int i = 0; i < items; i++) {
				int x = (int) (Math.random() * width / SnakeApp.blocksize);
				int y = (int) (Math.random() * height / SnakeApp.blocksize);
				level.addPickup(new Pickup(x, y, i));
				tellAll("PICKUP: " + x + " " + y + " " + i);
			}

			int interval = 500;
			long runstart, runstop;
			boolean running = true;

			cards.add(level, "GameView");
			((CardLayout) cards.getLayout()).show(cards, "GameView");
			frame.setIgnoreRepaint(true);

			// do game loop (until someone is hit)
			while (running) {
				runstart = System.currentTimeMillis();
				// do game updates
				level.doUpdate();
				for (Snake s : level.getSnakes()) {
					tellAll("DIRECTION: " + s.getName() + " "
							+ s.getDirection());
				}

				// check collisions
				List<String> collisions = level.checkCollisions();
				if (collisions.size() > 0
						&& collisions.get(0).equals("LEVEL COMPLETED")) {
					running = false;
				}
				
				for (String collision : collisions) {
					String[] tokens = collision.split("\\s+");
					if (tokens[0].equals("CRASH:")) {
						clients.get(tokens[1]).snake.setStatus(Snake.CRASHED);
					}
					tellAll(collision);
				}

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

			// tell all the clients to quit
			tellAll("SHUTDOWN");
			for (ClientHandler client : clients.values()) {
				client.setRunning(false);
			}
		}
	}

	private void tellAll(String line) {
		for (SynchronizedWriter writer : writers) {
			writer.println(line);
		}
	}

	private class StartGameBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Thread t = new Thread(new GameThread());
			t.start();
		}

	}

	private class ClientHandler implements Runnable {
		private SynchronizedReader reader;
		private Snake snake;
		private String name;
		private boolean running;

		public ClientHandler(SynchronizedReader r)
				throws IOException {
			this.running = true;
			this.reader = r;

			String player = reader.readLine();
			if (player.toUpperCase().startsWith("PLAYER:")) {
				String[] tokens = player.split("\\s+");
				name = tokens[1];
				Color color = new Color(Integer.parseInt(tokens[2]));
				snake = new Snake(name, color);
				clients.put(name, this);

				JLabel clientLabel = new JLabel(name);
				clientLabel.setForeground(color);
				connectionsPanel.add(clientLabel);
				connectionsPanel.revalidate();
			}
		}

		@Override
		public void run() {
			String line = null;
			String[] tokens = null;

			try {
				while (running) {
					line = reader.readLine();
					tokens = line.split("\\s+");
					if (tokens[0].toUpperCase().equals("DIRECTION:")
							&& tokens[1].equals(name)) {
						snake.setDirection(Integer.parseInt(tokens[2]));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setRunning(boolean running) {
			this.running = running;
		}

		public Snake getSnake() {
			return snake;
		}
	}
}

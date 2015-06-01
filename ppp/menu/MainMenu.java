package ppp.menu;

import ppp.GameState;
import ppp.PPP;
import ppp.game.PongGame;

public class MainMenu extends AbstractMenu {
	private static final long serialVersionUID = 1L;

	public MainMenu() {
		super();
		
		
		Task newGameTask = new Task() {
			public void doTask() {
				PPP ppp = PPP.getInstance();
				
				if (ppp.getGame() == null) {
					Task resumeTask = new Task() {
						public void doTask() {				
							PPP ppp = PPP.getInstance();
							ppp.setState(GameState.GAME);
						}
					};
					addMenuItem(1, new MenuItem("Resume Game", resumeTask));
				}
				
				PongGame game = new PongGame();
				ppp.setGame(game);
				ppp.setState(GameState.GAME);
			}
		};
		addMenuItem(new MenuItem("New Game", newGameTask));
		
		Task settingsTask = new Task() {
			public void doTask() {
				PPP ppp = PPP.getInstance();
				SettingsMenu settings = new SettingsMenu(ppp.getCurrentMenu());
				ppp.setCurrentMenu(settings);
				settings.repaint();
			}
		};
		addMenuItem(new MenuItem("Settings", settingsTask));
		
		Task exitTask = new Task() {
			public void doTask() {
				System.exit(0);
			}
		};
		addMenuItem(new MenuItem("Exit", exitTask));
		
		repaint();
	}
}

package ppp.menu;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import ppp.PPP;

public class SettingsMenu extends AbstractMenu {
	private static final long serialVersionUID = 1L;
	private Menu mainMenu;

	public SettingsMenu(Menu mainMenu) {
		super();

		this.mainMenu = mainMenu;

		Task backToMain = new Task() {
			public void doTask() {
				SettingsMenu parent = (SettingsMenu) PPP.getInstance()
						.getCurrentMenu();
				PPP.getInstance().setCurrentMenu(parent.getMainMenu());
			}
		};
		addMenuItem(new MenuItem("Back to Main Menu", backToMain));

		Task fullScreen = new Task() {
			public void doTask() {
				PPP ppp = PPP.getInstance();
				// Get graphics configuration...
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();

				if (!ppp.isFullscreen()) {
					// Change to full screen, 640 x 480, 32 bit color
					gd.setFullScreenWindow(ppp.getFrame());
					if (gd.isDisplayChangeSupported()) {
						gd.setDisplayMode(new DisplayMode(640, 480, 32,
								DisplayMode.REFRESH_RATE_UNKNOWN));
					}
				} else {
					gd.setFullScreenWindow(null);
				}
			}
		};
		StringTask isFullScreen = new StringTask() {
			public String getString() {
				String value = "";

				if (PPP.getInstance().isFullscreen())
					value = "ON";
				else
					value = "OFF";
				return value;
			}
		};
		addMenuItem(new MenuOption("Full Screen", fullScreen, isFullScreen));

		repaint();
	}

	public Menu getMainMenu() {
		return mainMenu;
	}

}

package ppp.menu;

import java.awt.image.BufferedImage;

public abstract interface Menu {
	public abstract void up();
	public abstract void down();
	public abstract void enter();
	public abstract void escape();
	public abstract BufferedImage getImage();
}

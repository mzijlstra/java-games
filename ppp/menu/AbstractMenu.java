package ppp.menu;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import ppp.PPP;

public class AbstractMenu extends Canvas implements Menu {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage buffer;
	private Graphics2D graphics;
	private LinkedList<MenuItem> menuItems;
	private int currentItem;
	private Font font;
	private Color menuColor;
	private Color selectColor;

	public AbstractMenu() {
		buffer = new BufferedImage(PPP.GAME_W, PPP.GAME_H,
				BufferedImage.TYPE_4BYTE_ABGR);
		graphics = buffer.createGraphics();
		RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.addRenderingHints(hints);
		graphics.setBackground(new Color(0, 0, 0, 0)); // transparent BG

		// set default font and colors
		font = new Font("Monospaced", Font.BOLD, 32);
		menuColor = Color.WHITE;
		selectColor = Color.BLACK;
		
		// add font and colors to graphics
		graphics.setFont(font);
		graphics.setColor(menuColor);
		
		// setup basics for MenuOption handling
		currentItem = 0;
		menuItems = new LinkedList<MenuItem>();
	}

	public LinkedList<MenuItem> getMenuItems() {
		return menuItems;
	}
	
	public void addMenuItem(MenuItem option) {
		menuItems.add(option);
	}
	
	public void addMenuItem(int index, MenuItem item) {
		menuItems.add(index, item);
	}
	
	public boolean removeMenuItem(MenuItem item) {
		return menuItems.remove(item);
	}

	public int getCurrentItem() {
		return currentItem;
	}

	public void setCurrentItem(int currentItem) {
		this.currentItem = currentItem;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public void down() {
		currentItem++;
		currentItem %= menuItems.size();
		repaint();
	}

	@Override
	public void up() {
		currentItem--;
		if (currentItem < 0) {
			currentItem = menuItems.size() -1;
		}
		currentItem %= menuItems.size();
		repaint();
	}

	@Override
	public void enter() {
		menuItems.get(currentItem).doOption();
		repaint();
	}

	@Override
	public void escape() {
		// maybe move to the exit option
		repaint();
	}

	@Override
	public BufferedImage getImage() {
		graphics.clearRect(0, 0, PPP.GAME_W, PPP.GAME_H);
		
		graphics.setPaint(new Color(200,200,200,200));
		graphics.fillRect(PPP.GAME_W/6, PPP.GAME_H/6, (PPP.GAME_W/6)*4, (PPP.GAME_H/6)*4);
		graphics.setPaint(Color.BLACK);
		graphics.drawRect(PPP.GAME_W/6, PPP.GAME_H/6, (PPP.GAME_W/6)*4, (PPP.GAME_H/6)*4);

		
		int x, y, width, height;
		int count = menuItems.size();
		FontMetrics metrics = graphics.getFontMetrics();
		height = metrics.getDescent();
		int start = (PPP.GAME_H - (count * (height + 25))) / 2;
		
		MenuItem option = null;
		for (int i = 0; i < count; i++) {
			option = menuItems.get(i);
			width = metrics.stringWidth(option.getName());
			y = start + (height +50) * i;
			x = (PPP.GAME_W / 2) - (width / 2);

			if (i != currentItem) {
				graphics.setColor(menuColor);
			} else {
				graphics.setColor(selectColor);
				y += 3;
			}
			graphics.drawString(option.getName(), x, y);
		}
		return buffer;
	}

}

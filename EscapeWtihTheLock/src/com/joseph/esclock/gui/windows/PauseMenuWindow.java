package com.joseph.esclock.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

import com.joseph.esclock.engine.GameEngine;
import com.joseph.esclock.gui.Window;
import com.joseph.esclock.gui.buttons.GenericSelectableButton;
import com.joseph.esclock.interfaces.IMouseReliant;
import com.joseph.esclock.reference.ScreenReference;
import com.joseph.esclock.util.FileSaveSystem;

/**
 * The window that pauses the game and has the buttons associated with pausing a game
 * @author Joseph
 *
 */
public class PauseMenuWindow extends Window {
	private static PauseMenuWindow instance;
	
	private boolean visible;
	private Font font;
	private FontRenderContext frc;
	private GenericSelectableButton resume;
	private GenericSelectableButton exit;
	
	
	private final String headder = "Pause Menu:";
	
	public PauseMenuWindow() {
		super(ScreenReference.WIDTH / 2 - (75), ScreenReference.HEIGHT / 2 - (55), 150, 110, false);
		this.visible = false;
		this.frc = GameEngine.getInstance().getFrc();
		this.font = ScreenReference.getTheFont();
		
		Rectangle2D r = font.getStringBounds(this.headder, frc);
		int yOff = (int) r.getHeight() + 10;
		int xOff = 5;
		
		this.resume = new GenericSelectableButton(x + xOff, y + yOff, "Resume Game", true, false, new IMouseReliant() {
			@Override
			public boolean onMouseEvent(MouseEvent e) {
				// TODO BUG BUG BUG: mouse stays default when menu brought back up and mouse is in resume
				if (!PauseMenuWindow.this.visible) {
					return false;
				}
				GameEngine.getInstance().setDefaultMouse();
				PauseMenuWindow.this.hide();
				return true;
			}
		});
		yOff += this.resume.getHeight0() + 10;
		
		this.exit = new GenericSelectableButton(x + xOff, y + yOff, "Exit Game", true, false, new IMouseReliant() {
			@Override
			public boolean onMouseEvent(MouseEvent e) {
				if (!PauseMenuWindow.this.visible) {
					return false;
				}
				try {
					FileSaveSystem.autoSaveGame();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.exit(0);
				return true;
			}
		});
		yOff += this.exit.getHeight0() + 10;
		
		GameEngine.getInstance().addButton(resume);
		GameEngine.getInstance().addButton(exit);
		
		
		instance = this;
	}

	@Override
	public void drawBackground(Graphics g, ImageObserver observer) {
		if (!visible) {
			return;
		}
		
		g.setColor(Color.WHITE);
		g.drawRect(x, y, width, height);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x + 1, y + 1, width - 1, height - 1);
		
		g.setColor(Color.WHITE);
		g.setFont(font);
		Rectangle2D r = font.getStringBounds(this.headder, frc);
		int yOff = (int) r.getHeight();
		int xOff = 5;
		g.drawString(this.headder, x + xOff, y + yOff);
		
		this.resume.drawBackground(g, observer);
		this.exit.drawBackground(g, observer);
	}

	@Override
	public void drawUpdateableElements(Graphics g, ImageObserver observer) {
		if (!visible) {
			return;
		}
		
		this.resume.drawUpdateableElements(g, observer);
		this.exit.drawUpdateableElements(g, observer);
	}

	@Override
	public void updateUpdateableElements(double deltaTime) {
		if (!visible) {
			return;
		}
		
		this.resume.updateUpdateableElements(deltaTime);
		this.exit.updateUpdateableElements(deltaTime);
	}

	@Override
	public void displayToolTip(Graphics g) {
		
	}
	
	public boolean isVisible() {
		return this.visible;
	}

	public void show() {
		this.visible = true;
	}
	
	public void hide() {
		this.visible = false;
	}
	
	public static PauseMenuWindow getInstance() {
		return instance;
	}
}
package com.joseph.esclock.gui.windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import com.joseph.esclock.engine.GameEngine;
import com.joseph.esclock.gui.Window;
import com.joseph.esclock.gui.buttons.GenericDownButton;
import com.joseph.esclock.gui.buttons.GenericUpButton;
import com.joseph.esclock.interfaces.IMouseReliant;
import com.joseph.esclock.reference.ScreenReference;

/**
 * Generic button that assigns the jobs between the different jobs in the village
 * @author Nathan Lim
 *
 */
public class GenericJobAssignmentWindow extends Window {
	private boolean visible;
	private GenericUpButton upButton;
	private GenericDownButton downButton;
	
	public GenericJobAssignmentWindow(int x, int y) {
		super(x, y, (int) ScreenReference.getTheFont().getStringBounds(""/* TODO*/, GameEngine.getInstance().getFrc()).getWidth() + (25 ), 40, true);
		this.visible = true;
		this.upButton = new GenericUpButton(this.x + width - 20, this.y, true, new IMouseReliant() {
			@Override
			public boolean onMouseEvent(MouseEvent e) {
				return false;
			}
		});
		
		this.downButton = new GenericDownButton(this.x + width - 20, this.y + 20, true, new IMouseReliant() {
			@Override
			public boolean onMouseEvent(MouseEvent e) {
				return false;
			}
		});
		GameEngine.getInstance().addButton(upButton);
		GameEngine.getInstance().addButton(downButton);
	
	}

	@Override
	public void drawBackground(Graphics g, ImageObserver observer) {
		if (!visible) {
			return;
		}
		
	}

	@Override
	public void drawUpdateableElements(Graphics g, ImageObserver observer) {
		if (!visible) {
			return;
		}
		
		g.setColor(Color.WHITE);
		g.drawRect(x, y, width, height);
		
		g.setFont(ScreenReference.getTheFont());
		
		g.drawString("", x + 5, y + 4 + ScreenReference.CHAR_HEIGHT);
		
		upButton.drawUpdateableElements(g, observer);
		downButton.drawUpdateableElements(g, observer);
		
	}

	@Override
	public void updateUpdateableElements(double deltaTime) {
		if (!visible) {
			return;
		}
		
		this.downButton.updateUpdateableElements(deltaTime);
		this.upButton.updateUpdateableElements(deltaTime);
	}

	@Override
	public void displayToolTip(Graphics g) {
		if (!visible) {
			return;
		}
		
	}
	
}

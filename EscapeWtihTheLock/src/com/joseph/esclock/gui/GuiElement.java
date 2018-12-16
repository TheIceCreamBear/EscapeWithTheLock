package com.joseph.esclock.gui;

import com.joseph.esclock.gui.buttons.GenericSelectableButton;
import com.joseph.esclock.reference.ScreenReference;

/**
 * Object realization of the {@link IGuiElement} interface. Top level class, handles position and visible
 * @author Joseph
 *
 */
public abstract class GuiElement implements IGuiElement {
	protected boolean visible;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public GuiElement(int x, int y, int width, int height) {
		this(x, y, width, height, false);
	}
	
	public GuiElement(int x, int y, int width, int height, boolean scaled) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * resets the dimensions of the element to the given params
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param scaled
	 */
	protected void resetDimensions(int x, int y, int width, int height, boolean scaled) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void show() {
		this.visible = true;
	}
	
	public void hide() {
		this.visible = false;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public int getWidth0() {
		return this.width;
	}
	
	public int getHeight0() {
		return this.height;
	}
}
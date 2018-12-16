package com.joseph.esclock.handlers;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.joseph.esclock.engine.GameEngine;
import com.joseph.esclock.gui.windows.ConsoleWindow;
import com.joseph.esclock.gui.windows.PauseMenuWindow;
import com.joseph.esclock.reference.Reference;

/**
 * GKELAH, or GlobalKeyEventHandlerAndListener, is a key event handler that listens for all
 * key events and does a specific action based on the state of the engine and the key pressed.
 * Used for KeyStroke logging for text typing or for special key that must perform a specific 
 * action the moment they are pressed as opposed to waiting for the next update cycle of the 
 * object that will be using that special key.
 * 
 * @author Joseph
 * @see InputHandler
 */
public class GKELAH implements KeyListener {
	private boolean[] keyDown;
	protected boolean[] frameKeyDown;

	public GKELAH() {
		this.keyDown = new boolean[600];
		this.frameKeyDown = this.keyDown.clone();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if (ConsoleWindow.getInstance().isVisible()) {
			ConsoleWindow.getInstance().notifyKeyTyped(e);
			return;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F2) {
			System.exit(0);
		}
		
		if (e.getKeyCode() == KeyEvent.VK_F1) {
			Reference.DEBUG_MODE = !Reference.DEBUG_MODE;
			return;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_F3 && GameEngine.getInstance().getPlayer().isDead()) {
			GameEngine.getInstance().reset();
		}
		
		if (PauseMenuWindow.getInstance().isVisible()) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				PauseMenuWindow.getInstance().hide();
				return;
			}
			return;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SLASH && !ConsoleWindow.getInstance().isVisible()) {
			ConsoleWindow.getInstance().show();
			return;
		}
		
		
		if (ConsoleWindow.getInstance().isVisible()) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				ConsoleWindow.getInstance().hide();
				return;
			} else {
				ConsoleWindow.getInstance().notifyKeyPressed(e);
				return;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			PauseMenuWindow.getInstance().show();
		}
		
		// user input
		this.keyDown[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		this.keyDown[e.getKeyCode()] = false;
	}
	
	public boolean isKeyDown(int keyCode) {
		return this.frameKeyDown[keyCode];
	}
	
	public void captureInput() {
		this.frameKeyDown = this.keyDown.clone();
	}
	
	public Point getMouseLocation() {
		return MouseInfo.getPointerInfo().getLocation();
	}
}
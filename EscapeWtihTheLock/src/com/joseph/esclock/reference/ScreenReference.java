package com.joseph.esclock.reference;

import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;

import com.joseph.esclock.engine.GameEngine;

/**
 * Like {@link Reference}, but for the Screen
 * 
 * @author David Santamaria
 *
 */
public class ScreenReference {
	// TODO the width and height wont be full screen dim
	public static final int WIDTH;
	public static final int HEIGHT;
	public static final int CHAR_WIDTH = 11;
	public static final int CHAR_HEIGHT = 23;
	
	static {
		WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	public static void doScreenCalc() {
		if (Reference.DEBUG_MODE) {
			System.err.println(Toolkit.getDefaultToolkit().getScreenResolution());
			System.err.println(Toolkit.getDefaultToolkit().getScreenSize());
		}
	}
	
	public static Font getMapFont() {
		return Reference.Fonts.MAP_FONT;
	}
	
	public static Font getTheFont() {
		return Reference.Fonts.DEFAULT_FONT;
	}
	
	public static Font getUnderlinedFont() {
		return Reference.Fonts.DEFAULT_UNDERLINED_FONT;
	}
	
	public static Point getMouseLocation() {
		return GameEngine.getInstance().getMouseLocation();
	}
}
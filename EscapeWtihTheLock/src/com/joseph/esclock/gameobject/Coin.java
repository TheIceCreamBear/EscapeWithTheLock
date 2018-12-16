package com.joseph.esclock.gameobject;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.joseph.esclock.reference.Reference.Images;
import com.joseph.esclock.reference.ScreenReference;

public class Coin extends GameObject {
	/**
	 * @param x the tile x cord
	 * @param y the tile y cord
	 */
	public Coin(int x, int y) {
		super(x, y);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(Images.COIN, x, y, null);
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, 40, 40);
	}
	
	public void moveY(int distance) {
		this.y += distance;
	}
	
	public boolean keepUpdating() {
		return y < ScreenReference.HEIGHT;
	}
}
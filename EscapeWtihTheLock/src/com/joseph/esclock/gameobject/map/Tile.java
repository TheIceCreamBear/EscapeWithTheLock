package com.joseph.esclock.gameobject.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.joseph.esclock.gameobject.GameObject;
import com.joseph.esclock.gui.Animation;
import com.joseph.esclock.reference.Reference;

public abstract class Tile extends GameObject {
	public static final int TILE_DIMENSION = 40;
	
	protected final Animation animation;

	public Tile(int x, int y, Animation animation, Random r) {
		super(x, y);
		this.animation = new Animation(animation);
		this.animation.setRandomStartInt(r);
	}

	@Override
	public void update() {
		this.animation.update();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(this.animation.getCurrentImage(), x, y, null);
		if (Reference.DEBUG_MODE) {
			g.setColor(Color.red);
			g.drawRect(x, y, TILE_DIMENSION, TILE_DIMENSION);
		}
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(x, y, TILE_DIMENSION, TILE_DIMENSION);
	}
	
	public abstract boolean causeDeath();
	public abstract boolean blockMovement();
	
	public void moveTileY(int distance) {
		this.y += distance;
	}
	
	public int getY() {
		return y;
	}
}
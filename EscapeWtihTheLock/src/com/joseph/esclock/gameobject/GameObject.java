package com.joseph.esclock.gameobject;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class GameObject {
	protected int x;
	protected int y;
	
	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void update();
	
	public abstract void draw(Graphics2D g);
	
	public abstract Rectangle getBoundingBox();
}
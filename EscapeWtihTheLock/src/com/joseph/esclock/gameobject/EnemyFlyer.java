package com.joseph.esclock.gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import com.joseph.esclock.gameobject.map.Map;
import com.joseph.esclock.gui.Animation;
import com.joseph.esclock.reference.Reference;
import com.joseph.esclock.reference.Reference.Animations;
import com.joseph.esclock.reference.Reference.Images;

public class EnemyFlyer extends Enemy {
	private Animation animation;
	private boolean dead = false;
	
	public EnemyFlyer(int x, int y) {
		super(x, y);
		this.animation = new Animation(Animations.FLYER);
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void updateY(int distance) {
		this.y += distance;
	}
	
	@Override
	public void update() {
		if (isDead()) {
			return;
		}
		
		this.y += 2 + Map.mapMovementSpeed;
		
		this.animation.update();
	}

	@Override
	public void draw(Graphics2D g) {
		if (isDead()) {
			return;
		}
		g.drawImage(this.animation.getCurrentImage(), x - 20, y - 20, null);
		if (Reference.DEBUG_MODE) {
			g.setColor(Color.MAGENTA);
			g.draw(getBoundingBox());
		}
	}

	@Override
	public Rectangle getBoundingBox() {
		if (isDead()) {
			return null;
		}
		Image i = this.animation.getCurrentImage();
		// TODO
		if (i.equals(Images.ENEMY_FLYER_FORWARD)) {
			return new Rectangle(x - 17, y - 7, 34, 19);
		}
		if (i.equals(Images.ENEMY_FLYER_MIDDLE)) {
			return new Rectangle(x - 20, y - 4, 40, 16);
		}
		return new Rectangle(x - 17, y - 5, 34, 17);
	}
}
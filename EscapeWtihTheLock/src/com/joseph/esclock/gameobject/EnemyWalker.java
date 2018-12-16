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

public class EnemyWalker extends Enemy {
	private Animation animation;
	private boolean dead = false;
	
	public EnemyWalker(int x, int y) {
		super(x, y);
		this.animation = new Animation(Animations.WALKER);
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
		
		this.y += 1 + Map.mapMovementSpeed;
		
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
		if (i.equals(Images.ENEMY_WALK_STILL)) {
			return new Rectangle(x - 10, y - 5, 20, 10);
		}
		return new Rectangle(x - 10, y - 9, 20, 18);
	}
}
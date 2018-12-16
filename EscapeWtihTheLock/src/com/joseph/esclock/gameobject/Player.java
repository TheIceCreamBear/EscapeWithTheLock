package com.joseph.esclock.gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.joseph.esclock.engine.GameEngine;
import com.joseph.esclock.gameobject.map.Map;
import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.gui.Animation;
import com.joseph.esclock.gui.Animation.AnimationFrame;
import com.joseph.esclock.reference.Reference;
import com.joseph.esclock.reference.Reference.Images;

public class Player extends GameObject {
	// TODO eventually speed will vary, horizontal will be constant
	private final Animation runningAnim;
	private boolean moving = false;
	// lane is range 0-9 (so that it doesnt error map)
	private int lane;
	private int laneCooldown = 0;
	private boolean dead;
	
	public Player(int x, int y, int startLane) {
		super(x, y);
		this.runningAnim = new Animation(new AnimationFrame(Images.PLAYER_STILL, 1), new AnimationFrame(Images.PLAYER_LEFT, 18), 
				new AnimationFrame(Images.PLAYER_STILL, 17), new AnimationFrame(Images.PLAYER_RIGHT, 18),
				new AnimationFrame(Images.PLAYER_STILL, 16));
		this.lane = startLane;
	}
	
	public void kill() {
		this.dead = true;
	}
	
	public void moveYByMap(int distance) {
		this.y += distance;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getLane() {
		return this.lane;
	}

	@Override
	public void update() {
		if (isDead()) {
			return;
		}
		this.moving = false;
		if (this.laneCooldown > 0) {
			this.laneCooldown--;
		}
		if (GameEngine.getKeyListener().isKeyDown(KeyEvent.VK_W)) {
			this.y -= Map.mapMovementSpeed;
			this.moving = true;
		}
		if (GameEngine.getKeyListener().isKeyDown(KeyEvent.VK_S)) {
			this.y += Map.mapMovementSpeed;
			this.moving = true;
		}
		if (GameEngine.getKeyListener().isKeyDown(KeyEvent.VK_A) && this.lane != 0 && laneCooldown == 0 && GameEngine.getMap().grassTile(lane - 1, y)) {
			this.x -= Tile.TILE_DIMENSION;
			this.lane--;
			this.laneCooldown = 7;
		}
		if (GameEngine.getKeyListener().isKeyDown(KeyEvent.VK_D) && this.lane != 9 && laneCooldown == 0 && GameEngine.getMap().grassTile(lane + 1, y)) {
			this.x += Tile.TILE_DIMENSION;
			this.lane++;
			this.laneCooldown = 7;
		}
		if (moving) {
			this.runningAnim.update();
		} else {
			this.runningAnim.restart();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (isDead()) {
			return;
		}
		g.drawImage(this.runningAnim.getCurrentImage(), x - 20, y - 20, null);
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
		Image i = this.runningAnim.getCurrentImage();
		if (i.equals(Images.PLAYER_STILL)) {
			return new Rectangle(x - 10, y - 5, 20, 10);
		}
		return new Rectangle(x - 10, y - 9, 20, 18);
	}	
}
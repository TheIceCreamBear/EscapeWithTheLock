package com.joseph.esclock.gameobject.map.tiles;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.reference.Reference.Animations;

public class WallTile extends Tile {
	public WallTile(int x, int y, Random r) {
		super(x, y, Animations.WALL, r);
	}

	@Override
	public boolean causeDeath() {
		return false;
	}

	@Override
	public boolean blockMovement() {
		return true;
	}
}
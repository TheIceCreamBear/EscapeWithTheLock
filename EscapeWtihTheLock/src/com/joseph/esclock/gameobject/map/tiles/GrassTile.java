package com.joseph.esclock.gameobject.map.tiles;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.reference.Reference;

public class GrassTile extends Tile {

	public GrassTile(int x, int y, Random r) {
		super(x, y, Reference.Animations.GRASS, r);
	}

	@Override
	public boolean causeDeath() {
		return false;
	}

	@Override
	public boolean blockMovement() {
		return false;
	}
}
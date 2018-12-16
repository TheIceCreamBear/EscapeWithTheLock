package com.joseph.esclock.gameobject.map.tiles;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.reference.Reference.Animations;

public class SpikePitTile extends Tile {
	public SpikePitTile(int x, int y, Random r) {
		super(x, y, Animations.SPIKE_PIT, r);
	}

	@Override
	public boolean causeDeath() {
		return true;
	}

	@Override
	public boolean blockMovement() {
		return false;
	}
}
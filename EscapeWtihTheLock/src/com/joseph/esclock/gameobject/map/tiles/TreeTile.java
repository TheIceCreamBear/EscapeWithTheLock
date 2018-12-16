package com.joseph.esclock.gameobject.map.tiles;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.reference.Reference.Animations;

public class TreeTile extends Tile {
	public TreeTile(int x, int y, Random r) {
		super(x, y, Animations.TREE, r);
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
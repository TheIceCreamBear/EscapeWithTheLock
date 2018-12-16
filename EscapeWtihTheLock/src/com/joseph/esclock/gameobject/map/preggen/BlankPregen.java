package com.joseph.esclock.gameobject.map.preggen;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.gameobject.map.tiles.GrassTile;

public class BlankPregen extends PregeneratedSection {
	
	public BlankPregen(int x, int y, Random r) {
		super(x, y, 1, r);
	}

	@Override
	protected void initSection(int x, int y) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < length; j++) {
				this.tiles[i].add(new GrassTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, r));
			}
		}
	}
}
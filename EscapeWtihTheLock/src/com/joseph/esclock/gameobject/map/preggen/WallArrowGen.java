package com.joseph.esclock.gameobject.map.preggen;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.gameobject.map.tiles.GrassTile;
import com.joseph.esclock.gameobject.map.tiles.WallTile;

public class WallArrowGen extends PregeneratedSection {
	private static final int GRASS_WIDTH = 3;
	
	public WallArrowGen(int x, int y, Random r) {
		super(x, y, 10, r);
	}

	@Override
	protected void initSection(int x, int y) {
		final boolean left = r.nextBoolean();
		int gap = 0;
		int grass = GRASS_WIDTH;
		
		int yoffset = 0;
		for (int row = 0; row < length; row++) {
			if (left) {
				for (int col = 0; col < tiles.length; col++) {
					if (gap > 0) {
						this.tiles[col].add(new WallTile(x + col * Tile.TILE_DIMENSION, y - yoffset, r));
						gap--;
					} else if (grass > 0) {
						this.tiles[col].add(new GrassTile(x + col * Tile.TILE_DIMENSION, y - yoffset, r));
						grass--;
					} else {
						this.tiles[col].add(new WallTile(x + col * Tile.TILE_DIMENSION, y - yoffset, r));
					}
				}
			} else {
				for (int col = tiles.length - 1; col >= 0; col--) {
					if (gap > 0) {
						this.tiles[col].add(new WallTile(x + col * Tile.TILE_DIMENSION, y - yoffset, r));
						gap--;
					} else if (grass > 0) {
						this.tiles[col].add(new GrassTile(x + col * Tile.TILE_DIMENSION, y - yoffset, r));
						grass--;
					} else {
						this.tiles[col].add(new WallTile(x + col * Tile.TILE_DIMENSION, y - yoffset, r));
					}
				}
			}
			if (row < length / 2) {
				gap = row + 1;
			} else {
				gap = length - row - 1;
			}
			grass = GRASS_WIDTH;
//			yoffset += Tile.TILE_DIMENSION / 2;
		}
	}
}
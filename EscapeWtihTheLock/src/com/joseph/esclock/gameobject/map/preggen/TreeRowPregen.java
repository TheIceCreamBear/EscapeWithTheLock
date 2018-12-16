package com.joseph.esclock.gameobject.map.preggen;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.gameobject.map.tiles.GrassTile;
import com.joseph.esclock.gameobject.map.tiles.TreeTile;

public class TreeRowPregen extends PregeneratedSection {
	
	public TreeRowPregen(int x, int y, Random r) {
		super(x, y, 1, r);
	}
	
	@Override
	protected void initSection(int x, int y) {		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < length; j++) {
				if (r.nextInt(4) == 0) {
					this.tiles[i].add(new TreeTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, r));
				} else {
					this.tiles[i].add(new GrassTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, r));
				}
			}
		}
	}
	
}
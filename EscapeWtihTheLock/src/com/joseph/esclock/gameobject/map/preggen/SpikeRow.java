package com.joseph.esclock.gameobject.map.preggen;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.gameobject.map.tiles.GrassTile;
import com.joseph.esclock.gameobject.map.tiles.SpikePitTile;

public class SpikeRow extends PregeneratedSection {
	public SpikeRow(int x, int y, Random r) {
		super(x, y, 1, r);
	}
	
	@Override
	protected void initSection(int x, int y) {
		int num = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < length; j++) {
				if (r.nextInt(4) != 0) {
					this.tiles[i].add(new SpikePitTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, r));
					num++;
				} else {
					this.tiles[i].add(new GrassTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, r));
				}
			}
		}
		if (num == 0) {
			int index = r.nextInt(tiles.length);
			this.tiles[index].removeLast();
			this.tiles[index].add(new GrassTile(x + index * Tile.TILE_DIMENSION, y, r));
		}
	}
}
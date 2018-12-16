package com.joseph.esclock.gameobject.map.preggen;

import java.util.Random;

import com.joseph.esclock.gameobject.map.Map;
import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.util.TileQueue;

public abstract class PregeneratedSection {
	protected TileQueue[] tiles;
	protected int length;
	protected Random r;
	
	protected int currentRow;
	protected int currentCol;
	
	protected PregeneratedSection(int x, int y, int lengthOfSection, Random r) {
		this.tiles = new TileQueue[Map.GAME_MAP_WIDTH];
		this.length = lengthOfSection;
		for (int i = 0; i < tiles.length; i++) {
			this.tiles[i] = new TileQueue(lengthOfSection);
		}
		this.r = r;
		
		this.currentCol = 0;
		this.currentRow = 0;
		
		this.initSection(x, y);
	}
	
	protected abstract void initSection(int x, int y);
	
	public int getCurrentGenerationY() {
		if (hasNextRow()) {
			return this.tiles[currentCol].peekAt(currentRow).getY();
		}
		return -1;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public boolean hasNextRow() {
		return this.currentRow < this.length;
	}
	
	public Tile getNext() {
		Tile t = this.tiles[currentCol].peekAt(currentRow);
		this.currentCol++;
		if (this.currentCol == Map.GAME_MAP_WIDTH) {
			this.currentCol = 0;
			this.currentRow++;
		}
		
		return t;
	}
}
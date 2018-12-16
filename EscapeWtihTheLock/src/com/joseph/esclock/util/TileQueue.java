package com.joseph.esclock.util;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.joseph.esclock.gameobject.map.Tile;

public class TileQueue {
	private final ArrayList<Tile> queue;
	private final int capacity;
	private int numberObjects;
	
	public TileQueue(int capacity) {
		this.queue = new ArrayList<Tile>(capacity);
		this.capacity = capacity;
		this.numberObjects = 0;
	}
	
	public synchronized void update(int distance, int visible) {
		for (int i = 0; i < queue.size(); i++) {
			Tile tile = this.queue.get(i);
			tile.moveTileY(distance);
			
			tile.update();
		}
	}
	
	public synchronized void draw(Graphics2D g, int visible) {
		for (int i = 0; i < visible && i < this.queue.size(); i++) {
			queue.get(i).draw(g);
		}
	}
	
	public synchronized void add(Tile t) {
		if (this.numberObjects == this.capacity)  {
			this.remove();
		}
		this.queue.add(t);
		this.numberObjects++;
	}
	
	public synchronized boolean contains(Tile t) {
		return this.queue.contains(t);
	}
	
	public synchronized Tile peek() {
		return this.queue.get(0);
	}
	
	public synchronized Tile peekAt(int i) {
		return this.queue.get(i);
	}
	
	public synchronized Tile peekLast() {
		return this.queue.get(numberObjects - 1);
	}
	
	public synchronized Tile remove() {
		if (this.numberObjects <= 0) {
			this.numberObjects = 0;
			return null;
		}
		this.numberObjects--;
		return this.queue.remove(0);
	}
	
	public synchronized Tile removeLast() {
		if (this.numberObjects <= 0) {
			this.numberObjects = 0;
			return null;
		}
		return this.queue.remove(this.numberObjects--);
	}
}
package com.joseph.esclock.gameobject.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import com.joseph.esclock.gameobject.Enemy;
import com.joseph.esclock.gameobject.EnemyFlyer;
import com.joseph.esclock.gameobject.EnemyWalker;
import com.joseph.esclock.gameobject.GameObject;
import com.joseph.esclock.gameobject.Player;
import com.joseph.esclock.gameobject.map.preggen.BlankPregen;
import com.joseph.esclock.gameobject.map.preggen.PregeneratedSection;
import com.joseph.esclock.gameobject.map.preggen.SpikeRow;
import com.joseph.esclock.gameobject.map.preggen.TreeRowPregen;
import com.joseph.esclock.gameobject.map.preggen.WallArrowGen;
import com.joseph.esclock.gameobject.map.tiles.GrassTile;
import com.joseph.esclock.gameobject.map.tiles.SpikePitTile;
import com.joseph.esclock.gameobject.map.tiles.TreeTile;
import com.joseph.esclock.gameobject.map.tiles.WallTile;
import com.joseph.esclock.reference.ScreenReference;
import com.joseph.esclock.util.TileQueue;

public class Map extends GameObject {
	public static int mapMovementSpeed = 2; // TODO speed may vary
	public static final int GAME_MAP_WIDTH = 10;
	private static final int TILES_VERTCALLY_ON_SCREEN = ScreenReference.HEIGHT / Tile.TILE_DIMENSION;
	private static final int TILES_HORIZONTALLY_ON_SCREEN = ScreenReference.WIDTH / Tile.TILE_DIMENSION;
	private static final int INACCESSIBLE_MAP_WIDTH = TILES_HORIZONTALLY_ON_SCREEN / 2 - GAME_MAP_WIDTH / 2;
	private static final int TILES_IN_LANE = TILES_VERTCALLY_ON_SCREEN * 2;
	
	private final HashMap<Class<? extends PregeneratedSection>, Double> chances;
	
	private TileQueue[] lanes;
	private Player player;
	private ArrayList<Enemy> enemies;
	private Random rand;
	private PregeneratedSection currentSection;
	private int delay = 40;
	private int randomGenDelay = 0;
	private int sawnDelay = 20;
	private int distanceTravled = 0;

	public Map(Player player) {
		super(0, TILES_VERTCALLY_ON_SCREEN * Tile.TILE_DIMENSION);
		mapMovementSpeed = 2;
		this.lanes = new TileQueue[TILES_HORIZONTALLY_ON_SCREEN];
//		this.rand = new Random(123456789);
		this.rand = new Random();
		this.player = player;
		this.enemies = new ArrayList<Enemy>();
		
		for (int i = 0; i < lanes.length; i++) {
			this.lanes[i] = new TileQueue(TILES_IN_LANE);
		}
		
		this.chances = new HashMap<Class<? extends PregeneratedSection>, Double>();
		this.chances.put(SpikeRow.class, 0.04166667); // 1/24
		this.chances.put(WallArrowGen.class, 0.03125); // 1/32
		this.chances.put(TreeRowPregen.class, .25); // 1/4
		
		init();
	}
	
	private void init() {
		for (int j = 0; j < TILES_IN_LANE; j++) {
			if (j < TILES_VERTCALLY_ON_SCREEN) {
				currentSection = new BlankPregen(INACCESSIBLE_MAP_WIDTH * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, rand);
			} else {
				currentSection = getNextPreGen(INACCESSIBLE_MAP_WIDTH * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION);
			}

			for (int i = 14; i < lanes.length - 14; i++) {
				if (i == INACCESSIBLE_MAP_WIDTH - 1 || i == TILES_HORIZONTALLY_ON_SCREEN - INACCESSIBLE_MAP_WIDTH) {
					this.lanes[i].add(new WallTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, rand));
				} else if (i < INACCESSIBLE_MAP_WIDTH || i > TILES_HORIZONTALLY_ON_SCREEN - INACCESSIBLE_MAP_WIDTH) {
					this.lanes[i].add(new GrassTile(x + i * Tile.TILE_DIMENSION, y - j * Tile.TILE_DIMENSION, rand));
				} else {
					if (this.currentSection.hasNextRow()) {
						try {
							this.lanes[i].add(this.currentSection.getNext());
						} catch (IndexOutOfBoundsException e) {
							System.err.println(this.currentSection);
							e.printStackTrace();
						}				
					}
				}
			}
		}
	}
	
	private PregeneratedSection getNextPreGen(int x, int y) {
		if (this.randomGenDelay > 0) {
			this.randomGenDelay--;
			return new BlankPregen(x, y, rand);
		}
		double chance = rand.nextDouble();
		double tileAdd = 0;
		Set<Class<? extends PregeneratedSection>> keySet = this.chances.keySet();
		for (Class<? extends PregeneratedSection> class1 : keySet) {
			tileAdd += this.chances.get(class1);
			if (tileAdd > chance) {
				return getSectionForClass(class1, x, y);
			}
		}
		
		return new BlankPregen(x, y, rand);
	}
	
	private PregeneratedSection getSectionForClass(Class<? extends PregeneratedSection> clazz, int x, int y) {
		if (clazz.equals(SpikeRow.class)) {
			this.randomGenDelay = rand.nextInt(2) + 2;
			return new SpikeRow(x, y, rand);
		} else if (clazz.equals(WallArrowGen.class)) {
			this.randomGenDelay = rand.nextInt(1) + 2;
			return new WallArrowGen(x, y, rand);
		} else if (clazz.equals(TreeRowPregen.class)) {
			this.randomGenDelay = rand.nextInt(2);
			return new TreeRowPregen(x, y, rand);
		} else {
			return new BlankPregen(x, y, rand);
		}
	}
	
	private void generateNewRow() {
		if (!this.currentSection.hasNextRow()) {
			Tile top = lanes[INACCESSIBLE_MAP_WIDTH + 1].peekLast();
			int generationY = top.getY() - Tile.TILE_DIMENSION;
			this.currentSection = getNextPreGen(INACCESSIBLE_MAP_WIDTH * Tile.TILE_DIMENSION, generationY);
		}
		
		int generationY = this.currentSection.getCurrentGenerationY();
		for (int i = 14; i < lanes.length - 14; i++) {
			if (i == INACCESSIBLE_MAP_WIDTH - 1 || i == TILES_HORIZONTALLY_ON_SCREEN - INACCESSIBLE_MAP_WIDTH) {
				this.lanes[i].add(new WallTile(x + i * Tile.TILE_DIMENSION, generationY, rand));
			} else if (i < INACCESSIBLE_MAP_WIDTH || i > TILES_HORIZONTALLY_ON_SCREEN - INACCESSIBLE_MAP_WIDTH) {
				this.lanes[i].add(new GrassTile(x + i * Tile.TILE_DIMENSION, generationY, rand));
			} else {
				try {
					this.lanes[i].add(this.currentSection.getNext());
				} catch (IndexOutOfBoundsException e) {
					System.err.println(this.currentSection);
					e.printStackTrace();
				}
			}
		}
	}
	
	public Random getRand() {
		return this.rand;
	}
	
	public boolean grassTile(int lane, int y) {
		int playerTileLoc = (this.lanes[INACCESSIBLE_MAP_WIDTH + 1].peek().getY() - y) / Tile.TILE_DIMENSION + 1;
		Tile playerTile = this.lanes[INACCESSIBLE_MAP_WIDTH + lane].peekAt(playerTileLoc);
		if (playerTile instanceof GrassTile) {
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		if (delay > 0) {
			delay--;
			return;
		}
		
		if (this.player.isDead()) {
			for (int i = 0; i < lanes.length; i++) {
				lanes[i].update(0, TILES_VERTCALLY_ON_SCREEN + 5);
			}
			return;
		}
		
		if (this.distanceTravled > 30000 && mapMovementSpeed == 7) {
			mapMovementSpeed = 8;
		} else if (this.distanceTravled > 20000 && mapMovementSpeed == 6) {
			mapMovementSpeed = 7;
		} else if (this.distanceTravled > 10000 && mapMovementSpeed == 5) {
			mapMovementSpeed = 6;
		} else if (this.distanceTravled > 5000 && mapMovementSpeed == 4) {
			mapMovementSpeed = 5;
		} else if (this.distanceTravled > 2500 && mapMovementSpeed == 3) {
			mapMovementSpeed = 4;
		} else if (this.distanceTravled > 1000 && mapMovementSpeed == 2) {
			mapMovementSpeed = 3;
		}
		
		if (this.sawnDelay > 0) {
			this.sawnDelay--;
		} else {
			if (rand.nextDouble() < .05) {
				int xOffset = (rand.nextInt(3) - 1) * Tile.TILE_DIMENSION;
				if (rand.nextBoolean()) {
					this.enemies.add(new EnemyFlyer((this.player.getLane() + INACCESSIBLE_MAP_WIDTH) * Tile.TILE_DIMENSION + 20 + xOffset, 
							this.player.getY() - Tile.TILE_DIMENSION * 10));
				} else {
					this.enemies.add(new EnemyWalker((this.player.getLane() + INACCESSIBLE_MAP_WIDTH) * Tile.TILE_DIMENSION + 20 + xOffset, 
							this.player.getY() - Tile.TILE_DIMENSION * 10));
				}
				this.sawnDelay = 60;
			}
		}
		
		this.y += mapMovementSpeed;
		this.distanceTravled += mapMovementSpeed;
		
		for (int i = 0; i < lanes.length; i++) {
			lanes[i].update(mapMovementSpeed, TILES_VERTCALLY_ON_SCREEN + 5);
		}
		
		Tile bottom = lanes[INACCESSIBLE_MAP_WIDTH + 1].peek();
		if (bottom.getY() > ScreenReference.HEIGHT + Tile.TILE_DIMENSION * 3) {
			this.generateNewRow();
		}
		
		this.player.moveYByMap(mapMovementSpeed);
		int playerLane = this.player.getLane() + INACCESSIBLE_MAP_WIDTH;
		int playerTileLoc = (this.lanes[INACCESSIBLE_MAP_WIDTH + 1].peek().getY() - this.player.getY()) / Tile.TILE_DIMENSION + 1;
		Tile playerTile = this.lanes[playerLane].peekAt(playerTileLoc);
		if (playerTile instanceof SpikePitTile) {
			this.player.kill();
			return;
		} else if (playerTile instanceof TreeTile || playerTile instanceof WallTile) {
			if (playerTile.getBoundingBox().intersects(player.getBoundingBox())) {
				this.player.moveYByMap(mapMovementSpeed);
			}
		}
		
		if (this.player.getY() > ScreenReference.HEIGHT) {
			this.player.kill();
			return;
		}
		
		for (Enemy enemy : enemies) {
			enemy.update();
			if (enemy.getBoundingBox().intersects(this.player.getBoundingBox())) {
				this.player.kill();
				return;
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		for (int i = 0; i < lanes.length; i++) {
			lanes[i].draw(g, TILES_VERTCALLY_ON_SCREEN + 5);
		}
		
		for (Enemy enemy : enemies) {
			enemy.draw(g);
		}
		
		g.setFont(ScreenReference.getTheFont());
		g.setColor(Color.white);
		g.drawString("Distance Travled: " + this.distanceTravled, 20, 20);
	}

	@Override
	public Rectangle getBoundingBox() {
		return null;
	}
}
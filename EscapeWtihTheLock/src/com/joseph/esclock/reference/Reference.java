package com.joseph.esclock.reference;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.joseph.esclock.gui.Animation;
import com.joseph.esclock.gui.Animation.AnimationFrame;

/**
 * Commonly used Objects or primitives stored in one location for quick Reference
 * 
 * @author Joseph
 *
 */
public class Reference {
	public static final String DIRPREFIX = System.getProperty("user.dir");
	public static boolean DEBUG_MODE = false;
	public static boolean HARD_CORE_DEBUG_MODE = false;
	
	public static class Colors {
		public static final Color CURSOR_COLOR = new Color(96, 96, 96);
	}
	
	public static class Fonts {
		private static Map<TextAttribute, Object> map;
		public static void init() {		
			map = new Hashtable<TextAttribute, Object>();
			map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			DEFAULT_UNDERLINED_FONT = DEFAULT_FONT.deriveFont(map);
		}
		
		public static final Font DEFAULT_FONT = new Font("Consolas", 0, 20);
		public static final Font MAP_FONT = new Font("Consolas", 0, 14);
		public static Font DEFAULT_UNDERLINED_FONT;
	}
	
	public static class Animations {
		public static final Animation GRASS = new Animation(new AnimationFrame(Images.GRASS_1, 20), new AnimationFrame(Images.GRASS_2, 20));
		public static final Animation WALL = new Animation(new AnimationFrame(Images.WALL, 200));
		public static final Animation TREE = new Animation(new AnimationFrame(Images.TREE_1, 20), new AnimationFrame(Images.TREE_2, 20));
		public static final Animation SPIKE_PIT = new Animation(new AnimationFrame(Images.SPIKE, 200));
		public static final Animation FLYER = new Animation(new AnimationFrame(Images.ENEMY_FLYER_MIDDLE, 1), 
				new AnimationFrame(Images.ENEMY_FLYER_FORWARD, 18), new AnimationFrame(Images.ENEMY_FLYER_MIDDLE, 17), 
				new AnimationFrame(Images.ENEMY_FLYER_BACK, 18), new AnimationFrame(Images.ENEMY_FLYER_MIDDLE, 16));
		public static final Animation WALKER = new Animation(new AnimationFrame(Images.ENEMY_WALK_STILL, 1), 
				new AnimationFrame(Images.ENEMY_WALK_LEFT, 18), new AnimationFrame(Images.ENEMY_WALK_STILL, 17), 
				new AnimationFrame(Images.ENEMY_WALK_RIGHT, 18), new AnimationFrame(Images.ENEMY_WALK_STILL, 16));
	}
	
	public static class Images {
		public static Image GRASS_1;
		public static Image GRASS_2;
		public static Image WALL;
		public static Image TREE_1;
		public static Image TREE_2;
		public static Image SPIKE;
		
		public static Image PLAYER_STILL;
		public static Image PLAYER_LEFT;
		public static Image PLAYER_RIGHT;
		
		public static Image ENEMY_WALK_STILL;
		public static Image ENEMY_WALK_LEFT;
		public static Image ENEMY_WALK_RIGHT;
		
		public static Image ENEMY_FLYER_FORWARD;
		public static Image ENEMY_FLYER_MIDDLE;
		public static Image ENEMY_FLYER_BACK;
		
		public static Image COIN;
		
		static {
			try {
				GRASS_1 = ImageIO.read(new File("resources/tiles/grass/grass1-2x.png"));
				GRASS_2 = ImageIO.read(new File("resources/tiles/grass/grass2-2x.png"));
				WALL = ImageIO.read(new File("resources/tiles/walls/wall2x.png"));
				TREE_1 = ImageIO.read(new File("resources/tiles/trees/tree1-2x.png"));
				TREE_2 = ImageIO.read(new File("resources/tiles/trees/tree2-2x.png"));
				SPIKE = ImageIO.read(new File("resources/tiles/pit-spikes/spikePit.png"));
				
				PLAYER_STILL = ImageIO.read(new File("resources/player/playerStill-2x.png"));
				PLAYER_LEFT = ImageIO.read(new File("resources/player/playerLeft-2x.png"));
				PLAYER_RIGHT = ImageIO.read(new File("resources/Player/playerRight-2x.png"));
				
				ENEMY_WALK_STILL = ImageIO.read(new File("resources/enemy/walker/enemyStill.png"));
				ENEMY_WALK_LEFT = ImageIO.read(new File("resources/enemy/walker/enemyLeft.png"));
				ENEMY_WALK_RIGHT = ImageIO.read(new File("resources/enemy/walker/enemyRight.png"));
				
				ENEMY_FLYER_FORWARD = ImageIO.read(new File("resources/enemy/flyer/enemyForward.png"));
				ENEMY_FLYER_MIDDLE = ImageIO.read(new File("resources/enemy/flyer/enemyMiddle.png"));
				ENEMY_FLYER_BACK = ImageIO.read(new File("resources/enemy/flyer/enemyBack.png"));
				
				COIN = ImageIO.read(new File("resources/coin.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
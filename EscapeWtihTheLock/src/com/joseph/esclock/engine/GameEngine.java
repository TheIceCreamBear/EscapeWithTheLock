package com.joseph.esclock.engine;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.joseph.esclock.gameobject.Coin;
import com.joseph.esclock.gameobject.GameObject;
import com.joseph.esclock.gameobject.Player;
import com.joseph.esclock.gameobject.RenderLockObject;
import com.joseph.esclock.gameobject.map.Map;
import com.joseph.esclock.gameobject.map.Tile;
import com.joseph.esclock.gui.AbstractButton;
import com.joseph.esclock.gui.IGuiElement;
import com.joseph.esclock.gui.windows.ConsoleWindow;
import com.joseph.esclock.gui.windows.EventWindow;
import com.joseph.esclock.gui.windows.PauseMenuWindow;
import com.joseph.esclock.handlers.GKELAH;
import com.joseph.esclock.handlers.MouseHandler;
import com.joseph.esclock.reference.Reference;
import com.joseph.esclock.reference.ScreenReference;
import com.joseph.esclock.threads.EventThread;
import com.joseph.esclock.threads.RenderThread;
import com.joseph.esclock.threads.ShutdownThread;

/**
 * Class responsible for doing all the heavy lifting in the game. Hold the
 * engine Algorithm and references to all objects used by the game.
 * 
 * @author David Santamaria - Original Author
 * @author Joseph Terribile - Current Maintainer
 */
public final class GameEngine {
	
	/**
	 * boolean that expressed the state of the engine, whether it is
	 * <code> running </code> or not
	 */
	private static boolean running = true;
	/**
	 * The instance of the GameEngine
	 */
	private static GameEngine instance;
	/**
	 * Displayed at the top of the screen. Expresses the fps, and time and other
	 * such things
	 */
	private static String stats = "";
	
	/**
	 * Used to display the screen
	 */
	private JFrame frame;
	
	/**
	 * First graphics instance
	 */
	private Graphics2D g;
	/**
	 * BufferedImage graphics instance
	 */
	private Graphics2D g2;
	/**
	 * Image that is displayed on the screen
	 */
	private BufferedImage i;
	
	private FontRenderContext frc;
	
	// Threads
	private RenderLockObject rlo;
	private RenderThread rtInstance;
	private ShutdownThread sdtInstance;
	private EventThread et;
	
	/**
	 * Instance of {@link GKELAH GKELAH} stored to keep a reference to it.
	 */
	private GKELAH keyHandlerInstance;
	
	/**
	 * the instance of the mouse handler object
	 */
	private MouseHandler mouseHandlerInstace;
	
	/**
	 * if the cursor is in the hand mode
	 */
	private boolean handCursor;
	
	/**
	 * the instance of the pause menu window
	 */
	private PauseMenuWindow pmw;
	private Map map;
	private Player player;
	private String playerDeadString = "You Died.";
	
	// LISTS OF OBJECTS
	private static final ArrayList<IGuiElement> GUI_ELEMENTS = new ArrayList<IGuiElement>();
	private static final ArrayList<AbstractButton> BUTTONS = new ArrayList<AbstractButton>();
	private static final ArrayList<GameObject> OBJECTS = new ArrayList<GameObject>();
	private static final ArrayList<Coin> COINS = new ArrayList<Coin>();
	
	/**
	 * @return the instance of the GameEngine
	 */
	public static GameEngine getInstance() {
		return instance;
	}
	
	public static GKELAH getKeyListener() {
		return instance.keyHandlerInstance;
	}
	
	public static Map getMap() {
		return instance.map;
	}
	
	/**
	 * @return state of {@link GameEngine#running GameEngine.running}
	 */
	public static boolean isRunning() {
		return running;
	}
	
	public static void main(String[] args) {
		if (Reference.DEBUG_MODE) {
			System.out.println(Runtime.getRuntime().maxMemory());
			System.err.println("x: " + ScreenReference.WIDTH + "y: " + ScreenReference.HEIGHT);
		}
		instance = new GameEngine();
		instance.run();
	}
	
	/**
	 * Starts the GameEngine
	 */
	public static void startGameEngine() {
		instance = new GameEngine();
		instance.run();
	}
	
	/**
	 * Initializes and instantiates
	 */
	private GameEngine() {
		initialize();
	}
	
	/**
	 * Initializes all the stuff
	 */
	private void initialize() {
		instance = this;
		if ((System.getProperty("os.name").contains("Windows") || System.getProperty("os.name").contains("windows")) && !System.getProperty("user.home").contains("AppData")) {
			System.setProperty("user.home", System.getProperty("user.home") + "/AppData/Roaming");
		}
		
		ScreenReference.doScreenCalc();
		
		Reference.Fonts.init();
		
		this.sdtInstance = new ShutdownThread();
		Runtime.getRuntime().addShutdownHook(sdtInstance);
		
		this.frame = new JFrame("Game Template");
		this.frame.setBounds(0, 0, ScreenReference.WIDTH, ScreenReference.HEIGHT);
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setVisible(true);
		
		this.rlo = new RenderLockObject();
		this.rtInstance = new RenderThread("RenderThread", this.rlo, this);
		this.rtInstance.start();
		
		this.keyHandlerInstance = new GKELAH();
		this.frame.addKeyListener(keyHandlerInstance);
		
		this.mouseHandlerInstace = new MouseHandler();
		this.frame.addMouseListener(mouseHandlerInstace);
		
		this.i = new BufferedImage(ScreenReference.WIDTH, ScreenReference.HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.g2 = this.i.createGraphics();
		this.g = (Graphics2D) frame.getGraphics();
		
		// Turn on AntiAliasing
		this.g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		this.frc = ((Graphics2D) g2).getFontRenderContext();
		
		this.et = new EventThread();
		this.et.start();
		
		com.joseph.esclock.util.FileSaveSystem.init();
		
		this.pmw = new PauseMenuWindow();
		
		// Start adding here
		this.addNewElement(new EventWindow());
		this.addNewElement(new ConsoleWindow());
		this.addNewElement(pmw);
		
		// GAME Starts here
		this.player = new Player(24 * Tile.TILE_DIMENSION - 20, ScreenReference.HEIGHT / 2, 4);
		this.map = new Map(this.player);
		this.addGameObject(this.player);
		
		
		com.joseph.esclock.util.FileSaveSystem.postInit();
		
		System.gc();
		
		this.releaseFocous();
	}
	
	/**
	 * removes a button from the mouse listeners
	 * 
	 * @param b
	 *            - the button
	 * @return if the removal was successful
	 */
	public boolean removeButton(AbstractButton b) {
		if (BUTTONS.contains(b)) {
			boolean bo = BUTTONS.remove(b);
			bo &= this.mouseHandlerInstace.removeMouseReliant(b);
			return bo;
		}
		return false;
	}
	
	/**
	 * adds a new button to listen to for mouse events
	 * 
	 * @param b
	 *            - the button
	 */
	public void addButton(AbstractButton b) {
		if (BUTTONS.contains(b)) {
			return;
		}
		
		this.mouseHandlerInstace.registerMouseReliant(b);
		BUTTONS.add(b);
	}
	
	public void addGameObject(GameObject o) {
		if (OBJECTS.contains(o)) {
			return;
		}
		OBJECTS.add(o);
	}
	
	public void addCoin(Coin c) {
		if (COINS.contains(c)) {
			return;
		}
		COINS.add(c);
	}
	
	/**
	 * adds a new element to the elements list
	 * 
	 * @param e
	 *            - the element
	 */
	private void addNewElement(IGuiElement e) {
		if (GUI_ELEMENTS.contains(e)) {
			return;
		}
		
		GUI_ELEMENTS.add(e);
		if (e instanceof AbstractButton) {
			this.addButton((AbstractButton) e);
		}
	}
	
	private ArrayList<Coin> toBeDeleted = new ArrayList<Coin>();
	
	/**
	 * Loops through all the updatables and updates them
	 * 
	 * @param deltaTime
	 *            - Time between each frame (used to evaluate things within
	 *            update methods of each object)
	 */
	private void update(double deltaTime) {
		if (pmw.isVisible()) {
			pmw.updateUpdateableElements(deltaTime);
			return;
		}
		
		this.keyHandlerInstance.captureInput();
		
		this.map.update();
		
		for (GameObject object : OBJECTS) {
			object.update();
		}
		
		for (IGuiElement gui : GUI_ELEMENTS) {
			gui.updateUpdateableElements(deltaTime);
		}
		
		toBeDeleted.clear();
		for (Coin coin : COINS) {
			if (!coin.keepUpdating()) {
				toBeDeleted.add(coin);
			}
		}
		for (Coin coin : toBeDeleted) {
			COINS.remove(coin);
		}
	}
	
	/**
	 * Loops through all the Drawables and draws them
	 * 
	 * @param g
	 *            - Graphics instance to draw upon
	 * @param observer
	 *            - observer to put graphics instance upon
	 */
	private void render(Graphics g, ImageObserver observer) {
		
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, ScreenReference.WIDTH, ScreenReference.HEIGHT);
		
		this.map.draw(g2);
		
		for (GameObject object : OBJECTS) {
			object.draw(g2);
		}
		
		for (IGuiElement iGuiElement : GUI_ELEMENTS) {
			iGuiElement.drawBackground(g2, observer);
			iGuiElement.drawUpdateableElements(g2, observer);
		}
		
		if (this.player.isDead()) {
			g2.setColor(Color.RED);
			g2.setFont(Reference.Fonts.DEFAULT_FONT);
			Rectangle2D r = Reference.Fonts.DEFAULT_FONT.getStringBounds(playerDeadString, frc);
			g2.drawString(playerDeadString, (int) (ScreenReference.WIDTH / 2 - r.getWidth() / 2), (int) (ScreenReference.HEIGHT / 2 - r.getHeight() / 2));
		}
		
		if (Reference.DEBUG_MODE) {
			g2.setColor(Color.GREEN);
			g2.setFont(Reference.Fonts.DEFAULT_FONT);
			g2.drawString(stats, 25, 60);
			
			Point p = getMouseLocation();
			if (p != null) {
				String s = p.toString();
				Rectangle2D r = Reference.Fonts.DEFAULT_FONT.getStringBounds(s, frc);
				int yOff = (int) r.getHeight();
				g2.drawString(s, p.x, p.y + yOff);
			}
		}
		
		g.drawImage(this.i, 0, 0, this.frame);
	}
	
	/**
	 * Short hand for {@link GameEngine#render(Graphics, ImageObserver)}, used
	 * by {@link com.joseph.thedarknessbeyond.threads.RenderThread RenderThread}
	 * to render the game onto the frame.
	 */
	public void render() {
		render(g, frame);
	}
	
	/**
	 * Runs the GameEngine
	 */
	private void run() {
		long elapsedTime = System.currentTimeMillis();
		final double ticksPerSecond = 60.0;
		double msPerFrame = 1000 / ticksPerSecond;
		int ticks = 0;
		int fps = 0;
		long timer = System.currentTimeMillis();
		long startTime;
		int seconds = 0;
		int minutes = 0;
		int hours = 0;
		
		while (running) {
			startTime = System.currentTimeMillis();
			
			update(0);
			ticks++;
			
			synchronized (rlo) {
				rlo.setWasNotified(true);
				rlo.notify();
			}
			fps++;
			
			elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime < msPerFrame) {
				long sleepTime = (long) (msPerFrame - elapsedTime);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("RUNNING BEHIND.");
			}
			
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				seconds++;
				if (seconds > 60) {
					seconds %= 60;
					minutes++;
					
					if (minutes > 60) {
						minutes %= 60;
						hours++;
					}
				}
				
				// GT stands for GameTime.
				stats = "Ticks: " + ticks + ", FPS: " + fps + ", GT: " + ((hours < 10) ? "0" + hours : hours) + ":" + ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
				if (Reference.DEBUG_MODE) {
					System.out.println(stats);
				}
				ticks = 0;
				fps = 0;
				if (Reference.DEBUG_MODE) {
					System.out.println(Runtime.getRuntime().freeMemory());
				}
				System.gc();
				if (Reference.DEBUG_MODE) {
					System.out.println(Runtime.getRuntime().freeMemory());
				}
			}
		}
	}
	
	public void reset() {
		synchronized (OBJECTS) {
			OBJECTS.remove(this.player);
			this.player = new Player(24 * Tile.TILE_DIMENSION - 20, ScreenReference.HEIGHT / 2, 4);
			this.map = new Map(this.player);
			this.addGameObject(this.player);
		}
	}
	
	/**
	 * sets the cursor of the frame to the hand
	 */
	public void setSelectMouse() {
		if (handCursor) {
			return;
		}
		this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.handCursor = true;
	}
	
	/**
	 * sets the cursor of the frame to the default
	 */
	public void setDefaultMouse() {
		if (!handCursor) {
			return;
		}
		this.frame.setCursor(Cursor.getDefaultCursor());
		this.handCursor = false;
	}
	
	/**
	 * gets the location of the mouse in the frame
	 * 
	 * @return - the location of the mouse relative to the frame
	 */
	public Point getMouseLocation() {
		return this.frame.getContentPane().getMousePosition();
	}
	
	/**
	 * Refocuses the frame to make sure that key events are captured
	 */
	public void releaseFocous() {
		this.frame.requestFocus();
	}
	
	public FontRenderContext getFrc() {
		return this.frc;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
/*
 * -XX:+UnlockCommercialFeatures -XX:+FlightRecorder
 * -XX:FlightRecorderOptions=stackdepth=2048
 * -XX:StartFlightRecording=duration=60m,filename=GameTemplate.jfr
 */
package com.joseph.esclock.threads;

import com.joseph.esclock.engine.GameEngine;
import com.joseph.esclock.gameobject.RenderLockObject;

/**
 * This thread is responsible for initiating the rendering of the engine onto the frame.
 * Using multithreading techniques, this thread is designed to only run once the engine
 * notifies the {@link com.joseph.thedarknessbeyond.gameobject.RenderLockObject RenderLockObject}
 * allowing the update thread to update for the next frame while this thread renders the
 * current frame.
 * @author Joseph
 *
 */
public class RenderThread extends Thread {
	private RenderLockObject rlo;
	private GameEngine gEngine;
	
	/**
	 * Constructs a new RenderThread
	 * @param name - name of the thread
	 * @param rlo - {@link com.joseph.thedarknessbeyond.gameobject.RenderLockObject RenderLockObject} 
	 * 		used by {@link com.joseph.esclock.engine.GameEngine GameEngine} to communicate across threads.
	 * @param ge - 
	 */
	public RenderThread(String name, RenderLockObject rlo, GameEngine ge) {
		super(name);
		this.rlo = rlo;
		this.gEngine = ge;
	}
	
	@Override
	public void run() {
		synchronized (rlo) {
			while (GameEngine.isRunning()) {
				try {
					rlo.wait();
					if (!rlo.wasNotified()) {
						continue;
					} else {
						gEngine.render();
						rlo.setWasNotified(false);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
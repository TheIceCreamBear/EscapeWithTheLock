package com.joseph.esclock.threads;

import com.joseph.esclock.engine.GameEngine;
import com.joseph.esclock.event.EventBus;

/**
 * the thread that resolves all events on the event bus
 * @author Joseph
 *
 */
public class EventThread extends Thread {
	public EventThread() {
		super("EventHandlerThread");
	}

	@Override
	public void run() {
		while (GameEngine.isRunning()) {
			if (EventBus.EVENT_BUS.hasUnResolved()) {
				EventBus.EVENT_BUS.resolve();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
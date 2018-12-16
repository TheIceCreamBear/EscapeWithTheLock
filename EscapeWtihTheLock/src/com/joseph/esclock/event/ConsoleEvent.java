package com.joseph.esclock.event;

/**
 * Event class specific to console commands
 * 
 * @author Joseph
 *
 */
public class ConsoleEvent extends Event {
	public ConsoleEvent(String s) {
		super("CONSOLE:" + s);
	}
}
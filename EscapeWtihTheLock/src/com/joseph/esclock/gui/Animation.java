package com.joseph.esclock.gui;

import java.awt.Image;
import java.util.Random;

public class Animation {
	private final int numberFrames;
	private int currentFrame;
	private int frameTimer;
	private int currentFrameDuration;
	private AnimationFrame[] animFrames;
	
	public Animation(Animation other) {
		this(other.animFrames);
	}
	
	public Animation(AnimationFrame... frames) {
		this.animFrames = frames;
		this.numberFrames = frames.length;
		this.currentFrame = 0;
		this.currentFrameDuration = this.animFrames[this.currentFrame].getDuration();
	}
	
	public void setRandomStartInt(Random r) {
		int max = 0;
		for (int i = 0; i < animFrames.length; i++) {
			max += animFrames[i].getDuration();
		}
		
		int start = r.nextInt(max);
		
		this.frameTimer = start;
		this.currentFrame = 0;
		for (int i = 0; i < animFrames.length; i++) {
			if (frameTimer > this.animFrames[i].getDuration()) {
				this.frameTimer -= this.animFrames[i].getDuration();
				this.currentFrame++;
			}
		}
	}
	
	public void update() {
		if (this.numberFrames == 1) {
			return;
		}
		
		this.frameTimer++;
		if (this.frameTimer == this.currentFrameDuration) {
			this.frameTimer = 0;
			
			this.currentFrame++;
			if (this.currentFrame == this.numberFrames) {
				this.currentFrame = 0;
			}
			this.currentFrameDuration = this.animFrames[this.currentFrame].getDuration();
		}
//		System.err.println(this.currentFrame + "/" + this.numberFrames + " frames");
//		System.err.println(this.frameTimer + "/" + this.currentFrameDuration + " time");
	}
	
	public Image getCurrentImage() {
		return this.animFrames[this.currentFrame].getImage();
	}
	
	public void restart() {
		this.currentFrame = 0;
		this.currentFrameDuration = this.animFrames[this.currentFrame].getDuration();
		this.frameTimer = 0;
	}
	
	public static class AnimationFrame {
		private Image image;
		private int duration;
		
		public AnimationFrame(Image image, int duration) {
			this.image = image;
			this.duration = duration;
		}
		
		public int getDuration() {
			return this.duration;
		}
		
		public Image getImage() {
			return this.image;
		}
	}
}
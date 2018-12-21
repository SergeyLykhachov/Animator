package com.yahoo.slykhachov.animator.model;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import com.yahoo.slykhachov.animator.util.Subject;
import com.yahoo.slykhachov.animator.util.Observer;

public class AnimationModel implements Subject {
	//public static int FRAME_RATE;
	private AnimationElement rootAe;
	private int frameRate;
	private Timer timer;
	private boolean isOnPause;
	private List<Observer> observers;
	private List<AnimationElement> animationElementList;
	private Runnable runnable;
	
	public AnimationModel(Dimension dimension, int frameRate) {
		this.frameRate = frameRate;
		//FRAME_RATE = frameRate;
		this.rootAe = new AnimationElement.Builder(0, 0, this.frameRate)
			.width((int) dimension.getWidth())
			.height((int) dimension.getHeight())
			.setBackground(Color.ORANGE)
			.build();
		this.timer = new Timer(
			this.frameRate,
			//FRAME_RATE,
			ae -> {
				this.rootAe.update();
				this.notifyObservers();
		});
		//this.timer.start();////
		this.isOnPause = true;
		this.observers = new ArrayList<>();
		this.animationElementList = new ArrayList<>();
		AnimationElement ae1 = new AnimationElement.Builder(0, 0, this.frameRate)
		//AnimationElement ae1 = new AnimationElement.Builder(100, 100)
			.image(".//img//img.png")
			//.width(100)
			//.height(100)
			.alpha(0.0f)
			//.setBackground(Color.BLUE)
			.build();
		this.rootAe.addChild(ae1);
		AnimationElement ae2 = new AnimationElement.Builder(220, 330, this.frameRate)
			.image(".//img//img.png")
			//.image(".//img//Thermometer.png")
			.width(70)
			.height(70)
			.build();
		//this.rootAe.addChild(ae2);
		ae1.addChild(ae2);
		AnimationElement ae3 = new AnimationElement.Builder(70, 80, this.frameRate)
			.image(".//img//luigi.png")
			.width(70)
			.height(70)
			.build();
		ae2.addChild(ae3);
		AnimationElement ae4 = new AnimationElement.Builder(400, 300, this.frameRate)
			//.image(".//img//img.png")
			//.width(50)
			//.height(50)
			.build();
		ae3.addChild(ae4);
		AnimationElement ae5 = new AnimationElement.Builder(450, 400, this.frameRate)
			.build();
		this.rootAe.addChild(ae5);
		this.runnable = () -> {
			ae1.translate(200, 100, 20000, 10000);
			ae1.rotate(720.0, 2000, 2000);
			ae1.scaleX(3.0, 30000, 0);
			ae1.scaleX(1.0 / 3, 300, 30000);
			ae1.transformImageAlpha(1.0f, 25000, 0);
			ae1.transformImageAlpha(0.5f, 5000, 25000);
	
			ae2.translate(200, 100, 10000, 0);
			ae2.rotate(360.0, 10000, 0);
			ae2.scaleX(2.0, 10000, 0);
			ae2.scaleY(0.333, 10000, 10000);
			ae2.scaleX(0.333, 1000, 15000);
			ae2.transformImageAlpha(0.0f, 30000, 1000);
			
			ae3.translate(300, 300, 10000, 1000);
			ae3.translate(-300, -300, 5000, 20000);
			ae3.rotate(1080.0, 30000, 500);
			ae3.scaleY(3.0, 10000, 0);
			ae3.scaleY(0.333, 10000, 10000);
			//ae3.rotate(1079.0, 30000, 500);
			ae3.transformImageAlpha(0.0f, 30000, 1000);
			
			ae4.translate(-300, -300, 6000, 0);
			ae4.translate(300, 300, 25000, 6000);
			//ae4.rotate(-9710.0, 30000, 0);
			ae4.rotate(9720.0, 30000, 0);
			//ae4.scaleX(4.0, 20000, 0);
			ae4.addAnimationSequence(".//img//animations", 50L, 6);
			ae4.transformImageAlpha(0.0f, 30000, 1000);
		
			ae5.translateX(-450, 0L, 0L);
			ae5.translateX(1400, 15000L, 1L);
			//ae4.rotate(-9710.0, 30000, 0);
			//ae5.rotate(9720.0, 30000, 0);
			//ae4.scaleX(4.0, 20000, 0);
			ae5.addAnimationSequence(".//img//animations", 50L, 6);
			ae5.transformImageAlpha(0.0f, 3000, 1000);
			ae5.transformImageAlpha(1.0f, 500, 4500);
			
			//System.out.println(ae5.getSpacialInterpolators());
			//System.out.println(ae5.getImageInterpolators());
		};
		this.animationElementList.add(ae1);
		this.animationElementList.add(ae2);
		this.animationElementList.add(ae3);
		this.animationElementList.add(ae4);
		this.animationElementList.add(ae5);
		//this.runnable.run();
	}
	public Timer getTimer() {
		return this.timer;
	}
	public boolean isOnPause() {
		return this.isOnPause;
	}
	public void setPause(boolean b) {
		this.isOnPause = b;
	}
	public List<AnimationElement> getAeList() {
		return this.animationElementList;
	}
	public Runnable getRunnable() {
		return this.runnable;
	}
	public AnimationElement getRootAnimationElement() {
		return this.rootAe;
	}
	public static void flushAndReplenishInterpolatorLists(List<AnimationElement> aeList, Runnable r) {
		aeList.forEach(
			e -> {
				e.getSpacialInterpolators().clear();
				e.getImageInterpolators().clear();
				
		});
		r.run();
	}
	//public void setFrameRate(int frameRate) {
	//	this.frameRate = frameRate;
	//}
	@Override
	public void registerObserver(Observer o) {
		this.observers.add(o);
	}
	@Override
	public void removeObserver(Observer o) {
		this.observers.remove(o);
	}
	@Override
	public void notifyObservers() {
		observers.forEach(Observer::update);
	}
}

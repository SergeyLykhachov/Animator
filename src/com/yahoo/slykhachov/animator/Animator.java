package com.yahoo.slykhachov.animator;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
//import com.yahoo.slykhachov.animator.util.*;
//import com.yahoo.slykhachov.animator.model.AnimationElement;
//import com.yahoo.slykhachov.animator.model.TransformGroup;
import com.yahoo.slykhachov.animator.model.AnimatorModel;
import com.yahoo.slykhachov.animator.view.AnimatorView;

public class Animator {
	//public static final int FRAME_RATE = 40; // 25 frames/second
	private Animation animation;
	private AnimatorView animatorView;
	private AnimatorModel animatorModel;
	Animator() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		if (d.getWidth() < 1200 || d.getHeight() < 768) {
			d = new Dimension(800, 600);
			System.out.println("Small Screen found");
		} else {
			//d = new Dimension(1024, 768);
			//d = new Dimension(1200, 800);
			//d = new Dimension(1200, 750);
		}
		this.setAnimation(new Animation(d));
		this.setAnimatorView(new AnimatorView(this.getAnimation()));
		this.setAnimatorModel(new AnimatorModel());
	}
	public static void main(String[] sa) {
		EventQueue.invokeLater(Animator::new);
	}
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	public Animation getAnimation() {
		return this.animation;
	}
	public void setAnimatorView(AnimatorView animatorView) {
		this.animatorView = animatorView;
	}
	public AnimatorView getAnimatorView() {
		return this.animatorView;
	}
	public void setAnimatorModel(AnimatorModel animatorModel) {
		this.animatorModel = animatorModel;
	}
	public AnimatorModel getAnimatorModel() {
		return this.animatorModel;
	}
}

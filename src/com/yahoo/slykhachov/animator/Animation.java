package com.yahoo.slykhachov.animator;

import java.awt.Dimension;
import com.yahoo.slykhachov.animator.model.AnimationModel;
import com.yahoo.slykhachov.animator.view.AnimationView;

public class Animation {
	private AnimationModel model;
	private AnimationView view;
	public Animation(Dimension dimension) {
		this.model = new AnimationModel(dimension, 40);
		this.view = new AnimationView(dimension, model);
		this.model.registerObserver(view);
	}
	public AnimationModel getAnimationModel() {
		return this.model;
	}
	public AnimationView getAnimationView() {
		return this.view;
	}
}

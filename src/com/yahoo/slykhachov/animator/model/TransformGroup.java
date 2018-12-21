package com.yahoo.slykhachov.animator.model;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public abstract class TransformGroup {
	protected List<TransformGroup> list;
	protected AffineTransform at;
	public TransformGroup() {
		this.list = new ArrayList<>();
		this.at = new AffineTransform();
	}
	public AffineTransform getTransform() {
		return this.at;
	}
	public void addChild(TransformGroup tg) {
		this.list.add(tg);
	}
	public List<TransformGroup> getChildren() {
		return this.list;
	}
}

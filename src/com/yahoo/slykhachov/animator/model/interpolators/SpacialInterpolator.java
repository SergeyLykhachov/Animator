package com.yahoo.slykhachov.animator.model.interpolators;

import java.awt.geom.AffineTransform;

public interface SpacialInterpolator {
	void interpolate(double parentX,double parentY, double parentWidth,
		double parentHeight, double x, double y, double width,
		double height, AffineTransform... atArr);
}

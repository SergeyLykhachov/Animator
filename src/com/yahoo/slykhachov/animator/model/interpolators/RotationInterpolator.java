package com.yahoo.slykhachov.animator.model.interpolators;

import java.awt.geom.AffineTransform;
import com.yahoo.slykhachov.animator.util.function.SeptConsumer;

public class RotationInterpolator implements SpacialInterpolator {
	private double finalTheta;
	private double intermediateTheta;
	private double rotationAlpha;
	private double rotationAlphaStep;
	private long delayMillis;
	private int frameRate;
	private SeptConsumer<AffineTransform> consumer;
	public RotationInterpolator(double degrees, long durationMillis, long delayMillis,
			int frameRate, SeptConsumer<AffineTransform> consumer) {
		this.finalTheta = Math.toRadians(degrees);
		this.delayMillis = delayMillis;
		this.frameRate = frameRate;
		this.rotationAlphaStep = 1.0 / ((double) durationMillis / this.frameRate);
		this.intermediateTheta = 0.0;
		this.rotationAlpha = 0.0;
		this.consumer = consumer;
	}
	@Override
	public void interpolate(double parentX, double parentY,
			double parentWidth, double parentHeight, double x, double y,
			double width, double height, AffineTransform... atArr) {
		if (this.delayMillis <= 0) {	
			if (this.rotationAlpha < 1.0) {
				this.rotationAlpha = (this.rotationAlpha + this.rotationAlphaStep) > 1.0 
					? 1.0 : this.rotationAlpha + this.rotationAlphaStep;
				this.intermediateTheta = this.rotationAlpha * this.finalTheta;
			}
			java.util.stream.Stream.of(atArr)
				.forEach(
					e -> {
						this.consumer.accept(
							e,
							x,
							y,
							width / 2,
							height / 2,
							parentX - parentWidth / 2,
							parentY - parentHeight / 2
						);
						e.rotate(this.intermediateTheta);
						e.translate(-(width / 2), -(height / 2));
			});
		} else {
			this.delayMillis -= this.frameRate;
			java.util.stream.Stream.of(atArr)
				.forEach(
					e -> {
						this.consumer.accept(
							e,
							x,
							y,
							width / 2,
							height / 2,
							parentX - parentWidth / 2,
							parentY - parentHeight / 2
						);
						e.translate(-(width / 2), -(height / 2));
			});
		}
	}
}

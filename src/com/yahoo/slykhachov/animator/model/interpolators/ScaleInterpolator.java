package com.yahoo.slykhachov.animator.model.interpolators;

import java.awt.geom.AffineTransform;
import com.yahoo.slykhachov.animator.util.function.SextConsumer;
//import static com.yahoo.slykhachov.animator.model.AnimationModel.*;

public class ScaleInterpolator implements SpacialInterpolator {
	private double intermediateScaleFactor;
	private double scaleAlpha;
	private double scaleAlphaStep;
	private double delta;
	private long delayMillis;
	private int frameRate;
	private SextConsumer<AffineTransform> consumer;
	public ScaleInterpolator(double scaleFactor, long durationMillis, long delayMillis,
			int frameRate, SextConsumer<AffineTransform> consumer) {
		this.delayMillis = delayMillis;
		this.frameRate = frameRate;
		this.scaleAlphaStep = 1.0 / ((double) durationMillis / this.frameRate);//FRAME_RATE);
		this.intermediateScaleFactor = 1.0;
		this.scaleAlpha = 0.0;
		this.delta = scaleFactor - 1.0;
		this.consumer = consumer;
	}
	@Override
	public void interpolate(double parentX, double parentY,
			double parentWidth, double parentHeight, double x, double y,
			double width, double height, AffineTransform... atArr) {
		if (this.delayMillis <= 0) {
			if (this.scaleAlpha < 1.0) {
				this.scaleAlpha = (this.scaleAlpha + this.scaleAlphaStep) > 1.0 ? 1.0 : this.scaleAlpha + this.scaleAlphaStep;
				this.intermediateScaleFactor = (this.scaleAlpha * this.delta) + 1.0;
			}
			java.util.stream.Stream.of(atArr)
				.forEach(
					e -> this.consumer.accept(
						e,
						intermediateScaleFactor,
						width / 2,
						height / 2,
						(x + (parentWidth / 2)) - parentX,
						(y + (parentHeight / 2)) - parentY
				)
			);
		} else {
			this.delayMillis -= this.frameRate;//FRAME_RATE;
			java.util.stream.Stream.of(atArr)
				.forEach(
					e -> this.consumer.accept(
						e,
						1.0,
						width / 2,
						height / 2,
						(x + (parentWidth / 2)) - parentX,
						(y + (parentHeight / 2)) - parentY
				)
			);
		}
	}
}

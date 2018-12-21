package com.yahoo.slykhachov.animator.model.interpolators;

import java.awt.geom.AffineTransform;
import com.yahoo.slykhachov.animator.util.function.QuadConsumer;
//import static com.yahoo.slykhachov.animator.model.AnimationModel.*;

public class TranslationInterpolator implements SpacialInterpolator {
	private double finalTranslation;
	private double intermediateTranslation;
	private double translationAlpha;
	private double translationAlphaStep;
	private long delayMillis;
	private int frameRate;
	private QuadConsumer<AffineTransform> consumer;
	public TranslationInterpolator(double pixels, long durationMillis, long delayMillis,
			int frameRate, QuadConsumer<AffineTransform> consumer) {
		this.frameRate = frameRate;
		this.delayMillis = delayMillis;
		this.finalTranslation = pixels;
		this.translationAlphaStep = 1.0 / ((double) durationMillis / this.frameRate);//FRAME_RATE);
		this.intermediateTranslation = 0.0;
		this.translationAlpha = 0.0;
		this.consumer = consumer;
	}
	@Override
	public void interpolate(double parentX, double parentY,
			double parentWidth, double parentHeight, double x, double y,
			double width, double height, AffineTransform... atArr) {
		if (this.delayMillis <= 0) {
			if (this.translationAlpha <= 1.0) {
				this.translationAlpha = (this.translationAlpha + this.translationAlphaStep) > 1.0 ? 1.0 : this.translationAlpha + this.translationAlphaStep;
				this.intermediateTranslation = this.translationAlpha * this.finalTranslation;
			}
			java.util.stream.Stream.of(atArr)
				.forEach(
					e -> this.consumer.accept(
						e,
						intermediateTranslation,
						((x - width / 2) + (parentWidth / 2)) - parentX,
						((y - height / 2) + (parentHeight / 2)) - parentY
					)
			);
		} else {
			this.delayMillis -= this.frameRate;//FRAME_RATE;
			java.util.stream.Stream.of(atArr)
				.forEach(
					e -> this.consumer.accept(
						e,
						0.0,
						((x - width / 2) + (parentWidth / 2)) - parentX,
						((y - height / 2) + (parentHeight / 2)) - parentY
					)
			);
		}
	}
}
	
package com.yahoo.slykhachov.animator.model.interpolators;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import com.yahoo.slykhachov.animator.model.AnimationElement;

public class AlphaInterpolator implements ImageInterpolator {
	private float intermediateAlpha;
	private float finalAlpha;
	private float alphaStep;
	private long durationMillis;
	private long delayMillis;
	private int frameRate;
	private Predicate<Integer> predicate;
	private BinaryOperator<Float> binaryOperator;
	public AlphaInterpolator(float alphaValue, long durationMillis, long delayMillis,
			int frameRate) {
		this.alphaStep = -2.0f;
		this.finalAlpha = alphaValue;
		this.intermediateAlpha = -1.0f;
		this.durationMillis = durationMillis;
		this.delayMillis = delayMillis;
		this.frameRate = frameRate;
	}
	@Override
	public void interpolate(AnimationElement ae) {
		if (this.delayMillis <= 0) {
			if (Float.compare(this.alphaStep, -2.0f) == 0) {
				this.intermediateAlpha = ae.getAlpha();
				float first;
				float second;
				if (Float.compare(this.intermediateAlpha, this.finalAlpha) > 0) {
					first = intermediateAlpha;
					second = finalAlpha;
					this.predicate = i -> i > 0;
					this.binaryOperator = (f1, f2) -> f1 - f2;
				} else {
					first = finalAlpha;
					second = intermediateAlpha;
					this.predicate = i -> i < 0;
					this.binaryOperator = Float::sum;//(f1, f2) -> f1 + f2;
				}
				this.alphaStep = (first - second) / ((float) this.durationMillis / this.frameRate);
			}
			if (this.predicate.test(Float.compare(this.intermediateAlpha, this.finalAlpha))) {
				this.intermediateAlpha = !this.predicate.test(
					Float.compare(
						this.binaryOperator.apply(
							this.intermediateAlpha,
							this.alphaStep
						),
						finalAlpha
					)
				) ? this.finalAlpha : this.binaryOperator.apply(this.intermediateAlpha, this.alphaStep);
				ae.setImageAlpha(this.intermediateAlpha);
				//System.out.println(this.intermediateAlpha);
			}	
		} else {
			this.delayMillis -= this.frameRate;
		}
	}
}

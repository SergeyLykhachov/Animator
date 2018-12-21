package com.yahoo.slykhachov.animator.model;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import com.yahoo.slykhachov.animator.model.interpolators.*;
import com.yahoo.slykhachov.animator.util.function.TriFunction;

public class AnimationElement extends TransformGroup {
	public static final TriFunction<BufferedImage, BufferedImage> IMAGE_RESCALER
		= AnimationElement::rescaleImage;
	private int frameRate;
	private double x;
	private double y;
	private double height;
	private double width;
	private BufferedImage image;
	private float alpha;
	private float initialAlpha;
	private List<SpacialInterpolator> cumulativeSpacialInterpolator;
	//private SortedSet<ImageInterpolator> cumulativeImageInterpolator;
	private List<ImageInterpolator> cumulativeImageInterpolator;
	private ImageSequencer imageSequencer;
	private AffineTransform scaleLessTransform;
	private AnimationElement(Builder builder) {
		this.frameRate = builder.frameRate;
		this.x = builder.x;
		this.y = builder.y;
		this.width = builder.width;
		this.height = builder.height;
		if (builder.image != null) {
			if ((Double.compare(this.width, 0.0) > 0) && (Double.compare(this.height, 0.0) > 0)) {
				this.image = AnimationElement.IMAGE_RESCALER.apply(
					builder.image,
					(int) this.width,
					(int) this.height
				);
			} else {
				if ((Double.compare(this.width, 0.0) > 0) && (Double.compare(this.height, 0.0) == 0)) {
					this.image = AnimationElement.IMAGE_RESCALER.apply(
						builder.image,
						(int) this.width,
						builder.image.getHeight()
					);
					this.height = (double) builder.image.getHeight();
				} else {
					if ((Double.compare(this.width, 0.0) == 0) && (Double.compare(this.height, 0.0) > 0)) {
						this.image = AnimationElement.IMAGE_RESCALER.apply(
							builder.image,
							builder.image.getWidth(),
							(int) this.height
						);
						this.width = (double) builder.image.getWidth();
					} else {
						this.image = builder.image;
						this.width = (double) builder.image.getWidth();
						this.height = (double) builder.image.getHeight();
					}
				}
			}
		} else {
			if ((Double.compare(this.width, 0.0) > 0) && (Double.compare(this.height, 0.0) > 0)) {
				this.image = new BufferedImage((int) this.width, (int) this.height, BufferedImage.TYPE_INT_ARGB);
				AnimationElement.setBackground(Color.LIGHT_GRAY, this.image);
			}
		}
		this.initialAlpha = builder.alpha;
		this.alpha = builder.alpha;
		this.cumulativeSpacialInterpolator = new ArrayList<>();
		//this.cumulativeImageInterpolator = new TreeSet<>(
		//	(interpolator1, interpolator2) -> {
		//		if (interpolator1 instanceof ImageSequencer) {
		//			return interpolator2 instanceof ImageSequencer ? 0 : -1;
		//		} else {
		//			return interpolator2 instanceof ImageSequencer ? 1 : 0;
		//		}
		//});
		this.cumulativeImageInterpolator = new ArrayList<>();
		if (this.width < 500 && this.height < 500) {
			super.at.translate(this.x - this.width / 2, this.y - this.height / 2);
		}
		this.scaleLessTransform = new AffineTransform();
		//this.scaleLessTransform.concatenate(super.at);
	}
	private static BufferedImage rescaleImage(BufferedImage img, int width, int height) {
	    Image i = img.getScaledInstance(
			width,
			height,
			Image.SCALE_FAST
		);
	    BufferedImage image = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = image.createGraphics();
	    g2d.drawImage(i, 0, 0, null);
	    g2d.dispose();
	    return image;
	}
	public void addAnimationSequence(String folderPath, long numOfFullPasses, int numOfFramesInLastPass) {
		ImageSequencer sequencr;
		if ((Double.compare(this.width, 0.0) <= 0) && (Double.compare(this.height, 0.0) <= 0)) {
			sequencr = new ImageSequencer(
				folderPath,
				numOfFullPasses,
				numOfFramesInLastPass,
				null,
				0,
				0
			);
			this.width = sequencr.getFrameWidth();
			this.height = sequencr.getFrameHeight();
		} else {
			sequencr = new ImageSequencer(
				folderPath,
				numOfFullPasses,
				numOfFramesInLastPass,
				IMAGE_RESCALER,
				(int) this.width,
				(int) this.height
			);
		}
		////////////////////////////////////////////////////////////////////////
		this.imageSequencer = sequencr;
		this.setImage(sequencr.getFirstFrame());
		////////////////////////////////////////////////////////////////////////
	}
	public void transformImageAlpha(float alphaValue, long durationMillis, long delayMillis) {
		this.cumulativeImageInterpolator.add(
			new AlphaInterpolator(
				alphaValue,
				durationMillis,
				delayMillis,
				this.frameRate
			)
		);
	}
	public void resetInternalTransforms() {
		super.at.setToIdentity();
		this.scaleLessTransform.setToIdentity();
	}
	private void repurposeTransforms() {
		if (this.cumulativeSpacialInterpolator.size() == 0) {
			super.at.setToIdentity();
			this.scaleLessTransform.setToIdentity();
		}
	}
	public void scaleX(double valueX, long durationMillis, long delayMillis) {
		repurposeTransforms();
		this.cumulativeSpacialInterpolator.add(
			new ScaleInterpolator(
				valueX,
				durationMillis,
				delayMillis,
				this.frameRate,
				this.cumulativeSpacialInterpolator.size() != 0
					? (transform, scale, ex, why, valX, valY) -> {
						at.translate(ex, why);
						transform.scale(scale, 1.0);
						at.translate(-ex, -why);
					} : (transform, scale, ex, why, valX, valY) -> {
							transform.translate(valX, valY);
							transform.scale(scale, 1.0);
							transform.translate(-ex, -why);
						}
			)
		);
	}
	public void scaleY(double valueY, long durationMillis, long delayMillis) {
		repurposeTransforms();
		this.cumulativeSpacialInterpolator.add(
			new ScaleInterpolator(
				valueY,
				durationMillis,
				delayMillis,
				this.frameRate,
				this.cumulativeSpacialInterpolator.size() != 0
					? (transform, scale, ex, why, valX, valY) -> {
						transform.translate(ex, why);
						transform.scale(1.0, scale);
						transform.translate(-ex, -why);
					} : (transform, scale, ex, why, valX, valY) -> {
							transform.translate(valX, valY);
							transform.scale(1.0, scale);
							transform.translate(-ex, -why);
						}
			)
		);
	}
	public void scale(double valueX, double valueY, long durationMillis, long delayMillis) {
		this.scaleX(valueX, durationMillis, delayMillis);
		this.scaleY(valueY, durationMillis, delayMillis);
	}
	public void translateX(double xPixels, long durationMillis, long delayMillis) {
		repurposeTransforms();
		this.cumulativeSpacialInterpolator.add(
			new TranslationInterpolator(
				xPixels,
				durationMillis,
				delayMillis,
				this.frameRate,
				this.cumulativeSpacialInterpolator.size() != 0
					? (transform, shift, ex, why) -> transform.translate(shift, 0)
						: (transform, shift, ex, why) -> transform.translate(ex + shift, why)
			)
		);
	}
	public void translateY(double yPixels, long durationMillis, long delayMillis) {
		repurposeTransforms();
		this.cumulativeSpacialInterpolator.add(
			new TranslationInterpolator(
				yPixels,
				durationMillis,
				delayMillis,
				this.frameRate,
				this.cumulativeSpacialInterpolator.size() != 0
					? (transform, shift, ex, why) -> transform.translate(0, shift)
						: (transform, shift, ex, why) -> transform.translate(ex, why + shift)
			)
		);
	}
	public void translate(double x, double y, long durationMillis, long delayMillis) {
		this.translateX(x, durationMillis, delayMillis);
		this.translateY(y, durationMillis, delayMillis);
	}
	public void rotate(double degrees, long durationMillis, long delayMillis) {
		repurposeTransforms();
		this.cumulativeSpacialInterpolator.add(
			new RotationInterpolator(
				degrees,
				durationMillis,
				delayMillis,
				this.frameRate,
				this.cumulativeSpacialInterpolator.size() != 0
					? (transform, ex, why, widthVal, heightVal, parentAbscissaInfo, parentOrdinateInfo)
							-> transform.translate(widthVal, heightVal)
						: (transform, ex, why, widthVal, heightVal, parentAbscissaInfo, parentOrdinateInfo)
							-> transform.translate(ex - parentAbscissaInfo, why - parentOrdinateInfo)
			)
		);
	}
	public void update() {
		for (TransformGroup tg : this.list) {
			((AnimationElement) tg).update(this.at, 0, 0, 0, 0);
		}
	}
	private void update(AffineTransform parentTransform, double parentX,
			double parentY, double parentWidth, double parentHeight) {
		///////////////////////////////////////////////////////////////
		if (this.imageSequencer != null) {
			this.imageSequencer.nextFrame(this);
		}
		this.cumulativeImageInterpolator.forEach(
			interpolator -> interpolator.interpolate(this)
		);
		///////////////////////////////////////////////////////////////	
		super.at.concatenate(parentTransform);
		this.scaleLessTransform.concatenate(parentTransform);
		if (this.cumulativeSpacialInterpolator.size() > 0) {
			this.cumulativeSpacialInterpolator.forEach(
				interpolator -> {
					if (!(interpolator instanceof ScaleInterpolator)) {
						interpolator.interpolate(
							parentX,
							parentY,
							parentWidth,
							parentHeight,
							this.x,
							this.y,
							this.width,
							this.height,
							this.scaleLessTransform,
							super.at
						);
					} else {
						interpolator.interpolate(
							parentX,
							parentY,
							parentWidth,
							parentHeight,
							this.x,
							this.y,
							this.width,
							this.height,
							super.at
						);
						if (this.cumulativeSpacialInterpolator.size() == 1) {
							this.scaleLessTransform.translate(
								((x - width / 2) + (parentWidth / 2)) - parentX,
								((y - height / 2) + (parentHeight / 2)) - parentY
							);
						} else {
							if (this.cumulativeSpacialInterpolator.get(0) instanceof ScaleInterpolator
									&& this.cumulativeSpacialInterpolator.get(1) instanceof ScaleInterpolator) {
								int index = this.cumulativeSpacialInterpolator.indexOf(interpolator);
								if (index == 0) {
									this.scaleLessTransform.translate(
										((x - width / 2) + (parentWidth / 2)) - parentX,
										((y - height / 2) + (parentHeight / 2)) - parentY
									);
								}
							}
						}
					}
			});
		} else {
			super.at.translate(
				(this.x - this.width / 2) + (parentWidth / 2),
				(this.y - this.height / 2) + (parentHeight / 2)
			);
			this.scaleLessTransform.translate(
				(this.x - this.width / 2) + (parentWidth / 2),
				(this.y - this.height / 2) + (parentHeight / 2)
			);
		}
		for (TransformGroup tg : this.list) {
			((AnimationElement) tg).update(
				this.scaleLessTransform,
				this.x,
				this.y,
				this.width,
				this.height
			);
		}
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWidth() {
		return this.width;
	}
	public BufferedImage getImage() {
		//return this.image;
		if (this.image == null) {
			return null;
		}
		BufferedImage img = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    g2d.setComposite(
			AlphaComposite.SrcOver.derive(
				this.alpha
			)
		);
	    g2d.drawImage(this.image, 0, 0, null);
	    g2d.dispose();
	    return img;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public void setImageAlpha(float alpha) {
		this.alpha = alpha;
	}
	public float getAlpha() {
		return this.alpha;
	}
	public float getInitialImageAlpha() {
		return this.initialAlpha;
	}
	public List<SpacialInterpolator> getSpacialInterpolators() {
		return this.cumulativeSpacialInterpolator;
	}
	//public SortedSet<ImageInterpolator> getImageInterpolators() {
	public List<ImageInterpolator> getImageInterpolators() {
		return this.cumulativeImageInterpolator;
	}
	public void setBackground(Color color) {
		setBackground(color, this.image);
	}
	private static void setBackground(Color color, BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		for (int i = 0; i < w; i++) {
		    for (int j = 0; j < h; j++) {
		        image.setRGB(i, j, color.getRGB());
		    }
		}
	}
	public static class Builder {
		private double x = 0;
		private double y = 0;
		private double height = 0;
		private double width = 0;
		private BufferedImage image = null;
		private float alpha = 1.0f;
		private int frameRate;
		public Builder(double x, double y, int frameRate) {
			this.x = x;
			this.y = y;
			this.frameRate = frameRate;
		}
		public Builder height(double val) {
			this.height = val;
			return this;
		}
		public Builder width(double val) {
			this.width = val;
			return this;
		}
		public Builder alpha(float alpha) {
			this.alpha = alpha;
			return this;
		}
		public Builder image(String path) {
			try {
				this.image = ImageIO.read(new File(path));
			} catch (java.io.IOException e) {
				this.image = new BufferedImage((int) this.width, (int) this.height, BufferedImage.TYPE_INT_ARGB);
				AnimationElement.setBackground(Color.RED, this.image);
			}
			return this;
		}
		public Builder setBackground(Color color) {
			if (this.image != null) {
				AnimationElement.setBackground(color, this.image);
			} else {
				this.image = new BufferedImage((int) this.width, (int) this.height, BufferedImage.TYPE_INT_ARGB);
				AnimationElement.setBackground(color, this.image);
			}
			return this;
		}
		public AnimationElement build() {
			return new AnimationElement(this);
		}
	}
}

package com.yahoo.slykhachov.animator.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
//import com.yahoo.slykhachov.animator.model.AnimationElement;
import com.yahoo.slykhachov.animator.util.function.TriFunction;

public class ImageSequencer {
	private List<BufferedImage> images;
	private long numOfFullPasses;
	private int numOfFramesInLastPass;
	private int curIndex;
	public ImageSequencer(String folderPath, long numOfFullPasses,
			int numOfFramesInLastPass,
			TriFunction<? super BufferedImage, ? extends BufferedImage> func,
			int width, int height) {
		File f = new File(folderPath);
		File[] list = f.listFiles();
		this.images = Stream.of(list)
			.filter(ImageSequencer::isImageType)
			.filter(ImageSequencer::isNumbered)
			.sorted(ImageSequencer.ascendingOrder())
			.collect(ImageSequencer.toListOfImages(width, height, func));
		this.numOfFullPasses = numOfFullPasses;
		this.numOfFramesInLastPass = numOfFramesInLastPass;
		this.curIndex = 0;
	}
	public void nextFrame(AnimationElement e) {
		if (this.numOfFullPasses != 0) {
			e.setImage(this.images.get(curIndex));
			if (curIndex == this.images.size() - 1) {
				this.curIndex = 0;
				this.numOfFullPasses--;
			} else {
				this.curIndex += 1;
			}
		} else {
			if (this.curIndex < this.numOfFramesInLastPass) {
				e.setImage(this.images.get(this.curIndex));
				this.curIndex += 1;
			}
		}
	}
	private static boolean isImageType(File f) {
		String[] arr = f.getAbsolutePath().split("\\.");
		return "jpg".equals(arr[arr.length - 1]) || "png".equals(arr[arr.length - 1]);
	}
	private static boolean isNumbered(File f) {
		String[] arr = f.getAbsolutePath().split("\\.");
		String s = arr[arr.length - 2];
		return Character.isDigit(s.charAt(s.length() - 1));
	}
	private static java.util.Comparator<File> ascendingOrder() {
		return (file1, file2) -> {
			String[] arr1 = file1.getAbsolutePath().split("\\.");
			String[] arr2 = file2.getAbsolutePath().split("\\.");
			int i1 = Integer.MAX_VALUE;
			int i2 = Integer.MAX_VALUE;
			String s = arr1[arr1.length - 2];
			try { 
				i1 = Integer.parseInt(s.substring(getNumStartIndex(s)));
			} catch (NumberFormatException | NullPointerException e) {
				e.printStackTrace();
			}
			s = arr2[arr2.length - 2];
			try {
				i2 = Integer.parseInt(s.substring(getNumStartIndex(s)));
			} catch (NumberFormatException | NullPointerException e) {
				e.printStackTrace();
			}
			return Integer.compare(i1, i2);
		};
	}
	private static int getNumStartIndex(String s) throws NullPointerException {
		if (!Character.isDigit(s.charAt(s.length() - 1))) {
			throw new NullPointerException();
		} else {
			int index = s.length() - 1;
			while ((index >= 0) && Character.isDigit(s.charAt(index - 1))) {
				index -= 1;
			}
			return index;
		}
	}
	private static Collector<File, ?, List<BufferedImage>> toListOfImages(int width, int height,
			TriFunction<? super BufferedImage, ? extends BufferedImage> func) {
		BiConsumer<List<File>, File> accumulator = (list, entry) -> list.add(entry);
		BinaryOperator<List<File>> combiner = (left, right) -> {
			for (File entry : right) {
				left.add(entry);
			}
			return left;
		};
		Function<List<File>, List<BufferedImage>> finisher = fileList -> {
			return fileList.stream()
				    	   .map(file -> ImageSequencer.readImageFromFile(file, func, width, height))
				    	   .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
		};
		return Collector.of(
			ArrayList::new,
			accumulator, 
			combiner,
			finisher
		);
	}
	private static BufferedImage readImageFromFile(File file,
			TriFunction<? super BufferedImage, ? extends BufferedImage> func,
			int width, int height) {
		BufferedImage image = null;
		try {
		    image = ImageIO.read(file);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return func != null ? func.apply(image, width, height) : image;
	}
	public long getNumOfFullPasses() {
		return this.numOfFullPasses;
	}
	public void setNumOfFullPasses(long numOfFullPasses) {
		this.numOfFullPasses = numOfFullPasses;
	}
	public int getFrameWidth() {
		return this.images.get(0)
		                  .getWidth();
	}
	public int getFrameHeight() {
		return this.images.get(0)
		                  .getHeight();
	}
	public BufferedImage getFirstFrame() {
		return this.images.get(0);
	}
}

package com.yahoo.slykhachov.animator.view;

//import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import com.yahoo.slykhachov.animator.model.AnimationElement;
import com.yahoo.slykhachov.animator.model.TransformGroup;
import com.yahoo.slykhachov.animator.model.AnimationModel;
import com.yahoo.slykhachov.animator.util.*;

public class AnimationView extends JPanel implements Observer {
	private AnimationModel animationModel;
	public AnimationView(Dimension dimension, AnimationModel animationModel) {
		super.setPreferredSize(dimension);
		this.animationModel = animationModel;
		this.setBorder(
			new CompoundBorder(
				new BevelBorder(BevelBorder.RAISED),
				new EtchedBorder()
			)
		);
	}
	@Override
	public void update() {
		this.repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		Queue<TransformGroup> childrenQueue = new MyQueue<>(
			this.animationModel.getRootAnimationElement()
				.getChildren()
		);
		g2d.drawRenderedImage(
			this.animationModel.getRootAnimationElement()
				.getImage(),
			this.animationModel.getRootAnimationElement()
				.getTransform()
		);
		while (childrenQueue.size() != 0) {
			AnimationElement e = (AnimationElement) childrenQueue.poll();
			childrenQueue.addAll(e.getChildren());
			BufferedImage img = e.getImage();
			if (img != null) {
				g2d.drawRenderedImage(img, e.getTransform());
			}
			e.resetInternalTransforms();
		}
		
		java.awt.Shape shape1 = new Ellipse2D.Double(0 - 5, 0 - 5, 10, 10);
        	java.awt.Shape shape2 = new Ellipse2D.Double(200 - 5, 100 - 5, 10, 10);
		
		java.awt.Shape shape3 = new Ellipse2D.Double(220 - 5, 330 - 5, 10, 10);
		java.awt.Shape shape4 = new Ellipse2D.Double(620 - 5, 530 - 5, 10, 10);
		
		java.awt.Shape shape5 = new Ellipse2D.Double(400 - 5, 300 - 5, 10, 10);
		java.awt.Shape shape6 = new Ellipse2D.Double(800 - 5, 500 - 5, 10, 10);
		
		java.awt.Shape shape7 = new Ellipse2D.Double(70 - 5, 80 - 5, 10, 10);
		java.awt.Shape shape8 = new Ellipse2D.Double(470 - 5, 280 - 5, 10, 10);
					
		g2d.setTransform(new AffineTransform());
		g2d.setColor(Color.BLACK);
		g2d.fill(shape1);
		g2d.fill(shape2);
		g2d.setColor(Color.GREEN);
		g2d.fill(shape3);
		g2d.fill(shape4);
		g2d.setColor(Color.BLUE);
		g2d.fill(shape5);
		g2d.fill(shape6);
		g2d.setColor(Color.RED);
		g2d.fill(shape7);
		g2d.fill(shape8);
		
		g2d.dispose();
	}
}

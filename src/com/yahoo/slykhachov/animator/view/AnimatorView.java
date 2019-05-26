package com.yahoo.slykhachov.animator.view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import com.yahoo.slykhachov.animator.Animation;
import com.yahoo.slykhachov.animator.model.AnimationModel;

public class AnimatorView extends JFrame {
	private AnimationView animationView;
	public AnimatorView(Animation animation) {
		super("Animator");
		this.setAnimationView(animation.getAnimationView());
		JPanel rootPanel = createRootPanel(this.animationView);
		super.getContentPane().add(rootPanel, BorderLayout.CENTER);
		super.getContentPane().add(createToolBar(animation.getAnimationModel()), BorderLayout.NORTH);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(true);
	}
	static JPanel createRootPanel(AnimationView animationView) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
    		c.weightx = 1.0;//0.66;
    		c.weighty = 1.0;//0.66;
    		c.gridx = 0;
    		c.gridy = 0;
		panel.add(
			animationView,
			c
		);
		return panel;
	}
	private JToolBar createToolBar(AnimationModel animationModel) {
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.setLayout(new FlowLayout());
		jtb.setBackground(Color.GRAY);
		JButton playButton = new JButton("REPLAY");
		JButton pauseButton = new JButton("PAUSE");
		playButton.addActionListener(
			ae -> {
				AnimationModel.flushAndReplenishInterpolatorLists(
					animationModel.getAeList(),
					animationModel.getRunnable()
				);
				animationModel.getAeList()
					.forEach(e -> e.setImageAlpha(e.getInitialImageAlpha()));
				if (animationModel.isOnPause()) {
					animationModel.getTimer().start();
					animationModel.setPause(false);
				}
		});
		pauseButton.addActionListener(
			ae -> {
				if (animationModel.isOnPause()) {
					animationModel.getTimer().start();
					animationModel.setPause(false);
				} else {
					animationModel.getTimer().stop();
					animationModel.setPause(true);
				}
		});
		jtb.add(playButton);
		jtb.add(pauseButton);
		return jtb;
	}
	public void setAnimationView(AnimationView animationView) {
		this.animationView = animationView;
	}
}

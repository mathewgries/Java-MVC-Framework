package com.freedompay.views;
import com.freedompay.controllers.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

// TODO: See if removing the component panels, and just building them out here saves you on layout issues
// I.E. Just create the panels you need on the view, not from importing
public class HomeView extends View {

	private static final long serialVersionUID = -2450001680426173348L;
	
	private Controller controller;

	private JPanel jumbotron;
	
	public void addController(Controller c) {
		controller = c;
	}
	
	public void build() {
		JPanel leftSpace = new JPanel();
		JPanel rightSpace = new JPanel();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.setBorder(bevel);
		
		this.buildLeftDivider();
		
		leftSpace.setAlignmentX(Component.CENTER_ALIGNMENT);
		jumbotron.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightSpace.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(leftSpace);
		this.add(jumbotron);
		this.add(rightSpace);
	}
	
	private void buildLeftDivider() {
		this.jumbotron = new JPanel();
		this.jumbotron.setBackground(Color.WHITE);
		Border bevel = BorderFactory.createLoweredBevelBorder();
		this.jumbotron.setBorder(bevel);
		
		JTextArea t = this.controller.getTextArea(this.controller.getInstructions());
		t.setLineWrap(false);
		t.setWrapStyleWord(false);
		this.jumbotron.add(t);
	}
}

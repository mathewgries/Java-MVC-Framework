package com.freedompay.controllers;
import com.freedompay.models.Model;
import com.freedompay.views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FileController extends Controller implements ActionListener{

	private View view;
	private Model model;
	
	@Override
	public void addView(View v) {
		view = v;
	}
	
	public void addModel(Model m) {
		model = m;
	}
	
	public JLabel getJLabel(String txt) {
		return new JLabel(txt);
	}
	
	public JButton getButton(String txt) {
		JButton btn = new JButton(txt);
		btn.addActionListener(this);
		return btn;
	}
	
	public JTextField getTextField(int length) {
		JTextField tf = new JTextField(length);
		return tf;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if(btn.getText() == "Browse") {
			this.openFileChooser();
		}
	}
	
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser("c:\\");
		int choice = fc.showDialog(this.view, "Open");
		if(choice == JFileChooser.APPROVE_OPTION) {
			
		}
	}
}

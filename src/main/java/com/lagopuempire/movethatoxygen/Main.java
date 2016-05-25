package com.lagopuempire.movethatoxygen;

import javax.swing.JFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		new Window();
	}
}

@SuppressWarnings("serial")
class Window extends JFrame {
	public Window() {
		this.add(Environment.getInstance());
		this.setTitle("ChemGame");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(900,900);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}
}

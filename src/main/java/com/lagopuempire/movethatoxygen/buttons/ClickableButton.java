package com.lagopuempire.movethatoxygen.buttons;

import com.lagopuempire.movethatoxygen.Drawable;
import com.lagopuempire.movethatoxygen.Environment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ClickableButton extends MouseAdapter implements Drawable {
	
	public ClickableButton(int x, int y, ButtonPressEvent buttonPressEvent) {
		this.x = x;
		this.y = y;
		this.event = buttonPressEvent;
		Environment.getInstance().addMouseListener(this);
	}

	protected int x;
	protected int y;
	private final ButtonPressEvent event;
	private boolean listenForClicks = true;
	protected boolean isClicked = false;
	
	public boolean containsPoint(int x, int y) {
		return (x > getLeftFace() && x < getRightFace() && y < getBottomFace() && y > getTopFace());
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public int getLeftFace() {
		return x;
	}
	
	public int getRightFace() {
		return (x + getWidth());
	}
	
	public int getTopFace() {
		return y;
	}
	
	public int getBottomFace() {
		return (y + getHeight());
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void unregisterListener() {
		Environment.getInstance().removeMouseListener(this);
	}

	public boolean isListenForClicks() {
		return listenForClicks;
	}

	public void setListenForClicks(boolean listenForClicks) {
		this.listenForClicks = listenForClicks;
		if(listenForClicks == false) {
			Environment.getInstance().removeMouseListener(this);
			//Environment.getInstance().getButtons().remove(this);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
//		System.out.println("coords: " + e.getX() + ", " + e.getY());
		if(this.listenForClicks) {
			if(this.containsPoint(e.getX(), e.getY())) {
//				System.out.println("click!");
				event.buttonPressed();
				this.isClicked = true;
				
				System.out.println("pressing button");
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(this.isClicked) {
//			System.out.println("unclick!");
			this.isClicked = false;
			event.buttonReleased();
		}
	}
	
}


package com.lagopusempire.movethatoxygen.buttons;

import java.awt.Color;
import java.awt.Graphics2D;

public class RectangleButton extends ClickableButton {

    public RectangleButton(int x1, int y1, int x2, int y2, Color color, int brightness, Color textColor, String text, int textX, int textY, ButtonPressEvent buttonPressEvent) {
        super(x1, y1, buttonPressEvent);
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.textColor = textColor;
        this.textX = textX;
        this.textY = textY;
        this.text = text;

        try {//TODO: do this right
            selectedColor = new Color(color.getRed() + brightness, color.getGreen() + brightness, color.getBlue() + brightness);
        } catch (IllegalArgumentException e) {
            selectedColor = color;
        }
    }

    private Color color, textColor, selectedColor;
    private String text;
    private int textX, textY;
    private final int x2, y2;
//	private boolean brightened = true;
//	private int brightenedTicks = brigthenedTicksConstant;
//	
//	private static final int brigthenedTicksConstant = 40;

    @Override
    public int getWidth() {
        return x2 - x;
    }

    @Override
    public int getHeight() {
        return y2 - y;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (color != null) {
            if (isClicked) {
//				brightened = true;
                g2d.setColor(selectedColor);
            } //			if(brightened) {
            //				g2d.setColor(selectedColor);
            //				brightenedTicks--;
            //				if(brightenedTicks == 0) {
            //					brightenedTicks = brigthenedTicksConstant;
            //					brightened = false;
            //				}
            //			} 
            else {
                g2d.setColor(color);
            }
        }

        //g2d.drawRect(x, y, getWidth(), getHeight());
        g2d.fillRect(x, y, getWidth(), getHeight());

        if (textColor != null) {
            g2d.setColor(textColor);
            //g2d.setColor(new Color)
        }

        g2d.drawString(text, textX, textY);
    }

    public int getTextX() {
        return textX;
    }

    public void setTextX(int textX) {
        this.textX = textX;
    }

    public int getTextY() {
        return textY;
    }

    public void setTextY(int textY) {
        this.textY = textY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

}

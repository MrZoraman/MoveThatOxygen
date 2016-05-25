package com.lagopusempire.movethatoxygen.buttons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BufferedImageButton extends ClickableButton {

    public BufferedImageButton(int x, int y, BufferedImage image, ButtonPressEvent buttonPressEvent) {
        super(x, y, buttonPressEvent);
        this.image = image;
        this.pressedImage = image;
    }

    public BufferedImageButton(int x, int y, BufferedImage image, BufferedImage pressedImage, ButtonPressEvent buttonPressEvent) {
        super(x, y, buttonPressEvent);
        this.image = image;
        this.pressedImage = pressedImage;
    }

    private boolean show = true;

    private final BufferedImage image;
    private final BufferedImage pressedImage;

    @Override
    public void draw(Graphics2D g2d) {
        if (show) {
            if (isClicked) {
                g2d.drawImage(pressedImage, x, y, null);
            } else {
                g2d.drawImage(image, x, y, null);
            }
        }
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

}

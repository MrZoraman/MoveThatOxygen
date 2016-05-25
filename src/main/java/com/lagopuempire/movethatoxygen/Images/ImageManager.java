package com.lagopuempire.movethatoxygen.Images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageManager {
	private ImageManager() {
		images = new HashMap<String, BufferedImage>();
	}
	
	private static ImageManager instance = null;
	
	private static ImageManager getInstance() {
		if(instance == null)
			instance = new ImageManager();
		return instance;
	}
	
	private Map<String, BufferedImage> images;
	
	private BufferedImage getImageInternal(String imageName) {
		if(images.containsKey(imageName)) {
			return images.get(imageName);
		} else {
			BufferedImage image = null;;
			try {
				image = ImageIO.read(getClass().getResource(imageName + ".png"));
			} catch (IOException e) {
				System.out.println("Image not found: " + imageName);
			} finally {
				images.put(imageName, image);
			}
			
			return image;
		}
	}
	
	public static BufferedImage getImage(String imageName) {
		return getInstance().getImageInternal(imageName);
	}
}
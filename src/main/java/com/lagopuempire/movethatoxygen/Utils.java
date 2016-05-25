package com.lagopuempire.movethatoxygen;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.coobird.thumbnailator.Thumbnails;

public class Utils {
	private Utils() { }
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException {
		  return Thumbnails.of(img).size(newW, newH).asBufferedImage();
	}
	
	public static double distance(int x1, int y1, int x2, int y2) {
		
		return Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
	}
	

	public static Set<Molecule> generateAmbientMolecules() throws IOException {
		Random random = new Random();
		Set<Molecule> molecules = new HashSet<Molecule>();
		for(int ii = 0; ii < 3; ii++) {
			StandardMolecule molecule = new StandardMolecule(Atom.HYDROGEN, random.nextInt(900), random.nextInt(900));
	//		molecule.setSpeed(0.1);
			molecule.setVelocityX(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setVelocityY(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setRotationRate(0.03 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setOpenConnections(0);
			molecule.setShowDarkened(true);
	//		molecule.setShowBlured(true);
			molecules.add(molecule);
		}
		for(int ii = 0; ii < 3; ii++) {
			StandardMolecule molecule = new StandardMolecule(Atom.OXYGEN, random.nextInt(900), random.nextInt(900));
	//		molecule.setSpeed(0.1);
			molecule.setVelocityX(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setVelocityY(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setOpenConnections(0);
			molecule.setShowDarkened(true);
	//		molecule.setShowBlured(true);
			molecule.setRotationRate(0.03 * (random.nextInt(2) == 0 ? 1 : -1));
			molecules.add(molecule);
		}
		for(int ii = 0; ii < 3; ii++) {
			StandardMolecule molecule = new StandardMolecule(Atom.CARBON, random.nextInt(900), random.nextInt(900));
	//		molecule.setSpeed(0.1);
			molecule.setVelocityX(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setVelocityY(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setRotationRate(0.03 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setOpenConnections(0);
			molecule.setShowDarkened(true);
	//		molecule.setShowBlured(true);
			molecules.add(molecule);
		}
		for(int ii = 0; ii < 3; ii++) {
			StandardMolecule molecule = new StandardMolecule(Atom.NITROGEN, random.nextInt(900), random.nextInt(900));
	//		molecule.setSpeed(0.1);
			molecule.setVelocityX(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setVelocityY(1 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setRotationRate(0.03 * (random.nextInt(2) == 0 ? 1 : -1));
			molecule.setOpenConnections(0);
			molecule.setShowDarkened(true);
	//		molecule.setShowBlured(true);
			molecules.add(molecule);
		}
		
		return molecules;
	}
}

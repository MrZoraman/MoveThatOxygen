package com.lagopusempire.movethatoxygen;

import java.io.IOException;
import java.util.Random;

public class StandardMolecule extends Molecule
{

	public StandardMolecule(Atom atom, int x, int y) throws IOException {
		super(atom, x, y);
		random = new Random();
		
		this.setSpeed(random.nextInt(10) + 1);
		
		velocityX = getSpeed() * (random.nextInt(2) == 0 ? 1 : -1);
		velocityY = getSpeed() * (random.nextInt(2) == 0 ? 1 : -1);
//		System.out.println("velocityX: " + velocityX + ", velocityY: " + velocityY);
		
		setRotationRate((random.nextDouble() + (random.nextInt(2) == 0 ? 1 : 0)) / (random.nextInt(2) == 0 ? 10 : -10));
//		setRotationRate(Math.PI / 4);
//		System.out.println("Rotation rate: " + getRotationRate());
	}
	
	public static StandardMolecule fromMolecule(Molecule molecule) {
		if(molecule instanceof StandardMolecule) {
			return (StandardMolecule) molecule;
		} else {
			try {
				StandardMolecule sm = new StandardMolecule(molecule.getAtom(), molecule.getX(), molecule.getY());
				sm.getAttatchedMolecules().addAll(molecule.getAttatchedMolecules());
				sm.setRotation(molecule.getRotation());
//				sm.setRotationRate(sm.getRotationRate());
				return sm;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private final Random random;
	
	private int velocityX, velocityY = 0;
	
	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}

	@Override
	public void topBorderCollision() {
		velocityY = -velocityY;
	}

	@Override
	public void bottomBorderCollision() { 
		velocityY = -velocityY;
	}

	@Override
	public void leftBorderCollision() { 
		velocityX = -velocityX;
	}

	@Override
	public void rightBorderCollision() { 
		velocityX = -velocityX;
	}
	
	@Override
	public void moveLocation(CollisionType type) {
		//System.out.println("moving...");
		this.setX(this.getX() + velocityX);
		this.setY(this.getY() + velocityY);
	}
	
}

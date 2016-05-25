package com.lagopusempire.movethatoxygen;

import com.lagopusempire.movethatoxygen.Images.ImageManager;
import com.lagopusempire.movethatoxygen.levels.LevelManager;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

public abstract class Molecule implements Drawable
{
	public Molecule(Atom atom, int x, int y) throws IOException {
		this.x = x;
		this.y = y;
		this.atom = atom;
		
		switch(atom) {
		case OXYGEN:
			//img = ImageIO.read(new File("/home/matt/Pictures/Oxygen.png"));\
			img = ImageIO.read(getClass().getResource("Images/Oxygen.png"));
			
			//img = (BufferedImage) img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			openConnections = 2;
			isDiatomic = true;
			break;
		case HYDROGEN:
			if(this instanceof ControllableMolecule) {
				//img = ImageIO.read(getClass().getResource("Images/PlayerHydrogen.png"));
				img = ImageManager.getImage("PlayerHydrogen");
			} else {
				img = ImageManager.getImage("Hydrogen");
			}
			img = Utils.resize(img, 64, 64);
			openConnections = 1;
			isDiatomic = true;
			break;
		case CARBON:
			img = ImageManager.getImage("Carbon");
			openConnections = 4;
			isDiatomic = false;
			break;
		case NITROGEN:
			img = ImageManager.getImage("Nitrogen");
			openConnections = 4;
			isDiatomic = true;
			break;
		default:
			img = ImageManager.getImage("Hydrogen");
			openConnections = 0;
			isDiatomic = false;
			//TODO: will fix
			break;
		}
		
		attatchedMolecules = new HashSet<>();
		adjacentMolecules = new HashSet<>();
		
		
		//get blurred image
		float ninth = 1.0f/9.0f;
		float[] blurKernel = {
				ninth, ninth, ninth,
				ninth, ninth, ninth,
				ninth, ninth, ninth
		};
		float num = 1.0f/20.0f;
		float[] darkKernel = {
				num, num, num,
				num, num, num,
				num, num, num
		};
		
		Map<Key, Object> map = new HashMap<>();
		map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
		blur = op.filter(img, null);
		op = new ConvolveOp(new Kernel(3, 3, darkKernel), ConvolveOp.EDGE_NO_OP, hints);
		dark = op.filter(img, null);
		
		sig = new MoleculeSignature();
		sig.addAtom(atom);
	}
	
	private int x, y;
	private final Atom atom;
	private int speed = 6;
	private BufferedImage img, blur, dark = null;
	private String name = "not named";
	private final Set<Molecule> attatchedMolecules, adjacentMolecules;
	private int xOffset, yOffset;
	private boolean independentMovement = true;
	private Molecule controlMolecule = null;
	private boolean limitedByBorders = true;
	private double rotationRate, rotation = 0;
	private int openConnections = 0;
	private final boolean isDiatomic;
	private boolean hasBonded = false;
	private boolean showBlured = false;
	private boolean showDarkened = false;
	
	private final MoleculeSignature sig;
	
	public void attatchMolecule(Molecule molecule) {
		if(openConnections != 0 || molecule.hasBonded) {
			if(controlMolecule != null) {
//				System.out.println("attatching " + molecule.getName() + " from up here");
				
				if(atom.equals(molecule.atom) && isDiatomic && attatchedMolecules.isEmpty()) {
					setOpenConnections(0);
					molecule.setOpenConnections(0);
				} else {
					setOpenConnections(getOpenConnections() - 1);
					molecule.setOpenConnections(molecule.getOpenConnections() - 1);
				}
				molecule.hasBonded = true;
				
				controlMolecule.attatchMolecule(molecule);
			} else {
				molecule.setIndependentMovement(false);
				molecule.limitedByBorders = false;
				molecule.controlMolecule = this;
				molecule.setXOffset(molecule.getX() - x);
				molecule.setYOffset(molecule.getY() - y);
				System.out.println("molecule (" + molecule.getName() + ") attatched to: " + getName());
				
				if(!molecule.hasBonded) {
					if(atom.equals(molecule.atom) && isDiatomic && attatchedMolecules.isEmpty()) {
						setOpenConnections(0);
						molecule.setOpenConnections(0);
					} else {
						setOpenConnections(getOpenConnections() - 1);
						molecule.setOpenConnections(molecule.getOpenConnections() - 1);
					}
				}

				attatchedMolecules.add(molecule);
				sig.addAtom(molecule.getAtom());
				System.out.println("Adding atom to sig: " + molecule.getAtom());
				molecule.hasBonded = false;

				if(this instanceof ControllableMolecule) {
					LevelManager.getInstance().getCurrentLevel().playerCollision((ControllableMolecule) this, molecule);
				}


			}
		} 
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		if(limitedByBorders) {
			if(x < 0)
				leftBorderCollision();
			else if (x + getWidth() > Environment.getInstance().getWidth())
				rightBorderCollision();
			else
				this.x = x;
		} else {
			this.x = x;
		}
	}
	
	public void  setY(int y) {
		if(limitedByBorders) {
			if(y < 0)
				topBorderCollision();
			else if (y + getHeight() > Environment.getInstance().getHeight())
				bottomBorderCollision();
			else
				this.y = y;
		} else {
			this.y = y;
		}
	}
	
	public Set<Molecule> getAttatchedMolecules() {
		return attatchedMolecules;
	}
	
	public void topBorderCollision() {
		//System.out.println("top border collision"); 
	}
	
	public void bottomBorderCollision() { 
		//System.out.println("bottom border collision");
	}
	
	public void leftBorderCollision() { 
		//System.out.println("left border collision");
	}
	
	public void rightBorderCollision() { 
		//System.out.println("right border collision");
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public Atom getAtom() {
		return atom;
	}
	
	public int getWidth() {
		return img.getWidth();
	}
	
	public int getHeight() {
		return img.getHeight();
	}
	
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
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	private void setXOffset(int xOffset) {
		this.xOffset = xOffset;
	}
	
	private void setYOffset(int yOffset) {
		this.yOffset = yOffset;
	}
	
	public boolean getIndependentMovement() {
		return independentMovement;
	}
	
	public double getRotationRate() {
		return rotationRate;
	}
	
	public void setRotationRate(double rotationRate)
	{
		this.rotationRate = rotationRate;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public boolean isDiatomic() {
		return isDiatomic;
	}
	
	public void setRotation(double rotation) {
		if(rotation > (Math.PI * 2)) {
			this.rotation = (rotation - (Math.PI * 2));
		} else if (rotation < (Math.PI * -2)) {
			this.rotation = (rotation + (Math.PI * 2));
		} else {
			this.rotation = rotation;
		}
	}
	
	public void setIndependentMovement(boolean independentMovement) {
//		System.out.println("Independent movement for " + getName() + " set to " + independentMovement);
		this.independentMovement = independentMovement;
	}
	
	public boolean isLimitedByBorders() {
		return limitedByBorders;
	}
	
	@Override
	public void draw(Graphics2D g2d)
	{
		double locX = img.getWidth() / 2;
		double locY = img.getWidth() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotation, locX, locY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage pic = img;
		if(this.isShowDarkened()) {
			g2d.drawImage(op.filter(dark,  null), getX(), getY(), null);
		} else {
			g2d.drawImage(op.filter(showBlured ? blur : pic, null), getX(), getY(), null);
		}
		setRotation(rotation + rotationRate);
	}
	
	public CollisionType getCollisionType(Molecule molecule) {
//		System.out.println("Collision between " + getName() + " and " + molecule.getName());
//		System.out.println("leftFace: " + getLeftFace());
//		System.out.println("rightFace: " + getRightFace());
//		System.out.println("topFace: " + getTopFace());
//		System.out.println("bottomFace: " + getBottomFace());
//		System.out.println();
//		System.out.println("m.leftFace: " + molecule.getLeftFace());
//		System.out.println("m.rightFace: " + molecule.getRightFace());
//		System.out.println("m.topFace: " + molecule.getTopFace());
//		System.out.println("m.bottomFace: " + molecule.getBottomFace());
//		System.out.println("--------------------------------");
		
		
		if ((getRightFace() == molecule.getLeftFace() ||
				(getRightFace() < (molecule.getLeftFace() + this.getSpeed()) && 
				getRightFace() > (molecule.getLeftFace() - this.getSpeed())))
				
            && getBottomFace() > molecule.getTopFace()
            && getTopFace() < molecule.getBottomFace())
        {
//			System.out.println("--------------------------------- right collision");
            return CollisionType.RIGHT;
        }
        else if ((getLeftFace() == molecule.getRightFace() ||
        		(getLeftFace() < (molecule.getRightFace() + this.getSpeed()) &&
        		getLeftFace() > (molecule.getLeftFace() - this.getSpeed())))
        		
            && getBottomFace() > molecule.getTopFace()
            && getTopFace() < molecule.getBottomFace())
        {
//        	System.out.println("--------------------------------- left collision");
            return CollisionType.LEFT;
        }
        else if ((getTopFace() == molecule.getBottomFace() ||
        		(getTopFace() < (molecule.getBottomFace() + this.getSpeed()) &&
        		getTopFace() > (molecule.getBottomFace() - this.getSpeed())))
        				
            && getRightFace() > molecule.getLeftFace()
            && getLeftFace() < molecule.getRightFace())
        {
//        	System.out.println("--------------------------------- top collision");
            return CollisionType.TOP;
        }
        else if ((getBottomFace() == molecule.getTopFace() ||
        		(getBottomFace() < (molecule.getTopFace() + this.getSpeed()) &&
        		getBottomFace() > (molecule.getTopFace() - this.getSpeed())))
        		
            && getRightFace() > molecule.getLeftFace()
            && getLeftFace() < molecule.getRightFace())
        {
//        	System.out.println("--------------------------------- bottom collision");
            return CollisionType.BOTTOM;
        }
        else
        {
            return CollisionType.NONE;
        }
	}
	
	public boolean containsPoint(int x, int y) {
		return (x > getLeftFace() && x < getRightFace() && y < getBottomFace() && y > getTopFace());
	}
	
	public boolean isColliding(Molecule molecule) {
		//return !(getCollisionType(molecule).equals(CollisionType.NONE));
		int centerX = this.getX() + (this.getWidth() / 2);
		int centerY = this.getY() + (this.getHeight() / 2);
		int mCenterX = molecule.getX() + (molecule.getWidth() / 2);
		int mCenterY = molecule.getY() + (molecule.getHeight() / 2);
		int collisionDistance = (this.getWidth() + molecule.getWidth()) / 2;
		double distance = Utils.distance(centerX, centerY, mCenterX, mCenterY);
		return (distance < collisionDistance);
	}
	
	public void move(CollisionType type) {
		//System.out.println("independent movement for " + getName() + ": " + independentMovement);
		if(this.independentMovement) {
			//System.out.println("move location called");
			this.moveLocation(type);
		}
		
		if(!attatchedMolecules.isEmpty()) {
			for(Molecule molecule : attatchedMolecules) {
				molecule.moveLike(this);
				//System.out.println("Moving " + molecule.getName() + " like " + this.getName());
			}
		}
	}
	
	private void moveLike(Molecule molecule) {
		this.setX(molecule.getX() + this.getXOffset());
		this.setY(molecule.getY() + this.getYOffset());
	}
	
	public abstract void moveLocation(CollisionType type);

	public int getOpenConnections() {
		return openConnections;
	}

	public void setOpenConnections(int openConnections) {
//		System.out.println("setting open connections for molecule " + getName() + " to " + openConnections);
		this.openConnections = openConnections;
	}
	
	public Set<Molecule> getAdjacentMolecules() {
		return adjacentMolecules;
	}

	public boolean isShowBlured() {
		return showBlured;
	}

	public void setShowBlured(boolean showBlured) {
		this.showBlured = showBlured;
	}

	public boolean isShowDarkened() {
		return showDarkened;
	}

	public void setShowDarkened(boolean showDarkened) {
		this.showDarkened = showDarkened;
	}
	
	public BufferedImage getImage() {
		return img;
	}
	
	public MoleculeSignature getSignature() {
		return sig;
	}
}
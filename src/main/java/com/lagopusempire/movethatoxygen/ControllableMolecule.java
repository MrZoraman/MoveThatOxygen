package com.lagopusempire.movethatoxygen;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class ControllableMolecule extends Molecule {

    public ControllableMolecule(Atom atom, int x, int y, int upKey, int downKey, int leftKey, int rightKey) throws IOException {
        super(atom, x, y);

        this.upKey = upKey;
        this.downKey = downKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
    }

    private final int upKey, downKey, leftKey, rightKey;
    private int distanceX, distanceY = 0;

    private int OxygensAttatched = 0;
    private int HydrogensAttatched = 0;
    private int CarbonsAttatched = 0;
    private int NitrogensAttatched = 0;

    @Override
    public void moveLocation(CollisionType type) {
        switch (type) {
            case LEFT:
                if (distanceX > 0) {
                    this.setX(this.getX() + distanceX);
                }
                this.setY(this.getY() + distanceY);
                break;
            case RIGHT:
                if (distanceX < 0) {
                    this.setX(this.getX() + distanceX);
                }
                this.setY(this.getY() + distanceY);
                break;
            case TOP:
                this.setX(this.getX() + distanceX);
                if (distanceY > 0) {
                    this.setY(this.getY() + distanceY);
                }
                break;
            case BOTTOM:
                this.setX(this.getX() + distanceX);
                if (distanceY < 0) {
                    this.setY(this.getY() + distanceY);
                }
                break;
            case NONE:
                this.setX(this.getX() + distanceX);
                this.setY(this.getY() + distanceY);
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == leftKey) {
            distanceX = 0;
        }
        if (key == rightKey) {
            distanceX = 0;
        }
        if (key == upKey) {
            distanceY = 0;
        }
        if (key == downKey) {
            distanceY = 0;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == leftKey) {
            distanceX = -getSpeed();
        }
        if (key == rightKey) {
            distanceX = getSpeed();
        }
        if (key == upKey) {
            distanceY = -getSpeed();
        }
        if (key == downKey) {
            distanceY = getSpeed();
        }
    }

//	public void move (boolean up, boolean down, boolean left, boolean right) {
//		if(left) {
//			distanceX = -getSpeed();
//		} else {
//			distanceX = 0;
//		}
//		if(right) {
//			distanceX = getSpeed();
//		} else {
//			distanceX = 0;
//		}
//		if(up) {
//			distanceY = -getSpeed();
//		} else {
//			distanceY = 0;
//		}
//		if(down) {
//			distanceY = getSpeed();
//		} else {
//			distanceY = 0;
//		}
//		
//		
//	}
    public int getOxygensAttatched() {
        return OxygensAttatched;
    }

    public void setOxygensAttatched(int oxygensAttatched) {
        OxygensAttatched = oxygensAttatched;
    }

    public int getHydrogensAttatched() {
        return HydrogensAttatched;
    }

    public void setHydrogensAttatched(int hydrogensAttatched) {
        System.out.println("incrementing hydrogens");
        HydrogensAttatched = hydrogensAttatched;
    }

    public int getCarbonsAttatched() {
        return CarbonsAttatched;
    }

    public void setCarbonsAttatched(int carbonsAttatched) {
        CarbonsAttatched = carbonsAttatched;
    }

    public int getNitrogensAttatched() {
        return NitrogensAttatched;
    }

    public void setNitrogensAttatched(int nitrogensAttatched) {
        NitrogensAttatched = nitrogensAttatched;
    }
}

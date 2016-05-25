package com.lagopuempire.movethatoxygen.levels;

import com.lagopuempire.movethatoxygen.Atom;
import com.lagopuempire.movethatoxygen.ControllableMolecule;
import com.lagopuempire.movethatoxygen.Environment;
import com.lagopuempire.movethatoxygen.Molecule;
import com.lagopuempire.movethatoxygen.MoleculeSignature;
import com.lagopuempire.movethatoxygen.StandardMolecule;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

public class StandardLevel implements Level, KeyListener {
	
	private boolean invalidAttempt = false;
	private boolean isValidMolecule = false;
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.YELLOW);
		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
		g2d.drawString("Press SPACE to reset", 720, 25);
		g2d.drawString("Press ENTER to submit", 720, 50);
		g2d.drawString("Press BACKSPACE to go back", 690, 75);
		
		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		g2d.setColor(Color.RED);
		
		if(invalidAttempt) {
			g2d.drawString("Unknown Molecule!", 25, 25);
		}
	}

	@Override
	public void levelStart() {
		
		Environment.getInstance().addKeyListener(this);
		
		try {
			Environment.getInstance().addMolecule(new ControllableMolecule(Environment.getInstance().getPlayerAtom(), 350, 350, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));

			switch(Environment.getInstance().getPlayerAtom()) {
			case CARBON:
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.CARBON, 100, 100));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 100, 400));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 500, 200));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.OXYGEN, 800, 50));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.OXYGEN, 50, 800));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 381, 106));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 313, 648));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 667, 775));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 726, 409));
				break;
			case HYDROGEN:
				break;
			case NITROGEN:
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.NITROGEN, 100, 100));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 100, 400));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 500, 200));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 800, 50));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 50, 800));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 381, 106));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 313, 648));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 667, 775));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 726, 409));
				break;
			case OXYGEN:
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.OXYGEN, 100, 100));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 100, 400));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 500, 200));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 800, 50));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 50, 800));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 381, 106));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 313, 648));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 667, 775));
				Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 726, 409));
				break;
			default:
				break;
			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void resetTicks() {
		ticks = 25;
	}
	
	private int ticks = 25;
	@Override
	public void tick() {
		if(invalidAttempt) {
			ticks--;
			if(ticks == 0) {
				invalidAttempt = false;
				resetTicks();
			}
		}
	}

	@Override
	public void reset() {
		resetTicks();
		Environment.getInstance().removeKeyListener(this);
		isValidMolecule = false;
		invalidAttempt = false;
	}

	@Override
	public void playerCollision(ControllableMolecule player, Molecule molecule) {
		System.out.println("Collision (attatchment)");
		Environment.getInstance().setStatusString(player.getSignature().getIdentifier());
		
		
		if(Environment.getInstance().getPlayers().size() > 0) {
			if(Environment.getInstance().getPlayers().get(0).getSignature().getFullSignature() != null) {
				resetTicks();
				MoleculeSignature.setPlayerSignature(Environment.getInstance().getPlayers().get(0).getSignature().getFullSignature());
				isValidMolecule = true;
				invalidAttempt = false;
			}
		}
	}

	@Override
	public Level getNextLevel() {
		return LevelManager.getInstance().getLevel("MenuLevel");
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
            switch (arg0.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    StandardMolecule mol = StandardMolecule.fromMolecule(Environment.getInstance().getPlayers().get(0));
                    if(isValidMolecule) {
                        LevelManager.getInstance().setCurrentLevel("QuizLevel");
                        QuizLevel lev = ((QuizLevel) LevelManager.getInstance().getCurrentLevel());
                        Random random = new Random();
                        mol.setVelocityX(random.nextInt(2) == 0 ? 1 : -1);
                        mol.setVelocityY(random.nextInt(2) == 0 ? 1 : -1);
                        mol.setShowDarkened(true);
                        mol.setRotationRate(0.0);
                        lev.setControlMolecule(mol);
                        for(Molecule molecule : mol.getAttatchedMolecules()) {
                            molecule.setShowDarkened(true);
                            //				System.out.println("level adding molecules");
                            molecule.setRotationRate(0.0);
                            lev.addMolecule(molecule);
                        }
                        lev.moveMolecules(600, 400);
                    } else {
                        invalidAttempt = true;
                    }
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    LevelManager.getInstance().setCurrentLevel("MenuLevel");
                    Environment.getInstance().setStatusString("");
                    break;
                case KeyEvent.VK_SPACE:
                    LevelManager.getInstance().setCurrentLevel("StandardLevel");
                    break;
                default:
                    break;
            }
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}

package com.lagopuempire.movethatoxygen.levels;

import com.lagopuempire.movethatoxygen.Atom;
import com.lagopuempire.movethatoxygen.ControllableMolecule;
import com.lagopuempire.movethatoxygen.Environment;
import com.lagopuempire.movethatoxygen.Molecule;
import com.lagopuempire.movethatoxygen.StandardMolecule;
import com.lagopuempire.movethatoxygen.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.Timer;

public class HydrogenLevel implements Level, KeyListener {
	
	public HydrogenLevel() {
	}

	private Timer hydrogenAdderTimer = null;
	private int hydrogens = 9;
	private int seconds = 0;
	private boolean showExitSign = false;
	
	@Override
	public void levelStart() {
		Environment.getInstance().addKeyListener(this);
		showExitSign = false;
		try {
			Environment.getInstance().addMolecule(new ControllableMolecule(Atom.HYDROGEN, 350, 350, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));
			
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 100, 100));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 100, 400));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 500, 200));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 800, 50));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 50, 800));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 381, 106));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 313, 648));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 667, 775));
			Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, 726, 409));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Environment.getInstance().setExitString("Press ENTER to continue");
		
		hydrogenAdderTimer = new Timer(1000, new ActionListener() {
			Random random = new Random();
			int counter = 0;
			int spawnCounter = random.nextInt(3);
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				System.out.println("action performed");
				try {
					if(counter == spawnCounter) {
//						System.out.println("attempting to place...");
						boolean moleculePlaced = false;
						while (!moleculePlaced) {
							int x = random.nextInt(900);
							int y = random.nextInt(900);
							boolean canPlace = true;
							for(Molecule player : Environment.getInstance().getPlayers()) {
								double distance = Utils.distance(player.getX(), player.getY(), x, y);
//								System.out.println("distance: " + distance);
								if(distance < 200) {
									canPlace = false;
								}
							}
							
							if(canPlace) {
								Environment.getInstance().addMolecule(new StandardMolecule(Atom.HYDROGEN, random.nextInt(900), random.nextInt(900)));
								hydrogens++;
								counter = 0;
								moleculePlaced = true;
							}
						}
					} else {
						counter++;
					}
					seconds++;
				} catch (IOException e) {
					e.printStackTrace();
				}
//				System.out.println("end action performed");
			}
		});
		this.hydrogenAdderTimer.start();
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.YELLOW);
		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		g2d.drawString("Hydrogens: " + hydrogens, 25, 75);
		g2d.drawString("Seconds: " + seconds, 25, 100);
		if(showExitSign) {
			g2d.drawString("Press ENTER to continue", 300, 300);
		}
	}

	@Override
	public void tick() {
		
	}
	
	private void stopRound() {
		if(this.hydrogenAdderTimer != null) {
			this.hydrogenAdderTimer.stop();
			this.hydrogenAdderTimer = null;
			this.showExitSign = true;
			for(Molecule molecule : Environment.getInstance().getMolecules()) {
				molecule.setIndependentMovement(false);
				molecule.setRotationRate(0);
				if(!(molecule instanceof ControllableMolecule || !molecule.isLimitedByBorders()))
					molecule.setShowDarkened(true);
			}
		}

		if(hydrogenAdderTimer != null) {
			if( hydrogenAdderTimer.isRunning()) {
				hydrogenAdderTimer.stop();
			}
		}
		
	}

	@Override
	public void reset() {
		seconds = 0;
		showExitSign = false;
		hydrogens = 9;
		//Environment.getInstance().setExitString("Press ENTER to reset");
		Environment.getInstance().removeKeyListener(this);
		stopRound();
	}

	@Override
	public void playerCollision(ControllableMolecule player, Molecule molecule) {
		Environment.getInstance().setStatusString(player.getSignature().getIdentifier());
		stopRound();
	}

	@Override
	public Level getNextLevel() {
		return LevelManager.getInstance().getLevel("MenuLevel");
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			LevelManager.getInstance().setCurrentLevel("MenuLevel");
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}

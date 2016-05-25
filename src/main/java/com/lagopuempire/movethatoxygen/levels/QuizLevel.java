package com.lagopuempire.movethatoxygen.levels;

import com.lagopuempire.movethatoxygen.CollisionType;
import com.lagopuempire.movethatoxygen.ControllableMolecule;
import com.lagopuempire.movethatoxygen.Environment;
import com.lagopuempire.movethatoxygen.Molecule;
import com.lagopuempire.movethatoxygen.MoleculeSignature;
import com.lagopuempire.movethatoxygen.buttons.ButtonPressEvent;
import com.lagopuempire.movethatoxygen.buttons.ClickableButton;
import com.lagopuempire.movethatoxygen.buttons.RectangleButton;
import com.lagopuempire.movethatoxygen.characteristics.BondPolarity;
import com.lagopuempire.movethatoxygen.characteristics.BondShape;
import com.lagopuempire.movethatoxygen.characteristics.IntermolecularForces;
import com.lagopuempire.movethatoxygen.characteristics.LonePairs;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class QuizLevel implements Level, KeyListener {
	
	public QuizLevel() {
		buttons = new HashSet<RectangleButton>();
		for(int ii = 0; ii < messages.length; ii++) {
			messages[ii] = "";
		}
		molecules = new HashSet<Molecule>();
	}
	
	private Molecule controlMolecule = null;
	private Set<Molecule> molecules = null;
	
	private Set<RectangleButton> buttons;
	
	public void addMolecule(Molecule molecule) {
		molecules.add(molecule);
		if(controlMolecule != null) {
			System.out.println("attatching molecule");
			controlMolecule.attatchMolecule(molecule);
		}
	}
	
	public void setControlMolecule(Molecule molecule) {
		controlMolecule = molecule;
		molecules.add(molecule);
	}
	
	public void moveMolecules(int x, int y) {
		controlMolecule.setX(x);
		controlMolecule.setY(y);
	}

	@Override
	public void draw(Graphics2D g2d) {
		for(Molecule molecule : molecules) {
			molecule.draw(g2d);
		}
		
		for(RectangleButton button : buttons) {
			button.draw(g2d);
		}
		

		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		g2d.setColor(Color.ORANGE);
		switch(state) {
		case BOND_SHAPE:
			g2d.drawString("Bond Shape", 75, 75);
			break;
		case BOND_ANGLE:
			g2d.drawString("Bond Angle", 75, 75);
			break;
		case BOND_POLARITY:
			g2d.drawString("Bond Polarity", 75, 75);
			break;
		case INTERMOLECULAR_FORCES:
			g2d.drawString("Intermolecular Forces", 75, 75);
			break;
		case LONE_PAIRS:
			g2d.drawString("Lone Pairs", 75, 75);
			break;
		}
		
		g2d.setColor(Color.YELLOW);
		int ymsg = 130;
		for(int ii = 0; ii < messages.length; ii++) {
//			System.out.println("writing message indexof[" + ii + "] y=" + 325 + ",x=" + ymsg);
			g2d.drawString(messages[ii], 325, ymsg);
			ymsg += 75;
		}

		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
		g2d.drawString("Press ENTER to continue", 718, 25);
		g2d.drawString("Press BACKSPACE to return", 700, 50);
	}
	
	private LevelState state = LevelState.BOND_SHAPE;
	private String[] messages = new String[7];
	private boolean canScore = true;
	
	/*
	 * public RectangleButton(
	 * int x1, 
	 * int y1, 
	 * int x2, 
	 * int y2, 
	 * Color color, 
	 * int brightness, 
	 * Color textColor, 
	 * String text, 
	 * int textX, 
	 * int textY, 
	 * ButtonPressEvent buttonPressEvent) {
	 */

	@Override
	public void levelStart() {
		
		//switch(bondShape)
		putButtons();
		//Environment.getInstance().setExitString("Press ENTER to continue");
		Environment.getInstance().addKeyListener(this);
		canScore = true;
		
	}
	
	private void clearButtons() {
		//System.out.println("clearing buttons");
		for(ClickableButton button : buttons) {
			button.unregisterListener();
		}
		buttons.clear();
	}
	
	public void nextState() {
		clearButtons();
		//canScore = true;
		//System.out.println("going to next state...");
		for(int ii = 0; ii < messages.length; ii++) {
			messages[ii] = "";
		}
		switch(state) {
		case BOND_ANGLE:
			state = LevelState.INTERMOLECULAR_FORCES;
			break;
		case BOND_POLARITY:
			state = LevelState.LONE_PAIRS;
			break;
		case BOND_SHAPE:
			state = LevelState.BOND_POLARITY;
			break;
		case INTERMOLECULAR_FORCES:
			state = LevelState.BOND_SHAPE;
			LevelManager.getInstance().setCurrentLevel("MenuLevel");
			break;
		case LONE_PAIRS:
			state = LevelState.BOND_ANGLE;
			break;
		default:
			state = LevelState.BOND_SHAPE;
			break;
		}
		
		this.putButtons();
	}

	@Override
	public void tick() {
		for(Molecule molecule : molecules) {
			molecule.move(CollisionType.NONE);
		}
	}

	@Override
	public void reset() {
		Environment.getInstance().removeKeyListener(this);

		//Environment.getInstance().setExitString("Press ENTER to reset");
		Environment.getInstance().setStatusString("");
		for(int ii = 0; ii < messages.length; ii++) {
			messages[ii] = "";
		}
		canScore = true;
		clearButtons();
		
		molecules.clear();
		controlMolecule = null;
		
		Environment.getInstance().setStatusString("");
	}

	@Override
	public void playerCollision(ControllableMolecule player, Molecule molecule) {
	}

	@Override
	public Level getNextLevel() {
		return LevelManager.getInstance().getLevel("MenuLevel");
	}
	
	enum LevelState {
		BOND_SHAPE,
		BOND_POLARITY,
		LONE_PAIRS,
		BOND_ANGLE,
		INTERMOLECULAR_FORCES
	}
	
	private void putButtons() {
		this.setCanScore(true);
		int xLeft = 75;
		switch(state) {
		case BOND_ANGLE:
			buttons.add(new RectangleButton(xLeft, 100, 300, 150, new Color(51, 102, 153), 35, new Color(232, 225, 32), "109.5", xLeft + 25, 130, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					System.out.println("Can score: " + canScore);
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 109.5) {
						messages[0] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[0] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 175, 300, 225, new Color(51, 102, 153), 35, new Color(232, 225, 32), "107", xLeft + 25, 205, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 107) {
						messages[1] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[1] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 250, 300, 300, new Color(51, 102, 153), 35, new Color(232, 225, 32), "104.5", xLeft + 25, 280, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 104.5) {
						messages[2] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[2] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 325, 300, 375, new Color(51, 102, 153), 35, new Color(232, 225, 32), "180", xLeft + 25, 355, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 180) {
						messages[3] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[3] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 400, 300, 450, new Color(51, 102, 153), 35, new Color(232, 225, 32), "120", xLeft + 25, 430, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 120) {
						messages[4] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[4] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 475, 300, 525, new Color(51, 102, 153), 35, new Color(232, 225, 32), "90", xLeft + 25, 505, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 90) {
						messages[5] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[5] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 550, 300, 600, new Color(51, 102, 153), 35, new Color(232, 225, 32), "107.5", xLeft + 25, 580, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondAngle() == 107.5) {
						messages[6] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[6] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			break;
			
			
			
		case BOND_POLARITY:
			buttons.add(new RectangleButton(xLeft, 100, 300, 150, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Polar", xLeft + 25, 130, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondPolarity().equals(BondPolarity.POLAR)) {
						messages[0] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[0] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 175, 300, 225, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Nonpolar", xLeft + 25, 205, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondPolarity().equals(BondPolarity.NONPOLAR)) {
						messages[1] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[1] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			break;
			
			
			
		case BOND_SHAPE:
			buttons.add(new RectangleButton(xLeft, 100, 300, 150, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Linear", xLeft + 25, 130, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.LINEAR)) {
						messages[0] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						System.out.println("setting to false from up here");
						messages[0] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 175, 300, 225, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Bent", xLeft + 25, 205, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.BENT)) {
						messages[1] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[1] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 250, 300, 300, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Trigonal Planar", xLeft + 25, 280, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.TRIGONAL_PLANAR)) {
						messages[2] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[2] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 325, 300, 375, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Tetrahedral", xLeft + 25, 355, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.TETRAHEDRAL)) {
						messages[3] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[3] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 400, 300, 450, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Square Planar", xLeft + 25, 430, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.SQUARE_PLANAR)) {
						messages[4] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[4] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 475, 300, 525, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Octohedral", xLeft + 25, 505, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.OCTOHEDRAL)) {
						messages[5] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[5] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 550, 300, 600, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Trigonal Bipyramidal", xLeft + 25, 580, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getBondShape().equals(BondShape.TRIGONAL_BYPYRAMIDAL)) {
						messages[6] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[6] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			break;
			
			
			
		case INTERMOLECULAR_FORCES:
			buttons.add(new RectangleButton(xLeft, 100, 300, 150, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Dipole-Dipole", xLeft + 25, 130, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getIntermolecularForces().equals(IntermolecularForces.DIPOLE_DIPOLE)) {
						messages[0] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[0] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 175, 300, 225, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Hydrogen", xLeft + 25, 205, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getIntermolecularForces().equals(IntermolecularForces.HYDROGEN)) {
						messages[1] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[1] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 250, 300, 300, new Color(51, 102, 153), 35, new Color(232, 225, 32), "London Dispersion", xLeft + 25, 280, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getIntermolecularForces().equals(IntermolecularForces.LONDON_DISPERSION)) {
						messages[2] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[2] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			break;
			
			
			
		case LONE_PAIRS:
			buttons.add(new RectangleButton(xLeft, 100, 300, 150, new Color(51, 102, 153), 35, new Color(232, 225, 32), "None", xLeft + 25, 130, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.NONE)) {
						messages[0] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[0] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 175, 300, 225, new Color(51, 102, 153), 35, new Color(232, 225, 32), "One", xLeft + 25, 205, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.ONE)) {
						messages[1] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[1] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 250, 300, 300, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Two", xLeft + 25, 280, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.TWO)) {
						messages[2] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[2] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			
			buttons.add(new RectangleButton(xLeft, 325, 300, 375, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Three", xLeft + 25, 355, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.THREE)) {
						messages[3] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[3] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			

			buttons.add(new RectangleButton(xLeft, 400, 300, 450, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Four", xLeft + 25, 430, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.FOUR)) {
						messages[4] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[4] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			

			buttons.add(new RectangleButton(xLeft, 475, 300, 525, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Six", xLeft + 25, 505, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.SIX)) {
						messages[5] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[5] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			

			buttons.add(new RectangleButton(xLeft, 550, 300, 600, new Color(51, 102, 153), 35, new Color(232, 225, 32), "Seven", xLeft + 25, 580, new ButtonPressEvent() {
				@Override
				public void buttonPressed() {
					if(MoleculeSignature.getPlayerSignature().getLonePairs().equals(LonePairs.SEVEN)) {
						messages[6] = "Correct!";
						if(canScore) {
							Environment.getInstance().setScore(Environment.getInstance().getScore() + 3);
						}
					} else {
						messages[6] = "Wrong";
					}
					setCanScore(false);
				}
			}));
			break;
		}
	}
	
	private void setCanScore(boolean canScore) {
		//System.out.println("setting canScore to " + canScore);
		//Thread.dumpStack();
		this.canScore = canScore;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
			nextState();
		} else if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			state = LevelState.BOND_SHAPE;
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

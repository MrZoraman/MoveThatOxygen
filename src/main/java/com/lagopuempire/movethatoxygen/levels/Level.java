package com.lagopuempire.movethatoxygen.levels;

import com.lagopuempire.movethatoxygen.ControllableMolecule;
import com.lagopuempire.movethatoxygen.Drawable;
import com.lagopuempire.movethatoxygen.Molecule;

public interface Level extends Drawable {
	
	public void levelStart();
	
	public void tick();
	
	public void reset();
	
	public void playerCollision(ControllableMolecule player, Molecule molecule);
	
	public Level getNextLevel();
}


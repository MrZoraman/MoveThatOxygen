package com.lagopusempire.movethatoxygen.levels;

import com.lagopusempire.movethatoxygen.ControllableMolecule;
import com.lagopusempire.movethatoxygen.Drawable;
import com.lagopusempire.movethatoxygen.Molecule;

public interface Level extends Drawable {

    public void levelStart();

    public void tick();

    public void reset();

    public void playerCollision(ControllableMolecule player, Molecule molecule);

    public Level getNextLevel();
}

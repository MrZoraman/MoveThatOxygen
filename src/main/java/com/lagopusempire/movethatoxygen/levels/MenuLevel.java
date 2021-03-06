package com.lagopusempire.movethatoxygen.levels;

import com.lagopusempire.movethatoxygen.Atom;
import com.lagopusempire.movethatoxygen.CollisionType;
import com.lagopusempire.movethatoxygen.ControllableMolecule;
import com.lagopusempire.movethatoxygen.Environment;
import com.lagopusempire.movethatoxygen.Images.ImageManager;
import com.lagopusempire.movethatoxygen.Molecule;
import com.lagopusempire.movethatoxygen.Utils;
import com.lagopusempire.movethatoxygen.buttons.BufferedImageButton;
import com.lagopusempire.movethatoxygen.buttons.ButtonPressEvent;
import com.lagopusempire.movethatoxygen.buttons.ClickableButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MenuLevel implements Level {

    public MenuLevel() {
        try {
            ambientMolecules = Utils.generateAmbientMolecules();
        } catch (IOException e) {
            ambientMolecules = new HashSet<>();
        }

        buttons = new HashSet<>();
    }

    private ClickableButton carbonButton;
    private ClickableButton oxygenButton;
    private ClickableButton hydrogenButton;
    private ClickableButton nitrogenButton;

    private Set<Molecule> ambientMolecules;

    Set<ClickableButton> buttons;

    @Override
    public void draw(Graphics2D g2d) {
        for (Molecule molecule : ambientMolecules) {
            molecule.draw(g2d);
        }

        for (ClickableButton button : buttons) {
            button.draw(g2d);
        }

        g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
        g2d.setColor(Color.GREEN);
        g2d.drawString("Select an atom", 325, 300);

        g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("Minigame", 680, 520);
        g2d.drawString("Press ESCAPE to exit anytime", 685, 25);

        g2d.setColor(Color.GRAY);
        g2d.drawString("Version 1.1.1", 800, 870);
    }

    @Override
    public void levelStart() {
        carbonButton = new BufferedImageButton(50, 375, ImageManager.getImage("Carbon"), new ButtonPressEvent() {
            @Override
            public void buttonPressed() {
                Environment.getInstance().setPlayerAtom(Atom.CARBON);
                LevelManager.getInstance().setCurrentLevel("StandardLevel");
                buttons.clear();
            }

            @Override
            public boolean canCloseAfterPress() {
                return true;
            }
        });

        oxygenButton = new BufferedImageButton(250, 375, ImageManager.getImage("Oxygen"), new ButtonPressEvent() {
            @Override
            public void buttonPressed() {
                Environment.getInstance().setPlayerAtom(Atom.OXYGEN);
                LevelManager.getInstance().setCurrentLevel("StandardLevel");
                buttons.clear();
            }

            @Override
            public boolean canCloseAfterPress() {
                return true;
            }
        });

        nitrogenButton = new BufferedImageButton(450, 375, ImageManager.getImage("Nitrogen"), new ButtonPressEvent() {
            @Override
            public void buttonPressed() {
                Environment.getInstance().setPlayerAtom(Atom.NITROGEN);
                LevelManager.getInstance().setCurrentLevel("StandardLevel");
                buttons.clear();
            }

            @Override
            public boolean canCloseAfterPress() {
                return true;
            }
        });

        hydrogenButton = new BufferedImageButton(650, 375, ImageManager.getImage("Hydrogen"), new ButtonPressEvent() {
            @Override
            public void buttonPressed() {
                LevelManager.getInstance().setCurrentLevel("HydrogenLevel");
                buttons.clear();
            }

            @Override
            public boolean canCloseAfterPress() {
                return true;
            }
        });

        buttons.add(carbonButton);
        buttons.add(oxygenButton);
        buttons.add(hydrogenButton);
        buttons.add(nitrogenButton);
    }

    @Override
    public void tick() {
//		System.out.println("tick");
        for (Molecule molecule : ambientMolecules) {
            molecule.move(CollisionType.NONE);
        }
    }

    @Override
    public void reset() {
        for (ClickableButton button : buttons) {
            button.unregisterListener();
        }
        buttons.clear();
    }

    @Override
    public void playerCollision(ControllableMolecule player, Molecule molecule) {

    }

    @Override
    public Level getNextLevel() {
        return this;
    }

}

package com.lagopusempire.movethatoxygen;

import com.lagopusempire.movethatoxygen.levels.Level;
import com.lagopusempire.movethatoxygen.levels.LevelManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Environment extends JPanel implements ActionListener {

    private Environment() {
        this.addKeyListener(new processKeyboardEvents());
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(java.awt.Color.BLACK);

        timer = new Timer(50, this);

        molecules = new ArrayList<Molecule>();
        players = new ArrayList<ControllableMolecule>();

        drawables = new HashSet<Drawable>();
        setScore(0);

    }

    private static Environment instance = null;

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
            LevelManager.getInstance();
            instance.timer.start();
        }
        return instance;
    }

    private final Timer timer;
    private final List<Molecule> molecules;
    private final List<ControllableMolecule> players;
    private String statusString = "";
    //private String exitString = "Press ENTER to reset";
    private final Set<Drawable> drawables;
    private int score;

    private Level currentLevel;

    private Atom playerAtom;

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        currentLevel.tick();

        for (int ii = 0; ii < players.size(); ii++) {
            CollisionType type = CollisionType.NONE;
            for (int kk = 0; kk < molecules.size(); kk++) {
                if (players.get(ii).equals(molecules.get(kk))) {
                    continue;
                }
                if (players.get(ii).isColliding(molecules.get(kk)) && molecules.get(kk).getIndependentMovement() == true) {
                    //type = players.get(ii).getCollisionType(molecules.get(kk));
                    //System.out.println("Initial loop: attatch");
                    players.get(ii).attatchMolecule(molecules.get(kk));
                    //this.currentLevel.playerCollision(players.get(ii), molecules.get(kk));
                    break;
                }
            }
            players.get(ii).move(type);
        }

        for (int ii = 0; ii < molecules.size(); ii++) {
            if ((!(molecules.get(ii) instanceof ControllableMolecule))
                    && molecules.get(ii).getIndependentMovement() == false) {
                for (int kk = 0; kk < molecules.size(); kk++) {
                    if (ii == kk) {
                        continue;
                    }
                    if ((!(molecules.get(kk) instanceof ControllableMolecule)) && (!(molecules.get(ii) instanceof ControllableMolecule))) {
                        if (molecules.get(ii).isColliding(molecules.get(kk)) && molecules.get(kk).getIndependentMovement() == true) {
                            molecules.get(ii).attatchMolecule(molecules.get(kk));
                            break;
                        }
                    }
                }
            }
            molecules.get(ii).move(CollisionType.NONE);
        }

        this.repaint();

    }

    public void addMolecule(Molecule molecule) {
        molecules.add(molecule);
        if (molecule instanceof ControllableMolecule) {
            players.add((ControllableMolecule) molecule);
        }
//		addDrawable(molecule);
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int ii = 0; ii < molecules.size(); ii++) {
            molecules.get(ii).draw(g2d);
        }

        g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
        g2d.setColor(Color.CYAN);

        g2d.drawString(statusString, 25, 25);

        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 14));
        //g2d.drawString(exitString, 720, 25);

        currentLevel.draw(g2d);

        for (Drawable drawable : drawables) {
            drawable.draw(g2d);
        }

        g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
        g2d.setColor(Color.GREEN);
        g2d.drawString("Score: " + score, 25, 50);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public List<ControllableMolecule> getPlayers() {
        return players;
    }

    public List<Molecule> getMolecules() {
        return molecules;
    }

    public void clearMolecules() {
        molecules.clear();
        players.clear();
    }

//	public String getExitString() {
//		return exitString;
//	}
//
//	public void setExitString(String exitString) {
//		this.exitString = exitString;
//	}
    public Atom getPlayerAtom() {
        return playerAtom;
    }

    public void setPlayerAtom(Atom playerAtom) {
        this.playerAtom = playerAtom;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    class processKeyboardEvents extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            for (ControllableMolecule mole : players) {
                mole.keyReleased(e);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
//			else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//				LevelManager.getInstance().setCurrentLevel(Environment.getInstance().currentLevel.getNextLevel());
//				return;
//			}

            for (ControllableMolecule mole : players) {
                mole.keyPressed(e);
            }
        }
    }
}

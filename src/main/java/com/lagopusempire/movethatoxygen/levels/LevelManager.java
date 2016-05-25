package com.lagopusempire.movethatoxygen.levels;

import com.lagopusempire.movethatoxygen.Drawable;
import com.lagopusempire.movethatoxygen.Environment;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public class LevelManager implements Drawable {
	private static LevelManager instance = null;
	
	public static LevelManager getInstance() {
		if(instance == null) {
			instance = new LevelManager();
		}
		return instance;
	}
	
	private LevelManager() {
		Environment.getInstance().addDrawable(this);
		levels = new HashMap<>();
		//TODO: seperate game engine
		levels.put("MenuLevel", new MenuLevel());
		levels.put("HydrogenLevel", new HydrogenLevel());
		levels.put("StandardLevel", new StandardLevel());
		levels.put("QuizLevel", new QuizLevel());
		levels.put("HighScoreLevel", new HighScoreLevel());
		System.out.println("levels map populated");
		currentLevel = levels.get("MenuLevel");
//		currentLevel = levels.get("QuizLevel");
//		currentLevel = levels.get("HighScoreLevel");
		currentLevel.levelStart();
		Environment.getInstance().setCurrentLevel(currentLevel);
	}
	
	private final Map<String, Level> levels;
	private Level currentLevel;
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		g2d.setColor(Color.GREEN);
		
		//g2d.drawString("Level " + levelNumber, 25, 250);
	}
	
	public Level getLevel(String level) {
		return levels.get(level);
	}
	
	public void setCurrentLevel(String level) {
		if(levels.containsKey(level)) {
			setCurrentLevel(levels.get(level));
		} else {
			System.out.println("Error: level \' " + level + "\' doens\'t exist!");
		}
	}
	
	public void setCurrentLevel(Level level) {
		System.out.println("changing level...");
		currentLevel.reset();
		Environment.getInstance().clearMolecules();
		currentLevel = level;
		Environment.getInstance().setCurrentLevel(currentLevel);
		currentLevel.levelStart();
//		Environment.getInstance().setStatusString("");
		//MoleculeSignature.setPlayerSignature(Environment.getInstance().getPlayers().get(0).getSignature());
	}
	
	public Level getCurrentLevel() {
		return currentLevel;
	}
}

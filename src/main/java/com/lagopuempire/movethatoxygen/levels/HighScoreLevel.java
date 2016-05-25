package com.lagopuempire.movethatoxygen.levels;

import com.lagopuempire.movethatoxygen.ControllableMolecule;
import com.lagopuempire.movethatoxygen.Environment;
import com.lagopuempire.movethatoxygen.Molecule;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class HighScoreLevel implements Level, KeyListener {
	
	public static final int TICKS_PER_CURSOR_PULSE = 10;
	public static final int ROW_ITERATOR = 20;
	public static final int COLUMN_ITERATOR = 20;
	
	private int tickCounter = 0;
	
	private int cursorX = 10;
	private int cursorY = 100;
	private final int cursorWidth = 20;
	private final int cursorHeight = 30;
	
	private boolean cursorSolid = false;
	private Entry currentEntry;
	
	private List<Entry> entries;
	
	public HighScoreLevel() {
		entries = new ArrayList<Entry>();
		entries.add(new Entry(100));
		currentEntry = entries.get(0);
	}

	@Override
	public void draw(Graphics2D g2d) {

		g2d.setFont(new Font("Book Antiqua", Font.PLAIN, 30));
		g2d.setColor(Color.CYAN);
		
		if(cursorSolid) {
			g2d.fillRect(cursorX, cursorY, cursorWidth, cursorHeight);
		} else {
		}

		g2d.drawString("A", cursorX, cursorY + 30);
	}
	
	private void moveCursorLeft() {
		cursorX += ROW_ITERATOR;
	}
	
	private void moveCursorRight() {
		cursorX -= ROW_ITERATOR;
	}

	@Override
	public void levelStart() {
		Environment.getInstance().addKeyListener(this);
	}

	@Override
	public void tick() {
		if(tickCounter == TICKS_PER_CURSOR_PULSE) {
			tickCounter = 0;
			if(cursorSolid == true) {
				cursorSolid = false;
			} else {
				cursorSolid = true;
			}
		} else {
			tickCounter++;
		}
	}

	@Override
	public void reset() {
		tickCounter = 0;
		cursorSolid = false;
		Environment.getInstance().removeKeyListener(this);
	}

	@Override
	public void playerCollision(ControllableMolecule player, Molecule molecule) {
	}

	@Override
	public Level getNextLevel() {
		return LevelManager.getInstance().getLevel("MenuLevel");
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		System.out.println("key pressed");
		moveCursorLeft();
	}
	
	public Entry getEntry(int index) {
		if(entries.get(index) != null) {
			return entries.get(index);
		} else {
			return entries.get(0);
		}
	}
	
	public void setCurrentEntry(int index) {
		this.currentEntry = getEntry(index);
		
	}
	

	class Entry {
		public Entry(int column) {
			this.column = column;
			chars = new ArrayList<Character>();
			
		}
		
		private final int column;
		private List<Character> chars;
		private int cursorX;
		private int cursorY;
		
		public int getColumn() {
			return column;
		}
		
		public List<Character> getChars() {
			return chars;
		}
		
		public void addChar(Character character) {
			chars.add(character);
			
		}
		
		public String getString() {
			String str = "";
			for(int ii = 0; ii < chars.size(); ii++) {
				str = str + chars.get(ii);
			}
			return str;
		}
		
		public void draw (Graphics2D g2d) {
			int x = 10;
			for(int ii = 0; ii < chars.size(); ii++) {
				g2d.drawString(chars.get(ii).toString(), x, column);
			}
		}
		
		
	}
}


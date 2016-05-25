package com.lagopuempire.movethatoxygen;

import com.lagopuempire.movethatoxygen.characteristics.BondPolarity;
import com.lagopuempire.movethatoxygen.characteristics.BondShape;
import com.lagopuempire.movethatoxygen.characteristics.IntermolecularForces;
import com.lagopuempire.movethatoxygen.characteristics.LonePairs;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MoleculeSignature {
	
	private static final Set<Atom> knownAtoms;
	private static final Set<MoleculeSignature> signatures;
	private static MoleculeSignature playerSignature;
	
	public MoleculeSignature() {
		amountOfAtoms = new HashMap<Atom, Integer>();
		for(Atom atom : knownAtoms) {
			setAtom(atom, 0);
		}
	}
	
	private final Map<Atom, Integer> amountOfAtoms;
	private String identifier = "";
	private BondPolarity bondPolarity;
	private BondShape bondShape;
	private IntermolecularForces intermolecularForces;
	private LonePairs lonePairs;
	private double bondAngle;
	private boolean isValid = false;
	
	public boolean isValid() {
		return isValid;
	}
	
	public void addAtom(Atom atom) {
		this.setAtom(atom, getAmount(atom) + 1);
	}
	
	public void removeAtom(Atom atom) {
		this.setAtom(atom, getAmount(atom) - 1);
	}
	
	private void setAtom(Atom atom, int amount) {
		if(!knownAtoms.contains(atom)) {
			knownAtoms.add(atom);
			//System.out.println("new atom found! " + atom);
			for(MoleculeSignature signature : signatures) {
				signature.setAtom(atom, 0);
			}
		}
		
		amountOfAtoms.put(atom, amount);
	}
	
	public int getAmount(Atom atom) {
		if(amountOfAtoms.containsKey(atom)) {
			return amountOfAtoms.get(atom);
		} else {
			amountOfAtoms.put(atom, 0);
			return 0;
		}
	}

	public String getIdentifier() {
		//look for right one
		MoleculeSignature sig = getFullSignature();
		if(sig == null) {
			return identifier;
		} else {
			return sig.identifier;
		}
	}
	
	public MoleculeSignature getFullSignature() {
		if(!signatures.isEmpty()) {
			for(MoleculeSignature signature : signatures) {
				//System.out.println("checking if this is equal to " + signature.identifier);
				if(equalTo(signature)) {
					return signature;
				}
			}
		}
		return null;
	}
	
	public boolean equalTo(MoleculeSignature signature) {
		for(Atom atom : knownAtoms) {
			//System.out.println(atom + " : if(" + getAmount(atom) + ") != " + signature.getAmount(atom));
			if(getAmount(atom) != signature.getAmount(atom)) {
				//System.out.println("returning false... -------");
				return false;
			}
		}
		
		return true;
	}

	private void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public void addSignature(MoleculeSignature signature) {
		signatures.add(signature);
	}
	
	public BondPolarity getBondPolarity() {
		return bondPolarity;
	}

	public void setBondPolarity(BondPolarity bondPolarity) {
		this.bondPolarity = bondPolarity;
	}

	public BondShape getBondShape() {
		return bondShape;
	}

	public void setBondShape(BondShape bondShape) {
		this.bondShape = bondShape;
	}

	public IntermolecularForces getIntermolecularForces() {
		return intermolecularForces;
	}

	public void setIntermolecularForces(IntermolecularForces intermolecularForces) {
		this.intermolecularForces = intermolecularForces;
	}

	public LonePairs getLonePairs() {
		return lonePairs;
	}

	public double getBondAngle() {
		return bondAngle;
	}

	public void setBondAngle(double bondAngle) {
		this.bondAngle = bondAngle;
	}

	public void setLonePairs(LonePairs lonePairs) {
		this.lonePairs = lonePairs;
	}

	static {
		knownAtoms = new HashSet<>();
		signatures = new HashSet<>();
		playerSignature = new MoleculeSignature();
		
		MoleculeSignature sig = new MoleculeSignature();
		sig.setIdentifier("Hydrogen (H2)");
		sig.setAtom(Atom.HYDROGEN, 2);
		sig.setBondShape(BondShape.LINEAR);
		sig.setBondPolarity(BondPolarity.NONPOLAR);
		sig.setLonePairs(LonePairs.NONE);
		sig.setBondAngle(180.0);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Oxygen (02)");
		sig.setAtom(Atom.OXYGEN, 2);
		sig.setBondShape(BondShape.LINEAR);
		sig.setBondPolarity(BondPolarity.NONPOLAR);
		sig.setLonePairs(LonePairs.FOUR);
		sig.setBondAngle(180.0);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Dihydrogen Monoxide (H20)");
		sig.setAtom(Atom.OXYGEN, 1);
		sig.setAtom(Atom.HYDROGEN, 2);
		sig.setBondShape(BondShape.BENT);
		sig.setBondPolarity(BondPolarity.POLAR);
		sig.setLonePairs(LonePairs.TWO);
		sig.setBondAngle(104.5);
		sig.setIntermolecularForces(IntermolecularForces.HYDROGEN);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Hydrogen Peroxide (H2O2)");
		sig.setAtom(Atom.OXYGEN, 2);
		sig.setAtom(Atom.HYDROGEN, 2);
		sig.setBondShape(BondShape.BENT);
		sig.setBondPolarity(BondPolarity.POLAR);
		sig.setLonePairs(LonePairs.TWO);
		sig.setBondAngle(109.5);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Ammonia (NH3)");
		sig.setAtom(Atom.NITROGEN, 1);
		sig.setAtom(Atom.HYDROGEN, 3);
		sig.setBondShape(BondShape.TETRAHEDRAL);
		sig.setBondPolarity(BondPolarity.POLAR);
		sig.setLonePairs(LonePairs.NONE);
		sig.setBondAngle(107.5);
		sig.setIntermolecularForces(IntermolecularForces.HYDROGEN);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Carbon Dioxide (C02)");
		sig.setAtom(Atom.CARBON, 1);
		sig.setAtom(Atom.HYDROGEN, 2);
		sig.setBondShape(BondShape.LINEAR);
		sig.setBondPolarity(BondPolarity.NONPOLAR);
		sig.setLonePairs(LonePairs.FOUR);
		sig.setBondAngle(180);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Hydrocyanic Acid (HCN)");
		sig.setAtom(Atom.HYDROGEN, 1);
		sig.setAtom(Atom.CARBON, 1);
		sig.setAtom(Atom.NITROGEN, 1);
		sig.setBondShape(BondShape.LINEAR);
		sig.setBondPolarity(BondPolarity.POLAR);
		sig.setLonePairs(LonePairs.ONE);
		sig.setBondAngle(180);
		sig.setIntermolecularForces(IntermolecularForces.DIPOLE_DIPOLE);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Carbonic Acid (H2CO3)");
		sig.setAtom(Atom.CARBON, 1);
		sig.setAtom(Atom.HYDROGEN, 2);
		sig.setAtom(Atom.OXYGEN, 3);
		sig.setBondShape(BondShape.TRIGONAL_PLANAR);
		sig.setBondPolarity(BondPolarity.POLAR);
		sig.setLonePairs(LonePairs.SIX);
		sig.setBondAngle(120);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Nitric Acid (HNO3)");
		sig.setAtom(Atom.HYDROGEN, 1);
		sig.setAtom(Atom.NITROGEN, 1);
		sig.setAtom(Atom.OXYGEN, 3);
		sig.setBondShape(BondShape.TRIGONAL_PLANAR);
		sig.setBondPolarity(BondPolarity.POLAR);
		sig.setLonePairs(LonePairs.SEVEN);
		sig.setBondAngle(120);
		sig.setIntermolecularForces(IntermolecularForces.HYDROGEN);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Butane (C2H6)");
		sig.setAtom(Atom.CARBON, 2);
		sig.setAtom(Atom.HYDROGEN, 6);
		sig.setBondShape(BondShape.TETRAHEDRAL);
		sig.setBondPolarity(BondPolarity.NONPOLAR);
		sig.setLonePairs(LonePairs.NONE);
		sig.setBondAngle(109.5);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Methane (CH4)");
		sig.setAtom(Atom.CARBON, 1);
		sig.setAtom(Atom.HYDROGEN, 4);
		sig.setBondShape(BondShape.TETRAHEDRAL);
		sig.setBondPolarity(BondPolarity.NONPOLAR);
		sig.setLonePairs(LonePairs.NONE);
		sig.setBondAngle(109.5);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		signatures.add(sig);
		
		sig = new MoleculeSignature();
		sig.setIdentifier("Nitrogen (N2)");
		sig.setAtom(Atom.NITROGEN, 2);
		sig.setBondShape(BondShape.LINEAR);
		sig.setBondPolarity(BondPolarity.NONPOLAR);
		sig.setLonePairs(LonePairs.TWO);
		sig.setBondAngle(180);
		sig.setIntermolecularForces(IntermolecularForces.LONDON_DISPERSION);
		sig.isValid = true;
		signatures.add(sig);
	}
	
	public static void setPlayerSignature(MoleculeSignature sig) {
		playerSignature = sig;
	}
	
	public static MoleculeSignature getPlayerSignature() {
		return playerSignature;
	}
}
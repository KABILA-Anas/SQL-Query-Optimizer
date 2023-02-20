package model;

import java.util.Vector;

public class Table {
	private String name;
	private int nbLignes;
	Vector<String> columns;
	
	public Table(String name, int nbLignes, Vector<String> columns) {
		this.name = name;
		this.nbLignes = nbLignes;
		this.columns = columns;
	}
	
	public boolean chercherColumn(String name) { return false;}
	
	
	
	
}

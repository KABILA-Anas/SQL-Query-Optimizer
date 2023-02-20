package model;

public class Relation extends Node{
	private String table;

	public Relation(String table) {
		super("Relation");
		this.table = table;
	}

	@Override
	public int Estimate() {
		// TODO Auto-generated method stub
		return super.Estimate();
	}

	@Override
	public String toString() {
		return table;
	}
	
	

}

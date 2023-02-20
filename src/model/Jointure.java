package model;

public class Jointure extends Node{
	
	private String cond;
	
	public Jointure(Node leftChild, Node rightChild,String cond) {
		super("Jointure",leftChild,rightChild);
		this.cond = cond;
	}
	
	@Override
	public int Estimate() {
		return super.Estimate();
	}

	@Override
	public String toString() {
		return "⋈";
	}	

	
}

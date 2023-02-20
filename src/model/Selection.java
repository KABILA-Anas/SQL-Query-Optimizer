package model;

public class Selection extends Node {
	
	private String cond;
	
	public Selection(Node leftChild,String cond) {
		super("Selection",leftChild);
		this.cond = cond;
	}
	
	@Override
	public int Estimate() {
		return super.Estimate();
	}

	@Override
	public String toString() {
		return "σ (" + cond + ")";
	}
	
	
}	

package model;

public class Union extends Node {
	
	public Union(Node leftChild, Node rightChild) {
		super("Union",leftChild,rightChild);
	}
	@Override
	public int Estimate() {
		return super.Estimate();
	}
	@Override
	public String toString() {
		return "∪";
	}
	
	
}

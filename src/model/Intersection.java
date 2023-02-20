package model;

public class Intersection extends Node{
	
	public Intersection(Node leftChild, Node rightChild) {
		super("Intersection",leftChild,rightChild);
	}

	@Override
	public int Estimate() {
		return super.Estimate();
	}

	@Override
	public String toString() {
		return "∩";
	}
}

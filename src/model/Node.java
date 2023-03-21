package model;


import java.util.Objects;

public class Node {
	private String name;
	private String expression = "";
	private double cout;
	private Node leftChild;
	private Node rightChild;


	//Constructeur pour les relations (feuilles)
	public Node(String name, String expression) {
		this.name = name;
		this.expression = expression;
	}

	//Constructeur pour un operateur Unaire
	public Node(String name, String expression, Node leftChild) {
		this.name = name;
		this.expression = expression;
		this.leftChild = leftChild;
	}

	//Constructeur pour un operateur binaire ( Union - Intersection - Produit cartesien )
	public Node(String name, Node leftChild, Node rightChild) {
		super();
		this.name = name;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	//Constructeur pour un operateur binaire ( Jointure )
	public Node(String name, String expression, Node leftChild, Node rightChild) {
		this.name = name;
		this.expression = expression;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}


	public static Node copierNode(Node node){
		if (node == null) {
			return null;
		}
		Node copy = new Node(node.name,node.expression,copierNode(node.leftChild),copierNode(node.rightChild));
		return copy;
	}


	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public double getCout() {
		return cout;
	}

	public void setCout(double cout) {
		this.cout = cout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	@Override
	public String toString() {
		switch(name.toLowerCase()){
			case "jointure":
				return "⋈";
			case "cartesien":
				return "X";
			case "selection":
				return "σ (" + expression + ")";
			case "union":
				return "∪";
			case "intersection":
				return "∩";
			case "projection":
				return "π (" + expression + ")";
			case "relation":
				return expression;
		}
		return name;
	}



	private int height(Node node){
		if(node == null)
			return  0;
		return 1 + Math.max(height(node.leftChild), height(node.rightChild));
	}

	public int height(){
		return height(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Node node)) return false;
		return Objects.equals(expression, node.expression) && Objects.equals(leftChild, node.leftChild) && Objects.equals(rightChild, node.rightChild);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, expression, cout, leftChild, rightChild);
	}
}

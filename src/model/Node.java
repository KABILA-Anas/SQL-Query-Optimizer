package model;



public class Node {
	private String name;
	private String expression = "";
	private Node leftChild;
	private Node rightChild;

	final private int COUNT = 10;

	
	
	public Node(String name, Node leftChild, Node rightChild) {
		super();
		this.name = name;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	public Node(String name, String expression, Node leftChild) {
		this.name = name;
		this.expression = expression;
		this.leftChild = leftChild;
	}

	public Node(String name, String expression) {
		this.name = name;
		this.expression = expression;
	}
	public static Node copierNode(Node node){
		if (node == null) {
			return null;
		}
		Node copy = new Node(node.name,node.expression,copierNode(node.leftChild),copierNode(node.rightChild));
		return copy;
	}
	public Node(String name, String expression, Node leftChild, Node rightChild) {
		this.name = name;
		this.expression = expression;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}


	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
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
			case "relation":
				return expression;
		}
		return null;
	}



	private int height(Node node){
		if(node == null)
			return  0;
		return 1 + Math.max(height(node.leftChild), height(node.rightChild));
	}

	public int height(){
		return height(this);
	}








	public void print2DUtil(Node root, int space)
	{
	    // Base case
	    if (root == null)
	        return;

	    // Increase distance between levels
	    space += COUNT;

	    // Process right child first
	    print2DUtil(root.rightChild, space);

	    // Print current node after space
	    // count
	    System.out.println();
	    for (int i = COUNT; i < space; i++)
	    	System.out.print(" ");
	    System.out.println(root);

	    // Process left child
	    print2DUtil(root.leftChild, space);
	}
	

}

package model;



public abstract class Node {
	private String name;
	private Node leftChild;
	private Node rightChild;
    final private int COUNT = 10;

	
	
	public Node(String name, Node leftChild, Node rightChild) {
		super();
		this.name = name;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public Node(String name, Node leftChild) {
		super();
		this.name = name;
		this.leftChild = leftChild;
	}
	public Node(String name) {
		super();
		this.name = name;
	}

	public int Estimate() {
		return 0;
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

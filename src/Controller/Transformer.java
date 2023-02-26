package Controller;

import java.util.*;


import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Query;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;
import model.Node;

public class Transformer {

	Query Q;
	Map<Integer,Vector<Node>> trees = new HashMap<Integer,Vector<Node>>();
	public Transformer(Query Q) {
		this.Q = Q;
	}

	public Map<Integer, Vector<Node>> getTrees() {
		return trees;
	}



	public Node TransformQuery() throws SyntaxeException, SemantiqueException {
		//Query Q = Decomposer.SplitQuery(query);
		return Q.buidTree();
	}

	//******************Generate all variations ***************************//
	public void TransformerTree() throws SyntaxeException, SemantiqueException {

		Vector<Vector<String>> conditions = Q.getConditions();
		addTree(Q.buidTree());
		if(conditions.size() != 0) {
			Vector<String> conditions1 = conditions.get(0);

			//System.out.println(conditions1);

			Vector<Vector<String>> combinations = vectorCombinations(conditions1);
			//Node tree = Q.buidTree();
			for (Vector<String> v : combinations) {
				//System.out.println(v);
				Vector<Vector<String>> cdts = new Vector<Vector<String>>();
				cdts.add(v);
				Query query = new Query(Q.getColumns(), Q.getTables_alias(), Q.getTables(), cdts);
				Node T = query.buidTree();
				addTree(T);
			}

			CSG();

		}

		JC();

		/*for(Vector<String> v : combinations){
			System.out.println(v);
		}*/

	}


	private boolean searchTree(Node T){
		Vector<Node> nodes = trees.get(T.height());
		if(nodes == null)
			return false;
		for(Node N : nodes)
			if(compareTree(T,N))
				return true;
		return false;
	}

	private void addTree(Node T){

		if(!searchTree(T)){
			if(trees.get(T.height()) == null)
				trees.put(T.height(), new Vector<Node>());
			trees.get(T.height()).add(T);
		}

	}

	private void addTreeWithoutCompare(Node T){

			if(trees.get(T.height()) == null)
				trees.put(T.height(), new Vector<Node>());
			trees.get(T.height()).add(T);

	}


	private boolean compareTree(Node T1, Node T2){
		if(T1 == null && T2 == null)
			return true;
		if(T1 == null || T2 == null)
			return false;
		if(T1.height() != T2.height())
			return false;

		if(!T1.getExpression().equals(T2.getExpression()) || !T1.getName().equals(T2.getName()))
				return false;

		if(!compareTree(T1.getRightChild(), T2.getRightChild()) || !compareTree(T1.getLeftChild(), T2.getLeftChild()))
			return false;
		return true;
	}

	public void printTrees() {
		for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet())
			for (Node n : entry.getValue()) {
				n.print2DUtil(n, 0);
				System.out.println("-------------------------------------------------------------------------------------");
			}

	}


	//***************************** JC *****************************//
	public void JC(){
		Vector<Node> tempNodes = new Vector<Node>();
		for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet()) {
			for (Node n : entry.getValue()) {
				Vector<Node> nodes = new Vector<Node>();
				JC(n,0, nodes);
				for(Node temp : nodes)
					tempNodes.add(temp);
			}
		}

		for(Node n : tempNodes)
			addTree(n);
	}

	private void JC(Node root, int l, Vector<Node> nodes){
		if(root == null)
			return;
		while (true){
			int[] level = {l};
			Node newTree = Node.copierNode(root);
			Node binaryNode = getBianaryNode(newTree, level);
			if(binaryNode == null)
				break;
			swapChilds(binaryNode);
			l++;
			JC(newTree, l, nodes);
			nodes.add(newTree);
			/*newTree.print2DUtil(newTree, 0);
			System.out.println("------------------------------------------------------------------");*/
		}
	}

	private Node getBianaryNode(Node root, int[] level){
		Node R = null;

		if(root == null)
			return null;

		if(root.getRightChild() != null && root.getLeftChild() != null){
			if(level[0] == 0)
				return root;
			level[0]--;
		}

		R = getBianaryNode(root.getLeftChild(), level);
		if(R != null)
			return R;

		R = getBianaryNode(root.getRightChild(), level);
		if(R != null)
			return R;

		return null;
	}

	private void swapChilds(Node N){
		Node tmp = N.getLeftChild();
		N.setLeftChild(N.getRightChild());
		N.setRightChild(tmp);
	}

	//******************************** CSG ********************************//



	//** Variate one tree
	private void CSG(Node root , Vector<Node> nodes) throws SyntaxeException, SemantiqueException {
		if(root != null){
			int[] childType = {-1};
			Node newTree = Node.copierNode(root);
			Node firstSelectionParent = getFirstSelectionParent(newTree,childType);
			if(firstSelectionParent != null) {
				switch (childType[0]) {
					case -1:
						newTree = moveSelection(firstSelectionParent);
						break;
					case 0:
						firstSelectionParent.setLeftChild(moveSelection(firstSelectionParent.getLeftChild()));
						break;
					case 1:
						firstSelectionParent.setRightChild(moveSelection(firstSelectionParent.getRightChild()));
						break;
				}
				nodes.add(newTree);

				CSG(newTree,nodes);
			}

		}
	}

	//** Variate all trees
	public void CSG() throws SyntaxeException, SemantiqueException {
		Vector<Node> tempNodes = new Vector<Node>();
		for (Map.Entry<Integer, Vector<Node>> entry : trees.entrySet()) {
			for (Node n : entry.getValue()) {
				Vector<Node> nodes = new Vector<Node>();
				CSG(n , nodes);
				for(Node temp : nodes)
					tempNodes.add(temp);
			}
		}

		for(Node n : tempNodes)
			addTree(n);

	}

	private boolean checkSelection(Node N){
		if(N.getName().equals("Relation"))
			return true;
		if(N.getRightChild() != null)
			return false;
		return checkSelection(N.getLeftChild());
	}

	public Node getFirstSelectionParent(Node root , int[] childType){
		Node R = null;
		if(root == null)
			return null;
		if(root.getName().equals("Selection")) {
			if(checkSelection(root.getLeftChild())){
				return null;
			}
			return root;
		}
		R = getFirstSelectionParent(root.getLeftChild() , childType);
		if(R != null) {
			if(R.getName().equals("Selection")){
				childType[0] = 0;
				return root;
			}
			return R;
		}
		R = getFirstSelectionParent(root.getRightChild(),childType);
		if(R != null){
			if(R.getName().equals("Selection")) {
				childType[0] = 1;
				return root;
			}
			return R;
		}

		return null;
	}

	public Node moveSelection(Node root) throws SyntaxeException, SemantiqueException {
		int[] childType = {0};
		Node relationParent, newRoot;

		newRoot = root.getLeftChild();
		relationParent = getRelationParent(root.getLeftChild(), Q.selectionRelation(root.getExpression()), childType);

		if(childType[0] == 1){
			root.setLeftChild(relationParent.getRightChild());
			relationParent.setRightChild(root);
		} else {
			root.setLeftChild(relationParent.getLeftChild());
			relationParent.setLeftChild(root);
		}

		return newRoot;
	}

	public Node getRelationParent(Node root, String realation, int[] childType){
		Node R = null;

		if(root == null)
			return  null;

		if(root.getExpression().equals(realation))
			return root;

		R = getRelationParent(root.getRightChild(), realation, childType);
		if(R != null) {
			if (R.getExpression().equals(realation)){
				childType[0] = 1;
				return root;
			}
			return R;
		}

		R = getRelationParent(root.getLeftChild(), realation, childType);
		if(R != null) {
			if (R.getExpression().equals(realation)){
				childType[0] = 0;
				return root;
			}
			return R;
		}

		return null;
	}





















	private Vector<Vector<String>> vectorCombinations(Vector<String> vector){
		Vector<Vector<String>> result = new Vector<Vector<String>>();
		generateVectorCombinations(vector, new boolean[vector.size()], new Vector<String>(), 0, result);
		return result;
	}

	private void generateVectorCombinations(Vector<String> vector, boolean[] used, Vector<String> current, int index, Vector<Vector<String>> result){
		if (index == vector.size()) {
			result.add((Vector<String>) current.clone());
			//current = new Vector<String>();
		} else {
			for (int i = 0; i < vector.size(); i++) {
				if (!used[i]) {
					used[i] = true;
					//current.remove(index);
					if(current.size() == vector.size())
						current.set(index, vector.get(i));
					else
						current.add(index, vector.get(i));
					generateVectorCombinations(vector, used, current, index + 1, result);
					used[i] = false;
				}
			}
		}
	}











	/*public Query SplitQuery(String query) throws SyntaxeException {

		query = query.toUpperCase();
		//String sql = "SELECT column1, column2 FROM table1 WHERE column3 = 'value'";
		//Split the query to 3 parts

		String regex = "SELECT\\s+(\\w+(\\s+(as\\s+)?\\w+)?|\\*)(\\s*,\\s*(\\w+(\\s+(as\\s+)?\\w+)?|\\*))*\\s+FROM\\s+(\\w+)(\\s+(as\\s+)?\\w+)?(\\s*(,\\s*\\w+)(\\s+(as\\s+)?\\w+)?)*\\s*";
		query = query.toUpperCase();
		if(query.contains("WHERE"))
			regex += "\\s+WHERE\\s+(.*)";

		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(query);

		if (!matcher.matches()) {
			throw new SyntaxeException();
		}



		int selectIndex = query.indexOf("SELECT");
		int fromIndex = query.indexOf("FROM");
		int whereIndex = query.indexOf("WHERE");


		//Split selected columns
		String selectPart = query.substring(selectIndex+6, fromIndex).trim();
		StringTokenizer tokenizer = new StringTokenizer(selectPart, ",");

	    Map<String,String> columns = new HashMap<String,String>();
		while (tokenizer.hasMoreTokens()) {
		    String token = tokenizer.nextToken().trim();
		    String alias=null;
		    String column = token;
		    if(token.indexOf(" AS ") != -1) {
		    	String col_alias[] = token.split(" AS ");
		    	column = col_alias[0].trim();
		    	alias = col_alias[col_alias.length-1].trim();
		    }else if(token.indexOf(" ") != -1){
		    	String col_alias[] = token.split(" ");
		    	column = col_alias[0].trim();
		    	alias = col_alias[col_alias.length-1].trim();
		    }
		    columns.put(column,alias);
        }


		//Split from tables
		String fromPart;
		if(whereIndex != -1)
			fromPart = query.substring(fromIndex+4, whereIndex).trim();
		else
			fromPart = query.substring(fromIndex+4).trim();

		StringTokenizer tokenizer1 = new StringTokenizer(fromPart,",");
	    //System.out.println("  Tables : ");
		Vector<String> tables = new Vector<String>();
	    Map<String,String>  tables_alias = new HashMap<String,String>();

	    while (tokenizer1.hasMoreTokens()) {
		    String token = tokenizer1.nextToken().trim();
		    String alias=null;
		    String table = token;


		    if(token.indexOf(" AS ") != -1) {
		    	String col_alias[] = token.split(" AS ");
		    	table = col_alias[0].trim();
		    	alias = col_alias[col_alias.length-1].trim();
		    }else if(token.indexOf(" ") != -1){
		    	String col_alias[] = token.split(" ");
		    	table = col_alias[0].trim();
		    	alias = col_alias[col_alias.length-1].trim();
		    }
		    tables.add(table);
		    tables_alias.put(alias,table);
        }


		//**Split where conditions
		Vector<Vector<String>> or_oper = new Vector<Vector<String>>();
		if(whereIndex != -1) {
			String wherePart = query.substring(whereIndex + 5).trim();
			String[] ors = wherePart.split(" OR ");
			for (String token : ors) {

				Vector<String> and_oper = new Vector<String>();
				String[] ands = token.split(" AND ");
				//System.out.println("    AND : ");
				for (String token1 : ands) {
					if (!token1.contains("LIKE") && !token1.contains("BETWEEN"))
						token1 = token1.replaceAll(" ", "");
					and_oper.add(token1);

				}

				or_oper.add(and_oper);
			}
		}

		return new Query(columns,tables_alias,tables,or_oper);
	}*/

}

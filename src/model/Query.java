package model;

import java.security.KeyStore.Entry;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.exception.SemantiqueException;
import model.exception.SyntaxeException;

public class Query {
	private Map<String,String> columns;
	private Map<String,String> tables_alias;
	private Set<String> tables;
    Vector<Vector<String>> conditions;
    private Node root;
    private Set<String> visitedTables;

    
	public Query(Map<String, String> columns, Map<String, String> tables_alias, Set<String> tables,
			Vector<Vector<String>> conditions) {
		this.columns = columns;
		this.tables_alias = tables_alias;
		this.tables = tables;
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return "Query [columns=" + columns + ", tables_alias=" + tables_alias + ", tables=" + tables + ", conditions="
				+ conditions + "]";
	}
	
	
	//** return type of condition **//
	public String conditionType(String condition) throws SyntaxeException {
		
		Matcher matcher;
		Pattern pattern;
		
		
		
		//** One Column Selection **//
		pattern = Pattern.compile("(\\w+)(\\.\\w+)?\\s*(<|>|<=|>=|=)\\s*(([0-9]+)|('\\w+'))");
		matcher = pattern.matcher(condition);
		if(matcher.matches())
			return "OneColSelection";
		
		//** Like Selection **//
		pattern = Pattern.compile("(\\w+)(\\.\\w+)?\\s+LIKE\\s+('\\%?(\\w+\\%?)*')");
		matcher = pattern.matcher(condition);
		if(matcher.matches())
			return "Like";
		
		//** Between Selection **//
		pattern = Pattern.compile("(\\w+)(\\.\\w+)?\\s+BETWEEN\\s+(([0-9]+)|('\\w+'))\\s+AND\\s+(([0-9]+)|('\\w+'))");
		matcher = pattern.matcher(condition);
		if(matcher.matches())
			return "Between";
		
		//** Jointure **//
		pattern = Pattern.compile("(\\w+)(\\.\\w+)?\\s*=\\s*(\\w+)(\\.\\w+)?");
		matcher = pattern.matcher(condition);
		if(matcher.matches())
			return "Jointure";
		
		//** Two Columns Selection **//
		pattern = Pattern.compile("(\\w+)(\\.\\w+)?\\s*(<|>|<=|>=)\\s*(\\w+)(\\.\\w+)?");
		matcher = pattern.matcher(condition);
		if(matcher.matches())
			return "TwoColSelection";
		
		throw new SyntaxeException();
		
	}
	
	
	
	//**//**Creer un operateur (Node) de jointure
	private boolean createJointure(String C, boolean relationCheck) throws SemantiqueException {
		
		StringTokenizer tokenizer;
		String relation1, relation2;
		String[] relations = C.split("=");
		tokenizer = new StringTokenizer(relations[0], ".");
		relation1 = tokenizer.nextToken(); //** check if it's a column or a table
		tokenizer = new StringTokenizer(relations[1], ".");
		relation2 = tokenizer.nextToken();
		
		if(tables_alias.containsKey(relation1))
			relation1 = tables_alias.get(relation1);
		else if(!tables.contains(relation1)) { /// Il faut chercher dans le catalog
			throw new SemantiqueException();
		}
			
		if(tables_alias.containsKey(relation2))
			relation2 = tables_alias.get(relation2);
		else if(!tables.contains(relation2)) { /// Il faut chercher dans le catalog
			throw new SemantiqueException();
		}
		//System.out.println(relation1 + "---" + relation2);
		
		if(root == null || !relationCheck) {
			
			if(root == null)
				root = new Jointure(new Relation(relation1), new Relation(relation2), C);
			
			if(!relationCheck)
				root = new Cartesien(root, new Jointure(new Relation(relation1), new Relation(relation2), C));
			
			visitedTables.add(relation1);
			visitedTables.add(relation2);
			
			return true;
		}
		
		/*if(visitedTables.contains(relation1) && visitedTables.contains(relation2)) {
			System.out.println();
			break;
		}*/
		
		if(visitedTables.contains(relation1)) {
			root = new Jointure(root, new Relation(relation2), C);
			visitedTables.add(relation2);
			
			return true;
		}
		
		if(visitedTables.contains(relation2)) {
			root = new Jointure(root, new Relation(relation1), C);
			visitedTables.add(relation1);
			
			return true;
		}
		
		
		return false;
		
	}
	
	//**Creer un operateur (Node) de selection
	private boolean createSelection(String C, boolean relationCheck) throws SemantiqueException, SyntaxeException {
		StringTokenizer tokenizer;
		String[] operands = null;
		
		switch(conditionType(C)) {
			case "OneColSelection":
				operands = C.split("<=|>=|<|>|=");
				break;
			case "Like" :
				operands = C.split(" LIKE ");
				break;
		
		}
		
		tokenizer = new StringTokenizer(operands[0], ".");
		String relation = tokenizer.nextToken().trim();
		
		if(tables_alias.containsKey(relation))
			relation = tables_alias.get(relation);
		else if(!tables.contains(relation)) { /// Il faut chercher dans le catalog
			throw new SemantiqueException();
		}
		
		if(root == null || !relationCheck) {
			if(root == null)
				root = new Selection(new Relation(relation), C);
			if(!relationCheck)
				root = new Cartesien(root, new Selection(new Relation(relation), C));
			visitedTables.add(relation);
			return true;
		}
		
		if(visitedTables.contains(relation)) {
			root = new Selection(root, C);
			return true;
		}
		return false;
	}
	
	//**//**Creer un operateur (Node) de jointure
		private boolean createTwoColSelection(String C, boolean relationCheck) throws SemantiqueException {
			
			StringTokenizer tokenizer;
			String relation1, relation2;
			String[] relations = C.split("<=|>=|<|>");
			tokenizer = new StringTokenizer(relations[0], ".");
			relation1 = tokenizer.nextToken(); //** check if it's a column or a table
			tokenizer = new StringTokenizer(relations[1], ".");
			relation2 = tokenizer.nextToken();
			
			if(tables_alias.containsKey(relation1))
				relation1 = tables_alias.get(relation1);
			else if(!tables.contains(relation1)) { /// Il faut chercher dans le catalog
				throw new SemantiqueException();
			}
				
			if(tables_alias.containsKey(relation2))
				relation2 = tables_alias.get(relation2);
			else if(!tables.contains(relation2)) { /// Il faut chercher dans le catalog
				throw new SemantiqueException();
			}
			//System.out.println(relation1 + "---" + relation2);
			
			if(root == null || !relationCheck) {
				
				if(root == null)
					root = new Selection(new Cartesien(new Relation(relation1), new Relation(relation2)), C);
				
				if(!relationCheck)
					root = new Cartesien(root, new Selection(new Cartesien(new Relation(relation1), new Relation(relation2)), C));
				
				visitedTables.add(relation1);
				visitedTables.add(relation2);
				
				return true;
			}
			
			/*if(visitedTables.contains(relation1) && visitedTables.contains(relation2)) {
				System.out.println();
				break;
			}*/
			
			if(visitedTables.contains(relation1)) {
				
				root = new Selection(new Cartesien(root, new Relation(relation2)), C);
				visitedTables.add(relation2);
				
				return true;
			}
			
			if(visitedTables.contains(relation2)) {
				
				root = new Selection(new Cartesien(root, new Relation(relation1)), C);
				visitedTables.add(relation1);
				
				return true;
			}
			
			
			return false;
			
		}
	
	//** relationCheck ? Tester si les tables necessaires sont deja viste : creer directement le noeud
	public boolean createNode(String C, boolean relationCheck) throws SemantiqueException, SyntaxeException {
		
		switch(conditionType(C)) {
		
		case "Jointure":
			return createJointure(C, relationCheck);
			
		case "OneColSelection":
		case "Like":	
			return createSelection(C, relationCheck);
		
		case "TwoColSelection" :
			return createTwoColSelection(C, relationCheck);
		
		}
		return false;
	}
	
	
	public Node buidTree() throws SemantiqueException, SyntaxeException {
		
		String condition;
		Node tmpNode  = null;
		
		for(Vector<String> V : conditions) {
			List<String> nodesQueue = new LinkedList<String>();
			visitedTables = new HashSet<String>();
			root = null;
			
			for(String C : V) {				
				
				//** Node relations exist
				if(createNode(C, true)) {
					
					boolean moreCheck = true;
					//** Tester s'il y a des noeuds qui peuvent etre cree
					while(moreCheck && !nodesQueue.isEmpty()) {
						
						int size = nodesQueue.size();
						moreCheck = false;
						
						while(size != 0) {
							condition = nodesQueue.remove(0);
							if(!createNode(condition, true))
								nodesQueue.add(condition);
							else
								moreCheck = true;
							size--;
						}
						
					}
				}else {
					nodesQueue.add(C);
				}
				
			}
			
			while(!nodesQueue.isEmpty()) {
			    
				condition = nodesQueue.remove(0);
				createNode(condition, false);
				
				boolean moreCheck = true;
				while(moreCheck && !nodesQueue.isEmpty()) {
					
					int size = nodesQueue.size();
					moreCheck = false;
					
					while(size != 0) {
						condition = nodesQueue.remove(0);
						if(!createNode(condition, true))
							nodesQueue.add(condition);
						else
							moreCheck = true;
						size--;
					}
					
				}
			}
			
			//** Creer un produit cartesienne avec chaque table dans le from qui ne se trouve pas dans le where
			for(String t : tables) {
				if(!visitedTables.contains(t)) {
					root = new Cartesien(root,new Relation(t));
				}
			}	
			
			//root.print2DUtil(root, 0);
			
			//System.out.println("**************************************************************************");
			
			if(tmpNode == null)
				tmpNode = root;
			else
				tmpNode = new Union(tmpNode, root);
			
			
		}//end for pour les unions
		
		tmpNode.print2DUtil(tmpNode, 0);
		
		
		return tmpNode;
		
	}
	
	


}
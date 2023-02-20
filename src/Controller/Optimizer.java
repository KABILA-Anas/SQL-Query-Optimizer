package Controller;

import java.util.*;


import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Query;
import model.Table;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;
import model.Catalog;
import model.Node;

public class Optimizer {
	
	private Catalog database;
	private Vector<Query> queries;
	
	public Optimizer() {}
	
	public Query SplitQuery(String query) throws SyntaxeException {
		
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
		
		
		query = query.toUpperCase();
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
		    	alias = col_alias[1].trim();
		    }else if(token.indexOf(" ") != -1){
		    	String col_alias[] = token.split(" ");
		    	column = col_alias[0].trim();
		    	alias = col_alias[1].trim();
		    }
		    columns.put(column,alias);
        }
		
		
		//Split from tables
		String fromPart = query.substring(fromIndex+4, whereIndex).trim();
		StringTokenizer tokenizer1 = new StringTokenizer(fromPart,",");
	    //System.out.println("  Tables : ");
	    Set<String> tables = new HashSet<String>();
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
		String wherePart = query.substring(whereIndex+5).trim();
	    String[] ors = wherePart.split(" OR ");	
	    Vector<Vector<String>> or_oper = new Vector<Vector<String>>();
	    for (String token : ors) {
	    	
	    	Vector<String> and_oper = new Vector<String>();
		    String[] ands = token.split(" AND ");
		    //System.out.println("    AND : ");
		    for (String token1: ands) {
		    	if(!token1.contains("LIKE") && !token1.contains("BETWEEN"))
		    		token1 = token1.replaceAll(" ","");
		    	and_oper.add(token1);

		    } 
		    
		    or_oper.add(and_oper);
		}  

		return new Query(columns,tables_alias,tables,or_oper);
	}
	
	public Node TransformQuery(String query) throws SyntaxeException, SemantiqueException {
		Query Q = SplitQuery(query);
		return Q.buidTree();
	}
	
	public Node OptimizeQuery(String query) {return null;}
	
	

}

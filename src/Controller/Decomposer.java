package Controller;

import model.Query;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Decomposer {

    public static String getQueryPart(String query) {
        query = query.toUpperCase();
        int whereIndex = query.indexOf("WHERE");
        String wherePart = query.substring(0, whereIndex+6);
        return wherePart;
    }

    public static Query SplitQuery(String query) throws SyntaxeException {

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
    }


    public static MyPair<String, String> selectionSplit(String C) {
        StringTokenizer tokenizer;
        String[] operands = null;
        Vector<MyPair<String, String>> elements = new Vector<MyPair<String, String>>();

        try{
            switch(conditionType(C)) {
                case "OneColSelection":
                    operands = C.split("<=|>=|<|>|=");
                    break;
                case "Like" :
                    operands = C.split(" LIKE ");
                    break;

            }
        } catch (SyntaxeException e) {
            System.out.println("Condition Type in Estimator");
        }

        tokenizer = new StringTokenizer(operands[0], ".");
        String firstElement = tokenizer.nextToken().trim();
        if(tokenizer.hasMoreTokens()){
            return (new MyPair<String, String>(tokenizer.nextToken(), firstElement));
        } else {
            return (new MyPair<String, String>(tokenizer.nextToken(), null)); ///check the catalog
        }

    }

    public static Vector<MyPair<String, String>> joinSplit(String C) {
        StringTokenizer tokenizer;
        Vector<MyPair<String, String>> elements = new Vector<MyPair<String, String>>();
        String relation1, relation2;
        String[] relations = C.split("<=|>=|<|>|=");


        tokenizer = new StringTokenizer(relations[0], ".");
        relation1 = tokenizer.nextToken(); //** check if it's a column or a table

        if(tokenizer.hasMoreTokens()){
            elements.add(new MyPair<String, String>(tokenizer.nextToken(), relation1));
        } else {
            elements.add(new MyPair<String, String>(tokenizer.nextToken(), null)); ///check the catalog
        }


        tokenizer = new StringTokenizer(relations[1], ".");
        relation2 = tokenizer.nextToken();

        if(tokenizer.hasMoreTokens()){
            elements.add(new MyPair<String, String>(tokenizer.nextToken(), relation2));
        } else {
            elements.add(new MyPair<String, String>(tokenizer.nextToken(), null)); ///check the catalog
        }


        return elements;
    }


    public static String joinSwap(String C) {
        String expression = "";
        Vector<MyPair<String, String>> elements = new Vector<MyPair<String, String>>();
        String[] relations = C.split("<=|>=|<|>|=");

        expression += relations[1] + "=" + relations[0];

        return expression;
    }


    public static String conditionType(String condition) throws SyntaxeException {

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
        pattern = Pattern.compile("(\\w+)(\\.\\w+)?\\s*(<|>|<=|>=|=)\\s*(\\w+)(\\.\\w+)?");
        matcher = pattern.matcher(condition);
        if(matcher.matches())
            return "Jointure";

        throw new SyntaxeException();

    }


    public static class MyPair<A, B> {
        private A first;
        private B second;

        public MyPair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }

}

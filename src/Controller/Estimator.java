package Controller;

import model.Node;
import model.Query;
import model.service.Catalog;

import java.util.HashMap;
import java.util.Map;

public class Estimator {
    private Map<String, Integer> nbrLines = new HashMap<String, Integer>();
    private Map<String, Integer> FBM = new HashMap<String, Integer>();
    private Node tree;
    Query query;
    private final double TempsTrans = 0.1;
    private final double TempsPosDébut = 1;
    private final double TR = 0.8;

    public Estimator(Node tree, Query query) {
        this.tree = tree;
        this.query = query;

        for (String table : query.getTables()){
            nbrLines.put(table, Catalog.getStatsTable(table, 1));
            FBM.put(table, Catalog.getStatsTable(table, 4));
        }
        /*System.out.println(nbrLines);
        System.out.println(FBM);*/
    }


    public double estimate(){
        estimate(tree.getLeftChild());
        return 0;
    }

    private int estimate(Node N){
        int left, right = 0;

        if (N.getLeftChild() == null)
            return nbrLines.get(N.getExpression());

        left = estimate(N.getLeftChild());

        //** Binary operator
        if (N.getRightChild() != null){
            right = estimate(N.getRightChild());
            /*for (Decomposer.MyPair<String, String> pair : Decomposer.joinSplit(N.getExpression())){
                System.out.println("Column : " + pair.getFirst() + "  Table : " + pair.getSecond());
            }*/
        } else {
            N.setCout(HS(N.getExpression()));
        }
        //System.out.println(left + right);
        //for (Object o : Decomposer.selectionSplit())

        return left + right;
    }


    public double FS(String expression){

        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(expression);
        double cout = (nbrLines.get(pair.getSecond())/FBM.get(pair.getSecond()) * TempsTrans);
        //System.out.println("Column : " + pair.getFirst() + "  Table : " + pair.getSecond());
        System.out.println("Cout = " + cout);
        return cout;

    }

    public double IS(String expression){

        String table, column;
        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(expression);
        table = pair.getSecond();
        column = pair.getFirst();
        int orderMoyen = Catalog.getColumnDesc(table, column, 2);
        double hauteur, cout = 0;
        hauteur = (int) Math.round(Math.log(nbrLines.get(table)) / Math.log(orderMoyen) + 0.5);



        if(Catalog.isPrimaryKey(table, column)){

            cout = (hauteur + 1) * (TempsTrans + TempsPosDébut);

        } else {

            double sel = nbrLines.get(pair.getSecond()) / Catalog.getColumnCard(column, table);
            cout = Math.round(((hauteur -1) + sel + sel/orderMoyen) * (TempsTrans + TempsPosDébut) + 0.5);

        }
        System.out.println("Cout = " + cout);

        return cout;

    }

    public double HS(String expression){

        String table, column;
        double FB, TH, cout = 0;
        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(expression);
        table = pair.getSecond();
        column = pair.getFirst();
        int NL = nbrLines.get(table);

        FB = FBM.get(table) * TR;
        TH = NL / FB;
        cout = (NL / (TH * FB)) * (TempsTrans + TempsPosDébut);
        System.out.println("Cout = " + cout);
        return cout;

    }

}

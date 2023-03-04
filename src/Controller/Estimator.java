package Controller;

import model.Node;
import model.Query;
import model.service.Catalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
            //BIB(N,4000,1000000);
            BII(N,4000,1000000);
            /*for (Decomposer.MyPair<String, String> pair : Decomposer.joinSplit(N.getExpression())){
                System.out.println("Column : " + pair.getFirst() + "  Table : " + pair.getSecond());
            }*/
        } /*else {
            switch (N.getName()){
                case "FS" :
                    N.setCout(FS(N.getExpression()));
                    break;
                case "IS" :
                    N.setCout(IS(N.getExpression()));
                    break;
                case "HS" :
                    N.setCout(HS(N.getExpression()));
                    break;
            }
        }*/

        return left + right;
    }

    //Jointure Algorithmes
    public int BIB(Node node , int left , int right){

        Vector<Decomposer.MyPair<String,String>> pairs = Decomposer.joinSplit(node.getExpression());
        String tableL, columnL, tableR, columnR;
        tableL = query.getAliasTable(pairs.get(0).getSecond());
        columnL = pairs.get(0).getFirst();
        tableR = query.getAliasTable(pairs.get(1).getSecond());
        columnR = pairs.get(1).getFirst();

        double Bl = left/FBM.get(tableL);
        double Br = right/FBM.get(tableR);

        node.setCout(Bl*((TempsTrans+TempsPosDébut)+(Br*TempsTrans)+TempsPosDébut));
        //System.out.println(node.getCout());
        return (left * right)/2;
    }

    public int BII(Node node , int left , int right){
        Vector<Decomposer.MyPair<String,String>> pairs = Decomposer.joinSplit(node.getExpression());
        String tableL, columnL, tableR, columnR;
        tableL = query.getAliasTable(pairs.get(0).getSecond());
        columnL = pairs.get(0).getFirst();
        tableR = query.getAliasTable(pairs.get(1).getSecond());
        columnR = pairs.get(1).getFirst();

        double Bl = left/FBM.get(tableL);
        //
        int orderMoyen = Catalog.getColumnDesc(tableR, columnR, 2);
        double hauteur, Tsecondaire = 0;
        hauteur = (int) Math.round(Math.log(right) / Math.log(orderMoyen) + 0.5);

        if(Catalog.isPrimaryKey(tableR, columnR)){
            Tsecondaire = (hauteur + 1) * (TempsTrans + TempsPosDébut);
        } else {
            double sel = right / Catalog.getColumnCard(columnR, tableR);
            Tsecondaire = Math.round(((hauteur-1) + sel + sel/orderMoyen) * (TempsTrans + TempsPosDébut) + 0.5);
        }
        //
        node.setCout( Bl * (TempsTrans + TempsPosDébut) + left * Tsecondaire );
        //System.out.println(node.getCout());
        //System.out.println(Tsecondaire);

        return (left+right)/2;
    }

    public int JTF(Node node, int left, int right){

        Vector<Decomposer.MyPair<String,String>> pairs = Decomposer.joinSplit(node.getExpression());
        String tableL, columnL, tableR, columnR;
        tableL = query.getAliasTable(pairs.get(0).getSecond());
        columnL = pairs.get(0).getFirst();
        tableR = query.getAliasTable(pairs.get(1).getSecond());
        columnR = pairs.get(1).getFirst();

        double Bl,Br;
        Bl = left / FBM.get(tableL);
        Br = right / FBM.get(tableR);


        return 0;
    }

    //Selection Algorithmes

    public int FS(Node node , int nbrLigne){

        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(node.getExpression());
        double cout = (nbrLigne/FBM.get(pair.getSecond()) * TempsTrans);
        //System.out.println("Column : " + pair.getFirst() + "  Table : " + pair.getSecond());
        node.setCout(cout);
        System.out.println("Cout = " + cout);
        return nbrLigne/2;

    }

    public int IS(Node node , int nbrLigne){

        String table, column;
        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(node.getExpression());
        table = query.getAliasTable(pair.getSecond());
        column = pair.getFirst();
        int orderMoyen = Catalog.getColumnDesc(table, column, 2);
        double hauteur, cout = 0;
        hauteur = (int) Math.round(Math.log(nbrLigne) / Math.log(orderMoyen) + 0.5);



        if(Catalog.isPrimaryKey(table, column)){

            cout = (hauteur + 1) * (TempsTrans + TempsPosDébut);

        } else {

            double sel = nbrLigne / Catalog.getColumnCard(column, table);
            cout = Math.round(((hauteur -1) + sel + sel/orderMoyen) * (TempsTrans + TempsPosDébut) + 0.5);

        }

        node.setCout(cout);
        System.out.println("Cout = " + cout);

        return nbrLigne/2;

    }

    public int HS(Node node , int nbrLigne){

        String table, column;
        double FB, TH, cout = 0;
        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(node.getExpression());
        table = query.getAliasTable(pair.getSecond());
        column = pair.getFirst();
        int NL = nbrLigne;

        FB = FBM.get(table) * TR;
        TH = NL / FB;
        cout = (NL / (TH * FB)) * (TempsTrans + TempsPosDébut);
        node.setCout(cout);
        System.out.println("Cout = " + cout);
        return nbrLigne/2;

    }





}

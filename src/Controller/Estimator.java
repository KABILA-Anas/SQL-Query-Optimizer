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
    private final double M = 50;

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


    public double estimate(double[] pipeline_cout){
        estimate(tree.getLeftChild());
        //we should move this section later !!!
        double ct = calculCoutTot(tree, pipeline_cout);
        tree.setCout(1.1);
        //
        return ct;
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
            //PJ(N,1000000,4000);
            switch (N.getName()){
                case "BIB" :
                    return BIB(N,left,right);
                case "BII" :
                    return BII(N,left,right);
                case "JTF" :
                    return JTF(N,left,right);
                case "JH" :
                    return JH(N,left,right);
                case "PJ" :
                    return PJ(N,left,right);
            }

            /*for (Decomposer.MyPair<String, String> pair : Decomposer.joinSplit(N.getExpression())){
                System.out.println("Column : " + pair.getFirst() + "  Table : " + pair.getSecond());
            }*/
        } else {
            switch (N.getName()){
                case "FS" :
                    return FS(N,left);
                case "IS" :
                    return IS(N,left);
                case "HS" :
                    return HS(N,left);
            }
        }

        return (int) ((left + right)*0.7);
    }

    private double calculCoutTot(Node node , double[] pipeline_cout ){
        double left=0,right =0;
        if(node.getLeftChild() == null)
            return 0;

        left = calculCoutTot(node.getLeftChild(),pipeline_cout);
        if(node.getRightChild() != null ) {
            right = calculCoutTot(node.getRightChild(), pipeline_cout);
            if(pipeline_cout[0] < node.getCout())
                pipeline_cout[0] = node.getCout();
        }
        if(node.getLeftChild().getLeftChild() != null)
            return left + right + node.getCout() + 1.1;
        return left + right + node.getCout();

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

        node.setCout(Math.round(Bl*((TempsTrans+TempsPosDébut)+(Br*TempsTrans)+TempsPosDébut) + 0.5));
        //System.out.println(node.getCout());
        return (int) ((left + right)*0.7);
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
        hauteur = (double) Math.round(Math.log(right) / Math.log(orderMoyen) + 0.5);

        if(Catalog.isPrimaryKey(tableR, columnR)){
            Tsecondaire = (hauteur + 1) * (TempsTrans + TempsPosDébut);
        } else {
            double sel = right / Catalog.getColumnCard(columnR, tableR);
            Tsecondaire = Math.round(((hauteur-1) + sel + sel/orderMoyen) * (TempsTrans + TempsPosDébut) + 0.5);
        }
        //
        node.setCout( Math.round(Bl * (TempsTrans + TempsPosDébut) + (left * Tsecondaire) +0.5));
        //System.out.println(node.getCout());
        //System.out.println(Tsecondaire);

        return (int) ((left + right)*0.7);
    }

    public int JTF(Node node, int left, int right){

        Vector<Decomposer.MyPair<String,String>> pairs = Decomposer.joinSplit(node.getExpression());
        String tableL, columnL, tableR, columnR;
        tableL = query.getAliasTable(pairs.get(0).getSecond());
        columnL = pairs.get(0).getFirst();
        tableR = query.getAliasTable(pairs.get(1).getSecond());
        columnR = pairs.get(1).getFirst();

        double Bl,Br,TempEsL,TempEsR,cout;

        Bl = left / FBM.get(tableL);
        Br = right / FBM.get(tableR);
        TempEsL = 2*((Bl/M)*TempsPosDébut + Bl*TempsTrans) + Bl*(2*(Math.log(Bl/M)/Math.log(M-1)) - 1)*(TempsTrans+TempsPosDébut);
        TempEsR = 2*((Br/M)*TempsPosDébut + Br*TempsTrans) + Br*(2*(Math.log(Br/M)/Math.log(M-1)) - 1)*(TempsTrans+TempsPosDébut);

        cout = TempEsL+TempEsR+2*(Bl+Br)*(TempsTrans+TempsPosDébut);
        node.setCout(Math.round(cout+0.5));
        //System.out.println(cout);
        return (int) ((left + right)*0.7);
    }

    public int JH(Node node, int left, int right){

        Vector<Decomposer.MyPair<String,String>> pairs = Decomposer.joinSplit(node.getExpression());
        String tableL, tableR;
        tableL = query.getAliasTable(pairs.get(0).getSecond());
        tableR = query.getAliasTable(pairs.get(1).getSecond());

        double Bl,Br,Bal_l,Bal_r,cout;

        Bl = (double)left / FBM.get(tableL);
        Br = (double)right / FBM.get(tableR);
        Bal_l = Bl * TempsTrans;
        Bal_r = Br * TempsTrans;

        //cout = Bal_l+Bal_r+2*(Bl+Br)*(TempsTrans+TempsPosDébut);
        cout = Bal_l+Bal_r+2*(Bl+Br)*(TempsTrans+TempsPosDébut);
        node.setCout(Math.round(cout+0.5));
        //System.out.println(cout);
        return (int) ((left + right)*0.7);
    }

    public int PJ(Node node, int left, int right){

        Vector<Decomposer.MyPair<String,String>> pairs = Decomposer.joinSplit(node.getExpression());
        String tableL, tableR;
        tableL = query.getAliasTable(pairs.get(0).getSecond());
        tableR = query.getAliasTable(pairs.get(1).getSecond());

        double Bl,Br,Bal_l,Bal_r, cout;

        Bl = left / FBM.get(tableL);
        Br = right / FBM.get(tableR);
        Bal_l = Bl * TempsTrans;
        Bal_r = Br * TempsTrans;

        //cout = Bal_l+Bal_r+2*(Bl+Br)*(TempsTrans+TempsPosDébut);
        cout = Bal_l+Bal_r;
        node.setCout(Math.round(cout+0.5));
        //System.out.println(cout);
        return (int) ((left + right)*0.7);
    }
    //Selection Algorithmes

    public int FS(Node node , int nbrLigne){

        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(node.getExpression());
        double cout = (nbrLigne/FBM.get(query.getAliasTable(pair.getSecond())) * TempsTrans);
        //System.out.println("Column : " + pair.getFirst() + "  Table : " + pair.getSecond());
        node.setCout(cout);
        //System.out.println("Cout = " + cout);
        return (int) (nbrLigne*0.7);

    }

    public int IS(Node node , int nbrLigne){

        String table, column;
        Decomposer.MyPair<String, String> pair = Decomposer.selectionSplit(node.getExpression());
        table = query.getAliasTable(pair.getSecond());
        column = pair.getFirst();
        int orderMoyen = Catalog.getColumnDesc(table, column, 2);
        double hauteur, cout = 0;
        hauteur = (double) Math.round(Math.log(nbrLigne) / Math.log(orderMoyen) + 0.5);



        if(Catalog.isPrimaryKey(table, column)){

            cout = (hauteur + 1) * (TempsTrans + TempsPosDébut);

        } else {

            double sel = nbrLigne / Catalog.getColumnCard(column, table);
            cout = Math.round(((hauteur -1) + sel + sel/orderMoyen) * (TempsTrans + TempsPosDébut) + 0.5);

        }

        node.setCout(cout);
        //System.out.println("Cout = " + cout);

        return (int) (nbrLigne*0.7);
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
        cout = (NL / (TH)) * (TempsTrans + TempsPosDébut);
        node.setCout(cout);
        //System.out.println("Cout = " + cout);
        return (int) (nbrLigne*0.7);
    }

}

package model.service;

import Controller.Decomposer;
import model.Query;
import model.exception.SyntaxeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Catalog {

    static File statistique = new File("catalog/statistiques.txt");
    static File description = new File("catalog/descrption.txt");
    Query query;
    Map<String,TableStats> tablesStats = new HashMap<String, TableStats>();
    Map<String,TableDesc> tablesDesc = new HashMap<String, TableDesc>();

    public Catalog(Query query) {
        this.query = query;
        InsetTableStats();
        InsetTableDesc();
    }

    public int getStatsTable(String table, int index){
        for (Map.Entry<String, TableStats> entry : tablesStats.entrySet()) {
            if(entry.getKey().equals(table)){
                TableStats tableStats = entry.getValue();
                return switch (index) {
                    case 1 -> tableStats.N;
                    case 2 -> tableStats.FB;
                    case 3 -> tableStats.TL;
                    case 4 -> tableStats.FBM;
                    default -> -1;
                };
            }
        }
        return -1;
    }

    public int getColumnCard(String column , String table){
        for (Map.Entry<String , TableStats > entry : tablesStats.entrySet()) {
            if(entry.getKey().equals(table)){
                TableStats tableStats = entry.getValue();
                for (Map.Entry<String, Integer> entry1 : tableStats.col_card.entrySet()) {
                    if(entry1.getKey().equals(column)){
                        return entry1.getValue();
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public  boolean isPrimaryKey(String table, String pcol){

        for (Map.Entry<String, TableDesc> entry : tablesDesc.entrySet()) {
            if(entry.getKey().equals(table)){
                TableDesc tableDesc = entry.getValue();
                return tableDesc.cle_primaire.equals(pcol);
            }
        }
        return false;
    }

    public int getColumnDesc(String table, String column, int index){

        for (Map.Entry<String, TableDesc> entry : tablesDesc.entrySet()) {
            if(entry.getKey().equals(table)){
                TableDesc tableDesc = entry.getValue();
                for (Map.Entry<String, ColDesc> entry1 : tableDesc.col_desc.entrySet()) {
                    if(entry1.getKey().equals(column)){
                        ColDesc colDesc = entry1.getValue();
                        return switch (index) {
                            case 1 -> colDesc.TE;
                            case 2 -> colDesc.M;
                            default -> -1;
                        };
                    }
                }
                return -1;
            }
        }
        return -1;

    }


    class TableStats{
        Integer N;
        Integer FB;
        Integer TL;
        Integer FBM;
        Map<String , Integer> col_card;

        public TableStats(Integer N, Integer FB, Integer TL, Integer FBM) {
            this.N = N;
            this.FB = FB;
            this.TL = TL;
            this.FBM = FBM;
            this.col_card = new HashMap<String,Integer>();
        }

        public void addColCard(String col , Integer card){
            this.col_card.put(col,card);
        }

        @Override
        public String toString() {
            return "TableStats{" +
                    "N=" + N +
                    ", FB=" + FB +
                    ", TL=" + TL +
                    ", FBM=" + FBM +
                    ", col_card=" + col_card +
                    '}';
        }
    }

    class TableDesc{
        String cle_primaire;
        Map<String , ColDesc> col_desc;

        public TableDesc(String cle_primaire) {
            this.cle_primaire = cle_primaire;
            this.col_desc = new HashMap<String,ColDesc>();
        }

        public void addColDesc(String col , ColDesc desc){
            this.col_desc.put(col,desc);
        }

        @Override
        public String toString() {
            return "TableDesc{" +
                    "cle_primaire='" + cle_primaire + '\'' +
                    ", col_desc=" + col_desc +
                    '}';
        }
    }

    class ColDesc{
        Integer TE;
        Integer M;

        public ColDesc(Integer TE, Integer M) {
            this.TE = TE;
            this.M = M;
        }

        @Override
        public String toString() {
            return "ColDesc{" +
                    "TE=" + TE +
                    ", M=" + M +
                    '}';
        }
    }



    //Methodes
    public void InsetTableStats(){
        try {
            Scanner myReader = new Scanner(statistique);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if(line.equals("#"))
                    if(myReader.hasNextLine())
                        line = myReader.nextLine();
                    else break;

                String[] data = line.split(";");
                String table = data[0].toUpperCase();
                if(query.getTables().contains(table)) {
                    TableStats tableStats = new TableStats(Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4]));
                    tablesStats.put(table,tableStats);
                    String column_line;
                    do{
                        column_line = myReader.nextLine();
                        String[] column_data = column_line.split(";");
                        if (!column_data[0].equals("#")) {
                            tableStats.addColCard(column_data[0].toUpperCase(),Integer.parseInt(column_data[1]));
                        }
                    }while (!column_line.equals("#"));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public void InsetTableDesc(){
        try {
            Scanner myReader = new Scanner(description);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if(line.equals("#"))
                    if(myReader.hasNextLine())
                        line = myReader.nextLine();
                    else break;

                String[] data = line.split(";");
                String table = data[0].toUpperCase();
                if(query.getTables().contains(table)) {
                    TableDesc tableDesc = new TableDesc(data[1]);
                    tablesDesc.put(table,tableDesc);
                    String column_line;
                    do{
                        column_line = myReader.nextLine();
                        String[] column_data = column_line.split(";");
                        if (!column_data[0].equals("#")) {
                            ColDesc colDesc = new ColDesc(Integer.parseInt(column_data[1]),Integer.parseInt(column_data[2]));
                            tableDesc.addColDesc(column_data[0].toUpperCase(),colDesc);
                        }
                    }while (!column_line.equals("#"));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "query=" + query +
                ", tablesStats=" + tablesStats +
                ", tablesDesc=" + tablesDesc +
                '}';
    }


    /*public static void main(String[] args) throws SyntaxeException {
        Query Q = Decomposer.SplitQuery("SELECT * from Projet P, Travaux T, Employer E where P.Pid=T.Tid and T.Tid=E.Eid and P.budget>0 and E.Prenom = 'Anas'");
        Catalog catalog = new Catalog(Q);
        //System.out.println(catalog);
        System.out.println(catalog.getStatsTable1("PROJET",4));
    }*/
}

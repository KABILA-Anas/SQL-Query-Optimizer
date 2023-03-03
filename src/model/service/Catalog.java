package model.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Catalog {
    static File statistique = new File("catalog/statistiques.txt");
    static File description = new File("catalog/descrption.txt");


    public static int getStatsTable(String table, int index){
        try {
            Scanner myReader = new Scanner(statistique);
            while (myReader.hasNextLine()) {
                if(myReader.nextLine().equals("#")){
                    String line = myReader.nextLine();
                    String[] data = line.split(";");
                    if(data[0].toUpperCase().equals(table.toUpperCase())) {
                        myReader.close();
                        return Integer.parseInt(data[index]);
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return -1;
    }

    public static int getColumnCard(String column , String table){
        try {
            Scanner myReader = new Scanner(statistique);
            while (myReader.hasNextLine()) {
                if(myReader.nextLine().equals("#")){
                    String line = myReader.nextLine();
                    String[] data = line.split(";");
                    if(data[0].toUpperCase().equals(table.toUpperCase())) {
                        do{
                            String column_line = myReader.nextLine();
                            String[] column_data = column_line.split(";");
                            //System.out.println(column_data[0]);
                            if (column_data[0].toUpperCase().equals(column.toUpperCase())) {
                                myReader.close();
                                return Integer.parseInt(column_data[1]);
                            }
                        }while (!column.equals("#"));
                        myReader.close();
                        return -1;
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isPrimaryKey(String table, String pcol){
        try {
            Scanner myReader = new Scanner(description);
            while (myReader.hasNextLine()) {
                if(myReader.nextLine().equals("#")){
                    String line = myReader.nextLine();
                    String[] data = line.split(";");
                    if(data[0].toUpperCase().equals(table.toUpperCase())) {
                        if(pcol.toUpperCase().equals(data[1].toUpperCase())) {
                            myReader.close();
                            return true;
                        }
                        myReader.close();
                        return false;
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static int getColumnDesc(String table, String column, int index){
        try {
            Scanner myReader = new Scanner(description);
            while (myReader.hasNextLine()) {
                if(myReader.nextLine().equals("#")){
                    String line = myReader.nextLine();
                    String[] data = line.split(";");
                    if(data[0].toUpperCase().equals(table.toUpperCase())) {
                        do{
                            String column_line = myReader.nextLine();
                            String[] column_data = column_line.split(";");
                            //System.out.println(column_data[0]);
                            if (column_data[0].toUpperCase().equals(column.toUpperCase())) {
                                myReader.close();
                                return Integer.parseInt(column_data[index]);
                            }
                        }while (!column.equals("#"));
                        myReader.close();
                        return -1;
                    }
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(getColumnDesc("table2","col2",1));
    }
}

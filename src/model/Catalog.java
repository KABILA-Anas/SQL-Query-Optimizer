package model;

import java.util.Vector;

public class Catalog {
	
	private Vector<Table> tables;

	public Catalog(Vector<Table> tables) {
		this.tables = tables;
		
		Table Table1, Table2, Table3;
		
		Table1 = new Table("Table1", 10, null);
		//Table1
		tables.add(Table1);
		
		Table1 = new Table("Table2", 10, null);
		tables.add(Table1);
		
		Table1 = new Table("Table3", 10, null);
		tables.add(Table1);
	}
	
	public Table chercherColumn(String name) { return new Table(null,0,null);}

	public Vector<Table> getTables() {
		return tables;
	}
	
	

}

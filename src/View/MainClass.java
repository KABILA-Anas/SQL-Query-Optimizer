package View;

import Controller.Decomposer;
import Controller.Estimator;
import Controller.Optimizer;
import Controller.Transformer;
import model.Node;
import model.Query;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;
import model.service.Catalog;

import java.util.Vector;

public class MainClass {

	public static Catalog catalog;

	public static void main(String[] args) {

		//SELECT * from Projet P, Travaux T, Employer E where P.Pid=T.Tid and T.Tid=E.Eid and P.budget>0 and E.Prenom = 'Anas';
		UI.run();

	}
	
	
}

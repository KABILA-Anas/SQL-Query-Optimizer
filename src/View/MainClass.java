package View;

import Controller.Decomposer;
import Controller.Estimator;
import Controller.Transformer;
import model.Node;
import model.Query;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;

import java.util.Vector;

public class MainClass {

	public static void main(String[] args) {
		

		//Query Q = optimizer.SplitQuery("SELECT column1 C1, column2 FROM table t1,table t2 WHERE column = 'value' AND a = b OR C = D AND F = G");
		//Query Q = optimizer.SplitQuery("SELECT Enom, Titre From Employe E, Projet P, Travaux T, C Where P.budget > 250  and E.Eid = T.Eid and T.budget like '250'");
		//Query Q = optimizer.SplitQuery("SELECT Enom, Titre From Employe E, Projet P, Travaux T, C Where P.budget > 250 and P.Pid = T.Pid and E.Eid = T.Eid");
		//Query Q = optimizer.SplitQuery("SELECT Enom, Titre From Employe E, Projet P, Travaux T Where E.Eid = T.Eid and P.budget > 250 and P.Pid = T.Pid");
		//System.out.println(Q);
		
		/*String query = "SELECT Enom, Titre From Employe E, Projet P, Travaux T Where P.budget > 250 and P.Pid = T.Pid or E.Eid = P.Eid or T.col like 'test'";
		*/
		
		
		
		/*String query = "SELECT Enom, Titre From Employe E, Projet P, Travaux T Where P.budget >= 250 and P.Pid = T.Pid and E.Eid = T.Eid";
		Node N = null;
		try {
			N = optimizer.TransformQuery(query);
		} catch (SyntaxeException | SemantiqueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		//String query = "SELECT Enom, Titre From Employe E, Projet P, Travaux T Where   E.Eid = T.Eid and T.budget >= 250 and P.budget >= 250 and P.Pid = T.Pid ";
		String query = "SELECT * from A, B where A.col1>0 and A.col1=B.col1";
		try {
			/*transformer.TransformerTree(transformer.SplitQuery(query));
			transformer.printTrees();*/

			Query Q = Decomposer.SplitQuery(query);
			Transformer transformer = new Transformer(Q);
			Node T = transformer.TransformQuery();
			//T.print2DUtil(T, 0);
			Estimator estimator = new Estimator(T, Q);
			estimator.estimate();
			//transformer.TransformerTree();

			System.out.println("\n-------------------------------\n");

		} catch (SyntaxeException | SemantiqueException e) {
			e.printStackTrace();
		}

		//Afficheur P = new Afficheur(N);
		//P.printTree();



		//UI.run();

	}
	
	
}

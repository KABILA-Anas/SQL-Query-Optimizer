package View;

import Controller.Optimizer;
import model.Node;
import model.exception.SemantiqueException;
import model.exception.SyntaxeException;

public class MainClass {

	public static void main(String[] args) {
		
		Optimizer optimizer = new Optimizer();
		//Query Q = optimizer.SplitQuery("SELECT column1 C1, column2 FROM table t1,table t2 WHERE column = 'value' AND a = b OR C = D AND F = G");
		//Query Q = optimizer.SplitQuery("SELECT Enom, Titre From Employe E, Projet P, Travaux T, C Where P.budget > 250  and E.Eid = T.Eid and T.budget like '250'");
		//Query Q = optimizer.SplitQuery("SELECT Enom, Titre From Employe E, Projet P, Travaux T, C Where P.budget > 250 and P.Pid = T.Pid and E.Eid = T.Eid");
		//Query Q = optimizer.SplitQuery("SELECT Enom, Titre From Employe E, Projet P, Travaux T Where E.Eid = T.Eid and P.budget > 250 and P.Pid = T.Pid");
		//System.out.println(Q);
		
		/*String query = "SELECT Enom, Titre From Employe E, Projet P, Travaux T Where P.budget > 250 and P.Pid = T.Pid or E.Eid = P.Eid or T.col like 'test'";
		*/
		
		
		
		/*String query = "SELECT Enom, Titre From Employe E, Projet P, Travaux T Where P.budget >= 250  and E.Eid = T.Eid and P.Pid = T.Pid";
		Node N = null;
		try {
			N = optimizer.TransformQuery(query);
		} catch (SyntaxeException | SemantiqueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Afficheur P = new Afficheur(N);
		//P.printTree();
		
		UI.run();

	}
	
	
}

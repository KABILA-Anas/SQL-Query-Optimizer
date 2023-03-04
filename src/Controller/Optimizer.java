package Controller;

import model.Node;
import model.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Optimizer {

    private Map<Node, Vector<Node>> ptrees = new HashMap<Node,Vector<Node>>();
    private Query query;
    public Optimizer(Map<Node, Vector<Node>> ptrees ,Query query) {
        this.ptrees = ptrees;
        this.query  = query;
    }

    public void estimatePtrees(){
        for (Map.Entry<Node, Vector<Node>> entry : ptrees.entrySet()) {
            for (Node n : entry.getValue()) {
                //Vector<Node> nodes = new Vector<Node>();
                new Estimator(n,query).estimate();
            }
        }
    }

}

package Controller;

import model.Node;
import model.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Optimizer {

    private Map<Node, Vector<Node>> ptrees = new HashMap<Node,Vector<Node>>();
    private static Map<Node, Decomposer.MyPair<Double, Double>> cout = new HashMap<Node, Decomposer.MyPair<Double, Double>>();
    private Decomposer.MyPair<Node, Node> optimalTree;
    private Query query;
    public Optimizer(Map<Node, Vector<Node>> ptrees ,Query query) {
        this.ptrees = ptrees;
        this.query  = query;
    }

    public Decomposer.MyPair<Node, Node> getOptimalTree() {
        estimatePtrees();
        return optimalTree;
    }

    public Map<Node, Decomposer.MyPair<Double, Double>> getCout() {
        return cout;
    }

    public static double getCoutTotale(Node N){
        return cout.get(N).getFirst();
    }

    public static double getCoutPipeline(Node N){
        return cout.get(N).getSecond();
    }

    public void estimatePtrees(){
        for (Map.Entry<Node, Vector<Node>> entry : ptrees.entrySet()) {
            for (Node n : entry.getValue()) {
                //Vector<Node> nodes = new Vector<Node>();
                double[] pipeline_cout = {0};
                Estimator estimator = new Estimator(n,query);
                double totalCout = estimator.estimate(pipeline_cout);
                cout.put(n, new Decomposer.MyPair<Double, Double>(totalCout, pipeline_cout[0]));

                if(optimalTree == null)
                    optimalTree = new Decomposer.MyPair<>(entry.getKey(), n);
                else if (getCoutTotale(optimalTree.getSecond()) > getCoutTotale(n))
                    optimalTree = new Decomposer.MyPair<>(entry.getKey(), n);
            }
        }
    }


    /*public void getOptimalTree() {



    }*/

}

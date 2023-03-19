package Controller;

import model.Node;
import model.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Optimizer {

    private Map<Node, Vector<Node>> ptrees = new HashMap<Node,Vector<Node>>();
    private static Map<Node, Decomposer.MyPair<Double, Double>> cout = new HashMap<Node, Decomposer.MyPair<Double, Double>>();
    private Decomposer.MyPair<Node, Node> optimalTree[] = new Decomposer.MyPair[2];
    private Query query;
    public Optimizer(Map<Node, Vector<Node>> ptrees ,Query query) {
        this.ptrees = ptrees;
        this.query  = query;
    }

    public Decomposer.MyPair<Node, Node>[] getOptimalTree() {
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

                if(optimalTree[0] == null) {
                    optimalTree[0] = new Decomposer.MyPair<>(entry.getKey(), n);
                }
                else if (getCoutPipeline(optimalTree[0].getSecond()) > getCoutPipeline(n)) {
                    optimalTree[0] = new Decomposer.MyPair<>(entry.getKey(), n);
                }

                if(optimalTree[1] == null) {
                    optimalTree[1] = new Decomposer.MyPair<>(entry.getKey(), n);
                }
                else if (getCoutTotale(optimalTree[1].getSecond()) > getCoutTotale(n)) {
                    optimalTree[1] = new Decomposer.MyPair<>(entry.getKey(), n);
                }
            }
        }
    }


    /*public void getOptimalTree() {



    }*/

}
